package com.example.android.rajpartystore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.rajpartystore.Model.Users;
import com.example.android.rajpartystore.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button JoinNowBtn,SignInBtn;
    private ProgressDialog loadingbar;
    private String parentDBname = "User";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadingbar = new ProgressDialog(this);
        JoinNowBtn = findViewById(R.id.goto_signUp_btn);
        SignInBtn = findViewById(R.id.goto_login_btn);
        Paper.init(this);
        SignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
        JoinNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
            }
        });

        String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);
        if (UserPhoneKey != "" && UserPasswordKey != ""){
            if (!TextUtils.isEmpty(UserPhoneKey)&& !TextUtils.isEmpty(UserPasswordKey)){

                AllowAccess(UserPhoneKey,UserPasswordKey);
                loadingbar.setTitle("Already Logged in Account");
                loadingbar.setMessage("Please Wait, While we are checking credential");
                loadingbar.setCanceledOnTouchOutside(false);
                loadingbar.show();




            }

        }
    }

    private void AllowAccess(final String phone, final String password) {
        final DatabaseReference rootref;
        rootref= FirebaseDatabase.getInstance().getReference();
        rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentDBname).child(phone).exists()) {
                    Users users = dataSnapshot.child(parentDBname).child(phone).getValue(Users.class);
                    if (users.getPhone().equals(phone)) {
                        if (users.getPassword().equals(password)) {
                            Toast.makeText(MainActivity.this, "Successfully,Logged In...", Toast.LENGTH_SHORT).show();
                            loadingbar.dismiss();
                            startActivity(new Intent(MainActivity.this, HomeActivity.class));

                        }else{
                            Toast.makeText(MainActivity.this, "Password is Incorrect", Toast.LENGTH_SHORT).show();
                        }

                    }


                }
                else {
                    Toast.makeText(MainActivity.this, "Account does not Exist.", Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
