package com.gokids.yoda_tech.gokids.settings.model;

/**
 * Created by manoj2prabhakar on 01/06/17.
 */

public class Cuisine {

    private int drawableID;
    private boolean isSelected = false;
    private String dispText;
    private String SpecialNeedID;


    public Cuisine(int drawableID, boolean isSelected, String dispText, String id) {
        this.drawableID = drawableID;
        this.isSelected = isSelected;
        this.dispText = dispText;
        this.SpecialNeedID= id;
    }

    public String getSpecialNeedID() {
        return SpecialNeedID;
    }

    public void setSpecialNeedID(String specialNeedID) {
        SpecialNeedID = specialNeedID;
    }

    public int getDrawableID() {
        return drawableID;
    }

    public void setDrawableID(int drawableID) {
        this.drawableID = drawableID;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getDispText() {
        return dispText;
    }

    public void setDispText(String dispText) {
        this.dispText = dispText;
    }
}
