package com.gokids.yoda_tech.gokidsapp.medical.network;

import android.content.Context;

import com.gokids.yoda_tech.gokidsapp.eat.model.Contact;
import com.gokids.yoda_tech.gokidsapp.eat.model.Specialization;
import com.gokids.yoda_tech.gokidsapp.medical.model.Doctor;
import com.gokids.yoda_tech.gokidsapp.medical.model.Medical;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

/**
 * Created by rigoe on 6/2/2017.
 */

public class ApiCalls {

    private static final String BASE_URL = "http://52.77.82.210/";
    private Context context;

    public ApiCalls(Context context) {
        this.context = context;
    }

    public ArrayList<Doctor> getDoctorsForMedical(String medical, String name, double lat, double longi, String sortBy, String specialization, int start, int count){
        String url = BASE_URL + "api/viewAllDoctorsForMedical";
        url += "/latitude/" + lat;
        url += "/longitude/" + longi;
        if(specialization != null){
            url += "/specialization/" + specialization;
        }
        else{
            url += "/specialization/-";
        }
        url += "/medical/" + medical + "/limitStart/"+ start +"/count/" + count;
        if(sortBy != null){
            url += "/sortBy/" + sortBy;
        }
        if(name != null){
            url += "/searchBy/" + name;
        }
        final ArrayList<Doctor> doctors = new ArrayList<>();
        System.out.println(url);

        Ion.with(context)
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        JsonArray doc = result.getAsJsonArray("result");
                        for(int i=0;i<doc.size();i++){
                            Doctor d = new Doctor();
                            JsonElement obj = doc.get(i);
                            d.setDoctorId(obj.getAsJsonObject().get("DoctorID").toString());
                            d.setDoctorname(obj.getAsJsonObject().get("DoctorName").toString());
                            d.setDoctorDetail(obj.getAsJsonObject().get("DoctorDetail").toString());
                            d.setEmail(obj.getAsJsonObject().get("Email").toString());
                            d.setProcedure(obj.getAsJsonObject().get("Procedure").toString());
                            d.setPrice(obj.getAsJsonObject().get("Price").toString());
                            d.setSchedule(obj.getAsJsonObject().get("Schedule").toString());
                            d.setFrequency(obj.getAsJsonObject().get("Frequency").toString());
                            d.setImage(obj.getAsJsonObject().get("Image").toString());
                            d.setName(obj.getAsJsonObject().get("Name").toString());
                            d.setAddress(obj.getAsJsonObject().get("Address").toString());
                            d.setPostal(obj.getAsJsonObject().get("Postal").toString());
                            d.setLatlong(obj.getAsJsonObject().get("LatLong").toString());
                            d.setKidsfinityScore(obj.getAsJsonObject().get("KidsfinityScore").getAsInt());
                            d.setDistance(obj.getAsJsonObject().get("Distance").toString());
                            d.setYearOfExp(obj.getAsJsonObject().get("YearExperience").toString());
                            d.setMySchedule(obj.getAsJsonObject().get("MySchedule").toString());
                            d.setRates(obj.getAsJsonObject().get("Rate").toString());
                            d.setTags(obj.getAsJsonObject().get("Tags").toString());
                            ArrayList<Specialization> spe = new ArrayList<Specialization>();
                            JsonArray spec = obj.getAsJsonObject().get("Specialization").getAsJsonArray();
                            for (int j=0;j<spec.size();j++){
                                Specialization s = new Specialization();
                                s.setSpecialization(spec.get(j).getAsJsonObject().get("Specialization").toString());
                                s.setSpecializationId(spec.get(j).getAsJsonObject().get("SpecializationID").toString());
                                spe.add(s);
                            }
                            d.setSpecializations(spe);
                            ArrayList<Contact> con = new ArrayList<>();
                            JsonArray cont = obj.getAsJsonObject().get("Contacts").getAsJsonArray();
                            for (int j=0;j<cont.size();j++){
                                Contact c = new Contact();
                                c.setContactId(cont.get(j).getAsJsonObject().get("ContactID").getAsLong());
                                c.setOwnerId(cont.get(j).getAsJsonObject().get("OwnerID").getAsString());
                                c.setPhoneNo(cont.get(j).getAsJsonObject().get("PhoneNo").getAsString());
                                con.add(c);
                            }
                            d.setContacts(con);
                            ArrayList<String> images = new ArrayList<String>();
                            if(obj.getAsJsonObject().get("Images").isJsonArray()){
                                for(int j=0;j<obj.getAsJsonObject().get("Images").getAsJsonArray().size();j++){
                                    images.add(obj.getAsJsonObject().get("Images").getAsJsonArray().get(j).getAsJsonObject().get("ImageUrl").getAsString());
                                }
                            }
                            d.setImages(images);
                        }
                    }
                });
        return doctors;
    }

    public ArrayList<Doctor> getDoctors(String name,double lat,double longi,String sortBy,String specialization,int start,int count){
        String url = BASE_URL + "api/viewAllDoctors";
        url += "/latitude/" + lat;
        url += "/longitude/" + longi;
        if(specialization != null){
            url += "/specialization/" + specialization;
        }
        else{
            url += "/specialization/-";
        }
        url += "/limitStart/"+ start +"/count/" + count;
        if(sortBy != null){
            url += "/sortBy/" + sortBy;
        }
        if(name != null){
            url += "/searchBy/" + name;
        }
        final ArrayList<Doctor> doctors = new ArrayList<>();
        System.out.println(url);

        Ion.with(context)
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        JsonArray doc = result.getAsJsonArray("result");
                        for(int i=0;i<doc.size();i++){
                            Doctor d = new Doctor();
                            JsonElement obj = doc.get(i);
                            d.setDoctorId(obj.getAsJsonObject().get("DoctorID").toString());
                            d.setDoctorname(obj.getAsJsonObject().get("DoctorName").toString());
                            d.setDoctorDetail(obj.getAsJsonObject().get("DoctorDetail").toString());
                            d.setEmail(obj.getAsJsonObject().get("Email").toString());
                            d.setProcedure(obj.getAsJsonObject().get("Procedure").toString());
                            d.setPrice(obj.getAsJsonObject().get("Price").toString());
                            d.setSchedule(obj.getAsJsonObject().get("Schedule").toString());
                            d.setFrequency(obj.getAsJsonObject().get("Frequency").toString());
                            d.setImage(obj.getAsJsonObject().get("Image").toString());
                            d.setName(obj.getAsJsonObject().get("Name").toString());
                            d.setAddress(obj.getAsJsonObject().get("Address").toString());
                            d.setPostal(obj.getAsJsonObject().get("Postal").toString());
                            d.setLatlong(obj.getAsJsonObject().get("LatLong").toString());
                            d.setKidsfinityScore(obj.getAsJsonObject().get("KidsfinityScore").getAsInt());
                            d.setDistance(obj.getAsJsonObject().get("Distance").toString());
                            d.setYearOfExp(obj.getAsJsonObject().get("YearExperience").toString());
                            //d.setMySchedule(obj.getAsJsonObject().get("MySchedule").toString());
                            d.setRates(obj.getAsJsonObject().get("Rate").toString());
                            d.setTags(obj.getAsJsonObject().get("Tags").toString());
                            ArrayList<Specialization> spe = new ArrayList<Specialization>();
                            JsonArray spec = obj.getAsJsonObject().get("Specialization").getAsJsonArray();
                            for (int j=0;j<spec.size();j++){
                                Specialization s = new Specialization();
                                s.setSpecialization(spec.get(j).getAsJsonObject().get("Specialization").toString());
                                s.setSpecializationId(spec.get(j).getAsJsonObject().get("SpecializationID").toString());
                                spe.add(s);
                            }
                            d.setSpecializations(spe);
                            ArrayList<Contact> con = new ArrayList<>();
                            JsonArray cont = obj.getAsJsonObject().get("Contacts").getAsJsonArray();
                            for (int j=0;j<cont.size();j++){
                                Contact c = new Contact();
                                c.setContactId(cont.get(j).getAsJsonObject().get("ContactID").getAsLong());
                                c.setOwnerId(cont.get(j).getAsJsonObject().get("OwnerID").getAsString());
                                c.setPhoneNo(cont.get(j).getAsJsonObject().get("PhoneNo").getAsString());
                                con.add(c);
                            }
                            d.setContacts(con);
                            ArrayList<String> images = new ArrayList<String>();
                            if(obj.getAsJsonObject().get("Images").isJsonArray()){
                                for(int j=0;j<obj.getAsJsonObject().get("Images").getAsJsonArray().size();j++){
                                    images.add(obj.getAsJsonObject().get("Images").getAsJsonArray().get(j).getAsJsonObject().get("ImageUrl").getAsString());
                                }
                            }
                            d.setImages(images);
                        }
                    }
                });
        return doctors;
    }

    public ArrayList<Medical> getMedicals(String category, String name, double lat, double longi, String sortBy, String specialization, int start, int count){
        String url = BASE_URL + "api/viewAllMedical";
        url += "/latitude/" + lat;
        url += "/longitude/" + longi;
        url += "/category/" + category;
        if(specialization != null){
            url += "/specialization/" + specialization;
        }
        else{
            url += "/specialization/-";
        }
        url +="/limitStart/"+start+"/count/" + count;
        if(sortBy != null){
            url += "/sortBy/" + sortBy;
        }
        if(name != null){
            url += "/searchBy/" + name;
        }
        final ArrayList<Medical> medicals = new ArrayList<>();
        System.out.println(url);

        Ion.with(context)
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        JsonArray med = result.getAsJsonArray("result");
                        for(int i=0;i<med.size();i++){
                            Medical m = new Medical();
                            JsonElement obj = med.get(i);
                            m.setMedicalID(obj.getAsJsonObject().get("MedicalID").toString());
                            m.setCategory(obj.getAsJsonObject().get("Category").toString());
                            m.setName(obj.getAsJsonObject().get("Name").toString());
                            m.setWebsite(obj.getAsJsonObject().get("Website").toString());
                            m.setEmail(obj.getAsJsonObject().get("Email").toString());
                            m.setAddress(obj.getAsJsonObject().get("Address").toString());
                            m.setPostal(obj.getAsJsonObject().get("Postal").toString());
                            m.setLatLong(obj.getAsJsonObject().get("LatLong").toString());
                            m.setSchedule(obj.getAsJsonObject().get("Schedule").toString());
                            m.setKidsfinityScore(obj.getAsJsonObject().get("KidsfinityScore").getAsInt());
                            m.setDistance(obj.getAsJsonObject().get("Distance").toString());
                            ArrayList<Specialization> spe = new ArrayList<Specialization>();
                            JsonArray spec = obj.getAsJsonObject().get("Specialization").getAsJsonArray();
                            for (int j=0;j<spec.size();j++){
                                Specialization s = new Specialization();
                                s.setSpecializationHC(spec.get(j).getAsJsonObject().get("SpecializationHC").toString());
                                s.setSpecializationId(spec.get(j).getAsJsonObject().get("SpecializationHCID").toString());
                                spe.add(s);
                            }
                            m.setSpecializations(spe);
                            if(obj.getAsJsonObject().get("Contacts").isJsonArray()){
                                ArrayList<Contact> con = new ArrayList<>();
                                JsonArray cont = obj.getAsJsonObject().get("Contacts").getAsJsonArray();
                                for (int j=0;j<cont.size();j++){
                                    Contact c = new Contact();
                                    c.setContactId(cont.get(j).getAsJsonObject().get("ContactID").getAsLong());
                                    c.setOwnerId(cont.get(j).getAsJsonObject().get("OwnerID").getAsString());
                                    c.setPhoneNo(cont.get(j).getAsJsonObject().get("PhoneNo").getAsString());
                                    con.add(c);
                                }
                                m.setContacts(con);
                            }

                            ArrayList<String> images = new ArrayList<String>();
                            if(obj.getAsJsonObject().get("Images").isJsonArray()){
                                for(int j=0;j<obj.getAsJsonObject().get("Images").getAsJsonArray().size();j++){
                                    images.add(obj.getAsJsonObject().get("Images").getAsJsonArray().get(j).getAsJsonObject().get("ImageUrl").getAsString());
                                }
                            }
                            m.setImages(images);
                        }
                    }
                });
        return medicals;
    }

    public ArrayList<Specialization> getAllSpecializations(){
        final ArrayList<Specialization> spe = new ArrayList<Specialization>();
        Ion.with(context)
                .load(BASE_URL + "api/viewAllSpecializations")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        JsonArray spec = result.getAsJsonObject().get("result").getAsJsonArray();
                        for (int j=0;j<spec.size();j++){
                            Specialization s = new Specialization();
                            s.setSpecialization(spec.get(j).getAsJsonObject().get("Specialization").toString());
                            s.setSpecializationId(spec.get(j).getAsJsonObject().get("SpecializationID").toString());
                            s.setSpecializationHC(spec.get(j).getAsJsonObject().get("SpecializationHC").toString());
                            s.setDepartment(spec.get(j).getAsJsonObject().get("Department").toString());
                            spe.add(s);
                        }
                    }
                });
        return spe;
    }
}
