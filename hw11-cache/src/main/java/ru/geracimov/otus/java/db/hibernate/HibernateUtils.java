package ru.geracimov.otus.java.db.hibernate;

import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.reflections.Reflections;

import javax.persistence.Entity;
import java.util.Set;

@UtilityClass
public final class HibernateUtils {

  public static SessionFactory buildSessionFactory(String configResourceFileName, String entityScanPackage) {
    Configuration configuration = new Configuration().configure(configResourceFileName);
    MetadataSources metadataSources = new MetadataSources(createServiceRegistry(configuration));
    getEntityClasses(entityScanPackage).forEach(metadataSources::addAnnotatedClass);
    Metadata metadata = metadataSources.getMetadataBuilder().build();
    return metadata.getSessionFactoryBuilder().build();
  }

  private static Set<Class<?>> getEntityClasses(String entityScanPackage) {
    Reflections reflections = new Reflections(entityScanPackage);
    return reflections.getTypesAnnotatedWith(Entity.class);
  }

  private static StandardServiceRegistry createServiceRegistry(Configuration configuration) {
    return new StandardServiceRegistryBuilder()
            .applySettings(configuration.getProperties()).build();
  }

}
