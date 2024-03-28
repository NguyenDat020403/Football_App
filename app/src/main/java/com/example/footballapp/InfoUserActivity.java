package com.example.footballapp;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InfoUserActivity extends AppCompatActivity {

    ImageView imvPhotoUser;
    EditText edtUserName,edtUserNumber,edtUserPassword,edtUserAddress;
    TextView txtNameChange,txtNumberChange,txtPassChange,txtAddressChange,txtUserName,txtUserEmail;
    Button btnLogout,btnBack,btnSaveChange;
    LinearLayout layoutSettingInfo,layoutNotification,layoutWallet;
    LinearLayout layoutNotificationS, layoutWalletS,layoutSettingS;
    ActivityResultLauncher<Intent> launcher;
    Uri PhotoUri;


    boolean captureState = true;
    boolean choseImv = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_user);
        Anhxa();


        addEvents();
        getCurrentUserInfo();
        checkClickChange();
        photoUserChange();
    }
    // Lưu Uri vào SharedPreferences
    private void savePhotoUri(Uri uri) {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("photoUri", uri.toString());
        editor.apply();
    }

    // Khôi phục Uri từ SharedPreferences
    private Uri getPhotoUri() {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String uriString = preferences.getString("photoUri", null);
        if (uriString != null) {
            return Uri.parse(uriString);
        } else {
            return null;
        }
    }


    private void addEvents() {
        layoutSettingS.setOnClickListener(v->{
            layoutNotification.setVisibility(View.GONE);
            layoutWallet.setVisibility(View.GONE);
            layoutSettingInfo.setVisibility(View.VISIBLE);
        });

        layoutWalletS.setOnClickListener(v->{
            layoutNotification.setVisibility(View.GONE);
            layoutWallet.setVisibility(View.VISIBLE);
            layoutSettingInfo.setVisibility(View.GONE);
        });
        layoutNotificationS.setOnClickListener(v->{
            layoutNotification.setVisibility(View.VISIBLE);
            layoutWallet.setVisibility(View.GONE);
            layoutSettingInfo.setVisibility(View.GONE);
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(choseImv){
            Toast.makeText(this, "Bạn chưa lưu giữ liệu!!!", Toast.LENGTH_SHORT).show();
        }
        
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1001) {
                if (captureState) { // Camera
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    imvPhotoUser.setImageBitmap(bitmap);
                    savePhotoUri(Uri.parse(String.valueOf(bitmap)));
                } else { // Bộ sưu tập
                    PhotoUri = data.getData();
                    imvPhotoUser.setImageURI(PhotoUri);
                    savePhotoUri(PhotoUri);
                }
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Khôi phục Uri của ảnh khi ứng dụng được mở lại
        PhotoUri = getPhotoUri();
        if (PhotoUri != null) {
            Picasso.get().load(PhotoUri).into(imvPhotoUser);
        }
    }


    private void photoUserChange() {

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result ->{
            if(result.getResultCode() == RESULT_OK && result.getData() != null){
                if(captureState){//camera
                    Bitmap bitmap = (Bitmap) result.getData().getExtras().get("data");
                    assert bitmap != null;
                    PhotoUri = Uri.parse(String.valueOf(bitmap));
                    imvPhotoUser.setImageBitmap(bitmap);
                    savePhotoUri(Uri.parse(bitmap.toString()));
                }else{
                    PhotoUri = result.getData().getData();
                    imvPhotoUser.setImageURI(PhotoUri);
                    savePhotoUri(PhotoUri);

                }

            }
        });
        Log.d("ANh photoUserChange", " "+PhotoUri);
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
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
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
            assert user != null;
            PhotoUri = Uri.parse(Objects.requireNonNull(user.getPhotoUrl()).toString());
        }
        Log.d("ANh changeUserInfo", " "+PhotoUri);

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(edtUserName.getText().toString())
                .setPhotoUri(PhotoUri)
                .build();

        edtUserName.setEnabled(Boolean.parseBoolean("false"));
        edtUserNumber.setEnabled(Boolean.parseBoolean("false"));
        edtUserPassword.setEnabled(Boolean.parseBoolean("false"));
        edtUserAddress.setEnabled(Boolean.parseBoolean("false"));

        assert user != null;
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

            Log.d("ANh", Objects.requireNonNull(user.getPhotoUrl()).toString());

            Picasso.get().load(getPhotoUri()).into(imvPhotoUser);

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
//        btnLogout = findViewById(R.id.btnLogout);
//        btnBack = findViewById(R.id.btnBack);
        layoutSettingInfo = findViewById(R.id.layoutSettingInfo);
        layoutNotification = findViewById(R.id.layoutNotification );
        layoutWallet = findViewById(R.id.layoutWallet);

        layoutNotificationS = findViewById(R.id.layoutNotificationS);
        layoutWalletS = findViewById(R.id.layoutWalletS);
        layoutSettingS = findViewById(R.id.layoutSettingS);
    }
}