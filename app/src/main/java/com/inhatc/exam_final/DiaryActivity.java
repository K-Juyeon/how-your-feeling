package com.inhatc.exam_final;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import java.util.List;

public class DiaryActivity extends AppCompatActivity {

    private String title, content, result, id;

    /* 검색 활용 */
    private List<String> list;          // 데이터를 넣은 리스트변수
    private ListView listView;          // 검색을 보여줄 리스트변수
    private ArrayList<String> arraylist;
    private ListViewAdapter adapter;

    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        Button btnViewMap = (Button) findViewById(R.id.btnViewMap);
        btnViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DiaryActivity.this, MapActivity.class);
                DiaryActivity.this.startActivity(intent);
            }
        });

        listView = (ListView) findViewById(R.id.listView);


        // 리스트를 생성한다.
        list = new ArrayList<String>();

        // 리스트의 모든 데이터를 arraylist에 복사한다.// list 복사본을 만든다.
        arraylist = new ArrayList<String>();
        arraylist.addAll(list);

        //리스트뷰에 목록 채워주는 도구인 adapter 준비
        adapter = new ListViewAdapter(list, this);

        mDatabaseReference.child("diary").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    int cnt = (int) task.getResult().getChildrenCount();
                    int i = 0;

                    ArrayList<HashMap<String, String>> members = (ArrayList<HashMap<String, String>>) task.getResult().getValue();
                    for (HashMap<String, String> member : members) {
                        title = member.get("title");
                        content = member.get("content");
                        result = member.get("result");
                        adapter.addItemToList(i, title, content, result, id);
                        listView.setAdapter(adapter);
                        i++;
                    }
                }
            }
        });

        /* 상세 내용 불러오기 */
        /* 리스트 아아템 눌렀을 경우 이벤트 리스너 실행 */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long ids) {
                /* 상세내용 class 연결 - intent */
                Intent intent = new Intent(DiaryActivity.this, DetailActivity.class);

                /* 리스트 아이템 id 연결 */
                id = ((TextView) view.findViewById(R.id.item_id)).getText().toString();
                title = ((TextView) view.findViewById(R.id.item_title)).getText().toString();
                content = ((TextView) view.findViewById(R.id.item_content)).getText().toString();
                result = ((TextView) view.findViewById(R.id.item_keyword)).getText().toString();

                /* putExtra의 첫 값은 식별 태그, 뒤에는 다음 화면에 넘길 값 */
                intent.putExtra("_id", id);
                intent.putExtra("title", title);
                intent.putExtra("content", content);
                intent.putExtra("result", result);

                startActivity(intent);

            }
        });

    }
}