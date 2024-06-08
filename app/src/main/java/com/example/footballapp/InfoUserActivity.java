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
import android.transition.Explode;
import android.transition.Transition;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import com.example.footballapp.model.User;
import com.example.footballapp.model.UserResponse;
import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Objects;

public class InfoUserActivity extends AppCompatActivity {

    ActivityInfoUserBinding binding;

    ImageView imvPhotoUser;
    Button btnLogout,btnSaveChange;

    ActivityResultLauncher<Intent> launcher;
    Uri PhotoUri;

    private DatabaseReference mDatabase;

    FirebaseAuth mAuth;
    StorageReference storageProfilePicsRef;

    boolean captureState = true;
    boolean choseImv = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInfoUserBinding.inflate(getLayoutInflater());
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        binding.bottomNavigation.setSelectedItemId(R.id.nav_profile);
        setContentView(binding.getRoot());
        Anhxa();
        mAuth = FirebaseAuth.getInstance();
        storageProfilePicsRef = FirebaseStorage.getInstance().getReference().child("Profile Pic");
        exportsUser();
        addEvents();
        passUserData();
        getCurrentUserInfo();
        photoUserChange();
    }

    private void exportsUser() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            String name = user.getDisplayName();
            String email = user.getEmail();
            String photourl = String.valueOf(user.getPhotoUrl());
            UserResponse userResponse = new UserResponse(userId, name, email,photourl);

            // Convert UserResponse to JSON
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonResponse = gson.toJson(userResponse);

            // Write JSON to file
            writeToFile(jsonResponse);
        }
    }

    private void writeToFile(String jsonResponse) {
        // Define the file name and path
        String fileName = "user_response.json";
        File path = getApplicationContext().getFilesDir();
        File file = new File(path, fileName);

        try {
            FileOutputStream stream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(stream);
            BufferedWriter writer = new BufferedWriter(outputStreamWriter);

            // Write JSON response to the file
            writer.write(jsonResponse);

            writer.close();
            outputStreamWriter.close();
            stream.close();

            Toast.makeText(this, "Data written to " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error writing file", Toast.LENGTH_SHORT).show();
        }
    }



    public void passUserData(){
        String userUserName = binding.txtUserName.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUserName);
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String nameFromDB = snapshot.child(userUserName).child("name").getValue(String.class);
                    String emailFromDB = snapshot.child(userUserName).child("email").getValue(String.class);
                    String usernameFromDB = snapshot.child(userUserName).child("username").getValue(String.class);
                    String passwordFromDB = snapshot.child(userUserName).child("password").getValue(String.class);
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");
                    reference.child(userUserName).child("email").setValue(binding.edtEmailChange.getText().toString());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }





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

                Transition explode = new Explode();
                explode.setDuration(0);
                getWindow().setExitTransition(explode);

                if(itemId == R.id.nav_tables){
                    startActivity(new Intent(InfoUserActivity.this, MainActivity.class));
//                    overridePendingTransition(R.anim.enter_from_right,R.anim.exit_to_left);
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
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                if (captureState) { // Camera
                    Bitmap bitmap = (Bitmap) Objects.requireNonNull(result.getData().getExtras()).get("data");
                    assert bitmap != null;
                    PhotoUri = Uri.parse(String.valueOf(bitmap));
                    imvPhotoUser.setImageBitmap(bitmap);
                } else { // Gallery
                    PhotoUri = result.getData().getData();
                    imvPhotoUser.setImageURI(PhotoUri);
                }
                Log.d("ANh photoUserChange", " " + PhotoUri);
            }
        });

        binding.btnSaveChange.setOnClickListener(v -> changeUserInfo());
    }




    private void changeUserInfo() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Kiểm tra nếu PhotoUri không null trước khi sử dụng
            if (PhotoUri != null) {
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(binding.edtUserName.getText().toString())
                        .setPhotoUri(PhotoUri)
                        .build();

                user.updateProfile(profileUpdates).addOnSuccessListener(unused -> {
                    Toast.makeText(InfoUserActivity.this, "Đã cập nhật.", Toast.LENGTH_SHORT).show();
                    getCurrentUserInfo();
                });

                binding.btnSaveChange.setVisibility(View.GONE);
                binding.edtUserName.setEnabled(false);
                binding.edtEmailChange.setEnabled(false);
            } else {
                // Xử lý trường hợp PhotoUri null
                Log.e("InfoUserActivity", "PhotoUri is null");
                // Hiển thị thông báo hoặc thực hiện hành động phù hợp
            }
        }
    }




    private void getCurrentUserInfo() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            
            String email = user.getEmail();

//            Log.d("ANh", Objects.requireNonNull(user.getPhotoUrl()).toString());
            Uri photoUrl = user.getPhotoUrl();
            if (photoUrl != null) {
                Log.d("ANh", photoUrl.toString());
                Glide.with(InfoUserActivity.this).load(photoUrl).into(imvPhotoUser);
            } else {
                // Xử lý trường hợp khi photoUrl là null, có thể làm gì đó như hiển thị ảnh mặc định hoặc thông báo cho người dùng.
            }//            Picasso.get().load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).into(imvPhotoUser);
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