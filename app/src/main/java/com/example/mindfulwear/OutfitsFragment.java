package com.example.mindfulwear;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;

public class OutfitsFragment extends Fragment implements CalenderAdapter.OnItemListener {
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_outfits, container, false);

        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView);
        monthYearText = view.findViewById(R.id.monthYearTV);
        eventListView = view.findViewById(R.id.eventListView);
        setWeekView();

        Button previousWeekButton = view.findViewById(R.id.previousWeek);
        Button nextWeekButton = view.findViewById(R.id.nextWeek);
        Button newEvent = view.findViewById(R.id.newEvent);

        previousWeekButton.setOnClickListener(v -> previousWeekAction());
        nextWeekButton.setOnClickListener(v -> nextWeekAction());
        newEvent.setOnClickListener(v -> newEventAction());

        return view;
    }

    private void setWeekView() {
        monthYearText.setText(CalenderUtils.monthYearFromDate(CalenderUtils.selectedDate));
        ArrayList<LocalDate> days = CalenderUtils.daysInWeekArray(CalenderUtils.selectedDate);

        CalenderAdapter calendarAdapter = new CalenderAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        setEventAdapter();
    }

    public void previousWeekAction() {
        CalenderUtils.selectedDate = CalenderUtils.selectedDate.minusWeeks(1);
        setWeekView();
    }

    public void nextWeekAction() {
        CalenderUtils.selectedDate = CalenderUtils.selectedDate.plusWeeks(1);
        setWeekView();
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        CalenderUtils.selectedDate = date;
        setWeekView();
    }

    @Override
    public void onResume() {
        super.onResume();
        setEventAdapter();
    }

    private void setEventAdapter() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userID = currentUser.getUid();
        DatabaseReference outfitsReference = FirebaseDatabase.getInstance().getReference("Users").child(userID).child("Outfits");
        outfitsReference.orderByChild("date").equalTo(CalenderUtils.selectedDate.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Event> dailyEvents = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String jacketUrl = snapshot.child("jacketImageURL").getValue(String.class);
                    String topUrl = snapshot.child("topImageURL").getValue(String.class);
                    String bottomUrl = snapshot.child("bottomImageURL").getValue(String.class);
                    String shoesUrl = snapshot.child("shoeImageURL").getValue(String.class);

                    Event event = new Event(CalenderUtils.selectedDate, jacketUrl, topUrl, bottomUrl, shoesUrl);
                    dailyEvents.add(event);
                }

                EventAdapter eventAdapter = new EventAdapter(getContext(), dailyEvents);
                eventListView.setAdapter(eventAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to display outfit data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void newEventAction() {
        startActivity(new Intent(getContext(), EventEditActivity.class));
    }

}
