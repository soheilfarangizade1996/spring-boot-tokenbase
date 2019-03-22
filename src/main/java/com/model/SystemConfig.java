package com.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;


@Entity
public class SystemConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String  sysTitle;

    private String sysValue;


    public SystemConfig() {
    }

    public SystemConfig(List<SystemConfig> value) {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSysTitle() {
        return sysTitle;
    }

    public void setSysTitle(String sysTitle) {
        this.sysTitle = sysTitle;
    }

    public String getSysValue() {
        return sysValue;
    }

    public void setSysValue(String sysValue) {
        this.sysValue = sysValue;
    }


    @Override
    public String toString() {
        return "SystemConfig{" +
                "id=" + id +
                ", sysTitle='" + sysTitle + '\'' +
                ", sysValue='" + sysValue + '\'' +
                '}';
    }
}
