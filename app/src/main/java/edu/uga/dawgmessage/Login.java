package edu.uga.dawgmessage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    /*
    Define objects
     */
    TextView registerUser;
    EditText username, password;
    Button button;
    String un, pw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        button = findViewById(R.id.loginButton);
        registerUser = findViewById(R.id.register);

        //Takes you to the registration page if you click the Register text

        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });
        //Button checks objects, validates them, and logs in if correct. Otherwise, displays messages
        //and awaits correct inputs
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                un = username.getText().toString();
                pw = password.getText().toString();
                if(un.equals("")){
                    username.setError("Username Left Blank");
                }
                else if(pw.equals("")){
                    password.setError("Password Left Blank");
                }
                else{
                    String url = "https://messagingapp-6c371.firebaseio.com/users.json";
                    final ProgressDialog progDiag = new ProgressDialog(Login.this);
                    progDiag.setMessage("Calling the Dawgs...");
                    progDiag.show();
                    //shows current progress

                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                        @Override
                        /*
                        The meat of the Login class,
                         */

                        public void onResponse(String s) {
                            if(s.equals("null")){
                                Toast.makeText(Login.this, "Username Not Found", Toast.LENGTH_LONG).show();
                            }
                            else{
                                try {
                                    JSONObject jobject = new JSONObject(s);
                                    if(!jobject.has(un)){
                                        Toast.makeText(Login.this, "Username Not Found", Toast.LENGTH_LONG).show();
                                    }
                                    else if(jobject.getJSONObject(un).getString("password").equals(pw)){
                                        UserInfo.username = un;
                                        UserInfo.password = pw;
                                        startActivity(new Intent(Login.this, Users.class));
                                    }
                                    else {
                                        Toast.makeText(Login.this, "The Password You Entered Was Incorrect", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            progDiag.dismiss();
                        }
                    },new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError oopsie) {
                            System.out.println(oopsie + "");
                            progDiag.dismiss();
                        }
                    });

                    RequestQueue queue = Volley.newRequestQueue(Login.this);
                    queue.add(request);
                }

            }
        });
    }

    private void getUserData(){

    }
}
