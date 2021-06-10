package algonquin.cst2335.mall0226;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
                Bitmap mBitmap = data.getParcelableExtra("data");
                ImageView profileImage = findViewById(R.id.imageView);
                profileImage.setImageBitmap(mBitmap);

                FileOutputStream fOut = null;
                try {
                    fOut = openFileOutput( "Picture.png", Context.MODE_PRIVATE);
                    mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();

                } catch (IOException e) {
                    e.printStackTrace();
                }
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

        //preferences
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String phoneNumber = prefs.getString("PhoneNumber", "");
        EditText phoneEditText = findViewById(R.id.phoneEditText);
        phoneEditText.setText(phoneNumber);//sets the email address to be the preference

        //stuff for call
        Button callButton = findViewById(R.id.phoneButton);
        callButton.setOnClickListener(clk->{
            SharedPreferences.Editor  editor = prefs.edit();
            editor.putString("PhoneNumber", phoneEditText.getText().toString());
            editor.apply();
            Intent call = new Intent(Intent.ACTION_DIAL);
            call.setData(Uri.parse("tel:" + phoneEditText.getText().toString()));
            startActivity(call);
        });

        //stuff for camera
        Button loginButton = findViewById(R.id.cameraButton);
        loginButton.setOnClickListener(  clk -> {Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, 3456);} );

        File file = new File(getFilesDir().toString());
        if(file.exists())
        {
            Bitmap theImage = BitmapFactory.decodeFile(file.getPath()+File.separator+"Picture.png");
            ImageView myImageView = findViewById(R.id.imageView);
            myImageView.setImageBitmap( theImage );
        }
    }
}
