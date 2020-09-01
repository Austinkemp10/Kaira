package com.example.android.portfolio.helpers;
/* =================================================================================================
 *              Project             :               Kaira
 *              Filename            :               Job.java
 *              Programmer          :               Austin Kempker
 *              Date                :               08/31/2020
 *              Description         :               This is a helper class used to create functions
 *                                                  used to validate user input.
 * ===============================================================================================*/
public class Validation {

    public boolean isEmpty(String s) {
        if(s.length() == 0) {
            return true;
        }
        return false;
    }
    public boolean isEmail(String s) {
        return s.matches("^[a-zA-Z0-9]+@[a-zA-Z0-9]+(.[a-zA-Z]{2,})$");
    }

    public int isValidPassword(String s) {
        /* Need to return an integer so we can see what is missing from the password
         *      Key:
         *          0   :   valid
         *          1   :   too short
         *          2   :   missing number
         *          3   :   missing uppercase
         */
        if(s.length() < 8) {
            return 1;
        }
        else if(!s.matches(".*\\d.*")) {
            return 2;
        }
        else if(s.equals(s.toLowerCase())) {
            return 3;
        }
        else {
            return 0;
        }

    }

    public boolean isMatch(String a, String b) {
        if(a.equals(b)) {
            return true;
        }
        return false;
    }
}
