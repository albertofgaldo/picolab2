package com.picolab.application.Controller;
import com.picolab.utils.MyListener;
import com.picolab.utils.ShowMessage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.picolab.R;
import com.picolab.domain.CanvasImage;
import com.picolab.utils.UrlNullException;
import com.picolab.view.showImage;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class CanvasController {

    ShowMessage showMessage;
    private CanvasImage canvasImage;
    MyListener ml;

    public CanvasController(MyListener ml){
        this.ml=ml;
    }

    //PIDO LOS DATOS MEDIANTE GET
    public CanvasImage obtenerDatosGet(final Context context, final ImageView image){

        String url = "https://colaborativepicture.herokuapp.com/canvas/user";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                JSONObject JsonCanvas = response;
                //JsonCanvas = response;
                try {
                    //int id = Integer.parseInt(JsonCanvas.get("id").toString());
                   //String urlCanvas = JsonCanvas.get("url").toString();
                    //canvasImage2.setId(id);
                    //canvasImage.setUrl(urlCanvas);
                    CanvasImage canvasImage = new CanvasImage(JsonCanvas.getInt("id"), JsonCanvas.getString("url"));
                    loadImageFromUrl(context, canvasImage.getUrl(), image);

                } catch (JSONException e) {
                    showMessage.toast(context,"Error en el JSON");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showMessage.toast(context,"Sin respuesta del Servidor");
            }
        });
        //queue.add(request);
        return canvasImage;
    }

    private void loadImageFromUrl(final Context context, String url, final ImageView image){
                Picasso.with(context).load(url).resize(850,850).placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(image, new com.squareup.picasso.Callback() {

                    @Override
                    public void onSuccess() {

                        ml.callback("loadImageSucced");
                    }

                    @Override
                    public void onError() {
                        showMessage.toast(context,"No hay imagen para cargar");
                        ml.callback("loadImageError");
                    }
                });

    }

}
