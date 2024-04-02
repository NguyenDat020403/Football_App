package com.example.footballapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEdit,passwordEdit;
    private Button btnlogin;
    ImageView btnGoogle;
    private TextView txtregister;
    private TextView txtforgotPassword;
    FirebaseAuth mAuth;

    private GoogleSignInClient client;
    String def = "708486072632-dmqdh6t2va2pl8btnpk26gllljq1vigp.apps.googleusercontent.com";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        emailEdit = (EditText) findViewById(R.id.edtemail);
        passwordEdit = (EditText) findViewById(R.id.edtpassword);
        btnlogin = (Button) findViewById(R.id.btnSignin);
        btnGoogle =  (ImageView) findViewById(R.id.btnGoogle);
        txtregister = (TextView) findViewById(R.id.txtSignup);
        txtforgotPassword = (TextView) findViewById(R.id.txtForgotPassword);

        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(def)
                .requestEmail()
                .build();


        client = GoogleSignIn.getClient(this,options);

        btnGoogle.setOnClickListener(v -> {
            Intent i = client.getSignInIntent();
            startActivityForResult(i,1000);
        });

        txtforgotPassword.setOnClickListener(v -> {
                resetPassword();
        });
        btnlogin.setOnClickListener(v -> login());
        txtregister.setOnClickListener(v -> register());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_CANCELED){
        if(requestCode == 1000){
            Log.d("Tag","onActivityR google signin result");
            Task<GoogleSignInAccount> accountTask  = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = accountTask.getResult(ApiException.class);
                firebaseAuthGoogleAccount(account);

            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        }
        }
    }

    private void firebaseAuthGoogleAccount(@NonNull GoogleSignInAccount account) {
        Log.d("tag","firebaseAuthGoogleAccount being firebase auth with google");
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        mAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.d("tag","onSuccess: Login");
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        //get info
                        assert firebaseUser != null;
                        String uid = firebaseUser.getUid();
                        String email = firebaseUser.getEmail();

                        Log.d("tag","uid: " + uid);
                        Log.d("tag","email: " + email);

                        if(authResult.getAdditionalUserInfo().isNewUser()){
                            Log.d("tag","onSuccess: Account created ");

                            Toast.makeText(LoginActivity.this, "Account created...", Toast.LENGTH_SHORT).show();

                        }else{
                            Log.d("tag","onSuccess: Existing created ");

                            Toast.makeText(LoginActivity.this, "Existing created...", Toast.LENGTH_SHORT).show();

                        }
                        startActivity(new Intent(LoginActivity.this, MatchEvents.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("tag","onFailure Login Failed " );
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseAuth.getInstance().signOut();
    }

    private void resetPassword() {
        Intent i = new Intent(LoginActivity.this,ResetPasswordActivity.class);
        startActivity(i);
    }


//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if(user!=null){
//            Intent intent = new Intent(this, leagueSelect.class);
//            startActivity(intent);
//        }
//    }
//    @Override
//    protected void onStop() {
//        super.onStop();
//
////        client.signOut();
//    }
    private void login() {
        String email,pass;
        email = emailEdit.getText().toString();
        pass = passwordEdit.getText().toString();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Vui lòng nhập email!!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(pass)){
            Toast.makeText(this,"Vui lòng nhập password!!",Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Đăng nhập thành công",Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginActivity.this, leagueSelect.class);
                    startActivity(intent);

                }else{
                    Toast.makeText(getApplicationContext(),"Đăng nhập không thành công",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void register() {
        Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(i);

    }
}
