package org.positivecode.positivefaces.component.behavior;

import org.positivecode.positivefaces.component.api.Confirmable;
import org.richfaces.component.behavior.ClientBehavior;

import javax.faces.FacesException;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.FacesBehavior;
import javax.faces.context.FacesContext;

/**
 * Based on {@code org.primefaces.behavior.confirm.ConfirmBehavior}.
 *
 * @author positiveCoder
 */
@FacesBehavior("org.positivefaces.behavior.ConfirmBehavior")
@ResourceDependency(library = "org.positivefaces", name = "js/positivefaces.js")
public class ConfirmBehavior extends ClientBehavior {
    @Override
    public String getScript(ClientBehaviorContext behaviorContext) {
        FacesContext context = behaviorContext.getFacesContext();
        UIComponent component = behaviorContext.getComponent();
        String source = component.getClientId(context);
        if (component instanceof Confirmable) {
            String script = "PositiveFaces.confirm({"
                    + "source:'" + source + "',"
                    + "sourceEvent:event,"
                    + "header:'" + getHeader() + "',"
                    + "message:'" + getMessage() + "',"
                    + "icon:'" + getIcon() + "'"
                    + "});return false;";
            ((Confirmable) component).setConfirmationScript(script);
            return null;
        }
        throw new FacesException("Component " + source + " is not "
                + "\"confirmable\". ConfirmBehavior can only be attached to "
                + "component that implements " + Confirmable.class.getName()
                + " interface");
    }

    public String getHeader() {
        return (String) getStateHelper().eval(Properties.header);
    }

    public void setHeader(String header) {
        getStateHelper().put(Properties.header, header);
    }

    public String getIcon() {
        return (String) getStateHelper().eval(Properties.icon);
    }

    public void setIcon(String icon) {
        getStateHelper().put(Properties.icon, icon);
    }

    @Override
    public void setLiteralAttribute(String name, Object value) {
        if (compare(Properties.header, name))
            setHeader((String) value);
        else if (compare(Properties.icon, name))
            setIcon((String) value);
        else if (compare(Properties.message, name))
            setMessage((String) value);
    }

    public String getMessage() {
        return (String) getStateHelper().eval(Properties.message);
    }

    public void setMessage(String message) {
        getStateHelper().put(Properties.message, message);
    }

    private enum Properties {header, icon, message}
}
