package com.example.login_register;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

class RegisterActivity extends AppCompatActivity {
    EditText password, username, email;
    Button btn_register;
    DatabaseReference reference;
    FirebaseAuth Auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        email=findViewById(R.id.email);

        Auth=FirebaseAuth.getInstance();

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Register");
        getActionBar().setDisplayHomeAsUpEnabled(true);


        btn_register.setOnClickListener(v -> {
            String txt_username = username.getText().toString();
            String txt_email = email.getText().toString();
            String txt_password = password.getText().toString();
            if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                Toast.makeText(RegisterActivity.this,"All Fileds are required", Toast.LENGTH_SHORT).show();
            }
            else if (txt_password.length()<6){
                Toast.makeText(RegisterActivity.this,"Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            }
            else {
                register(txt_username,txt_email,txt_password);
            }
        });
    }

    private void  register  (String username,String password,String email){

        Auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser FirebaseUser = Auth.getCurrentUser();
                        assert FirebaseUser != null;
                        String userid = FirebaseUser.getUid();

                        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("id", userid);
                        hashMap.put("username", username);
                        hashMap.put("imageurl", "default");
                        reference.setValue(hashMap).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Intent intent = new Intent(RegisterActivity.this, RegisterActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                finish();
                            }

                        });

                    } else {
                        Toast.makeText(RegisterActivity.this, "you can't register with this email or password ", Toast.LENGTH_SHORT).show();
                    }


    });
    }
}