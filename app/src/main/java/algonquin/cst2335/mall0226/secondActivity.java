package algonquin.cst2335.mall0226;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class secondActivity extends AppCompatActivity {




    @Override
    protected void onStart() {
        super.onStart();
        Log.w( "onStart", "In onStart() - The application is now visible on screen(secondActivity)" );
    }
    /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    }*/

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==3456){
            if(resultCode == RESULT_OK){
                Bitmap thumbnail = data.getParcelableExtra("data");
                ImageView profileImage = findViewById(R.id.imageView);
                profileImage.setImageBitmap(thumbnail);
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Intent fromPrevious = getIntent();
        String emailAddress = fromPrevious.getStringExtra("EmailAddress");
        TextView textView = findViewById(R.id.textView);
        textView.setText("Welcome back "+emailAddress);
        Button callButton = findViewById(R.id.phoneButton);

        callButton.setOnClickListener(clk->{
            Intent call = new Intent(Intent.ACTION_DIAL);
            TextView phoneEditText = findViewById(R.id.phoneEditText);
            call.setData(Uri.parse("tel:" + phoneEditText.getText().toString()));
            startActivity(call);
        });


        Button loginButton = findViewById(R.id.cameraButton);
        loginButton.setOnClickListener(  clk -> {Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, 3456);} );
    }
}