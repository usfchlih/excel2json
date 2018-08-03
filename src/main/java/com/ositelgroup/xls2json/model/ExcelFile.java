package com.ositelgroup.xls2json.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Entity to hold ExcelFile Info
 */
@Entity
public class ExcelFile {

    @Id
    @GeneratedValue
    private long id;
    private String fileName;
    private boolean isUploaded;
    private String path;

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setUploaded(boolean uploaded) {
        isUploaded = uploaded;
    }

}
