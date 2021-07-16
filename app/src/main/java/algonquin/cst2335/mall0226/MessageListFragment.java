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

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MessageListFragment extends Fragment{
    ArrayList<ChatMessage> messages = new ArrayList<>();
    RecyclerView chatList;
    MyChatAdapter adt;
    MyOpenHelper opener;
    SQLiteDatabase db;
    Button send;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstaceState){
        View chatLayout=inflater.inflate(R.layout.chatlayout,container,false);
        send = chatLayout.findViewById(R.id.sendbtn);

        chatList = chatLayout.findViewById(R.id.myrecycler);
        Button send = chatLayout.findViewById(R.id.sendbtn);
        Button receive = chatLayout.findViewById(R.id.recievebtn);
        EditText edit = chatLayout.findViewById(R.id.edittext);

        opener = new MyOpenHelper(getContext());
        db = opener.getWritableDatabase();

        adt = new MyChatAdapter();
        chatList.setAdapter( adt );
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        chatList.setLayoutManager(layoutManager);
         send.setOnClickListener(click->{
             SimpleDateFormat sdf = new SimpleDateFormat("EEEE,dd-MMM-yyyy hh-mm-ss a", Locale.getDefault());
             String currentDateandTime = sdf.format(new Date());

             ChatMessage cm = new ChatMessage(edit.getText().toString(),1, currentDateandTime );
             messages.add(cm);
             edit.setText("");
             adt.notifyItemInserted(messages.size()-1);
         });
        receive.setOnClickListener(click->{
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE,dd-MMM-yyyy hh-mm-ss a", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());

            ChatMessage cm = new ChatMessage(edit.getText().toString(),2, currentDateandTime );
            messages.add(cm);
            edit.setText("");
            adt.notifyItemInserted(messages.size()-1);

        });

        return chatLayout;
    }

    public void notifyMessageDeleted(ChatMessage chosenMessage, int chosenPosition) {

                AlertDialog.Builder builder = new AlertDialog.Builder( getContext() )
                .setMessage( "Do you want to delete this?" +chosenMessage.getMessage())
                .setTitle("Question:")
                .setNegativeButton("no",(dialog,cl)->{})
                .setPositiveButton("yes",(dialog,cl)->{
                    //position = getAbsoluteAdapterPosition();
                    ChatMessage removedMessage = messages.get(chosenPosition);
                    messages.remove(chosenPosition);
                    db.delete(MyOpenHelper.TABLE_NAME, "_id=?", new String[] {Long.toString(removedMessage.getId())});
                    adt.notifyItemRemoved(chosenPosition);
                    Snackbar.make(send, "You deleted message #"+chosenPosition, Snackbar.LENGTH_SHORT)
                            .setAction("Undo", clk->{
                                messages.add(chosenPosition, removedMessage);
                                adt.notifyItemInserted(chosenPosition); //redraw the inserted item
                            })
                            .show();
                    });
                builder.create().show();
    }

    private class MyRowViews extends RecyclerView.ViewHolder{
        TextView messageText;
        TextView timeText;
        int position = -1;
        public MyRowViews(View itemView) {
            super(itemView);
            itemView.setOnClickListener(click->{
                ChatRoom parentActivity = (ChatRoom)getContext();
                int position = getAbsoluteAdapterPosition();
                parentActivity.userClickedMessage(messages.get(position),position);

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

    public class ChatMessage{
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
