package app.consulto.extras;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import app.consulto.R;
import app.consulto.grids;
import app.consulto.receipt;

public class wallet extends Fragment
{
    private View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = getLayoutInflater().inflate(R.layout.wallet_view,container,false);
        ImageView pay = (ImageView)view.findViewById(R.id.paytm);
        pay.setImageBitmap(BitmapFactory.decodeFile(new File(getContext().getFilesDir(),"paytm.png").getAbsolutePath()));
        final TextView t =view.findViewById(R.id.money);
        view.findViewById(R.id.refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t.setText(refresh_money());
            }
        });
        set_transac_listview();
        t.setText("Rs "+ grids.money);
        FloatingActionButton fab = view.findViewById(R.id.addmoney);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View ve = getLayoutInflater().inflate(R.layout.money,null);
                final BottomSheetDialog di =new BottomSheetDialog(grids.con,R.style.bag);
                di.setContentView(ve);
                di.show();
                final TextInputEditText amount = ve.findViewById(R.id.amount);
                ve.findViewById(R.id.pay).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(amount.getText().toString().trim().isEmpty())
                        {
                            amount.setError("Amount cannot be null");
                        }
                        else
                        {
                            di.cancel();
                            di.dismiss();
                            pay(amount.getText().toString().trim());
                        }
                    }
                });
            }
        });
        return view;
    }
    private String refresh_money()
    {
        return "Rs "+grids.money;
    }
    private void set_transac_listview()
    {
        Intent i = new Intent(grids.con, receipt.class);
        final Handler h = new Handler()
        {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                i.putExtras(msg.getData());
                startActivity(i);
            }
        };
        ListView listView = view.findViewById(R.id.transaction_list);
        StringRequest sr = new StringRequest(Request.Method.POST, "http://consulto.oromap.in/get_list.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject ob = new JSONObject(response);
                    JSONArray arr = ob.getJSONArray("transactions");
                    if(arr.length()==0)
                    {
                        view.findViewById(R.id.notrans).setVisibility(View.VISIBLE);
                    }
                    else {
                        view.findViewById(R.id.notrans).setVisibility(View.INVISIBLE);
                        transac_adapter c = new transac_adapter(grids.con, R.layout.transac_listview, arr,h);
                        listView.setAdapter(c);
                    }
                }catch (Exception e){
                    Toast.makeText(grids.con,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(grids.con,"Error",Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map hash = new HashMap<String,String>();
                hash.put("email",grids.email);
                hash.put("password",grids.password);
                return hash;
            }
        };
        grids.queue.add(sr);
    }
    private void payment(String hash,String amount,String orderid)
    {
        AlertDialog.Builder ab = new AlertDialog.Builder(grids.con,R.style.dialog);
        View v = getLayoutInflater().inflate(R.layout.done,null);
        LottieAnimationView lottie = v.findViewById(R.id.lottie);
        TextView text = v.findViewById(R.id.text);
        v.findViewById(R.id.loginbut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grids.ad.cancel();
                grids.ad.dismiss();
            }
        });
        ab.setView(v);
        ab.setCancelable(false);
        grids.ad = ab.create();
        grids.ad.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        PaytmPGService service = PaytmPGService.getStagingService("");
        HashMap<String,String> ob = new HashMap<String,String>();
        ob.put("MID",getResources().getString(R.string.mid));
        ob.put("ORDER_ID",orderid);
        ob.put("CUST_ID",grids.email);
        ob.put("CHANNEL_ID","WAP");
        ob.put("CHECKSUMHASH",hash);
        ob.put("INDUSTRY_TYPE_ID","Retail");
        ob.put("TXN_AMOUNT",amount+".00");
        ob.put("WEBSITE","WEBSTAGING");
        ob.put("CALLBACK_URL","https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID="+orderid);
        PaytmOrder order =  new PaytmOrder(ob);
        service.initialize(order,null);
        service.startPaymentTransaction(grids.con, true, true, new PaytmPaymentTransactionCallback() {
            @Override
            public void onTransactionResponse(Bundle inResponse) {
                view.findViewById(R.id.pb).setVisibility(View.VISIBLE);
                view.findViewById(R.id.moneyview).setVisibility(View.INVISIBLE);
                final Map hash = new HashMap<String,String>();
                hash.put("status",inResponse.get("STATUS").toString());
                hash.put("orderid",inResponse.get("ORDERID").toString());
                hash.put("type",inResponse.get("BANKNAME").toString());
                hash.put("amount",inResponse.get("TXNAMOUNT").toString());
                hash.put("response",inResponse.get("RESPMSG").toString());
                hash.put("date",inResponse.get("TXNDATE").toString());
                hash.put("txnid",inResponse.get("TXNID").toString());
                hash.put("email",grids.email);
                hash.put("hash",grids.unicode);
                hash.put("password",grids.password);
                StringRequest sr = new StringRequest(Request.Method.POST, "http://consulto.oromap.in/transaction.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        grids.money = response.trim();
                        grids.ad.show();
                        if(inResponse.get("STATUS").toString().contains("SUCCESS"))
                        {
                            text.setText("Money added successfully");
                            lottie.setAnimation("done.json");
                            lottie.playAnimation();
                        }
                        else
                        {
                            text.setText("Error in Transaction");
                            lottie.setAnimation("error.json");
                            lottie.playAnimation();
                        }
                        TextView t =view.findViewById(R.id.money);
                        t.setText("Rs "+grids.money);
                        view.findViewById(R.id.pb).setVisibility(View.INVISIBLE);
                        view.findViewById(R.id.moneyview).setVisibility(View.VISIBLE);
                        set_transac_listview();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        return hash;
                    }
                };
                grids.queue.add(sr);
            }

            @Override
            public void networkNotAvailable() {
            }

            @Override
            public void clientAuthenticationFailed(String inErrorMessage) {

            }

            @Override
            public void someUIErrorOccurred(String inErrorMessage) {

            }

            @Override
            public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {

            }

            @Override
            public void onBackPressedCancelTransaction() {
            }

            @Override
            public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
            }
        });
    }
    private void pay(String amount) {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            if (getContext().checkSelfPermission(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
                grids.pd.show();
                grids.unicode = DigestUtils.shaHex(String.valueOf((new Random()).nextInt(500000000)));
                String orderid = String.valueOf((new  Random()).nextInt(2000000));
                StringRequest sr = new StringRequest(Request.Method.POST, "http://consulto.oromap.in/sample.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        grids.pd.cancel();
                        grids.pd.dismiss();
                        payment(response.trim(),amount,orderid);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),"Error",Toast.LENGTH_LONG).show();
                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map hash = new HashMap<String,String>();
                        hash.put("mid",getResources().getString(R.string.mid));
                        hash.put("orderid",orderid);
                        hash.put("customerid",grids.email);
                        hash.put("channelid","WAP");
                        hash.put("INDUSTRY_TYPE_ID","Retail");
                        hash.put("TXN_AMOUNT",amount+".00");
                        hash.put("WEBSITE","WEBSTAGING");
                        hash.put("email",grids.email);
                        hash.put("password",grids.password);
                        hash.put("hash",grids.unicode);
                        hash.put("CALLBACK_URL","https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID="+orderid);
                        return hash;
                    }
                };
                grids.queue.add(sr);
            }
            else
            {
                String[] pers = {Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS};
                grids.notgood = amount;
                requestPermissions(pers,44);
            }
        }
    }
}
