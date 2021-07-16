package algonquin.cst2335.mall0226;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ChatRoom extends AppCompatActivity {
    boolean isTablet=false;
    MessageListFragment chatFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_layout);

        isTablet= findViewById(R.id.detailsRoom) !=null;

        chatFragment = new MessageListFragment();
        FragmentManager fMgr = getSupportFragmentManager();
        FragmentTransaction tx = fMgr.beginTransaction();
        tx.add(R.id.fragmentRoom, chatFragment);
        tx.commit();// This line actually loads the fragment into the specified FrameLayout
    }

    public void userClickedMessage(MessageListFragment.ChatMessage chatMessage, int position) {
        MessageDetailsFragment mdFragment = new MessageDetailsFragment(chatMessage, position);

        if(isTablet){
            getSupportFragmentManager().beginTransaction().replace(R.id.detailsRoom, mdFragment).commit();

        }else{ //on a phone
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentRoom, mdFragment).commit();
        }

    }

    public void notifyMessageDeleted(MessageListFragment.ChatMessage chosenMessage, int chosenPosition) {
        chatFragment.notifyMessageDeleted(chosenMessage, chosenPosition);
    }
}
