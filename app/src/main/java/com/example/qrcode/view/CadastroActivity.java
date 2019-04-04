package com.example.qrcode.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.qrcode.R;
import com.example.qrcode.api.BioAPI;
import com.example.qrcode.helper.ConfiguracaoFirebase;
import com.example.qrcode.helper.VisitanteFirebase;
import com.example.qrcode.model.RetornoGET;
import com.example.qrcode.model.Visitante;
import com.example.qrcode.model.RetornoPOST;
import com.example.qrcode.model.VisitanteAPI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.qrcode.util.RetrofitInitializer.getRetrofitInstance;

public class CadastroActivity extends AppCompatActivity {

    private EditText etCadastroNome, etCadastroEmpresa, etCadastroEmail, etCadastroSenha;
    private Button btnCadastrar;
    private ProgressBar pbCadastrar;

    private Visitante visitante;

    private FirebaseAuth autenticacao;
    private FirebaseStorage storage;
    private StorageReference reference;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    //CAMERA
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    private Button btnTirarFoto;
    private ImageView iv_foto;
    private String mCurrentPhotoPath;
    private Uri fotoURI;
    private String nomeFoto = "";
    private byte[] dadosImagem = null;

    //VERIFICAR ID
    private BioAPI bioAPI;
    private String img;
    public static OkHttpClient.Builder httpClient =  new OkHttpClient.Builder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        //BioAPI
        storage = FirebaseStorage.getInstance();
        reference = storage.getReference();

        bioAPI = getRetrofitInstance().create(BioAPI.class);

        //método POST
        //postAPI();

        //Converte foto para base 64
        /*Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.foto_teste4);
        img = codeImg(bm);*/

        //Inicialiar componentes
        inicializar();

        //Configura toolbar
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_green_24dp);

        //Cadastro do usuário
        pbCadastrar.setVisibility(View.GONE);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = etCadastroNome.getText().toString();
                String empresa = etCadastroEmpresa.getText().toString();
                String email = etCadastroEmail.getText().toString();
                String senha = etCadastroSenha.getText().toString();
                String photopath = nomeFoto;

                if (!nome.isEmpty()) {
                    if (!empresa.isEmpty()) {
                        if (!email.isEmpty()) {
                            if (!senha.isEmpty()) {
                                if (!photopath.isEmpty()) {

                                    visitante = new Visitante();

                                    visitante.setNome(nome);
                                    visitante.setEmpresa(empresa);
                                    visitante.setEmail(email);
                                    visitante.setSenha(senha);
                                    visitante.setPhotopath(photopath);
                                    visitante.setPhotopath(nomeFoto);

                                    cadastrar(visitante);
                                    salvarFirebase();

                                } else {
                                    Toast.makeText(CadastroActivity.this, "É necessário tirar foto para se cadastrar", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(CadastroActivity.this, "Preencha a senha!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(CadastroActivity.this, "Preencha o e-mail!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(CadastroActivity.this, "Preencha a empresa!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CadastroActivity.this, "Preencha o nome", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Tirar foto do visitante
        btnTirarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tirarFotoIntent();
            }
        });

    }

    private void cadastrar(final Visitante visitante) {

        pbCadastrar.setVisibility(View.VISIBLE);
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                visitante.getEmail(),
                visitante.getSenha()
        ).addOnCompleteListener(
                this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            try {

                                pbCadastrar.setVisibility(View.GONE);

                                //Salvar dados do visitante no firebase
                                String idVisitante = task.getResult().getUser().getUid();
                                visitante.setId(idVisitante);
                                visitante.salvar();

                                //Salvar dados no profile do Firebase
                                VisitanteFirebase.atualizarNomeUsuario(visitante.getNome(), visitante.getEmpresa());

                                Toast.makeText(CadastroActivity.this, "Cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), TelaInicialLogadoActivity.class));
                                finish();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {

                            pbCadastrar.setVisibility(View.GONE);

                            String erroExecucao = "";
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                erroExecucao = "Digite uma senha mais forte";
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                erroExecucao = "Por favor, digite um e-mail válido";
                            } catch (FirebaseAuthUserCollisionException e) {
                                erroExecucao = "Este conta já foi cadastra";
                            } catch (Exception e) {
                                erroExecucao = "Erro ao cadastrar usuário: " + e.getMessage();
                                e.printStackTrace();
                            }

                            Toast.makeText(CadastroActivity.this, "Erro: " + erroExecucao, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            Toast.makeText(this, "Foto registrada com sucesso!", Toast.LENGTH_SHORT).show();
        }
    }

    private void salvarFirebase() {
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
        ByteArrayOutputStream bous = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 45, bous);
        dadosImagem = bous.toByteArray();
        final StorageReference imageRef = reference.child(nomeFoto);

        final UploadTask uploadTask = imageRef.putBytes(dadosImagem);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(CadastroActivity.this, "erro ao salvar imagem", Toast.LENGTH_SHORT).show();
                apagarFoto(mCurrentPhotoPath);

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(CadastroActivity.this, "Imagem Salva", Toast.LENGTH_SHORT).show();
                apagarFoto(mCurrentPhotoPath);
            }
        });
    }

    private void tirarFotoIntent() {
        Intent intentFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intentFoto.resolveActivity(getPackageManager()) != null) {

            File fotoFile = null;
            try {
                fotoFile = criarImagemArquivo();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (fotoFile != null) {
                fotoURI = FileProvider.getUriForFile(
                        this,
                        "com.example.qrcode",
                        fotoFile
                );
                intentFoto.putExtra(MediaStore.EXTRA_OUTPUT, fotoURI);
                startActivityForResult(intentFoto, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void apagarFoto(String caminhoFoto) {
        boolean fotoDeletada = new File(caminhoFoto).delete();
        if (fotoDeletada) {
            Log.d("APAGAR", "Foto apagada");
        } else {
            Log.d("APAGAR", "Erro ao apagar foto");
        }
    }

    private File criarImagemArquivo() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageNameFile = "JPG_" + timeStamp + "_";
        nomeFoto = imageNameFile + ".jpg";

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageNameFile,
                ".jpg",
                storageDir
        );

        mCurrentPhotoPath = image.getAbsolutePath();

        return image;
    }

    public String codeImg(Bitmap nomeFoto) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        nomeFoto.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b1 = baos.toByteArray();

        return android.util.Base64.encodeToString(b1, android.util.Base64.DEFAULT);
    }

    public void getAPI() {
        //GET API
        Call<RetornoGET> call = bioAPI.verificarId("{\"token\": \"17EA147C-5755-11E7-988F-E4FD2F5B434E\"}");

        call.enqueue(new Callback<RetornoGET>() {
            @Override
            public void onResponse(Call<RetornoGET> call, Response<RetornoGET> response) {

                Log.d("Call1", "Response: " + response.body().getMessage());
            }

            @Override
            public void onFailure(Call<RetornoGET> call, Throwable t) {
                Log.d("Call1", "Failure: " + String.valueOf(t));
            }
        });
    }

    public void postAPI() {

        VisitanteAPI visitanteAPI = new VisitanteAPI("Testando1", "testando1@testando1.com", img);

        Call<RetornoPOST> call = bioAPI.criarVisitante(visitanteAPI, "{\"token\": \"17EA147C-5755-11E7-988F-E4FD2F5B434E\"}");
        call.enqueue(new Callback<RetornoPOST>() {
            @Override
            public void onResponse(Call<RetornoPOST> call, Response<RetornoPOST> response) {
                Log.d("call2", "onResponse: " + response.toString());
            }

            @Override
            public void onFailure(Call<RetornoPOST> call, Throwable t) {
                Log.d("call2", "onFailure: " + t.getMessage());
            }
        });
    }

    private void inicializar() {

        etCadastroNome = findViewById(R.id.etAlterarNome);
        etCadastroEmpresa = findViewById(R.id.etAlterarEmpresa);
        etCadastroEmail = findViewById(R.id.etAlterarEmail);
        etCadastroSenha = findViewById(R.id.etCadastroSenha);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        pbCadastrar = findViewById(R.id.pbSalvarAlteracoes);

        etCadastroNome.requestFocus();

        btnTirarFoto = findViewById(R.id.btnSalvarAlteracoes);

    }

    @Override
    public boolean onSupportNavigateUp() {

        finish();
        return false;

    }


}
