package org.positivecode.positivefaces.component.api;

/**
 * Based on {@code org.primefaces.component.api.Confirmable}.
 *
 * @author positiveCode
 */
public interface Confirmable {
    public String getConfirmationScript();

    public void setConfirmationScript(String confirmationScript);

    public boolean requiresConfirmation();
}
