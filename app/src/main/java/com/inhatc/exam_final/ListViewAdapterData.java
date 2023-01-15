package com.inhatc.exam_final;

public class ListViewAdapterData {

    private int id;         // 번호
    private String title;   // 제목
    private String content; // 내용
    private String keyword; // 키워드
    private String place;   // 장소
    private float star;     // 별점
    private String img;     // 이미지


    public void setTitle(String title){this.title = title;}
    public String getTitle(){return this.title;}

    public void setContent(String content){this.content = content;}
    public String getContent(){return this.content;}

    public void setKeyword(String keyword){this.keyword = keyword;}
    public String getKeyword(){return this.keyword;}

    public void setPlace(String place){this.place = place;}
    public String getPlace(){return this.place;}

    public void setStar(float star){this.star = star;}
    public float getStar(){return this.star;}

    public void setId(int id){this.id = id;}
    public int getId(){return this.id;}

    public void setImg(String img){this.img = img;}
    public String getImg(){return this.img;}


    public ListViewAdapterData() {
        this.id = id;
        this.title = title;
        this.content = content;
        this.keyword = keyword;
        this.place = place;
        this.star = star;
        this.img = img;
    }

}
