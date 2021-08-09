package algonquin.cst2335.mall0226;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
/**
 * @author Andraya Mallette 040997475
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {
    TextView currentTempTV;
    TextView maxTempTV;
    TextView minTempTV;
    TextView humidityTV;
    TextView descriptionTV;
    ImageView iconTV;
    EditText cityField;
    Button forecastBtn;


    Bitmap image;
    String description = null;
    String iconName = null;
    String current = null;
    String min = null;
    String max = null;
    String humidity = null;

    private String stringURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentTempTV = findViewById(R.id.currentTemp);
        maxTempTV = findViewById(R.id.maxTemp);
        minTempTV = findViewById(R.id.minTemp);
        humidityTV = findViewById(R.id.humidity);
        descriptionTV = findViewById(R.id.description);
        iconTV = findViewById(R.id.icon);
        cityField = findViewById(R.id.cityTextField);
        forecastBtn = findViewById(R.id.forecastButton);

        Toolbar myToolbar=findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);




        forecastBtn.setOnClickListener((click) ->{

            String cityName=cityField.getText().toString();
            myToolbar.getMenu().add(0,5,0,cityName).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
            runForecast(cityName);

        });

    }
    public void runForecast(String cityName){
        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Getting forecase")
                .setMessage("We're ca;;ing people in "+cityName + " to look outside their windows and tell us what's the weather like over there.")
                .setView(new ProgressBar(MainActivity.this))
                .show();

        Executor newThread = Executors.newSingleThreadExecutor();
        newThread.execute(()->{
            try {
                stringURL ="https://api.openweathermap.org/data/2.5/weather?q="
                        + URLEncoder.encode(cityName, "UTF-8")
                        + "&appid=7e943c97096a9784391a981c4d878b22&units=metric&mode=xml";

                URL url = new URL(stringURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(in, "UTF-8");



                while( xpp.next() != XmlPullParser.END_DOCUMENT ){
                    switch(xpp.getEventType()){
                        case XmlPullParser.START_TAG:
                            if(xpp.getName().equals("temperature")){
                                current = xpp.getAttributeValue(null, "value");  //this gets the current temperature

                                min = xpp.getAttributeValue(null, "min"); //this gets the min temperature

                                max = xpp.getAttributeValue(null, "max"); //this gets the max temperature
                            }else if(xpp.getName().equals("weather")){
                                description = xpp.getAttributeValue(null, "value");  //this gets the weather description

                                iconName = xpp.getAttributeValue(null, "icon"); //this gets the icon name
                            }else if(xpp.getName().equals("humidity")){
                                humidity=xpp.getAttributeValue(null, "value");
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            break;
                        case XmlPullParser.TEXT:
                            break;
                    }
                }
                image = assignImage(iconName);

                runOnUiThread(()->{
                    cityField.setText(cityName);

                    TextView tv = findViewById(R.id.currentTemp);
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
                    dialog.hide();


                    ImageView iv = findViewById(R.id.icon);
                    iv.setImageBitmap(image);
                    iv.setVisibility(View.VISIBLE);
                });



            } catch (IOException | XmlPullParserException ioe) {
                ioe.printStackTrace();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        float oldSize=14;

        switch(item.getItemId()){
            case 5:
                String cityName=item.getTitle().toString();
                runForecast(cityName);
                break;
            case R.id.hide_views:
                currentTempTV.setVisibility(View.INVISIBLE);
                maxTempTV.setVisibility(View.INVISIBLE);
                minTempTV.setVisibility(View.INVISIBLE);
                humidityTV.setVisibility(View.INVISIBLE);
                descriptionTV.setVisibility(View.INVISIBLE);
                iconTV.setVisibility(View.INVISIBLE);
                cityField.setText("");//clear the city name
                break;
            case R.id.id_increase:
                oldSize++;
                currentTempTV.setTextSize(oldSize);
                maxTempTV.setTextSize(oldSize);
                minTempTV.setTextSize(oldSize);
                humidityTV.setTextSize(oldSize);
                descriptionTV.setTextSize(oldSize);
                cityField.setTextSize(oldSize);
                break;
            case R.id.id_decrease:
                oldSize=Float.max(oldSize-1, 5);
                currentTempTV.setTextSize(oldSize);
                maxTempTV.setTextSize(oldSize);
                minTempTV.setTextSize(oldSize);
                humidityTV.setTextSize(oldSize);
                descriptionTV.setTextSize(oldSize);
                cityField.setTextSize(oldSize);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return true;
    }


    private Bitmap assignImage(String iconName) {

        try{

            File file = new File(getFilesDir(), iconName +".png");
            if(file.exists()){
                image=BitmapFactory.decodeFile(getFilesDir()+"/"+iconName+".png");//file cannot be found
                return image;
            }else{
                java.net.URL imgUrl = new URL( "https://openweathermap.org/img/w/"+ iconName +".png" );
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


}