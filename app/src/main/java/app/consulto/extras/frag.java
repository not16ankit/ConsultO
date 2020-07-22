package app.consulto.extras;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import app.consulto.R;

public class frag extends Fragment
{
    private Message m;
    private Bundle b;
    private Handler ghandle;
    private String texts;
    private Handler h;
    private TextView tv;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.devnotes_views,null);
        tv = (TextView) v.findViewById(R.id.tip);
      h = new Handler()
        {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                tv.setText(tv.getText()+String.valueOf(msg.getData().getChar("new")));
            }
        };
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        tv.setText("");
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<texts.length();i++)
                {
                    m = new Message();
                    b = new Bundle();
                    b.putChar("new",texts.charAt(i));
                    m.setData(b);
                    h.sendMessage(m);
                    try {
                        Thread.sleep(70);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ghandle.sendEmptyMessage(0);
            }
        }).start();
    }


    public frag(String text, Handler h)
    {
        this.ghandle = h;
        this.texts = text;
    }
}