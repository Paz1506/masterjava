package ru.javaops.masterjava.persist;

import com.google.common.collect.ImmutableList;
import ru.javaops.masterjava.persist.dao.GroupDao;
import ru.javaops.masterjava.persist.model.Group;
import ru.javaops.masterjava.persist.model.GroupType;

import java.util.List;

public class GroupTestData {

    public static Group GROUP1;
    public static Group GROUP2;
    public static Group GROUP3;
    public static Group GROUP4;

    public static List<Group> FIRST4_GROUPS;

    public static void init() {

        ProjectTestData.init();
        ProjectTestData.setUp();

        GROUP1 = new Group("masterjava01", GroupType.current, ProjectTestData.PROJECT1.getId());
        GROUP2 = new Group("topjava06", GroupType.finished, ProjectTestData.PROJECT2.getId());
        GROUP3 = new Group("topjava07", GroupType.finished, ProjectTestData.PROJECT2.getId());
        GROUP4 = new Group("topjava08", GroupType.current, ProjectTestData.PROJECT2.getId());

        FIRST4_GROUPS = ImmutableList.of(GROUP1, GROUP2, GROUP3, GROUP4);
    }

    public static void setUp() {
        GroupDao dao = DBIProvider.getDao(GroupDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            FIRST4_GROUPS.forEach(dao::insert);
        });
    }
}
