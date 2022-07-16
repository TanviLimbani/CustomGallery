package com.tl.gallerydemo;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;
import java.util.HashMap;


public class Folders_Adapter extends RecyclerView.Adapter<Folders_Adapter.ViewHolder> {
    itemClick click;
    Context context;
    HashMap<String, ArrayList<ImageData>> folderData = new HashMap<>();
    ArrayList<String> titleList = new ArrayList<>();
    int pos = 0;

    
    public interface itemClick {
        void OnClick(ArrayList<ImageData> arrayList, String str);
    }

    @Override
    public long getItemId(int i) {
        return (long) i;
    }

    public void setData(HashMap<String, ArrayList<ImageData>> hashMap, ArrayList<String> arrayList) {
        this.folderData = hashMap;
        this.titleList = arrayList;
        notifyDataSetChanged();
    }

    public Folders_Adapter(Context context, itemClick itemclick) {
        this.context = context;
        this.click = itemclick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_fol_list, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        TextView textView = viewHolder.txt_title;
        textView.setText(this.titleList.get(i) + "");
        TextView textView2 = viewHolder.txt_size;
        textView2.setText("( " + this.folderData.get(this.titleList.get(i)).size() + " )");
        if (TextUtils.isEmpty(this.titleList.get(i))) {
            return;
        }
        if (this.titleList.get(i).equalsIgnoreCase(this.titleList.get(this.pos))) {
            viewHolder.li_main.setBackgroundColor(this.context.getResources().getColor(R.color.purple_500));
            viewHolder.img_selected.setVisibility(0);
            viewHolder.txt_title.setTextColor(this.context.getResources().getColor(R.color.white));
            viewHolder.txt_size.setTextColor(this.context.getResources().getColor(R.color.white));
            return;
        }
        viewHolder.li_main.setBackgroundColor(this.context.getResources().getColor(R.color.white));
        viewHolder.img_selected.setVisibility(4);
        viewHolder.txt_title.setTextColor(this.context.getResources().getColor(R.color.black));
        viewHolder.txt_size.setTextColor(this.context.getResources().getColor(R.color.gray));
    }

    @Override 
    public int getItemCount() {
        return this.titleList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_selected;
        LinearLayout li_main;
        TextView txt_size;
        TextView txt_title;

        public ViewHolder(View view) {
            super(view);
            this.li_main = (LinearLayout) view.findViewById(R.id.li_main);
            this.txt_size = (TextView) view.findViewById(R.id.txt_size);
            this.txt_title = (TextView) view.findViewById(R.id.txt_title);
            this.img_selected = (ImageView) view.findViewById(R.id.img_selected);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view2) {
                    Folders_Adapter.this.pos = ViewHolder.this.getAdapterPosition();
                    if (ViewHolder.this.getAdapterPosition() != -1 && ViewHolder.this.getAdapterPosition() < Folders_Adapter.this.titleList.size()) {
                        Folders_Adapter.this.click.OnClick(Folders_Adapter.this.folderData.get(Folders_Adapter.this.titleList.get(ViewHolder.this.getAdapterPosition())), Folders_Adapter.this.titleList.get(ViewHolder.this.getAdapterPosition()));
                        Folders_Adapter.this.notifyDataSetChanged();
                    }
                }
            });
        }
    }
}
