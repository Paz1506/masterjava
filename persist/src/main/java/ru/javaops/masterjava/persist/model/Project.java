package ru.javaops.masterjava.persist.model;

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
public class Project extends BaseEntity {

    private @NonNull String name;
    private @NonNull String description;

    public Project(Integer id, String description, String name) {
        this.description = description;
        this.name = name;
        this.id = id;

    }
}
