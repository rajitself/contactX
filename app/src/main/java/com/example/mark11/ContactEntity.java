package com.example.mark11;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "contacts")
public class ContactEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String number;

    // Optional: constructor
    public ContactEntity(String name, String number) {
        this.name = name;
        this.number = number;
    }

    // Optional: getters and setters (Room works fine without them if fields are public)
}
