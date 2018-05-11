package com.svk.tutorialapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.svk.tutorialapplication.databaseutils.models.TodoModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = MainActivity.class.getSimpleName();//Class name can be refactored.
    public final int REQ_CODEADD = 1337;

    FloatingActionButton fab_add;

    Context mContext;
    ArrayList<TodoModel> todoModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindViews();

        init();
    }

    private void init() {
        mContext = this;
        fab_add.setOnClickListener(this);
    }

    private void bindViews() {
        fab_add = findViewById(R.id.fab_add);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_add:
                startActivityForResult(new Intent(mContext, AddTodoActivity.class), REQ_CODEADD);
                break;
            default:
                Log.e(TAG, "onClick: "+ "some problem" );
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_CODEADD && resultCode == RESULT_OK){
            getUpdatedTodoList();
        }
    }

    public ArrayList<TodoModel> getUpdatedTodoList() {
        return null;
    }
}
