package ru.javaops.masterjava.persist.model;

import lombok.*;

import java.util.Date;

/**
 * @author Pavel Zaytsev
 * <p>
 * Created on 08.01.2019
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
public class MailResult extends BaseEntity {

    @NonNull
    private String toM;

    @NonNull
    private String fromM;

    @NonNull
    private Date dateTime;

    @NonNull
    private String status;

}
