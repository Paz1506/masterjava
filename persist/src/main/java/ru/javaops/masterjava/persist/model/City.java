package ru.javaops.masterjava.persist.model;

import lombok.*;

/**
 * @author Pavel Zaytsev
 *
 * Created on 06.01.2019
 */

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class City extends BaseEntity {

    private @NonNull String shortcut;
    private @NonNull String name;

    public City(Integer id, String shortcut, String name) {
        this.shortcut = shortcut;
        this.name = name;
        this.id = id;

    }
}
