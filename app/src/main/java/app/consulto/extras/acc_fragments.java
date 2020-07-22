package app.consulto.extras;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import app.consulto.R;
import app.consulto.grids;

public class acc_fragments extends Fragment {
    private int type=0;
    private Handler handler;
    private String email = "";
    private String password = "";
    private RequestQueue q;
    private boolean a;
    private boolean b;
    private AlertDialog ad;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.google,container,false);
        ImageView socialicon;
        androidx.appcompat.app.AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
       final  View viw = getLayoutInflater().inflate(R.layout.keys_alert,null);
        ab.setView(viw);
        ad = ab.create();
        ad.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        switch (type)
        {
            case 1:
                v = inflater.inflate(R.layout.normal,container,false);
                break;
            case 2:
                v = inflater.inflate(R.layout.google,container,false);
                break;
            case 3:
                v = inflater.inflate(R.layout.instagram,container,false);
                break;
        }
        v.findViewById(R.id.download).setOnClickListener(new View.OnClickListener() {
                                                             @Override
                                                             public void onClick(View v) {
                                                               ad.show();
                                                                 final CheckBox pub = viw.findViewById(R.id.pub);
                                                                 final CheckBox priv = viw.findViewById(R.id.priv);
                                                               viw.findViewById(R.id.loginbut).setOnClickListener(new View.OnClickListener() {
                                                                   @Override
                                                                   public void onClick(View v) {
                                                                       a = pub.isChecked();
                                                                       b = priv.isChecked();
                                                                       if(a||b)
                                                                       {
                                                                          download_keys(a,b);
                                                                       }
                                                                   }
                                                               });
                                                             }
                                                         });
        v.findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message m = new Message();
                Bundle b = new Bundle();
                b.putInt("code",0);
                m.setData(b);
                handler.sendMessage(m);
            }
        });
        TextInputEditText tv = v.findViewById(R.id.email);
        tv.setText(grids.usern);
        return v;
    }
    public acc_fragments(int type1,Handler handle,String em,String pas,RequestQueue queue)
    {
        this.email = em;
        this.q = queue;
        this.password = pas;
        this.handler = handle;
        this.type = type1;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==99)
        {
            download_keys(a,b);
        }
    }

    private void download_keys(final boolean a, final boolean b) {
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.M) {
            if (getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                final ProgressDialog pd = new ProgressDialog(getContext(), R.style.slert);
                pd.setMessage("Downloading Keys.....");
                pd.setCancelable(false);
                pd.show();
                ad.cancel();
                ad.dismiss();
                StringRequest sr = new StringRequest(Request.Method.POST, "http://consulto.oromap.in/keys.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            pd.cancel();
                            pd.dismiss();
                            JSONObject ob = new JSONObject(response);
                            String pub = "";
                            String priv = "";
                            FileWriter fos;
                            File file;
                            if(a&&b)
                            {
                                pub = ob.getString("pubkey");
                                priv = ob.getString("privkey");
                                file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "gm_pub.asc");
                                fos = new FileWriter(file);
                                fos.write(pub);
                                fos.flush();
                                fos.close();
                                file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "gm_priv.asc");
                                fos = new FileWriter(file);
                                fos.write(priv);
                                fos.flush();
                                fos.close();
                                Toast.makeText(getContext(),"Keys Stored to /Downloads",Toast.LENGTH_LONG).show();
                            }
                            else if(a)
                            {
                                pub = ob.getString("pubkey");
                                file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "gm_pub.asc");
                                fos = new FileWriter(file);
                                fos.write(pub);
                                fos.flush();
                                fos.close();
                                Toast.makeText(getContext(),"Public Key Stored to /Downloads",Toast.LENGTH_LONG).show();
                            }
                            else if(b)
                            {
                                priv = ob.getString("privkey");
                                file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "gm_priv.asc");
                               fos = new FileWriter(file);
                               fos.write(priv);
                              fos.flush();
                                fos.close();
                                Toast.makeText(getContext(),"Private Key Stored to /Downloads",Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(getContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map hash = new HashMap<String, String>();
                        hash.put("email", email);
                        int aa = (a)?1:0;
                        int bb = (b)?1:0;
                        hash.put("pub", String.valueOf(aa));
                        hash.put("priv", String.valueOf(bb));
                        hash.put("password", password);
                        return hash;
                    }
                };
                q.add(sr);
            } else {
                String pers[] = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
                ActivityCompat.requestPermissions(getActivity(),pers,99);
            }
        }
    }
}