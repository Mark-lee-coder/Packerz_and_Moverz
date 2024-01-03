package com.example.packerzmoverz.constructors;

import org.jetbrains.annotations.NotNull;

public class User {
        public String textName, textEmail, textPNumber, textPassword;

        public User(String textName, String textEmail, String textPNumber, String textPassword) {
            this.textName = textName;
            this.textEmail = textEmail;
            this.textPNumber = textPNumber;
            this.textPassword = textPassword;
        }

        public User() {

        }
}