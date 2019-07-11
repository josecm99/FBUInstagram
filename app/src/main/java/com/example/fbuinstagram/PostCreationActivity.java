package com.example.fbuinstagram;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class PostCreationActivity extends AppCompatActivity {


    private ImageView ivImage;
    private EditText etDescription;
    private Button btnPost;
    private static final String APP_TAG = "PostCreationActivity";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_creation);


        Log.d(APP_TAG, "Created the activity");


//        ivImage = (ImageView) findViewById(R.id.ivImage);
//        etDescription = (EditText) findViewById(R.id.etDescription);
//        btnPost = (Button) findViewById(R.id.btnPost);
//
//
//        String photoFilePath = getIntent().getExtras().getString("URI");
//
//        // by this point we have the camera photo on disk
//        Bitmap takenImage = BitmapFactory.decodeFile(photoFilePath);
//
//        // See BitmapScaler.java: https://gist.github.com/nesquena/3885707fd3773c09f1bb
//        Bitmap resizedBitmap = BitmapScaler.scaleToFitWidth(takenImage, WIDTH);
//
//
//        ivImage.setImageBitmap(resizedBitmap);
//
//
//        btnPost.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String caption = etDescription.getText().toString();
//
//                if (caption.matches("")){
//                    Toast.makeText(PostCreationActivity.this, "Please add a caption", Toast.LENGTH_LONG).show();
//                }
//                else{
//                    Intent result = new Intent();
//
//                    result.putExtra("caption", caption);
//                    setResult(RESULT_OK, result);
//                    finish();
//                }
//            }
//        });







    }
}
