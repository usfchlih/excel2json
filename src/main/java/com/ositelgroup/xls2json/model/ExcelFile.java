package com.ositelgroup.xls2json.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;


@Entity
public class ExcelFile {

    @Id
    @GeneratedValue
    private long id;
    private String fileName;
    private boolean isUploaded;


    public ExcelFile() {
    }



    public ExcelFile(String fileName, boolean isUploaded) {
        this.fileName = fileName;
        this.isUploaded = isUploaded;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String filename) {
        this.fileName = filename;
    }

    public boolean isUploaded() {
        return isUploaded;
    }

    public void setUploaded(boolean uploaded) {
        isUploaded = uploaded;
    }

}