package ru.javaops.masterjava.service.mail.rest;


import com.google.common.collect.Lists;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.hibernate.validator.constraints.NotBlank;
import org.thymeleaf.util.StringUtils;
import ru.javaops.masterjava.service.mail.Attachment;
import ru.javaops.masterjava.service.mail.GroupResult;
import ru.javaops.masterjava.service.mail.MailServiceExecutor;
import ru.javaops.masterjava.service.mail.MailWSClient;
import ru.javaops.masterjava.service.mail.util.Attachments;
import ru.javaops.masterjava.web.WebStateException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

@Path("/")
public class MailRS {
    @GET
    @Path("test")
    @Produces(MediaType.TEXT_PLAIN)
    public String test() {
        return "Test";
    }

    @POST
    @Path("send")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
//    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public GroupResult send(
//                            @NotBlank @FormParam("users") String users,
                            @NotBlank @FormDataParam("users") String users,
//                            @FormParam("subject") String subject,
                            @FormDataParam("subject") String subject,
//                            @NotBlank @FormParam("body") String body,
                            @NotBlank @FormDataParam("body") String body,
//                            @FormDataParam("attach") InputStream uploadedInputStream,
                            @FormDataParam("attach") File uploadedInputStream,
                            @FormDataParam("attach") FormDataContentDisposition fileDetail
                           ) throws WebStateException, FileNotFoundException {

        //https://github.com/awslabs/aws-serverless-java-container/issues/223
        //Не помогло, файлы все еще битые
        InputStream fileIs = new FileInputStream(uploadedInputStream);
        System.out.println("!!!!!!!!!!! " + fileDetail.getFileName());
        // проверка пришедшего файла
        List<Attachment> attachments = Lists.newArrayList();
        if (!StringUtils.isEmptyOrWhitespace(fileDetail.getFileName())) {
//            attachments.add(Attachments.getAttachment(fileDetail.getFileName(), uploadedInputStream));
            attachments.add(Attachments.getAttachment(fileDetail.getFileName(), fileIs));
        }

        return MailServiceExecutor.sendBulk(MailWSClient.split(users), subject, body, attachments);
    }
}