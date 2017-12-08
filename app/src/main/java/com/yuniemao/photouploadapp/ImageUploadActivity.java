package com.yuniemao.photouploadapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuniemao on 12/2/17.
 */

public class ImageUploadActivity extends BaseActivity implements View.OnClickListener{

    private StorageReference storageRef;
    private DatabaseReference databaseRef;

    private EditText descrip;
    private Switch permission;

    private Uri filePath;
    private String downloadUrl;
    private String authorId;
    private String description;
    private boolean isPrivate;

    private static final String TAG = "Image Upload";
    private static final int SELECT_IMAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_photo);

        storageRef = FirebaseStorage.getInstance().getReference();
        databaseRef = FirebaseDatabase.getInstance().getReference();

        descrip = findViewById(R.id.description);
        permission = findViewById(R.id.permission_toggle);

        findViewById(R.id.select_image).setOnClickListener(this);
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_IMAGE && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            authorId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            description = descrip.getText().toString();
            isPrivate = permission.isChecked();

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            storageRef = storageRef.child(filePath.getLastPathSegment());

            storageRef.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        downloadUrl = taskSnapshot.getDownloadUrl().toString();

                        Log.d(TAG, "download url is " + downloadUrl);
                        Image image = new Image(authorId, description, filePath.getLastPathSegment(), downloadUrl);
                        Map<String, Object> map = new HashMap<>();
                        map.put("authorId", image.getAuthorId());
                        map.put("description", image.getDescription());
                        map.put("filePath", image.getFilePath());
                        map.put("fileName", image.getFileName());

                        if (isPrivate) {
                            databaseRef.child("private").child(authorId).push().setValue(map);
                        }
                        else {
                            databaseRef.child("public").push().setValue(map);
                        }
                        Toast.makeText(ImageUploadActivity.this, "Photo Uploaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(ImageUploadActivity.this, "Upload Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            progressDialog.dismiss();

            startActivity(new Intent(ImageUploadActivity.this, MainActivity.class));
            finish();
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.select_image) {
            selectImage();
        }
    }
}
