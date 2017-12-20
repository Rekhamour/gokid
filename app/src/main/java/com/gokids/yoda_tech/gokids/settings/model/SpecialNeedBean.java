package com.gokids.yoda_tech.gokids.settings.model;

import java.util.ArrayList;

/**
 * Created by Lenovo on 8/9/2017.
 */
public class SpecialNeedBean {
   private   ArrayList<String> needs ;
   private   ArrayList<String> cuisineneeds;
   private   ArrayList<String> dietsneed ;
   private   ArrayList<String> alleryneeds ;


    public ArrayList<String> getNeeds() {

//        Log.e("Specialneed","Specialneed size"+needs.size());
        return needs;
    }

    public void setNeeds(ArrayList<String> needs) {
//        needs.addAll(getAlleryneeds());
      //  needs.addAll(getCuisineneeds());
      //  needs.addAll(getDietsneed());
        this.needs = needs;
    }

    public ArrayList<String> getCuisineneeds() {
        return cuisineneeds;
    }

    public void setCuisineneeds(ArrayList<String> cuisineneeds) {
        this.cuisineneeds = cuisineneeds;
    }

    public ArrayList<String> getDietsneed() {
        return dietsneed;
    }

    public void setDietsneed(ArrayList<String> dietsneed) {
        this.dietsneed = dietsneed;
    }

    public ArrayList<String> getAlleryneeds() {
        return alleryneeds;
    }

    public void setAlleryneeds(ArrayList<String> alleryneeds) {
        this.alleryneeds = alleryneeds;
    }

    public void setObj(String needs) {
        this.needs.add(needs);
    }
}
