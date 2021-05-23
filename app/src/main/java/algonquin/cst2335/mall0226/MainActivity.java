package algonquin.cst2335.mall0226;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView mytext = findViewById(R.id.textview);
        Button mybutton = findViewById(R.id.mybutton);
        CheckBox mycb = findViewById(R.id.mycb);
        //SwitchCompat mysc = findViewById(R.id.mysc);
        RadioButton myradio = findViewById(R.id.myradio);
        EditText myedit = findViewById(R.id.myedittext);

        ImageView myimage = findViewById(R.id.logoalgonquin);
        ImageButton imgbtn = findViewById( R.id.myimagebutton );

        Context context = getApplicationContext();
        mybutton.setOnClickListener( vw ->mytext.setText("your edit text has: " + myedit.getText().toString()));
        mycb.setOnCheckedChangeListener((btn, isChecked)->Toast.makeText(context, "You clicked on the Checkbox and it is now: " + mycb.isChecked(), Toast.LENGTH_LONG).show());
        //mysc.setOnCheckedChangeListener((btn, isChecked)->"You clicked on the Switch and it is now: " + mysc.isChecked()", Toast.LENGTH_SHORT));
        myradio.setOnCheckedChangeListener((btn, isChecked)->Toast.makeText(context, "You clicked on the Radio Button and it is now: " + myradio.isChecked(), Toast.LENGTH_SHORT).show());
    //"The width = " + width + " and height = " + height.
        imgbtn.setOnClickListener(vw-> Toast.makeText(context, "The width = " + imgbtn.getWidth() + " and height = " + imgbtn.getHeight(),Toast.LENGTH_SHORT).show());
    }
}