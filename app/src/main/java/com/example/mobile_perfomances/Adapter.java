package com.example.mobile_perfomances;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class Adapter extends BaseAdapter {

    protected Context mContext;
    String Image="";
    List<Perfom> list_p;

    public Adapter (Context mContext, List<Perfom> listPer)
    {
        this.mContext = mContext;
        this.list_p = listPer;
    }

    @Override
    public int getCount() {
        return list_p.size();
    }

    @Override
    public Object getItem(int i) {
        return list_p.get(i);
    }

    @Override
    public long getItemId(int i) {
        return list_p.get(i).getID();
    }

    private Bitmap getUserImage(String encodedImg)
    {
        if(encodedImg!=null&& !encodedImg.equals("null")) {
            byte[] bytes = Base64.decode(encodedImg, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
        else {
            return null;
        }
    }



    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        @SuppressLint("ViewHolder") View v = View.inflate(mContext,R.layout.item_mask,null);
        TextView Title = v.findViewById(R.id.t_Title);
        TextView Genre = v.findViewById(R.id.t_Genre);
        TextView Producer = v.findViewById(R.id.t_Producer);
        ImageView Image = v.findViewById(R.id.Img);

        Perfom perfomance = list_p.get(i);
        Title.setText(perfomance.getTitle());
        Genre.setText(perfomance.getGenre());
        Producer.setText(perfomance.getProducer());

        DecodeImageClass DI = new DecodeImageClass(mContext);
        Image.setImageBitmap(DI.getUserImage(perfomance.getImage()));

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, update_delete_data.class);
                intent.putExtra(Perfom.class.getSimpleName(), perfomance);
                mContext.startActivity(intent);
            }
        });

        return  v;
    }
}
