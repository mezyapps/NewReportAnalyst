package com.mezyapps.new_reportanalyst.view.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mezyapps.new_reportanalyst.R;
import com.mezyapps.new_reportanalyst.connection.ConnectionCommon;

import java.sql.Connection;

public class HomeFragment extends Fragment {

    private Context mContext;
    private ConnectionCommon connectionCommon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_home, container, false);
        mContext=getActivity();

        find_View_IdS(view);
        events();

        return view;
    }

    private void find_View_IdS(View view) {
        connectionCommon=new ConnectionCommon();
        Connection connection=connectionCommon.connectionDatabase();
        if(connection==null)
        {
            Toast.makeText(mContext, "Connection Problem Try Again", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(mContext, "Connection Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    private void events() {

    }


}
