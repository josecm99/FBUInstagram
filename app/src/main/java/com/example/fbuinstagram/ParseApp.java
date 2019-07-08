package com.example.fbuinstagram;

import android.app.Application;

import com.example.fbuinstagram.models.Post;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        //Register so that Parse knows that we have a model that encapsulates Parse data
        ParseObject.registerSubclass(Post.class);

        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("josecm99")
                .clientKey("jose-cruz-mendoza")
                .server("http://josecm99-fbu-instagram.herokuapp.com/parse")
                .build();

        Parse.initialize(configuration);
    }
}
