package edu.uga.dawgmessage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;

public class Chat extends AppCompatActivity {
    LinearLayout linLayout;
    RelativeLayout relLayout;
    ImageView sendMessage;
    EditText messageDisplay;
    ScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        /*
        Create and define the objects used in the class
         */
        linLayout = (LinearLayout) findViewById(R.id.layout1);
        relLayout = findViewById(R.id.layout2);
        sendMessage = findViewById(R.id.sendButton);
        messageDisplay = findViewById(R.id.messageArea);
        scrollView = findViewById(R.id.scrollView);
        //First and Second Users are Instantiated

        final FirebaseDatabase firstUser = FirebaseDatabase.getInstance();
        final FirebaseDatabase secondUser = FirebaseDatabase.getInstance();
        /*
        Controls the button that submits the messages
         */
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageDisplay.getText().toString();
                if(messageText.equals("") == false)
                /*
                 Send the message if there is one to send
                 */
                {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", UserInfo.username);
                    firstUser.getReference().child("messages").child(UserInfo.username + "_" + UserInfo.chatRoom).setValue(map);
                    secondUser.getReference().child("messages").child(UserInfo.chatRoom + "_" + UserInfo.username).setValue(map);
                    messageDisplay.setText("");
                }
                else
                {
                    //doNothing();
                }
            }
        });
        /*
        When the message is sent by either user, add it to the message scroller, the side
        depending on which user sent it
         */
        DatabaseReference ref = firstUser.getReference().child("messages");
        ref.addChildEventListener(new ChildEventListener()  {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = (Map<String, Object>) dataSnapshot.getValue();

                String message = (String)map.get("message");
                String userName = (String)map.get("userSent");

                if (userName != (UserInfo.username)) {
                    addMessageBox("You:-\n" + message, 1);
                } else {
                    addMessageBox(UserInfo.chatRoom + ":-\n" + message, 2);
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    /*
    Method to add a message box to the screen, using parameters and the user to determine which side the message goes on.
     */
    public void addMessageBox(String message, int type){
        TextView textView = new TextView(Chat.this);
        textView.setText(message);
        LinearLayout.LayoutParams parameters = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        parameters.weight = 1.0f;
        textView.setLayoutParams(parameters);
        if(type == 1) {
            parameters.gravity = Gravity.RIGHT;
            //textView.setBackgroundResource(R.drawable.bubble_in);
        }
        else{
            parameters.gravity = Gravity.LEFT;
            //textView.setBackgroundResource(R.drawable.bubble_out);
        }
        linLayout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }
}