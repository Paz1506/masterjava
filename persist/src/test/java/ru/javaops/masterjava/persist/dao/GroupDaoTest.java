package ru.javaops.masterjava.persist.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.javaops.masterjava.persist.GroupTestData;
import ru.javaops.masterjava.persist.model.Group;

import java.util.List;

import static ru.javaops.masterjava.persist.GroupTestData.FIRST4_GROUPS;

public class GroupDaoTest extends AbstractDaoTest<GroupDao> {

    public GroupDaoTest() {
        super(GroupDao.class);
    }

    @BeforeClass
    public static void init() throws Exception {
        GroupTestData.init();
    }

    @Before
    public void setUp() throws Exception {
        GroupTestData.setUp();
    }

    @Test
    public void getWithLimit() {
        List<Group> groups = dao.getWithLimit(4);
        Assert.assertEquals(FIRST4_GROUPS, groups);
    }

    @Test
    public void insertBatch() throws Exception {
        dao.clean();
        dao.insertBatch(FIRST4_GROUPS, 3);
        Assert.assertEquals(4, dao.getWithLimit(100).size());
    }

//    @Test
//    public void getSeqAndSkip() throws Exception {
//        int seq1 = dao.getSeqAndSkip(5);
//        int seq2 = dao.getSeqAndSkip(1);
//        Assert.assertEquals(5, seq2 - seq1);
//    }
}