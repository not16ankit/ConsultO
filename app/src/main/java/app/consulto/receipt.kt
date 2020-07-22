package app.consulto

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_receipt.*
import java.io.File

class receipt : AppCompatActivity() {
    private var o  =0;
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = resources.getColor(R.color.foreground)
        }
            super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receipt)
        rece.background = BitmapDrawable(resources,BitmapFactory.decodeFile(File(filesDir,"reciept.png").absolutePath))
       val bun = intent.extras
        val amount = bun!!.getString("amount")
        val status = bun.getString("status")!!.replace("TXN_","")
        val type = bun.getString("type")
        val txnid = bun.getString("txnid")
        val orderid = bun.getString("orderid")
        val date = bun.getString("date")!!.trim().split(" ")
        supportActionBar!!.setTitle("Reciept : "+orderid)
        if(status.contains("CC"))
        {
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_beenhere_24))
        }
        else{
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_error_outline_24))
        }
        this.amount.setText("Rs "+amount)
        this.status.setText(status)
        this.type.setText(type)
        this.txnid.setText("TXN ID : "+txnid)
        this.orderid.setText(orderid)
        this.date.setText(date[0])
        this.time.setText(date[1])
    }
}