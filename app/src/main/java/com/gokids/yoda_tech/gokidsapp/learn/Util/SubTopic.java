package com.gokids.yoda_tech.gokidsapp.learn.Util;

import java.io.Serializable;

/**
 * Created by manoj2prabhakar on 19/04/17.
 */

public class SubTopic implements Serializable {
    private String topicID;
    private String topicName;
    private boolean selected;

    public SubTopic(String topicID, String topicName, boolean selected){
        this.topicID = topicID;
        this.topicName = topicName;
        this.selected = selected;
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
