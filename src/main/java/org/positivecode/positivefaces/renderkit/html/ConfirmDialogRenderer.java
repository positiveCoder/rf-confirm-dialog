package org.positivecode.positivefaces.renderkit.html;

import com.google.common.base.Strings;
import org.positivecode.positivefaces.component.UIConfirmDialog;
import org.positivecode.positivefaces.context.DefaultXMLConsumer;
import org.positivecode.positivefaces.context.NullSafeSAXResponseWriter;
import org.richfaces.renderkit.HtmlConstants;
import org.richfaces.renderkit.html.PopupPanelRenderer;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author positiveCoder
 */
@FacesRenderer(rendererType = ConfirmDialogRenderer.RENDERER_TYPE,
        componentFamily = UIConfirmDialog.COMPONENT_FAMILY)
@ResourceDependencies({
        @ResourceDependency(name = "richfaces-event.js"),
        @ResourceDependency(library = "org.richfaces", name = "base-component.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "popupPanel.js"),
        @ResourceDependency(library = "org.richfaces", name = "popupPanelBorders.js"),
        @ResourceDependency(library = "org.richfaces", name = "popupPanelSizer.js"),
        @ResourceDependency(library = "org.richfaces", name = "popupPanel.ecss"),
        @ResourceDependency(library = "org.positivefaces", name="js/positivefaces.js"),
        @ResourceDependency(library = "org.positivefaces", name = "js/confirmdialog.js")
})
public class ConfirmDialogRenderer extends PopupPanelRenderer {
    public static final String RENDERER_TYPE = "org.positivefaces.ConfirmDialogRenderer";

    private static final String FACET_HEADER = "header";

    private static final String STYLE_CLASS_CONTENT          = "rf-pp-cnt";
    private static final String STYLE_CLASS_CONTENT_SCROLLER = "rf-pp-cnt-scrlr";
    private static final String STYLE_CLASS_ICON             = "pf-confirmdlg-icon";
    private static final String STYLE_CLASS_MESSAGE          = "pf-confirmdlg-msg";
    private static final String STYLE_CLASS_BUTTON_PANE      = "pf-confirmdlg-btn-pane";

    private static final Pattern CLIENT_COMPONENT_CTOR_CALL = Pattern.compile("new\\s+([\\w.]+)\\(");

    @Override
    public void doEncodeEnd(ResponseWriter writer,
                            FacesContext context,
                            UIComponent component) throws IOException {
        UIComponent headerFacet = component.getFacet(FACET_HEADER);
        if (headerFacet != null)
            headerFacet.setRendered(false);
        UIConfirmDialog confirmDialog = (UIConfirmDialog) component;
        String header = confirmDialog.getHeader();
        if (header == null)
            confirmDialog.setHeader("");
        ResponseWriter originalWriter = context.getResponseWriter();
        XMLConsumer xmlConsumer = new XMLConsumer(originalWriter, context, confirmDialog);
        NullSafeSAXResponseWriter saxWriter = new NullSafeSAXResponseWriter(xmlConsumer);
        context.setResponseWriter(saxWriter);
        saxWriter.startDocument();
        super.doEncodeEnd(saxWriter, context, component);
        saxWriter.endDocument();
        context.setResponseWriter(originalWriter);
    }

    @Override
    public void renderChildren(FacesContext facesContext, UIComponent component)
            throws IOException {
        // Do nothing
    }

    public void renderHeaderFacet(FacesContext context, UIComponent component) throws IOException {
        // Do nothing
    }

    @Override
    protected Class<? extends UIComponent> getComponentClass() {
        return UIConfirmDialog.class;
    }

    private void encodeButtonPane(ResponseWriter writer,
                                  FacesContext context,
                                  UIConfirmDialog dialog)
            throws IOException {
        writer.startElement("div", null);
        writer.writeAttribute("id", Strings
                .nullToEmpty(dialog.getClientId(context)) + "_btn_pane", null);
        writer.writeAttribute("class", STYLE_CLASS_BUTTON_PANE, null);
        super.renderChildren(context, dialog);
        writer.endElement("div");
    }

    private void encodeIconElement(ResponseWriter writer) throws IOException {
        writer.startElement("span", null);
        writer.writeAttribute("class", STYLE_CLASS_ICON, null);
        writer.endElement("span");
    }

    private void encodeMessageElement(ResponseWriter writer, UIConfirmDialog dialog)
            throws IOException {
        writer.startElement("span", null);
        writer.writeAttribute("class", STYLE_CLASS_MESSAGE, null);
        String msg = dialog.getMessage();
        if (!Strings.isNullOrEmpty(msg))
            writer.writeText(msg, null);
        writer.endElement("span");
    }

    private class XMLConsumer extends DefaultXMLConsumer {
        final UIConfirmDialog dialog;
        final String contentDivId;
        final String contentScrollerDivId;
        final Deque<Element> startedElements = new ArrayDeque<>();

        private XMLConsumer(ResponseWriter writer,
                            FacesContext context,
                            UIConfirmDialog dialog) {
            super(writer, context);
            this.dialog = dialog;
            String clientId = Strings.nullToEmpty(dialog.getClientId(context));
            contentDivId = clientId + "_content";
            contentScrollerDivId = clientId + "_content_scroller";
        }

        @Override
        public void startElement(String uri,
                                 String localName,
                                 String qName,
                                 Attributes atts) throws SAXException {
            super.startElement(uri, localName, qName, atts);
            Element startedElement = new Element(localName, atts);
            startedElements.push(startedElement);
            if (isContentDiv(startedElement)) {
                ResponseWriter writer = getResponseWriter();
                try {
                    encodeIconElement(writer);
                    encodeMessageElement(writer, dialog);
                } catch (IOException e) {
                    throw new SAXException(e.getMessage(), e);
                }
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            super.endElement(uri, localName, qName);
            if (isContentScrollerDiv(startedElements.pop()))
                try {
                    encodeButtonPane(getResponseWriter(), getFacesContext(),
                            dialog);
                } catch (IOException e) {
                    throw new SAXException(e.getMessage(), e);
                }
        }

        @Override
        public void characters(char[] ch, int start, int length)
                throws SAXException {
            if (ch != null && ch.length > 0
                    && isJavaScript(startedElements.peek())) {
                String text;
                if (start == 0 && ch.length == length)
                    text = new String(ch);
                else
                    text = new String(Arrays.copyOfRange(ch, start,
                            start + length));
                Matcher matcher = CLIENT_COMPONENT_CTOR_CALL.matcher(text);
                if (matcher.find()) {
                    ch = matcher.replaceFirst("new PositiveFaces.ui.ConfirmDialog(").toCharArray();
                    start = 0;
                    length = ch.length;
                }
            }
            super.characters(ch, start, length);
        }

        private boolean isContentDiv(Element element) {
            if (HtmlConstants.DIV_ELEM.equals(element.getLocalName())
                    && element.getAttributes() != null) {
                Attributes attrs = element.getAttributes();
                String id = getAttributeValue(attrs, HtmlConstants
                        .ID_ATTRIBUTE);
                if (contentDivId.equals(id)) {
                    String className = getAttributeValue(attrs, HtmlConstants
                            .CLASS_ATTRIBUTE);
                    if (STYLE_CLASS_CONTENT.equals(className))
                        return true;
                }
            }
            return false;
        }

        private boolean isContentScrollerDiv(Element element) {
            if (HtmlConstants.DIV_ELEM.equals(element.getLocalName())
                    && element.getAttributes() != null) {
                Attributes attrs = element.getAttributes();
                String id = getAttributeValue(attrs, HtmlConstants
                        .ID_ATTRIBUTE);
                if (contentScrollerDivId.equals(id)) {
                    String className = getAttributeValue(attrs, HtmlConstants
                            .CLASS_ATTRIBUTE);
                    if (STYLE_CLASS_CONTENT_SCROLLER.equals(className))
                        return true;
                }
            }
            return false;
        }

        private boolean isJavaScript(Element element) {
            if (HtmlConstants.SCRIPT_ELEM.equals(element.getLocalName())
                    && element.getAttributes() != null) {
                String type = getAttributeValue(element.getAttributes(),
                        HtmlConstants.TYPE_ATTR);
                if (HtmlConstants.TEXT_JAVASCRIPT_TYPE.equals(type))
                    return true;
            }
            return false;
        }
    }
}
