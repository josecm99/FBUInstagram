package com.example.fbuinstagram;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fbuinstagram.models.Post;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {


    private List<Post> mPosts;

    //Context that we get from onCreateBindHolder
    Context mContext;


    public PostAdapter(List<Post> posts){
        this.mPosts = posts;
    }// end constructor


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        //Get the context from the ViewGroup
        mContext = viewGroup.getContext();

        //Create a LayoutInflater using the context
        LayoutInflater inflater = LayoutInflater.from(mContext);

        //Inflate what will be the Post row in the RecyclerView
            //Create a View for it
        View postView = inflater.inflate(R.layout.item_post, viewGroup, false);

        //Finally, create a ViewHolder object
        ViewHolder viewHolder = new ViewHolder(postView);

        return viewHolder;
    }// end onCreateViewHolder

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        //Get the current post and populate the information on the current view
        Post currPost = mPosts.get(i);

        viewHolder.tvDescription.setText(currPost.getDescription() );

        Glide.with(mContext)
                .load(currPost.getImage().getUrl() )
                .into(viewHolder.ivImage);

    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView ivImage;
        public TextView tvDescription;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
        }


    }

}
