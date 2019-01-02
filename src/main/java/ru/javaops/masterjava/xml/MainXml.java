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

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MainXml {

    private static final JaxbParser JAXB_PARSER = new JaxbParser(ObjectFactory.class);

    static {
        JAXB_PARSER.setSchema(Schemas.ofClasspath("payload.xsd"));
    }

    public static void main(String[] args) throws IOException, JAXBException {
        getUsersOfProjects("topjava");
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

//        List<User> userList = payload.getUsers().getUser().stream().filter(user -> user.getGroups().getGroup()).collect(Collectors.toList());
    }
}
