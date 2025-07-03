package com.example.mark11;

public class ContactModel {
    public int id;  // Add this field
    public int img;
    public String name, number;

    public ContactModel(int id, int img, String name, String number) {
        this.id = id;
        this.img = img;
        this.name = name;
        this.number = number;
    }
}

