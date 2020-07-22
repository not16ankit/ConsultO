package app.consulto.extras

import android.content.Context
import android.os.Handler
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class pageradapter(c:Context,fm:FragmentManager,text:List<String>,handler:Handler) : FragmentPagerAdapter(fm) {
    lateinit var texts:List<String>
    lateinit var handlerr: Handler
    init {
        handlerr = handler
        texts = text
    }
    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return texts.get(position)
    }

    override fun getItem(position: Int): Fragment {
        var frag:Fragment = signin_frag(handlerr)
        when(position)
        {
            0->
            {
              frag = signin_frag(handlerr)
            }
            1->
            {
                frag = signup_frag(handlerr)
            }
        }
        return frag
    }
}