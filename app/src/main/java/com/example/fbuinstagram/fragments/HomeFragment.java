package com.example.fbuinstagram.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fbuinstagram.PostAdapter;
import com.example.fbuinstagram.R;
import com.example.fbuinstagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {


    //Information used for the RecyclerView
    private RecyclerView rvPosts;
    private ArrayList<Post> posts;
    private PostAdapter adapter;
    private SwipeRefreshLayout swipeContainer;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        rvPosts = view.findViewById(R.id.rvPosts);

        posts = new ArrayList<>();
        adapter = new PostAdapter(posts);

        loadTopPosts();


        rvPosts.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity() );

        rvPosts.setLayoutManager(layoutManager);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchTimelineAsync();

            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    public void fetchTimelineAsync() {
        // Send the network request to fetch the updated data

        //Start by clearing what is currently in the posts list
        adapter.clear();
        loadTopPosts();
        swipeContainer.setRefreshing(false);

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


//                        Log.d("HomeActivity", "Post ID: "
//                                + objects.get(i).getDescription()
//                                + "\tUsername: " + objects.get(i).getUser().getUsername()
//                                + "\tCreated at: " + objects.get(i).getCreatedAt());
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