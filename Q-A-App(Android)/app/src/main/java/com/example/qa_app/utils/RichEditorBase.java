package com.example.qa_app.utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qa_app.R;

import java.io.IOException;

public abstract class RichEditorBase extends AppCompatActivity {
    protected EditText editText;
    private static final int REQUEST_CODE_IMAGE_PICK = 1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        editText = findViewById(R.id.edittext_rich_text);
        setupRichTextEditor();
    }
    private void insertImage(Uri imageUri) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bitmap != null) {
            bitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, false);
            ImageSpan imageSpan = new ImageSpan(this, bitmap);
            SpannableString spannableString = new SpannableString("a");
            spannableString.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            int start = editText.getSelectionStart();
            editText.getText().insert(start, spannableString);
        }
    }

    protected abstract int getLayoutId(); // 子类提供布局ID

    private void setupRichTextEditor() {
        findViewById(R.id.button_insert_image).setOnClickListener(v -> insertImage());
        findViewById(R.id.button_add_link).setOnClickListener(v -> addLink());
    }

    protected void insertImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_IMAGE_PICK);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            insertImage(selectedImageUri);
        }
    }

    protected void addLink() {
        
    }
}
