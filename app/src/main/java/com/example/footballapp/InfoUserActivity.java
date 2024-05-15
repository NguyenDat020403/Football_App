package com.example.footballapp;

import static com.example.footballapp.LoginActivity.googleSignInClient;

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
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
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

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
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

    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    String myUri ="";
    StorageTask updateTask;
    StorageReference storageProfilePicsRef;

    boolean captureState = true;
    boolean choseImv = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_user);
        Anhxa();
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        storageProfilePicsRef = FirebaseStorage.getInstance().getReference().child("Profile Pic");

        
        addEvents();
        getCurrentUserInfo();
        checkClickChange();
        photoUserChange();
    }
    // Lưu Uri vào SharedPreferences
    // Upload ảnh lên Firebase Storage
//    private void uploadImageToFirebaseStorage(Uri imageUri) {
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//        assert user != null;
//        StorageReference storageRef = FirebaseStorage.getInstance().getReference()
//                .child("user_profile_images").child(user.getUid()); // userId là ID của người dùng
//        Log.i("up1",user.getUid());
//        Log.i("up2",storageRef.toString());
//        storageRef.putFile(imageUri)
//                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        // Lấy URL của ảnh đã upload
//                        Task<Uri> downloadUrl = storageRef.getDownloadUrl();
//                        downloadUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri uri) {
//                                String imageUrl = uri.toString();
//                                Toast.makeText(InfoUserActivity.this, "Upload ảnh thành công!", Toast.LENGTH_SHORT).show();
//                                saveImageUrlToUserProfile(imageUrl);
//                            }
//                        });
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(InfoUserActivity.this, "Upload ảnh thất bại!", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//
//    // Lưu URL ảnh vào hồ sơ người dùng trong Firestore hoặc Realtime Database
//    private void saveImageUrlToUserProfile(String imageUrl) {
//        // Cập nhật hồ sơ người dùng với URL ảnh mới trong Firestore hoặc Realtime Database
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        assert user != null;
//        DocumentReference userRef = db.collection("users").document(user.getUid()); // userId là ID của người dùng
//        Log.i("save", userRef.toString());
//
//        userRef.update("profileImageUrl", imageUrl)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(InfoUserActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(InfoUserActivity.this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//
//    // Hiển thị ảnh của người dùng khi họ đăng nhập lại
//    private void displayUserProfileImage() {
//        // Đọc URL ảnh từ hồ sơ người dùng trong Firestore hoặc Realtime Database
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        assert user != null;
//        DocumentReference userRef = db.collection("users").document(user.getUid()); // userId là ID của người dùng
//        Log.i("Hình ảnh", userRef.toString());
//        userRef.get()
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        if (documentSnapshot.exists()) {
//                            String imageUrl = documentSnapshot.getString("profileImageUrl");
//                            Glide.with(InfoUserActivity.this).load(imageUrl).into(imvPhotoUser);
//                            // Glide, Picasso, hoặc Universal Image Loader là những thư viện phổ biến
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(InfoUserActivity.this, "Lỗi khi tải đường dẫn ảnh", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }



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
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        googleSignInClient.signOut();
        LoginManager.getInstance().logOut();
        CookieManager.getInstance().removeAllCookies(new ValueCallback<Boolean>() {
            @Override
            public void onReceiveValue(Boolean value) {
                // Thông báo cho người dùng
                Toast.makeText(InfoUserActivity.this, "Đã đăng xuất!", Toast.LENGTH_SHORT).show();
                // Chuyển về trang đăng nhập
                Intent intent = new Intent(InfoUserActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        CookieManager.getInstance().flush();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1001) {
                if (captureState) { // Camera
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    imvPhotoUser.setImageBitmap(bitmap);
//                    uploadImageToFirebaseStorage(Uri.parse(String.valueOf(bitmap)));
                } else { // Bộ sưu tập
                    PhotoUri = data.getData();
                    imvPhotoUser.setImageURI(PhotoUri);
//                    uploadImageToFirebaseStorage(PhotoUri);
                }
            }
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
//                    uploadImageToFirebaseStorage(Uri.parse(bitmap.toString()));
                }else{
                    PhotoUri = result.getData().getData();
                    imvPhotoUser.setImageURI(PhotoUri);
//                    uploadImageToFirebaseStorage(PhotoUri);
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
            Glide.with(InfoUserActivity.this).load(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl())).into(imvPhotoUser);
//            Picasso.get().load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).into(imvPhotoUser);
//            displayUserProfileImage();
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
//        btnBack = findViewById(R.id.btnBack);
        layoutSettingInfo = findViewById(R.id.layoutSettingInfo);
        layoutNotification = findViewById(R.id.layoutNotification );
        layoutWallet = findViewById(R.id.layoutWallet);

        layoutNotificationS = findViewById(R.id.layoutNotificationS);
        layoutWalletS = findViewById(R.id.layoutWalletS);
        layoutSettingS = findViewById(R.id.layoutSettingS);
    }
}