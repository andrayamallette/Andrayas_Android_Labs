package algonquin.cst2335.mall0226;

import androidx.appcompat.app.AppCompatActivity;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.Character;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @author Andraya Mallette 040997475
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {


    private String stringURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button forecastBtn = findViewById(R.id.forecastButton);
        EditText cityText = findViewById(R.id.cityTextField);
        forecastBtn.setOnClickListener((click) ->{
            Executor newThread = Executors.newSingleThreadExecutor();
            newThread.execute(()->{
                try {

                    String cityName = cityText.getText().toString();
                    stringURL ="https://api.openweathermap.org/data/2.5/weather?q="
                            + URLEncoder.encode(cityName, "UTF-8")
                            + "&appid=7e943c97096a9784391a981c4d878b22&units=metric";

                    URL url = new URL(stringURL);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                    String text = (new BufferedReader(
                            new InputStreamReader(in, StandardCharsets.UTF_8)))
                            .lines()
                            .collect(Collectors.joining("\n"));

                    JSONObject theDocument = new JSONObject(text);
                    //not sure if these are needed but I'm adding anyways
                    JSONObject coord = theDocument.getJSONObject("coord");
                    JSONArray weatherArray = theDocument.getJSONArray("weather");
                    JSONObject position0 = weatherArray.getJSONObject(0);
                    String description = position0.getString("description");
                    String iconName = position0.getString("icon");

                    int vis = theDocument.getInt("visibility");
                    String name = theDocument.getString("name");

                    //meow


                    JSONObject mainObject = theDocument.getJSONObject( "main" );
                    double current = mainObject.getDouble("temp");
                    double min = mainObject.getDouble("temp_min");
                    double max = mainObject.getDouble( "temp_max");
                    int humidity = mainObject.getInt("humidity");

                    runOnUiThread(()->{
                        TextView tv = findViewById(R.id.temp);
                        tv.setText("The current temperature is " + current);
                        tv.setVisibility(View.VISIBLE);

                        tv = findViewById(R.id.maxTemp);
                        tv.setText("the max temperature is " + max);
                        tv.setVisibility(View.VISIBLE);

                        tv = findViewById(R.id.minTemp);
                        tv.setText("the min temperature is " + min);
                        tv.setVisibility(View.VISIBLE);

                        tv = findViewById(R.id.humidity);
                        tv.setText("the humidity is " + humidity);
                        tv.setVisibility(View.VISIBLE);

                        tv = findViewById(R.id.description);
                        tv.setText(description);
                        tv.setVisibility(View.VISIBLE);

                        ;
                        ImageView iv = findViewById(R.id.icon);
                        iv.setImageBitmap(assignImage(iconName));
                        iv.setVisibility(View.VISIBLE);
                    });



                } catch (IOException | JSONException ioe) {
                    ioe.printStackTrace();
                }
            });
        });

        /* deprecated code
        tv = findViewById(R.id.editText);
        et = findViewById(R.id.editText);
        btn = findViewById(R.id.button);

        btn.setOnClickListener(clk->{
            String password=et.getText().toString();
            if (checkPasswordComplexity(password)){
                tv.setText("Your password meets the requirements");
            }else if(!checkPasswordComplexity(password)){
                tv.setText("You shall not pass!");
            }
        });
        */
    }

    private Bitmap assignImage(String iconName) {
        Bitmap image = null;
        try{

            File file = new File(getFilesDir(), iconName +".png");
            if(file.exists()){
                image=BitmapFactory.decodeFile(getFilesDir()+"/"+".png");//file cannot be found
                return image;
            }else{
                URL imgUrl = new URL( "https://openweathermap.org/img/w/"+ iconName +".png" );
                HttpURLConnection connection = (HttpURLConnection) imgUrl.openConnection();
                connection.connect();//fails here android.os.NetworkOnMainThreadException
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    image = BitmapFactory.decodeStream(connection.getInputStream());
                    image.compress(Bitmap.CompressFormat.PNG, 100, openFileOutput(iconName +".png", Activity.MODE_PRIVATE));
                    return image;
                }

            }

        }catch(IOException e){
            e.printStackTrace();
        }
        return image;
    }

    /**This function checks if the password has an upper and lower case letter a number and a special symbol
     *
     * @param password the String object that we are checking
     * @return returns false if there lacks an upper/lower case letter, number, or a special character
     */
    private boolean checkPasswordComplexity(String password) {
        boolean foundUpperCase, foundLowerCase, foundNumber, foundSpecial;
        foundUpperCase = foundLowerCase = foundNumber = foundSpecial = false;

        Context context=getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        for( int i=0; i<password.length(); i++){
            char c = password.charAt(i);
            if(Character.isUpperCase(c)){
                foundUpperCase=true;
            }else if(Character.isLowerCase(c)){
                foundLowerCase=true;
            }else if(Character.isDigit(c)){
                foundNumber=true;
            }else if(isSpecialCharacter(c)){
                foundSpecial=true;
            }
        }
        if(!foundUpperCase){
            CharSequence text="You are missing an upper case";
            Toast toast= Toast.makeText(context, text, duration);
            toast.show();
            return false;
        }else if (!foundLowerCase){
            CharSequence text="You are missing a lower case";
            Toast toast= Toast.makeText(context, text, duration);
            toast.show();
            return false;
        }else if (!foundNumber){
            CharSequence text="You are missing a number";
            Toast toast= Toast.makeText(context, text, duration);
            toast.show();
            return false;
        }else if (!foundSpecial){
            CharSequence text="You are missing a special character";
            Toast toast= Toast.makeText(context, text, duration);
            toast.show();
            return false;
        }else {
            return true;
        }

    }

    /**This method is used to determine if the parameter is one of these special characters "!@#$%^&*"
     *
     * @param c the char passed in that we are checking
     * @return returns false if no special character is detected
     */
    private boolean isSpecialCharacter(char c){
        switch (c){
            case '!':
            case '@':
            case '#':
            case '$':
            case '%':
            case '^':
            case '&':
            case '*': return true;
            default: return false;
        }
    }
}