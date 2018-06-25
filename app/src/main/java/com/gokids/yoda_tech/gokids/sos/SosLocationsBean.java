package com.gokids.yoda_tech.gokids.sos;

import java.io.Serializable;

/**
 * Created by Lenovo on 4/18/2018.
 */

public class SosLocationsBean  implements Serializable{
  private   String  Counter;
    private   String Sos;
    private   String SosEmail;
    private   String SosPhoneNo;
    private   String SosLocation;
    private   String DeviceToken;

    public String getCounter() {
        return Counter;
    }

    public void setCounter(String counter) {
        Counter = counter;
    }

    public String getSos() {
        return Sos;
    }

    public void setSos(String sos) {
        Sos = sos;
    }

    public String getSosEmail() {
        return SosEmail;
    }

    public void setSosEmail(String sosEmail) {
        SosEmail = sosEmail;
    }

    public String getSosPhoneNo() {
        return SosPhoneNo;
    }

    public void setSosPhoneNo(String sosPhoneNo) {
        SosPhoneNo = sosPhoneNo;
    }

    public String getSosLocation() {
        return SosLocation;
    }

    public void setSosLocation(String sosLocation) {
        SosLocation = sosLocation;
    }

    public String getDeviceToken() {
        return DeviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        DeviceToken = deviceToken;
    }
}
