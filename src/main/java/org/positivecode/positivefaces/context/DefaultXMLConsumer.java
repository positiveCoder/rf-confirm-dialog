package org.positivecode.positivefaces.context;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author positiveCoder
 */
public class DefaultXMLConsumer extends DefaultHandler2 {
    private ResponseWriter writer;
    private FacesContext context;

    public DefaultXMLConsumer(ResponseWriter writer, FacesContext context) {
        if (writer == null)
            throw new IllegalArgumentException("Response writer cannot be null");
        if (context == null)
            throw new IllegalArgumentException("Faces context cannot be null");
        this.writer = writer;
        this.context = context;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        try {
            writer.writeText(ch, start, length);
        } catch (IOException e) {
            throw new SAXException(e.getMessage(), e);
        }
    }

    @Override
    public void comment(char[] ch, int start, int length) throws SAXException {
        try {
            writer.writeComment(Arrays.copyOfRange(ch, start, start + length));
        } catch (IOException e) {
            throw new SAXException(e.getMessage(), e);
        }
    }

    @Override
    public void startCDATA() throws SAXException {
        try {
            writer.startCDATA();
        } catch (IOException e) {
            throw new SAXException(e.getMessage(), e);
        }
    }

    @Override
    public void endCDATA() throws SAXException {
        try {
            writer.endCDATA();
        } catch (IOException e) {
            throw new SAXException(e.getMessage(), e);
        }
    }

    @Override
    public void startDocument() throws SAXException {
        try {
            writer.startDocument();
        } catch (IOException e) {
            throw new SAXException(e.getMessage(), e);
        }
    }

    @Override
    public void endDocument() throws SAXException {
        try {
            writer.endDocument();
        } catch (IOException e) {
            throw new SAXException(e.getMessage(), e);
        }
    }

    @Override
    public void startElement(String uri,
                             String localName,
                             String qName,
                             Attributes atts) throws SAXException {
        try {
            writer.startElement(localName, null);
            writeAttributes(atts);
        } catch (IOException e) {
            throw new SAXException(e.getMessage(), e);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        try {
            writer.endElement(localName);
        } catch (IOException e) {
            throw new SAXException(e.getMessage(), e);
        }
    }

    protected int getAttributeIndex(Attributes atts, String localName) {
        for (int i = 0; i < atts.getLength(); i++)
            if (atts.getLocalName(i).equals(localName))
                return i;
        return -1;
    }

    protected String getAttributeValue(Attributes atts, String localName) {
        for (int i = 0; i < atts.getLength(); i++)
            if (atts.getLocalName(i).equals(localName))
                return atts.getValue(i);
        return null;
    }

    protected FacesContext getFacesContext() {
        return context;
    }

    protected ResponseWriter getResponseWriter() {
        return writer;
    }

    private void writeAttributes(Attributes attrs) throws IOException {
        if (attrs != null)
            for (int i = 0; i < attrs.getLength(); i++)
                writer.writeAttribute(attrs.getLocalName(i), attrs.getValue(i),
                        null);
    }

    protected class Element {
        private final String localName;
        private final Attributes attributes;

        public Element(String localName, Attributes attributes) {
            this.localName = localName;
            this.attributes = attributes;
        }

        public String getLocalName() {
            return localName;
        }

        public Attributes getAttributes() {
            return attributes;
        }
    }
}
