package com.picolab.view;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.picolab.R;
import com.rm.freedrawview.FreeDrawView;
import com.rm.freedrawview.PathDrawnListener;
import com.rm.freedrawview.PathRedoUndoCountChangeListener;
import com.rm.freedrawview.ResizeBehaviour;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.picolab.view.showImage.canvasImage;
import static java.lang.Thread.sleep;

public class paintImage extends AppCompatActivity {

    FreeDrawView mSignatureView;
    Button btn_save;
    ImageView image;
    TextView whiteButton, redButton, blueButton, yellowButton, greenButton, blackButton, orangeButton, pinkButton, purpleButton, skyButton, clearButton, grayButton, textViewFino, textViewNormal, textViewGrueso;
    private RequestQueue queue;
    private Uri filePath;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paintimage);

        url = getIntent().getExtras().getString("url");
        image = (ImageView)findViewById(R.id.imageOriView);
        whiteButton = (TextView)findViewById(R.id.colorWhiteButton);
        redButton = (TextView)findViewById(R.id.colorRedButton);
        blueButton = (TextView)findViewById(R.id.colorBlueButton);
        yellowButton = (TextView)findViewById(R.id.colorYellowButton);
        greenButton = (TextView)findViewById(R.id.colorGreenButton);
        blackButton = (TextView)findViewById(R.id.colorBlackButton);
        orangeButton = (TextView)findViewById(R.id.colorOrangeButton);
        pinkButton = (TextView)findViewById(R.id.colorPinkButton);
        purpleButton = (TextView)findViewById(R.id.colorPurpleButton);
        skyButton = (TextView)findViewById(R.id.colorSkyButton);
        grayButton = (TextView)findViewById(R.id.colorGrayButton);
        clearButton = (TextView)findViewById(R.id.clearButton);
        textViewFino = (TextView)findViewById(R.id.textViewFino);
        textViewNormal = (TextView)findViewById(R.id.textViewNormal);
        textViewGrueso = (TextView)findViewById(R.id.textViewGrueso);
        btn_save = (Button) findViewById(R.id.btn_save);
        mSignatureView = (FreeDrawView) findViewById(R.id.parentView);


        mSignatureView.setPaintWidthPx(12);
        //mSignatureView.setPaintWidthPx(12);
        mSignatureView.setPaintWidthDp(12);
        //mSignatureView.setPaintWidthDp(6);
        mSignatureView.setPaintAlpha(255);// from 0 to 255
        mSignatureView.setResizeBehaviour(ResizeBehaviour.CROP);// Must be one of ResizeBehaviour
        // values;
        // This listener will be notified every time the path done and undone count changes
        mSignatureView.setPathRedoUndoCountChangeListener(new PathRedoUndoCountChangeListener() {
            @Override
            public void onUndoCountChanged(int undoCount) {
                // The undoCount is the number of the paths that can be undone
            }

            @Override
            public void onRedoCountChanged(int redoCount) {
                // The redoCount is the number of path removed that can be redrawn
            }
        });
        // This listener will be notified every time a new path has been drawn
        mSignatureView.setOnPathDrawnListener(new PathDrawnListener() {
            @Override
            public void onNewPathDrawn() {
                // The user has finished drawing a path
            }

            @Override
            public void onPathStart() {
                // The user has started drawing a path
            }
        });
        // This will take a screenshot of the current drawn content of the view
        mSignatureView.getDrawScreenshot(new FreeDrawView.DrawCreatorListener() {
            @Override
            public void onDrawCreated(Bitmap draw) {
                // The draw Bitmap is the drawn content of the View
            }

            @Override
            public void onDrawCreationError() {
                // Something went wrong creating the bitmap, should never
                // happen unless the async task has been canceled
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignatureView.getDrawScreenshot(new FreeDrawView.DrawCreatorListener() {
                    @Override
                    public void onDrawCreated(Bitmap draw) {
                        //TODO:Aqui ya puedes coger el Bitmap y hacer lo que quieras
                        uploadImage(draw);
                        //showToast("Tu dibujo se ha enviado");
                        btn_save.setEnabled(false);
                    }
                    @Override
                    public void onDrawCreationError() {
                        //TODO:Muestra error!!
                    }
                });
            }
        });

        whiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignatureView.setPaintColor(Color.WHITE);
            }
        });
        redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignatureView.setPaintColor(Color.RED);
            }
        });
        blueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignatureView.setPaintColor(Color.BLUE);
            }
        });
        yellowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignatureView.setPaintColor(Color.YELLOW);
            }
        });
        blackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignatureView.setPaintColor(Color.BLACK);
            }
        });
        greenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignatureView.setPaintColor(Color.GREEN);
            }
        });
        orangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignatureView.setPaintColor(Color.parseColor("#E49817"));
            }
        });
        pinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignatureView.setPaintColor(Color.parseColor("#EB17BC"));
            }
        });
        purpleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignatureView.setPaintColor(Color.parseColor("#7817D7"));
            }
        });
        skyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignatureView.setPaintColor(Color.parseColor("#17DCEB"));
            }
        });
        grayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignatureView.setPaintColor(Color.parseColor("#C4C4C4"));
            }
        });
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignatureView.clearDraw();
                mSignatureView.setPaintColor(Color.BLACK);
            }
        });
        textViewFino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewFino.setBackgroundColor(Color.BLACK);
                textViewFino.setTextColor(Color.WHITE);
                textViewNormal.setBackgroundColor(Color.WHITE);
                textViewNormal.setTextColor(Color.BLACK);
                textViewGrueso.setBackgroundColor(Color.WHITE);
                textViewGrueso.setTextColor(Color.BLACK);
                mSignatureView.setPaintWidthPx(12);
                mSignatureView.setPaintWidthDp(12);
            }
        });
        textViewNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewNormal.setBackgroundColor(Color.BLACK);
                textViewNormal.setTextColor(Color.WHITE);
                textViewGrueso.setBackgroundColor(Color.WHITE);
                textViewGrueso.setTextColor(Color.BLACK);
                textViewFino.setBackgroundColor(Color.WHITE);
                textViewFino.setTextColor(Color.BLACK);
                mSignatureView.setPaintWidthPx(25);
                mSignatureView.setPaintWidthDp(25);
            }
        });
        textViewGrueso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewGrueso.setBackgroundColor(Color.BLACK);
                textViewGrueso.setTextColor(Color.WHITE);
                textViewNormal.setBackgroundColor(Color.WHITE);
                textViewNormal.setTextColor(Color.BLACK);
                textViewFino.setBackgroundColor(Color.WHITE);
                textViewFino.setTextColor(Color.BLACK);
                mSignatureView.setPaintWidthPx(50);
                mSignatureView.setPaintWidthDp(50);
            }
        });
        loadImageFromUrl(url);
    }

    private void uploadImage(Bitmap draw) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        draw.compress(Bitmap.CompressFormat.JPEG,100, baos);
        byte[] data = baos.toByteArray();
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // Create a storage reference from our app
        StorageReference storageRef = storage.getReferenceFromUrl("gs://picolab-c4b9b.appspot.com");

        // Create a reference to "mountains.jpg"
        final StorageReference mountainsRef = storageRef.child(canvasImage.getId() + ".jpg");
        final StorageReference ref = null;
        final UploadTask uploadTask = mountainsRef.putBytes(data);


        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return mountainsRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    //System.out.println("Upload " + downloadUri);
                    showToast("Tu dibujo se ha enviado");
                    // Toast.makeText(mActivity, "Successfully uploaded", Toast.LENGTH_SHORT).show();
                    if (downloadUri != null) {

                        canvasImage.setUrl(downloadUri.toString()); //YOU WILL GET THE DOWNLOAD URL HERE !!!!
                        devolverDatosPut();

                    }

                } else {
                    // Handle failures
                    // ...
                }
            }
        });

    }

    private void devolverDatosPut() {

        //poner break point aquí
        String url = "https://colaborativepicture.herokuapp.com/canvas/user";

        RequestQueue queue = Volley.newRequestQueue(this);

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", canvasImage.getId());
            jsonObject.put("url", canvasImage.getUrl());
        }
        catch (JSONException e) {
        }
        // handle exception        }

        JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // response
                        // Log.d("Response", response.toString());
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        // Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders()
            {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
            @Override
            public byte[] getBody() {

                try {
                    // Log.i("json", jsonObject.toString());
                    return jsonObject.toString().getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        queue.add(putRequest);
        }


    private void loadImageFromUrl(String url){
        Picasso.with(paintImage.this).load(url).placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(image, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        //findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        showToast("No hay imagen para cargar");
                    }
                });
    }


    public void showToast(String text){
        Toast toast = new Toast(paintImage.this);
        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.makeText(paintImage.this, text, Toast.LENGTH_SHORT).show();
    }

    public JsonObject parseCanvas(){

        JsonObject obj = new JsonObject();
        obj.addProperty("id",canvasImage.getId());
        obj.addProperty("url",canvasImage.getUrl());

        return obj;
    }


}