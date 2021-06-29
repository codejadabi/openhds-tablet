package org.openhds.mobile.model;

import java.io.ByteArrayInputStream;

import org.xml.sax.SAXException;

import junit.framework.TestCase;

public class FormXmlReaderTest extends TestCase {

    String locationXml = 
            "<data id=\"location_registration_v1\">" +
            " <openhds>" +    
    		"  <fieldWorkerId>FWEK1</fieldWorkerId>" +
    		"  <hierarchyId>IDD</hierarchyId>" +
    		"  <locationId>AKPIDUIDD001</locationId>" +
    		" </openhds>" +
    		"  <locationName>Test House Name</locationName>" +
    		"  <locationType>RUR</locationType>" +
    		"  <latlong></latlong>" +
    		"</data>";
    
    String jrFormID= "location_registration_v1";

    public void testShouldReadLocationXml() throws SAXException {
        FormXmlReader xmlReader = new FormXmlReader();
        ByteArrayInputStream input = new ByteArrayInputStream(locationXml.getBytes());
        Location location = xmlReader.readLocation(input, jrFormID);

        assertEquals("Test House Name", location.getName());
        assertEquals("AKPIDUIDD001", location.getExtId());
        assertEquals("IDD", location.getHierarchy());
    }

    String visitXml = 
            "<data id=\"visit_registration_v3\">" +
            " <openhds>" + 
            "  <visitId>VAKPIDUIDD11001</visitId>" +
            "  <locationId>AKPIDUIDD001</locationId>" +
            "  <roundNumber>1</roundNumber>" +            
            " </openhds>" +            
            "  <visitDate>2008-12-15</visitDate>" +
            "</data>";

  /*  public void testShouldReadVisitXml() {
        FormXmlReader xmlReader = new FormXmlReader();
        ByteArrayInputStream input = new ByteArrayInputStream(visitXml.getBytes());
        Visit visit = xmlReader.readVisit(input, jrFormID);

        assertEquals("2008-12-15", visit.getDate());
        assertEquals("VAKPIDUIDD11001", visit.getExtId());
        assertEquals("AKPIDUIDD001", visit.getLocation());
        assertEquals("1", visit.getRound());
    }

    String socialGroupXml = 
            "<data id=\"socialgroup_registration_v1\">" +
            "  <householdId>AKPIDUIDD00101</householdId>" +
            "  <householdName>John Doe</householdName>" +
            "  <individualId>AKPIDUIDD0010101</individualId>" +
            "</data>";
    
    public void testShouldReadSocialGroupXml() {
        FormXmlReader xmlReader = new FormXmlReader();
        ByteArrayInputStream input = new ByteArrayInputStream(socialGroupXml.getBytes());
        SocialGroup sg = xmlReader.readSocialGroup(input, jrFormID);

        assertEquals("AKPIDUIDD00101", sg.getExtId());
        assertEquals("AKPIDUIDD0010101", sg.getGroupHead());
        assertEquals("John Doe", sg.getGroupName());
    }*/
}
