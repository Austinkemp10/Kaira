package com.example.android.portfolio;
/* =================================================================================================
 *              Project             :               Kaira
 *              Filename            :               ListFragment.java
 *              Programmer          :               Austin Kempker
 *              Date                :               08/31/2020
 *              Description         :               This class takes the values in the job database
 *                                                  for the logged in user id and displays them with
 *                                                  a color code associated with the status of the
 *                                                  job. When a specific job is clicked it opens up
 *                                                  a popup where the user can change the status of
 *                                                  a job.
 *
 * ===============================================================================================*/

import android.content.Intent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.android.portfolio.helpers.Job;
import com.example.android.portfolio.helpers.JobsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.android.portfolio.MainActivity.mJobs;

public class ListFragment extends Fragment {

    final int requestCodeList = 10001;
    Job job = new Job();


    /* =============================================================================================
     *          Function        :       onCreateView
     *
     *          Description     :       This function gets the information from the layout and maps
     *                                  it to objects. Then sends information to the jobPopup
     *                                  fragment when a job is clicked. (Using Intent)
     *
     *          Arguments       :       LayoutInflater inflater
     *                                  ViewGroup container
     *                                  Bundle savedInstanceState
     * ===========================================================================================*/
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mJobs.clear();

        final View view =  inflater.inflate(R.layout.fragment_list, container, false);

        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final ListView listView = (ListView) view.findViewById(R.id.list);

        //Send information to the jobPopup class so that it can display job information on the popup
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), jobPopup.class);
                intent.putExtra("EXTRA_COMPANY_NAME", mJobs.get(i).getCompanyName());
                intent.putExtra("EXTRA_JOB_NAME", mJobs.get(i).getJobName());
                intent.putExtra("EXTRA_LOCATION", i);
                getActivity().startActivityForResult(intent, requestCodeList);
            }
        });


        //Update the database with the job when things are added and changed then refresh view
        FirebaseDatabase.getInstance().getReference("Jobs/" + uid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                job = snapshot.getValue(Job.class);
                mJobs.add(job);

                JobsAdapter adapter = new JobsAdapter(view.getContext(), mJobs);

                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
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
