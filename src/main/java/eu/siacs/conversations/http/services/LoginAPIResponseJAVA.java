package eu.siacs.conversations.http.services;

// import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
// import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString, Root.class); */


import java.io.Serializable;

public class LoginAPIResponseJAVA implements Serializable {

    public boolean Success;
    public String Message;
    public Object Error;
    public Data Data;

    public LoginAPIResponseJAVA.Data getData() {
        return Data;
    }

    public void setData(LoginAPIResponseJAVA.Data data) {
        Data = data;
    }


    public class Data implements Serializable {
        public String token;

        public UserBasicInfo user;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public UserBasicInfo getUser() {
            return user;
        }

        public void setUser(UserBasicInfo user) {
            this.user = user;
        }

    }

}



