package com.example.mark11;


import android.annotation.SuppressLint;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class RecyclerContactAdapter extends RecyclerView.Adapter<RecyclerContactAdapter.ViewHolder> {

    Context context;
    ArrayList<ContactModel> arrContact;
    ContactDatabase myDatabase;
    public static int getRandomColor() {
        Random rnd = new Random();
        return Color.rgb(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }


    public static Bitmap createLetterIcon(String letter, int color) {
        int size = 100; // size of the icon in pixels

        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // Draw background circle
        Paint backgroundPaint = new Paint();
        backgroundPaint.setColor(color);
        backgroundPaint.setAntiAlias(true);
        canvas.drawCircle(size / 2, size / 2, size / 2, backgroundPaint);

        // Draw letter
        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(48);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);

        Rect textBounds = new Rect();
        textPaint.getTextBounds(letter, 0, 1, textBounds);

        float x = size / 2f;
        float y = size / 2f - (textPaint.descent() + textPaint.ascent()) / 2f;
        canvas.drawText(letter.toUpperCase(), x, y, textPaint);

        return bitmap;
    }



    public RecyclerContactAdapter(Context context, ArrayList<ContactModel> arrContact, ContactDatabase myDatabase) {
        this.context = context;
        this.arrContact = arrContact;
        this.myDatabase = myDatabase;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.contact_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ContactModel contact = arrContact.get(position);
        holder.txtname.setText(contact.name);
        holder.txtnumber.setText(contact.number);
        //holder.img.setImageResource(contact.img);

        String firstLetter = contact.name.substring(0, 1).toUpperCase();
        int color = getRandomColor();
        // or random color

        Bitmap icon = createLetterIcon(firstLetter, color);
        holder.img.setImageBitmap(icon);


        holder.llRow.setOnLongClickListener(view -> {
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.activity_add_contact_dialog);

            EditText edtName = dialog.findViewById(R.id.edtName);
            EditText edtNumber = dialog.findViewById(R.id.edtNumber);
            Button btnSaveContact = dialog.findViewById(R.id.btnSaveContact);
            Button btnDeleteContact = dialog.findViewById(R.id.btnDeleteContact);
            TextView dialogTitle = dialog.findViewById(R.id.dialogTitle);

            // Prefill data
            edtName.setText(contact.name);
            edtNumber.setText(contact.number);

            btnSaveContact.setText("Update");
            dialogTitle.setText("Update Contact");
            btnDeleteContact.setVisibility(View.VISIBLE);

            // Update contact
            btnSaveContact.setOnClickListener(v -> {
                String name = edtName.getText().toString().trim();
                String number = edtNumber.getText().toString().trim();

                // Update in database
                ContactEntity updatedEntity = new ContactEntity(name, number);
                updatedEntity.id = contact.id;
                myDatabase.contactDao().updateContact(updatedEntity);

                // Update local list
                contact.name = name;
                contact.number = number;

                notifyDataSetChanged();

                dialog.dismiss();
            });

            // Delete contact
            btnDeleteContact.setOnClickListener(v -> {
                ContactEntity entity = new ContactEntity(contact.name, contact.number);
                entity.id = contact.id;

                // Delete from database
                myDatabase.contactDao().deleteContact(entity);

                // Remove from list
                arrContact.remove(position);

                // Sort alphabetically
                Collections.sort(arrContact, (c1, c2) -> c1.name.compareToIgnoreCase(c2.name));
                notifyDataSetChanged();

                dialog.dismiss();
            });

            dialog.show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return arrContact.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtname, txtnumber;
        ImageView img;
        LinearLayout llRow;

        public ViewHolder(View itemView) {
            super(itemView);
            txtname = itemView.findViewById(R.id.txtname);
            txtnumber = itemView.findViewById(R.id.txtnumber);
            img = itemView.findViewById(R.id.img);
            llRow = itemView.findViewById(R.id.llRow);
        }
    }
}
