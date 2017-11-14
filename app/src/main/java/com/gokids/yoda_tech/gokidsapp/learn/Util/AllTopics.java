package com.gokids.yoda_tech.gokidsapp.learn.Util;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by manoj2prabhakar on 21/04/17.
 */

public class AllTopics implements Serializable{
    private ArrayList<SubTopic> academics;
    private ArrayList<SubTopic> arts;
    private ArrayList<SubTopic> sports;
  
    public AllTopics(ArrayList<SubTopic> academics, ArrayList<SubTopic> arts, ArrayList<SubTopic> sports) {
        this.academics = academics;
        this.arts = arts;
        this.sports = sports;
    }

    public ArrayList<SubTopic> getAcademics() {
        return academics;
    }

    public void setAcademics(ArrayList<SubTopic> academics) {
        this.academics = academics;
    }

    public ArrayList<SubTopic> getArts() {
        return arts;
    }

    public void setArts(ArrayList<SubTopic> arts) {
        this.arts = arts;
    }

    public ArrayList<SubTopic> getSports() {
        return sports;
    }

    public void setSports(ArrayList<SubTopic> sports) {
        this.sports = sports;
    }
}
