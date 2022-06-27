package com.example.bucketlistcurator;

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


    public CustomAdapter(ListActivity listActivity, List<Event> eventList) {
        this.listActivity = listActivity;
        this.eventList = eventList;
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

                //show data in toast
                String title = eventList.get(position).getTitle();
                String description = eventList.get(position).getDescription();
               // Toast.makeText(listActivity, title+"\n"+description,Toast.LENGTH_SHORT).show();
                Snackbar.make(view, title+"\n"+description , Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }

            @Override
            public void onItemLongClick(View view, int position) {
                // called when the user long clicks item
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    //bind views / set data
        holder.mTitle.setText(eventList.get(position).getTitle());
        holder.mDescription.setText(eventList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
}
