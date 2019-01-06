package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.model.Group;

import java.util.List;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class GroupDao implements AbstractDao {

    public Group insert(Group group) {
        if (group.isNew()) {
            int id = insertGeneratedId(group);
            group.setId(id);
        } else {
            insertWitId(group);
        }
        return group;
    }

    @SqlQuery("SELECT nextval('group_seq')")
    abstract int getNextVal();

    @Transaction
    public int getSeqAndSkip(int step) {
        int id = getNextVal();
        DBIProvider.getDBI().useHandle(h -> h.execute("SELECT setval('group_seq', " + (id + step - 1) + ")"));
        return id;
    }

    @SqlUpdate("INSERT INTO groups (name, project_id, type) VALUES (:name, :projectId, CAST(:type AS GROUP_TYPE))")
    @GetGeneratedKeys
    abstract int insertGeneratedId(@BindBean Group group);

    @SqlUpdate("INSERT INTO groups (id, name, project_id, type) VALUES (:id, :name, :projectId, CAST(:type AS GROUP_TYPE)) ")
    abstract void insertWitId(@BindBean Group group);

    @SqlQuery("SELECT * FROM groups ORDER BY name LIMIT :it")
    public abstract List<Group> getWithLimit(@Bind int limit);

    //   http://stackoverflow.com/questions/13223820/postgresql-delete-all-content
    @SqlUpdate("TRUNCATE groups")
    @Override
    public abstract void clean();

    //    https://habrahabr.ru/post/264281/
    @SqlBatch("INSERT INTO groups (id, name, project_id, type) VALUES (:id, :name, :projectId, CAST(:type AS GROUP_TYPE))" +
              "ON CONFLICT DO NOTHING")
    public abstract int[] insertBatch(@BindBean List<Group> groups, @BatchChunkSize int chunkSize);


//    public List<String> insertAndGetConflictEmails(List<Group> users) {
//        int[] result = insertBatch(users, users.size());
//        return IntStreamEx.range(0, users.size())
//                          .filter(i -> result[i] == 0)
//                          .mapToObj(index -> users.get(index).getEmail())
//                          .toList();
//    }
}
