package com.jxaummd.dormcontrol;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SetFragment extends Fragment {

    AppPref mPref = AppPref.getInstance();

    HttpRequest hReq;
    String resData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_set, container, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (hReq == null)
            hReq = new HttpRequest();

        if (mPref.isFirstRun()) {
            mPref.setDefault();
            MqttRequest mReq = MqttRequest.getInstance(
                    mPref.getPref(AppPref.tControlAddr),
                    AppPref.deviceId,
                    mPref.getPref(AppPref.tUserName),
                    mPref.getPref(AppPref.tPassWord));
        }

        EditText roomEdit = getActivity().findViewById(R.id.et_roomId);
        Button getBtn = getActivity().findViewById(R.id.btn_getData);
        TextView resText = getActivity().findViewById(R.id.tv_resData);

        getBtn.setOnClickListener(v -> {
            if (mPref.isFirstRun()) mPref.genUuid();
            mPref.setRoomId(roomEdit.getText().toString());
            hReq.setContent(mPref.getPref(AppPref.tConfigAddr), AppPref.fConfigType, mPref.getPref(AppPref.tRoomId));
            hReq.pubRequest(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    resData = response.body().string();
                    Log.d("DevcHTTP", resData);
                }
            });
            resText.setText(resData);
        });


    }
}
