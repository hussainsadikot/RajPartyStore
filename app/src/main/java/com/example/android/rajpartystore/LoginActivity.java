package com.example.android.rajpartystore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.rajpartystore.Model.Users;
import com.example.android.rajpartystore.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    private EditText InputNumber,Inputpassword;
    private Button LoginButton;
    private ProgressDialog loadingbar;
    private String parentDBname = "User";
    private CheckBox checkBoxRememberMe;
    private TextView AdminLink,NotAdminLink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        InputNumber = findViewById(R.id.et_phone_number_login);
        Inputpassword = findViewById(R.id. et_pwd_login);
        LoginButton = findViewById(R.id.login_btn);
        AdminLink = findViewById(R.id.admin_link_tv);
        NotAdminLink =findViewById(R.id.not_admin_link_tv);


        loadingbar = new ProgressDialog(this);
        checkBoxRememberMe = (CheckBox) findViewById(R.id.remeber_me_chkbox);
        Paper.init(this);
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginButton.setText("Login Admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                parentDBname ="Admin";
            }
        });
        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginButton.setText("Login");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                parentDBname ="User";
            }
        });
    }

    private void login() {

        String phone = InputNumber.getText().toString().trim();
        String password = Inputpassword.getText().toString().trim();

        if(TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Please Write Your Number", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please Write Password ", Toast.LENGTH_SHORT).show();
        }
        else {
            loadingbar.setTitle("Login Account");
            loadingbar.setMessage("Please Wait, While we are checking credential");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();
            AllowAccess(phone,password);
        }


    }

    private void AllowAccess(final String phone, final String password) {
            if(checkBoxRememberMe.isChecked()){
                Paper.book().write(Prevalent.UserPhoneKey,phone);
                Paper.book().write(Prevalent.UserPasswordKey,password);
            }
        final DatabaseReference rootref;
        rootref= FirebaseDatabase.getInstance().getReference();
        rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentDBname).child(phone).exists()) {
                    Users users = dataSnapshot.child(parentDBname).child(phone).getValue(Users.class);
                    if (users.getPhone().equals(phone)) {
                        if (users.getPassword().equals(password)) {
                            if(parentDBname.equals("Admin")) {
                                Toast.makeText(LoginActivity.this, "Admin Successfully Logged In...", Toast.LENGTH_SHORT).show();
                                loadingbar.dismiss();
                                startActivity(new Intent(LoginActivity.this, AdminAddNewProductsActivity.class));
                            }else if(parentDBname.equals("User")){
                                Toast.makeText(LoginActivity.this, " Successfully Logged In...", Toast.LENGTH_SHORT).show();
                                loadingbar.dismiss();
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            }
                        }else{
                            Toast.makeText(LoginActivity.this, "Password is Incorrect", Toast.LENGTH_SHORT).show();
                        }

                    }


                }
                else {
                    Toast.makeText(LoginActivity.this, "Account does not Exist.", Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
