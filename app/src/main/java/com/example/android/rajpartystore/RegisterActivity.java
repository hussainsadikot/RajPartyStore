package com.example.android.rajpartystore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button createaccountButton;
    private EditText InputName,InputNumber,InputPassword;
    private ProgressDialog loadingbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        createaccountButton = findViewById(R.id.register_btn);
        InputName = findViewById(R.id.et_name_register);
        InputNumber = findViewById(R.id.et_phone_number_register);
        InputPassword = findViewById(R.id. et_pwd_register);
        loadingbar = new ProgressDialog(this);
        createaccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAccount();
            }
        });



    }

    private void CreateAccount() {
        String name = InputName.getText().toString().trim();
        String phone = InputNumber.getText().toString().trim();
        String password = InputPassword.getText().toString().trim();
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please Write Your Name", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Please Write Your Number", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please Write Password ", Toast.LENGTH_SHORT).show();
        }
        else {
            loadingbar.setTitle("Create Account");
            loadingbar.setMessage("Please Wait, While we are checking credential");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();
            validatePhoneNumberandPassword(name,phone,password);
            
        }

    }

    private void validatePhoneNumberandPassword(final String name, final String phone, final String password) {
        final DatabaseReference rootref;
        rootref= FirebaseDatabase.getInstance().getReference();
        rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.child("Users").child(phone).exists())){
                    HashMap<String,Object> userdataMap = new HashMap<>();
                    userdataMap.put("phone",phone);
                    userdataMap.put("name",name);
                    userdataMap.put("password",password);
                    rootref.child("User").child(phone).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "Congratulation,Account is created.", Toast.LENGTH_SHORT).show();
                                loadingbar.dismiss();
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            }
                            else{
                                loadingbar.dismiss();
                                Toast.makeText(RegisterActivity.this, "NetworkError", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }
                else{
                    Toast.makeText(RegisterActivity.this, "User Already Registered", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
