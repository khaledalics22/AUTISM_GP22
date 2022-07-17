package com.example.Authentication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;

import com.example.RestApis.LoginData;
import com.example.SelectModel.ChooseModels;
import com.example.ocvapp.R;
import com.example.Interfaces.EndPointAPI;
import com.example.RestApis.RetrofitClass;
import com.example.RestApis.SignupRequest;
import com.example.RestApis.User;

import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivityLogin extends AppCompatActivity {
    private EndPointAPI mEndPointAPI;
    private EditText password,email;
    private Button btnlg;
    String emailstring, passwordstring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);
        email=(EditText) findViewById(R.id.editTextTextEmailAddresslg);
        password=(EditText) findViewById(R.id.editTextPasswordlg);
        btnlg=(Button) findViewById(R.id.loginbtn);
        mEndPointAPI = RetrofitClass.getClient().create(EndPointAPI.class);
        btnlg.setOnClickListener(new View.OnClickListener() {

            //            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                emailstring = email.getText().toString();
                passwordstring = password.getText().toString();
                submit();
            }
        });

    }
    private void submit() {
        LoginData loginData = new LoginData(emailstring,passwordstring);

        mEndPointAPI.login(loginData).enqueue(new Callback<SignupRequest>() {
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
                    User.setToken(response.body().getToken());
                    User.setPassword(loginData.getPassword());
                    User.setEmail(loginData.getIdentifier());
                    Toast.makeText(MainActivityLogin.this, "Success ", Toast.LENGTH_SHORT).show();
                    try {
                        Log.v("88888888", "Save");
                        Save_token(User.getToken());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    final Intent intent;
                    intent =  new Intent(MainActivityLogin.this, ChooseModels.class);
                    startActivity(intent);

                }

                else {
                    Log.i("wwwwwwwwwwwwwww", loginData.getPassword()+loginData.getIdentifier() );}
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
                Log.e("iiiiiiii", t.getMessage());
                Toast.makeText(MainActivityLogin.this, "fail " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    void Save_token(String token) throws IOException
    {
        //create and save a file
        FileOutputStream local_file_out = openFileOutput("Token", Context.MODE_PRIVATE);
        //Write token on a file
        local_file_out.write(token.getBytes());
        Log.v("7777777", ""+token.getBytes());
        local_file_out.close();
    }
    @Override
    public void onBackPressed(){
        final Intent intent;
        intent =  new Intent(MainActivityLogin.this, MainActivity.class);
        startActivity(intent);
    }

}