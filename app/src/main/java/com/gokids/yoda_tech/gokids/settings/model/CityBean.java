package com.gokids.yoda_tech.gokids.settings.model;

/**
 * Created by Lenovo on 2/21/2018.
 */

public class CityBean {
  public String  Counter;
  public String  CityID;
  public String  City;

    public String getCounter() {
        return Counter;
    }

    public void setCounter(String counter) {
        Counter = counter;
    }

    public String getCityID() {
        return CityID;
    }

    public void setCityID(String cityID) {
        CityID = cityID;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    @Override
    public String toString() {
        return "CityBean{" +
                "Counter='" + Counter + '\'' +
                ", CityID='" + CityID + '\'' +
                ", City='" + City + '\'' +
                '}';
    }
}
