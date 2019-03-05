package com.chainoid.bloodradar.brapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class OptionsRecyclerAdapter extends RecyclerView.Adapter<OptionsRecyclerAdapter.MyViewHolder>{
    private OnOptionsRecyclerViewItemClickListener listener;
    private Context mContext;
    private String[] optionsList;
    private Integer[] optionsIcon;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView imgOptionsIcon;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.options_name);
            imgOptionsIcon = (ImageView) view.findViewById(R.id.img);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.options_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    public OptionsRecyclerAdapter(String[] optionsList,Integer[] optionsIcon,Context context) {
        this.optionsList = optionsList;
        this.mContext=context;
        this.optionsIcon=optionsIcon;
    }

    public void setOnItemClickListener(OnOptionsRecyclerViewItemClickListener listener)
    {
        this.listener = listener;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.name.setText(optionsList[position]);
        holder.imgOptionsIcon.setImageResource(optionsIcon[position]);
        animate(holder,mContext);
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                listener.onRecyclerViewItemClicked(position, -1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return optionsList.length;
    }

    public void animate(RecyclerView.ViewHolder viewHolder,Context context) {
        final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(context, R.anim.anticipateovershoot_interpolator);
        viewHolder.itemView.setAnimation(animAnticipateOvershoot);
    }
}
