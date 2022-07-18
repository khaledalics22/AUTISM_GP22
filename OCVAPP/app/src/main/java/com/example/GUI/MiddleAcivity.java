package com.example.GUI;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ocvapp.R;

public class MiddleAcivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_middle_acivity);
        int[] ids = new int[]{
                R.drawable.food, R.drawable.animals, R.drawable.clothes,
                R.drawable.feelings, R.drawable.people,
                R.drawable.colors, R.drawable.shapes, R.drawable.activities,
                R.drawable.transport, R.drawable.numbers
        };
        int len = ids.length;
        Item[] myListData = new Item[len];
        for (int i = 0; i < len; i++) {
            myListData[i] = new Item("", ids[i], 0);
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        MainAdapter adapter = new MainAdapter(myListData);
        recyclerView.setHasFixedSize(true);
        int col = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(this, col));
        recyclerView.setAdapter(adapter);
    }
}
/*
 *0 like,not like,want
 *1 like,saw,want
 *2 dirty,wear my dress,like
 *3 i'am ,feel,do you feel
 *4 name,love,miss
 *5 it's,favorite,can yo give me
 *6 it's,draw,favorite
 *7 like,not like,want
 *8 take,like,not like
 *9 it's,want,favourite
 * */
