package com.mindhash.MindhashApp.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EmailTokenJAXB {
    private String emailtoken;

    public EmailTokenJAXB(String emailtoken) {
        this.emailtoken = emailtoken;
    }

    public EmailTokenJAXB(){}

    public String geToken() {
        return this.emailtoken;
    }

    public void setEmailtoken(String emailtoken){ this.emailtoken = emailtoken; }
}
