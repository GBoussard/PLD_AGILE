package drawmap.util;

import drawmap.model.*;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLParser extends DefaultHandler{
    //Nous nous servirons de cette variable plus tard
    private String node = null;
    private CityMap cm;

    public XMLParser(CityMap cm) {
        this.cm = cm;
    }

    //début du parsing
    public void startDocument() throws SAXException {
        System.out.println("Début du parsing");
    }
    //fin du parsing
    public void endDocument() throws SAXException {
        System.out.println("Fin du parsing");
    }

    public void startElement(String namespaceURI, String lname,
                             String qname, Attributes attrs)
            throws SAXException {

        if(qname == "intersection") {
            cm.addIntersection(new Intersection(Long.parseLong(attrs.getValue("id")), Double.parseDouble(attrs.getValue("latitude")),
                    Double.parseDouble(attrs.getValue("longitude"))));
        }
        if(qname == "segment") {
            cm.addSegment(new Segment(Double.parseDouble(attrs.getValue("length")), cm.getIntersectionById(Long.parseLong(attrs.getValue("origin"))),
                    cm.getIntersectionById(Long.parseLong(attrs.getValue("destination"))), attrs.getValue("name")));

        }
    }

    public void endElement() throws SAXException {

    }
}