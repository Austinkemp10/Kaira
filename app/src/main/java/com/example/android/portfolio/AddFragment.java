package com.example.android.portfolio;
/* =================================================================================================
 *              Project             :               Kaira
 *              Filename            :               AddFragment.java
 *              Programmer          :               Austin Kempker
 *              Date                :               08/31/2020
 *              Description         :               This class allows the user to enter information
 *                                                  for a job and pushes it to the firebase database
 *                                                  categorized under:
 *                                                      Root
 *                                                          |___Jobs
 *                                                                  |___Current User ID
 *
 * ===============================================================================================*/
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.android.portfolio.helpers.Job;
import com.example.android.portfolio.helpers.Reminder;
import com.example.android.portfolio.helpers.Validation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.Context.ALARM_SERVICE;

public class AddFragment extends Fragment {
    //Database Reference
    DatabaseReference ref;

    //Create validation object
    Validation validation = new Validation();

    //Create Job object to pass into database
    Job job;

    //Component object creation
    EditText editTextCompanyName;
    EditText editTextJobName;
    EditText editTextWebsite;
    EditText editTextContactName;
    EditText editTextContactEmail;
    EditText editTextApplicationLink;
    Spinner dropdown;
    Button buttonAdd;
    String[] items = new String[]{"One Week", "Two Weeks", "One Month", "Never"};

    String companyName, jobName, website, contactName, contactEmail, applicationLink;
    int dropdownChoice;


    /* =============================================================================================
     *          Function        :       onCreateView
     *
     *          Description     :       This function gets the information from the layout and maps
     *                                  it to objects. Then handles the database logic once the
     *                                  button is clicked
     *
     *          Arguments       :       LayoutInflater inflater
     *                                  ViewGroup container
     *                                  Bundle savedInstanceState
     * ===========================================================================================*/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add, container, false);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        editTextCompanyName = (EditText) view.findViewById(R.id.editTextCompanyName);
        editTextJobName = (EditText) view.findViewById(R.id.editTextJobName);
        editTextWebsite = (EditText) view.findViewById(R.id.editTextWebsite);
        editTextContactName = (EditText) view.findViewById(R.id.editTextContactName);
        editTextContactEmail = (EditText) view.findViewById(R.id.editTextContactEmail);
        editTextApplicationLink = (EditText) view.findViewById(R.id.editTextApplicationLink);
        dropdown = (Spinner) view.findViewById(R.id.spinnerRemind);
        buttonAdd = (Button) view.findViewById(R.id.buttonAdd); 

        //Create the spinner and set values to it using the above array. Warnings here are for possible null values
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        ref = FirebaseDatabase.getInstance().getReference();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get data from GUI objects and put it into variables
                companyName = editTextCompanyName.getText().toString().trim();
                jobName = editTextJobName.getText().toString().trim();
                website = editTextWebsite.getText().toString().trim();
                contactName = editTextContactName.getText().toString().trim();
                contactEmail = editTextContactEmail.getText().toString().trim();
                applicationLink = editTextApplicationLink.getText().toString().trim();
                dropdownChoice = dropdown.getSelectedItemPosition();

                //Job object initialization
                job = new Job(companyName, jobName, website, contactName, contactEmail, applicationLink, dropdownChoice);

                //Set the notification time based on the dropdown choice
                long currentTime = System.currentTimeMillis();
                int interval;
                if(dropdownChoice == 0) {
                    interval = 7;
                } else if (dropdownChoice == 1) {
                    interval = 14;
                } else if (dropdownChoice == 2) {
                    interval = 30;
                } else {
                    interval = -1;
                }


                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    CharSequence name = "jobReminderChannel";
                    String description = "Channel built for Job Reminder";
                    int importance = NotificationManager.IMPORTANCE_DEFAULT;
                    NotificationChannel channel = new NotificationChannel("notifyJob", name, importance);
                    channel.setDescription(description);

                    NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
                    notificationManager.createNotificationChannel(channel);
                }


                Intent intent = new Intent(getContext(), Reminder.class);
                intent.putExtra("COMPANY_NAME", companyName);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);
                AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, currentTime + 1000/*currentTime + AlarmManager.INTERVAL_DAY * interval*/, pendingIntent);

                //Push values to database
                ref.child("Jobs").child(user.getUid()).child(job.getJobID()).setValue(job);
                if(interval >= 0) {
                    Toast.makeText(AddFragment.this.getActivity(), "Job added and reminder set!", Toast.LENGTH_LONG);
                }
                else {
                    Toast.makeText(AddFragment.this.getActivity(), "Job Added!", Toast.LENGTH_LONG);
                }

                //Clear the fields
                editTextCompanyName.setText("");
                editTextJobName.setText("");
                editTextWebsite.setText("");
                editTextContactName.setText("");
                editTextContactEmail.setText("");
                editTextApplicationLink.setText("");
            }
        });

        return view;
    }
}
