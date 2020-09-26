package com.AshutoshKarmakar.flashchatnewfirebase;

import android.util.Log;

public class InstanceMessage {
    String message;
    String author;


    public InstanceMessage(String message, String author){
        this.message = message;
        this.author = author;
        Log.d("FlashChat", "InstanceMessage:  constructor is called");
    }

    public InstanceMessage() {
    }

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }

}
