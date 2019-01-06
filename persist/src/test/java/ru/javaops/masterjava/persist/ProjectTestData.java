package ru.javaops.masterjava.persist;

import com.google.common.collect.ImmutableList;
import ru.javaops.masterjava.persist.dao.ProjectDao;
import ru.javaops.masterjava.persist.model.Project;

import java.util.List;

public class ProjectTestData {

    public static Project PROJECT1;
    public static Project PROJECT2;

    public static List<Project> FIRST2_PROJECTS;

    public static void init() {

        PROJECT1 = new Project("masterjava", "Masterjava");
        PROJECT2 = new Project("topjava", "Topjava");

        FIRST2_PROJECTS = ImmutableList.of(PROJECT1, PROJECT2);
    }

    public static void setUp() {
        ProjectDao dao = DBIProvider.getDao(ProjectDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            FIRST2_PROJECTS.forEach(dao::insert);
        });
    }
}
