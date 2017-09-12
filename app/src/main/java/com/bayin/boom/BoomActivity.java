package com.bayin.boom;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bayin.boom.bomb.BombLayout;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2017/9/12.
 ****************************************/

public class BoomActivity extends AppCompatActivity {

    private BombLayout bombLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boom);

        bombLayout = (BombLayout) findViewById(R.id.bomb_layout);

        findViewById(R.id.bt_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bombLayout.startTimeCount();
            }
        });
    }
}
