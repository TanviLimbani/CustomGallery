package com.tl.gallerydemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageShowActivity extends AppCompatActivity {

    private ImageView img_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_show);

        String imge_path = getIntent().getStringExtra("selected_path");

        img_show =(ImageView)findViewById(R.id.img_show);

        Glide.with(ImageShowActivity.this).load(imge_path).into(img_show);
    }
}