package com.unitedware.collage;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    // Grabs a file directory and creates the array needed for the gridView
    @SuppressWarnings("null")
    public Integer[] getImagesFromDirTo(String dir) {
        Integer[] finalGridList = null;
        Bitmap[] gridImages = null;
        File filePath = new File(dir);
        String[] filesInPath = filePath.list();

        for (int i = 0; i < filesInPath.length; i++) {
            String fileToConvert = Environment.getExternalStorageDirectory()
                    + File.separator + dir;
            Bitmap newImage = BitmapFactory.decodeFile(fileToConvert);
            gridImages[i] = newImage;
        }

        return finalGridList;
    }

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) { // if it's not recycled, initialize some
                                   // attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    String dir = Environment.getExternalStorageDirectory() + File.separator
            + "Collage/";

    // A place holder for right now
    private Integer[] mThumbIds = { R.drawable.ic_launcher };
}