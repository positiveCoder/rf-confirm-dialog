package org.positivecode.positivefaces.renderkit.html;

import org.positivecode.positivefaces.component.UICommandLink;
import org.positivecode.positivefaces.context.ConfirmCommandResponseWriter;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;

/**
 * @author positiveCoder
 */
@FacesRenderer(rendererType = "org.positivefaces.CommandLinkRenderer",
        componentFamily = UICommand.COMPONENT_FAMILY)
public class CommandLinkRenderer extends org.richfaces.renderkit.html.CommandLinkRenderer {
    @Override
    public void doEncodeEnd(ResponseWriter writer,
                            FacesContext context,
                            UIComponent component) throws IOException {
        super.doEncodeEnd(new ConfirmCommandResponseWriter(writer, (UICommandLink) component),
                context, component);
    }

    @Override
    protected Class<? extends UIComponent> getComponentClass() {
        return UICommandLink.class;
    }
}
