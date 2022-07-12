package com.example.try1;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Locale;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{
    private Item[] listdata;
    private TextToSpeech mTTS;
    Context cont;
    public Adapter(Item[] listdata) {
        this.listdata = listdata;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         cont = parent.getContext();
        mTTS = new TextToSpeech(parent.getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = 0;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        result = mTTS.setLanguage(Locale.forLanguageTag("ar"));
                    }
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.itemlayout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.imageView.setImageResource(listdata[position].getImage());
        final String name=listdata[position].getName();
        final int image=listdata[position].getImage();
        final int type=listdata[position].getType();
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation= AnimationUtils.loadAnimation(cont,R.anim.blink_anim);
                holder.imageView.startAnimation(animation);
                mTTS.speak(name, TextToSpeech.QUEUE_FLUSH, null);


                    String key="name";
                    String key1="image";
                    String key2="type";
                    final Intent intent;
                    intent =  new Intent(cont, Details.class);
                    intent.putExtra(key, name);
                    intent.putExtra(key1,image);
                    intent.putExtra(key2,type);

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
        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }

}