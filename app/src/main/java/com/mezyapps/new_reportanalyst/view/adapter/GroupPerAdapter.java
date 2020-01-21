package com.mezyapps.new_reportanalyst.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mezyapps.new_reportanalyst.R;
import com.mezyapps.new_reportanalyst.model.GroupPerModel;
import com.mezyapps.new_reportanalyst.model.ProductTableModel;

import java.util.ArrayList;

public class GroupPerAdapter extends ArrayAdapter<GroupPerModel> {

    private ArrayList<GroupPerModel> groupPerModelArrayList;
    private Context mContext;
    private LayoutInflater inflater;

    public GroupPerAdapter(Context context, ArrayList<GroupPerModel> groupPerModelArrayList) {
        super(context, R.layout.list_groupper_auto_complete_adapter);
        this.mContext = context;
        this.groupPerModelArrayList = groupPerModelArrayList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        view = inflater.inflate(R.layout.list_groupper_auto_complete_adapter, null);
        GroupPerModel groupPerModel = getItem(position);
        TextView textGroup_name = view.findViewById(R.id.textGroup_name);
        LinearLayout ll_group_list = view.findViewById(R.id.ll_group_list);

        textGroup_name.setText(groupPerModel.getGROUPNAME());
        view.setTag(groupPerModel);

        if (position % 2 == 1) {
            ll_group_list.setBackgroundResource(R.color.white);
        } else {
            ll_group_list.setBackgroundResource(R.color.ice_cream);
        }

        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((GroupPerModel) (resultValue)).getGROUPNAME();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results=new FilterResults();
            ArrayList<GroupPerModel> suggestions=new ArrayList<>();



            if(constraint==null || constraint.length()==0)
            {
                suggestions.addAll(groupPerModelArrayList);
            }
            else
            {
                String filterPattern=constraint.toString().toLowerCase().trim();

                for (GroupPerModel groupPerModel :groupPerModelArrayList)
                {
                    if (groupPerModel.getGROUPNAME().toLowerCase().contains(filterPattern))
                    {
                        suggestions.add(groupPerModel);
                    }
                }
            }

            results.values=suggestions;
            results.count=suggestions.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            if (results != null && results.count > 0) {
                addAll((ArrayList<GroupPerModel>) results.values);
            }
            notifyDataSetChanged();
        }

    };
}
