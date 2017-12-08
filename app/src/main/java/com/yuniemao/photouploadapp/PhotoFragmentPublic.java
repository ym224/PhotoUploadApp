package com.yuniemao.photouploadapp;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 * Created by yuniemao on 12/7/17.
 */

public class PhotoFragmentPublic extends TabsFragment{

    private static final String TAG = "Photo Fragment Public";
    private static final String PHOTO_FRAGMENT_TAB = "Photo Fragment Public";
    private String tab;

    public PhotoFragmentPublic(){}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        Query query;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        query = databaseReference.child("public").limitToFirst(50);
        return query;
    }

    public void setTab(String tab){
        this.tab = PHOTO_FRAGMENT_TAB;
    }

    public String getTab(){
        return tab;
    }
}
