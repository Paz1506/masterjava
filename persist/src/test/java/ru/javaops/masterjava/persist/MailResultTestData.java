package ru.javaops.masterjava.persist;

import com.google.common.collect.ImmutableList;
import ru.javaops.masterjava.persist.dao.MailResultDao;
import ru.javaops.masterjava.persist.model.MailResult;

import java.util.Date;
import java.util.List;

public class MailResultTestData {

    public static MailResult RESULT1;
    public static MailResult RESULT2;
    public static MailResult RESULT3;
    public static List<MailResult> FIRST3_RESULT_LIST;

    public static void init() {

        RESULT1 = MailResult.builder()
                            .fromM("RESULT1")
                            .toM("1@mailmailmail.ru")
                            .status("success")
                            .dateTime(new Date())
                            .build();

        RESULT2 = MailResult.builder()
                            .fromM("RESULT2")
                            .toM("2@mailmailmail.ru")
                            .status("success")
                            .dateTime(new Date())
                            .build();

        RESULT3 = MailResult.builder()
                            .fromM("RESULT3")
                            .toM("2@mailmailmail.ru")
                            .status("success")
                            .dateTime(new Date())
                            .build();

        FIRST3_RESULT_LIST = ImmutableList.of(RESULT1, RESULT2, RESULT3);
    }

    public static void setUp() {
        MailResultDao dao = DBIProvider.getDao(MailResultDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            FIRST3_RESULT_LIST.forEach(dao::insert);
        });
        FIRST3_RESULT_LIST = dao.getAll();
    }
}
