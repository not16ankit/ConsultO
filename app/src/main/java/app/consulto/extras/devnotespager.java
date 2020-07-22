package app.consulto.extras;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import app.consulto.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class devnotespager extends FragmentPagerAdapter {
    private String[] quotes;
    private Handler ghandler;
    public devnotespager(FragmentManager fm,String[] quote,Handler h)
    {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.ghandler = h;
        this.quotes = quote;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return new frag(quotes[position],ghandler);
    }

    @Override
    public int getCount() {
        return quotes.length;
    }
}

