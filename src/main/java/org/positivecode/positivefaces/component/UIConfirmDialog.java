package org.positivecode.positivefaces.component;

import org.positivecode.positivefaces.renderkit.html.ConfirmDialogRenderer;
import org.richfaces.component.UIPopupPanel;

import javax.faces.component.FacesComponent;

/**
 * @author positiveCoder
 */
@FacesComponent("org.positivefaces.ConfirmDialog")
public class UIConfirmDialog extends UIPopupPanel {
    public static final String COMPONENT_FAMILY = "org.positivefaces.ConfirmDialog";

    public UIConfirmDialog() {
        setRendererType(ConfirmDialogRenderer.RENDERER_TYPE);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getMessage() {
        return (String) getStateHelper().eval(Properties.message);
    }

    public void setMessage(String message) {
        getStateHelper().put(Properties.message, message);
    }

    @Override
    public boolean isModal() {
        return true;
    }

    @Override
    public void setModal(boolean modal) {
        // Do nothing
    }

    private enum Properties {message}
}
