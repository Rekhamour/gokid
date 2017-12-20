package com.gokids.yoda_tech.gokids.medical.model;

import com.gokids.yoda_tech.gokids.eat.model.Contact;
import com.gokids.yoda_tech.gokids.eat.model.Specialization;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by rigoe on 6/2/2017.
 */

public class Medical implements Serializable {
    private String medicalID;
    private String category;
    private String name;
    private String Website;
    private String email;
    private String address;
    private String postal;
    private String latLong;
    private String schedule;
    private long kidsfinityScore;
    private String distance;
    private ArrayList<Specialization> specializations;
    private ArrayList<String> images;
    private ArrayList<Contact> contacts;
    private String tags;

    public String getMedicalID() {
        return medicalID;
    }

    public void setMedicalID(String medicalID) {
        this.medicalID = medicalID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() {
        return Website;
    }

    public void setWebsite(String website) {
        Website = website;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getLatLong() {
        return latLong;
    }

    public void setLatLong(String latLong) {
        this.latLong = latLong;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public long getKidsfinityScore() {
        return kidsfinityScore;
    }

    public void setKidsfinityScore(long kidsfinityScore) {
        this.kidsfinityScore = kidsfinityScore;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public ArrayList<Specialization> getSpecializations() {
        return specializations;
    }

    public void setSpecializations(ArrayList<Specialization> specializations) {
        this.specializations = specializations;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }
}
