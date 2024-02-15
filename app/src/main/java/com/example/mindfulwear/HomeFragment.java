package com.example.mindfulwear;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private ProgressBar progressBar;
    private ArrayList<String> imageList;
    private RecyclerView recyclerView;
    private ImageAdapter adapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        progressBar = view.findViewById(R.id.progressBar);
        recyclerView = view.findViewById(R.id.recyclerView);
        imageList = new ArrayList<>();
        adapter = new ImageAdapter(imageList, requireContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Inspiration Images");

        progressBar.setVisibility(View.VISIBLE);
        storageReference.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference fileRef : listResult.getItems()) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imageList.add(uri.toString());
                            adapter.notifyDataSetChanged(); // Notify adapter of data change
                            Log.d("item", uri.toString());
                        }
                    });
                }
                recyclerView.setAdapter(adapter); // Set adapter once data is loaded
                progressBar.setVisibility(View.GONE);
            }
        });

        return view;
    }
}
