package ru.javaops.masterjava.service.mail.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.javaops.masterjava.service.mail.MailResultTestData;
import ru.javaops.masterjava.service.mail.model.MailResult;

import java.util.List;

import static ru.javaops.masterjava.service.mail.MailResultTestData.FIRST3_RESULT_LIST;


public class MailResultDaoTest extends AbstractDaoTest<MailResultDao> {

    public MailResultDaoTest() {
        super(MailResultDao.class);
    }

    @BeforeClass
    public static void init() throws Exception {
        MailResultTestData.init();
    }

    @Before
    public void setUp() throws Exception {
        MailResultTestData.setUp();
    }

    @Test
    public void getAll() {
        List<MailResult> users = dao.getAll();
        Assert.assertEquals(FIRST3_RESULT_LIST, users);
    }

    @Test
    public void insertBatch() throws Exception {
        dao.clean();
        FIRST3_RESULT_LIST.forEach(mr -> dao.insert(mr));
        Assert.assertEquals(3, dao.getAll().size());
    }
}