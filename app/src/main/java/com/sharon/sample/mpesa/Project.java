package com.sharon.sample.mpesa;

public class Project {

    private String name;
    private String description;
    private String date;

    public Project(){
        // required default constructor
    }

    // setters
    public void setName(String projectName){
        this.name = projectName;
    }
    public  void setDescription(String description){
        this.description = description;
    }

    public void setDate(String date){
        this.date = date;
    }
    // getters
    public String getName(){ return  name;}
    public  String getDescription(){return description;}
    public String getDate(){return date;}
}
