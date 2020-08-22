package com.example.android.portfolio;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.android.portfolio.helpers.Job;
import com.example.android.portfolio.helpers.JobsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

import static com.example.android.portfolio.MainActivity.mJobs;

public class ListFragment extends Fragment {

    Job job = new Job();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mJobs.clear();

        final View view =  inflater.inflate(R.layout.fragment_list, container, false);

        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final ListView listView = (ListView) view.findViewById(R.id.list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), jobPopup.class);
                intent.putExtra("EXTRA_COMPANY_NAME", mJobs.get(i).getCompanyName());
                intent.putExtra("EXTRA_JOB_NAME", mJobs.get(i).getJobName());
                intent.putExtra("EXTRA_LOCATION", i);
                startActivityForResult(intent, 1);
            }
        });


        FirebaseDatabase.getInstance().getReference(uid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                job = snapshot.getValue(Job.class);
                mJobs.add(job);

                JobsAdapter adapter = new JobsAdapter(view.getContext(), mJobs);

                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
                /*for(int x = 0; x < mJobs.size(); x++) {
                    System.out.println(mJobs.get(x).getCompanyName());
                }*/
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                mJobs.clear();
                job = snapshot.getValue(Job.class);

                JobsAdapter adapter = new JobsAdapter(view.getContext(), mJobs);

                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}
