package com.gokids.yoda_tech.gokids.medical.model;

import com.gokids.yoda_tech.gokids.eat.model.Contact;
import com.gokids.yoda_tech.gokids.eat.model.Specialization;

import java.util.ArrayList;

/**
 * Created by rigoe on 6/2/2017.
 */

public class Doctor {
    private String doctorId;
    private String doctorname;
    private String doctorDetail;
    private String email;
    private String procedure;
    private String price;
    private String schedule;
    private String frequency;
    private String image;
    private String medicalId;
    private String name;
    private String address;
    private String postal;
    private String latlong;
    private int kidsfinityScore;
    private String distance;
    private String yearOfExp;
    private String offer;
    private ArrayList<Specialization> specializations;
    private ArrayList<String> images;
    private ArrayList<Contact> contacts;
    private String tags;
    private String rates;
    private String mySchedule;

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorname() {
        return doctorname;
    }

    public void setDoctorname(String doctorname) {
        this.doctorname = doctorname;
    }

    public String getDoctorDetail() {
        return doctorDetail;
    }

    public void setDoctorDetail(String doctorDetail) {
        this.doctorDetail = doctorDetail;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProcedure() {
        return procedure;
    }

    public void setProcedure(String procedure) {
        this.procedure = procedure;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMedicalId() {
        return medicalId;
    }

    public void setMedicalId(String medicalId) {
        this.medicalId = medicalId;
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

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getLatlong() {
        return latlong;
    }

    public void setLatlong(String latlong) {
        this.latlong = latlong;
    }

    public int getKidsfinityScore() {
        return kidsfinityScore;
    }

    public void setKidsfinityScore(int kidsfinityScore) {
        this.kidsfinityScore = kidsfinityScore;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getYearOfExp() {
        return yearOfExp;
    }

    public void setYearOfExp(String yearOfExp) {
        this.yearOfExp = yearOfExp;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
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

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getRates() {
        return rates;
    }

    public void setRates(String rates) {
        this.rates = rates;
    }

    public String getMySchedule() {
        return mySchedule;
    }

    public void setMySchedule(String mySchedule) {
        this.mySchedule = mySchedule;
    }
}
