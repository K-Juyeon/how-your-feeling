package com.inhatc.exam_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText editName, editId, editPw, editChaptcha, editPwCheck;
    private Button btnSignup, btnCaptcha, btnCheck;
    private ImageView im;
    private DatabaseReference mDatabaseReference;
    TextView btnLogin;
    Captcha c;
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //firebase 접근 설정
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        editName = (EditText) findViewById(R.id.editName);
        editId = (EditText) findViewById(R.id.editId);
        editPw = (EditText) findViewById(R.id.editPw);
        editChaptcha = (EditText) findViewById(R.id.edtChaptcha);
        editPwCheck = (EditText) findViewById(R.id.editPwCheck);

        im = (ImageView) findViewById(R.id.capchaView);

        c = playCaptcha();

        btnLogin = (TextView) findViewById(R.id.btnSignin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnCheck = (Button) findViewById(R.id.btnCheck);
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = editId.getText().toString().trim();
                List<String> idList = new ArrayList<>();
                mDatabaseReference.child("user").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {

                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        }
                        else {
                            ArrayList<HashMap<String, String>> members = (ArrayList<HashMap<String, String>>) task.getResult().getValue();
                            for (HashMap<String, String> member : members) {
                                if (member.get("userId").equals(id)) {
                                    idList.add(member.get("userId"));
                                }
                            }
                            if (idList.size() == 0) {
                                flag = 3;
                                showMessage(RegisterActivity.this, "사용 가능한 아이디입니다.");
                            } else {
                                flag = 1;
                                showMessage(RegisterActivity.this, "이미 사용중인 아이디입니다.");
                            }
                        }
                    }
                });
            }
        });

        btnCaptcha = (Button) findViewById(R.id.btnCapcha);
        btnCaptcha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c = playCaptcha();
                editChaptcha.setText(null);
            }
        });

        btnSignup = (Button) findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //사용자 입력 정보
                String id = editId.getText().toString().trim();
                String pw = editPw.getText().toString().trim();
                String pwck = editPwCheck.getText().toString().trim();
                String name = editName.getText().toString().trim();
                String ans = editChaptcha.getText().toString().trim();

                if (id.length() == 0 || pw.length() == 0 || pwck.length() == 0 || name.length() == 0 || ans.length() == 0) {
                    showMessage(RegisterActivity.this, "모든 항목을 입력해주세요");
                } else {
                    if (flag == 0) {
                        showMessage(RegisterActivity.this, "아이디 중복확인을 해주세요.");
                    } else if (flag == 1) {
                        showMessage(RegisterActivity.this, "이미 사용중인 아이디입니다.");
                    } else {
                        if (pw.equals(pwck)) {
                            if (pw.length() < 5) {
                                showMessage(RegisterActivity.this, "비밀번호는 5자 이상으로 입력해주세요.");
                            } else {
                                if (ans.equals(c.answer)) {
                                    createUser(id);
                                } else {
                                    showMessage(RegisterActivity.this, "정확한 보안문자를 입력해주세요.");
                                }
                            }
                        } else {
                            showMessage(RegisterActivity.this, "비밀번호를 확인해주세요.");
                        }
                    }
                }
            }
        });
    }

    public Captcha playCaptcha() {
        Captcha t = new TextCaptcha(300, 100, 5, TextCaptcha.TextOptions.NUMBERS_AND_LETTERS);
        Captcha m = new MathCaptcha(300, 100, MathCaptcha.MathOptions.PLUS_MINUS_MULTIPLY);

        Captcha cList[] = {t, m};

        Random rand = new Random();

        Captcha c = cList[rand.nextInt(2)];
        im.setImageBitmap(c.image);
        im.setLayoutParams(new LinearLayout.LayoutParams(c.width * 5, c.height * 2));
        return c;
    }

    public void showMessage(Context context, String text) {
        //long 5초 short 2초
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.show();
    }


    public void createUser(String userid) {
        String pw = editPw.getText().toString().trim();
        String name = editName.getText().toString().trim();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference();

        reference.child("user").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    String cnt = String.valueOf(task.getResult().getChildrenCount());

                    reference.child("user").child(cnt).child("userId").setValue(userid);
                    reference.child("user").child(cnt).child("userName").setValue(name);
                    reference.child("user").child(cnt).child("userPw").setValue(pw);

                    Toast.makeText(RegisterActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}