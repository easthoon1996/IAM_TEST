package com.dreamsecurity.iam.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmployeeDto {
    @JsonProperty("employeeId")
    private String pernr;
    @JsonProperty("firstName")
    private String ename;
    private String orgeh;
    private String plans;
    private String email;
    private String begda;
    private String endda;

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
