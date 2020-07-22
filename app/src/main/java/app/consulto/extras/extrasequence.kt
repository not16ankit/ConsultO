package app.consulto.extras

import android.content.Context
import app.consulto.R
import kotlin.random.Random

class extrasequence(con: Context) {
    lateinit var c:Context
    init {
        c = con
    }
    fun faiza(m:String):String
    {
        val jumble ="a43317ba1fe"+ m+"39b04d6925cef";
        return jumble;
    }
}