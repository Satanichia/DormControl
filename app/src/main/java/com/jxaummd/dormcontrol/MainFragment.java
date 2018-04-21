package com.jxaummd.dormcontrol;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainFragment extends Fragment {

    AppPref mPref = AppPref.getInstance();
    MqttRequest mReq = MqttRequest.getInstance(
            mPref.getPref(AppPref.tControlAddr),
            AppPref.deviceId,
            mPref.getPref(AppPref.tUserName),
            mPref.getPref(AppPref.tPassWord));

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button btn_on = getActivity().findViewById(R.id.button1);

        btn_on.setOnClickListener(v -> {
            mReq.setMsg(mPref.getPref(AppPref.tSwMsg));
            mReq.connect(mPref.getPref(AppPref.tUuid), new MqttCallbackExtended() {
                @Override
                public void connectComplete(boolean reconnect, String serverURI) {
                    Log.d("MqttRequest",
                            "Connect complete. reconnect:"+reconnect+" url:"+serverURI);
                }

                @Override
                public void connectionLost(Throwable cause) {
                    Log.d("MqttRequest",
                            "Connection lost.");
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    Log.d("MqttRequest",
                            "Message arrived. topic:"+topic+" message:"+message);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    Log.d("MqttRequest","Delivery complete");
                }
            });
        });

    }
}
