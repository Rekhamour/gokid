package com.gokids.yoda_tech.gokids.ecommerce.model;

import java.io.Serializable;

/**
 * Created by Lenovo on 5/15/2018.
 */

public class ClothingBean implements Serializable {
    private boolean isSelected;
    private String dispText;
    private String id;

    public ClothingBean( boolean isSelected, String dispText, String id) {
        this.isSelected = isSelected;
        this.dispText = dispText;
        this.id=id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

