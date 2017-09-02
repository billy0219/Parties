package com.now.parties;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class ProfileSetup extends AppCompatActivity {

    private ImageButton mProfileImageButton;
    private Button mProfileSetupButton;
    private EditText mProfileNameEditText;

    private Uri mImageUri = null;
    public static final int GALLERY_REQUEST = 1;

    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private StorageReference mStorageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mStorageReference = FirebaseStorage.getInstance().getReference().child("profile_Image");

        mProfileImageButton = (ImageButton) findViewById(R.id.profileImageButton);
        mProfileNameEditText = (EditText) findViewById(R.id.profileNameEditText);
        mProfileSetupButton = (Button) findViewById(R.id.profileSetupButton);

        mProfileImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });

        mProfileSetupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String user_id = mFirebaseAuth.getCurrentUser().getUid();
                final String user_name = mProfileNameEditText.getText().toString().trim();
                if(!TextUtils.isEmpty(user_name) && mImageUri != null) {
                    StorageReference filepath = mStorageReference.child(mImageUri.getLastPathSegment());
                    filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String downloadUri = taskSnapshot.getDownloadUrl().toString();
                            mDatabaseReference.child("userInformation").child(user_id).child("userName").setValue(user_name);
                            mDatabaseReference.child("userInformation").child(user_id).child("userPhoto").setValue(downloadUri);

                            Intent backToMain = new Intent(ProfileSetup.this, MainActivity.class);
                            startActivity(backToMain);
                        }
                    });

                }

            }
        });
    }
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mImageUri = result.getUri();
                mProfileImageButton.setImageURI(mImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                }
            }
    }
}
