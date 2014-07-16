package org.positivecode.positivefaces.context;

import org.positivecode.positivefaces.component.api.Confirmable;
import org.richfaces.renderkit.HtmlConstants;

import javax.faces.context.ResponseWriter;
import javax.faces.context.ResponseWriterWrapper;
import java.io.IOException;

/**
 * @author positiveCoder
 */
public class ConfirmCommandResponseWriter extends ResponseWriterWrapper {
    private ResponseWriter originalWriter;
    private Confirmable confirmableCommand;

    public ConfirmCommandResponseWriter(ResponseWriter originalWriter,
                                        Confirmable confirmableCommand) {
        this.originalWriter = originalWriter;
        this.confirmableCommand = confirmableCommand;
    }

    @Override
    public ResponseWriter getWrapped() {
        return originalWriter;
    }

    @Override
    public void writeAttribute(String name,
                               Object value,
                               String property) throws IOException {
        if (HtmlConstants.ONCLICK_ATTRIBUTE.equals(name)
                && confirmableCommand.requiresConfirmation()) {
            super.writeAttribute("data-pfconfirmcommand", value, null);
            value = confirmableCommand.getConfirmationScript();
        }
        super.writeAttribute(name, value, property);
    }
}
