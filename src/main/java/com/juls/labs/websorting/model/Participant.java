package com.juls.labs.websorting.model;

public class Participant extends User {

    public Participant(String username, String phone) {
        super(username, phone, "Participant");
    }

    public Participant(){
        super("","","Participant");
    }

}
