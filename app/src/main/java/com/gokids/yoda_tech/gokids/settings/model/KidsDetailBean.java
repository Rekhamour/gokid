package com.gokids.yoda_tech.gokids.settings.model;

import java.util.ArrayList;

/**
 * Created by Lenovo on 8/11/2017.
 */
public class KidsDetailBean  {
    private String KidID;
    private String Age;
    private String Gender;
    private ArrayList<NeedBean> needsbean;

    public String getKidID() {
        return KidID;
    }

    public void setKidID(String kidID) {
        KidID = kidID;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public ArrayList<NeedBean> getNeedsbean() {
        return needsbean;
    }

    public void setNeedsbean(ArrayList<NeedBean> needsbean) {
        this.needsbean = needsbean;
    }
}
