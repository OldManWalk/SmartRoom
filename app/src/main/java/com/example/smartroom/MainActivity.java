package com.example.smartroom;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.example.smartroom.Service.SocketService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
public class MainActivity extends AppCompatActivity {
    Boolean socketStatus = false;
    FloatingActionButton fab;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //设置底部栏
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        //浮动按钮的设置
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(socketStatus == false) {
                Snackbar.make(view, "未连接Socket服务器，点击链接", Snackbar.LENGTH_INDEFINITE).setAction("链接", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this,SocketService.class);
                        bindService(intent, serviceConnection, Service.BIND_AUTO_CREATE);
                    }
                }).show();
                }
                else{
                    Snackbar.make(view, "已连接Socket服务器，点击关闭链接", Snackbar.LENGTH_INDEFINITE).setAction("关闭链接", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            unbindService(serviceConnection);
                            socketStatus = false;
                        }
                    }).show();
                }

            }
        });

    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            SocketService mService = ((SocketService.MyBinder) service).getService();
            socketStatus = mService.getStatus();
            System.out.println(mService.getStatus());
            Toast.makeText(MainActivity.this,""+mService.getStatus(),Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("test_out", "----->服务已断开连接");
        }
    };
}

