package com.bethejustice.myapplication4;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetworkStatus {

    public static final int TYPE_WIFI =1;
    public static final int TYPE_MOBILE =2;
    public static final int TYPE_NOT_CONNECTED =3;
    Context context;

    public NetworkStatus(Context context){
        this.context = context;
    }


    public int getConnectivityStatus(){
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if(networkInfo != null){
            int type = networkInfo.getType();
            if(type == ConnectivityManager.TYPE_MOBILE){
                return TYPE_MOBILE;
            } else if (type == ConnectivityManager.TYPE_WIFI){
                return TYPE_WIFI;
            }
        }
        return TYPE_NOT_CONNECTED;
    }

    public int checkNetworkConnection(){

        int networkStatus = getConnectivityStatus();

        if(networkStatus == NetworkStatus.TYPE_MOBILE){
            Toast.makeText(context, "인터넷이 연결되었습니다.", Toast.LENGTH_LONG).show();
        } else if(networkStatus == NetworkStatus.TYPE_WIFI){
            Toast.makeText(context, "인터넷이 연결되었습니다.", Toast.LENGTH_LONG).show();
        } else{
            Toast.makeText(context, "인터넷이 연결되지 않았습니다.", Toast.LENGTH_LONG).show();
        }

        return networkStatus;
    }
}
