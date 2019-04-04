package com.example.qrcode.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.qrcode.R;
import com.example.qrcode.helper.ConfiguracaoFirebase;
import com.example.qrcode.view.ar.ARActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.google.firebase.auth.FirebaseAuth.getInstance;

public class TelaInicialLogadoActivity extends AppCompatActivity {

    private Button btnComprar, btnEncontrarLogado, btnTutorialLogado, btnEditarPerfil;
    private TextView tvNome, tvEmpresa, tvPhygits;
    private DatabaseReference mDatabase;
    private static FirebaseAuth firebaseAuth;
    private String id = "1";
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial_logado);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        //Configura toolbar
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("");
        setSupportActionBar( toolbar );

        tvNome = findViewById(R.id.tvNome);
        tvEmpresa = findViewById(R.id.tvEmpresa);
        tvPhygits = findViewById(R.id.tvPhygits);

        btnComprar = findViewById(R.id.btnComprar);
        btnEncontrarLogado = findViewById(R.id.btnEncontrarLogado);
        btnTutorialLogado = findViewById(R.id.btnTutorialLogado);
        //btnEditarPerfil = findViewById(R.id.btnEditarPerfil);

        btnComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListaProdutosActivity.class);
                startActivity(intent);
            }
        });

        btnEncontrarLogado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ARActivity.class);
                startActivity(intent);
            }
        });

        btnTutorialLogado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TutorialActivity.class);
                startActivity(intent);
            }
        });

        FirebaseUser firebaseUser =  firebaseAuth.getInstance().getCurrentUser();

        String uId = firebaseUser.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("visitantes").child(uId).child("nome").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String nome = dataSnapshot.getValue(String.class);
                tvNome.setText(nome);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabase.child("visitantes").child(uId).child("empresa").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String empresa = dataSnapshot.getValue(String.class);
                tvEmpresa.setText(empresa);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabase.child("clientes").child(id).child("saldo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String phygits = dataSnapshot.getValue(String.class);
                tvPhygits.setText(phygits);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), TelaInicialActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_sair :
                deslogarUsuario();
                finish();
                //startActivity(new Intent(getApplicationContext(), TelaInicialActivity.class));
                break;

            case R.id.menu_edit :
                Intent intent = new Intent(getApplicationContext(), EditarPerfilActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deslogarUsuario(){
        try{
            autenticacao.signOut();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}

