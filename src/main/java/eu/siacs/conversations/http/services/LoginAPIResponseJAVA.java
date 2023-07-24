package eu.siacs.conversations.http.services;

// import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
// import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString, Root.class); */


public class LoginAPIResponseJAVA{

    public boolean Success;
    public String Message;
    public Object Error;
    public Data Data;


    public class Data{
        public String token;
        public User user;
    }

    public class User{
        public String accountId;
        public String firstName;
        public String lastName;
        public String email;
        public String phoneNumber;
        public String imageURL;
        public int id;
        public String jId;
    }
}



