package com.chainoid.bloodradar.brapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class DonorHistoryRecyclerAdapter extends RecyclerView.Adapter<DonorHistoryRecyclerAdapter.MyViewHolder>{
    private Context mContext;
    private String[] name;
    private String[] address;
    private String[] txTS;
    private String[] phone;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtName;
        public TextView txtAddress;
        public TextView txtPhone;
        public TextView txtTxTS;

        //public TextView txtLocation;
        //public TextView txtStatus;


        public MyViewHolder(View view) {
            super(view);

            txtName = (TextView) view.findViewById(R.id.txtName);
            txtAddress = (TextView) view.findViewById(R.id.txtAddress);
            txtTxTS = (TextView) view.findViewById(R.id.txtTxTS);
            txtPhone  = (TextView) view.findViewById(R.id.txtPhone);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.donor_history_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    public DonorHistoryRecyclerAdapter(String[] txTS, String[] name, String[] address, String[] phone, Context context) {


        this.txTS      = txTS;
        this.name = name;
        this.address   = address;
        this.phone   = phone;

        this.mContext=context;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.txtName.setText(name[position]);
        holder.txtAddress.setText(address[position]);
        holder.txtPhone.setText(phone[position]);
        holder.txtTxTS.setText(txTS[position]);

        animate(holder,mContext);
    }

    @Override
    public int getItemCount() {
        return name.length;
    }

    public void animate(RecyclerView.ViewHolder viewHolder,Context context) {
        final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(context, R.anim.anticipateovershoot_interpolator);
        viewHolder.itemView.setAnimation(animAnticipateOvershoot);
    }
}
