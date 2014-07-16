package org.positivecode.positivefaces.component;

import org.positivecode.positivefaces.component.api.Confirmable;

import javax.faces.component.FacesComponent;

/**
 * @author positiveCoder
 */
@FacesComponent("org.positivefaces.CommandLink")
public class UICommandLink extends org.richfaces.component.UICommandLink
        implements Confirmable {
    private ConfirmationScriptHolder confirmScriptHolder = new ConfirmationScriptHolder();

    @Override
    public String getConfirmationScript() {
        return confirmScriptHolder.getConfirmationScript();
    }

    @Override
    public void setConfirmationScript(String confirmationScript) {
        confirmScriptHolder.setConfirmationScript(confirmationScript);
    }

    @Override
    public boolean requiresConfirmation() {
        return confirmScriptHolder.requiresConfirmation();
    }
}
