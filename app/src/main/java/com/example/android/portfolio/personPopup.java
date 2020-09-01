package com.example.android.portfolio;
/* =================================================================================================
 *              Project             :               Kaira
 *              Filename            :               personPopup.java
 *              Programmer          :               Austin Kempker
 *              Date                :               08/31/2020
 *              Description         :               This class takes user input and updates the user
 *                                                  information in the database.
 * ===============================================================================================*/
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.portfolio.helpers.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class personPopup extends Activity {

    //Reference to database
    DatabaseReference ref;

    String name, jobType, portfolio, linkedIn, experience;

    TextView tvName;
    TextView tvJobType;
    TextView tvPortfolio;
    TextView tvLinkedIn;
    Button updateButton;

    /* =============================================================================================
     *          Function        :       onCreate
     *
     *          Description     :       This function gets info using intent from JobFragment,
     *                                  displays it, then sends information back using intent after
     *                                  user updates it.
     *
     *          Arguments       :       Bundle savedInstanceState
     * ===========================================================================================*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.person_popup);

        //Get objects from UI
        tvName = findViewById(R.id.editTextName);
        tvJobType = findViewById(R.id.editTextJobType);
        tvPortfolio = findViewById(R.id.editTextPortfolio);
        tvLinkedIn = findViewById(R.id.editTextLinkedIn);
        updateButton = findViewById(R.id.buttonUpdate);

        //set OnclickListener to button
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = "" + tvName.getText().toString().trim();
                jobType = "" + tvJobType.getText().toString().trim();
                portfolio = "" + tvPortfolio.getText().toString().trim();
                linkedIn = "" + tvLinkedIn.getText().toString().trim();

                User user = new User(name, jobType, portfolio, linkedIn);

                //push value to database
                ref = FirebaseDatabase.getInstance().getReference();
                ref.child("Users").child(user.getUserID()).setValue(user);
                finish();
            }
        });




        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width= dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .8));

    }
}
