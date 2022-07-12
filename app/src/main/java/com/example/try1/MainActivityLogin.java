package com.example.try1;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;

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
                    user.setToken(response.body().getToken());
                    user.setPassword(loginData.getPassword());
                    user.setEmail(loginData.getIdentifier());
                    Toast.makeText(MainActivityLogin.this, "Success " + response.code(), Toast.LENGTH_SHORT).show();
                    final Intent intent;
                    intent =  new Intent(MainActivityLogin.this, MenuActivity.class);
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
                Log.e("Intro Activity", t.getMessage());
                Toast.makeText(MainActivityLogin.this, "fail " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    public void onBackPressed(){
        final Intent intent;
        intent =  new Intent(MainActivityLogin.this, MainActivity.class);
        startActivity(intent);
    }

}