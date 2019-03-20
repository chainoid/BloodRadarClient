package com.chainoid.bloodradar.brapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class ItemHistoryRecyclerAdapter extends RecyclerView.Adapter<ItemHistoryRecyclerAdapter.MyViewHolder>{
    private Context mContext;

    private String[] bpackId;
    private String[] txTS;

    //private String[] donorId;
    //private String[] donationTS;
    //private String[] amount;

    private String[] holder;
    private String[] status;

    //private String[] uniqueID;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtBpackId;

        public TextView txtHolder;
        public TextView txtStatus;

        public TextView txtTxTS;
        //public TextView txtDonationTS;
        //public TextView txtAmount;
        //public TextView txtLocation;

        public MyViewHolder(View view) {
            super(view);


            txtBpackId = (TextView) view.findViewById(R.id.txtBpackId);
            txtHolder  = (TextView) view.findViewById(R.id.txtHolder);
            txtStatus  = (TextView) view.findViewById(R.id.txtStatus);

            txtTxTS    = (TextView) view.findViewById(R.id.txtTxTS);

            //txtDonationTS = (TextView) view.findViewById(R.id.txtDonationTS);
            //txtStatus  = (TextView) view.findViewById(R.id.txtStatus);
            //txtLocation = (TextView) view.findViewById(R.id.txtLocation);


        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    public ItemHistoryRecyclerAdapter(String[] bpackId, String[] txTS,  String[] packHolder, String[] status, Context context) {

        this.bpackId = bpackId;
        this.txTS    = txTS;
        this.holder = packHolder;
        this.status = status;

        //this.donorId = donorId;
        //this.donationTS = donationTS;
        //this.amount = amount;

        this.mContext=context;

    }

    @Override
    public void onBindViewHolder(MyViewHolder myHolder, final int position) {

        myHolder.txtBpackId.setText(bpackId[position]);
        myHolder.txtTxTS.setText(txTS[position]);

        myHolder.txtHolder.setText(holder[position]);
        myHolder.txtStatus.setText(status[position]);

        animate(myHolder, mContext);
    }

    @Override
    public int getItemCount() {
        return bpackId.length;
    }

    public void animate(RecyclerView.ViewHolder viewHolder,Context context) {
        final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(context, R.anim.anticipateovershoot_interpolator);
        viewHolder.itemView.setAnimation(animAnticipateOvershoot);
    }
}
