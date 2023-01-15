package com.inhatc.exam_final;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class AddActivity extends AppCompatActivity {
    EditText title, place, content; // 제목, 위치, 내용
    Button btnAdd; // 추가 버튼
    List<String> list = new ArrayList<>(); // 체크박스 담는 리스트
    String result = ""; // 선택된 키워드

    float star_result = 0.0F; // 별점

    private DatabaseReference mDatabaseReference;
    User user;

    /*이미지 설정*/
    private final int GET_GALLERY_IMAGE = 200;
    ImageView imageview;
    String imgName = "image";       // 이미지 이름
    String imgNameRandom;           // 랜덤 변수
    Uri selectedImageUri;           // uri 받을 변수
    Integer arr_random_num[] = new Integer[10]; // 랜덤

    /*별점*/
    RatingBar ratingBar;
    TextView rating_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        title = findViewById(R.id.title);
        place = findViewById(R.id.place);
        content = findViewById(R.id.textarea);
        imageview = (ImageView)findViewById(R.id.imageView);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        user = new User();

        Intent intent = getIntent();
        String loc = intent.getExtras().getString("lat");
        loc = loc + ", ";
        loc = loc + intent.getExtras().getString("lon");

        place.setText(loc);

        /* 체크박스 */
        CheckBox cb1 = (CheckBox) findViewById(R.id.check1);
        CheckBox cb2 = (CheckBox) findViewById(R.id.check2);
        CheckBox cb3 = (CheckBox) findViewById(R.id.check3);
        CheckBox cb4 = (CheckBox) findViewById(R.id.check4);
        CheckBox cb5 = (CheckBox) findViewById(R.id.check5);
        CheckBox cb6 = (CheckBox) findViewById(R.id.check6);
        CheckBox cb7 = (CheckBox) findViewById(R.id.check7);
        CheckBox cb8 = (CheckBox) findViewById(R.id.check8);
        CheckBox cb9 = (CheckBox) findViewById(R.id.check9);
        CheckBox cb10 = (CheckBox) findViewById(R.id.check10);
        CheckBox cb11 = (CheckBox) findViewById(R.id.check11);

        /*
        맛집 추가 이벤트:
            검증
            - 테그가 몇개 이상인지
        */
        btnAdd = findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                int cnt = 0;

                if (cb1.isChecked()) {cnt +=1; list.add(String.valueOf(cb1.getText()));}
                if (cb2.isChecked()) {cnt +=1; list.add(String.valueOf(cb2.getText()));}
                if (cb3.isChecked()) {cnt +=1; list.add(String.valueOf(cb3.getText()));}
                if (cb4.isChecked()) {cnt +=1; list.add(String.valueOf(cb4.getText()));}
                if (cb5.isChecked()) {cnt +=1; list.add(String.valueOf(cb5.getText()));}
                if (cb6.isChecked()) {cnt +=1; list.add(String.valueOf(cb6.getText()));}
                if (cb7.isChecked()) {cnt +=1; list.add(String.valueOf(cb7.getText()));}
                if (cb8.isChecked()) {cnt +=1; list.add(String.valueOf(cb8.getText()));}
                if (cb9.isChecked()) {cnt +=1; list.add(String.valueOf(cb9.getText()));}
                if (cb10.isChecked()) {cnt +=1; list.add(String.valueOf(cb10.getText()));}
                if (cb11.isChecked()) {cnt +=1; list.add(String.valueOf(cb11.getText()));}

                /* 만약에 키워드가 3개 이상 선택되었을 경우 */
                if(cnt > 3) {
                    Toast.makeText(getApplicationContext(),"키워드 개수를 초과했습니다. ", Toast.LENGTH_SHORT).show();
                } // 다시 처음 추가 화면으로 돌아간다.
                else {

                    result = String.join("      ", list);

                    mDatabaseReference.child("diary").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Log.e("firebase", "Error getting data", task.getException());
                            }
                            else {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference reference = database.getReference();

                                reference.child("diary").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        if (!task.isSuccessful()) {
                                            Log.e("firebase", "Error getting data", task.getException());
                                        }
                                        else {
                                            String cnt = String.valueOf(task.getResult().getChildrenCount());

                                            reference.child("diary").child(cnt).child("user").setValue(user.getUser_id());
                                            reference.child("diary").child(cnt).child("title").setValue(title.getText().toString().trim());
                                            reference.child("diary").child(cnt).child("place").setValue(place.getText().toString().trim());
                                            reference.child("diary").child(cnt).child("content").setValue(content.getText().toString().trim());
                                            reference.child("diary").child(cnt).child("result").setValue(result.trim());
                                            reference.child("diary").child(cnt).child("star_result").setValue(star_result);
                                            reference.child("diary").child(cnt).child("img").setValue(imgName.trim());

                                            Toast.makeText(AddActivity.this, "등록 성공", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(), DiaryActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                });
                            }
                        }
                    });
                    Intent intent = new Intent(getApplicationContext(), DiaryActivity.class);
                    startActivity(intent);
                } // main list가 업데이트되고 사용자는 main list로 화면이 바뀐다.
            }
        });


        /* 별점 매기기 */
        rating_result = findViewById(R.id.rating_result);
        rating_result.setText("0점");
        ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rating_result.setText(rating + "점");
                star_result = rating;
            }
        });

        /*이미지 설정, 갤러리에서 불러오기*/
        imageview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent. setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });
    }

    /*이미지 불러오기 2*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            ContentResolver resolver = getContentResolver();
            imageview.setImageURI(selectedImageUri);
            try {
                InputStream instream = resolver.openInputStream(selectedImageUri);
                Bitmap imgBitmap = BitmapFactory.decodeStream(instream);
                instream.close();   // 스트림 닫아주기
                saveBitmapToJpeg(imgBitmap);    // 내부 저장소에 저장
            } catch (Exception e) {
                Log.d("파일 불러오기:", "실패");
            }


        }
    }
    public void saveBitmapToJpeg(Bitmap bitmap) {   // 선택한 이미지 내부 저장소에 저장
        Random random = new Random();
        for(int i = 0; i < arr_random_num.length; i++){
            //0~999까지 10000개의 숫자중 랜덤발생 +1을 해줘서 1~10000으로 바꿈
            arr_random_num[i] = random.nextInt(10000) + 1;
            //랜덤숫자를 텍스트뷰에 대입
            imgNameRandom = Integer.toString(arr_random_num[i]);
        }
        imgName = imgName + imgNameRandom + ".png";
        File tempFile = new File(getCacheDir(), imgName);    // 파일 경로와 이름 넣기
        try {
            tempFile.createNewFile();   // 자동으로 빈 파일을 생성하기
            FileOutputStream out = new FileOutputStream(tempFile);  // 파일을 쓸 수 있는 스트림을 준비하기
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);   // compress 함수를 사용해 스트림에 비트맵을 저장하기
            out.close();    // 스트림 닫아주기
        } catch (Exception e) {
            Log.d("파일 저장:", "실패");
        }
    }
}