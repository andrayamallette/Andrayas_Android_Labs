package algonquin.cst2335.mall0226;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    MyOpenHelper opener;
    SQLiteDatabase db;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatlayout);

        Button send = findViewById(R.id.sendbtn);
        Button receive = findViewById(R.id.recievebtn);
        EditText edit = findViewById(R.id.edittext);

        opener = new MyOpenHelper(this);
        db = opener.getWritableDatabase();

        chatList = findViewById(R.id.myrecycler);
       // setContentView(R.layout.chatlayout);

        Cursor results =db.rawQuery("Select * from " + MyOpenHelper.TABLE_NAME + ";", null);


        int _idCol = results.getColumnIndex("_id");
        int messageCol = results.getColumnIndex(MyOpenHelper.col_message);
        int sendCol = results.getColumnIndex(MyOpenHelper.col_send_receive);
        int timeCol = results.getColumnIndex(MyOpenHelper.col_time_sent);

        while(results.moveToNext()){
            long id = results.getInt( _idCol );
            String message = results.getString( messageCol );
            String time = results.getString( timeCol );
            int sendOrReceive = results.getInt( sendCol );
            messages.add(new ChatMessage(message, sendOrReceive, time, id));
        }

        adt = new MyChatAdapter();
        chatList.setAdapter( adt );
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        chatList.setLayoutManager(layoutManager);
         send.setOnClickListener(click->{
             SimpleDateFormat sdf = new SimpleDateFormat("EEEE,dd-MMM-yyyy hh-mm-ss a", Locale.getDefault());
             String currentDateandTime = sdf.format(new Date());

             ChatMessage cm = new ChatMessage(edit.getText().toString(),1, currentDateandTime );
             ContentValues newRow = new ContentValues();
             newRow.put(MyOpenHelper.col_message, cm.getMessage());
             newRow.put(MyOpenHelper.col_send_receive, cm.getSendOrRecieve());
             newRow.put(MyOpenHelper.col_time_sent, cm.getTimeSent());

             long newId = db.insert(MyOpenHelper.TABLE_NAME, MyOpenHelper.col_message, newRow);

             cm.setId(newId);
             messages.add(cm);
             edit.setText("");
             adt.notifyItemInserted(messages.size()-1);
         });
        receive.setOnClickListener(click->{
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE,dd-MMM-yyyy hh-mm-ss a", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());

            ChatMessage cm = new ChatMessage(edit.getText().toString(),2, currentDateandTime );
            ContentValues newRow = new ContentValues();
            newRow.put(MyOpenHelper.col_message, cm.getMessage());
            newRow.put(MyOpenHelper.col_send_receive, cm.getSendOrRecieve());
            newRow.put(MyOpenHelper.col_time_sent, cm.getTimeSent());

            long newId = db.insert(MyOpenHelper.TABLE_NAME, MyOpenHelper.col_message, newRow);

            cm.setId(newId);
            messages.add(cm);
            edit.setText("");
            adt.notifyItemInserted(messages.size()-1);

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

                    db.delete(MyOpenHelper.TABLE_NAME, "_id=?", new String[] {Long.toString(removedMessage.getId())});
                    adt.notifyItemRemoved(position);

                    Snackbar.make(messageText, "You deleted message #"+position, Snackbar.LENGTH_LONG)
                            .setAction("Undo", clk->{
                                messages.add(position, removedMessage);
                                db.execSQL("Insert into "+MyOpenHelper.TABLE_NAME + " values('"+removedMessage.getId()+
                                                "', '" + removedMessage.getMessage() +
                                                "', '" + removedMessage.getSendOrRecieve() +
                                                "', '" + removedMessage.getTimeSent()+ "');");
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
        long id;

        public void setId(long l){id = l; }
        public long getId(){ return id;}

        public ChatMessage(String message, int sendOrRecieve, String timesent) {
            this.message = message;
            this.sendOrRecieve = sendOrRecieve;
            this.timesent = timesent;
        }
        public ChatMessage(String message, int sendOrRecieve, String timesent, long id) {
            this.message = message;
            this.sendOrRecieve = sendOrRecieve;
            this.timesent = timesent;
            this.id=id;
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
