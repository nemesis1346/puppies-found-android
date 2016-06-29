package com.mywaytech.puppiessearchclient.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.mywaytech.puppiessearchclient.R;
import com.mywaytech.puppiessearchclient.controllers.fragments.AdoptionFragment;
import com.mywaytech.puppiessearchclient.controllers.fragments.WallFragment;
import com.mywaytech.puppiessearchclient.models.UserPetObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by m.maigua on 4/13/2016.
 */
public class NewPetActivity extends BaseActivity {
    public static final int FRAGOPTIONS1 = 01;
    private EditText newAddress;
    private EditText newComment;
    private EditText newResponsable;
    private int newImage;
    static int TAKE_PICTURE_PHOTO = 3;
    private static final int CAMERA_REQUEST = 0;
    private static final int BACK_TOACTIVITY = 1;
    private Button btn_image;
    private Button btn_report;

    private ImageView imageShow;

    public static final String FRAGMENT_VALUE = "com.mywaytech.puppiessearchclient.extras.extra_fragment_value";

    private Bitmap photo;
    private UserPetObject userPetObject;

    private File file;
    private int callback;
    private String final_path;

    @Override
    public int getToolbarTitle() {
        return R.string.resport_lost_pet;
    }

    @Override
    public int getContentResource() {
        return R.layout.activity_new_lost_pet;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callback = getIntent().getIntExtra(FRAGMENT_VALUE, 0);

        newAddress = (EditText) findViewById(R.id.edit_text_address_lost_activity);
        newComment = (EditText) findViewById(R.id.edit_text_comment_lost_activity);
        newResponsable = (EditText) findViewById(R.id.edit_text_name_lost_activity);

        btn_image = (Button) findViewById(R.id.input_image);
        btn_report = (Button) findViewById(R.id.btn_reportar);

        imageShow = (ImageView) findViewById(R.id.showImage);

        btn_image.setOnClickListener(addPhoto);
        btn_report.setOnClickListener(backToActivity);
    }

    public View.OnClickListener addPhoto = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //  intent.putExtra(NewPetActivity.EXTRA_PHOTO, file);
            startActivityForResult(intent, CAMERA_REQUEST);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            btn_image.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            btn_image.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            photo = (Bitmap) data.getExtras().get("data");

            file = new File(getBaseContext().getExternalFilesDir(null) + "/images");
            if (!file.isDirectory()) {
                file.mkdir();
            }

            file = new File(getBaseContext().getExternalFilesDir(null) + "/images", "img_" + System.currentTimeMillis() + ".jpg");
            final_path = file.getPath();
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                photo.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                imageShow.setImageBitmap(photo);
                imageShow.setVisibility(View.VISIBLE);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //userPetObject = new UserPetObject(newResponsable.getText().toString(), newAddress.getText().toString(), data.getSerializableExtra(EXTRA_PHOTO).toString(), newComment.getText().toString());
            //data.putExtra(MainActivity.EXTRA_NEWPET_DATA,userPetObject);
        }

//        if (requestCode == BACK_TOACTIVITY && resultCode == Activity.RESULT_OK) {
//            if (newResponsable.getText().toString().isEmpty() || newAddress.getText().toString().isEmpty() || newComment.getText().toString().isEmpty()) {
//                Toast.makeText(NewPetActivity.this, "Ingrese Campos", Toast.LENGTH_LONG).show();
//            } else {
//                userPetObject = new UserPetObject(newResponsable.getText().toString(), newAddress.getText().toString(), imageShow.getImageAlpha(), newComment.getText().toString());
//                data.putExtra(MainActivity.EXTRA_NEWPET_DATA, userPetObject);
//                startActivity(data);
//            }
//        }

    }

    public View.OnClickListener backToActivity = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            //intent.putExtra(MainActivity.EXTRA_NEWPET_DATA, userPetObject);
            if (newResponsable.getText().toString().isEmpty() || newAddress.getText().toString().isEmpty() || newComment.getText().toString().isEmpty() || final_path.isEmpty()) {
                Toast.makeText(NewPetActivity.this, "Ingrese Campos", Toast.LENGTH_LONG).show();
            } else {
                userPetObject = new UserPetObject(newResponsable.getText().toString(), newAddress.getText().toString(), final_path, newComment.getText().toString());
                switch (callback) {
                    case 1:
                        intent.putExtra(WallFragment.EXTRA_UPDATE_PET, userPetObject);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                        break;
                    case 2:
                        intent.putExtra(AdoptionFragment.EXTRA_ADOPT_PET, userPetObject);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                        break;
                    default:
                        intent.putExtra(WallFragment.EXTRA_UPDATE_PET, userPetObject);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                        break;
                }

            }


            //startActivityForResult(intent, BACK_TOACTIVITY);
        }
    };


}
