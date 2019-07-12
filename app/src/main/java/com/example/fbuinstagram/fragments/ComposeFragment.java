package com.example.fbuinstagram.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fbuinstagram.BitmapScaler;
import com.example.fbuinstagram.R;
import com.example.fbuinstagram.interfaces.NavigationHomeCallback;
import com.example.fbuinstagram.models.Post;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class ComposeFragment extends Fragment {


    private ImageView ivImage;
    private EditText etDescription;
    private Button btnPost;
    private static final String APP_TAG = "ComposeFragment";
    public static final int WIDTH = 200;

    //Used for opening up the Image Capture Intent
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public final static int POST_CREATION_REQUEST_CODE = 123;
    public String photoFileName = "photo.jpg";
    File photoFile;
    File resizedFile;

    NavigationHomeCallback navigateHome;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compose, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivImage = (ImageView) view.findViewById(R.id.ivImage);
        etDescription = (EditText) view.findViewById(R.id.etDescription);
        btnPost = (Button) view.findViewById(R.id.btnPost);


        Log.d(APP_TAG, "Inside onViewCreated");


        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String caption = etDescription.getText().toString();

                if (caption.matches("")){
                    Toast.makeText(getActivity(), "Please add a caption", Toast.LENGTH_LONG).show();
                }
                else{

                    if (resizedFile == null){
                        Toast.makeText(getActivity(), "Please add a photo", Toast.LENGTH_LONG).show();
                    }
                    else {

                        Log.d(APP_TAG, "About to try to create a new post");

                        ParseUser user = ParseUser.getCurrentUser();
                        ParseFile newFile = new ParseFile(resizedFile);

                        createPost(caption, newFile, user);

                        etDescription.setText("");
                        ivImage.setImageResource(0);


                        navigateHome.returnHome();

//                    HomeFragment homeFragment = new HomeFragment();
//                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    fragmentTransaction.replace(R.id.flContainer, homeFragment);
//                    fragmentTransaction.addToBackStack(null);
//                    fragmentTransaction.commit();
                    }
                }

            }
        });

        onLaunchCamera();

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
        Uri fileProvider = FileProvider.getUriForFile(getActivity(), "com.codepath.fileprovider", photoFile);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        Log.d(APP_TAG, "Put stuff in Intent (FileProvider)");

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
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


                //Redoing this in this Activity so that we can save the resized version for later use
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath() );

                // See BitmapScaler.java: https://gist.github.com/nesquena/3885707fd3773c09f1bb
                Bitmap resizedBitmap = BitmapScaler.scaleToFitWidth(takenImage, WIDTH);


                // Configure byte output stream
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                // Compress the image further
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
                // Create a new file for the resized bitmap (`getPhotoFileUri` defined above)
                resizedFile = getPhotoFile(photoFileName + "_resized");
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

                ivImage.setImageBitmap(resizedBitmap);

                Log.d(APP_TAG, "Set up the image bitmap");




//                //Will now use createPost to actually put the Post on our Parse backend
//                String caption = data.getExtras().getString("caption");
//
//                //Also make sure to get the current user
//                ParseUser user = ParseUser.getCurrentUser();
//
//                //And turn the File into a ParseFile
//                ParseFile parseFile = new ParseFile(resizedFile);
//                createPost(caption, parseFile, user);



            } else { // Result was a failure
                Toast.makeText(getActivity(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();

                //TODO - Make sure to send the user back to the home screen in this case ALSO

            }

        }// end if check for CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE

    }// end onActivityResult


    public void setInterface(NavigationHomeCallback navigateHome){
        this.navigateHome = navigateHome;
    }


    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFile(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

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






}
