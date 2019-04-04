package com.example.qrcode.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.qrcode.R;
import com.example.qrcode.model.Produto;
import com.example.qrcode.view.ListaProdutosActivity;

import java.util.ArrayList;
import java.util.List;

import static com.example.qrcode.view.ListaProdutosActivity.total;

public class AdapterProdutos extends RecyclerView.Adapter<AdapterProdutos.MyViewHolder> {

    private List<Produto> listaProdutos;
    private Context context;
    private List<String> carrinho = new ArrayList<>();

    public List<String> getCarrinho() {
        return carrinho;
    }

    public void setCarrinho(List<String> carrinho) {
        this.carrinho = carrinho;
    }

    public AdapterProdutos(List<Produto> lista, Context context) {
        this.listaProdutos = lista;
        this.context = context;
    }

    public interface ItemClickListener {
        void onItemClick(int position, Produto produto, String quantidade);
    }

    private ItemClickListener itemClickListener;

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void atualizarStatus(int position) {
        Produto produto = listaProdutos.get(position);
        if (produto.getStatus().equals("1")) {
            produto.setStatus("0");
        } else {
            produto.setStatus("1");
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemLista = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.adapter_produtos,
                viewGroup,
                false
        );
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {

        final Produto produto = listaProdutos.get(i);

        myViewHolder.produto.setText(produto.getProduto());
        myViewHolder.valor.setText(produto.getValor());
        myViewHolder.tvQuantidade.setText(produto.getQuantidade());

        myViewHolder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = myViewHolder.getAdapterPosition();

                int atual = Integer.parseInt(listaProdutos.get(position).getQuantidade());

                listaProdutos.get(position).setQuantidade(String.valueOf(atual + 1));

                myViewHolder.tvQuantidade.setText(listaProdutos.get(position).getQuantidade());

                int valorDefault = Integer.parseInt(listaProdutos.get(position).getValor());

                //myViewHolder.valor.setText(String.valueOf(valorDefault * novo));
                total = total + valorDefault;
                ((ListaProdutosActivity) context).updateTextTotal("Total: " + String.valueOf(total) + " pontos");
            }
        });

        myViewHolder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = myViewHolder.getAdapterPosition();
                int atual = Integer.parseInt(listaProdutos.get(position).getQuantidade());
                int novo;

                if (atual >= 1) {
                    listaProdutos.get(position).setQuantidade(String.valueOf(atual - 1));
                } else {
                    listaProdutos.get(position).setQuantidade(String.valueOf(1));
                }
                myViewHolder.tvQuantidade.setText(listaProdutos.get(position).getQuantidade());

                int valorDefault = Integer.parseInt(listaProdutos.get(position).getValor());

                if ((Integer.parseInt(listaProdutos.get(position).getQuantidade())) >= 1) {
                    //myViewHolder.valor.setText(String.valueOf(valorDefault * novo));
                    total = total - valorDefault;
                    ((ListaProdutosActivity) context).updateTextTotal("Total: " + String.valueOf(total) + " pontos");
                } else {
                    myViewHolder.ll_status.setBackgroundColor(context.getColor(R.color.nao_selecionado));

                    myViewHolder.tvQuantidade.setVisibility(View.GONE);
                    myViewHolder.btnMinus.setVisibility(View.GONE);
                    myViewHolder.btnAdd.setVisibility(View.GONE);

                    listaProdutos.get(position).setQuantidade("1");
                    listaProdutos.get(position).setStatus("0");

                    carrinho.remove(listaProdutos.get(position).getProduto());

                    String carrinhoString = carrinho.toString().replace("[", "").replace("]", "").trim();

                    if (carrinhoString.equals("")) {
                        ((ListaProdutosActivity) context).updateTextListaProdutos("Nenhum item selecionado");
                    } else {
                        ((ListaProdutosActivity) context).updateTextListaProdutos(carrinhoString);
                    }

                    total = total - valorDefault;
                    ((ListaProdutosActivity) context).updateTextTotal("Total: " + String.valueOf(total) + " pontos");
                }
            }
        });


        if (produto.getStatus().equals("0")) {
            myViewHolder.ll_status.setBackgroundColor(context.getColor(R.color.nao_selecionado));

            myViewHolder.tvQuantidade.setVisibility(View.GONE);
            myViewHolder.btnMinus.setVisibility(View.GONE);
            myViewHolder.btnAdd.setVisibility(View.GONE);

            listaProdutos.get(i).setQuantidade("1");


            //myViewHolder.tvQuantidade.setText("1");
        } else {
            myViewHolder.ll_status.setBackgroundColor(context.getColor(R.color.selecionado));

            myViewHolder.tvQuantidade.setVisibility(View.VISIBLE);
            myViewHolder.btnMinus.setVisibility(View.VISIBLE);
            myViewHolder.btnAdd.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return listaProdutos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView produto;
        TextView valor;
        TextView tvQuantidade;
        ConstraintLayout ll_status;
        ImageButton btnAdd, btnMinus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            produto = itemView.findViewById(R.id.tvProduto);
            valor = itemView.findViewById(R.id.tvValor);
            ll_status = itemView.findViewById(R.id.ll_status);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            tvQuantidade = itemView.findViewById(R.id.tvQuantidade);
            btnMinus = itemView.findViewById(R.id.btnMinus);

            ll_status.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Produto produto = listaProdutos.get(position);

            itemClickListener.onItemClick(position, produto, tvQuantidade.getText().toString());
        }
    }


}