package com.example.mindfulwear;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment {

    Button button;
    FirebaseUser user;
    TextView user_email, clothingItemsNumber, favouriteItemsNumber;
    DatabaseReference databaseReference;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();
        user_email = view.findViewById(R.id.user_details);
        button = view.findViewById(R.id.logout);
        clothingItemsNumber= view.findViewById(R.id.clothingItemsNumber);
        favouriteItemsNumber = view.findViewById(R.id.favouriteItemsNumber);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        if (user ==  null){
            Intent intent = new Intent(getContext(), Login.class);
            startActivity(intent);
            getActivity().finish();
        } else {
            user_email.setText(user.getEmail());
            getClothingItemsCount();
            getFavoriteItemsCount();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), Login.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }

    private void getClothingItemsCount() {
        DatabaseReference itemsReference = FirebaseDatabase.getInstance().getReference("Users")
                .child(user.getUid()).child("Items");

        itemsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        long count = dataSnapshot.getChildrenCount();
                        clothingItemsNumber.setText(String.valueOf(count));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle database error
                    }
                });
    }

    private void getFavoriteItemsCount() {
        DatabaseReference favouriteReference = FirebaseDatabase.getInstance().getReference("Users")
                .child(user.getUid()).child("Items");

        favouriteReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count = 0;
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    if (itemSnapshot.child("favourite").getValue(Boolean.class)) {
                        count++;
                    }
                }
                favouriteItemsNumber.setText(String.valueOf(count));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
    }
}
