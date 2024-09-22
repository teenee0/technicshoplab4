package com.example.technicshoplab4.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String typee;
    private String groupp;
    private LocalDate importDate;
    private LocalDate exportDate;
    private String driverFullName;

    public Warehouse() {}

    public Warehouse(String type, String group, LocalDate importDate, LocalDate exportDate, String driverFullName) {
        this.typee = type;
        this.groupp = group;
        this.importDate = importDate;
        this.exportDate = exportDate;
        this.driverFullName = driverFullName;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return typee;
    }

    public void setType(String type) {
        this.typee = type;
    }

    public String getGroup() {
        return groupp;
    }

    public void setGroup(String group) {
        this.groupp = group;
    }

    public LocalDate getImportDate() {
        return importDate;
    }

    public void setImportDate(LocalDate importDate) {
        this.importDate = importDate;
    }

    public LocalDate getExportDate() {
        return exportDate;
    }

    public void setExportDate(LocalDate exportDate) {
        this.exportDate = exportDate;
    }

    public String getDriverFullName() {
        return driverFullName;
    }

    public void setDriverFullName(String driverFullName) {
        this.driverFullName = driverFullName;
    }
}
