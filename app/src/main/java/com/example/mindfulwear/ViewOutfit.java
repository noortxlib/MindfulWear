package com.example.mindfulwear;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View; // Import View class

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewOutfit extends AppCompatActivity {

    private ImageView jacketImageView, topImageView, bottomImageView, shoesImageView;
    private TextView eventDateTV;
    private DatabaseReference outfitsReference;
    private String userID; // Initialize userID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_outfit);

        // Initialize views
        jacketImageView = findViewById(R.id.jacketImageView);
        topImageView = findViewById(R.id.topImageView);
        bottomImageView = findViewById(R.id.jeansImageView); // Corrected ID
        shoesImageView = findViewById(R.id.shoesImageView);
        eventDateTV = findViewById(R.id.eventDateTV);

        // Get selected date from intent extra
        String selectedDate = getIntent().getStringExtra("selectedDate");
        eventDateTV.setText(selectedDate);

        // Get userID from Firebase
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Query database for outfits for the selected date
        outfitsReference = FirebaseDatabase.getInstance().getReference("Users").child(userID).child("Outfits");
        outfitsReference.orderByChild("date").equalTo(selectedDate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Check if any outfits found for the selected date
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Retrieve outfit details from database
                        String jacketUrl = snapshot.child("jacketImageURL").getValue(String.class);
                        String topUrl = snapshot.child("topImageURL").getValue(String.class);
                        String bottomUrl = snapshot.child("bottomImageURL").getValue(String.class);
                        String shoesUrl = snapshot.child("shoeImageURL").getValue(String.class);

                        // Load outfit images into ImageViews using Glide
                        Glide.with(ViewOutfit.this).load(jacketUrl).into(jacketImageView);
                        Glide.with(ViewOutfit.this).load(topUrl).into(topImageView);
                        Glide.with(ViewOutfit.this).load(bottomUrl).into(bottomImageView);
                        Glide.with(ViewOutfit.this).load(shoesUrl).into(shoesImageView);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ViewOutfit.this, "Failed to load outfit data", Toast.LENGTH_SHORT).show();
            }
        });

        // Set click listener for the back button
        findViewById(R.id.backAction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Finish the activity when back button is clicked
            }
        });
    }
}

