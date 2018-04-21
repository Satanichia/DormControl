package com.jxaummd.dormcontrol;

import android.content.SharedPreferences;

import java.util.UUID;

public class AppPref {
    /*Instance*/
    private static AppPref instance;

    private SharedPreferences mPref;
    /*Preferences Data Tags*/
    public static final String tUuid = "uuid";
    public static final String tConfigAddr = "config_addr";
    public static final String tControlAddr = "ctrl_addr";
    public static final String tRoomId = "room_id";
    public static final String tUserName = "username";
    public static final String tPassWord = "password";
    public static final String tSwMsg = "msg_sw";
    public static final String tOnMsg = "msg_on";
    public static final String tOffMsg = "msg_off";
    /*固定的 数据*/
    public static final String fNewVerType = "chkupd";
    public static final String fStuNumType = "stuId";
    public static final String fConfigType = "roomId";
    /*储存的 不可修改的 数据*/
    private String pUuid;
    private String pConfigAddr;
    private String pControlAddr;
    private String pUserName;
    private String pPassWord;
    private String pSwMsg;
    private String pOnMsg;
    private String pOffMsg;
    /*储存的 可修改的 数据*/
    private String gRoomId;

    public static String deviceId = "s20001";

    private AppPref() {
        this.mPref = AppDormCtrl.getContext().getSharedPreferences("config", 0);
        reloadPref();
    }

    public static AppPref getInstance() {
        if (instance == null) {
            instance = new AppPref();
        }
        return instance;
    }

    public boolean isFirstRun() {
        //return (mPref.getString(tUuid, null) == null);
        return true;
    }

    public void setDefault() {
        mPref.edit()
                .putString(tConfigAddr, "http://r.ayaki.cn/rc/dget.php")
                .putString(tControlAddr, "tcp://r.ayaki.cn:1883")
                .putString(tUserName, "esp")
                .putString(tPassWord, "wifi")
                .putString(tSwMsg, "#sw")
                .putString(tOnMsg, "#on")
                .putString(tOffMsg, "#off")
                .apply();
        reloadPref();
    }

    public void genUuid() {
        String[] chars = new String[] { "a", "b", "c", "d", "e", "f",
                "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
                "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
                "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
                "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
                "W", "X", "Y", "Z" };
        StringBuilder shortBuffer = new StringBuilder();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        mPref.edit()
                .putString(tUuid, shortBuffer.toString())
                .apply();
    }

    public void setRoomId(String r) {
        mPref.edit()
                .putString(tRoomId, r)
                .apply();
        reloadPref();
    }


    public String getPref(String para) {
        switch (para) {
            case tUuid:
                return pUuid;
            case tConfigAddr:
                return pConfigAddr;
            case tControlAddr:
                return pControlAddr;
            case tUserName:
                return pUserName;
            case tPassWord:
                return pPassWord;
            case tSwMsg:
                return pSwMsg;
            case tOnMsg:
                return pOnMsg;
            case tOffMsg:
                return pOffMsg;

            case tRoomId:
                return gRoomId;

            default:
                return "";
        }
    }

    private void reloadPref() {
        pUuid = mPref.getString(tRoomId, "");
        pConfigAddr = mPref.getString(tConfigAddr, "");

        pControlAddr = mPref.getString(tControlAddr, "");
        gRoomId = mPref.getString(tRoomId, "");
        pUserName = mPref.getString(tUserName, "");
        pPassWord = mPref.getString(tPassWord, "");
        pSwMsg = mPref.getString(tSwMsg, "");
        pOnMsg = mPref.getString(tOnMsg, "");
        pOffMsg = mPref.getString(tOffMsg, "");
    }

}