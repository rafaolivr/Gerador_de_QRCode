package com.example.qrcode.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.ListPreloader;
import com.example.qrcode.R;
import com.example.qrcode.helper.ConfiguracaoFirebase;
import com.example.qrcode.helper.VisitanteFirebase;
import com.example.qrcode.model.Visitante;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditarPerfilActivity extends AppCompatActivity {

    private EditText etAlterarNome, etAlterarEmpresa, etAlterarEmail;
    private Button btnSalvarAlteracoes;
    private DatabaseReference mDatabase;
    private Visitante visitanteLogado;
    private static FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        //Configura toolbar
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_green_24dp);

        visitanteLogado = VisitanteFirebase.getDadosVisitanteLogado();

        inicializar();

        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();

        String uId = firebaseUser.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("visitantes").child(uId).child("nome").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String nome = dataSnapshot.getValue(String.class);
                etAlterarNome.setText(nome);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabase.child("visitantes").child(uId).child("empresa").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String empresa = dataSnapshot.getValue(String.class);
                etAlterarEmpresa.setText(empresa);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabase.child("visitantes").child(uId).child("email").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String email = dataSnapshot.getValue(String.class);
                etAlterarEmail.setText(email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnSalvarAlteracoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nomeAtualizado = etAlterarNome.getText().toString();
                String empresaAtualizada = etAlterarEmpresa.getText().toString();

                if (!nomeAtualizado.isEmpty()) {
                    if (!empresaAtualizada.isEmpty()) {
                        //Atualizar infos do visitante no perfil
                        VisitanteFirebase.atualizarNomeUsuario(nomeAtualizado, empresaAtualizada);

                        //Atualizar infos do visitante no banco de dados
                        visitanteLogado.setNome(nomeAtualizado);
                        visitanteLogado.setEmpresa(empresaAtualizada);
                        visitanteLogado.atualizar();

                        Toast.makeText(EditarPerfilActivity.this, "Dados Alterados com sucesso!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(EditarPerfilActivity.this, "Preencha a empresa", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditarPerfilActivity.this, "Preencha o nome", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void inicializar() {

        etAlterarNome = findViewById(R.id.etAlterarNome);
        etAlterarEmail = findViewById(R.id.etAlterarEmail);
        etAlterarEmpresa = findViewById(R.id.etAlterarEmpresa);
        etAlterarEmail.setFocusable(false);


        btnSalvarAlteracoes = findViewById(R.id.btnSalvarAlteracoes);

    }

    @Override
    public boolean onSupportNavigateUp() {

        finish();
        return false;

    }

}
