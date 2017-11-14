package com.gokids.yoda_tech.gokidsapp.eat.model;

import java.io.Serializable;

/**
 * Created by rigoe on 6/2/2017.
 */

public class Specialization implements Serializable{
    private String specializationId;
    private String specialization;
    private String specializationHC;
    private String department;

    public String getSpecializationId() {
        return specializationId;
    }

    public void setSpecializationId(String specializationId) {
        this.specializationId = specializationId;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getSpecializationHC() {
        return specializationHC;
    }

    public void setSpecializationHC(String specializationHC) {
        this.specializationHC = specializationHC;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
