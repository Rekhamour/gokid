package com.gokids.yoda_tech.gokids.learn.Util;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by manoj2prabhakar on 19/04/17.
 */

public class ProviderDetails implements Serializable {
    private String learnProviderID;
    private String name;
    private String address;
    private String postalCode;
    private String latlong;
    private String email;
    private String courseID;
    private String title;
    private String details;
    private String priceSummary;
    private String pricePrefix;
    private String ageGroup;
    private String kidsAffinityScore;
    private String status;
    private String distance;
    private ArrayList<Contacts> contacts;
    private ArrayList<Images> images;
    private ArrayList<Classes> classes;


    public ProviderDetails(String learnProviderID, String name, String address, String postalCode, String latlong, String email, String courseID, String title, String details, String priceSummary, String pricePrefix, String ageGroup, String kidsAffinityScore, String status, String distance, ArrayList<Contacts> contacts, ArrayList<Images> images, ArrayList<Classes> classes) {
        this.learnProviderID = learnProviderID;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.latlong = latlong;
        this.email = email;
        this.courseID = courseID;
        this.title = title;
        this.details = details;
        this.priceSummary = priceSummary;
        this.pricePrefix = pricePrefix;
        this.ageGroup = ageGroup;
        this.kidsAffinityScore = kidsAffinityScore;
        this.status = status;
        this.distance = distance;
        this.contacts = contacts;
        this.images = images;
        this.classes = classes;
    }


    public String getLearnProviderID() {
        return learnProviderID;
    }

    public void setLearnProviderID(String learnProviderID) {
        this.learnProviderID = learnProviderID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getLatlong() {
        return latlong;
    }

    public void setLatlong(String latlong) {
        this.latlong = latlong;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getPriceSummary() {
        return priceSummary;
    }

    public void setPriceSummary(String priceSummary) {
        this.priceSummary = priceSummary;
    }

    public String getPricePrefix() {
        return pricePrefix;
    }

    public void setPricePrefix(String pricePrefix) {
        this.pricePrefix = pricePrefix;
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }

    public String getKidsAffinityScore() {
        return kidsAffinityScore;
    }

    public void setKidsAffinityScore(String kidsAffinityScore) {
        this.kidsAffinityScore = kidsAffinityScore;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public ArrayList<Contacts> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<Contacts> contacts) {
        this.contacts = contacts;
    }

    public ArrayList<Images> getImages() {
        return images;
    }

    public void setImages(ArrayList<Images> images) {
        this.images = images;
    }

    public ArrayList<Classes> getClasses() {
        return classes;
    }

    public void setClasses(ArrayList<Classes> classes) {
        this.classes = classes;
    }
}
