package com.github.rmannibucau.blog.dao.internal;

import com.github.rmannibucau.blog.dao.api.JpaRepository;
import com.github.rmannibucau.blog.dao.api.Page;
import com.github.rmannibucau.blog.dao.api.PageRequest;
import com.github.rmannibucau.blog.dao.api.Param;
import com.github.rmannibucau.blog.dao.api.Query;
import com.github.rmannibucau.blog.dao.api.Repository;

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
import java.util.concurrent.Callable;

@Repository
@ApplicationScoped
public class RepositoryHandler implements InvocationHandler, Serializable {
    private static final String FLUSH_SUFFIX = "AndFlush";

    @PersistenceContext(name = "je-blog")
    private EntityManager em;

    @Inject
    private DaoTransactionResolver tx;

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        if (isTransactional(method)) {
            return tx.run(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    return doInvoke(proxy, method, args);
                }
            });
        }
        return doInvoke(proxy, method, args);
    }

    // TODO: cache metadata?
    private Object doInvoke(final Object proxy, final Method method, final Object[] args) {
        Object result = null;

        final String name = method.getName();
        if (JpaRepository.class.equals(method.getDeclaringClass())) { // map primitives of the EntityManager
            if (name.equals("findById") && args.length == 1) {
                result = em.find(findGenericArg(proxy, 0), args[0]);
            } else if (name.startsWith("save") && args.length == 1) {
                final Object id = em.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(args[0]);
                if (id == null) {
                    em.persist(args[0]);
                } else {
                    em.merge(args[0]);
                }
            } else if (name.equals("deleteById") && args.length == 1) {
                em.remove(em.find(findGenericArg(proxy, 0), args[0]));
            } else if (name.equals("delete") && args.length == 1) {
                em.remove(args[0]);
            } else if ("hasId".equals(name) && args.length == 1) {
                return em.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(args[0]) != null;
            } else {
                throw new UnsupportedOperationException(name + " not yet implemented");
            }
        } else { // convention over NamedQuery
            final Class<?> entityClass = findGenericArg(proxy, 0);
            final javax.persistence.Query query = em.createNamedQuery(queryName(entityClass.getSimpleName(), name));
            final PageRequest request = setParameters(method.getParameterAnnotations(), args, query, false);

            final Class<?> returnType = method.getReturnType();
            if (Collection.class.isAssignableFrom(returnType)) {
                result = query.getResultList();
            } else if (Page.class.isAssignableFrom(returnType)) {
                if (request == null) {
                    throw new IllegalArgumentException("Missing PageRequest as parameter - to get a Page you need to fill a PageRequest");
                }
                result = new Page<Object>(query.getResultList(), request, count(entityClass.getSimpleName(), method, args));
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

    private static PageRequest setParameters(final Annotation[][] methodAnnotations, final Object[] args, final javax.persistence.Query query, final boolean skipRequest) {
        PageRequest request = null;

        for (int i = 0; i < args.length; i++) {
            if (PageRequest.class.isInstance(args[i])) {
                if (!skipRequest) {
                    request = PageRequest.class.cast(args[i]);
                    query.setFirstResult(request.getOffset());
                    query.setMaxResults(request.getPageSize());
                }
            } else {
                query.setParameter(findParamName(methodAnnotations[i]), args[i]);
            }
        }

        return request;
    }

    private long count(final String domain, final Method method, final Object[] args) {
        final Query annotation = method.getAnnotation(Query.class);
        final String query;
        if (annotation == null || annotation.countQuery().isEmpty()) {
            query = "count";
        } else {
            query = annotation.countQuery();
        }

        TypedQuery<Long> typedQuery;
        try {
            typedQuery = em.createNamedQuery(queryName(domain, query), Long.class);
        } catch (final Exception e) {
            typedQuery = em.createQuery("select count(e) from " + domain + " e", Long.class);
        }

        setParameters(method.getParameterAnnotations(), args, typedQuery, true);

        return typedQuery.getSingleResult();
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

    private static boolean isTransactional(final Method method) {
        final Query query = method.getAnnotation(Query.class);
        return query == null || query.needTransaction();
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
}
