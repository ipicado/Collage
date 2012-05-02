package com.unitedware.collage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UploadPhoto extends Activity implements OnClickListener {

    private Uri mImageCaptureUri, outputFileUri;
    boolean isViewHidden = false;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 2;
    int startUp = 0;

    Button selectAnother;
    Bundle extras;
    Bitmap userPhoto;
    ImageView mImageView;
    TextView title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photoview);

        initialize();

        if (startUp == 0) {
            // Run dialog code on startup
            isViewHidden = true;
            choosePhotoOption();
            startUp++;
        }
    }

    public void initialize() {
        selectAnother = (Button) findViewById(R.id.bChoosePhoto);
        selectAnother.setOnClickListener(this);
        selectAnother.setVisibility(View.GONE);
        title = (TextView) findViewById(R.id.tvUploadTitle);
        title.setVisibility(View.GONE);

        /*
         * Create the directory if needed for the gridView This is setup to also
         * test to make sure if it was created, I needed something visual right
         * now, this won't go in final development
         */
        File folder = new File(Environment.getExternalStorageDirectory()
                + "/Collage");
        boolean success = false;
        if (!folder.exists()) {
            success = folder.mkdir();
        }
        // if (success == true) {
        // // Toast msg = Toast.makeText(UploadPhoto.this,
        // // "Folder was created",
        // // Toast.LENGTH_LONG);
        // // msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2,
        // // msg.getYOffset() / 2);
        // // msg.show();
        // } else {
        // // Toast msg = Toast.makeText(UploadPhoto.this,
        // // "Folder was either already\ncreated or it failed",
        // // Toast.LENGTH_LONG);
        // // msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2,
        // // msg.getYOffset() / 2);
        // // msg.show();
        // }
    }

    public void choosePhotoOption() {

        final String[] items = new String[] { "Take from camera",
                "Select from gallery" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_item, items);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                // Get picture from camera
                if (item == 0) {
                    // Takes the photo and stores it to a temporary directory
                    Intent intent = new Intent(
                            android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    mImageCaptureUri = Uri.fromFile(new File(
                            Environment.getExternalStorageDirectory(),
                            "/Collage/tmp_upload_"
                                    + String.valueOf(System.currentTimeMillis())
                                    + ".jpg"));
                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                            mImageCaptureUri);
                    startActivityForResult(intent, PICK_FROM_CAMERA);

                    try {
                        intent.putExtra("return-date", true);
                        startActivityForResult(intent, PICK_FROM_CAMERA);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }

                }

                else { // pick from file
                    Intent intent = new Intent();

                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);

                    startActivityForResult(Intent.createChooser(intent,
                            "Complete action using"), PICK_FROM_FILE);
                }
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.show();

    }

    public boolean showView(boolean isHidden) {

        // Reveals layout if necessary
        if (isHidden == true) {
            selectAnother.setVisibility(View.VISIBLE);
            title.setVisibility(View.VISIBLE);
            return isHidden = false;
        }
        return isHidden;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onActivityResult(int, int,
     * android.content.Intent) This onActivity result needs to changed from just
     * setting one picture, to multiple pictures, in order to do this you need
     * to save these to a file directory, I have already set up the permissions.
     * This will allow me to use the ImageAdapter class grab all the photos from
     * the directory and display them into the gridview
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {
            case PICK_FROM_CAMERA:

                mImageView.setImageURI(outputFileUri);
                userPhoto = (Bitmap) data.getExtras().get("data");
                // mImageView.setImageBitmap(userPhoto);
                showView(isViewHidden);
                break;

            case PICK_FROM_FILE:

                // This gets the path data from the data given from the Uri and
                // then grabs the image and sets it to the Bitmap variable
                Uri targetUri = data.getData();
                String photoDir = targetUri.toString();

                File tempPhoto = new File(photoDir, "tmp_copied_"
                        + String.valueOf(System.currentTimeMillis()) + ".jpg");

                showView(isViewHidden);

                break;
            }
        }
        if (resultCode == RESULT_CANCELED) {
            // showView(isViewHidden);
        }

    }

    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
        case R.id.bChoosePhoto:
            choosePhotoOption();
            break;
        }
    }
}
