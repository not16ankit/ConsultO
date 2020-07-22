package app.consulto.extras;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import app.consulto.*;

public class chat_adapter extends ArrayAdapter {
    private ArrayList<String> messages;
    private ArrayList<String> usernames;
    private String email = "";
    public chat_adapter(@NonNull Context context, int resource,ArrayList<String> messages,ArrayList<String> usernames,String emai) {
        super(context, resource);
        this.messages = messages;
        this.email = emai;
        this.usernames = usernames;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater li = LayoutInflater.from(getContext());
        View v = li.inflate(R.layout.sent,null);
        if(usernames.get(position).equalsIgnoreCase(email))
        {
            v = li.inflate(R.layout.sent,null);
            TextView tv = (TextView)v.findViewById(R.id.chat);
            tv.setText(messages.get(position));
        }
        else
        {
            v = li.inflate(R.layout.recieved,null);
            TextView tv = (TextView)v.findViewById(R.id.chat);
            tv.setText(messages.get(position));
        }
        v.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.chat_anim));
        return v;
    }

    @Override
    public int getCount() {
        return messages.size();
    }
}
