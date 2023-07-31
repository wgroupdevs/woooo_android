package eu.siacs.conversations.http.model;

import java.io.Serializable;



public  class UserBasicInfo implements Serializable {
    public String accountId;
    public String firstName;
    public String lastName;
    public String email;
    public Boolean isVarified;
    public String phoneNumber;
    public String imageURL;
    public int id;
    public String jid;

    public String getAccountId() {
        return accountId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getVarified() {
        return isVarified;
    }

    public void setVarified(Boolean varified) {
        isVarified = varified;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}