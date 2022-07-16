package com.tl.gallerydemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;


import java.util.ArrayList;


public class Photos_Adapter extends RecyclerView.Adapter<Photos_Adapter.ViewHolder> {
    itemClick click;
    Context context;
    ArrayList<ImageData> dataList = new ArrayList<>();

    
    public interface itemClick {
        void OnClick(ImageData imageData);
    }

    @Override 
    public long getItemId(int i) {
        return (long) i;
    }

    public void setData(ArrayList<ImageData> arrayList) {
        this.dataList.clear();
        this.dataList.addAll(arrayList);
        notifyDataSetChanged();
    }

    public Photos_Adapter(Context context, itemClick itemclick) {
        this.context = context;
        this.click = itemclick;
    }

    @Override 
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_photos, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Glide.with(this.context).load(this.dataList.get(i).getPath()).apply((BaseRequestOptions<?>) new RequestOptions().centerCrop()).placeholder(ContextCompat.getDrawable(this.context, R.drawable.placeholder2)).into(viewHolder.photos);
    }

    @Override
    public int getItemCount() {
        return this.dataList.size();
    }

    
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView photos;

        public ViewHolder(View view) {
            super(view);
            this.photos = (ImageView) view.findViewById(R.id.photos);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view2) {
                    Photos_Adapter.this.click.OnClick(Photos_Adapter.this.dataList.get(ViewHolder.this.getAdapterPosition()));
                }
            });
        }
    }
}
