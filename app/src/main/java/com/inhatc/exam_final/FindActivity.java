package com.inhatc.exam_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FindActivity extends AppCompatActivity {

    EditText editId, editName;
    Button btnFind;
    TextView txtPw;

    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        editId = (EditText) findViewById(R.id.editId);
        editName = (EditText) findViewById(R.id.editName);
        txtPw = (TextView) findViewById(R.id.txtPw);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        btnFind = (Button) findViewById(R.id.btnFind);
        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = editId.getText().toString().trim();
                String name = editName.getText().toString().trim();

                List<String> idList = new ArrayList<>();

                if (id.length() == 0 || name.length() == 0) {
                    Toast toast = Toast.makeText(FindActivity.this, "모든 항목을 입력해주세요.", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    mDatabaseReference.child("user").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Log.e("firebase", "Error getting data", task.getException());
                            }
                            else {
                                String findPw = null;

                                ArrayList<HashMap<String, String>> members = (ArrayList<HashMap<String, String>>) task.getResult().getValue();
                                for (HashMap<String, String> member : members) {
                                    if (member.get("userId").equals(id) && member.get("userName").equals(name)) {
                                        idList.add(member.get("userId"));
                                        findPw = member.get("userPw");
                                    }
                                }
                                if (idList.size() == 0) {
                                    Toast toast = Toast.makeText(FindActivity.this, "찾으시는 가입정보가 없습니다.", Toast.LENGTH_LONG);
                                    toast.show();
                                } else {
                                    txtPw.setText(findPw);
                                }
                            }
                        }
                    });
                }
            }
        });
    }
}