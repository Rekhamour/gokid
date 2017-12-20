package com.gokids.yoda_tech.gokids.learn.Util;

import java.io.Serializable;

/**
 * Created by manoj2prabhakar on 19/04/17.
 */

public class Images implements Serializable{
    private String counter;
    private String imageID;
    private String ownerID;
    private String imageURL;

    public Images(String counter, String imageID, String ownerID, String imageURL) {
        this.counter = counter;
        this.imageID = imageID;
        this.ownerID = ownerID;
        this.imageURL = imageURL;
    }

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }

    public String getImageID() {
        return imageID;
    }

    public void setImageID(String imageID) {
        this.imageID = imageID;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
