package com.example.footballapp;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.squareup.picasso.Picasso;

public class InfoUserActivity extends AppCompatActivity {

    ImageView imvPhotoUser;
    EditText edtUserName,edtUserNumber,edtUserPassword,edtUserAddress;
    TextView txtNameChange,txtNumberChange,txtPassChange,txtAddressChange,txtUserName,txtUserEmail;
    Button btnLogout,btnBack,btnSaveChange;
    ActivityResultLauncher<Intent> launcher;



    boolean captureState = true;
    boolean choseImv = false;
    Uri PhotoUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_user);
        Anhxa();
        getCurrentUserInfo();
        checkClickChange();
        photoUserChange();
//        changePass();
    }

//    private void changePass() {
//        btnChangePassword.setOnClickListener(v ->{
//            Intent intent = new Intent(this, ResetPasswordActivity.class);
//            startActivity(intent);
//        });
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(choseImv){
            Toast.makeText(this, "Bạn chưa lưu giữ liệu!!!", Toast.LENGTH_SHORT).show();
        }
        
    }
    

    private void photoUserChange() {

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result ->{
            if(result.getResultCode() == RESULT_OK && result.getData() != null){
                if(captureState){//camera
                    Bitmap bitmap = (Bitmap) result.getData().getExtras().get("data");
                    assert bitmap != null;
                    PhotoUri = Uri.parse(bitmap.toString());
                    imvPhotoUser.setImageBitmap(bitmap);
                }else{
                    Uri selectedPhotoUri = result.getData().getData();
                    PhotoUri = selectedPhotoUri;
                    imvPhotoUser.setImageURI(selectedPhotoUri);
                }

            }
        });
        btnSaveChange.setOnClickListener(v -> changeUserInfo());
    }


    private void checkClickChange() {


        txtNameChange.setOnClickListener(v -> {
            edtUserName.setEnabled(true);
        });
        txtNumberChange.setOnClickListener(v -> {
            edtUserNumber.setEnabled(true);
        });
        txtPassChange.setOnClickListener(v -> {
            edtUserPassword.setEnabled(true);
        });
        txtAddressChange.setOnClickListener(v -> {
            edtUserAddress.setEnabled(true);
        });
        imvPhotoUser.setOnClickListener(v -> {
            choseImv = true;

            Dialog dialog = new Dialog(InfoUserActivity.this);
            dialog.setContentView(R.layout.bottom_image_user);

            LinearLayout loCam = dialog.findViewById(R.id.layoutCamera);
            loCam.setOnClickListener(v12 -> {
                captureState = true;
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                launcher.launch(intent);
                dialog.dismiss();

            });
            LinearLayout loGallary = dialog.findViewById(R.id.layoutGallary);
            loGallary.setOnClickListener(v1 -> {
                captureState= false;
                Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                launcher.launch(intent);
                dialog.dismiss();
            });
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.show();
        });

    }

    private void changeUserInfo() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(!choseImv){
            PhotoUri = user.getPhotoUrl();
        }else{
        }
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(edtUserName.getText().toString())
                .setPhotoUri(PhotoUri)
                .build();

//        StorageReference storageRef = rootRef.getReferenceFromUrl("gs://footballappbase.appspot.com");
//
//
//        StorageReference riversRef = storageRef.child("users/"+PhotoUri.getLastPathSegment());
//        UploadTask uploadTask = riversRef.putFile(PhotoUri);
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(InfoUserActivity.this, "Thất bại~", Toast.LENGTH_SHORT).show();
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Toast.makeText(InfoUserActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
//
//            }
//        });
        edtUserName.setEnabled(Boolean.parseBoolean("false"));
        edtUserNumber.setEnabled(Boolean.parseBoolean("false"));
        edtUserPassword.setEnabled(Boolean.parseBoolean("false"));
        edtUserAddress.setEnabled(Boolean.parseBoolean("false"));


        user.updateProfile(profileUpdates).addOnSuccessListener(unused -> {
            Toast.makeText(InfoUserActivity.this, "Đã cập nhật.", Toast.LENGTH_SHORT).show();
            getCurrentUserInfo();
        });

    }



    private void getCurrentUserInfo() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
            assert photoUrl != null;
            Log.d("ANh", photoUrl.toString());
            Picasso.get().load(photoUrl.toString()).into(imvPhotoUser);

                if (name == null){
                    edtUserName.setText("User");
                    txtUserName.setText("User");
                }else{
                    edtUserName.setText(name);
                    txtUserName.setText(name);
                }
                if(email == null){
                    txtUserEmail.setText("abc@gmail.com");
                }else{
                    txtUserEmail.setText(email);
                }

        }
        else{
            Toast.makeText(this, "Chưa đăng nhập", Toast.LENGTH_SHORT).show();
        }
    }

    private void Anhxa() {
        imvPhotoUser = findViewById(R.id.imvPhotoUser);
        edtUserName = findViewById(R.id.edtUserName);
        txtUserName = findViewById(R.id.txtUserName);
        txtUserEmail= findViewById(R.id.txtUserEmail);
        edtUserName = findViewById(R.id.edtUserName);
        edtUserNumber = findViewById(R.id.edtUserNumber);
        edtUserPassword = findViewById(R.id.edtUserPassword);
        edtUserAddress = findViewById(R.id.edtUserAddress);

        txtNameChange = findViewById(R.id.txtNameChange);
        txtNumberChange = findViewById(R.id.txtNumberChange);
        txtPassChange = findViewById(R.id.txtPassChange);
        txtAddressChange = findViewById(R.id.txtAddressChange);

        btnSaveChange = findViewById(R.id.btnSaveChange);
        btnLogout = findViewById(R.id.btnLogout);
        btnBack = findViewById(R.id.btnBack);

    }
}