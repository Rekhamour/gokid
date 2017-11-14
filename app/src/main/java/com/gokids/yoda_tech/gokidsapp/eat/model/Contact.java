package com.gokids.yoda_tech.gokidsapp.eat.model;

import java.io.Serializable;

/**
 * Created by rigoe on 6/2/2017.
 */

public class Contact implements Serializable {

    private long contactId;
    private String ownerId;
    private String phoneNo;

    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
