package com.example.fbuinstagram.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("Post")
public class Post extends ParseObject {

    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_USER= "user";

    //GETTERS

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    //SETTERS

    public void setDesciption(String desciption){
        put(KEY_DESCRIPTION, desciption);
    }

    public void setImage(ParseFile image){
        put(KEY_IMAGE, image);
    }

    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }

    public static class Query extends ParseQuery<Post> {

        public Query(){
            super(Post.class);
        }


        public Query getTopPosts(){
            setLimit(20);
            return this; //"Builder" pattern. Allows users to chain these Query methods
        }

        public Query withUser(){
            include("user");
            return this;//"Builder" pattern. Allows users to chain these Query methods
        }

    }// end inner class


}
