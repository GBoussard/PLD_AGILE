package drawmap.util;

import drawmap.model.*;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class XMLParser {
    //Nous nous servirons de cette variable plus tard
    private String node = null;
    private CityMap cm;
    private DeliveryTour dt;


    private class MapParser extends DefaultHandler {
        private CityMap cm;

        public MapParser(CityMap cm) {
            this.cm = cm;
        }

        public void startDocument() throws SAXException {
            System.out.println("Starting map parsing");
        }

        //fin du parsing
        public void endDocument() throws SAXException {
            System.out.println("End of map parsing");
        }

        public void startElement(String namespaceURI, String lname,
                                 String qname, Attributes attrs)
                throws SAXException {

            if (qname == "intersection") {
                cm.addIntersection(Long.parseLong(attrs.getValue("id")), Double.parseDouble(attrs.getValue("latitude")),
                        Double.parseDouble(attrs.getValue("longitude")));
            }
            if (qname == "segment") {
                cm.addSegment(Double.parseDouble(attrs.getValue("length")), Long.parseLong(attrs.getValue("origin")),
                        Long.parseLong(attrs.getValue("destination")), attrs.getValue("name"));

            }
        }

    }

    private class TourParser extends DefaultHandler {

        private DeliveryTour dt;
        private CityMap cm;

        public TourParser(DeliveryTour dt, CityMap cm) {
            this.dt = dt;
            this.cm = cm;
        }

        public void startDocument() throws SAXException {
            System.out.println("Starting delivery tour parsing");
        }

        //fin du parsing
        public void endDocument() throws SAXException {
            System.out.println("Fin du parsing");
        }

        public void startElement(String namespaceURI, String lname,
                                 String qname, Attributes attrs)
                throws SAXException {

            if (qname == "request") {
                Long pickupAddress = Long.parseLong(attrs.getValue("pickupAddress"));
                Long deliveryAddress = Long.parseLong(attrs.getValue("deliveryAddress"));

                Integer pickupDuration = Integer.parseInt(attrs.getValue("pickupDuration"));
                Integer deliveryDuration = Integer.parseInt(attrs.getValue("deliveryDuration"));


                dt.addRequest(new Request(cm.getIntersectionById(pickupAddress), cm.getIntersectionById(deliveryAddress), pickupDuration, deliveryDuration));

            }
            if (qname == "request") {
                dt.setOrigin(cm.getIntersectionById(Long.parseLong(attrs.getValue("address"))));
                try {
                    Date departureTime = new SimpleDateFormat("H:m:s").parse(attrs.getValue("departureTime"));
                    dt.setDepartureTime(departureTime);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        }


    }

    public XMLParser(CityMap cm, DeliveryTour dt) {
        this.cm = cm;
        this.dt = dt;
    }

    public void parseMap(String filepath) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            parser.parse(new File(filepath), new MapParser(this.cm));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void parseTour(String filepath, CityMap cm){
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            parser.parse(new File(filepath), new TourParser(this.dt, cm));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}