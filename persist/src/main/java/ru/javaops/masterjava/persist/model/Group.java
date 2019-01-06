package ru.javaops.masterjava.persist.model;

import com.bertoncelj.jdbi.entitymapper.Column;
import lombok.*;

/**
 * @author Pavel Zaytsev
 * <p>
 * Created on 06.01.2019
 */

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Group extends BaseEntity {

    private @NonNull String name;
    private @NonNull GroupType type;
    @Column("project_id")
    private @NonNull Integer projectId;

    public Group(Integer id, Integer projectId, String name, GroupType type) {
        this.type = type;
        this.name = name;
        this.id = id;
        this.projectId = projectId;
    }
}
