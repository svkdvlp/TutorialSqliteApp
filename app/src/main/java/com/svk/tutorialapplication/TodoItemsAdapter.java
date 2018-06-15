package com.svk.tutorialapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.svk.tutorialapplication.databaseutils.models.TodoModel;
import com.svk.tutorialapplication.utils.AppUtils;

import java.util.ArrayList;

/**
 * Created by web59 on 11/5/18.
 */

public class TodoItemsAdapter extends RecyclerView.Adapter<TodoItemsAdapter.ItemViewHolder> {

    private Context context;
    private ArrayList<TodoModel> todoModelArrayList;
    private OnListItemActionListener onListItemActionListener;

    public TodoItemsAdapter(Context context, ArrayList<TodoModel> todoModelArrayList) {
        this.context = context;
        this.todoModelArrayList = todoModelArrayList;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_title;
        private TextView tv_description;
        private TextView tv_notifyTime;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_description = itemView.findViewById(R.id.tv_description);
            tv_notifyTime = itemView.findViewById(R.id.tv_notifyTime);
        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.todo_item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) {
        final TodoModel todoModel = todoModelArrayList.get(position);
        holder.tv_title.setText(todoModel.getTitle());
        holder.tv_description.setText(todoModel.getDescription());
        holder.tv_notifyTime.setText(AppUtils.getformattedTimeString(todoModel.getNotifyTime()));

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                /*LayoutInflater layoutInflater = ((MainActivity) context).getLayoutInflater();
                View view = layoutInflater.inflate(R.layout.edit_delete_layout, null);
                TextView tv_title = view.findViewById(R.id.tv_title);
                TextView tv_description = view.findViewById(R.id.tv_description);
                TextView tv_notifyTime = view.findViewById(R.id.tv_notifyTime);
                tv_title.setText(todoModel.getTitle());
                tv_description.setText(todoModel.getDescription());
                tv_notifyTime.setText(AppUtils.getformattedTimeString(todoModel.getNotifyTime()));*/

                CharSequence[] items = {"Edit", "Delete"};
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0 : //Edit option
                                if(onListItemActionListener != null)
                                    onListItemActionListener.onEditSelected(todoModel);
                                break;

                            case 1 : //Delete option
                                if(onListItemActionListener != null)
                                    onListItemActionListener.onDeleteSelected(todoModel.getId(),todoModel.getTitle());
                                break;
                        }
                    }
                }).show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return todoModelArrayList.size();
    }

    public interface OnListItemActionListener {
        void onEditSelected(TodoModel todoModel);
        void onDeleteSelected(int item_id, String item_title);
    }

    public void setOnListItemActionListener(OnListItemActionListener listener) {
        this.onListItemActionListener = listener;
    }

}
