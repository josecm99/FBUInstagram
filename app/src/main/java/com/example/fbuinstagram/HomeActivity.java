package com.example.fbuinstagram;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.fbuinstagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private static final String IMAGE_PATH = "~/Desktop/ConductorDog.jpeg";
    private Button btnCreate;
    private Button btnRefresh;
    private Button btnLogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        //Initialize a create button and set an onClickListener
        btnCreate = (Button) findViewById(R.id.btnCreate);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO MAKE SURE TO CHANGE THIS ALL LATER
//                final String description = etDescription.getText().toString();
//                final ParseUser currUser = ParseUser.getCurrentUser();
//
//                final File file = new File(IMAGE_PATH);
//                final ParseFile parseFile = new ParseFile(file);
//
//                createPost(description, parseFile, currUser);
            }
        });

        //Initialize refresh button and set an onClickListener
        btnRefresh = (Button) findViewById(R.id.btnRefresh);

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTopPosts();
            }
        });


        //Initialize logout button and set onClickListener
        btnLogout = (Button) findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                finish();
            }
        });

        //Modify the ActionBar
        ActionBar actionBar = getSupportActionBar();

        //Hide title
        getSupportActionBar().setTitle("");
        //Allows icon to be added
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.drawable.icon);
        actionBar.setDisplayUseLogoEnabled(true);



        loadTopPosts();
    }// end onCreate


    private void createPost(String description, ParseFile imageFile, ParseUser user){

        final Post newPost = new Post();
        newPost.setDesciption(description);
        newPost.setImage(imageFile);
        newPost.setUser(user);

        newPost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){
                    Log.d("HomeActivity", "Created Post successfully! :)");
                }else{
                    Log.e("HomeActivity","Post creation had a booboo :(");
                    e.printStackTrace();
                }
            }
        });

    }

    private void loadTopPosts() {

        final Post.Query postsQuery = new Post.Query();
        postsQuery
                .getTopPosts()
                .withUser();

        postsQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null){
                    for(int i =0; i < objects.size(); i++) {
                        Log.d("HomeActivity", "Post ID: "
                                + objects.get(i).getDescription()
                                + "\nUsername: " + objects.get(i).getUser().getUsername() );
                    }// end for

                }
                else{
                    Log.e("HomeActivity","Query getting unsuccessful :(");

                    e.printStackTrace();
                }
            }
        });

    }
}
