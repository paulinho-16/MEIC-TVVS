package de.dominik_geyer.jtimesched.project;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProjectSerializerTest {
    private final String filename = "src/test/resources/WriteXmlTest.xml";
    private final ProjectSerializer ps = new ProjectSerializer(filename);

    public Document readXml() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new File(filename));
    }

    @Test
    public void testWriteXml_NullParameter_ShouldThrowException() {
        assertThrows(NullPointerException.class, () -> ps.writeXml(null));
    }

    @Test
    public void testWriteXml_EmptyList_ShouldWriteNoProjects() throws TransformerConfigurationException, IOException, SAXException, ParserConfigurationException {
        List<Project> projects = new ArrayList<>();
        ps.writeXml(projects);

        Document document = readXml();

        Element root = document.getDocumentElement();
        NodeList nl = root.getElementsByTagName("project");
        assertEquals(0, nl.getLength());
    }

    @Test
    public void testWriteXml_ProjectList_ShouldWriteAllProjects() throws TransformerConfigurationException, IOException, SAXException, ParserConfigurationException, ProjectException {
        // Create Projects
        List<Project> projects = new ArrayList<>();
        Project prj1 = new Project("Running Project");
        Project prj2 = new Project("Checked Project");
        Project prj3 = new Project("Times Project");

        // Change parameters of each project
        prj1.start();
        prj1.setNotes("A quick note");
        prj2.setChecked(true);
        prj2.setColor(new Color(3, 145, 255));
        prj3.setSecondsToday(440);
        prj3.setSecondsOverall(3600);
        prj3.setTimeCreated(Date.from(Instant.parse("2018-10-16T00:00:00.000Z")));

        // Write the Projects to the XML files
        projects.add(prj1);
        projects.add(prj2);
        projects.add(prj3);
        ps.writeXml(projects);

        // Read the Written XML
        Document document = readXml();
        Element root = document.getDocumentElement();
        NodeList nl = root.getElementsByTagName("project");

        // There is actually three projects in the XML file
        assertEquals(3, nl.getLength());

        // Compare the information of the projects with the information on the XML file

        // Project 1 (running)
        Element e = (Element) nl.item(0);
        String running = e.getElementsByTagName("running").item(0).getFirstChild().getNodeValue();
        // Even though project has started, the program writes "no" to the XML file, because in a new execution every project must be paused
        assertEquals("no", running);

        String notes = e.getElementsByTagName("notes").item(0).getFirstChild().getNodeValue();
        assertEquals("A quick note", notes);

        // Project 2 (checked)
        e = (Element) nl.item(1);

        String checked = e.getElementsByTagName("checked").item(0).getFirstChild().getNodeValue();
        assertEquals("yes", checked);

        NodeList pnl = e.getElementsByTagName("color");
        if (pnl.getLength() != 0) {
            e = (Element) pnl.item(0);
            int r = Integer.parseInt(e.getAttribute("red"));
            int g = Integer.parseInt(e.getAttribute("green"));
            int b = Integer.parseInt(e.getAttribute("blue"));
            int a = Integer.parseInt(e.getAttribute("alpha"));

            assertEquals(new Color(r, g, b, a), new Color(3, 145, 255));
        }

        // Project 3 (times)
        e = (Element) nl.item(2);

        int secondsToday = Integer.parseInt(((Element) e.getElementsByTagName("time").item(0)).getAttribute("today"));
        int secondsOverall = Integer.parseInt(((Element) e.getElementsByTagName("time").item(0)).getAttribute("overall"));

        assertEquals(440, secondsToday);
        assertEquals(3600, secondsOverall);

        long created = Long.parseLong(e.getElementsByTagName("created").item(0).getFirstChild().getNodeValue());
        assertEquals(Date.from(Instant.parse("2018-10-16T00:00:00.000Z")), new Date(created));
    }
}