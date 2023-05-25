package com.example.listed;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LinkAdapter extends RecyclerView.Adapter<LinkAdapter.ViewHolder>{

    //Initialise the list item here
    private ArrayList<LinkModel> arrayListTopLinks;
//    private ArrayList<LinkModel> arrayListRecentLinks;
    //Creating context for toast
    private Context context;

    public LinkAdapter(ArrayList<LinkModel> arrayListTopLinks, Context context) {
        this.arrayListTopLinks = arrayListTopLinks;
        this.context = context;

//        arrayListRecentLinks = new ArrayList<>(arrayListRecentLinks);
    }

    //View holder(it calls the created recycler View)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_link,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    // data setting is done here
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.textViewLinkTitle.setText(arrayListTopLinks.get(position).getTitle());
        holder.textViewTimesAgo.setText(arrayListTopLinks.get(position).getTimesAgo());
        holder.textViewTotalClicks.setText(arrayListTopLinks.get(position).getTotalClicks());
        holder.textViewSmartLink.setText(arrayListTopLinks.get(position).getSmartLink());
        Picasso.with(context).load(arrayListTopLinks.get(position).getImageUrl()).into(holder.imageViewLink);

    }

    @Override
    public int getItemCount() {
        return arrayListTopLinks.size();
    }

    //Every view inside the recycler view is declared and initialised here
    public class ViewHolder extends RecyclerView.ViewHolder{
        //Declaration
        private TextView textViewLinkTitle, textViewTimesAgo,textViewTotalClicks, textViewSmartLink;
        private ImageView imageViewLink;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewLinkTitle = itemView.findViewById(R.id.tv_title);
            textViewTimesAgo = itemView.findViewById(R.id.tv_times_ago);
            textViewTotalClicks = itemView.findViewById(R.id.tv_total_clicks);
            textViewSmartLink = itemView.findViewById(R.id.tv_smart_link);
            imageViewLink =  itemView.findViewById(R.id.img_view_link);
        }
    }
}
