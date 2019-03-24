package ru.javaops.masterjava.webapp.akka;

import lombok.extern.slf4j.Slf4j;
import ru.javaops.masterjava.service.mail.GroupResult;
import ru.javaops.masterjava.service.mail.MailRemoteService;
import ru.javaops.masterjava.service.mail.util.MailUtils.MailObject;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;

import javax.servlet.AsyncContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static ru.javaops.masterjava.webapp.WebUtil.createMailObject;
import static ru.javaops.masterjava.webapp.akka.AkkaWebappListener.akkaActivator;

@WebServlet(value = "/sendAkkaTyped", loadOnStartup = 1, asyncSupported = true)
@Slf4j
@MultipartConfig
public class AkkaTypedSendServlet extends HttpServlet {

    private MailRemoteService mailService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        mailService = akkaActivator.getTypedRef(MailRemoteService.class, "akka.tcp://MailService@127.0.0.1:2553/user/mail-remote-service");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");

        // HW_11.1 1 из запроса достаем асинхронный контекст
        // просто getContext - выдавал ошибку, что запрос не асинхронный
        AsyncContext asyncContext = req.startAsync();

        // HW_11.1 2 выставляем таймаут для ответа
        asyncContext.setTimeout(5000L);

        // HW_11.1 3
        Runnable asyncWork = () -> {
            log.info("!!! AKKA start Async sending...");

            // HW_11.1 4 из сохраненного контекста получаем ответ
            ServletResponse response = asyncContext.getResponse();

            // HW_11.1 5 из непосредственно отправка через AKKA
            try {
                String resultAsyncSending = sendAkka(createMailObject(req));
                response.setContentType("text/plain");

                // HW_11.1 6 выводим результат пользователю
                PrintWriter writer = response.getWriter();
                writer.write(resultAsyncSending);
                writer.flush();

                log.info("!!! AKKA Async sending successful!");

            } catch (Exception e) {
                log.info("!!! AKKA error Async sending: " + e.getMessage());
            }

            asyncContext.complete();
        };

        // HW_11.1 7 запускаем вышеописанную задачу в ThreadPool'е томката
        asyncContext.start(asyncWork);

//        doAndWriteResponse(resp, () -> sendAkka(createMailObject(req)));
    }

    private String sendAkka(MailObject mailObject) throws Exception {
        scala.concurrent.Future<GroupResult> future = mailService.sendBulk(mailObject);
        log.info("Receive future, waiting result ...");
        GroupResult groupResult = Await.result(future, Duration.create(10, "seconds"));
        return groupResult.toString();
    }
}