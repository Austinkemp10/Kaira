package com.example.android.portfolio;
/* =================================================================================================
 *              Project             :               Kaira
 *              Filename            :               Register.java
 *              Programmer          :               Austin Kempker
 *              Date                :               08/31/2020
 *              Description         :               This class validates user input to ensure it
 *                                                  matches our criteria then logs the user in the
 *                                                  firebase database.
 * ===============================================================================================*/
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.portfolio.helpers.*;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

    //Create variables for the GUI objects
    EditText editTextEmail, editTextPassword, editTextConfirmPassword;
    Button buttonLogin;
    TextView textViewLoginFromRegister;
    ProgressBar progressBar;

    //Create variable for the Firebase Authentication
    FirebaseAuth fAuth;

    //Create object to help with validation
    Validation validation = new Validation();


    /* =============================================================================================
     *          Function        :       onCreate
     *
     *          Description     :       This function sets the onClickListener to the register
     *                                  Button and adds the user to firebase when it is clicked.
     *
     *          Arguments       :       Bundle savedInstanceState
     * ===========================================================================================*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Here we set the variables created above to objects in the GUI
        editTextEmail = findViewById(R.id.editTextJobName);
        editTextPassword = findViewById(R.id.editTextWebsite);
        editTextConfirmPassword = findViewById(R.id.editTextContactName);
        buttonLogin = findViewById(R.id.buttonAdd);
        textViewLoginFromRegister = findViewById(R.id.textViewLoginFromRegister);
        progressBar = findViewById(R.id.progressBar);

        //Here we set the instance variable for the firebase Authentication
        fAuth = FirebaseAuth.getInstance();


        if(fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String confirmPassword = editTextConfirmPassword.getText().toString().trim();

                //Here we will be using the helper class to ensure the data entered is valid.
                if(validation.isEmail(email) && !validation.isEmpty(email)) {
                    if(validation.isValidPassword(password) == 0) {
                        if(validation.isMatch(password, confirmPassword)) {
                            //Handle the given data it represents a valid account
                            progressBar.setVisibility(View.VISIBLE);

                            //Register user in firebase
                            fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    //If it is successful inform the user, if not tell the user why
                                    if(task.isSuccessful()) {
                                        //Tell the user the account was created.
                                        Toast.makeText(Register.this, "Account Created", Toast.LENGTH_SHORT).show();
                                        FirebaseUser user = fAuth.getCurrentUser();

                                        //Update the user interface with the logged in user information
                                        //Possibly pass the user to

                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    } else {
                                        //Something went wrong with account creation tell the user
                                        try {
                                            throw task.getException();
                                        } catch(FirebaseAuthWeakPasswordException e) {
                                            editTextPassword.setError("Password is too weak!");
                                            editTextPassword.requestFocus();
                                        } catch(FirebaseAuthInvalidCredentialsException e) {
                                            editTextEmail.setError("Invalid Email!");
                                            editTextEmail.requestFocus();
                                        } catch(FirebaseAuthUserCollisionException e) {
                                            editTextEmail.setError("User already exists!");
                                            editTextEmail.requestFocus();
                                        } catch(Exception e) {
                                            Log.e("Register", e.getMessage());
                                        }
                                    }
                                }
                            });
                        }
                        else {
                            //Passwords don't match
                            editTextPassword.setError("Password and Confirm Password must match");
                            editTextConfirmPassword.setError("Password and Confirm Password must match");
                        }
                    }
                    else {
                        //Not a valid password (Check number for cases)
                        if(validation.isValidPassword(password) == 1) {
                            //Password too short
                            editTextPassword.setError("Password must be 8 characters long");
                        }
                        else if(validation.isValidPassword(password) == 2) {
                            //Password doesn't have a number in it
                            editTextPassword.setError("Password must contain a number");
                        }
                        else if(validation.isValidPassword(password) == 3) {
                            //Password doesn't have an uppercase
                            editTextPassword.setError("Password must contain an uppercase letter");
                        }
                    }
                }
                else {
                    //Not valid email
                    editTextEmail.setError("Invalid Email");
                }
                validation.isMatch(password, confirmPassword);
            }
        });

        textViewLoginFromRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Take us to the Login activity if they click login
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
    }
}