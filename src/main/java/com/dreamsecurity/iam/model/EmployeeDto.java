package com.dreamsecurity.iam.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmployeeDto {
    @JsonProperty("employeeId")
    private String pernr;
    @JsonProperty("firstName")
    private String ename;
    @JsonProperty("department")
    private String orgeh;
    @JsonProperty("departmentName")
    private String orgehName;
    @JsonProperty("jobTitle")
    private String plans;
    @JsonProperty("workEmail")
    private String email;
    @JsonProperty("hireDate")
    private String begda;
    @JsonProperty("terminationDate")
    private String endda;

    public String getOrgehName() {
        return orgehName;
    }

    public void setOrgehName(String orgehName) {
        this.orgehName = orgehName;
    }

    public String getPernr() {
        return pernr;
    }

    public void setPernr(String pernr) {
        this.pernr = pernr;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getOrgeh() {
        return orgeh;
    }

    public void setOrgeh(String orgeh) {
        this.orgeh = orgeh;
    }

    public String getPlans() {
        return plans;
    }

    public void setPlans(String plans) {
        this.plans = plans;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBegda() {
        return begda;
    }

    public void setBegda(String begda) {
        this.begda = begda;
    }

    public String getEndda() {
        return endda;
    }

    public void setEndda(String endda) {
        this.endda = endda;
    }
}
