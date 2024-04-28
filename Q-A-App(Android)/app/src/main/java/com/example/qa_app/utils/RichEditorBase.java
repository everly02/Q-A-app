package com.example.qa_app.utils;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.qa_app.R;

import java.io.IOException;
import java.io.InputStream;

public abstract class RichEditorBase extends AppCompatActivity {
    private static final int REQUEST_IMAGE_PERMISSION = 100; // 自定义请求码

    private void showEditLinkDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_edit_link, null);

        EditText editTextLinkText = dialogView.findViewById(R.id.edit_text_link_text);
        EditText editTextLinkUrl = dialogView.findViewById(R.id.edit_text_link_url);

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("编辑超链接")
                .setView(dialogView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String linkText = editTextLinkText.getText().toString();
                        String linkUrl = editTextLinkUrl.getText().toString();
                        int start = editText.getSelectionStart();
                        int end = editText.getSelectionEnd();

                        SpannableStringBuilder ssb = new SpannableStringBuilder(editText.getText());
                        ssb.replace(start, end, linkText);
                        ssb.setSpan(new URLSpan(linkUrl), start, start + linkText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        editText.setText(ssb);
                    }
                })
                .setNegativeButton("取消", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // 检查READ_MEDIA_IMAGES权限
    private void checkReadMediaImagesPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED) {
            // 如果应用没有权限，向用户请求权限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                    REQUEST_IMAGE_PERMISSION);
        }
    }

    // 重写onRequestPermissionsResult以处理用户的选择
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_IMAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 用户同意授权，可以执行相关操作
                // ...
            } else {
                // 用户拒绝授权，根据需求处理，比如提示用户权限重要性
                Toast.makeText(this, "Permission denied, some features may not work.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    protected EditText editText;
    private static final int REQUEST_CODE_PICK_IMAGE = 1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        editText = findViewById(R.id.edittext_rich_text);
        setupRichTextEditor();
    }

    protected abstract int getLayoutId(); // 子类提供布局ID

    private void setupRichTextEditor() {
        findViewById(R.id.button_insert_image).setOnClickListener(v -> {
            checkReadMediaImagesPermission();
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
        });
        findViewById(R.id.button_add_link).setOnClickListener(v -> showEditLinkDialog());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                float scaleWidth = ((float) 200) / width;
                float scaleHeight = ((float) 200) / height;
                Matrix matrix = new Matrix();
                matrix.postScale(scaleWidth, scaleHeight);
                Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

                SpannableStringBuilder ssb = new SpannableStringBuilder(editText.getText());
                int cursorPosition = editText.getSelectionStart();
                ssb.insert(cursorPosition, " ");
                ssb.setSpan(new ImageSpan(resizedBitmap), cursorPosition, cursorPosition + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                editText.setText(ssb);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
