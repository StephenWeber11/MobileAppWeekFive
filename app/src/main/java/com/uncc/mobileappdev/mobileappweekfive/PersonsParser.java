package com.uncc.mobileappdev.mobileappweekfive;

import android.util.Xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Stephen Weber on 2/19/2018.
 */

public class PersonsParser {
    public static class PersonsSAXParser extends DefaultHandler{
        ArrayList<Person> persons;
        Person person;
        Address address;
        StringBuilder innerXml;

        static public ArrayList<Person> parsePersons(InputStream inputStream) throws IOException, SAXException {
            PersonsSAXParser parser = new PersonsSAXParser();
            Xml.parse(inputStream, Xml.Encoding.UTF_8, parser);
            return parser.persons;
        }

        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
            this.persons = new ArrayList<>();
            this.innerXml = new StringBuilder();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);

            if(localName.equals("person")){
                person = new Person();
                person.setId(Long.parseLong(attributes.getValue("id")));

            } else if(localName.equals("address")){
                address = new Address();
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);

            if(localName.equals("name")){
                person.setName(innerXml.toString());
            }else if(localName.equals("age")){
                person.setAge(Integer.parseInt(innerXml.toString().trim()));
            }else if(localName.equals("line1")){
                address.setLine1(innerXml.toString());
            }else if(localName.equals("city")){
                address.setCity(innerXml.toString());
            }else if(localName.equals("state")){
                address.setState(innerXml.toString());
            }else if(localName.equals("zip")){
                address.setZip(innerXml.toString());
            }else if(localName.equals("address")){
                person.setAddress(address);
                persons.add(person);
            }

            innerXml.setLength(0);
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            innerXml.append(ch, start, length);
        }
    }

    public static class PersonsPullParser{
        static public ArrayList<Person> parsePersons(InputStream inputStream) throws IOException, SAXException, XmlPullParserException {
            ArrayList<Person> persons = new ArrayList<>();
            Person person = null;
            Address address = null;

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(inputStream, "UTF-8");

            int event = parser.getEventType();

            while(event != XmlPullParser.END_DOCUMENT){

                switch(event){
                    case XmlPullParser.START_TAG:
                        if(parser.getName().equals("person")){
                            person = new Person();
                            person.setId(Long.parseLong(parser.getAttributeValue(null, "id")));
                        } else if(parser.getName().equals("address")){
                            address = new Address();
                        }else if(parser.getName().equals("name")){
                            person.setName(parser.nextText().trim());

                        }else if(parser.getName().equals("age")){
                            person.setAge(Integer.parseInt(parser.nextText().trim()));

                        }else if(parser.getName().equals("line1")){
                            address.setLine1(parser.nextText().trim());

                        }else if(parser.getName().equals("city")){
                            address.setCity(parser.nextText().trim());

                        }else if(parser.getName().equals("state")){
                            address.setState(parser.nextText().trim());

                        }else if(parser.getName().equals("zip")){
                            address.setZip(parser.nextText().trim());

                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("address")){
                            person.setAddress(address);
                        }else if(parser.getName().equals("person")){
                            persons.add(person);
                        }

                    default:
                        break;
                }

                event = parser.next();
            }


            return persons;
        }
    }

}
