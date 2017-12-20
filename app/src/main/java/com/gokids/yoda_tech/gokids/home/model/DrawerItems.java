package com.gokids.yoda_tech.gokids.home.model;

/**
 * Created by manoj2prabhakar on 19/05/17.
 */

public class DrawerItems {
    private String nameText;
    private int image;

    public DrawerItems(String nameText, int image) {
        this.nameText = nameText;
        this.image = image;
    }

    public String getNameText() {
        return nameText;
    }

    public void setNameText(String nameText) {
        this.nameText = nameText;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
