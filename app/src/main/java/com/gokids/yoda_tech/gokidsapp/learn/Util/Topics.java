package com.gokids.yoda_tech.gokidsapp.learn.Util;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by manoj2prabhakar on 18/04/17.
 */

public class Topics implements Serializable {
    private String topicID;
    private String topicName;
    private ArrayList<String> subTopicIDs;
    private ArrayList<String> subTopicNames;



    public Topics(String topicID, String topicName, ArrayList<String> subTopicIDs, ArrayList<String> subTopicNames){
        this.topicID = topicID;
        this.topicName = topicName;
        this.subTopicIDs = subTopicIDs;
        this.subTopicNames = subTopicNames;
    }

    public String getTopicID() {
        return topicID;
    }

    public void setTopicID(String topicID) {
        this.topicID = topicID;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public ArrayList<String> getSubTopicIDs() {
        return subTopicIDs;
    }

    public void setSubTopicIDs(ArrayList<String> subTopicIDs) {
        this.subTopicIDs = subTopicIDs;
    }

    public ArrayList<String> getSubTopicNames() {
        return subTopicNames;
    }

    public void setSubTopicNames(ArrayList<String> subTopicNames) {
        this.subTopicNames = subTopicNames;
    }
}
