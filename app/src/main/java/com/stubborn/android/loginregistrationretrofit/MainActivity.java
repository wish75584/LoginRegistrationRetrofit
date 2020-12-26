package com.stubborn.android.loginregistrationretrofit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText uid, uname;
    ImageView img;
    Button send, selectimg;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    String s_uid;
    String s_uname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uid = (EditText) findViewById(R.id.uid);
        uname = (EditText) findViewById(R.id.uname);
        img = (ImageView) findViewById(R.id.img);
        send = (Button) findViewById(R.id.send);
        selectimg = (Button) findViewById(R.id.select_img);

        selectimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s_uid = uid.getText().toString();
                s_uname = uname.getText().toString();

                openFileChooser();
            }
        });

    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            try {

                InputStream is = getContentResolver().openInputStream(data.getData());

                uploadImage(getBytes(is), s_uid, s_uname);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();

        int buffSize = 1024;
        byte[] buff = new byte[buffSize];

        int len = 0;
        while ((len = is.read(buff)) != -1) {
            byteBuff.write(buff, 0, len);
        }

        return byteBuff.toByteArray();
    }


    private void uploadImage(byte[] imageBytes, String personId, String personName) {


        ApiInterface apiInterface = ApiClient.getAppClient().create(ApiInterface.class);


        RequestBody userId = RequestBody.create(MediaType.parse("multipart/form-data"), personId);
        RequestBody fullName = RequestBody.create(MediaType.parse("multipart/form-data"), personName);


        File file = new File(String.valueOf(imageBytes));
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);

        MultipartBody.Part profilePic = MultipartBody.Part.createFormData("user_image", file.getName(), requestFile);

        Call<MyResponse> call = apiInterface.uploadImage(profilePic, userId, fullName);

        call.enqueue(new Callback<MyResponse>() {
            private Call<Response> call;
            private Throwable t;

            @Override
            public void onResponse(Call<MyResponse> call, retrofit2.Response<MyResponse> response) {


                if (response.isSuccessful()) {

                    MyResponse responseBody = response.body();
                    Toast.makeText(MainActivity.this, response.body().getMessage() + "", Toast.LENGTH_SHORT).show();
                } else {

                    ResponseBody errorBody = response.errorBody();

                    Gson gson = new Gson();

                    try {

                        Response errorResponse = gson.fromJson(errorBody.string(), Response.class);
                        Toast.makeText(MainActivity.this, response.body().getMessage() + "", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}