package algonquin.cst2335.mall0226;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.BreakIterator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w( "onDestroy", "In onDestroy() - Any memory used by the application is freed" );
    }

    private static String TAG = "MainActivity";
    @Override
    protected void onResume() {
        super.onResume();
        Log.w( "onResume", "In onResume() - The application is now responding to user input" );
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w( "onStop", "In onStop() - The application is no longer visible" );
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w( "onPause", "In onPause() - The application no longer responds to user input" );
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.w( "onStart", "In onStart() - The application is now visible on screen" );
    }

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    public void sendMessage(){
        Intent nextPage = new Intent( MainActivity.this, secondActivity.class);
        EditText emailEditText = findViewById(R.id.emailEditText);
        Log.w("sendMessage",emailEditText.getText().toString());
        nextPage.putExtra( "EmailAddress", emailEditText.getText().toString() );

        startActivity(nextPage);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.w( TAG, "In onCreate() - Loading Widgets" );

        //preferences
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String emailAddress = prefs.getString("LoginName", "");
        EditText emailEditText = findViewById(R.id.emailEditText);
        emailEditText.setText(emailAddress);//sets the email address to be the preference

        Button loginButton = findViewById(R.id.submitButton);
        loginButton.setOnClickListener(  clk -> {
            SharedPreferences.Editor  editor = prefs.edit();
            editor.putString("LoginName", emailEditText.getText().toString());
            editor.apply();
            sendMessage() ; } );
    }
}