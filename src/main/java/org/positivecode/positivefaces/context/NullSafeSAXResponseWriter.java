package org.positivecode.positivefaces.context;

import org.ajax4jsf.io.SAXResponseWriter;
import org.xml.sax.ContentHandler;

import java.io.IOException;

/**
 * @author positiveCoder
 */
public class NullSafeSAXResponseWriter extends SAXResponseWriter {
    public NullSafeSAXResponseWriter(ContentHandler consumer) {
        super(consumer);
    }

    @Override
    public void writeAttribute(String name,
                               Object value,
                               String property) throws IOException {
        if (value != null)
            super.writeAttribute(name, value, property);
    }

    @Override
    public void writeURIAttribute(String name,
                                  Object value,
                                  String property) throws IOException {
        if (value != null)
            super.writeAttribute(name, value, property);
    }
}
