package com.gokids.yoda_tech.gokids.sos;

import java.io.Serializable;

/**
 * Created by Lenovo on 4/18/2018.
 */

public class SenderLocationsBean implements Serializable {
    private String Counter;
    private String SOSLocation;
    private String SosEmail;
    private String ContactEmail;
    private String ContactPhone;
    private String ContactDeviceToken;
    private String ContactName;
    private String ContactLocation;

    public String getCounter() {
        return Counter;
    }

    public void setCounter(String counter) {
        Counter = counter;
    }

    public String getSOSLocation() {
        return SOSLocation;
    }

    public void setSOSLocation(String SOSLocation) {
        this.SOSLocation = SOSLocation;
    }

    public String getSosEmail() {
        return SosEmail;
    }

    public void setSosEmail(String sosEmail) {
        SosEmail = sosEmail;
    }

    public String getContactEmail() {
        return ContactEmail;
    }

    public void setContactEmail(String contactEmail) {
        ContactEmail = contactEmail;
    }

    public String getContactPhone() {
        return ContactPhone;
    }

    public void setContactPhone(String contactPhone) {
        ContactPhone = contactPhone;
    }

    public String getContactDeviceToken() {
        return ContactDeviceToken;
    }

    public void setContactDeviceToken(String contactDeviceToken) {
        ContactDeviceToken = contactDeviceToken;
    }

    public String getContactName() {
        return ContactName;
    }

    public void setContactName(String contactName) {
        ContactName = contactName;
    }

    public String getContactLocation() {
        return ContactLocation;
    }

    public void setContactLocation(String contactLocation) {
        ContactLocation = contactLocation;
    }
}
