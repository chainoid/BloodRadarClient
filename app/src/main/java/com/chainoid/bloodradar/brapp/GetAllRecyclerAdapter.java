package com.chainoid.bloodradar.brapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class GetAllRecyclerAdapter extends RecyclerView.Adapter<GetAllRecyclerAdapter.MyViewHolder>{
    private Context mContext;
    private String[] donorID;
    private String[] owner;
    private String[] phone;
    private String[] address;
    private String[] age;

    private String[] uniqueID;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtOwner;
        public TextView txtAddress;
        public TextView txtPhone;
        public TextView txtUniqueID;
        public TextView txtDonorID;
        public TextView txtAge;

        public MyViewHolder(View view) {
            super(view);
            txtOwner = (TextView) view.findViewById(R.id.txtAddress);
            txtAddress = (TextView) view.findViewById(R.id.txtAddress);
            txtPhone = (TextView) view.findViewById(R.id.txtPhone);
            txtUniqueID = (TextView) view.findViewById(R.id.txtUniqueID);
            txtDonorID = (TextView) view.findViewById(R.id.txtDonorID);
            txtAge = (TextView) view.findViewById(R.id.txtAge);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.get_products_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    public GetAllRecyclerAdapter(String[] productID,String[] owner,String[] timestamp,String[] location,String[] uniqueID, String[] age, Context context) {
        this.donorID = productID;
        this.mContext=context;
        this.owner=owner;
        this.phone =timestamp;
        this.address=location;
        this.uniqueID=uniqueID;
        this.age = age;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.txtOwner.setText(owner[position]);
        holder.txtAddress.setText(address[position]);
        holder.txtDonorID.setText(donorID[position]);
        holder.txtPhone.setText(phone[position]);
        holder.txtUniqueID.setText(uniqueID[position]);
        holder.txtAge.setText(age[position]);
        animate(holder,mContext);
    }

    @Override
    public int getItemCount() {
        return donorID.length;
    }

    public void animate(RecyclerView.ViewHolder viewHolder,Context context) {
        final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(context, R.anim.anticipateovershoot_interpolator);
        viewHolder.itemView.setAnimation(animAnticipateOvershoot);
    }
}
