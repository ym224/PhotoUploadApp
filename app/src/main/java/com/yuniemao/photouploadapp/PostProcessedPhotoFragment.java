package com.yuniemao.photouploadapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by yuniemao on 12/7/17.
 */

public class PostProcessedPhotoFragment extends TabsFragment {

    private static final String TAG = "ProcessedPhoto Fragment";
    private static final String PHOTO_FRAGMENT_TAB = "Photo Fragment";
    private String tab;
    private View rootView;
    private DatabaseReference mDatabase;
    private RecyclerView recyclerView;
    private StorageReference mStorage;

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        Query query;
        query = databaseReference.child("public").limitToFirst(50);

        return query;
    }

    public void setTab(String tab){
        this.tab = PHOTO_FRAGMENT_TAB;
    }

    public String getTab(){
        return tab;
    }


    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_photo_gallery, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();

        recyclerView = rootView.findViewById(R.id.image_gallery);
        //recyclerView.setHasFixedSize(true);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LinearLayoutManager mManager = new LinearLayoutManager(getActivity());

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
                    Log.d(TAG, "in populate view");
                    if (searchString == null || image.getDescription().equals(searchString)) {
                        if (image.getFileRef() != null) {

                            mStorage = getStorageRefFromString(image.getFileRef());
                            Log.d(TAG, "final storage ref: " + mStorage.toString());

                            mStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri downloadUrl) {
                                    Log.d(TAG, "storage download url: " + downloadUrl.toString());

                                    viewHolder.updateView(downloadUrl.toString());
                                }
                            });
                        }

                    } else {
                        viewHolder.itemView.setVisibility(View.GONE);
                    }
                }
            };
            recyclerView.setLayoutManager(mManager);
            recyclerView.setAdapter(mAdapter);
        }
    }

    public StorageReference getStorageRefFromString(String fileRef) {
        String[] filePaths = fileRef.split("/");

        String asciiFileName = "";
        boolean isPrivate = false;
        if (filePaths.length == 6) {
            asciiFileName = "ascii-" + filePaths[5];
            isPrivate = true;
        }
        else if (filePaths.length == 5) {
            asciiFileName = "ascii-" + filePaths[4];
        }
        else {
            Log.e(TAG, "error in file storage ref path");
        }

        Log.d(TAG, "ascii file name: " + asciiFileName);

        if (isPrivate) {
            mStorage = mStorage.child("private").child(filePaths[4]).child(asciiFileName);
        }
        else {
            mStorage = mStorage.child("public").child(asciiFileName);
        }
        return mStorage;
    }


}
