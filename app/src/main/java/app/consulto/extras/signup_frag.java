package app.consulto.extras;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.wang.avi.AVLoadingIndicatorView;

import app.consulto.*;
public class signup_frag extends Fragment {
    private Handler handler;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.signup,null);
        final TextInputEditText firstet  = (TextInputEditText)v.findViewById(R.id.firstname);
        final TextInputEditText lastet  = (TextInputEditText)v.findViewById(R.id.lastname);
        final TextInputEditText emailet  = (TextInputEditText)v.findViewById(R.id.email);
        final TextInputEditText passet  = (TextInputEditText)v.findViewById(R.id.password);
        final CheckBox checkBox = (CheckBox)v.findViewById(R.id.termsandconditions);
        v.findViewById(R.id.loginbut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = firstet.getText().toString().trim()+" "+lastet.getText().toString().trim();
                String email = emailet.getText().toString().trim();
                String password = passet.getText().toString().trim();
                if(name.isEmpty())
                {
                    firstet.setError("Field Required");
                }
                else
                {
                    if(email.isEmpty())
                    {
                        emailet.setError("Field Required");
                    }
                    else
                    {
                        if(password.isEmpty())
                        {
                            passet.setError("Field Required");
                        }
                        else
                        {
                            if(!checkBox.isChecked())
                            {
                                Toast.makeText(getContext(),"Your consent for terms acceptance is required.",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Bundle b = new Bundle();
                                Message mess = new Message();
                                b.putInt("type",2);
                                b.putInt("ripof",1);
                                b.putString("name",name);
                                b.putString("email",email);
                                b.putString("password",password);
                                mess.setData(b);
                                handler.sendMessage(mess);
                            }
                        }
                    }
                }
            }
        });
        v.findViewById(R.id.changepage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                Message mess = new Message();
                b.putInt("type",69);
                b.putInt("ripof",1);
                mess.setData(b);
                handler.sendMessage(mess);
            }
        });
        return v;
    }
    public signup_frag(Handler h)
    {
        this.handler = h;
    }
}
