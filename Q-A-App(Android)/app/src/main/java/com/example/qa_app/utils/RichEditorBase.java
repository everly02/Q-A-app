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
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.qa_app.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;

public abstract class RichEditorBase extends AppCompatActivity {
    private static final int REQUEST_IMAGE_PERMISSION = 100;

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

    private void checkReadMediaImagesPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED) {
            // 如果应用没有权限，向用户请求权限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                    REQUEST_IMAGE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_IMAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {

                Toast.makeText(this, "Permission denied, some features may not work.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    protected EditText editText;
    private static final int REQUEST_CODE_PICK_IMAGE = 1001;

    private static final int PICK_IMAGE_REQUEST = 100;
    Button insertImageButton;
    Button addLinkButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        editText = findViewById(R.id.edittext_rich_text);
        insertImageButton = findViewById(R.id.button_insert_image);
        addLinkButton = findViewById(R.id.button_add_link);
        insertImageButton.setOnClickListener(v -> {
            checkReadMediaImagesPermission();
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
        });
        addLinkButton.setOnClickListener(v -> {

            showEditLinkDialog();
        });
    }

    protected abstract int getLayoutId();


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                String encodedImage = encodeImage(bitmap);
                insertImage(encodedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String encodeImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
    private void insertImage(String base64Image) {
        String imageTag = "<img src='data:image/jpeg;base64," + base64Image + "'/>";
        Spanned spanned = Html.fromHtml(imageTag, new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                try {
                    byte[] decodedString = Base64.decode(source.split(",")[1], Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    Drawable drawable = new BitmapDrawable(getResources(), decodedByte);
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                    return drawable;
                } catch (Exception e) {
                    return null;
                }
            }
        }, null);
        editText.setText(spanned);
    }


}
