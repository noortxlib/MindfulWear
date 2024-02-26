package com.example.mindfulwear;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class EventEditActivity extends AppCompatActivity {
    ImageView jacketImageView, topImageView, bottomImageView, shoesImageView;
    TextView eventDateTV;
    DatabaseReference databaseReference;
    ArrayList<DataClass> clothingItems;
    Button saveEventAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);

        jacketImageView = findViewById(R.id.jacketImageView);
        topImageView = findViewById(R.id.topImageView);
        bottomImageView = findViewById(R.id.jeansImageView);
        shoesImageView = findViewById(R.id.shoesImageView);
        eventDateTV = findViewById(R.id.eventDateTV);
        saveEventAction = findViewById(R.id.saveEventAction);

        eventDateTV.setText(CalenderUtils.formattedDate(CalenderUtils.selectedDate));
        clothingItems = new ArrayList<>();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userID = currentUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userID).child("Items");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DataClass dataClass = dataSnapshot.getValue(DataClass.class);
                    if (dataClass != null) {
                        clothingItems.add(dataClass);
                    }
                }
                generateOutfit(clothingItems);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EventEditActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateOutfit(ArrayList<DataClass> clothingItems) {
        if (!clothingItems.isEmpty()) {
            Random random = new Random();
            DataClass jacket = getRandomItemByCategory(clothingItems, Arrays.asList("jacket", "coat", "cardigan"), random);
            DataClass top = getRandomItemByCategory(clothingItems, Arrays.asList("top", "shirt", "jumper", "sweatshirt", "hoodie"), random);
            DataClass bottom = getRandomItemByCategory(clothingItems, Arrays.asList("bottom", "jeans", "skirt", "shorts", "trousers"), random);
            DataClass shoes = getRandomItemByCategory(clothingItems, Arrays.asList("shoes", "trainers", "heels"), random);

            loadImageIntoImageView(jacket.getDataImage(), jacketImageView);
            loadImageIntoImageView(top.getDataImage(), topImageView);
            loadImageIntoImageView(bottom.getDataImage(), bottomImageView);
            loadImageIntoImageView(shoes.getDataImage(), shoesImageView);

            saveEventAction.setOnClickListener(v -> {
                LocalDate selectedDate = CalenderUtils.selectedDate;
                String jacketUrl = jacket.getDataImage();
                String topUrl = top.getDataImage();
                String bottomUrl = bottom.getDataImage();
                String shoesUrl = shoes.getDataImage();

                Event outfitEvent = new Event(selectedDate, jacketUrl, topUrl, bottomUrl, shoesUrl);
                saveOutfitToDatabase(outfitEvent);
                Event.eventsList.add(outfitEvent);
            });
        }
    }

    private DataClass getRandomItemByCategory(ArrayList<DataClass> items, List<String> categoryType, Random random) {
        ArrayList<DataClass> filteredItems = new ArrayList<>();
        for (DataClass item : items) {
            String category = item.getDataCategory().toLowerCase();
            for (String keyword : categoryType) {
                if (category.contains(keyword.toLowerCase())) {
                    filteredItems.add(item);
                    break;
                }
            }
        }
        if (filteredItems.isEmpty()) {
            return null;
        } else {
            return filteredItems.get(random.nextInt(filteredItems.size()));
        }
    }

    private void loadImageIntoImageView(String imageUrl, ImageView imageView) {
        Glide.with(this)
                .load(imageUrl)
                .into(imageView);
    }

    private void saveOutfitToDatabase(Event event) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userID = currentUser.getUid();

        DatabaseReference outfitsReference = FirebaseDatabase.getInstance().getReference("Users").child(userID).child("Outfits");
        String eventId = outfitsReference.push().getKey();

        Map<String, Object> eventValues = new HashMap<>();
        eventValues.put("date", event.getDate().toString());
        eventValues.put("jacketImageURL", event.getJacketImageURL());
        eventValues.put("topImageURL", event.getTopImageURL());
        eventValues.put("bottomImageURL", event.getBottomImageURL());
        eventValues.put("shoeImageURL", event.getShoeImageURL());

        outfitsReference.child(eventId).setValue(eventValues)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(EventEditActivity.this, "Outfit saved successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(EventEditActivity.this, "Failed to save outfit", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
