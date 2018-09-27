package com.example.morais.brisatvtest.View;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.morais.brisatvtest.Model.ReplayChannel;
import com.example.morais.brisatvtest.R;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class ReplayFragment extends Fragment {

    static View.OnClickListener myOnClickListener;
    private static RecyclerView recyclerView;
    private static ArrayList<ReplayChannel> replayChannels;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_replay, container, false);

        myOnClickListener = new MyOnClickListener(view.getContext());
        recyclerView = view.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(view.getContext(), LinearLayout.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        replayChannels = new ArrayList<ReplayChannel>();
        for (int i = 0; i < 10; i++) {
            replayChannels.add(new ReplayChannel());
        }

        adapter = new ReplayAdapter(replayChannels);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private static class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(this.context, "Clicou", Toast.LENGTH_SHORT).show();
//            removeItem(v);
        }

    }



}
