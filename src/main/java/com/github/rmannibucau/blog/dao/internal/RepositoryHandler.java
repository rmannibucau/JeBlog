package com.github.rmannibucau.blog.dao.internal;

import com.github.rmannibucau.blog.dao.api.JpaRepository;
import com.github.rmannibucau.blog.dao.api.Page;
import com.github.rmannibucau.blog.dao.api.PageRequest;
import com.github.rmannibucau.blog.dao.api.Param;
import com.github.rmannibucau.blog.dao.api.Query;
import com.github.rmannibucau.blog.dao.api.Repository;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
@ApplicationScoped
public class RepositoryHandler implements InvocationHandler, Serializable {
    private static final String FLUSH_SUFFIX = "AndFlush";

    private final ConcurrentMap<Method, Metadata> metadatas = new ConcurrentHashMap<>();

    @PersistenceContext(name = "je-blog")
    private EntityManager em;

    @Inject
    private DaoTransactionResolver tx;

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass()) && "toString".equals(method.getName())) {
            return toString();
        }

        Metadata metadata = metadatas.get(method);
        if (metadata == null) {
            metadata = initMetaData(proxy, method);
            metadatas.putIfAbsent(method, metadata);
        }

        if (metadata.txRequired) {
            return tx.run(new DoInvoke(method, args, metadata));
        }
        return doInvoke(method, args, metadata);
    }

    @PreDestroy
    public void cleanUp() {
        metadatas.clear();
    }

    private Metadata initMetaData(final Object proxy, final Method method) {
        final boolean providedMethod = JpaRepository.class.equals(method.getDeclaringClass());
        final Query annotation = method.getAnnotation(Query.class);

        final Metadata metadata = new Metadata();
        metadata.domainClass = findGenericArg(proxy, 0);
        metadata.txRequired = annotation == null || annotation.needTransaction();

        if (!providedMethod) {
            if (annotation == null || annotation.countQuery().isEmpty()) {
                metadata.countQueryName = queryName(metadata.domainClass.getSimpleName(), method.getName().replace("find", "count"));
            } else {
                metadata.countQueryName = queryName(metadata.domainClass.getSimpleName(), annotation.countQuery());
            }

            metadata.queryName = queryName(metadata.domainClass.getSimpleName(), method.getName());

            try { // check the count query exists
                em.createNamedQuery(metadata.countQueryName, Long.class);
            } catch (final Exception e) {
                // default query, matches only findAll
                metadata.countQuery  = "select count(e) from " + metadata.domainClass.getSimpleName() + " e";
            }

            // init param names
            final Class<?>[] parameterTypes = method.getParameterTypes();
            final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            for (int i = 0; i < parameterTypes.length; i++) {
                if (!PageRequest.class.isAssignableFrom(parameterTypes[i])) {
                    metadata.parameterNames.put(i, findParamName(parameterAnnotations[i]));
                }
            }
        }

        return metadata;
    }

    private Object doInvoke(final Method method, final Object[] args, final Metadata metadata) {
        Object result = null;

        final String name = method.getName();
        if (JpaRepository.class.equals(method.getDeclaringClass())) { // map primitives of the EntityManager
            if (name.equals("findById") && args.length == 1) {
                result = em.find(metadata.domainClass, args[0]);
            } else if (name.startsWith("save") && args.length == 1) {
                if (getIdentifier(args[0]) == null) {
                    em.persist(args[0]);
                } else {
                    em.merge(args[0]);
                }
            } else if (name.equals("deleteById") && args.length == 1) {
                em.remove(em.find(metadata.domainClass, args[0]));
            } else if (name.equals("delete") && args.length == 1) {
                em.remove(args[0]);
            } else if ("hasId".equals(name) && args.length == 1) {
                return getIdentifier(args[0]) != null;
            } else {
                throw new UnsupportedOperationException(name + " not yet implemented");
            }
        } else { // convention over NamedQuery
            final javax.persistence.Query query = em.createNamedQuery(metadata.queryName);
            final PageRequest request = setParameters(metadata, args, query, false);

            final Class<?> returnType = method.getReturnType();
            if (Collection.class.isAssignableFrom(returnType)) {
                result = query.getResultList();
            } else if (Page.class.isAssignableFrom(returnType)) {
                if (request == null) {
                    throw new IllegalArgumentException("Missing PageRequest as parameter - to get a Page you need to fill a PageRequest");
                }
                result = new Page<Object>(query.getResultList(), request, count(metadata, args));
            } else {
                try {
                    result = query.getSingleResult();
                } catch (final Exception e) {
                    result = null;
                }
            }
        }

        if (method.getName().endsWith(FLUSH_SUFFIX)) {
            em.flush();
        }

        return result;
    }

    private Object getIdentifier(final Object arg) {
        return em.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(arg);
    }

    private long count(final Metadata metadata, final Object[] args) {
        final TypedQuery<Long> typedQuery;
        if (metadata.countQueryName != null) {
            typedQuery = em.createNamedQuery(metadata.countQueryName, Long.class);
        } else {
            typedQuery = em.createQuery(metadata.countQuery, Long.class);
        }

        setParameters(metadata, args, typedQuery, true);

        return typedQuery.getSingleResult();
    }

    private static PageRequest setParameters(final Metadata metadata, final Object[] args, final javax.persistence.Query query, final boolean skipRequest) {
        if (args == null) {
            return null;
        }

        PageRequest request = null;

        for (int i = 0; i < args.length; i++) {
            if (PageRequest.class.isInstance(args[i])) {
                if (!skipRequest) {
                    request = PageRequest.class.cast(args[i]);
                    query.setFirstResult(request.getOffset());
                    query.setMaxResults(request.getPageSize());
                }
            } else {
                query.setParameter(metadata.parameterNames.get(i), args[i]);
            }
        }

        return request;
    }

    private static String queryName(final String simpleName, final String methodName) {
        final String name = simpleName + "." + methodName;
        if (name.endsWith(FLUSH_SUFFIX)) {
            return name.substring(0, name.length() - FLUSH_SUFFIX.length());
        }
        return name;
    }

    private static String findParamName(final Annotation[] annotations) {
        for (final Annotation a : annotations) {
            if (Param.class.equals(a.annotationType())) {
                return Param.class.cast(a).value();
            }
        }
        throw new IllegalStateException("Can't find @Param");
    }

    private static Class<?> findGenericArg(final Object o, final int idx) {
        if (o == null) {
            throw new IllegalStateException("Can't find entity type from a null proxy");
        }

        for (final Type type : o.getClass().getGenericInterfaces()) {
            if (!Class.class.isInstance(type)) {
                continue;
            }

            final Class<?> clazz = Class.class.cast(type);
            if (JpaRepository.class.isAssignableFrom(clazz)) {
                for (final Type type2 : clazz.getGenericInterfaces()) {
                    if (!ParameterizedType.class.isInstance(type2)) {
                        continue;
                    }

                    final ParameterizedType clazz2 = ParameterizedType.class.cast(type2);
                    if (Class.class.isInstance(clazz2.getRawType())
                            && JpaRepository.class.isAssignableFrom(Class.class.cast(clazz2.getRawType()))) {
                        return Class.class.cast(clazz2.getActualTypeArguments()[idx]);
                    }
                }
            }
        }

        throw new IllegalStateException(o + " is not an instance of Repository");
    }

    private class DoInvoke implements Callable<Object> {
        private final Method method;
        private final Object[] args;
        private final Metadata metadata;

        public DoInvoke(final Method method, final Object[] args, final Metadata metadata) {
            this.method = method;
            this.args = args;
            this.metadata = metadata;
        }

        @Override
        public Object call() throws Exception {
            return doInvoke(method, args, metadata);
        }
    }

    private static class Metadata {
        public Class<?> domainClass = null;
        public boolean txRequired = true;
        public String queryName = null;
        public String countQueryName = null;
        public String countQuery = null;
        public Map<Integer, String> parameterNames = new HashMap<>();
    }
}
