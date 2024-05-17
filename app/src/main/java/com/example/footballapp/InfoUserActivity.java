package com.example.footballapp;

import static com.example.footballapp.LoginActivity.googleSignInClient;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;

import com.bumptech.glide.Glide;
import com.example.footballapp.databinding.ActivityInfoUserBinding;
import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.Objects;

public class InfoUserActivity extends AppCompatActivity {

    ActivityInfoUserBinding binding;

    ImageView imvPhotoUser;
    Button btnLogout,btnSaveChange;

    ActivityResultLauncher<Intent> launcher;
    Uri PhotoUri;

    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    StorageReference storageProfilePicsRef;

    boolean captureState = true;
    boolean choseImv = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInfoUserBinding.inflate(getLayoutInflater());
        binding.bottomNavigation.setSelectedItemId(R.id.nav_profile);








        setContentView(binding.getRoot());
        Anhxa();
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        storageProfilePicsRef = FirebaseStorage.getInstance().getReference().child("Profile Pic");

        
        addEvents();
        getCurrentUserInfo();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    private void addEvents() {
        //Nav
        //def - profile
        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if(itemId == R.id.nav_tables){
                    startActivity(new Intent(InfoUserActivity.this, MainActivity.class));
                    return true;
                } else if (itemId == R.id.nav_stats) {
                    startActivity(new Intent(InfoUserActivity.this, PlayerTopActivity.class));
                    return true;
                } else if (itemId == R.id.nav_fixtures) {
                    startActivity(new Intent(InfoUserActivity.this, MatchEvents.class));
                    return true;
                } else if (itemId == R.id.nav_news) {
                    startActivity(new Intent(InfoUserActivity.this, NewsActivity.class));
                    return true;
                } else if (itemId == R.id.nav_profile) {

                }
                return  false;
            }
        });



        //option - others


        binding.imvEditName.setOnClickListener(v->{
            binding.edtUserName.setEnabled(true);
            binding.btnSaveChange.setVisibility(View.VISIBLE);
        });
        binding.imvEditEmail.setOnClickListener(v->{
            binding.edtEmailChange.setEnabled(true);
            binding.btnSaveChange.setVisibility(View.VISIBLE);
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
            Objects.requireNonNull(dialog.getWindow()).setGravity(Gravity.BOTTOM);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.show();
        });

         binding.btnLogout.setOnClickListener(v -> logout());

    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        googleSignInClient.signOut();
        LoginManager.getInstance().logOut();
        CookieManager.getInstance().removeAllCookies(value -> {
            // Thông báo cho người dùng
            Toast.makeText(InfoUserActivity.this, "Đã đăng xuất!", Toast.LENGTH_SHORT).show();
            // Chuyển về trang đăng nhập
            Intent intent = new Intent(InfoUserActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
        CookieManager.getInstance().flush();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1001) {
                if (captureState) { // Camera
                    Bitmap bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
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
                    Bitmap bitmap = (Bitmap) Objects.requireNonNull(result.getData().getExtras()).get("data");
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
        binding.btnSaveChange.setOnClickListener(v -> changeUserInfo());
    }



    private void changeUserInfo() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(!choseImv){
            assert user != null;
            PhotoUri = Uri.parse(Objects.requireNonNull(user.getPhotoUrl()).toString());
        }
        Log.d("ANh changeUserInfo", " "+PhotoUri);

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(binding.edtUserName.getText().toString())
                .setPhotoUri(PhotoUri)
                .build();
        assert user != null;
        user.updateProfile(profileUpdates).addOnSuccessListener(unused -> {
            Toast.makeText(InfoUserActivity.this, "Đã cập nhật.", Toast.LENGTH_SHORT).show();
            getCurrentUserInfo();
        });
            binding.btnSaveChange.setVisibility(View.GONE);
            binding.edtUserName.setEnabled(false);
            binding.edtEmailChange.setEnabled(false);
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
                binding.txtUserName.setText("User");
                binding.edtUserName.setText("User");
            }else{
                binding.txtUserName.setText(name);
                binding.edtUserName.setText(name);
            }
            if(email == null){
                binding.txtUserEmail.setText("abc@gmail.com");
                binding.edtEmailChange.setText("abc@gmail.com");
            }else{
                binding.txtUserEmail.setText(email);
                binding.edtEmailChange.setText(email);
            }

        }
        else{
            Toast.makeText(this, "Chưa đăng nhập", Toast.LENGTH_SHORT).show();
        }
    }

    private void Anhxa() {
        imvPhotoUser = findViewById(R.id.imvPhotoUser);
    }
}