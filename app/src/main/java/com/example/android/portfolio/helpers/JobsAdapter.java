package com.example.android.portfolio.helpers;
/* =================================================================================================
 *              Project             :               Kaira
 *              Filename            :               JobAdapter.java
 *              Programmer          :               Austin Kempker
 *              Date                :               08/31/2020
 *              Description         :               This is a helper class used to create the layout
 *                                                  for the jobs on the ListFragment page. The color
 *                                                  and design is decided here.
 *
 * ===============================================================================================*/
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.portfolio.R;

import java.util.ArrayList;

public class JobsAdapter extends ArrayAdapter<Job> {
    public JobsAdapter(Context context, ArrayList<Job> jobs) {
        super(context, 0, jobs);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Job job = getItem(position);
        String state;
        if(job != null) {
            state = job.getJobState();
        } else {
            return convertView;
        }

        if(convertView == null) {
            if (state.equals("Applied")) {
                //Set color to blue
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.blue_item_job, parent, false);
            } else if (state.equals("Accepted")) {
                //Set color to green
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.green_item_job, parent, false);
            } else if (state.equals("Rejected")) {
                //Set color to red
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.red_item_job, parent, false);
            } else if (state.equals("Interview")) {
                //Set color Yellow
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.yellow_item_job, parent, false);
            } else {
                //Leave color background
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_job, parent, false);
            }

        }

        TextView textViewCompanyName = (TextView) convertView.findViewById(R.id.textViewCompanyName);
        TextView textViewJobName = (TextView) convertView.findViewById(R.id.textViewJobName);
        TextView textViewStatus = (TextView) convertView.findViewById(R.id.textViewJobStatus);

        textViewCompanyName.setText(job.getCompanyName());
        textViewJobName.setText(job.getJobName());
        textViewStatus.setText(job.getJobState());

        return convertView;
    }
}
