package com.example.qa_app.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qa_app.ApiServiceSingleton;
import com.example.qa_app.QuestionApiService;
import com.example.qa_app.R;
import com.example.qa_app.utils.FileUtils;
import com.google.android.material.textfield.TextInputEditText;
import java.io.File;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText usernameEditText, emailEditText, passwordEditText;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        Button pickImageBtn = findViewById(R.id.pickImageBtn);
        Button registerBtn = findViewById(R.id.registerBtn);

        pickImageBtn.setOnClickListener(view -> pickImage());
        registerBtn.setOnClickListener(view -> registerUser());

        // ActivityResultLauncher 初始化
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        // TODO:更新UI显示所选图片的缩略图
                    }
                });
    }

    // 图片选择器的启动器
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    private void registerUser() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();

        if (selectedImageUri == null || username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "All fields and an avatar are required", Toast.LENGTH_SHORT).show();
            return;
        }


        // 创建 service
        QuestionApiService service = ApiServiceSingleton.getApiService();

        RequestBody usernameBody = RequestBody.create(MediaType.parse("multipart/form-data"), username);
        RequestBody passwordBody = RequestBody.create(MediaType.parse("multipart/form-data"), password);
        RequestBody emailBody = RequestBody.create(MediaType.parse("multipart/form-data"), email);


        File imageFile = FileUtils.uriToFile(this, selectedImageUri); // 实现这个方法来获取文件路径
        if (imageFile != null) {
            // 创建 RequestBody 实例
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
            MultipartBody.Part body = MultipartBody.Part.createFormData("avatar", imageFile.getName(), requestFile);
            // 使用 Retrofit 上传文件
            Call<ResponseBody> call = service.register(usernameBody, passwordBody, emailBody, body);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(RegisterActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }


    }
}
//
