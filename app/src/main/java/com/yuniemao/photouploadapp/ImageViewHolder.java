package com.yuniemao.photouploadapp;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

/**
 * Created by yuniemao on 12/4/17.
 */

public class ImageViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "ImageView Holder";

    private StorageReference ref;
    private TextView desc;
    private ImageView imageView;

    public ImageViewHolder(View itemView) {
        super(itemView);

        ref = FirebaseStorage.getInstance().getReference();
        desc = itemView.findViewById(R.id.image_description);
        imageView = itemView.findViewById(R.id.image_view);
    }

    public void updateView(Image image) {
        desc.setText(image.getDescription());

        Picasso.with(itemView.getContext())
                .load(image.getFilePath())
                .into(imageView);

    }
}
