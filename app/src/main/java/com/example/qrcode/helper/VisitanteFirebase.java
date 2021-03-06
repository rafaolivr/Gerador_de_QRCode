package com.example.qrcode.helper;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.qrcode.model.Visitante;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class VisitanteFirebase {

    public static FirebaseUser getVisitanteAtual(){

        FirebaseAuth visitante = ConfiguracaoFirebase.getFirebaseAutenticacao();
        return visitante.getCurrentUser();
    }

    public static void atualizarNomeUsuario(String nome, String empresa){
        try{

            //Usuario logado no App
            FirebaseUser visitanteLogado = getVisitanteAtual();

            //Configurar objeto para alteração do perfil
            UserProfileChangeRequest profile  = new UserProfileChangeRequest
                    .Builder()
                    .setDisplayName(nome)
                    .build();

            visitanteLogado.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()){
                        Log.d("Perfil", "Erro ao atualizar nome de Perfil");
                    } else {

                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Visitante getDadosVisitanteLogado(){

        FirebaseUser firebaseUser = getVisitanteAtual();

        Visitante visitante = new Visitante();
        visitante.setEmail(firebaseUser.getEmail());
        visitante.setNome(firebaseUser.getDisplayName());
        visitante.setId(firebaseUser.getUid());

        return visitante;
    }
}
