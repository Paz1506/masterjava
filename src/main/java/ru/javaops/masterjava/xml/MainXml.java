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
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MainXml {

    private static final JaxbParser JAXB_PARSER = new JaxbParser(ObjectFactory.class);

    static {
        JAXB_PARSER.setSchema(Schemas.ofClasspath("payload.xsd"));
    }

    public static void main(String[] args) throws IOException, JAXBException, XMLStreamException {
        getUsersOfProjects("topjava");

        getUsersOfProjectsStax("topjava");
    }

    private static void getUsersOfProjects(String projectName) throws IOException, JAXBException {
        Payload payload = JAXB_PARSER.unmarshal(
                Resources.getResource("payload.xml").openStream());

        // Проект
        Project project = payload.getProjects()
                                 .getProject()
                                 .stream()
                                 .filter(project1 -> project1.getName()
                                                             .equalsIgnoreCase(projectName))
                                 .findFirst().get();

        // Группы, принадл. этому проекту
        List<Group> groups = payload.getGroups()
                                    .getGroup()
                                    .stream()
                                    .filter(group -> group.getProject()
                                                          .equals(project))
                                    .collect(Collectors.toList());

        List<User> users = payload.getUsers()
                                  .getUser();

        Set<User> result = new HashSet<>();

        for (User user : users) {
            List<User.Groups.Group> groupList = user.getGroups().getGroup();
            for (User.Groups.Group group : groupList) {
                System.out.println("Group: " + group + " | User: " + user);
                if (groups.contains(group.getGroup())) {
                    result.add(user);
                }
            }
        }

        System.out.println("result = " + result.size());
    }

    private static void getUsersOfProjectsStax(String projectName) throws IOException, XMLStreamException {
        String name;
        String email;

//        try (StaxStreamProcessor processor =
//                     new StaxStreamProcessor(Resources.getResource("payload.xml").openStream())) {
//            XMLStreamReader reader = processor.getReader();
//
//            //Вывести все города
//            String city;
//            while ((city = processor.getElementValue("City")) != null) {
//                System.out.println(city);
//            }
//
//        }

        try (StaxStreamProcessor processor =
                     new StaxStreamProcessor(Resources.getResource("payload.xml").openStream())) {
            XMLStreamReader reader = processor.getReader();

            //Вывести все емейлы пользователей
            String userEmail;
            String fullName;
            String group;
            List<String> userGroups = new ArrayList<>();
            while ((userEmail = processor.getElementAttributeValue("User", 2)) != null) {
                fullName = processor.getElementValue("fullName");

//                group = processor.getElementAttributeValue("group", 0);

                System.out.println("==== user ====");
                System.out.println(userEmail);
                System.out.println(fullName);
//                System.out.println(group);

                String gr;
                while ((gr = processor.getElementAttributeValue("group", 0)) != null) {
                    System.out.println(gr);
                    if (processor.endElement("group") && processor.endElement("groups")) break;
                }

            }

        }
    }
}
