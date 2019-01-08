package ru.javaops.masterjava.service.mail;

import com.typesafe.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import ru.javaops.masterjava.config.Configs;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.MailResultDao;
import ru.javaops.masterjava.persist.model.MailResult;

import java.util.Date;
import java.util.List;

@Slf4j
public class MailSender {

    private static MailResultDao mailResultDao = DBIProvider.getDao(MailResultDao.class);
    private static Email email = new SimpleEmail();
    private static Config emailConfig = Configs.getConfig("mail.conf", "mail");
    private static String host = emailConfig.getString("host");
    private static Integer port = emailConfig.getInt("port");
    private static String username = emailConfig.getString("username");
    private static String password = emailConfig.getString("password");
    private static Boolean useSSL = emailConfig.getBoolean("useSSL");
    private static Boolean useTLS = emailConfig.getBoolean("useTLS");
    private static Boolean debug = emailConfig.getBoolean("debug");
    private static String fromName = emailConfig.getString("fromName");

    static {
        email.setHostName(host);
        email.setSmtpPort(port);
        email.setAuthenticator(new DefaultAuthenticator(username, password));
        email.setSSLOnConnect(useSSL);
        email.setTLS(useTLS);
        email.setDebug(debug);
    }

    static void sendMail(List<Addressee> to, List<Addressee> cc, String subject, String body) {
        log.info("Send mail to \'" + to + "\' cc \'" + cc + "\' subject \'" + subject + (log.isDebugEnabled() ? "\nbody=" + body : ""));

        for (Addressee addressee : to) {
            try {

                email.setFrom(username);
                email.setSubject(subject);
                email.setMsg(body);
                email.addTo(addressee.getEmail());

                if (!cc.isEmpty()) {
                    cc.forEach(c -> {
                        try {
                            if (c.getEmail() != null && !c.getEmail().isEmpty()) {
                                email.addCc(c.getEmail());
                            }
                        } catch (EmailException e) {
                            log.info("Error add CC: " + c);
                            e.printStackTrace();
                        }
                    });
                }

                email.send();

                MailResult mailResult = MailResult.builder()
                                                  .dateTime(new Date())
                                                  .fromM(username)
                                                  .toM(addressee.getEmail())
                                                  .status("success")
                                                  .build();

                mailResultDao.insert(mailResult);

            } catch (EmailException e) {
                log.info("Send mail is NOT SUCCESS :(");
                e.printStackTrace();

                MailResult mailResult = MailResult.builder()
                                                  .dateTime(new Date())
                                                  .fromM(username)
                                                  .toM(addressee.getEmail())
                                                  .status("failed")
                                                  .build();
                mailResultDao.insert(mailResult);
            }
        }

    }
}
