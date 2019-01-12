package ru.javaops.masterjava.service.mail.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.dao.AbstractDao;
import ru.javaops.masterjava.service.mail.model.MailResult;

import java.util.List;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class MailResultDao implements AbstractDao {

    @SqlUpdate("TRUNCATE mailresult CASCADE ")
    @Override
    public abstract void clean();

    @SqlQuery("SELECT * FROM mailresult")
    public abstract List<MailResult> getAll();

    @SqlUpdate("INSERT INTO mailresult (fromM, toM, dateTime, status) VALUES (:fromM, :toM, :dateTime, :status)")
    public abstract void insert(@BindBean MailResult mailResult);
}
