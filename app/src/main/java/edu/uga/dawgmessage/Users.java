package edu.uga.dawgmessage;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;

public class Users extends AppCompatActivity {
    ListView usersList;
    TextView noUsersText;
    ArrayList<String> al = new ArrayList<>();
    int totalUsers = 0;
    ProgressDialog progDiag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        usersList = findViewById(R.id.usersList);
        noUsersText = findViewById(R.id.noUsersText);
        progDiag = new ProgressDialog(Users.this);
        progDiag.setMessage("Calling the Dawgs...");
        progDiag.show();
        String url = "https://messagingapp-6c371.firebaseio.com/users.json";
        //makes a request for a list of users, and if it finds any it runs the doOnSuccess method.
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                doOnSuccess(s);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError oopsie) {
                System.out.println(oopsie + "");
            }
        });
        //Creates and adds a RequestQueue for Users
        RequestQueue queue = Volley.newRequestQueue(Users.this);
        queue.add(request);
        //When you click on a user from the list, it launches the chat activity with the user in question
        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserInfo.chatRoom = al.get(position);
                startActivity(new Intent(Users.this, Chat.class));
            }
        });
    }
    /*
    Iterates through and populates the list if there's at least a single user
     */
    public void doOnSuccess(String s){
        try {
            JSONObject jobject = new JSONObject(s);
            Iterator i = jobject.keys();
            String key = "";
            while(i.hasNext()){
                key = i.next().toString();
                if(!key.equals(UserInfo.username)) {
                    al.add(key);
                }
                totalUsers++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //if there aren't any users, show the noUsersText object and hide the usersList object.
        if(totalUsers <=1){
            noUsersText.setVisibility(View.VISIBLE);
            usersList.setVisibility(View.GONE);
        }
        //if there are users, hide the noUsersText object and show the usersList object instead.
        else{
            noUsersText.setVisibility(View.GONE);
            usersList.setVisibility(View.VISIBLE);
            usersList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al));
        }
        progDiag.dismiss();
    }
}