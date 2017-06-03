package com.cpsdbd.sohelcon;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.nguyenhoanglam.imagepicker.activity.ImagePicker;
import com.nguyenhoanglam.imagepicker.activity.ImagePickerActivity;
import com.nguyenhoanglam.imagepicker.model.Image;

import com.cpsdbd.sohelcon.BaseClass.User;
import com.cpsdbd.sohelcon.Response.RegisterResponse;
import com.cpsdbd.sohelcon.Utility.URLs;
import com.cpsdbd.sohelcon.Utility.UserLocalStore;
import com.cpsdbd.sohelcon.Volley.MyPostVolley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class UploadPhotoActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView picHolder;

    private Button btnBrowse,btnUpload;

    private static final int REQUEST_CODE_PICKER=100;

    private UserLocalStore userLocalStore;
    private Gson gson;

    private int user_id;

    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);

        userLocalStore = new UserLocalStore(this);

        user_id = userLocalStore.getLoggedInUser().getUser_id();
        gson = new Gson();

        Log.d("USERID",user_id+"");

        initView();
    }


    private void initView() {
        picHolder = (ImageView) findViewById(R.id.imageContainer);

        btnBrowse = (Button)findViewById(R.id.btn_browse);
        btnUpload = (Button) findViewById(R.id.btn_upload);

        btnBrowse.setOnClickListener(this);
        btnUpload.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_browse:

                ImagePicker.create(this)
                        .folderMode(true) // folder mode (false by default)
                        .folderTitle("Folder") // folder selection title
                        .imageTitle("Tap to select") // image selection title
                        .single() // single mode

                        .showCamera(true)
                        .imageDirectory("Camera")
                        .start(REQUEST_CODE_PICKER);

                break;


            case R.id.btn_upload:

                Bitmap bitmap = ((BitmapDrawable)picHolder.getDrawable()).getBitmap();


                // Bitmap bitmap=MyUtils.decodeSampledBitmapFromFile(path,100,100);
                if(bitmap!= null){
                    String image =getStringImage(bitmap);
                    uploadImage(user_id,image);
                }





                break;
        }
    }

   /* public void openGallery() {
        startActivityForResult(new GalleryHelper()
                .setMultiselection(false)
                .getCallingIntent(this), REQUEST_CODE_GALLERY);
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICKER && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES);
            // do your logic ....
            Image image = images.get(0);

            String path = image.getPath();

            Uri uri = Uri.fromFile(new File(path));

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                Bitmap resized = Bitmap.createScaledBitmap(bitmap,(int)(bitmap.getWidth()*0.2), (int)(bitmap.getHeight()*0.2), true);
                picHolder.setImageBitmap(resized);
            } catch (IOException e) {
                e.printStackTrace();
            }





        }

        /*if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK) {
            List<GalleryMedia> galleryMedias =
                    data.getParcelableArrayListExtra(GalleryActivity.RESULT_GALLERY_MEDIA_LIST);






            Uri file = Uri.fromFile(new File(galleryMedias.get(0).mediaUri()));

            path = file.getPath();


            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), file);
                Log.d("MYTEST",bitmap.getWidth()+"");
                Log.d("MYTEST",bitmap.getHeight()+"");
                Bitmap resized = Bitmap.createScaledBitmap(bitmap,(int)(bitmap.getWidth()*0.2), (int)(bitmap.getHeight()*0.2), true);
                picHolder.setImageBitmap(resized);
            } catch (IOException e) {
                e.printStackTrace();
            }



        }*/
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(int user_id,String image){

        Map<String,String> map = new HashMap<>();
        map.put("user_id",String.valueOf(user_id));
        map.put("image",String.valueOf(image));


        MyPostVolley myPostVolley = new MyPostVolley(this,map, URLs.UPLOAD_IMAGE,"Please Wait While Uploading Image");
        myPostVolley.applyPostVolley();
        myPostVolley.setVolleyPostListener(new MyPostVolley.VolleyPostListener() {
            @Override
            public void getResposefromVolley(String response) {
                RegisterResponse registerResponse = gson.fromJson(response,RegisterResponse.class);

                Log.d("Response",response);

                if(registerResponse.getSuccess()==1){

                    User user = userLocalStore.getLoggedInUser();
                    user.setUser_photo(registerResponse.getMessage());
                    userLocalStore.storeUserData(user);
                    UploadPhotoActivity.this.finish();

                    Log.d("IMMMMMM",registerResponse.getMessage());
                    Log.d("IMMMMMM",userLocalStore.getLoggedInUser().getUser_photo());

                    Intent intent = new Intent(UploadPhotoActivity.this,HomeActivity.class);
                    startActivity(intent);
                }
            }
        });

    }
}
