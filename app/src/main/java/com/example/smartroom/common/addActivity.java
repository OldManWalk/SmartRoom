package com.example.smartroom.common;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartroom.R;

public class addActivity extends AppCompatActivity {
    private MyDBOpenHelper dbHelper = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        //获取SQLiteOpenHelper对象
        dbHelper = new MyDBOpenHelper(this, this.getFilesDir().toString() + "/contacts.db3", 1);
        mehdi.sakout.fancybuttons.FancyButton btn_save = findViewById(R.id.bt_save);
        //为“保存”按钮绑定监听器
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_name = findViewById(R.id.studentname);
                EditText et_phone = findViewById(R.id.studentphone);
                String nameStr = et_name.getText().toString();
                String phoneStr = et_phone.getText().toString();
                // 数据库表执行插入语句
                dbHelper.getReadableDatabase().execSQL("insert into contact values(null , ? , ?)",
                        new String[]{nameStr, phoneStr});
                finish();
            }
        });
    }

}
