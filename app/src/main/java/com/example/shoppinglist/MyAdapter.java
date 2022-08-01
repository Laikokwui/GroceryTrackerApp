package com.example.shoppinglist;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<Item> itemList;
    private ArrayList<Item> tempItemList;
    private String currentFragment;
    private OnItemClickListener onItemClickListener;

    public MyAdapter(String currentFragment, ArrayList<Item> itemArrayList, OnItemClickListener onItemClickListener) {
        this.itemList = itemArrayList;
        this.tempItemList = new ArrayList<>();
        this.currentFragment = currentFragment;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.itemlist, parent,false);
        return new MyViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        if (getItemCount() > 0) {
            Item item = tempItemList.get(position);
            holder.textView_item_name.setText(item.getName());
            holder.textView_size.setText("Size: " + item.getSize());
            holder.textView_quantity.setText("Qty: " + item.getQty());
            holder.switch_bought.setChecked(item.getBought()==1);

            if (item.getBought() == 1) { holder.imageView_icon.setImageResource(R.drawable.bought); }
            else if (item.getUrgent() == 1) { holder.imageView_icon.setImageResource(R.drawable.urgent); }
            else { holder.imageView_icon.setImageResource(R.drawable.buy); }

            if (currentFragment.equals("Completed")) {
                holder.switch_bought.setVisibility(View.INVISIBLE);
                String date = "<b>Date Bought: </b>" + item.getDate();
                holder.textView_date.setText(Html.fromHtml(date));
                holder.textView_date.setVisibility(View.VISIBLE);
            }
            else {
                holder.textView_date.setVisibility(View.INVISIBLE);
                holder.switch_bought.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return tempItemList.size();
    }

    public void setItemList(ArrayList<Item> itemList) {
        this.itemList = new ArrayList<>(itemList);
        ModifyList();
        notifyDataSetChanged();
    }

    public void setFragment(String currentFragment) {
        this.currentFragment = currentFragment;
        ModifyList();
        notifyDataSetChanged();
    }

    private void ModifyList() {
        if (!tempItemList.isEmpty()) { tempItemList.clear(); }

        if ("Completed".equals(currentFragment)) {
            for (Item item : itemList) {
                if (item.getBought() == 1) { tempItemList.add(item); }
            }
        }
        else if ("Home".equals(currentFragment)) {
            for (Item item : itemList) {
                if (item.getBought() == 0) { tempItemList.add(item); }
            }
        }
        else if ("Urgent".equals(currentFragment)) {
            for (Item item : itemList) {
                if (item.getBought() == 0 && item.getUrgent() == 1) { tempItemList.add(item); }
            }
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView textView_item_name, textView_quantity, textView_size, textView_date;
        private ImageView imageView_icon;
        private SwitchCompat switch_bought;
        private OnItemClickListener onItemClickListener;

        public MyViewHolder(@NonNull View itemView, final OnItemClickListener onItemClickListener) {
            super(itemView);
            textView_item_name = itemView.findViewById(R.id.textView_show_item);
            textView_quantity = itemView.findViewById(R.id.textView_show_quantity);
            textView_size = itemView.findViewById(R.id.textView_show_size);
            textView_date = itemView.findViewById(R.id.textView_dateBoughtText);
            imageView_icon = itemView.findViewById(R.id.imageView_icon);
            this.onItemClickListener = onItemClickListener;

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            switch_bought = itemView.findViewById(R.id.Switch_bought);

            if (currentFragment.equals("Completed")) { // For completed
                switch_bought.setVisibility(View.GONE);
            }
            else { // For all or urgent
                textView_date.setVisibility(View.GONE);
                switch_bought.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        int clickPos = getAdapterPosition();
                        Item item = tempItemList.get(clickPos);
                        if (compoundButton.isChecked() && item.getBought() == 0) { onItemClickListener.onToggleSwitch(item); }
                    }
                });
            }
        }

        @Override
        public void onClick(View v) {
            Item item = tempItemList.get(getAdapterPosition());
            onItemClickListener.onClick(item, currentFragment);
        }

        @Override
        public boolean onLongClick(View v) {
            Item item = tempItemList.get(getAdapterPosition());
            onItemClickListener.onLongClick(item);
            return true;
        }
    }

    public interface OnItemClickListener {
        void onToggleSwitch(Item item);
        void onClick(Item item, String currentFragment);
        void onLongClick(Item item);
    }
}
