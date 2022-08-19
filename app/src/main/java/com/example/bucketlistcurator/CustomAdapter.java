package com.example.bucketlistcurator;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<ViewHolder> {

    ListActivity listActivity;
    List<Event> eventList;
    private final Context mContext;

    public CustomAdapter(ListActivity listActivity, List<Event> eventList,Context context) {
        this.listActivity = listActivity;
        this.eventList = eventList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemView);
        //handle item clicks here
        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // called when user clicks item

               Event event =  eventList.get(position);
                Intent scanIntent = new Intent(mContext, ScanTicketActivity.class);
                scanIntent.putExtra("eventId", event.getId());
                mContext.startActivity(scanIntent);

            }

            @Override
            public void onItemLongClick(View view, int position) {
                //show data in toast
                String title = eventList.get(position).getTitle();
                String description = eventList.get(position).getInfo();
                // called when the user long clicks item
                Snackbar.make(view, title+"\n"+description , Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    //bind views / set data
        holder.mTitle.setText(eventList.get(position).getTitle());
        holder.mDescription.setText(eventList.get(position).getInfo());
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
}
