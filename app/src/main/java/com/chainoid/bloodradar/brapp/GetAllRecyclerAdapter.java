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
    private String[] productID;
    private String[] owner;
    private String[] timestamp;
    private String[] location;
    private String[] uniqueID;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtOwner;
        public TextView txtLocation;
        public TextView txtTS;
        public TextView txtUniqueID;
        public TextView txtProductID;

        public MyViewHolder(View view) {
            super(view);
            txtOwner = (TextView) view.findViewById(R.id.txtOwner);
            txtLocation = (TextView) view.findViewById(R.id.txtLocation);
            txtTS = (TextView) view.findViewById(R.id.txtTS);
            txtUniqueID = (TextView) view.findViewById(R.id.txtUniqueID);
            txtProductID = (TextView) view.findViewById(R.id.txtProductID);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.get_products_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    public GetAllRecyclerAdapter(String[] productID,String[] owner,String[] timestamp,String[] location,String[] uniqueID,Context context) {
        this.productID = productID;
        this.mContext=context;
        this.owner=owner;
        this.timestamp=timestamp;
        this.location=location;
        this.uniqueID=uniqueID;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.txtOwner.setText(owner[position]);
        holder.txtLocation.setText(location[position]);
        holder.txtProductID.setText(productID[position]);
        holder.txtTS.setText(timestamp[position]);
        holder.txtUniqueID.setText(uniqueID[position]);
        animate(holder,mContext);
    }

    @Override
    public int getItemCount() {
        return productID.length;
    }

    public void animate(RecyclerView.ViewHolder viewHolder,Context context) {
        final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(context, R.anim.anticipateovershoot_interpolator);
        viewHolder.itemView.setAnimation(animAnticipateOvershoot);
    }
}
