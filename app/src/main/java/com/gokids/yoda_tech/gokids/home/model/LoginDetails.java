package com.gokids.yoda_tech.gokids.home.model;

import java.util.ArrayList;

/**
 * Created by manoj2prabhakar on 19/05/17.
 */

public class LoginDetails  {
    private String userName;
    private String imageURL;
    private ArrayList<DrawerItems> itemses;

    public LoginDetails(String userName, String imageURL, ArrayList<DrawerItems> itemses) {
        this.userName = userName;
        this.imageURL = imageURL;
        this.itemses = itemses;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public ArrayList<DrawerItems> getItemses() {
        return itemses;
    }

    public void setItemses(ArrayList<DrawerItems> itemses) {
        this.itemses = itemses;
    }
}
