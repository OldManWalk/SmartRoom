package com.example.smartroom.ui.home.homeextra;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.smartroom.R;
import com.example.smartroom.common.MyCursorAdapter;
import com.example.smartroom.common.MyDBOpenHelper;
import com.example.smartroom.common.addActivity;

public class studentFragement extends Fragment {
    private SQLiteDatabase db;
    private MyDBOpenHelper dbHelper = null;
    private StringBuilder sb;
    EditText et_find;
    ListView lv_show;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_student, container, false);
        dbHelper = new MyDBOpenHelper(getActivity(), getActivity().getFilesDir().toString() + "/contacts.db3", 1);

        mehdi.sakout.fancybuttons.FancyButton btn_add = root.findViewById(R.id.btn_add);
        //为“新增联系人”按钮绑定监听器
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), addActivity.class);
                startActivity(intent);

            }
        });

        Button btn_find = root.findViewById(R.id.btn_find);
        et_find = root.findViewById(R.id.et_find);
        lv_show = root.findViewById(R.id.lv_show);
        //为“查找”按钮绑定监听器
        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameOrPhoneStr = et_find.getText().toString();
                //执行查询操作
                Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
                        "select * from contact where name ==?  or phone==?",
                        new String[]{nameOrPhoneStr, nameOrPhoneStr});
                //显示查询结果
                MyCursorAdapter adapter = new MyCursorAdapter(
                        getActivity(), R.layout.cell, cursor, new String[]{"name", "phone"},
                        new int[]{R.id.tv_Name, R.id.tv_Phone}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
                );
                lv_show.setAdapter(adapter);
            }
        });
        return root;
    }

    //当前Activity从停止状态再次回到运行状态时，onStart()函数会被回调
    @Override
    public void onStart() {
        super.onStart();
        Cursor cursor = dbHelper.getReadableDatabase()
                .rawQuery("select * from contact", null);
        inflateList(cursor);
    }

    //把查询结果cursor，填充显示到ListView控件中
    private void inflateList(Cursor cursor) {
// 填充SimpleCursorAdapter
        MyCursorAdapter adapter=new MyCursorAdapter( getActivity(), R.layout.cell, cursor, new String[]{"name", "phone"},
                new int[]{R.id.tv_Name, R.id.tv_Phone}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        lv_show.setAdapter(adapter);
        lv_show.setOnItemClickListener(new AdapterView.OnItemClickListener() {
              @Override
              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                  ListView listView = (ListView) parent;
                  com.ramotion.foldingcell.FoldingCell a= view.findViewById(R.id.folding_cell);
                  a.toggle(false);
              }         });
    }

    //当前Activity销毁前onDestroy()函数会被回调
    @Override
    public void onDestroy() {
        super.onDestroy();
        // 退出程序时关闭MyDatabaseHelper里的SQLiteDatabase
        dbHelper.close();
    }
}
