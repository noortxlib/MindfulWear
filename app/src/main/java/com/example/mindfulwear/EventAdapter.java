package com.example.mindfulwear;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.List;

public class EventAdapter extends ArrayAdapter<Event> {
    public EventAdapter(@NonNull Context context, List<Event> events) {
        super(context, 0, events);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Event event = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_cell, parent, false);
        }

        ImageView imageViewJacket = convertView.findViewById(R.id.imageViewJacket);
        ImageView imageViewTop = convertView.findViewById(R.id.imageViewTop);
        ImageView imageViewBottom = convertView.findViewById(R.id.imageViewBottom);
        ImageView imageViewShoes = convertView.findViewById(R.id.imageViewShoes);

        if (event != null) {
            loadImageIntoImageView(event.getJacketImageURL(), imageViewJacket);
            loadImageIntoImageView(event.getTopImageURL(), imageViewTop);
            loadImageIntoImageView(event.getBottomImageURL(), imageViewBottom);
            loadImageIntoImageView(event.getShoeImageURL(), imageViewShoes);
        }

        return convertView;
    }

    private void loadImageIntoImageView(String imageUrl, ImageView imageView) {
        Glide.with(getContext())
                .load(imageUrl)
                .into(imageView);
    }
}