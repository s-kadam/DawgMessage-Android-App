package edu.uga.dawgmessage;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonWriter;
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
import com.google.firebase.database.FirebaseDatabase;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.io.OutputStreamWriter;

import static java.lang.System.out;

/*
Handles registration of new accounts
 */
public class Register extends AppCompatActivity {
    EditText username, password, confirmPass;
    Button button;
    String user, pass, confirmPW;
    TextView login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        confirmPass = findViewById(R.id.password2);
        button = findViewById(R.id.registerButton);
        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });
        /*
        When the button is clicked, if everything is on the level
         (username, password all valid, password and confirmation match, etc.)
         the user is registered and becomes a part of the Users database.
         */
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = username.getText().toString();
                pass = password.getText().toString();
                confirmPW = password.getText().toString();

                //Toast.makeText(Register.this, "CLICKED", Toast.LENGTH_LONG).show();
                if(user.equals("")){
                    username.setError("Username Left Blank");
                }
                else if(pass.equals("")){
                    password.setError("Password Left Blank");
                }
                else if(confirmPW.equals("")){
                    password.setError("Password Confirmation Left Blank");
                }
                else if(!user.matches("[A-Za-z0-9]+")){
                    username.setError("Username Must Be Letters/Numbers Only");
                }
                else if(user.length()<5){
                    username.setError("Username Too Short, Must Be >5 Characters");
                }
                else if(pass.length()<5){
                    password.setError("Password Too Short, Must Be >5 Characters");
                }
                else if(!pass.equals(confirmPW))
                {
                    password.setError("Passwords Did Not Match, Try Again!");
                }
                else {
                    final ProgressDialog progDiag = new ProgressDialog(Register.this);
                    progDiag.setMessage("Calling the Dawgs...");
                    progDiag.show();

                    String url = "https://messagingapp-6c371.firebaseio.com/users.json";

                    final StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                        @Override
                        public void onResponse(String s) {

                            FirebaseDatabase userRef = FirebaseDatabase.getInstance("https://messagingapp-6c371.firebaseio.com/");
                            //if there aren't any users, don't bother with the check
                            if(s.equals("null")) {
                                userRef.getReference().child("users").child(user).setValue(user);
                                userRef.getReference().child("users").child(user).child("password").setValue(pass);
                                Toast.makeText(Register.this, "Congratulations! You've Registered", Toast.LENGTH_LONG).show();
                            }
                            else {
                                try {
                                    Toast.makeText(Register.this, s, Toast.LENGTH_LONG).show();
                                    JSONObject jobject = new JSONObject(s);
                                    if (!jobject.has(user)) {
                                        userRef.getReference().child("users").child(user).setValue(user);
                                        userRef.getReference().child("users").child(user).child("password").setValue(pass);
                                        Toast.makeText(Register.this, "Congratulations! You've Successfully Registered", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(Register.this, "Sorry, This Username is Already In Use!", Toast.LENGTH_LONG).show();
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
                            out.println("" + oopsie );
                            progDiag.dismiss();
                        }
                    });
                    /*
                    Creates a new RequestQueue out of the current info and adds it immediately to the User database
                    */
                    RequestQueue queue = Volley.newRequestQueue(Register.this);
                    queue.add(request);
                }
            }
        });
    }
}