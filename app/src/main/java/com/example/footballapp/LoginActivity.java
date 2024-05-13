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
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
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

    public static GoogleSignInClient googleSignInClient;
    String default_web_client_id = "708486072632-7pq7v8t8a8uo7r7llahg4eb09ioif9us.apps.googleusercontent.com";

    private static final int REQ_ONE_TAP = 2;  // Can be any integer unique to the Activity.
    private boolean showOneTapUI = true;


    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult o) {
            if (o.getResultCode() == RESULT_OK){
                Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(o.getData());
                try {
                    GoogleSignInAccount signInAccount = accountTask.getResult(ApiException.class);
                    AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(),null);
                    mAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                mAuth = FirebaseAuth.getInstance();
                                Toast.makeText(getApplicationContext(),"Đăng nhập thành công",Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(LoginActivity.this, leagueSelect.class);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Đăng nhập không thành công",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }catch (ApiException e){

                }
            }
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);


        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null){
            Intent intent = new Intent(LoginActivity.this, leagueSelect.class);
            startActivity(intent);
        }


        emailEdit = (EditText) findViewById(R.id.edtemail);
        passwordEdit = (EditText) findViewById(R.id.edtpassword);
        btnlogin = (Button) findViewById(R.id.btnSignin);
        btnGoogle =  (ImageView) findViewById(R.id.btnGoogle);
        txtregister = (TextView) findViewById(R.id.txtSignup);
        txtforgotPassword = (TextView) findViewById(R.id.txtForgotPassword);

        FirebaseApp.initializeApp(this);

        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(default_web_client_id)
                        .requestEmail().build();

        googleSignInClient = GoogleSignIn.getClient(LoginActivity.this,options);

        btnGoogle.setOnClickListener(v -> {
//            FirebaseAuth.getInstance().signOut();
//            googleSignInClient.signOut();
           Intent intent = googleSignInClient.getSignInIntent();
           activityResultLauncher.launch(intent);

        });

        txtforgotPassword.setOnClickListener(v -> {
                resetPassword();
        });
        btnlogin.setOnClickListener(v -> login());
        txtregister.setOnClickListener(v -> register());

    }





//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        FirebaseAuth.getInstance().signOut();
//        googleSignInClient.signOut();
//
//    }

    private void resetPassword() {
        Intent i = new Intent(LoginActivity.this,ResetPasswordActivity.class);
        startActivity(i);
    }



//    @Override
//    protected void onStop() {
//        super.onStop();
//        FirebaseAuth.getInstance().signOut();
//        googleSignInClient.signOut();
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
