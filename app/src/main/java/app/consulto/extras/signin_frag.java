package app.consulto.extras;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.wang.avi.AVLoadingIndicatorView;

import org.w3c.dom.Text;

import app.consulto.*;

public class signin_frag extends Fragment {
    private Handler handelr;
    private String email = "";
    private String password = "";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.signin,null);
        final TextInputEditText emailet = v.findViewById(R.id.email);
        final TextInputEditText passwordet = v.findViewById(R.id.password);
        v.findViewById(R.id.loginbut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailet.getText().toString().trim();
                password = passwordet.getText().toString().trim();
                if(email.isEmpty())
                {
                    emailet.setError("Required Field");
                }
                else
                {
                    if(password.isEmpty())
                    {
                        passwordet.setError("Required Field");
                    }
                    else
                    {
                        Bundle b = new Bundle();
                        Message mess = new Message();
                        b.putString("email",email);
                        b.putString("password",password);
                        b.putInt("type",1);
                        b.putInt("ripof",0);
                        mess.setData(b);
                        handelr.sendMessage(mess);
                    }
                }
            }
        });
        v.findViewById(R.id.google_but).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                Message mess = new Message();
                b.putInt("type",3);
                b.putInt("ripof",0);
                mess.setData(b);
                handelr.sendMessage(mess);
            }
        });
        v.findViewById(R.id.facebook_but).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                Message mess = new Message();
                b.putInt("type",4);
                b.putInt("ripof",0);
                mess.setData(b);
                handelr.sendMessage(mess);
            }
        });
        v.findViewById(R.id.changepage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                Message mess = new Message();
                b.putInt("type",69);
                b.putInt("ripof",0);
                mess.setData(b);
                handelr.sendMessage(mess);
            }
        });
        return v;
    }
    public signin_frag(Handler h)
    {
        this.handelr = h;
    }
}
