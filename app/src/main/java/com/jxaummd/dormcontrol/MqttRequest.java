package com.jxaummd.dormcontrol;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttRequest {

    private static MqttRequest instance;

    private String mSvrAddr;
    private String mDeviceId;
    private MqttConnectOptions options;
    private MqttMessage mMessage;

    private MqttRequest(String svrAddr,
                        String deviceId,
                        String userName,
                        String passWord) {
        this.mSvrAddr = svrAddr;
        this.mDeviceId = deviceId;
        this.options = new MqttConnectOptions();
        options.setCleanSession(false);
        options.setUserName(userName);
        options.setPassword(passWord.toCharArray());
        options.setConnectionTimeout(10);// 设置超时时间
        options.setKeepAliveInterval(20);// 设置会话心跳时间
    }

    public static MqttRequest getInstance(String s, String d, String u, String p) {
        if (instance == null)
            instance = new MqttRequest(s, d, u, p);
        return instance;
    }

    public void connect(String topic, MqttCallback callback) {
        try {
            MqttClient client = new MqttClient(mSvrAddr, topic, new MemoryPersistence());
            client.setCallback(callback);
            client.connect(options);
            client.publish(mDeviceId, mMessage);//设置消息的topic，并发送。
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    void setMsg(String message) {
        this.mMessage = new MqttMessage(message.getBytes());
        mMessage.setQos(2);//设置消息发送质量，可为0,1,2.
        mMessage.setRetained(false);//服务器是否保存最后一条消息，若保存，client再次上线时，将再次受到上次发送的最后一条消息。
    }

}
