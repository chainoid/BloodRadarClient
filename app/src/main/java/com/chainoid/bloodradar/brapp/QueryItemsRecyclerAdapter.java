package com.chainoid.bloodradar.brapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class QueryItemsRecyclerAdapter extends RecyclerView.Adapter<QueryItemsRecyclerAdapter.MyViewHolder>{
    private Context mContext;
    private String[] donorID;
    private String[] name;
    private String[] phone;
    private String[] address;
    private String[] ssn;
    private String[] age;
    private String[] sex;

    private String[] uniqueID;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtOwner;
        public TextView txtAddress;
        public TextView txtPhone;
        public TextView txtSSN;
        public TextView txtDonorID;
        public TextView txtAge;
        public TextView txtSex;

        public MyViewHolder(View view) {
            super(view);
         //   txtOwner = (TextView) view.findViewById(R.id.txtAddress);
            txtAddress = (TextView) view.findViewById(R.id.txtAddress);
            txtPhone = (TextView) view.findViewById(R.id.txtPhone);
        //    txtUniqueID = (TextView) view.findViewById(R.id.txtUniqueID);
            txtDonorID = (TextView) view.findViewById(R.id.txtDonorID);
            txtAge = (TextView) view.findViewById(R.id.txtAge);
            txtSSN = (TextView) view.findViewById(R.id.txtSSN);
            txtSex = (TextView) view.findViewById(R.id.txtSex);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.query_item_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    public QueryItemsRecyclerAdapter(String[] donorID, String[] name, String[] phone, String[] address, String[] ssn, String[] age, String[] sex, Context context) {
        this.donorID = donorID;
        this.mContext=context;
        this.name = name;
        this.phone =phone;
        this.address=address;
        this.ssn=ssn;
        this.age=age;
        this.sex=sex;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
     //   holder.txtOwner.setText(name[position]);
        holder.txtAddress.setText(address[position]);
        holder.txtDonorID.setText(donorID[position]);
        holder.txtPhone.setText(phone[position]);
        holder.txtSSN.setText(ssn[position]);
        holder.txtAge.setText(age[position]);
        holder.txtSex.setText(sex[position]);
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
