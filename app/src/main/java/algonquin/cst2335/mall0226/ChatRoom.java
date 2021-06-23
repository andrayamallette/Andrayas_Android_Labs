package algonquin.cst2335.mall0226;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChatRoom extends AppCompatActivity {
    ArrayList<ChatMessage> messages = new ArrayList<>();
    RecyclerView chatList;
    MyChatAdapter adt;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatlayout);

        Button send = findViewById(R.id.sendbtn);
        Button receive = findViewById(R.id.recievebtn);
        EditText edit = findViewById(R.id.edittext);

        chatList = findViewById(R.id.myrecycler);
       // setContentView(R.layout.chatlayout);

        adt = new MyChatAdapter();
        chatList.setAdapter( adt );
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        chatList.setLayoutManager(layoutManager);
         send.setOnClickListener(click->{
             SimpleDateFormat sdf = new SimpleDateFormat("EEEE,dd-MMM-yyyy hh-mm-ss a", Locale.getDefault());
             String currentDateandTime = sdf.format(new Date());
             ChatMessage thisMessage = new ChatMessage(edit.getText().toString(),1, currentDateandTime );

             messages.add(thisMessage);
             edit.setText("");
             adt.notifyItemInserted(messages.size()-1);
         });
        receive.setOnClickListener(click->{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());
            ChatMessage thisMessage = new ChatMessage(edit.getText().toString(),2, currentDateandTime );

            messages.add(thisMessage);
            adt.notifyItemInserted(messages.size()-1);
            edit.setText("");

        });

    }
    private class MyRowViews extends RecyclerView.ViewHolder{
        TextView messageText;
        TextView timeText;
        int position = -1;
        public MyRowViews(View itemView) {
            super(itemView);
            itemView.setOnClickListener(click->{
                AlertDialog.Builder builder = new AlertDialog.Builder( ChatRoom.this )
                .setMessage( "Do you want to delete this?" +messageText.getText())
                .setTitle("Question:")
                .setNegativeButton("no",(dialog,cl)->{})
                .setPositiveButton("yes",(dialog,cl)->{

                    position = getAbsoluteAdapterPosition();

                    ChatMessage removedMessage = messages.get(position);
                    messages.remove(position);
                    adt.notifyItemRemoved(position);

                    Snackbar.make(messageText, "You deleted message #"+position, Snackbar.LENGTH_LONG)
                            .setAction("Undo", clk->{
                                messages.add(position, removedMessage);
                                adt.notifyItemInserted(position); //redraw the inserted item
                            })
                            .show();
                    });
                builder.create().show();
            });

            messageText = itemView.findViewById(R.id.message);
            timeText=itemView.findViewById(R.id.time);
        }

    }
    private class MyChatAdapter extends RecyclerView.Adapter<MyRowViews>{

        @Override
        public MyRowViews onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater inflater = getLayoutInflater();

            int layoutID;
            if (viewType==1)
                layoutID=R.layout.sent_message;
            else
                layoutID=R.layout.receive_message;
            View loadedRow=inflater.inflate(layoutID, parent, false);
            return new MyRowViews(loadedRow);
        }
        @Override
        public void onBindViewHolder(MyRowViews holder, int position) {
            holder.messageText.setText(messages.get(position).getMessage());
            holder.timeText.setText(messages.get(position).getTimeSent());
            //holder.setPosition(position); for some reason this one doesn't work
        }

        @Override
        public int getItemViewType(int position) {
            return messages.get(position).getSendOrRecieve();
        }

        @Override
        public int getItemCount() {
            return messages.size();
        }
    }

    private class ChatMessage{
        String message;
        int sendOrRecieve;
        String timesent;

        public ChatMessage(String message, int sendOrRecieve, String timesent) {
            this.message = message;
            this.sendOrRecieve = sendOrRecieve;
            this.timesent = timesent;
        }

        public String getMessage() {
            return message;
        }

        public int getSendOrRecieve() {
            return sendOrRecieve;
        }

        public String getTimeSent() {
            return timesent;
        }
    }
}
