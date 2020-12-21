package com.example.smartroom.ui.notifications;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.example.smartroom.R;
import com.example.smartroom.common.ClockView2;
import com.example.smartroom.Service.SocketService;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import java.util.ArrayList;
import java.util.List;


public class NotificationsFragment extends Fragment {
    com.github.mikephil.charting.charts.LineChart mylineChart;
    private ClockView2 CPU_temp;
    private ClockView2 RAM_USED;
    private ClockView2 ROM_USED;
    private ClockView2 CPU_USED;
    ToggleButton bt_connect = null;
    TextView txt;
    private float cpu_temp;
    private float RAM;
    private float ROM;
    private float cpu_used;
    float[] all;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
//获取控件
        txt = root.findViewById(R.id.tv_text);
        bt_connect = root.findViewById(R.id.bt_connect);
        CPU_temp = root.findViewById(R.id.CPU_temp);
        CPU_USED = root.findViewById(R.id.CPU_used);
        RAM_USED = root.findViewById(R.id.RAM_used);
        ROM_USED = root.findViewById(R.id.ROM_used);
        //为ToggleButton控件绑定监听器
        bt_connect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Intent intent = new Intent(getActivity(), SocketService.class);
                    getActivity().bindService(intent, serviceConnection, Service.BIND_AUTO_CREATE);
                            // 执行完毕后给handle,handl来改变UI组件
                            handler.sendEmptyMessage(0);
                } else {    // 停止链接Socket
                    getActivity().unbindService(serviceConnection);
                }
            }
        });
        //利用安卓存储树莓派温度制成温度折线图。
        mylineChart = root.findViewById(R.id.temp_line);
        initLineChart(mylineChart);
        return root;
    }
    // 定义Handler对象
    private Handler handler = new Handler() {
        @Override
        // 当有消息发送出来的时候就执行Handler的这个方法
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // 处理UI
            CPU_temp.setCompleteDegree(cpu_temp);
            CPU_USED.setCompleteDegree(cpu_used);
            RAM_USED.setCompleteDegree(RAM);
            ROM_USED.setCompleteDegree(ROM);
            txt.setText("ok");
            //CPU_temp.setCompleteDegree(line2);
            Log.i("MSG", "收到消息----->" + cpu_temp +"   "+ RAM +"   "+ ROM );
        }
    };


    public void initLineChart(com.github.mikephil.charting.charts.LineChart mylineChart){
        List<Entry> entry = new ArrayList<Entry>();
        Entry c1e1 = new Entry(10, 0);
        Entry c1e2 = new Entry(20, 5);
        Entry c1e3 = new Entry(30, 10);
        Entry c1e4 = new Entry(40, 15);

        entry.add(c1e1);
        entry.add(c1e2);
        entry.add(c1e3);
        entry.add(c1e4);
        LineDataSet data = new  LineDataSet(entry,"linechart");
        LineData lineData = new LineData(data);
        mylineChart.setData(lineData);
    }


    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            SocketService mService = ((SocketService.MyBinder) service).getService();
                mService.getJSon();
                all = mService.returnfloat();
                cpu_temp = all[0];
                cpu_used = all[1];
                RAM  =all[2];
                ROM=all[3];
            System.out.println(mService.getStatus());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("test_out", "----->onServiceDisconnected");
        }
    };
}



