package com.picolab.view;

import android.app.AutomaticZenRule;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.picolab.R;
import com.picolab.application.Controller.CanvasController;
import com.picolab.domain.CanvasImage;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class showImage extends AppCompatActivity {

    TextView count;
    ImageView image;
    Button showImageButton;

    CanvasController canvasController;

    private RequestQueue queue;
    protected static CanvasImage canvasImage = new CanvasImage();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        count = (TextView)findViewById(R.id.counterText);
        image = (ImageView)findViewById(R.id.imageOriView);
        showImageButton = (Button)findViewById(R.id.showImageButton);

        queue = Volley.newRequestQueue(this);

        showImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        showImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               canvasController.obtenerDatosGet(showImage.this,image);

            }
        });
    }
    public void startCountDown(){
        new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                long l = millisUntilFinished / 1000;
                if(l<=3){count.setTextColor(Color.RED);}
                count.setText(Long.toString(l));
            }

            public void onFinish() {
                //Toast.makeText(showImage.this,"Cuenta finalizada", Toast.LENGTH_SHORT).show();
                Intent paintImage = new Intent(showImage.this, com.picolab.view.paintImage.class);
                paintImage.putExtra("url",canvasImage.getUrl());
                startActivity(paintImage);
                finishAffinity();
            }
        }.start();
    }
}
