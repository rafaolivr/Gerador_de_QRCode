package com.example.qrcode.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrcode.R;
import com.example.qrcode.adapter.AdapterProdutos;
import com.example.qrcode.model.Produto;
import com.example.qrcode.model.QRCode;
import com.example.qrcode.model.Teste;

import java.util.ArrayList;
import java.util.List;

public class ListaProdutosActivity extends AppCompatActivity {

    private RecyclerView rvProdutos;
    private List<Produto> listaProdutos = new ArrayList<>();
    private TextView tvListaProdutos;
    private TextView tvTotal;
    private Button btnGerarQRCode;
    private ImageView ivQRCode;

    public static int total = 0;

    QRCode qrCode = new QRCode();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_produtos);

        //Configura toolbar
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("");
        setSupportActionBar( toolbar );

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_green_24dp);

        total = 0;
        //
        inicializarVariaveis();

        //Lista de Produtos
        this.criarProdutos();

        //Configura o Adapter
        final AdapterProdutos adapterProdutos = new AdapterProdutos(listaProdutos, ListaProdutosActivity.this);

        //Configura o RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rvProdutos.setLayoutManager(layoutManager);
        rvProdutos.setHasFixedSize(true);
        rvProdutos.setAdapter(adapterProdutos);

        adapterProdutos.setOnItemClickListener(new AdapterProdutos.ItemClickListener() {
            @Override
            public void onItemClick(int position, Produto produto, String quantidade) {
                adapterProdutos.atualizarStatus(position);

                int valor = Integer.parseInt(produto.getValor());

                if (produto.getStatus().equals("1")){
                    adapterProdutos.getCarrinho().add(produto.getProduto());
                    total = total + valor;
                } else {
                    adapterProdutos.getCarrinho().remove(produto.getProduto());
                    valor = valor * (Integer.parseInt(quantidade));
                    total = total - valor;
                }

                Log.d("Abacaxi", quantidade);

                String carrinhoString = adapterProdutos.getCarrinho().toString().replace("[", "").replace("]", "").trim();

                if(carrinhoString.equals("")){
                    tvListaProdutos.setText("Nenhum item selecionado");
                }
                else {
                    tvListaProdutos.setText(carrinhoString);
                }
                tvTotal.setText("Total: " + String.valueOf(total) + " pontos");
                qrCode.setDesconto(String.valueOf(total));
            }
        });

        btnGerarQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Log.d("total", String.valueOf(total));

                if(total > 0){
                    Intent intent = new Intent(getApplicationContext(), QRCodeActivity.class);

                    intent.putExtra("id", qrCode.getId());
                    intent.putExtra("desconto", qrCode.getDesconto());

                    startActivity(intent);
                    finish();

                }
                else{
                    Toast.makeText(ListaProdutosActivity.this, "Valor total incorreto, verifique e tente novamente.", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public void inicializarVariaveis(){
        rvProdutos = findViewById(R.id.rvProdutos);
        tvListaProdutos = findViewById(R.id.tvListaProdutos);
        tvTotal = findViewById(R.id.tvTotal);
        btnGerarQRCode = findViewById(R.id.btnGerarQRCode);
    }

    private void criarProdutos(){
        Produto produto = new Produto("Produto 1", "50");
        this.listaProdutos.add(produto);

        produto = new Produto("Produto 2", "100");
        this.listaProdutos.add(produto);

        produto = new Produto("Produto 3", "30");
        this.listaProdutos.add(produto);

        produto = new Produto("Produto 4", "20");
        this.listaProdutos.add(produto);

        produto = new Produto("Produto 5", "10");
        this.listaProdutos.add(produto);

        produto = new Produto("Produto 6", "15");
        this.listaProdutos.add(produto);

        produto = new Produto("Produto 7", "100");
        this.listaProdutos.add(produto);

        produto = new Produto("Produto 8", "100");
        this.listaProdutos.add(produto);

        produto = new Produto("Produto 9", "100");
        this.listaProdutos.add(produto);

        produto = new Produto("Produto 10", "100");
        this.listaProdutos.add(produto);
    }

    public void updateTextListaProdutos(String text){
        tvListaProdutos.setText(text);
    }

    public void updateTextTotal(String text){
        tvTotal.setText(text);
    }

    @Override
    public boolean onSupportNavigateUp() {

        finish();
        return false;

    }
}
