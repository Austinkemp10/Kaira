package com.example.android.portfolio;
/* =================================================================================================
 *              Project             :               Kaira
 *              Filename            :               PersonFragment.java
 *              Programmer          :               Austin Kempker
 *              Date                :               08/31/2020
 *              Description         :               This class gets the user information from the
 *                                                  database and displays it to the user. From this
 *                                                  Fragment you are able to update the user info,
 *                                                  update the user profile picture, and log out of
 *                                                  the application.
 * ===============================================================================================*/
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.android.portfolio.helpers.User;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonFragment extends Fragment {
    DatabaseReference ref;

    CircleImageView profile_picture;
    TextView textViewName;
    TextView textViewJobType;
    TextView textViewPortfolio;
    TextView textViewLinkedIn;
    Button buttonUpdate;
    Button buttonLogOut;

    /* =============================================================================================
     *          Function        :       onCreateView
     *
     *          Description     :       This function gets the information from the layout and maps
     *                                  it to objects. Then on user update option opens popup where
     *                                  user can update information. If user clicks profile picture
     *                                  the gallery opens up and they can change the photo.
     *
     *          Arguments       :       LayoutInflater inflater
     *                                  ViewGroup container
     *                                  Bundle savedInstanceState
     * ===========================================================================================*/
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person, container, false);

        profile_picture = view.findViewById(R.id.profile_image);
        textViewName = view.findViewById(R.id.textViewName);
        textViewJobType = view.findViewById(R.id.textViewJobType);
        textViewPortfolio = view.findViewById(R.id.textViewPortfolio);
        textViewLinkedIn = view.findViewById(R.id.textViewLinkedIn);
        buttonUpdate = view.findViewById(R.id.buttonUpdate);
        buttonLogOut = view.findViewById(R.id.buttonLogOut);

        //Get Data from database for user information
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageReference = storage.getReference("images/" + uid);
        final int requestCodePerson = 10002;
        ref = FirebaseDatabase.getInstance().getReference("Users/" + uid);

        //put received data into respective objects(set default values for null situation)
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                profile_picture.setImageDrawable(null);
                //Make checks for null image if so set the drawable option.
                try {
                    Glide.with(getContext()).using(new FirebaseImageLoader()).load(storageReference).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(profile_picture);
                } catch (Exception e) {
                    System.out.println(e);
                }

                if(user == null) {
                    textViewName.setText("Update Name");
                    textViewJobType.setText("Update Job Type");
                    textViewPortfolio.setText("Update Portfolio");
                    textViewLinkedIn.setText("Update LinkedIn");
                    profile_picture.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_person_24));
                } else {
                    textViewName.setText(user.getUsername());
                    textViewJobType.setText(user.getJobType());
                    textViewPortfolio.setText(user.getPortfolio());
                    textViewLinkedIn.setText(user.getLinkedIn());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PersonFragment.this.getActivity(), "Profile failed to load!", Toast.LENGTH_LONG);
            }
        });


        //Set onclickListeners for Buttons
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), personPopup.class);
                getActivity().startActivityForResult(intent, requestCodePerson);
            }
        });


        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), Login.class);
                startActivity(intent);
            }
        });

        profile_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                getActivity().startActivityForResult(photoPickerIntent, 3);
            }
        });

        return view;
    }
}
