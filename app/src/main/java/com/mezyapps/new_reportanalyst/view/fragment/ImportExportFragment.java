package com.mezyapps.new_reportanalyst.view.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mezyapps.new_reportanalyst.R;
public class ImportExportFragment extends Fragment {

    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_import_export, container, false);

        find_View_IdS(view);
        events();
        return view;
    }

    private void find_View_IdS(View view) {

    }

    private void events() {

    }


}
