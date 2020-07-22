package app.consulto.extras;

import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import app.consulto.R;
import app.consulto.grids;

public class acc extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.account,container,false);
        view.findViewById(R.id.settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message m = new Message();
                Bundle b = new Bundle();
                b.putInt("code",1);
                m.setData(b);
                grids.logouthandler.sendMessage(m);
            }
        });
        TextView tv = (TextView)view.findViewById(R.id.name);
        tv.setText(grids.name.trim());
        TabLayout tabs = (TabLayout)view.findViewById(R.id.acc_tabs);
        ViewPager vp = (ViewPager)view.findViewById(R.id.vp3333);
        acc_pager_adapter page = new acc_pager_adapter(getParentFragmentManager(),grids.type,grids.logouthandler,grids.mysubscriptions,grids.email,grids.password,grids.queue);
        vp.setAdapter(page);
        tabs.setupWithViewPager(vp);
        return view;
    }
}