package com.yuniemao.photouploadapp;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class PhotoFragmentPrivate extends TabsFragment{

    private static final String TAG = "Photo Fragment Private";
    private static final String PHOTO_FRAGMENT_TAB = "Photo Fragment Private";
    private String tab;

    public PhotoFragmentPrivate(){}

        @Override
        public Query getQuery(DatabaseReference databaseReference) {
            Query query = null;
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                query = databaseReference.child("private").child(user.getUid()).limitToFirst(50);
            }
            return query;
        }

    public void setTab(String tab){
        this.tab = PHOTO_FRAGMENT_TAB;
    }

    public String getTab(){
        return tab;
    }
}
