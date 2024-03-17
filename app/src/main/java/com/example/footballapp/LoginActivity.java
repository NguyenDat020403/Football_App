package com.example.footballapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEdit,passwordEdit;
    private Button btnlogin;
    private TextView txtregister;
    FirebaseAuth mAuth;

    private GoogleSignInClient client;
    private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == 1){
                Intent i = result.getData();

                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(i);
                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                            .addOnCompleteListener(task1 -> {
                                if(task1.isSuccessful()){
                                    Intent intent = new Intent(getApplicationContext(), leagueSelect.class);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(LoginActivity.this, task1.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });


                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(LoginActivity.this, "Đăng nhập Google không thành công", Toast.LENGTH_SHORT).show();
            }
        }
    });
    private String def = "708486072632-dmqdh6t2va2pl8btnpk26gllljq1vigp.apps.googleusercontent.com";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        emailEdit = (EditText) findViewById(R.id.edtemail);
        passwordEdit = (EditText) findViewById(R.id.edtpassword);
        btnlogin = (Button) findViewById(R.id.btnSignin);
        txtregister = (TextView) findViewById(R.id.txtSignup);
//        btnLoginGoogle = (Button) findViewById(R.id.btnSignInGoogle);
//        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(def)
//                .requestEmail()
//                .build();
//
//
//        client = GoogleSignIn.getClient(this,options);

//        btnLoginGoogle.setOnClickListener(v -> {
//            Intent i = client.getSignInIntent();
//            mActivityResultLauncher.launch(i);
//
//        });

        btnlogin.setOnClickListener(v -> login());
        txtregister.setOnClickListener(v -> register());
    }



    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            Intent intent = new Intent(this, leagueSelect.class);
            startActivity(intent);
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().signOut();
//        client.signOut();
    }
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
