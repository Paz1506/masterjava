/**
 * @author Pavel Zaytsev
 *
 * Created on 02.01.2019
 */

package ru.javaops.masterjava.xml;

import com.google.common.io.Resources;
import ru.javaops.masterjava.xml.schema.*;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.Schemas;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;

public class MainXml {

    private static final JaxbParser JAXB_PARSER = new JaxbParser(ObjectFactory.class);

    static {
        JAXB_PARSER.setSchema(Schemas.ofClasspath("payload.xsd"));
    }

    public static void main(String[] args) throws IOException, JAXBException {
        getUsersOfProjects("masterjava");
    }

    private static void getUsersOfProjects(String groupName) throws IOException, JAXBException {
        Payload payload = JAXB_PARSER.unmarshal(
                Resources.getResource("payload.xml").openStream());

        // юзеры
        payload.getUsers().getUser().forEach(user -> System.out.println(user.getGroups().getGroup().get(0).getGroup()));
        payload.getUsers().getUser().forEach(user -> System.out.println(user.getFullName()));

        //Ура! Ведут на один и тот же объект!
        // проекты
        System.out.println(payload.getProjects().getProject().get(0));

        // группы
        payload.getGroups().getGroup().forEach(group -> System.out.println(group));
//        System.out.println(payload.getGroups().getGroup().get(0).getProject());

//        List<User> userList = payload.getUsers().getUser();
//        userList.forEach(user -> System.out.println(user.getCity().getGroup().get(0).getGroup()));
    }
}
