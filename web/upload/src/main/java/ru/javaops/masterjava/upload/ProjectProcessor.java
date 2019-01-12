package ru.javaops.masterjava.upload;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.ProjectDao;
import ru.javaops.masterjava.persist.model.Project;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.stream.XMLStreamException;
import java.util.ArrayList;
import java.util.Map;

@Slf4j
public class ProjectProcessor {
    private final ProjectDao projectDao = DBIProvider.getDao(ProjectDao.class);
    private final GroupProcessor groupProcessor = new GroupProcessor();

    public Map<String, Project> process(StaxStreamProcessor processor) throws XMLStreamException {
        val map = projectDao.getAsMap();
        val newProjects = new ArrayList<Project>();

        while (processor.startElement("Project", "Projects")) {
            val name = processor.getAttribute("name");
            val descripion = processor.getElementValue("description");
            if (!map.containsKey(name)) {
                Project p = new Project(name, descripion);
                newProjects.add(p);
                log.info("Insert project " + p);
                int id = projectDao.insertGeneratedId(p);
                try {
                    groupProcessor.process(processor, id);
                } catch (XMLStreamException e) {
                    e.printStackTrace();
                }
            } else {
                int id = projectDao.getAll()
                                   .stream()
                                   .filter(p -> p.getName().equalsIgnoreCase(name))
                                   .findFirst()
                                   .orElse(null)
                                   .getId();

                groupProcessor.process(processor, id);
            }
        }

        return projectDao.getAsMap();
    }
}
