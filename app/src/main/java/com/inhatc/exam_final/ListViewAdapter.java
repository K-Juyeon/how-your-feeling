package com.inhatc.exam_final;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter extends BaseAdapter{

    ArrayList<ListViewAdapterData> list = new ArrayList<ListViewAdapterData>();
    private Context context;
    private List<String> list2;
    private LayoutInflater inflate;
    private ViewHolder viewHolder;

    public ListViewAdapter(List<String> list2, Context context){
        this.list2 = list2;
        this.context = context;
        this.inflate = LayoutInflater.from(context);
    }
    //배열의 크기 반환
    @Override
    public int getCount() {
        return list.size();
    }
    //배열에 아이템을 현재 위치값을 넣어 가져온다
    @Override
    public Object getItem(int i) {
        return list.get(i);
    }
    //위치값을 반환
    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            view = inflate.inflate(R.layout.item_listview,null);

            viewHolder = new ViewHolder();
            viewHolder.label = (TextView) view.findViewById(R.id.item_title);

            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)view.getTag();
        }

        // 리스트에 있는 데이터를 리스트뷰 셀에 뿌린다.
        viewHolder.label.setText(list.get(i)+"");

        final Context context = viewGroup.getContext();

        //리스트뷰에 아이템이 인플레이트 되어있는지 확인한후
        //아이템이 없다면 아래처럼 아이템 레이아웃을 인플레이트 하고 view객체에 담는다.
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_listview,viewGroup,false);
        }


        //아이템에 존재하는 텍스트뷰 객체들을 view객체에서 찾아 가져온다
        TextView tvId = (TextView)view.findViewById(R.id.item_id);
        TextView tvTitle = (TextView)view.findViewById(R.id.item_title);
        TextView tvContent = (TextView)view.findViewById(R.id.item_content);
        TextView tvKeyword = (TextView)view.findViewById(R.id.item_keyword);

        //현재 포지션에 해당하는 아이템에 글자를 적용하기 위해 list배열에서 객체를 가져온다.
        ListViewAdapterData listdata = list.get(i);

        //가져온 객체안에 있는 글자들을 각 뷰에 적용한다
        tvId.setText(Integer.toString(listdata.getId()));
        tvTitle.setText(listdata.getTitle());
        tvContent.setText(listdata.getContent());
        tvKeyword.setText(listdata.getKeyword());
//        imageView.setImageResource(Integer.parseInt(listdata.getImg()));

        return view;
    }

    //ArrayList로 선언된 list 변수에 목록 채워주기
    public void addItemToList(int id, String title, String content, String keyword, String img){
        ListViewAdapterData listdata = new ListViewAdapterData();

        listdata.setId(id);
        listdata.setTitle(title);
        listdata.setContent(content);
        listdata.setKeyword(keyword);
        listdata.setImg(img);

        //값들의 조립이 완성된 listdata 객체 한개를 list배열에 추가
        list.add(listdata);
    }

    class ViewHolder{
        public TextView label;
    }

}
