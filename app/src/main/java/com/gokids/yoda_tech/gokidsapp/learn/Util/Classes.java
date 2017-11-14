package com.gokids.yoda_tech.gokidsapp.learn.Util;

import java.io.Serializable;

/**
 * Created by manoj2prabhakar on 19/04/17.
 */

public class Classes implements Serializable {
    private String classID;
    private String subTopicID;
    private String subTopic;
    private String price;
    private String competencyLevel;
    private String cLMessage;
    private String mySchedule;

    public Classes(String classID, String subTopicID, String subTopic, String price, String competencyLevel, String cLMessage, String mySchedule) {
        this.classID = classID;
        this.subTopicID = subTopicID;
        this.subTopic = subTopic;
        this.price = price;
        this.competencyLevel = competencyLevel;
        this.cLMessage = cLMessage;
        this.mySchedule = mySchedule;
    }

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }

    public String getSubTopicID() {
        return subTopicID;
    }

    public void setSubTopicID(String subTopicID) {
        this.subTopicID = subTopicID;
    }

    public String getSubTopic() {
        return subTopic;
    }

    public void setSubTopic(String subTopic) {
        this.subTopic = subTopic;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCompetencyLevel() {
        return competencyLevel;
    }

    public void setCompetencyLevel(String competencyLevel) {
        this.competencyLevel = competencyLevel;
    }

    public String getcLMessage() {
        return cLMessage;
    }

    public void setcLMessage(String cLMessage) {
        this.cLMessage = cLMessage;
    }

    public String getMySchedule() {
        return mySchedule;
    }

    public void setMySchedule(String mySchedule) {
        this.mySchedule = mySchedule;
    }
}
