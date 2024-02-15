package com.example.mindfulwear;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MyAdapter extends BaseAdapter {

    private Context context;
    private List<DataClass> dataList;
    LayoutInflater layoutInflater;


    public MyAdapter(Context context, List<DataClass> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    public void searchDataList(List<DataClass> searchList){
        dataList = searchList;
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if (layoutInflater == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (view == null) {
            view = layoutInflater.inflate(R.layout.grid_item, null);
            holder = new ViewHolder();
            holder.gridImage = view.findViewById(R.id.gridImage);
            holder.favouriteIcon = view.findViewById(R.id.favoriteIcon);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        DataClass currentItem = dataList.get(i);

        Glide.with(context).load(currentItem.getDataImage()).into(holder.gridImage);

        if (currentItem.isFavourite()) {
            holder.favouriteIcon.setImageResource(R.drawable.heart_red);
        } else {
            holder.favouriteIcon.setImageResource(R.drawable.heart);
        }

        // Handle favourite button click
        holder.favouriteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentItem.setFavourite(!currentItem.isFavourite());
                updateFavouriteState(currentItem);
            }
        });

        return view;
    }

    private void updateFavouriteState(DataClass item) {

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                .child(userID).child("Items").child(item.getKey()).child("favourite");

        databaseReference.setValue(item.isFavourite())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            notifyDataSetChanged();
                        } else {
                            Toast.makeText(context, "Failed to update favourite state", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    static class ViewHolder {
        ImageView gridImage;
        ImageView favouriteIcon;
    }

}