package com.example.try1;

import com.google.gson.annotations.SerializedName;

public class userinfo
{
        @SerializedName("_id")
        private String id;
        @SerializedName("userName")
        private String userName;
        @SerializedName("email")
        private String email;
        @SerializedName("firstName")
        private String firstName;
        @SerializedName("lastName")
        private String lastName;
        @SerializedName("type")
        private String type;
        @SerializedName("brithDay")
        private String brithDay;
        @SerializedName("__v")
        private Integer v;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getBrithDay() {
            return brithDay;
        }

        public void setBrithDay(String brithDay) {
            this.brithDay = brithDay;
        }

        public Integer getV() {
            return v;
        }

        public void setV(Integer v) {
            this.v = v;
        }
}
