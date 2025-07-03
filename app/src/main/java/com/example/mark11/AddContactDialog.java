package com.example.mark11;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class AddContactDialog extends Dialog {

    private EditText edtName, edtNumber;
    private Button btnSaveContact;
    private OnContactSavedListener listener;

    public interface OnContactSavedListener {
        void onContactSaved(String name, String number);
    }

    public AddContactDialog(@NonNull Context context, OnContactSavedListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_contact_dialog);

        edtName = findViewById(R.id.edtName);
        edtNumber = findViewById(R.id.edtNumber);
        btnSaveContact = findViewById(R.id.btnSaveContact);
        // 👇 Hide the Delete button in Add mode
        Button btnDeleteContact = findViewById(R.id.btnDeleteContact);
        btnDeleteContact.setVisibility(View.GONE);


        btnSaveContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtName.getText().toString().trim();
                String number = edtNumber.getText().toString().trim();

                if (name.isEmpty() || number.isEmpty()) {
                    Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Pass data back to activity/fragment
                listener.onContactSaved(name, number);
                dismiss();
            }
        });
    }
}
