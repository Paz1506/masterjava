package ru.javaops.masterjava.persist;

import com.google.common.collect.ImmutableList;
import ru.javaops.masterjava.persist.dao.CityDao;
import ru.javaops.masterjava.persist.model.City;

import java.util.List;

public class CityTestData {

    public static City CITY1;
    public static City CITY2;
    public static City CITY3;
    public static City CITY4;

    public static List<City> FIRST4_CITIES;

    public static void init() {

        CITY1 = new City("kiv", "Kiev");
        CITY2 = new City("mnsk", "Minsk");
        CITY3 = new City("mow", "Moscow");
        CITY4 = new City("spb", "Saints-Peterburg");

        FIRST4_CITIES = ImmutableList.of(CITY1, CITY2, CITY3, CITY4);
    }

    public static void setUp() {
        CityDao dao = DBIProvider.getDao(CityDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            FIRST4_CITIES.forEach(dao::insert);
        });
    }
}
