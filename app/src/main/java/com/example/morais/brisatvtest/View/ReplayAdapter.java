package com.example.morais.brisatvtest.View;

import android.content.Intent;
import android.graphics.Movie;
import android.os.Bundle;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.telecom.Connection;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.morais.brisatvtest.Model.ReplayChannel;
import com.example.morais.brisatvtest.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReplayAdapter extends RecyclerView.Adapter<ReplayAdapter.ReplayViewHolder>{

    private ArrayList<ReplayChannel> dataSet;


    public static class ReplayViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ReplayViewHolder(View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.replay_channel);
        }

    }

    public ReplayAdapter (ArrayList<ReplayChannel> data) {
        this.dataSet = data;
    }


    @Override
    public ReplayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.replay_channel_card, parent, false);

        view.setOnClickListener(ReplayFragment.myOnClickListener);

        ReplayViewHolder myViewHolder = new ReplayViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(ReplayViewHolder holder, int position) {
        ImageView imageView = holder.imageView;
        imageView.setImageResource(dataSet.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
