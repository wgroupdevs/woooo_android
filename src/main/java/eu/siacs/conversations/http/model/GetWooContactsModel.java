package eu.siacs.conversations.http.model;

import java.util.ArrayList;


public class GetWooContactsModel {
    public boolean Success;
    public String Message;
    public String Error;
    public ArrayList<WooContactsData> Data;
}

class WooContactsData {
    public String id = "";
    public String name = "";
    public String phoneNumber = "";
    public int contactId;
    public String contactUniqueId;
    public String image;
    public String email;
    public String description;
    public String time;
    public boolean favourite;
    public boolean archived;
    public boolean block;
    public String joined;
    public boolean isMute;
    public boolean added;
    public String jid;
    public boolean isTemp;
}


