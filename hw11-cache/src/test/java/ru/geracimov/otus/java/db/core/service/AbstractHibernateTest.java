package ru.geracimov.otus.java.db.core.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.stat.EntityStatistics;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import ru.geracimov.otus.java.cache.CacheManager;
import ru.geracimov.otus.java.db.core.model.AddressDataSet;
import ru.geracimov.otus.java.db.core.model.PhoneDataSet;
import ru.geracimov.otus.java.db.core.model.User;
import ru.geracimov.otus.java.db.hibernate.HibernateUtils;

public abstract class AbstractHibernateTest {
    private static final String HIBERNATE_CFG_XML_FILE_RESOURCE = "hibernate-test.cfg.xml";
    protected static final String DEFAULT_USER_NAME = "default user name";
    protected static final String DEFAULT_PHONE_DATASET1 = "+70987654321";
    protected static final String DEFAULT_PHONE_DATASET2 = "+71234567890";
    protected static final String DEFAULT_ADDRESS_DATASET1 = "Custom address";


    protected SessionFactory sessionFactory;
    protected CacheManager cacheManager;

    @BeforeEach
    public void setUp() {
        sessionFactory = HibernateUtils.buildSessionFactory(HIBERNATE_CFG_XML_FILE_RESOURCE, "ru.geracimov.otus.java.db.core.model");
        cacheManager = new CacheManager();
    }

    @AfterEach
    void tearDown() {
        sessionFactory.close();
        cacheManager.clear();
    }

    protected User buildDefaultUser() {
        return new User(DEFAULT_USER_NAME);
    }

    protected User buildDefaultUserWithChildren() {
        final User user = buildDefaultUser();
        user.setAddressDataSet(new AddressDataSet(DEFAULT_ADDRESS_DATASET1));
        user.addPhoneDataSet(new PhoneDataSet(DEFAULT_PHONE_DATASET1));
        user.addPhoneDataSet(new PhoneDataSet(DEFAULT_PHONE_DATASET2));
        return user;
    }

    protected void saveUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            saveUser(session, user);
        }
    }

    protected void saveUser(Session session, User user) {
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
    }

    protected User loadUser(long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.find(User.class, id);
        }
    }

    protected EntityStatistics getUserStatistics() {
        Statistics stats = sessionFactory.getStatistics();
        return stats.getEntityStatistics(User.class.getName());
    }

    protected EntityStatistics getPhoneDataSetStatistics() {
        Statistics stats = sessionFactory.getStatistics();
        return stats.getEntityStatistics(PhoneDataSet.class.getName());
    }

    protected EntityStatistics getAddressDataSetStatistics() {
        Statistics stats = sessionFactory.getStatistics();
        return stats.getEntityStatistics(AddressDataSet.class.getName());
    }

}
