package com.mezyapps.new_reportanalyst.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mezyapps.new_reportanalyst.R;
import com.mezyapps.new_reportanalyst.db.entity.OrderEntryProduct;
import com.mezyapps.new_reportanalyst.utils.SelectProductDataInterface;

import java.util.ArrayList;

public class OrderEntryProductAdapter extends RecyclerView.Adapter<OrderEntryProductAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<OrderEntryProduct> orderEntryProductArrayList;
    int cnt = 1;
    private boolean isDialog;
    private SelectProductDataInterface selectProductDataInterface;

    public OrderEntryProductAdapter(Context mContext, ArrayList<OrderEntryProduct> orderEntryProductArrayList, boolean isDialog,
                                    SelectProductDataInterface selectProductDataInterface) {
        this.mContext = mContext;
        this.orderEntryProductArrayList = orderEntryProductArrayList;
        this.isDialog = isDialog;
        this.selectProductDataInterface = selectProductDataInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_order_entry_product_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final OrderEntryProduct orderEntryProduct = orderEntryProductArrayList.get(position);

        holder.textProductName.setText("(" + cnt + ") " + orderEntryProduct.getProduct_name());
        holder.textDist.setText(orderEntryProduct.getDist_amt1());
        holder.textDist2.setText(orderEntryProduct.getDist_amt2());
        holder.textGst.setText(orderEntryProduct.getGst_amt());
        holder.textSubTotal.setText(orderEntryProduct.getFinal_total());
        cnt++;
        if (isDialog) {
            holder.iv_edit_product.setVisibility(View.VISIBLE);
        } else {
            holder.iv_edit_product.setVisibility(View.GONE);
        }

        holder.iv_edit_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectProductDataInterface.selectProductData(orderEntryProduct);
            }
        });

        String qty_rate="Qty "+orderEntryProduct.getQty() +" X "+" Rate "+orderEntryProduct.getRate();

        holder.textSubTotalAmt.setText(orderEntryProduct.getSub_total());
        holder.textQty_rate.setText(qty_rate);
    }

    @Override
    public int getItemCount() {
        return orderEntryProductArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textProductName, textDist, textGst, textSubTotal, textQty_rate, textSubTotalAmt,textDist2;
        private ImageView iv_edit_product;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textProductName = itemView.findViewById(R.id.textProductName);
            textDist = itemView.findViewById(R.id.textDist);
            textGst = itemView.findViewById(R.id.textGst);
            textSubTotal = itemView.findViewById(R.id.textSubTotal);
            iv_edit_product = itemView.findViewById(R.id.iv_edit_product);
            textQty_rate = itemView.findViewById(R.id.textQty_rate);
            textSubTotalAmt = itemView.findViewById(R.id.textSubTotalAmt);
            textDist2 = itemView.findViewById(R.id.textDist2);
        }
    }
}
