package com.github.rmannibucau.blog.dao.util;

import com.github.rmannibucau.blog.dao.Repository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.cdi.CdiRepositoryBean;
import org.springframework.data.repository.cdi.CdiRepositoryExtensionSupport;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.UnsatisfiedResolutionException;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.PassivationCapable;
import javax.enterprise.inject.spi.ProcessBean;
import javax.enterprise.util.AnnotationLiteral;
import javax.persistence.EntityManager;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SpringDataCustomExtension extends CdiRepositoryExtensionSupport {
    private final Map<Set<Annotation>, Bean<EntityManager>> entityManagers = new HashMap<>();

    <X> void processBean(final @Observes ProcessBean<X> processBean) {
        final Bean<X> bean = processBean.getBean();
        for (final Type type : bean.getTypes()) {
            if (type instanceof Class<?> && EntityManager.class.isAssignableFrom((Class<?>) type)) {
                entityManagers.put(new HashSet<>(bean.getQualifiers()), (Bean<EntityManager>) bean);
            }
        }
    }

    void afterBeanDiscovery(final @Observes AfterBeanDiscovery afterBeanDiscovery, final BeanManager beanManager) {
        for (Map.Entry<Class<?>, Set<Annotation>> entry : getRepositoryTypes()) {
            final Class<?> repositoryType = entry.getKey();
            final Set<Annotation> qualifiers = entry.getValue();
            final Bean<?> repositoryBean = createRepositoryBean(repositoryType, qualifiers, beanManager);
            afterBeanDiscovery.addBean(repositoryBean);
        }
    }

    private <T> Bean<T> createRepositoryBean(final Class<T> repositoryType, final Set<Annotation> qualifiers,
                                             final BeanManager beanManager) {
        final Bean<EntityManager> entityManagerBean = entityManagers.get(qualifiers);
        if (entityManagerBean == null) {
            throw new UnsatisfiedResolutionException(String.format("Unable to resolve a bean for '%s' with qualifiers %s.",
                    EntityManager.class.getName(), qualifiers));
        }
        return new JpaPassivationCapableRepositoryBean<>(beanManager, entityManagerBean, qualifiers, repositoryType);
    }

    private static class JpaPassivationCapableRepositoryBean<T> extends CdiRepositoryBean<T> implements PassivationCapable {
        private final Bean<EntityManager> entityManagerBean;
        private final String id;

        private JpaPassivationCapableRepositoryBean(final BeanManager bm, final Bean<EntityManager> em,
                                            final Set<Annotation> qualifiers, final Class<T> repositoryType) {
            super(qualifiers, repositoryType, bm);
            entityManagerBean = em;
            id = "SpringData[" + repositoryType.getName() + "]"; // simple but enough for this app

            qualifiers.clear();
            qualifiers.add(AnyAnnotationLiteral.INSTANCE);
            qualifiers.add(RepositoryAnnotationLiteral.INSTANCE);
        }

        @Override
        public T create(CreationalContext<T> creationalContext, Class<T> repositoryType) {
            return new JpaRepositoryFactory(getDependencyInstance(entityManagerBean, EntityManager.class)).getRepository(repositoryType);
        }

        @Override
        public Class<? extends Annotation> getScope() {
            return ApplicationScoped.class;
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public String getId() {
            return id;
        }
    }

    private static class RepositoryAnnotationLiteral extends AnnotationLiteral<Repository> implements Repository {
        private static final long serialVersionUID = 511359421048623933L;
        private static final RepositoryAnnotationLiteral INSTANCE = new RepositoryAnnotationLiteral();
    }

    private static class AnyAnnotationLiteral extends AnnotationLiteral<Any> implements Any {
        private static final long serialVersionUID = 7261821376671361463L;
        private static final AnyAnnotationLiteral INSTANCE = new AnyAnnotationLiteral();
    }
}
