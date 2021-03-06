package com.example.android.portfolio;
/* =================================================================================================
 *              Project             :               Kaira
 *              Filename            :               jobPopup.java
 *              Programmer          :               Austin Kempker
 *              Date                :               08/31/2020
 *              Description         :               This class gets String extras passed using
 *                                                  Intent from the JobFragment it is called from.
 *                                                  Then it gives the user the option to change the
 *                                                  current status of the job. Once it is selected
 *                                                  the status of the job as well as the location of
 *                                                  the job is passed back to the JobFragment, where
 *                                                  the updates will be seen.
 * ===============================================================================================*/
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class jobPopup extends Activity {

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

        setContentView(R.layout.job_detail_popup);

        TextView textViewCompanyName = findViewById(R.id.textViewCompanyName);
        TextView textViewJobName = findViewById(R.id.textViewJobName);
        final Spinner spinnerJobStatus = findViewById(R.id.spinnerChangeStatus);
        Button buttonClose = findViewById(R.id.buttonClose);

        String companyName = getIntent().getStringExtra("EXTRA_COMPANY_NAME");
        String jobName = getIntent().getStringExtra("EXTRA_JOB_NAME");
        final int location = getIntent().getIntExtra("EXTRA_LOCATION", 0);

        textViewCompanyName.setText(companyName);
        textViewJobName.setText(jobName);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.job_status_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJobStatus.setAdapter(adapter);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width= dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .8));

        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selection = (String) spinnerJobStatus.getSelectedItem();

                Intent intent = new Intent();
                intent.putExtra("EXTRA_SELECTION", selection);
                intent.putExtra("EXTRA_LOCATION", location);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }
}

