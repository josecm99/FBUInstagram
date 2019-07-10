package com.example.fbuinstagram;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class PostDetailsActivity extends AppCompatActivity {


    private ImageView ivImage;
    private TextView tvDescription;
    private TextView tvTimestamp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);



        ivImage = (ImageView) findViewById(R.id.ivImage);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvTimestamp = (TextView) findViewById(R.id.tvTimestamp);

        //Unpack the information from the Intent
        String imageURL = getIntent().getExtras().getString("photo");
        String imageDescription = getIntent().getExtras().getString("caption");
        String timestamp = getIntent().getExtras().getString("timestamp");


        Glide.with(this)
                .load(imageURL)
                .into(ivImage);

        tvDescription.setText(imageDescription);

        tvTimestamp.setText(timestamp);

    }
}
