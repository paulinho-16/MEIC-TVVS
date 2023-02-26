package de.dominik_geyer.jtimesched.project;

import de.dominik_geyer.jtimesched.JTimeSchedApp;
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
import static org.junit.jupiter.api.Assertions.*;
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
        prj1.setQuotaToday(60);
        prj1.setQuotaOverall(120);
        prj2.setChecked(true);
        prj2.setColor(new Color(3, 145, 255));
        prj3.setSecondsToday(440);
        prj3.setSecondsOverall(3600);
        prj3.setTimeCreated(Date.from(Instant.parse("2018-10-16T00:00:00.000Z")));

        // Write the Projects to the XML file
        projects.add(prj1);
        projects.add(prj2);
        projects.add(prj3);
        ps.writeXml(projects);

        // Read the Written XML
        Document document = readXml();
        Element root = document.getDocumentElement();

        // Verify XML properties
        assertEquals("UTF-8", document.getXmlEncoding());
        assertTrue(document.getDocumentElement().getTextContent().contains("    "));
        assertEquals(JTimeSchedApp.getAppVersion(), document.getDocumentElement().getAttribute("version"));
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

        int quotaToday = Integer.parseInt(((Element) e.getElementsByTagName("quota").item(0)).getAttribute("today"));
        int quotaOverall = Integer.parseInt(((Element) e.getElementsByTagName("quota").item(0)).getAttribute("overall"));

        assertEquals(60, quotaToday);
        assertEquals(120, quotaOverall);

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

    private void compareProjects(Project proj, Project returned) {
        assertAll("Test projects equality",
            () -> assertEquals(proj.getSecondsToday(), returned.getSecondsToday()),
            () -> assertEquals(proj.getSecondsOverall(), returned.getSecondsOverall()),
            () -> assertEquals(proj.isChecked(), returned.isChecked()),
            () -> assertEquals(proj.getColor(), returned.getColor()),
            () -> assertEquals(proj.getTimeCreated(), returned.getTimeCreated()),
            () -> assertEquals(proj.getTimeStart(), returned.getTimeStart()),
            () -> assertEquals(proj.getNotes(), returned.getNotes()),
            () -> assertEquals(proj.getQuotaToday(), returned.getQuotaToday()),
            () -> assertEquals(proj.getQuotaOverall(), returned.getQuotaOverall()),
            () -> assertTrue(proj.getTitle() == null && returned.getTitle().equals("") || proj.getTitle().equals(returned.getTitle()))
        );
    }

    @Test
    public void testReadXml_EmptyList_ShouldReadNoProjects() throws IOException, SAXException, ParserConfigurationException, TransformerConfigurationException {
        List<Project> projects = new ArrayList<>();
        ps.writeXml(projects);
        assertTrue(ps.readXml().isEmpty());
    }

    @Test
    public void testReadXml_ProjectList_ShouldReadAllProjects() throws ProjectException, IOException, SAXException, TransformerConfigurationException, ParserConfigurationException {
        List<Project> projects = new ArrayList<>();
        List<Project> read_projects;

        Project proj1 = new Project("Running Project");
        Project proj2 = new Project("Checked Project");
        Project proj3 = new Project("Times Project");
        Project proj4 = new Project("No Title");

        // Change parameters of each project
        proj1.start();
        proj1.setNotes("A quick note");
        proj1.setQuotaToday(60);
        proj1.setQuotaOverall(120);
        proj2.setChecked(true);
        proj2.setColor(new Color(3, 145, 255));
        proj3.setSecondsToday(440);
        proj3.setSecondsOverall(3600);
        proj3.setTimeCreated(Date.from(Instant.parse("2018-10-16T00:00:00.000Z")));
        proj3.setTimeStart(Date.from(Instant.parse("2018-10-18T00:00:00.000Z")));
        proj4.setTitle("");

        // Write the Projects to the XML file
        projects.add(proj1);
        projects.add(proj2);
        projects.add(proj3);
        projects.add(proj4);
        ps.writeXml(projects);

        read_projects = ps.readXml();

        // Check projects attributes
        Project read_proj1 = read_projects.get(0);
        Project read_proj2 = read_projects.get(1);
        Project read_proj3 = read_projects.get(2);
        Project read_proj4 = read_projects.get(3);

        compareProjects(proj1, read_proj1);
        compareProjects(proj2, read_proj2);
        compareProjects(proj3, read_proj3);
        compareProjects(proj4, read_proj4);
    }

    @Test
    public void testReadXml_ImpossibleField_ShouldReturnRunning() throws IOException, SAXException, ParserConfigurationException {
        Project proj;
        String filename2 = "src/test/resources/WriteXmlTest2.xml";
        ProjectSerializer ps2 = new ProjectSerializer(filename2);

        proj = ps2.readXml().get(0);

        assertTrue(proj.isRunning());
        assertEquals(0, proj.getQuotaOverall());
        assertEquals(0, proj.getQuotaToday());
        assertEquals("", proj.getNotes());
    }
}