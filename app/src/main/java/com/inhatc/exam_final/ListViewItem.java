package com.inhatc.exam_final;
import android.graphics.drawable.Drawable;

public class ListViewItem {
    private Drawable iconDrawable ;
    private String titleStr ;
    private String contentStr ;
    private String keywordStr ;

    public void setIcon(Drawable icon) {
        iconDrawable = icon ;
    }
    public void setTitle(String title) {
        titleStr = title ;
    }
    public void setContent(String content) {
        contentStr = content ;
    }
    public void setKeyword(String keyword) {
        keywordStr = keyword ;
    }

    public Drawable getIcon() {
        return this.iconDrawable ;
    }
    public String getTitle() {
        return this.titleStr ;
    }
    public String getContent() {
        return this.contentStr ;
    }
    public String getKeyword() {
        return this.keywordStr ;
    }
}
