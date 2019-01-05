package ru.javaops.masterjava.show;

import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.UserDao;
import ru.javaops.masterjava.persist.model.User;

import java.util.List;

public class UserProcessor {

    private static final UserDao dao = DBIProvider.getDao(UserDao.class);

    public List<User> getUsers() {
        return dao.getWithLimit(20);
    }
}
