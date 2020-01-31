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

import com.mezyapps.new_reportanalyst.R;
import com.mezyapps.new_reportanalyst.model.ProductTableModel;

import java.util.ArrayList;

public class ProductAutoCompleteAdapter extends ArrayAdapter<ProductTableModel> {

    private ArrayList<ProductTableModel> productTableModelArrayList;
    private Context mContext;
    private LayoutInflater inflater;

    public ProductAutoCompleteAdapter(Context context, ArrayList<ProductTableModel> productTableModelArrayList) {
        super(context, R.layout.list_product_auto_complete_adapter);
        this.mContext = context;
        this.productTableModelArrayList = productTableModelArrayList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        view = inflater.inflate(R.layout.list_product_auto_complete_adapter, null);
        ProductTableModel productTableModel = getItem(position);
        TextView textProd_Name = view.findViewById(R.id.textProd_Name);
        TextView textProd_price = view.findViewById(R.id.textProd_price);
        LinearLayout ll_product_list = view.findViewById(R.id.ll_product_list);

        textProd_price.setVisibility(View.GONE);
     //   textProd_price.setText(productTableModel.getSALERATE1());
        textProd_Name.setText(productTableModel.getPMSTNAME());
        view.setTag(productTableModel);

        if (position % 2 == 1) {
            ll_product_list.setBackgroundResource(R.color.white);
        } else {
            ll_product_list.setBackgroundResource(R.color.ice_cream);
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
            String str = ((ProductTableModel) (resultValue)).getPMSTNAME();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
           FilterResults results=new FilterResults();
           ArrayList<ProductTableModel> suggestions=new ArrayList<>();



           if(constraint==null || constraint.length()==0)
           {
               suggestions.addAll(productTableModelArrayList);
           }
           else
           {
               String filterPattern=constraint.toString().toLowerCase().trim();

               for (ProductTableModel productTableModel :productTableModelArrayList)
               {
                   if (productTableModel.getPMSTNAME().toLowerCase().contains(filterPattern))
                   {
                       suggestions.add(productTableModel);
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
                addAll((ArrayList<ProductTableModel>) results.values);
            }
            notifyDataSetChanged();
        }

    };
}
