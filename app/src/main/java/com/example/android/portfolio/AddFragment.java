package com.example.android.portfolio;

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
import com.example.android.portfolio.helpers.Validation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

                //Push values to database
                ref.child(user.getUid()).child(job.getJobID()).setValue(job); //ref.child(user.getUid()).push().setValue(job);
                Toast.makeText(AddFragment.this.getActivity(), "Job Added!", Toast.LENGTH_LONG);

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
