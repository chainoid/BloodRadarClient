package com.chainoid.bloodradar.brapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import static com.chainoid.bloodradar.brapp.R.layout.donor_update_layout;

public class UpdateDonorRecyclerAdapter extends RecyclerView.Adapter<UpdateDonorRecyclerAdapter.MyViewHolder>{
    private Context mContext;
    private String[] donorId;
    private String[] name;
    private String[] phone;
    private String[] address;
    private String[] age;

    private String[] uniqueID;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName;
        public TextView txtBtype;
        public TextView txtAddress;
        public TextView txtPhone;
        public TextView txtDonorId;
        public TextView txtAge;


        public MyViewHolder(View view) {
            super(view);
            txtName = (TextView) view.findViewById(R.id.txtName);
            txtAddress = (TextView) view.findViewById(R.id.txtAddress);
            txtPhone = (TextView) view.findViewById(R.id.txtPhone);
            txtBtype = (TextView) view.findViewById(R.id.txtBtype);
            txtDonorId = (TextView) view.findViewById(R.id.txtDonorId);
            txtAge = (TextView) view.findViewById(R.id.txtAge);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(donor_update_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    public UpdateDonorRecyclerAdapter(String[] donorId, String[] name, String[] phone, String[] address, String[] age, Context context) {
        this.donorId = donorId;
        this.mContext=context;
        this.name = name;
        this.phone =phone;
        this.address=address;
        this.age=age;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.txtName.setText(name[position]);
        holder.txtAddress.setText(address[position]);
        holder.txtDonorId.setText(donorId[position]);
        holder.txtPhone.setText(phone[position]);
        holder.txtAge.setText(age[position]);

        animate(holder,mContext);
    }

    @Override
    public int getItemCount() {
        return donorId.length;
    }

    public void animate(RecyclerView.ViewHolder viewHolder,Context context) {
        final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(context, R.anim.anticipateovershoot_interpolator);
        viewHolder.itemView.setAnimation(animAnticipateOvershoot);
    }
}
