package com.example.mark11;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerViewContacts;
    RecyclerContactAdapter adapter;
    ArrayList<ContactModel> arrContact = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        recyclerViewContacts = findViewById(R.id.recyclerViewContact);
        recyclerViewContacts.setLayoutManager(new LinearLayoutManager(this));

        // ✅ STEP 1: Get DB instance
        ContactDatabase db = ContactDatabase.getInstance(this);

        // ✅ STEP 2: Insert sample data (optional, once)
        if (db.contactDao().getAllContacts().isEmpty()) {
            db.contactDao().insertContact(new ContactEntity("Rahul", "1234567890"));
            db.contactDao().insertContact(new ContactEntity("Aditya", "9876543210"));
        }

        // ✅ STEP 3: Fetch from Room and add to ContactModel
        List<ContactEntity> contactList = db.contactDao().getAllContacts();
        Collections.sort(arrContact, (c1, c2) -> c1.name.compareToIgnoreCase(c2.name));
        // Inside onCreate
        for (ContactEntity e : contactList) {
            arrContact.add(new ContactModel(e.id, R.drawable.baseline_person_24, e.name, e.number));
        }

// Fix adapter constructor call:
        adapter = new RecyclerContactAdapter(this, arrContact, db);

        recyclerViewContacts.setAdapter(adapter);

        // ✅ STEP 5: FloatingActionButton click
        FloatingActionButton fab = findViewById(R.id.btnAdd); // 🟢 ensure correct ID in XML

        fab.setOnClickListener(view -> {
            AddContactDialog dialog = new AddContactDialog(MainActivity.this, (name, number) -> {
                // Insert into DB
                ContactEntity entity = new ContactEntity(name, number);
                db.contactDao().insertContact(entity);

                // Retrieve inserted ID
                List<ContactEntity> updatedList = db.contactDao().getAllContacts();
                ContactEntity last = updatedList.get(updatedList.size() - 1); // assumes last is the new

                // Add to adapter list
                ContactModel newContact = new ContactModel(last.id, R.drawable.baseline_person_24, name, number);
                arrContact.add(newContact);


                adapter.notifyDataSetChanged();
            });
            dialog.show();
        });


    }
}
