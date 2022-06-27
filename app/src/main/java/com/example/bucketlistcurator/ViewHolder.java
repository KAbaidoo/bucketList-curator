package com.example.bucketlistcurator;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {
    TextView mTitle, mDescription;
    View mView;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        mView = itemView;

        //set click listener
       itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());
           }
       });

       //set long click listener
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mClickListener.onItemLongClick(v, getAdapterPosition());
                return true;
            }
        });

        //initialize views with model_layout
        mTitle = itemView.findViewById(R.id.textView_title);
        mDescription = itemView.findViewById(R.id.textView_description);
    }
    private ViewHolder.ClickListener mClickListener;


    public interface ClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
    public void setOnClickListener (ViewHolder.ClickListener clickListener){
        mClickListener = clickListener;
    }
}
