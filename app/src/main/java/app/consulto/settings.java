package app.consulto;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;

public class settings extends AppCompatActivity {
private AlertDialog a;
private  FileWriter fos;
private int o = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            getWindow().setStatusBarColor(getResources().getColor(R.color.background));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
       Spinner s = (Spinner)findViewById(R.id.spinner);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               a.show();
               try {
                   fos= new FileWriter(new File(getFilesDir(),"theme.txt"));
                   fos.write(String.valueOf(id));
                   fos.close();
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });
       AlertDialog.Builder ad=  new AlertDialog.Builder(this);
        ad.setMessage("Please restart app to change theme.");
        ad.setCancelable(false);
        ad.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                a.cancel();
                a.dismiss();
            }
        });
      a =ad.create();
    }
}