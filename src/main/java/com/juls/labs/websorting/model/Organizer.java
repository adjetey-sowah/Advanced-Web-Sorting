package com.juls.labs.websorting.model;

public class Organizer extends User {


    public Organizer(String username, String phone) {
        super(username, phone, "organizer");
    }

    public Organizer()
    {
        super("","","organizer");
    }
}


