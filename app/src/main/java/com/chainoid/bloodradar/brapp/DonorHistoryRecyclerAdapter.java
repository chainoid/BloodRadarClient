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
   // private String[] donorId;
   // private String[] donationTS;
   // private String[] amount;
   // private String[] location;
   // private String[] status;

    //private String[] uniqueID;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtName;
        public TextView txtAddress;
        //public TextView txtDonationTS;
        //public TextView txtAmount;
        //public TextView txtLocation;
        //public TextView txtStatus;


        public MyViewHolder(View view) {
            super(view);

            txtName = (TextView) view.findViewById(R.id.txtName);
            txtAddress = (TextView) view.findViewById(R.id.txtAddress);
            //txtDonationTS = (TextView) view.findViewById(R.id.txtDonationTS);
            //txtStatus  = (TextView) view.findViewById(R.id.txtStatus);
            //txtLocation = (TextView) view.findViewById(R.id.txtLocation);
            //txtAmount = (TextView) view.findViewById(R.id.txtAmount);
            //txtStatus  = (TextView) view.findViewById(R.id.txtStatus);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.query_item_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    public DonorHistoryRecyclerAdapter(String[] name, String[] address, Context context) {

        this.name = name;
        this.address   = address;
        //this.donorId = donorId;
        //this.donationTS = donationTS;
        //this.amount = amount;
        //this.location = location;
        //this.status=status;

        this.mContext=context;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
     //   holder.txtOwner.setText(name[position]);

        holder.txtName.setText(name[position]);
        holder.txtAddress.setText(address[position]);
        //holder.txtDonationTS.setText(donationTS[position]);
        //holder.txtAmount.setText(amount[position]);
        //holder.txtLocation.setText(location[position]);
        //holder.txtStatus.setText(status[position]);

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
