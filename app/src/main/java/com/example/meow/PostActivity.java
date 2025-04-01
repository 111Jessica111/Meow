package com.example.meow;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PostActivity extends AppCompatActivity {

    private ImageButton btn_back;
    private EditText post_writing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_post);

        btn_back = findViewById(R.id.btn_back);
        post_writing = findViewById(R.id.post_writing);

        //SharedPreference数据显示
        SharedPreferences sharedPreferences = getSharedPreferences("post",MODE_PRIVATE);
        String content = sharedPreferences.getString("content","");
        post_writing.setText(content);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(PostActivity.this);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT
                ));

                dialog.setContentView(R.layout.post_dailog);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.show();

                TextView btn_post_save = dialog.findViewById(R.id.btn_post_save);
                TextView btn_post_delete = dialog.findViewById(R.id.btn_post_delete);
                TextView btn_post_cancel = dialog.findViewById(R.id.btn_post_cancel);

                SharedPreferences.Editor editor = getSharedPreferences("post",MODE_PRIVATE).edit();

                //SharedPreference保存
                btn_post_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editor.putString("content",post_writing.getText().toString());
                        editor.apply();
                        dialog.dismiss();
                        finish();
                    }
                });
                //删除，返回原界面
                btn_post_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editor.clear();
                        editor.apply();
                        post_writing.setText("");
                        dialog.dismiss();
                        finish();
                    }
                });
                //取消，回到Post界面
                btn_post_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }
}