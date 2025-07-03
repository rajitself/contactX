package com.example.mark11;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactDao {

    // ✅ Create
    @Insert
    void insertContact(ContactEntity contact);

    // ✅ Read All
    @Query("SELECT * FROM contacts ORDER BY name COLLATE NOCASE ASC")
    List<ContactEntity> getAllContacts();

    // ✅ Update
    @Update
    void updateContact(ContactEntity contact);

    // ✅ Delete
    @Delete
    void deleteContact(ContactEntity contact);
}
