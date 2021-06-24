package algonquin.cst2335.mall0226;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.Character;

/**
 * @author Andraya Mallette 040997475
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {
    /**This holds the textView */
    private TextView tv=null;

    /**This holds the edit text to enter the password*/
    private EditText et=null;

    /**This holds the button that is used for checking the password complexity*/
    private Button btn=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        int duration = Toast.LENGTH_SHORT;
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