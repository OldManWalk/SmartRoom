package com.example.smartroom.Service;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;

import com.example.smartroom.MainActivity;
import com.example.smartroom.R;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class SocketService extends Service {
    private boolean as = false;
    private DatagramSocket socket;
    private JSONObject js;
    private float[] all  = new float[]{0,0,0,0};
    private InetAddress address;
    private   int port=8080;
    private  byte[] data;
    private DatagramPacket packet;
    private DatagramPacket packet2;
    private String reply;
    @Nullable
    @Override
    public void onCreate() {
        super.onCreate();
        //开启一个线程，使服务可以连接到socket
        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    address=InetAddress.getByName("192.168.42.160");
                    data="给爷发消息".getBytes();
                    //2.创建数据报，包含发送的数据信息
                    packet=new DatagramPacket(data, data.length, address, port);
                    //3.创建DatagramSocket对象
                    socket=new DatagramSocket();

                    //4.向服务器端发送数据报
                    socket.send(packet);
                    byte[] data2=new byte[1024];
                    packet2=new DatagramPacket(data2, data2.length);
                    //2.接收服务器响应的数据
                    socket.receive(packet2);
                    //3.读取数据
                    reply=new String(data2, 0, packet2.getLength());
                    js = new JSONObject(reply);
                    //python端传输的json格式的信息，通过解析其中的信息获取温度等数据
                    String cpu_temp = js.getString("cpu_temp");
                    float a = Integer.parseInt(cpu_temp);
                    String cpu_used = js.getString("cpu_used");
                    float b = Integer.parseInt(cpu_used);
                    String ram = js.getString("ram");
                    float c = Integer.parseInt(ram);
                    String rom = js.getString("rom");
                    float d = Integer.parseInt(rom);
                    all = new float[]{a, b, c, d};
                    System.out.println("我是客户端，服务器说："+a);
                    as = true;
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    System.out.println("失败");
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("失败");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        Notification.Builder localBuilder = new Notification.Builder(this);
        localBuilder.setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0));
        localBuilder.setAutoCancel(false);
        localBuilder.setSmallIcon(R.mipmap.ic_launcher);
        localBuilder.setTicker("Foreground Service Start");
        localBuilder.setContentTitle("Socket服务端");
        localBuilder.setContentText("正在运行...");
        startForeground(1, localBuilder.getNotification());
        System.out.println("服务器被创建");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("服务器开始了");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if(as == true){
            socket.close();
            System.out.println("服务被摧毁了");
        as = false;
        }else {
            System.out.println("摧毁失败");
        }
        super.onDestroy();
    }


    public class MyBinder extends Binder {
        public SocketService getService(){
            return SocketService.this;
        }
    }

    private MyBinder sBinder = new MyBinder();

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("test_out","----->onBind");
        return sBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i("test_out","----->关闭了链接");
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.i("test_out","----->关闭了链接");
    }
    //获取目前socket开启状态
    public boolean getStatus(){
        return as;
    }

    public void getJSon() {
        new Thread(new Runnable(){
            @Override
            public void run() {
        try
            {
                address=InetAddress.getByName("192.168.42.160");
                data="设备信息".getBytes();
                packet=new DatagramPacket(data, data.length, address, port);
                socket.send(packet);
                byte[] data2=new byte[1024];
                packet2=new DatagramPacket(data2, data2.length);
                socket.receive(packet2);
                //3.读取数据
                reply=new String(data2, 0, packet2.getLength());
                js = new JSONObject(reply);
                //python端传输的json格式的信息，通过解析其中的信息获取温度等数据
                String cpu_temp = js.getString("cpu_temp");
                float a = Integer.parseInt(cpu_temp);
                String cpu_used = js.getString("cpu_used");
                float b = Integer.parseInt(cpu_used);
                String ram = js.getString("ram");
                float c = Integer.parseInt(ram);
                String rom = js.getString("rom");
                float d = Integer.parseInt(rom);
                all = new float[]{a, b, c, d};
            } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
            }
        }).start();
    }

public void OpenLight()
{
    new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                address=InetAddress.getByName("192.168.42.160");
                byte[] data="1".getBytes();
                //2.创建数据报，包含发送的数据信息
                packet=new DatagramPacket(data, data.length, address, port);
                //3.创建DatagramSocket对象
                //4.向服务器端发送数据报
                socket=new DatagramSocket();
                socket.send(packet);
                byte[] data2=new byte[1024];
                packet2=new DatagramPacket(data2, data2.length);
                //2.接收服务器响应的数据
                socket.receive(packet2);
                //3.读取数据
                String reply=new String(data2, 0, packet2.getLength());
                System.out.println(reply);
            }catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }).start();
}

public void DisOPenlight(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                InetAddress address= null;
                try {
                    address = InetAddress.getByName("192.168.42.160");
                    byte[] data="01".getBytes();
                    //2.创建数据报，包含发送的数据信息
                    DatagramPacket packet=new DatagramPacket(data, data.length, address, port);
                    //3.创建DatagramSocket对象
                    //4.向服务器端发送数据报
                    socket.send(packet);
                    byte[] data2=new byte[1024];
                    DatagramPacket packet2=new DatagramPacket(data2, data2.length);
                    //2.接收服务器响应的数据
                    socket.receive(packet2);
                    //3.读取数据
                    String reply=new String(data2, 0, packet2.getLength());
                    System.out.println(reply);
                }catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
}
public void OpenAC(){
    new Thread(new Runnable() {
        @Override
        public void run() {
            InetAddress address= null;
            try {
                address = InetAddress.getByName("192.168.42.160");
                byte[] data="2".getBytes();
                //2.创建数据报，包含发送的数据信息
                DatagramPacket packet=new DatagramPacket(data, data.length, address, port);
                //3.创建DatagramSocket对象
                //4.向服务器端发送数据报
                socket.send(packet);
                byte[] data2=new byte[1024];
                DatagramPacket packet2=new DatagramPacket(data2, data2.length);
                //2.接收服务器响应的数据
                socket.receive(packet2);
                //3.读取数据
                String reply=new String(data2, 0, packet2.getLength());
                System.out.println(reply);
            }catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }).start();
}
public void DisOpenAc(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                InetAddress address= null;
                try {
                    address = InetAddress.getByName("192.168.42.160");
                    byte[] data="02".getBytes();
                    //2.创建数据报，包含发送的数据信息
                    DatagramPacket packet=new DatagramPacket(data, data.length, address, port);
                    //3.创建DatagramSocket对象
                    //4.向服务器端发送数据报
                    socket.send(packet);
                    byte[] data2=new byte[1024];
                    DatagramPacket packet2=new DatagramPacket(data2, data2.length);
                    //2.接收服务器响应的数据
                    socket.receive(packet2);
                    //3.读取数据
                    String reply=new String(data2, 0, packet2.getLength());
                    System.out.println(reply);
                }catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
}
public void OpenFlash(){
    new Thread(new Runnable() {
        @Override
        public void run() {
            InetAddress address= null;
            try {
                address = InetAddress.getByName("192.168.42.160");
                byte[] data="3".getBytes();
                //2.创建数据报，包含发送的数据信息
                DatagramPacket packet=new DatagramPacket(data, data.length, address, port);
                //3.创建DatagramSocket对象
                //4.向服务器端发送数据报
                socket.send(packet);
                byte[] data2=new byte[1024];
                DatagramPacket packet2=new DatagramPacket(data2, data2.length);
                //2.接收服务器响应的数据
                socket.receive(packet2);
                //3.读取数据
                String reply=new String(data2, 0, packet2.getLength());
                System.out.println(reply);
            }catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }).start();
}
public void DisOpenFlash(){
    new Thread(new Runnable() {
        @Override
        public void run() {
            InetAddress address= null;
            try {
                address = InetAddress.getByName("192.168.42.160");
                byte[] data="03".getBytes();
                //2.创建数据报，包含发送的数据信息
                DatagramPacket packet=new DatagramPacket(data, data.length, address, port);
                //3.创建DatagramSocket对象
                //4.向服务器端发送数据报
                socket.send(packet);
                byte[] data2=new byte[1024];
                DatagramPacket packet2=new DatagramPacket(data2, data2.length);
                //2.接收服务器响应的数据
                socket.receive(packet2);
                //3.读取数据
                String reply=new String(data2, 0, packet2.getLength());
                System.out.println(reply);
            }catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }).start();
}

public float[] returnfloat(){
        return all;
}
    public void Connect (){

    }


}
