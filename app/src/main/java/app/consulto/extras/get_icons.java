package app.consulto.extras;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class get_icons extends AsyncTask {
    private Context con;
    private Handler handler;
    private Message m;
    private Bundle bu;
    private String[] icons;
    private boolean all = true;
    public get_icons(Context c, Handler h,String ics[])
    {
        this.icons = ics;
        this.handler = h;
        this.con = c;
    }
    @Override
    protected Object doInBackground(Object[] objects) {
        URL u;
        HttpURLConnection ucon;
        InputStream is;
        int l = 0;
        FileOutputStream fos;
        byte[] b = new byte[1024];
        try {
        for(int i=0;i<icons.length;i++)
        {
                u = new URL("https://kikplus.000webhostapp.com/resources/icons/"+icons[i]+".png");
                ucon = (HttpURLConnection)u.openConnection();
                fos = new FileOutputStream(new File(con.getFilesDir(),icons[i]+".png"));
                is = ucon.getInputStream();
                while((l=is.read(b))>-1)
                {
                    fos.write(b,0,l);
                }
                fos.flush();
                fos.close();
            }
        }
        catch (Exception e) {
            m = new Message();
            bu = new Bundle();
            bu.putBoolean("done",false);
            m.setData(bu);
            handler.sendMessage(m);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        m = new Message();
        bu = new Bundle();
        bu.putBoolean("done",true);
        m.setData(bu);
        handler.sendMessage(m);
    }
}
