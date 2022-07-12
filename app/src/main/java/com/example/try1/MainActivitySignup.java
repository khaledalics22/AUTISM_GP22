package com.example.try1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivitySignup extends AppCompatActivity {

    private EndPointAPI mEndPointAPI;
    private EditText firstname,lastname,email,password,username,birthday,gender;
    private Button btnsignup;
    String emailstring, passwordstring, firstnamestring,lastnamestring,usernamestring,birthdaystring,genderstring;
//    String token="Bearer " +"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2MWNmNTQ4MTEwNGE4YjIwODRhODljMWIiLCJpYXQiOjE2NDA5Nzc1MzcsImV4cCI6MjQyOTAwNDUzMTE4ODEyNzQwfQ.f5D5fS-Hvv99EgSL4FdArLwC8PM95r4ugVEVRQ4Yr8w";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_signup);
        firstname=(EditText) findViewById(R.id.editFirstName);
        lastname=(EditText) findViewById(R.id.editTextLastName);
        email=(EditText) findViewById(R.id.editTextTextEmailAddress);
        password=(EditText) findViewById(R.id.editTextPassword);
        username=(EditText) findViewById(R.id.editUserName);
        birthday=(EditText) findViewById(R.id.editBirthdate);
        gender=(EditText) findViewById(R.id.editGender);
        btnsignup=(Button) findViewById(R.id.signupbtn);
////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////
        mEndPointAPI = RetrofitClass.getClient().create(EndPointAPI.class);
        btnsignup.setOnClickListener(new View.OnClickListener() {

            //            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                firstnamestring = firstname.getText().toString();
                lastnamestring = lastname.getText().toString();
                emailstring = email.getText().toString();
                passwordstring = password.getText().toString();
                usernamestring=username.getText().toString();
                Log.i("ppppppppppp",username.getText().toString());
                birthdaystring=birthday.getText().toString();
                genderstring=gender.getText().toString();
                submit();
            }
        });

    }
    //    @RequiresApi(api = Build.VERSION_CODES.N)
    private void submit() {
        SignUpData signUpData = new SignUpData(usernamestring,passwordstring,emailstring,firstnamestring,lastnamestring,birthdaystring,genderstring);

        mEndPointAPI.signUp(signUpData).enqueue(new Callback<SignupRequest>() {
            /**
             * Invoked for a received HTTP response.
             * <p>
             * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
             * Call {@link Response#isSuccessful()} to determine if the response indicates success.
             *
             * @param call
             * @param response
             */
            @Override
            public void onResponse(Call<SignupRequest> call, retrofit2.Response<SignupRequest> response) {
                if (response.isSuccessful()) {
                    Log.v("aaaaaaaaaaaaaaaaaaa", response.body().toString());
                    user.setToken(response.body().getToken());
                    user.setUserName(signUpData.getUserName());
                    user.setPassword(signUpData.getPassword());
                    user.setLastname(signUpData.getLastName());
                    user.setFirstname(signUpData.getFirstName());
                    user.setEmail(signUpData.getEmail());
                    user.setBrithDay(signUpData.getBrithDay());
                    user.setGander(signUpData.getGander());
                    Toast.makeText(MainActivitySignup.this, "Success " + response.code(), Toast.LENGTH_SHORT).show();
                    final Intent intent;
                    intent =  new Intent(MainActivitySignup.this, MenuActivity.class);
                    startActivity(intent);

                }

                else {
                    Log.i("wwwwwwwwwwwwwww", signUpData.getEmail()+signUpData.getFirstName() );}
            }

            /**
             * Invoked when a network exception occurred talking to the server or when an unexpected
             * exception occurred creating the request or processing the response.
             *
             * @param call
             * @param t
             */
            @Override
            public void onFailure(Call<SignupRequest> call, Throwable t) {
                Log.e("Intro Activity", t.getMessage());
                Toast.makeText(MainActivitySignup.this, "fail " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
@Override
    public void onBackPressed(){
    final Intent intent;
    intent =  new Intent(MainActivitySignup.this, MainActivity.class);
    startActivity(intent);
}

}
