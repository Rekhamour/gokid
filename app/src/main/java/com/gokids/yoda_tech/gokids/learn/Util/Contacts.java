package com.gokids.yoda_tech.gokids.learn.Util;

import java.io.Serializable;

/**
 * Created by manoj2prabhakar on 19/04/17.
 */

public class Contacts implements Serializable {
    private String contactID;
    private String ownerID;
    private String phoneNumber;

    public Contacts(String contactID, String ownerID, String phoneNumber) {
        this.contactID = contactID;
        this.ownerID = ownerID;
        this.phoneNumber = phoneNumber;
    }

    public String getContactID() {
        return contactID;
    }

    public void setContactID(String contactID) {
        this.contactID = contactID;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
