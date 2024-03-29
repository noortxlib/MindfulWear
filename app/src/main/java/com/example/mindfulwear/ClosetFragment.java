package com.example.mindfulwear;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;


import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import com.google.firebase.database.ValueEventListener;

public class ClosetFragment extends Fragment {
    FloatingActionButton fab;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    GridView gridView;
    ArrayList<DataClass> dataList;
    MyAdapter adapter;

    SearchView searchView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_closet, container, false);

        fab = view.findViewById(R.id.fab);
        gridView = view.findViewById(R.id.gridView);
        dataList = new ArrayList<>();
        adapter = new MyAdapter(requireContext(), dataList);
        gridView.setAdapter(adapter);
        searchView = view.findViewById(R.id.search);
        searchView.clearFocus();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userID = currentUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userID).child("Items");


        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DataClass dataClass = dataSnapshot.getValue(DataClass.class);
                    if (dataClass != null) {
                        dataClass.setKey(dataSnapshot.getKey());

                        // Checks if item is favourite
                        Boolean isFavourite = dataSnapshot.child("favourite").getValue(Boolean.class);
                        if (isFavourite == true) {
                            dataClass.setFavourite(true);
                        } else {
                            dataClass.setFavourite(false);
                        }

                        dataList.add(dataClass);
                    }
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UploadActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    public void searchList(String text){
        ArrayList<DataClass> searchList = new ArrayList<>();
        for (DataClass dataClass: dataList){
            if(dataClass.getDataCategory().toLowerCase().contains(text.toLowerCase()) ||
            dataClass.getDataColour().toLowerCase().contains(text.toLowerCase())){
                searchList.add(dataClass);
            }
        }

        adapter.searchDataList(searchList);
    }
}