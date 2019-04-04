package com.example.qrcode.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.example.qrcode.R;
import com.example.qrcode.model.QRCode;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.Base64;

public class QRCodeActivity extends AppCompatActivity {

    private ImageView ivQRCode;
    public static final int ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        //Configura toolbar
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("");
        setSupportActionBar( toolbar );

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_green_24dp);

        ivQRCode = findViewById(R.id.ivQRCode);


        gerarQRCode();
    }

    public void gerarQRCode(){

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        Intent intent = getIntent();
        QRCode model = new QRCode();

        model.setId(intent.getStringExtra("id"));
        model.setDesconto(intent.getStringExtra("desconto"));

        Gson gson = new Gson();

        String json = gson.toJson(model);
        String encoded = Base64.getEncoder().encodeToString(json.getBytes());

        try{

            BitMatrix bitMatrix = multiFormatWriter.encode(encoded, BarcodeFormat.QR_CODE, 600, 600);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            ivQRCode.setImageBitmap(bitmap);

        }catch (WriterException e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {

        finish();
        return false;

    }

}
