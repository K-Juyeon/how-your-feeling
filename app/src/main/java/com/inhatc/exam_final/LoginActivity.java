package com.inhatc.exam_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    EditText edtId, edtPw;
    Button btnSignin;
    private DatabaseReference mDatabaseReference;
    User user = new User();
    //https://bellog.tistory.com/3

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtId = (EditText) findViewById(R.id.editId);
        edtPw = (EditText) findViewById(R.id.editPw);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        TextView btnSignup = (TextView) findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(intent);
            }
        });

        TextView btnFind = (TextView) findViewById(R.id.btnFind);
        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, FindActivity.class);
                LoginActivity.this.startActivity(intent);
            }
        });

        btnSignin = (Button) findViewById(R.id.btnSignin);
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = edtId.getText().toString().trim();
                String pw = edtPw.getText().toString().trim();

                if (id.length() == 0 || pw.length() == 0) {
                    Toast toast = Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 입력해주세요.", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                } else {
                    login(id, pw);
                }
            }
        });
    }

    public void login(String id, String pw) {
        mDatabaseReference.child("user").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                boolean flag = false;
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    ArrayList<HashMap<String, String>> members = (ArrayList<HashMap<String, String>>) task.getResult().getValue();
                    for (HashMap<String, String> member : members) {
                        if (member.get("userId").equals(id) && member.get("userPw").equals(pw)) {
                            flag = true;
                            user.setUser_id(id);
                            user.setUser_pw(pw);
                            Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                    if (flag == false) {
                        Toast toast = Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 확인해주세요.", Toast.LENGTH_LONG);
                        toast.show();
                        return;
                    }
                }
            }
        });
    }
}