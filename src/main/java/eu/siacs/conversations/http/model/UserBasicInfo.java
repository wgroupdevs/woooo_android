package eu.siacs.conversations.http.model;

import java.io.Serializable;


public class UserBasicInfo implements Serializable {
    public String accountId;
    public String firstName;
    public String lastName;
    public String email;
    public String phoneNumber;
    public String imageURL;
    public int id;
    public String jid;
    public boolean isVarified;
    public String description;
    public String dateOfBirth;

    public String address;
    public String city;
    public String postalCode;
    public String state;
    public String country;
    public String language;
    public String languageCode;

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    public String walletAddress;


    public String getAccountId() {
        return accountId;
    }

    public String getLanguage() {
        return language;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDOB() {
        return dateOfBirth;
    }

    public void setDOB(String dob) {
        this.dateOfBirth = dob;
    }

    public String getAbout() {
        return description;
    }

    public void setAbout(String about) {
        this.description = about;
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

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setLanguageCode(String email) {
        this.languageCode = languageCode;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}