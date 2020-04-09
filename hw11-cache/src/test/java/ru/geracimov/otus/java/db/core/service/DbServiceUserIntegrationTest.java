package ru.geracimov.otus.java.db.core.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.geracimov.otus.java.cache.HwCache;
import ru.geracimov.otus.java.cache.HwListener;
import ru.geracimov.otus.java.db.core.model.PhoneDataSet;
import ru.geracimov.otus.java.db.core.model.User;
import ru.geracimov.otus.java.db.hibernate.dao.UserDaoHibernate;
import ru.geracimov.otus.java.db.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис для работы с пользователями в рамках БД должен ")
@SuppressWarnings({"Convert2Diamond", "Convert2Lambda"})
public class DbServiceUserIntegrationTest extends AbstractHibernateTest {
    private DbServiceUserImpl serviceUser;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        SessionManagerHibernate sessionManagerHibernate = new SessionManagerHibernate(sessionFactory);
        UserDaoHibernate userDaoHibernate = new UserDaoHibernate(sessionManagerHibernate);
        serviceUser = new DbServiceUserImpl(userDaoHibernate);

        HwCache<Long, User> userCache = cacheManager.createCache("userCache", Long.class, User.class);
        HwListener<Long, User> listener = new HwListener<Long, User>() {
            @Override
            public void notify(Long key, User value, String action) {
                System.out.println(String.format("key:%s, value:%s, action: %s", key, value, action));
            }
        };
        userCache.addListener(listener);
        serviceUser.setCache(userCache);
    }


    @Test
    @DisplayName(" корректно сохранять пользователя")
    public void shouldCorrectSaveUser() {
        User user = buildDefaultUserWithChildren();
        final long savedId = serviceUser.saveUser(user);
        assertThat(savedId).isGreaterThan(0);

        final Optional<User> savedUser = serviceUser.getUserWithAddress(savedId);
        assertThat(savedUser).isNotEmpty().get().isEqualToIgnoringGivenFields(user, "phoneDataSets");
        assertThat(savedUser.get().getPhoneDataSets())
                .isNotNull()
                .hasSize(2)
                .containsExactly(user.getPhoneDataSets().toArray(new PhoneDataSet[2]));
    }

    @DisplayName(" корректно сохранять пользователя")
    @Test
    public void shouldCorrectSaveUser2() {
        User expectedUser = buildDefaultUserWithChildren();
        long id = serviceUser.saveUser(expectedUser);
        assertThat(getUserStatistics().getInsertCount()).isEqualTo(1);
        assertThat(getPhoneDataSetStatistics().getInsertCount()).isEqualTo(2);
        assertThat(getAddressDataSetStatistics().getInsertCount()).isEqualTo(1);

        assertThat(id).isGreaterThan(0);

        User actualUser = loadUser(id);
        assertThat(actualUser).isNotNull().hasFieldOrPropertyWithValue("name", expectedUser.getName());

        expectedUser = buildDefaultUser();
        expectedUser.setId(id);
        long newId = serviceUser.saveUser(expectedUser);

        assertThat(newId).isGreaterThan(0).isEqualTo(id);
        actualUser = loadUser(newId);
        assertThat(actualUser).isNotNull().hasFieldOrPropertyWithValue("name", expectedUser.getName());
    }

    @Test
    @DisplayName(" корректно загружать пользователя по заданному id")
    public void shouldFindCorrectUserById() {
        User expectedUser = buildDefaultUser();
        saveUser(expectedUser);

        assertThat(expectedUser.getId()).isGreaterThan(0);

        Optional<User> mayBeUser = serviceUser.getUser(expectedUser.getId());

        assertThat(mayBeUser).isPresent().get()
                .hasFieldOrPropertyWithValue(User.Fields.id, expectedUser.getId())
                .hasFieldOrPropertyWithValue(User.Fields.name, expectedUser.getName())
                .hasFieldOrPropertyWithValue(User.Fields.addressDataSet, null);
        assertThat(mayBeUser.get().getPhoneDataSets()).isNotNull().hasSize(0);
    }

    @DisplayName(" корректно изменять пользователя")
    @Test
    public void shouldCorrectUpdateUser() {
        User expectedUser = buildDefaultUserWithChildren();
        long id = serviceUser.saveUser(expectedUser);

        assertThat(id).isGreaterThan(0);
        assertThat(expectedUser.getPhoneDataSets()).hasSize(2);

        final PhoneDataSet phoneDataSet = new PhoneDataSet("88888888");
        expectedUser.addPhoneDataSet(phoneDataSet);
        long newId = serviceUser.saveUser(expectedUser);
        assertThat(newId).isEqualTo(id);

        final List<PhoneDataSet> phoneDataSets = expectedUser.getPhoneDataSets();
        assertThat(phoneDataSets).hasSize(3).last().hasFieldOrPropertyWithValue(PhoneDataSet.Fields.number, "88888888");


        expectedUser.removePhoneDataSet(phoneDataSet);
        long afterDeleteId = serviceUser.saveUser(expectedUser);
        assertThat(afterDeleteId).isEqualTo(id);

        final List<PhoneDataSet> phoneDataSetsAfterDelete = expectedUser.getPhoneDataSets();
        assertThat(phoneDataSetsAfterDelete).hasSize(2);
    }

}
