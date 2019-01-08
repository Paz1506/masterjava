package ru.javaops.masterjava.service.mail;

import com.typesafe.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import ru.javaops.masterjava.config.Configs;

import java.util.List;

@Slf4j
public class MailSender {

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

    static void sendMail(List<Addressee> to, List<Addressee> cc, String subject, String body) {
        log.info("Send mail to \'" + to + "\' cc \'" + cc + "\' subject \'" + subject + (log.isDebugEnabled() ? "\nbody=" + body : ""));

        try {
            email.setHostName(host);
            email.setSmtpPort(port);
            email.setAuthenticator(new DefaultAuthenticator(username, password));
            email.setSSLOnConnect(useSSL);
            email.setTLS(useTLS);
            email.setDebug(debug);
            email.setFrom(username);
            email.setSubject(subject);
            email.setMsg(body);

            to.forEach(t -> {
                try {
                    email.addTo(t.getEmail());
                } catch (EmailException e) {
                    log.info("Error add TO: " + t);
                }
            });

            cc.forEach(c -> {
                try {
                    email.addCc(c.getEmail());
                } catch (EmailException e) {
                    log.info("Error add CC: " + c);
                }
            });

            email.send();

        } catch (EmailException e) {
            log.info("Send mail is NOT SUCCESS :(");
            e.printStackTrace();
        }
    }
}
