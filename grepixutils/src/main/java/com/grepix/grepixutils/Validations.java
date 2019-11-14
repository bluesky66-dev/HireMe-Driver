package com.grepix.grepixutils;

import android.content.Context;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

/**
 * Created by devin on 2017-10-10.
 */

public class Validations {


    public static boolean isValidateLogin(Context context, EditText Email, EditText Psw) {
        boolean isValid = true;
        if (Utils.net_connection_check(context)) {



            String Get_Email = Email.getText().toString();
            Get_Email = Get_Email.toLowerCase();
            String Get_Password = Psw.getText().toString();

            if (!Email.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                Email.setError(context.getResources().getString(R.string.required));
                isValid = false;
            } else {
                Email.setError(null);
            }
            if (Psw.getText().toString().length() == 0) {
                Psw.setError(context.getResources().getString(R.string.required));
                isValid = false;
            } else {
                Psw.setError(null);
            }



        } else {
          isValid= false;
            Toast.makeText(context, R.string.please_check_network, Toast.LENGTH_SHORT).show();
        }
        return isValid;

    }

    public static boolean isValidateRegister(Context context, EditText etfirst_name, EditText etlast_name, EditText email, EditText etmobile, EditText password, EditText repassword) {
        boolean isValid = true;
        if (Utils.net_connection_check(context)) {
            //  validate first name;
            if (etfirst_name.getText().toString().length() == 0) {
                etfirst_name.setError(context.getResources().getString(R.string.please_enter_your_First_Name));
                isValid = false;
            } else {
                etfirst_name.setError(null);
            }
            //  validate last name;
            if (etlast_name.getText().toString().length() == 0) {
                etlast_name.setError(context.getResources().getString(R.string.please_enter_your_last_Name));
                isValid = false;
            } else {
                etlast_name.setError(null);
            }

            //  validate email;
            if (!email.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                isValid = false;
                email.setError(context.getResources().getString((R.string.please_enter_your_valid_email_id)));
            } else {
                email.setError(null);
            }

            // mobile
            if (!isValidMobileNumber(context,etmobile.getText().toString())) {
                isValid = false;
                etmobile.setError(context.getResources().getString(R.string.please_enter_your_valid_number));
            } else {
                etmobile.setError(null);
            }
            // password
            if (password.getText().toString().length() < 6) {
                isValid = false;
                password.setError(context.getResources().getString(R.string.Password_should_be_at_least_4_char));
            } else {
                password.setError(null);
            }

            if (repassword.getText().toString().equals(password.getText().toString())) {
                repassword.setError(null);
            } else {
                isValid = false;
                repassword.setError(context.getResources().getString(R.string.password_not_matched));
            }

        }else{
            isValid=false;
        }

        return isValid;
    }

    private static boolean isValidMobileNumber(Context context, String pass) {
        if (pass != null || pass.length()<12 || pass.length()>6 ) {
            return true;
        }
        else {
            Toast.makeText(context, "Please enter the correct phone number", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public static boolean isValidateRecoverPassword(Context context, EditText Email) {
          boolean isValid=true;
        if (Utils.net_connection_check(context)) {
            String Get_email = Email.getText().toString();

            if (Email.getText().toString().equals("")) {
                isValid=false;
                Email.setError("Required");
            } else if (Get_email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+") && Get_email.length() > 0) {
                isValid=true;
            }
        }else{
            isValid=false;
        }
        return isValid;
    }

    public static boolean isValidateUpdateProfile(Context context, EditText etfname, EditText etlname, EditText etMob, Switch switchChngPswd, EditText etOldPswd, EditText etNewPswd, EditText etConfirmPswd,String oldPassword) {
        boolean isVaild = true;
        if(Utils.net_connection_check(context)){
            if (etfname.getText().toString().length() == 0) {
                isVaild = false;
                etfname.setError(context.getResources().getString(R.string.please_enter_your_First_Name));
            } else {
                etfname.setError(null);
            }
            if (etlname.getText().toString().length() == 0) {
                isVaild = false;
                etlname.setError(context.getResources().getString(R.string.please_enter_your_last_Name));
            } else {
                etlname.setError(null);
            }

            if (!isValidMobileNumber(context,etMob.getText().toString())) {
                etMob.setError(context.getResources().getString(R.string.please_enter_your_valid_number));
                isVaild = false;
            }

            else {
                etMob.setError(null);
            }


            if (switchChngPswd.isChecked()) {
                if (etOldPswd.getText().toString().length() == 0) {
                    isVaild = false;
                    etOldPswd.setError(context.getResources().getString(R.string.Old_Password));
                    // Toast.makeText(getApplicationContext(), "Old Password does not match", Toast.LENGTH_LONG).show();
                    return isVaild;
                } else {
                    etOldPswd.setError(null);
                }
                if (!etOldPswd.getText().toString().equals(oldPassword)) {
                    isVaild = false;
                    etOldPswd.setError(context.getResources().getString(R.string.old_Password_is_not_currect));
                    return isVaild;
                } else {
                    etOldPswd.setError(null);
                }
                if (etNewPswd.getText().toString().length() == 0) {
                    isVaild = false;
                    etNewPswd.setError(context.getResources().getString(R.string.enter_new_password));
                    return isVaild;
                } else {

                    etNewPswd.setError(null);
                }
                if (etNewPswd.getText().toString().length() < 6) {
                    etNewPswd.setError(context.getResources().getString(R.string.Password_should_be_at_least_4_char));
                    return false;
                } else {
                    etNewPswd.setError(null);
                }
                if (etConfirmPswd.getText().toString().length() == 0) {
                    isVaild = false;
                    etConfirmPswd.setError(context.getResources().getString(R.string.enter_new_confirmed));
                    return isVaild;
                } else {
                    etConfirmPswd.setError(null);
                }
                if (!etNewPswd.getText().toString().equals(etConfirmPswd.getText().toString())) {
                    isVaild = false;
                    etConfirmPswd.setError(context.getResources().getString(R.string.new_Password_doesnt_match));
                    return isVaild;
                } else {
                    etConfirmPswd.setError(null);
                }
            }
        }else{
            isVaild=false;
            return isVaild;
        }

         return isVaild;
    }
}
