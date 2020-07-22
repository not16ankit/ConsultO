package app.consulto.extras

import android.content.Context
import android.os.Handler
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.android.volley.RequestQueue

class acc_pager_adapter(fm:FragmentManager,type1:Int,handler: Handler,mysubs:Array<String>,usern:String,passw:String,q:RequestQueue):FragmentPagerAdapter(fm){
    private var type=0
    lateinit var subs:Array<String>;
    lateinit var hand:Handler
    private var username = ""
    private var password = ""
    lateinit var qu:RequestQueue
    init {
        username = usern
        password = passw
        qu = q
        hand = handler
        type = type1
        subs = mysubs;
    }
    override fun getItem(position: Int): Fragment {
        var f:Fragment = acc_fragments(type,hand,username,password,qu)
        when(position)
        {
            0->
            {
                f = acc_fragments(type,hand,username,password,qu)
            }
            1->
            {
                f = two(subs,qu,username,password)
            }
        }
        return f
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        if(position==0)
            return "Account"
        else
            return "My Subscriptions"
    }
}