package com.example.smartroom.ui.dashboard;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.smartroom.R;
import com.example.smartroom.Service.SocketService;
import com.suke.widget.SwitchButton;

                    public class DashboardFragment extends Fragment {
                        private com.suke.widget.SwitchButton allSwitch;
                        private com.suke.widget.SwitchButton button1;
                        private com.suke.widget.SwitchButton button2;
                        private com.suke.widget.SwitchButton button3;
                        private SocketService mService;
                        private WebView webview;
                        public View onCreateView(@NonNull LayoutInflater inflater,
                                                 ViewGroup container, Bundle savedInstanceState) {
                            View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
                            webview = (WebView) root.findViewById(R.id.webview);
                            load();
                            allSwitch = root.findViewById(R.id.openSerview);
                            button1 = root.findViewById(R.id.Light);
                            button2 = root.findViewById(R.id.AC);
                            button3 = root.findViewById(R.id.Flash);
                            allSwitch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                                    if (isChecked){
                    Intent intent = new Intent(getActivity(), SocketService.class);
                    getActivity().bindService(intent, serviceConnection, Service.BIND_AUTO_CREATE);
                }else{
                    getActivity().unbindService(serviceConnection);
                }
            }
        });

        button1.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked){
                          mService.OpenLight();
                    Toast.makeText(getContext(),"已开灯",Toast.LENGTH_SHORT);
                }else{
                    mService.DisOPenlight();
                    Toast.makeText(getContext(),"已关灯",Toast.LENGTH_SHORT);
                }
            }
        });
        button2.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked){
                    mService.OpenAC();
                    Toast.makeText(getContext(),"已开空调",Toast.LENGTH_SHORT);
                }else{
                    mService.DisOpenAc();
                    Toast.makeText(getContext(),"已关空调",Toast.LENGTH_SHORT);
                }
            }
        });
        button3.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked){
                    mService.OpenFlash();
                    Toast.makeText(getContext(),"已开电源",Toast.LENGTH_SHORT);
                }else{
                   mService.DisOpenFlash();
                    Toast.makeText(getContext(),"已关闭总电源",Toast.LENGTH_SHORT);
                }
            }
        });

        return root;
    }


    private void load() {
        WebSettings webviewSettings = webview.getSettings();
        webviewSettings.setJavaScriptEnabled(true);
        webviewSettings.setJavaScriptEnabled(true);
        webviewSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webviewSettings.setAllowFileAccess(true);// 设置允许访问文件数据
        webviewSettings.setSupportZoom(true);
        webviewSettings.setBuiltInZoomControls(true);
        webviewSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webviewSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webviewSettings.setDomStorageEnabled(true);
        webviewSettings.setDatabaseEnabled(true);
        //设置载入页面自适应手机屏幕，居中显示
        webviewSettings.setUseWideViewPort(true);
        webviewSettings.setLoadWithOverviewMode(true);
//        webview.setWebViewClient(new WebViewClient());
        webview.setWebChromeClient(new WebChromeClient());
        webview.loadUrl("192.168.42.160:8080");
    }
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = ((SocketService.MyBinder) service).getService();
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("test_out", "----->onServiceDisconnected");
        }
    };

}


