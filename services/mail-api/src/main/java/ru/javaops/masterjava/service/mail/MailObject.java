package ru.javaops.masterjava.service.mail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Pavel Zaytsev
 * <p>
 * Created on 10.03.2019
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MailObject implements Serializable {

    private String subject;
    private String body;
    private String users;
}
