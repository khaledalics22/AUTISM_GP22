package com.example.RestApis;
import com.google.gson.annotations.SerializedName;

public class SignUpData {
    @SerializedName("userName")

    private String userName;
    @SerializedName("password")

    private String password;
    @SerializedName("email")

    private String email;
    @SerializedName("gander")

    private String gander;
    @SerializedName("brithDay")

    private  String brithDay;
    @SerializedName("firstName")

    private String firstName;
    @SerializedName("lastName")

    private String lastName;


    // Getter Methods

    public SignUpData(String userName, String password, String email, String firstName, String lastName, String brithDay, String gander)
    {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.gander = gander;
        this.brithDay = brithDay;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBrithDay() {
        return brithDay;
    }

    public String getGander() {
        return gander;
    }

    // Setter Methods

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setBrithDay(String brithDay) {
        this.brithDay = brithDay;
    }

    public void setGander(String gander) {
        this.gander = gander;
    }
}