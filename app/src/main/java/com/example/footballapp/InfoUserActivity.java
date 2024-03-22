package com.example.footballapp;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.squareup.picasso.Picasso;

public class InfoUserActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    private ImageView imvPhotoUser;
    private EditText edtNameUser;
    private EditText edtEmailUser;
    TextView txtChangePhoto,txtChangeName,txtChangeEmail;
    Button btnChangePassword,btnSave;
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
    }

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
        btnSave.setOnClickListener(v -> changeUserInfo());
    }


    private void checkClickChange() {

        txtChangeEmail.setOnClickListener(v -> {
            edtEmailUser.setEnabled(true);
        });
        txtChangeName.setOnClickListener(v -> {
            edtNameUser.setEnabled(true);
        });
        txtChangePhoto.setOnClickListener(v -> {
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
            PhotoUri = Uri.parse(user.getPhotoUrl().toString());
        }else{
        }
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(edtNameUser.getText().toString())
                .setPhotoUri(PhotoUri)
                .build();
        user.updateProfile(profileUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(InfoUserActivity.this, "Đã cập nhật.", Toast.LENGTH_SHORT).show();
                getCurrentUserInfo();
            }
        });

    }



    private void getCurrentUserInfo() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();

            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
                if(photoUrl == null){

                }else{
//                    imvPhotoUser.setImageURI(photoUrl);
                    Picasso.get().load(photoUrl.toString()).into(imvPhotoUser);
                }
                if (name == null){
                    edtNameUser.setText("User");
                }else{
                    edtNameUser.setText(name);
                }
                if(email == null){
                    edtEmailUser.setText("abc@gmail.com");
                }else{
                    edtEmailUser.setText(email);
                }

        }
        else{
            Toast.makeText(this, "Chưa đăng nhập", Toast.LENGTH_SHORT).show();
        }
    }

    private void Anhxa() {
        imvPhotoUser = findViewById(R.id.imvPhotoUser);
        edtNameUser = findViewById(R.id.edtNameUser);
        edtEmailUser = findViewById(R.id.edtEmailUser);
        txtChangePhoto= findViewById(R.id.txtChangePhoto);
        txtChangeName = findViewById(R.id.txtChangeName);
        txtChangeEmail = findViewById(R.id.txtChangeEmail);
        btnSave = findViewById(R.id.btnSaveChange);
        btnChangePassword = findViewById(R.id.btnChangePassword);
    }
}