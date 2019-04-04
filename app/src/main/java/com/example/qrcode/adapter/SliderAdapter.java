package com.example.qrcode.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.qrcode.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context){
        this.context = context;
    }

    //Arrays
    public int[] slide_images = {
            R.drawable.everis,
            R.drawable.everis,
            R.drawable.everis,
            R.drawable.everis
    };

    public String[] slide_headings = {
            "Cadastre-se no app",
            "Encontre a everis",
            "Interaja e ganhe phygits",
            "Compre produtos"
    };

    public String[] slide_descs = {
            "Use \"Entrar\" para entrar nas telas de login e cadastro do app. Você pode se cadastrar preenchendo os dados e logar em nosso app para ter acesso a loja da everis e para ganhar phygits interagindo com as logos da everis espalhados pela feira.",
            "Use \"Encontrar a everis\" para encontrar o stand da everis na feira.",
            "Use \"Interagir\" para interagir com os logos da everis e ganhar phygits. Você pode usar seus phygits para comprar produtos na loja da everis.",
            "Use \"Comprar\" para entrar na loja da everis e gastar seus phygits. Ao selecionar os itens que deseja comprar clique em \"Gerar QRCode\" e mostre o QRCode gerado para a atendente em nossa loja. Pronto! Seus phygits serão gastos e você receberá os produtos que selecionou."
    };

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(View view,Object o) {
        return view == (ConstraintLayout) o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {


        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        CircleImageView slideImageView = view.findViewById(R.id.slide_image);
        TextView slideHeading = view.findViewById(R.id.slide_heading);
        TextView slideDescription = view.findViewById(R.id.slide_desc);

        slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);
        slideDescription.setText(slide_descs[position]);

        container.addView(view);

        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}
