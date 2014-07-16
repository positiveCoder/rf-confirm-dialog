package org.positivecode.positivefaces.component;

import com.google.common.base.Strings;

/**
 * @author positiveCoder
 */
public class ConfirmationScriptHolder {
    private String confirmationScript;

    public String getConfirmationScript() {
        return confirmationScript;
    }

    public void setConfirmationScript(String confirmationScript) {
        this.confirmationScript = confirmationScript;
    }

    public boolean requiresConfirmation() {
        return !Strings.isNullOrEmpty(confirmationScript);
    }
}
