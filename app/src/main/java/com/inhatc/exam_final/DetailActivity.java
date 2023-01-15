package com.inhatc.exam_final;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class DetailActivity extends AppCompatActivity {
    private TextView id;
    private ImageView img;
    private EditText title, star, content, keyword, map;
    String board_seq = "";
    Button btnDelete, btnUpdate;
    int num; // 받은 아이디

    String imgPath;
    String place;

    private DatabaseReference mDatabaseReference;

    /* 이미지 불러오기 (파일) */
    public int getRawResIdByName(String resName) {
        String pkgName = this.getPackageName();
        // Return 0 if not found.
        int resID = this.getResources().getIdentifier(resName, "raw", pkgName);
        Log.i("AndroidVideoView", "Res Name: " + resName + "==> Res ID = " + resID);
        return resID;
    }

    /* 디테일 화면 (상세 내용) */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        /* xml과 id와 연결 */
        id = (EditText) findViewById(R.id.id_tv);
        title = (EditText) findViewById(R.id.title_tv);         // 제목
        star = (EditText) findViewById(R.id.star_tv);           // 별점
        keyword = (EditText) findViewById(R.id.keyword_tv);     // 키워드
        map = (EditText) findViewById(R.id.map_tv);             // 위치
        content = (EditText) findViewById(R.id.content_tv);     // 내용
        img = (ImageView) findViewById(R.id.imageView2);        // 이미지
        board_seq = getIntent().getStringExtra("board_seq");

        btnDelete = findViewById(R.id.btnDelete);  // 삭제하기
        btnUpdate = findViewById(R.id.btnUpdate);  // 수정하기

        // 보내온 Intent를 얻는다
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        Log.d("bundle", String.valueOf(bundle));

        InitData();


        // 새로 추가된 부분, select
        num = Integer.parseInt(bundle.getString("_id"));
        Log.d("snow", num+"|| printID");
        DBSearch(num);  // 넘긴 id 값 받음

        /* 삭제하기 */
        DeleteData();
        /* 수정하기 */
        UpdateData();

        mDatabaseReference.child("diary").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    String findPw = null;
                    ArrayList<HashMap<String, String>> diarys = (ArrayList<HashMap<String, String>>) task.getResult().getValue();
                    for (HashMap<String, String> diary : diarys) {
                        imgPath = diary.get("img");
                        place = diary.get("place");
                    }
                }
            }
        });

    }

    private void InitData() {

        // 해당 게시물의 데이터를 읽어오는 함수, 파라미터로 보드 번호를 넘김
        LoadBoard loadBoard = new LoadBoard();
        loadBoard.execute(board_seq);

    }


    class LoadBoard extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "onPreExecute");
        }
    }

    // SELECT * FROM best_list WHERE _id == num
    void DBSearch(Integer id) {
        mDatabaseReference.child("diary").child(id.toString()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    HashMap<String, String> member = (HashMap<String, String>) task.getResult().getValue();

                    String titles = member.get("title");
                    String places = member.get("place");
                    String contents = member.get("content");
                    String keywords = member.get("result");
                    String stars = String.valueOf(member.get("star_result"));
                    String imgs = member.get("img");

                    title.setText(titles);        // 제목
                    star.setText(stars+"");       // 별점
                    keyword.setText(keywords);    // 키워드
                    map.setText(places);          // 위치
                    content.setText(contents);    // 내용

                    try {
                        String imgpath = getCacheDir() + "/" + imgs;   // 내부 저장소에 저장되어 있는 이미지 경로
                        Bitmap bm = BitmapFactory.decodeFile(imgpath);
                        img.setImageBitmap(bm);   // 내부 저장소에 저장된 이미지를 이미지뷰에 셋
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "파일 로드 실패", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void DeleteData(){
        btnDelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mDatabaseReference.child("diary").child(String.valueOf(num)).removeValue();
                Toast.makeText(DetailActivity.this,"삭제 성공",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), DiaryActivity.class);
                startActivity(intent);
            }
        });
    }

    public void UpdateData(){
        btnUpdate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("title", title.getText().toString());
                map.put("content", content.getText().toString());
                map.put("result", keyword.getText().toString());
                map.put("star_result", star.getText().toString());
                map.put("img", imgPath);
                map.put("place", place);

                mDatabaseReference.child("diary").child(String.valueOf(num)).updateChildren(map);

                Toast.makeText(DetailActivity.this,"수정 성공",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), DiaryActivity.class);
                startActivity(intent);
            }
        });
    }
}





