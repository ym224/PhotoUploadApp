package com.yuniemao.photouploadapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class TabsFragment extends Fragment {

    private static final String TAG = "Tabs";
    private DatabaseReference mDatabase;
    FirebaseRecyclerAdapter<Image, ImageViewHolder> mAdapter;
    private View rootView;
    private RecyclerView recyclerView;
    private LinearLayoutManager mManager;
    private boolean isPrivate;
    public String query;
    private ImageView imageView;

    public TabsFragment(){}

    public Query getQuery(DatabaseReference databaseReference) {
        Query query;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            query = databaseReference.child("private").child(user.getUid()).limitToFirst(50);
        }
        else {
            query = databaseReference.child("public").limitToFirst(50);
        }
        return query;
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_photo_gallery, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        recyclerView = rootView.findViewById(R.id.image_gallery);
        //recyclerView.setHasFixedSize(true);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mManager = new LinearLayoutManager(getActivity());
        //mManager.setReverseLayout(true);
        //mManager.setStackFromEnd(true);
        final Query imageQuery = getQuery(mDatabase);
        if (imageQuery == null) {
            Log.d(TAG, "no image results");
            mAdapter = null;
        } else {
            Log.d(TAG, "got image results");
            Log.d(TAG, imageQuery.toString());

            mAdapter = new FirebaseRecyclerAdapter<Image, ImageViewHolder>(Image.class, R.layout.fragment_photo, ImageViewHolder.class, imageQuery) {

                @Override
                protected void populateViewHolder(final ImageViewHolder viewHolder, final Image image, final int position) {
                    Log.d(TAG, "query is " + query);
                    if (query == null || image.getDescription().equals(query)) {
                        viewHolder.updateView(image);
                    } else {
                        viewHolder.itemView.setVisibility(View.GONE);
                    }
                }
            };
            recyclerView.setLayoutManager(mManager);
            recyclerView.setAdapter(mAdapter);
        }
    }

    public void setQuery(String query) {
        this.query = query;
    }

}
