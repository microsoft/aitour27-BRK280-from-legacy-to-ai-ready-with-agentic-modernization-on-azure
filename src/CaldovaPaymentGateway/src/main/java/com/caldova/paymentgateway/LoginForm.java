package com.caldova.paymentgateway;

import org.apache.struts.action.ActionForm;

public class LoginForm extends ActionForm {
    private String sessionToken;

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }
}
