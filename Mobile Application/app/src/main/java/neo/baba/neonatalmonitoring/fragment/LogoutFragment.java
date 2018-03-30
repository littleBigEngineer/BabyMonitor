package neo.baba.neonatalmonitoring.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import neo.baba.neonatalmonitoring.R;

/**
 * Created by rober on 07/03/2018.
 */

public class LogoutFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.login_activity, container, false);
    }
}
