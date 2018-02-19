package com.mustafa.arif.reddit.backend;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Special class to download images using picasso library
 */

public class  ImageDownloader implements Target {
    final Context context;
    final String imageDir;
    final String imageName;

    public ImageDownloader(final Context context, final String imageDir, final String imageName) {
        this.context=context;
        this.imageDir=imageDir;
        this.imageName=imageName;
    }

    @Override
    public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String iPath = createFolder(imageDir)
                        .concat("/")
                        .concat(createImageFileName(imageName));
                final File myImageFile = new File(iPath);
                formatingForVisibleInGallery(context.getContentResolver(), iPath, imageName);
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(myImageFile);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {
    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {
        if (placeHolderDrawable != null) {
        }
    }

    private Uri formatingForVisibleInGallery(ContentResolver contentResolver, String imagePath, String description) {
        ContentValues v = new ContentValues();
        v.put(MediaStore.Images.Media.DESCRIPTION, description);
        long dateTaken = new Date().getTime();
        v.put(MediaStore.Images.Media.DATE_ADDED, dateTaken);
        v.put(MediaStore.Images.Media.DATE_TAKEN, dateTaken);
        v.put(MediaStore.Images.Media.DATE_MODIFIED, dateTaken);
        v.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        File f = new File(imagePath);
        File parent = f.getParentFile();
        String path = parent.toString().toLowerCase();
        String name = parent.getName().toLowerCase();
        v.put(MediaStore.Images.ImageColumns.BUCKET_ID, path.hashCode());
        v.put(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, name);
        v.put(MediaStore.Images.Media.SIZE, f.length());
        v.put("_data", imagePath);
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, v);


    }

    private static String createImageFileName(String fileName) {
        return fileName.concat(".png");
    }

    private static String createFolder(String name) {
        String folderName = (Environment.getExternalStorageDirectory().getAbsolutePath().toString())
                .concat("/")
                .concat(name)
                .concat("/");

        File dir = new File(folderName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return folderName;
    }
}
