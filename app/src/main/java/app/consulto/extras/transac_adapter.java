package app.consulto.extras;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;

import app.consulto.R;
import app.consulto.grids;

public class transac_adapter extends ArrayAdapter
{
    private JSONArray ob;
    private Handler h;
    public transac_adapter(Context c, int res, JSONArray arr, Handler handler)
    {
        super(c,res);
        this.h = handler;
        this.ob = arr;
    }

    @Override
    public int getCount() {
        return ob.length();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.transac_listview,null);
        ImageView im = convertView.findViewById(R.id.statusimage);
        try {
            String status = ob.getJSONObject((ob.length()-1)-position).getString("status");
            String amount = ob.getJSONObject((ob.length()-1)-position).getString("amount");
            String orderid = ob.getJSONObject((ob.length()-1)-position).getString("orderid");
            String type = ob.getJSONObject((ob.length()-1)-position).getString("type");
            String date = ob.getJSONObject((ob.length()-1)-position).getString("date");
            String response = ob.getJSONObject((ob.length()-1)-position).getString("response");
            String txnid = ob.getJSONObject((ob.length()-1)-position).getString("txnid");
            TextView t = convertView.findViewById(R.id.orderid);
            TextView k = convertView.findViewById(R.id.amount);
            k.setText("Rs "+amount);
            t.setText("Order Id    :   "+orderid);
            if(status.contains("TXN_SUCCESS"))
            {
                // im.setColorFilter(Color.GREEN);
                im.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_baseline_beenhere_24));
            }
            else
            {
                //  im.setColorFilter(Color.RED);
                im.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_baseline_error_outline_24));
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message m = new Message();
                    Bundle b = new Bundle();
                    b.putString("amount",amount);
                    b.putString("status",status);
                    b.putString("txnid",txnid);
                    b.putString("type",type);
                    b.putString("orderid",orderid);
                    b.putString("date",date);
                    b.putInt("o", grids.o);
                    m.setData(b);
                    h.sendMessage(m);
                }
            });
        }catch (Exception e){
            Toast.makeText(getContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
        }
        return convertView;
    }
}
