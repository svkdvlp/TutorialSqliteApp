package com.svk.tutorialapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.svk.tutorialapplication.databaseutils.ToDoDatabaseHelper;
import com.svk.tutorialapplication.databaseutils.models.TodoModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = MainActivity.class.getSimpleName();//Class name can be refactored.
    public final int REQ_CODE_ADD = 1337;
    public final int REQ_CODE_UPDATE = 1338;

    private RecyclerView rv_todoItems;
    private FloatingActionButton fab_add;
    private LinearLayoutManager layoutManager;

    private Context mContext;
    private ArrayList<TodoModel> todoModelArrayList = new ArrayList<>();
    private TodoItemsAdapter todoItemsAdapter;
    private ToDoDatabaseHelper mToDoDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindViews();

        init();
        updateTodoListUI();

        todoItemsAdapter.setOnListItemActionListener(new TodoItemsAdapter.OnListItemActionListener() {
            @Override
            public void onEditSelected(TodoModel todoModel) {
                //Toast.makeText(mContext, ""+todoModel.getId(), Toast.LENGTH_SHORT).show();
                gotoAddUpdateTodoActivity(todoModel);
            }

            @Override
            public void onDeleteSelected(int item_id) {
                //Toast.makeText(mContext, ""+item_id, Toast.LENGTH_SHORT).show();
                deleteJobItem(item_id);
            }
        });
    }

    private void deleteJobItem(int item_id) {
        int rows_effected = mToDoDatabaseHelper.deleteJobItem(item_id);
        if(rows_effected > 0) {
            updateTodoListUI();
        }
    }

    private void gotoAddUpdateTodoActivity(TodoModel todoModel) {
        Intent intent = new Intent(mContext, AddUpdateTodoActivity.class);
        intent.putExtra("todo_model", todoModel);
        startActivityForResult(intent, REQ_CODE_UPDATE);
    }

    private void init() {
        mContext = this;
        mToDoDatabaseHelper = ToDoDatabaseHelper.getInstance(getApplicationContext());
        fab_add.setOnClickListener(this);

        layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rv_todoItems.setLayoutManager(layoutManager);
        rv_todoItems.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        todoItemsAdapter = new TodoItemsAdapter(mContext, todoModelArrayList);
        rv_todoItems.setAdapter(todoItemsAdapter);
    }

    private void bindViews() {
        fab_add = findViewById(R.id.fab_add);
        rv_todoItems = findViewById(R.id.rv_todoItems);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_add:
                startActivityForResult(new Intent(mContext, AddUpdateTodoActivity.class), REQ_CODE_ADD);
                break;
            default:
                Log.e(TAG, "onClick: "+ "some problem" );
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_ADD :
            case REQ_CODE_UPDATE :
                if(resultCode == RESULT_OK)
                    updateTodoListUI();
                break;
        }
    }

    public ArrayList<TodoModel> getUpdatedTodoList() {
        return mToDoDatabaseHelper.getJobList();
    }

    private void updateTodoListUI() {
        ArrayList<TodoModel> list = getUpdatedTodoList();
        if(list != null) {
            todoModelArrayList.clear();
            todoModelArrayList.addAll(list);
            todoItemsAdapter.notifyDataSetChanged();
        }
    }

}
