package ru.otus.hibernate.dao;

import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.core.model.PhoneDataSet;
import ru.otus.core.model.User;
import ru.otus.hibernate.AbstractHibernateTest;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("ResultOfMethodCallIgnored")
@DisplayName("Dao для работы с пользователями должно ")
public class UserDaoHibernateTest extends AbstractHibernateTest {
    private SessionManagerHibernate sessionManagerHibernate;
    private UserDaoHibernate userDaoHibernate;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        sessionManagerHibernate = new SessionManagerHibernate(sessionFactory);
        userDaoHibernate = new UserDaoHibernate(sessionManagerHibernate);
    }

    @Test
    @DisplayName(" корректно загружать пользователя по заданному id")
    public void shouldFindCorrectUserById() {
        User expectedUser = buildDefaultUser();
        saveUser(expectedUser);
        assertThat(getUserStatistics().getInsertCount()).isEqualTo(1);

        assertThat(expectedUser.getId()).isGreaterThan(0);

        sessionManagerHibernate.beginSession();
        Optional<User> mayBeUser = userDaoHibernate.findById(expectedUser.getId());
        sessionManagerHibernate.commitSession();

        assertThat(mayBeUser).isPresent().get()
                             .hasFieldOrPropertyWithValue(User.Fields.id, expectedUser.getId())
                             .hasFieldOrPropertyWithValue(User.Fields.name, expectedUser.getName())
                             .hasFieldOrPropertyWithValue(User.Fields.addressDataSet, null);

        assertThat(getUserStatistics().getLoadCount()).isEqualTo(1);
        assertThatThrownBy(() -> mayBeUser.get().getPhoneDataSets().get(0))
                .isInstanceOf(LazyInitializationException.class);
    }

    @DisplayName(" корректно сохранять пользователя")
    @Test
    public void shouldCorrectSaveUser() {
        User expectedUser = buildDefaultUserWithChildren();
        sessionManagerHibernate.beginSession();
        long id = userDaoHibernate.saveUser(expectedUser);
        sessionManagerHibernate.commitSession();

        assertThat(id).isGreaterThan(0);

        User actualUser = loadUser(id);
        assertThat(actualUser).isNotNull().hasFieldOrPropertyWithValue("name", expectedUser.getName());

        expectedUser = buildDefaultUser();
        expectedUser.setId(id);
        sessionManagerHibernate.beginSession();
        long newId = userDaoHibernate.saveUser(expectedUser);
        sessionManagerHibernate.commitSession();

        assertThat(newId).isGreaterThan(0).isEqualTo(id);
        actualUser = loadUser(newId);
        assertThat(actualUser).isNotNull().hasFieldOrPropertyWithValue("name", expectedUser.getName());
    }

    @DisplayName(" корректно изменять пользователя")
    @Test
    public void shouldCorrectUpdateUser() {
        User expectedUser = buildDefaultUserWithChildren();
        sessionManagerHibernate.beginSession();
        long id = userDaoHibernate.saveUser(expectedUser);
        sessionManagerHibernate.commitSession();

        assertThat(id).isGreaterThan(0);
        assertThat(expectedUser.getPhoneDataSets()).hasSize(2);

        final PhoneDataSet phoneDataSet = new PhoneDataSet("88888888");
        expectedUser.addPhoneDataSet(phoneDataSet);
        sessionManagerHibernate.beginSession();
        long newId = userDaoHibernate.saveUser(expectedUser);
        sessionManagerHibernate.commitSession();
        assertThat(newId).isEqualTo(id);

        sessionManagerHibernate.beginSession();
        final List<PhoneDataSet> phoneDataSets = expectedUser.getPhoneDataSets();
        assertThat(phoneDataSets).hasSize(3).last().hasFieldOrPropertyWithValue(PhoneDataSet.Fields.number, "88888888");
        sessionManagerHibernate.commitSession();


        expectedUser.removePhoneDataSet(phoneDataSet);
        sessionManagerHibernate.beginSession();
        long afterDeleteId = userDaoHibernate.saveUser(expectedUser);
        sessionManagerHibernate.commitSession();
        assertThat(afterDeleteId).isEqualTo(id);

        sessionManagerHibernate.beginSession();
        final List<PhoneDataSet> phoneDataSetsAfterDelete = expectedUser.getPhoneDataSets();
        assertThat(phoneDataSetsAfterDelete).hasSize(2);
        sessionManagerHibernate.commitSession();
    }
}