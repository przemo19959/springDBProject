package pl.dabrowski.GameShop.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.Type;

//@Component
//public class SpringDataRESTConfig implements RepositoryRestConfigurer {
//
//    @Autowired
//    private EntityManager entityManager;
//
//    @Override
//    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
//        config.getCorsRegistry()//
//                .addMapping("/**")//
//                .allowedOrigins("http://127.0.0.1:5500")//
//                .allowedMethods("*");
//
//        config.exposeIdsFor(entityManager.getMetamodel().getEntities().stream()
//                .map(Type::getJavaType)
//                .toArray(Class[]::new));
//
//
//    }
//}
