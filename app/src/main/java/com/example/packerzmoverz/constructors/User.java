package com.example.packerzmoverz.constructors;

public class User {
        public String UserName, UserEmail, PhoneNumber, Password;

        public User(String UserName, String UserEmail, String PhoneNumber, String Password) {
            this.UserName = UserName;
            this.UserEmail = UserEmail;
            this.PhoneNumber = PhoneNumber;
            this.Password = Password;
        }

        public User() {

        }
}