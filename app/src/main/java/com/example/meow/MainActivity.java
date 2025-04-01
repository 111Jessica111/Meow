package com.example.meow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.meow.Fragment.ChatFragment;
import com.example.meow.Fragment.HelpFragment;
import com.example.meow.Fragment.MainFragment;
import com.example.meow.Fragment.MineFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MainFragment mMainFragment;
    private HelpFragment mHelpFragment;
    private ChatFragment mChatFragment;
    private MineFragment mMineFragment;

    private ImageButton navigation_main;
    private ImageButton navigation_help;
    private ImageButton navigation_chat;
    private ImageButton navigation_mine;
    private FragmentTransaction fragmentTransaction;
    private ImageView btn_post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        //初始化
        navigation_main = findViewById(R.id.navigation_main);
        navigation_help = findViewById(R.id.navigation_help);
        navigation_chat = findViewById(R.id.navigation_chat);
        navigation_mine = findViewById(R.id.navigation_mine);

        btn_post = findViewById(R.id.btn_post);

        //导航页面设置监听
        navigation_main.setOnClickListener(this);
        navigation_help.setOnClickListener(this);
        navigation_chat.setOnClickListener(this);
        navigation_mine.setOnClickListener(this);

        //初始为MainFragment
        if (savedInstanceState == null) {
            mMainFragment = new MainFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fl_content, mMainFragment)
                    .commit();
        }

        //post跳转
        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PostActivity.class);
                startActivity(intent);
            }
        });
    }

    //设置点击事件，显示对应的fragment
    @Override
    public void onClick(View v) {
        //获取当前点击的碎片
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //隐藏当前的碎片
        hideFragment(fragmentTransaction);

        if (v.getId() == R.id.navigation_main){
            if (mMainFragment == null){
                mMainFragment = new MainFragment();
                fragmentTransaction.add(R.id.fl_content,mMainFragment);
            }else {
                fragmentTransaction.show(mMainFragment);
            }
        } else if (v.getId() == R.id.navigation_help) {
            if (mHelpFragment == null){
                mHelpFragment = new HelpFragment();
                fragmentTransaction.add(R.id.fl_content,mHelpFragment);
            }else {
                fragmentTransaction.show(mHelpFragment);
            }
        } else if (v.getId() == R.id.navigation_chat) {
            if (mChatFragment == null){
                mChatFragment = new ChatFragment();
                fragmentTransaction.add(R.id.fl_content,mChatFragment);
            }else {
                fragmentTransaction.show(mChatFragment);
            }
        }else {
            if (mMineFragment == null){
                mMineFragment = new MineFragment();
                fragmentTransaction.add(R.id.fl_content,mMineFragment);
            }else {
                fragmentTransaction.show(mMineFragment);
            }
        }
        //提交
        fragmentTransaction.commit();
    }

    private void hideFragment(FragmentTransaction fragmentTransaction) {
        //隐藏
        if (mMainFragment != null){
            fragmentTransaction.hide(mMainFragment);
        }
        if (mHelpFragment != null) {
            fragmentTransaction.hide(mHelpFragment);
        }
        if (mChatFragment != null) {
            fragmentTransaction.hide(mChatFragment);
        }
        if (mMineFragment != null){
            fragmentTransaction.hide(mMineFragment);
        }
    }
}