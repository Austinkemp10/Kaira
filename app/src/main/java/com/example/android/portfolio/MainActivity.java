package com.example.android.portfolio;
/* =================================================================================================
 *              Project             :               Kaira
 *              Filename            :               MainActivity.java
 *              Programmer          :               Austin Kempker
 *              Date                :               08/31/2020
 *              Description         :               This class is the landing page and logic page
 *                                                  for the Fragments(List,Add,Fragment) and helps
 *                                                  navigate through the BottomNavigationView. This
 *                                                  file also includes the onActivityResult function
 *                                                  where the logic behind the popup boxes
 *                                                  throughout the app is located.
 * ===============================================================================================*/
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.portfolio.helpers.Job;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    DatabaseReference ref;

    Button button;
    public static ArrayList<Job> mJobs = new ArrayList<>();

    /* =============================================================================================
     *          Function        :       onCreate
     *
     *          Description     :       This function gets the information from the layout and maps
     *                                  it to objects.
     *
     *          Arguments       :       Bundle savedInstanceState
     * ===========================================================================================*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
       bottomNav.setOnNavigationItemSelectedListener(navListener);
    }


    /* =============================================================================================
     *          Function        :       onActivityResult
     *
     *          Description     :       This function handles the logic for all of the pop-up
     *                                  windows throughout the application by receiving the
     *                                  requestCode and deciding which pop-up is creating the
     *                                  request, then handling the logic for that situation.
     *
     *          Arguments       :       int requestCode
     *                                  int resultCode
     *                                  Intent data
     * ===========================================================================================*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("Request code received from ListFragment is: " + requestCode);

        //Update the Job list after the user is done with the jobPopup
        if(resultCode == Activity.RESULT_OK && requestCode == 10001) {
            String result = data.getStringExtra("EXTRA_SELECTION");
            int location = data.getIntExtra("EXTRA_LOCATION", 0);
            mJobs.get(location).setJobState(result);

            //Get the current logged in user
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            //Set the data in the firebase database
            ref = FirebaseDatabase.getInstance().getReference();

            Map<String, Object> map = new HashMap<>();
            map.put("jobState", result);
            ref.child("Jobs").child(user.getUid()).child(mJobs.get(location).getJobID()).updateChildren(map);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ListFragment()).commit();
        }
        //Update the User information after the user is done with the personPopup
        else if (resultCode == Activity.RESULT_OK && requestCode == 10002) {
            ref = FirebaseDatabase.getInstance().getReference();

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PersonFragment()).commit();
        }
        //Update the User profile image after the user selects the image from gallery
        else if (resultCode == Activity.RESULT_OK && requestCode == 3) {

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference();
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            Uri filePath = null;
            filePath = data.getData();

            System.out.println("The file path of the image is: " + filePath);

           StorageReference ref = storageReference.child("images").child(uid);
           ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
               @Override
               public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                   Toast.makeText(MainActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                   getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PersonFragment()).commit();
               }
           }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
                   Toast.makeText(MainActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
               }
           });
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch(item.getItemId()) {
                case R.id.nav_list:
                    selectedFragment = new ListFragment();
                    break;
                case R.id.nav_add:
                    selectedFragment = new AddFragment();
                    break;
                case R.id.nav_person:
                    selectedFragment = new PersonFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    selectedFragment).commit();

            return true;
        }
    };

}