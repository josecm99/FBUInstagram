package com.example.fbuinstagram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.fbuinstagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private static final String IMAGE_PATH = "~/Desktop/ConductorDog.jpeg";
    private static final String APP_TAG = "HomeActivity";
    public static final int WIDTH = 250;
    private Button btnCreate;
    private Button btnRefresh;
    private Button btnLogout;

    //Used for opening up the Image Capture Intent
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public final static int POST_CREATION_REQUEST_CODE = 123;
    public String photoFileName = "photo.jpg";
    File photoFile;

    //Information used for the RecyclerView
    private RecyclerView rvPosts;
    private ArrayList<Post> posts;
    private PostAdapter adapter;
    private SwipeRefreshLayout swipeContainer;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        //Initialize a create button and set an onClickListener
        btnCreate = (Button) findViewById(R.id.btnCreate);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(APP_TAG, "About to launch camera");
                onLaunchCamera();
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

        getSupportActionBar().setTitle("");

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.nav_logo_whiteout);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        //Setup for the RecyclerView
        rvPosts = findViewById(R.id.rvPosts);

        posts = new ArrayList<>();
        adapter = new PostAdapter(posts);

        //Loading all the posts after having created the adapter
            //Making sure to notify adapter of insertions
        loadTopPosts();


        rvPosts.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        rvPosts.setLayoutManager(layoutManager);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchTimelineAsync(0);

            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);



    }// end onCreate


    public void fetchTimelineAsync(int page) {
        // Send the network request to fetch the updated data

        //TODO - Fetch the new information from Parse to populate the screen with updated information :)

        //Start by clearing what is currently in the posts list
        adapter.clear();
        loadTopPosts();
        swipeContainer.setRefreshing(false);

    }



    public void onLaunchCamera(){
        // create Intent to take a picture and return control to the calling application
        Log.d(APP_TAG, "Creating camera intent");

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference to access to future access
        photoFile = getPhotoFile(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(HomeActivity.this, "com.codepath.fileprovider", photoFile);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        Log.d(APP_TAG, "Put stuff in Intent (FileProvider)");

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(cameraIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d(APP_TAG, "Am inside onActivityResult");

        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {


                Log.d(APP_TAG, "Result was OK and we should have gotten an image");

                Log.d(APP_TAG, "Boutta blast an intent");
                Intent postCreationIntent = new Intent(HomeActivity.this, PostCreationActivity.class);

                postCreationIntent.putExtra("URI", photoFile.getAbsolutePath() );

                startActivityForResult(postCreationIntent, POST_CREATION_REQUEST_CODE);


            } else { // Result was a failure
                Toast.makeText(HomeActivity.this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }

        }// end if check for CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE

        else if (requestCode == POST_CREATION_REQUEST_CODE){
            if (resultCode == RESULT_OK){

                Log.d(APP_TAG, "Trying to create a POST here :))");

                //Redoing this in this Activity so that we can save the resized version for later use
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath() );

                // See BitmapScaler.java: https://gist.github.com/nesquena/3885707fd3773c09f1bb
                Bitmap resizedBitmap = BitmapScaler.scaleToFitWidth(takenImage, WIDTH);


                // Configure byte output stream
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                // Compress the image further
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
                // Create a new file for the resized bitmap (`getPhotoFileUri` defined above)
                File resizedFile = getPhotoFile(photoFileName + "_resized");
                try {
                    resizedFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(resizedFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                // Write the bytes of the bitmap to file
                try {
                    fos.write(bytes.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //Once all the above code has run, we can use the new 'resizedFile' to create our post


                //Will now use createPost to actually put the Post on our Parse backend
                String caption = data.getExtras().getString("caption");

                //Also make sure to get the current user
                ParseUser user = ParseUser.getCurrentUser();

                //And turn the File into a ParseFile
                ParseFile parseFile = new ParseFile(resizedFile);
                createPost(caption, parseFile, user);

            }// end if check for RESULT_OK

        }// end if check for POST_CREATION_REQUEST_CODE

    }// end onActivityResult


    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFile(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(APP_TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }


    private void createPost(String description, ParseFile imageFile, ParseUser user){

        final Post newPost = new Post();
        newPost.setDesciption(description);
        newPost.setImage(imageFile);
        newPost.setUser(user);

        //Also make sure to add the post manually to the list so that we can see it right away :)
        posts.add(0, newPost);
        adapter.notifyItemInserted(0);
        rvPosts.scrollToPosition(0);


        newPost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){
                    Log.d(APP_TAG, "Created Post successfully! :)");
                }else{
                    Log.e(APP_TAG,"Post creation had a booboo :(");
                    e.printStackTrace();
                }
            }
        });

    }

    private void loadTopPosts() {

        final Post.Query postsQuery = new Post.Query();
        postsQuery
                .getTopPosts()
                .withUser()
                .orderByDescending("createdAt");

        postsQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null){
                    for(int i =0; i < objects.size(); i++) {

                        //Add to the posts list and make sure to notify the adapter
                        posts.add(objects.get(i) );
                        adapter.notifyItemInserted(posts.size() - 1);


                        Log.d("HomeActivity", "Post ID: "
                                + objects.get(i).getDescription()
                                + "\tUsername: " + objects.get(i).getUser().getUsername()
                                + "\tCreated at: " + objects.get(i).getCreatedAt());
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