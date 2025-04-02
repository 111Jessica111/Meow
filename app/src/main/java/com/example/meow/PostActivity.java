package com.example.meow;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.widget.Toast;



public class PostActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btn_back;
    private EditText post_writing;
    private ImageView post_image1;
    private ImageView post_image2;
    private ImageView post_image3;

    public static final int CHOOSE_PHOTO = 2;
    private RelativeLayout btn_tag;
    private RelativeLayout btn_locate;
    private ImageView tag_dot;
    private TextView tag_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_post);

        btn_back = findViewById(R.id.btn_back);
        post_writing = findViewById(R.id.post_writing);
        btn_tag = findViewById(R.id.btn_tag);
        tag_dot = findViewById(R.id.tag_dot);
        tag_content = findViewById(R.id.tag_content);
        btn_locate = findViewById(R.id.btn_locate);

        //照片获取
        post_image1 = findViewById(R.id.post_image1);
        post_image2 = findViewById(R.id.post_image2);
        post_image3 = findViewById(R.id.post_image3);

        //SharedPreference数据显示
        SharedPreferences sharedPreferences = getSharedPreferences("post",MODE_PRIVATE);
        String content = sharedPreferences.getString("content","");
        //显示保存的文字
        post_writing.setText(content);

        //back按键监听
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
                        //处理文字的保存
                        if (content != null){
                            editor.putString("content",post_writing.getText().toString());
                        }
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

        //设置照片监听
        post_image1.setOnClickListener(this);
        post_image2.setOnClickListener(this);
        post_image3.setOnClickListener(this);

        //tag按键监听
        btn_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(PostActivity.this);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT
                ));

                dialog.setContentView(R.layout.post_tag);
                dialog.show();

                LinearLayoutCompat common_share = dialog.findViewById(R.id.common_share);
                LinearLayoutCompat SOS_share = dialog.findViewById(R.id.SOS_share);

                common_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tag_dot.getDrawable() != getResources().getDrawable(R.mipmap.dot_orange)){
                            tag_dot.setImageResource(R.mipmap.dot_orange);
                            tag_content.setText("可爱的喵桑");
                            tag_content.setTextColor(getResources().getColor(R.color.grey));
                        }
                        dialog.dismiss();
                    }
                });

                SOS_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tag_dot.getDrawable() != getResources().getDrawable(R.mipmap.dot_red)){
                            tag_dot.setImageResource(R.mipmap.dot_red);
                            tag_content.setText("求助SOS！");
                            tag_content.setTextColor(getResources().getColor(R.color.red));
                        }
                        dialog.dismiss();
                    }
                });
            }
        });

        //获取当前的位置
        btn_locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        //判断是否点击添加照片
        if (v.getId() == R.id.post_image1 || v.getId() == R.id.post_image2 || v.getId() == R.id.post_image3){
            if (post_image1.getDrawable() == null || post_image2.getDrawable() == null || post_image3.getDrawable() == null){
                choosePhotoInPhone();
            }else {
                Toast.makeText(this,"You have already add three photo!",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void choosePhotoInPhone() {
        //判断是否有访问权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, CHOOSE_PHOTO);
        } else {
            //权限已被授予，启动选择照片的Intent
            JumpToAlbum();
        }
    }
    //打开相册
    private void JumpToAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,CHOOSE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, int deviceId) {
        if (requestCode == CHOOSE_PHOTO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //权限被授予，执行选择照片的操作
                JumpToAlbum();
            } else {
                //权限被拒绝，处理相应逻辑
                Toast.makeText(this,"You denied the permission",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_PHOTO && resultCode == RESULT_OK && data != null) {
            Uri selectImageUri = data.getData();
            if (selectImageUri != null) {
                if (post_image1.getDrawable() == null) {
                    post_image1.setImageURI(selectImageUri);
                    post_image1.setBackgroundColor(Color.WHITE);
                    post_image2.setVisibility(View.VISIBLE);
                } else if (post_image1.getDrawable() != null && post_image2.getDrawable() == null) {
                    post_image2.setVisibility(View.VISIBLE);
                    post_image2.setImageURI(selectImageUri);
                    post_image2.setBackgroundColor(Color.WHITE);
                    post_image3.setVisibility(View.VISIBLE);
                } else if (post_image2.getDrawable() != null && post_image3.getDrawable() == null) {
                    post_image3.setVisibility(View.VISIBLE);
                    post_image3.setImageURI(selectImageUri);
                    post_image3.setBackgroundColor(Color.WHITE);
                }
            }
        }
    }
}