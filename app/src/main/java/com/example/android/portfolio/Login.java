package com.example.android.portfolio;
/* =================================================================================================
 *              Project             :               Kaira
 *              Filename            :               Login.java
 *              Programmer          :               Austin Kempker
 *              Date                :               08/31/2020
 *              Description         :               This class takes user input of email and
 *                                                  password and verifies it against the firebase
 *                                                  backend, if successful it logs the user into
 *                                                  their account.
 * ===============================================================================================*/
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.portfolio.helpers.Validation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    //Create variables for each of the GUI objects
    EditText editTextEmail, editTextPassword;
    Button buttonLogin;
    TextView textViewRegisterFromLogin;
    ProgressBar progressBar;

    //Create an object to reference for the database
    FirebaseAuth fAuth;

    Validation validation = new Validation();

    /* =============================================================================================
     *          Function        :       onCreate
     *
     *          Description     :       This function takes login credentials from a user and
     *                                  verifies it against the firebase database.
     *
     *          Arguments       :       Bundle savedInstanceState
     * ===========================================================================================*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Set variables above to GUI objects
        editTextEmail = findViewById(R.id.editTextJobName);
        editTextPassword = findViewById(R.id.editTextWebsite);
        buttonLogin = findViewById(R.id.buttonAdd);
        textViewRegisterFromLogin = findViewById(R.id.textViewRegisterFromLogin);
        progressBar = findViewById(R.id.progressBar2);

        //Get the instance of the database
        fAuth = FirebaseAuth.getInstance();


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //What happens when the login button is clicked
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                //Here we will be using the helper class to ensure the data entered is valid.
                if (validation.isEmail(email) && !validation.isEmpty(email)) {
                    if (validation.isValidPassword(password) == 0) {
                        fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()) {
                                    Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                }
                                else {
                                    Toast.makeText(Login.this, "Email and Password don't match our records.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        //Not a valid password (Check number for cases)
                        if (validation.isValidPassword(password) == 1) {
                            //Password too short
                            editTextPassword.setError("Password must be 8 characters long");
                        } else if (validation.isValidPassword(password) == 2) {
                            //Password doesn't have a number in it
                            editTextPassword.setError("Password must contain a number");
                        } else if (validation.isValidPassword(password) == 3) {
                            //Password doesn't have an uppercase
                            editTextPassword.setError("Password must contain an uppercase letter");
                        }
                    }
                } else {
                    //Not valid email
                    editTextEmail.setError("Invalid Email");
                }
            }
        });

        textViewRegisterFromLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //What happens when the register text is clicked
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });


    }
}