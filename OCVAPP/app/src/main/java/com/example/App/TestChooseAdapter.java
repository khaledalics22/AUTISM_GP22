package com.example.App;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ocvapp.R;

public class TestChooseAdapter extends RecyclerView.Adapter<TestChooseAdapter.ViewHolder> {
    private Item[] listdata;
    Context cont;

    public TestChooseAdapter(Item[] listdata) {
        this.listdata = listdata;
    }

    @Override
    public TestChooseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        cont = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.mainitemlayout, parent, false);
        TestChooseAdapter.ViewHolder viewHolder = new TestChooseAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final TestChooseAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.imageView.setImageResource(listdata[position].getImage());
        final String name = listdata[position].getName();

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Animation animation= AnimationUtils.loadAnimation(cont,R.anim.blink_anim);
//                holder.imageView.startAnimation(animation);
//                  holder.txt.setText("aaaaaaaaaaaaa");
                Intent intent;
                String key = "answer";
                intent = new Intent(cont, TestChoose.class);
                intent.putExtra(key, position);
                cont.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return listdata.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        //        public TextView txt;
        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.mainimageView);
//            this.txt=(TextView) itemView.findViewById(R.id.testtext);

        }
    }

}
