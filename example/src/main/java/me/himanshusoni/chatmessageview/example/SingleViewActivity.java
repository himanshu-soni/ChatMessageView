package me.himanshusoni.chatmessageview.example;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import me.himanshusoni.chatmessageview.ChatMessageView;

public class SingleViewActivity extends AppCompatActivity implements View.OnClickListener {

    private ChatMessageView mChatMessageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mChatMessageView = (ChatMessageView) findViewById(R.id.chatMessageView);

        findViewById(R.id.btn_arrow_gravity_start).setOnClickListener(this);
        findViewById(R.id.btn_arrow_gravity_end).setOnClickListener(this);
        findViewById(R.id.btn_arrow_gravity_center).setOnClickListener(this);
        findViewById(R.id.btn_arrow_position_left).setOnClickListener(this);
        findViewById(R.id.btn_arrow_position_right).setOnClickListener(this);
        findViewById(R.id.btn_arrow_position_top).setOnClickListener(this);
        findViewById(R.id.btn_arrow_position_bottom).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_arrow_gravity_start) {
            mChatMessageView.setArrowGravity(ChatMessageView.ArrowGravity.START);
        } else if (id == R.id.btn_arrow_gravity_center) {
            mChatMessageView.setArrowGravity(ChatMessageView.ArrowGravity.CENTER);
        } else if (id == R.id.btn_arrow_gravity_end) {
            mChatMessageView.setArrowGravity(ChatMessageView.ArrowGravity.END);
        } else if (id == R.id.btn_arrow_position_left) {
            mChatMessageView.setArrowPosition(ChatMessageView.ArrowPosition.LEFT);
        } else if (id == R.id.btn_arrow_position_right) {
            mChatMessageView.setArrowPosition(ChatMessageView.ArrowPosition.RIGHT);
        } else if (id == R.id.btn_arrow_position_top) {
            mChatMessageView.setArrowPosition(ChatMessageView.ArrowPosition.TOP);
        } else if (id == R.id.btn_arrow_position_bottom) {
            mChatMessageView.setArrowPosition(ChatMessageView.ArrowPosition.BOTTOM);
        }
    }
}
