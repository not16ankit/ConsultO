package app.consulto;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import app.consulto.extras.acc;
import app.consulto.extras.chats;
import app.consulto.extras.home;
import app.consulto.extras.noti;
import app.consulto.extras.wallet;

public class main_pager extends FragmentStateAdapter {

    public main_pager(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment f = new home();
        switch (position) {
            case 0:
                f = new home();
                break;
            case 1:
                f = new wallet();
                break;
            case 2:
                f = new chats();
                break;
            case 3:
                f = new noti();
                break;
            case 4:
                f = new acc();
                break;
        }
        return f;
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
