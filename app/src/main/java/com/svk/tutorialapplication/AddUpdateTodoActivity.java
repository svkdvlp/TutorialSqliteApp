package com.svk.tutorialapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.svk.tutorialapplication.alarmutils.AlarmHelper;
import com.svk.tutorialapplication.alarmutils.AlarmReceiver;
import com.svk.tutorialapplication.databaseutils.ToDoDatabaseHelper;
import com.svk.tutorialapplication.databaseutils.models.TodoModel;

import java.util.Calendar;

import static com.svk.tutorialapplication.utils.AppUtils.getformattedTimeString;
import static com.svk.tutorialapplication.utils.AppUtils.isTimeValid;
import static com.svk.tutorialapplication.utils.AppUtils.showToast;

public class AddUpdateTodoActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = AddUpdateTodoActivity.class.getSimpleName();

    Toolbar toolbar;

    private ToDoDatabaseHelper mToDoDatabaseHelper;

    private Button btn_add_update_job;
    private EditText edt_title,edt_desc;
    private TextView tv_picktime;
    private ImageView iv_picktime;

    private Context mContext;
    private String titleStr, descStr, timeStr;
    private Calendar cal;
    private long selectedTimeMillis;

    private int item_id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);

        bindViews();

        init();

        TodoModel todoModel = (TodoModel) getIntent().getSerializableExtra("todo_model");
        if(todoModel != null) {
            getSupportActionBar().setTitle("Update Job");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            btn_add_update_job.setText("UPDATE JOB");
            item_id = todoModel.getId();
            edt_title.setText(todoModel.getTitle());
            edt_desc.setText(todoModel.getDescription());
            selectedTimeMillis = todoModel.getNotifyTime();
            tv_picktime.setText(getformattedTimeString(todoModel.getNotifyTime()));
        }
        else {
            getSupportActionBar().setTitle("Add Job");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            btn_add_update_job.setText("ADD JOB");
        }

    }

    private void bindViews() {
        btn_add_update_job = findViewById(R.id.btn_add_update_job);
        edt_title = findViewById(R.id.edt_title);
        edt_desc = findViewById(R.id.edt_desc);
        tv_picktime = findViewById(R.id.tv_picktime);
        iv_picktime = findViewById(R.id.iv_picktime);
    }

    private void init() {
        mContext = this;
        mToDoDatabaseHelper = ToDoDatabaseHelper.getInstance(getApplicationContext());
        btn_add_update_job.setOnClickListener(this);
        iv_picktime.setOnClickListener(this);
        titleStr = descStr = timeStr = "";
        selectedTimeMillis = 0L;
        cal = Calendar.getInstance();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_update_job:
                titleStr = edt_title.getText().toString().trim();
                descStr = edt_desc.getText().toString().trim();

                if(item_id != -1)
                    updateJob(titleStr,descStr);
                else
                    saveToDoJob(titleStr,descStr);
                break;
            case R.id.iv_picktime:
                showPickTimeDialog();
                break;
            default:
                Log.e(TAG, "onClick: "+ "some problem" );
        }
    }

    private void updateJob(String titleStr, String descStr) {
        //Validation
        if(titleStr.equals("")){
            showToast(mContext,"Please enter some title");
            return;
        }else if(descStr.equals("")){
            showToast(mContext,"Please enter some description");
            return;
        }else if(selectedTimeMillis <= 0L){
            showToast(mContext,"Please enter notify time");
            return;
        }

        TodoModel item = new TodoModel(item_id, titleStr, descStr, selectedTimeMillis);
        int effected_rows = mToDoDatabaseHelper.updateJob(item);
        if(effected_rows>0){
            AlarmHelper.deleteJobAlarm(mContext,item_id,titleStr);
        }
        setResult(RESULT_OK);
        finish();
    }

    private void saveToDoJob(String titleStr, String descStr) {

        //Validation
        if(titleStr.equals("")){
            showToast(mContext,"Please enter some title");
            return;
        }else if(descStr.equals("")){
            showToast(mContext,"Please enter some description");
            return;
        }else if(selectedTimeMillis <= 0L){
            showToast(mContext,"Please enter notify time");
            return;
        }

        //setJobAlarm(selectedTimeMillis);

        TodoModel item = new TodoModel(titleStr,descStr,selectedTimeMillis);
        int id = mToDoDatabaseHelper.addJob(item);
        //setJobAlarm(selectedTimeMillis,id,titleStr);
        AlarmHelper.setJobAlarm(mContext,selectedTimeMillis,id,titleStr);
        setResult(RESULT_OK);
        finish();
    }

    /*private void setJobAlarm(long selectedTimeMillis, int reqCode, String titleStr) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent myIntent = new Intent(mContext, AlarmReceiver.class);
        myIntent.putExtra(KEY_JOB_TITLE,titleStr);
        myIntent.putExtra(KEY_JOB_id,reqCode);

        PendingIntent  pendingIntent = PendingIntent.getBroadcast(mContext, reqCode, myIntent, 0);
        try {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, selectedTimeMillis, pendingIntent);
        }catch (Exception e){
            e.printStackTrace();
            showToast(mContext, "Alarm set error");
        }
    }*/

    private void showPickTimeDialog() {
        // Get Current Time
        cal = Calendar.getInstance();
        int mHour = cal.get(Calendar.HOUR_OF_DAY);
        int mMinute = cal.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        cal = Calendar.getInstance();
                        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        cal.set(Calendar.MINUTE, minute);
                        cal.set(Calendar.SECOND, 0);
                        cal.set(Calendar.MILLISECOND, 0);

                        if(isTimeValid(cal.getTimeInMillis())) {
                            timeStr = getformattedTimeString(cal.getTimeInMillis());
                            selectedTimeMillis = cal.getTimeInMillis();
                            tv_picktime.setText(timeStr);
                        }else {
                            timeStr = "";
                            selectedTimeMillis = 0L;
                            tv_picktime.setText("");
                            showToast(mContext,"Please select a valid timestamp");
                        }

                    }
                }, mHour, mMinute, false);

        timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                timeStr = "";
                selectedTimeMillis = 0L;
                tv_picktime.setText("");
            }
        });

        timePickerDialog.show();
    }
}
