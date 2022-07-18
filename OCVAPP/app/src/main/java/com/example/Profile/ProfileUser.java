package com.example.Profile;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Authentication.MainActivity;
import com.example.Interfaces.EndPointAPI;
import com.example.Objects.User;
import com.example.RestApis.RetrofitClass;
import com.example.RestApis.userinfo;
import com.example.ocvapp.R;

import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;

public class ProfileUser extends AppCompatActivity {
    private EndPointAPI mEndPointAPI;
    private TextView firstname,lastname,email,type,username,birthday;
    private Button btnlogout;
    private ProgressDialog progressDoalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);
        loading();
        firstname=(TextView) findViewById(R.id.editFirstNamep);
        lastname=(TextView) findViewById(R.id.editTextLastNamep);
        email=(TextView) findViewById(R.id.editTextTextEmailAddressp);
//        type=(TextView) findViewById(R.id.editTextType);
        username=(TextView) findViewById(R.id.editUserNamep);
        birthday=(TextView) findViewById(R.id.editBirthdatep);
        btnlogout=(Button) findViewById(R.id.Logout);
        Log.i("ppppppppppppppppppppppppppppp1", ""+ User.getToken());
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                try {
                    Log.v("33333333333333", " logout Delete");
                    Delete_token();
                    final Intent intent;
                    intent = new Intent(ProfileUser.this, MainActivity.class);
                    startActivity(intent);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
////////////////////////////////////////////////////////////////////
        mEndPointAPI = RetrofitClass.getClient().create(EndPointAPI.class);

        updateprofile();
    }
    //"Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2MmQwNmQ3ZTZlMDQxYjJiNWMyN2NlYjMiLCJpYXQiOjE2NTc4MjY2ODYsImV4cCI6MjQyOTAwNDUzMTM1NjYxOTAwfQ.TCC_s_-3qLVDcuDsoZdKEHDbnTRMPbEACFiLXjnzf6M"
    void Delete_token() throws IOException {
        FileOutputStream local_file_out = openFileOutput("Token", Context.MODE_PRIVATE);
        local_file_out.write("Delete".getBytes());
        local_file_out.close();
    }
    public void updateprofile(){
        String token= User.getToken();
        mEndPointAPI.Get_InfoUser(token).enqueue(new Callback<userinfo>() {
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
            public void onResponse(Call<userinfo> call, retrofit2.Response<userinfo> response) {
                if (response.isSuccessful()) {
                    userinfo uinf=response.body();
                    Log.v("assss", response.body().toString());
                    firstname.setText(uinf.getFirstName());
                    lastname.setText(uinf.getLastName());
                    email.setText(uinf.getEmail());
                    username.setText(uinf.getUserName());
                    birthday.setText(uinf.getBrithDay());
                    Log.v("llllllllllllllll", ""+firstname+lastname);
                    finish_loading();
                }

                else {
                    Log.i("wwwwwwwwwwwwwww", ""+response.body());}
            }

            /**
             * Invoked when a network exception occurred talking to the server or when an unexpected
             * exception occurred creating the request or processing the response.
             *
             * @param call
             * @param t
             */
            @Override
            public void onFailure(Call<userinfo> call, Throwable t) {
                Log.e("fail", t.getMessage());
            }
        });


    }
void loading(){
    progressDoalog = new ProgressDialog(ProfileUser.this,R.style.ProgressBar);
    progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    progressDoalog.show();
}
void finish_loading(){
    progressDoalog.dismiss();

}

}