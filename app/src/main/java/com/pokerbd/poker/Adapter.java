package com.pokerbd.poker;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class Adapter extends PagerAdapter {

    private List<Model> models;
    private LayoutInflater layoutInflater;
    private Context context;

    public Adapter(List<Model> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @Override
    public float getPageWidth(int position){
        return(0.33f);
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item, container, false);

        ImageView imageView;
        TextView blind, buyin, table_name;

        imageView = view.findViewById(R.id.image);
        blind = view.findViewById(R.id.blind);
        buyin = view.findViewById(R.id.buyin);
        table_name = view.findViewById(R.id.table_name);

        imageView.setImageResource(models.get(position).getImage());
        blind.setText(models.get(position).getBlind());
        buyin.setText(models.get(position).getBuyin());
        table_name.setText(models.get(position).get_tableName());


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LobbyScreen.class);
                intent.putExtra("param", models.get(position).get_tableName());
                context.startActivity(intent);
                //finish();
            }
        });

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
