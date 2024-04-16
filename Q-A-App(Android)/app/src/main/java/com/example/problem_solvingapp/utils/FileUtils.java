package com.example.problem_solvingapp.utils;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {

    public static File uriToFile(@NonNull Context context, @NonNull Uri uri) {
        File file = new File(context.getFilesDir(), "upload_" + System.currentTimeMillis());
        try (InputStream inputStream = context.getContentResolver().openInputStream(uri);
             OutputStream outputStream = new FileOutputStream(file)) {
            if (inputStream != null) {
                byte[] buf = new byte[1024];
                int len;
                while ((len = inputStream.read(buf)) > 0) {
                    outputStream.write(buf, 0, len);
                }
            }
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
