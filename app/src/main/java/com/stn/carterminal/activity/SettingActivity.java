package com.stn.carterminal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.stn.carterminal.common.GlobalVariable;
import com.stn.carterminal.R;
import com.stn.carterminal.constant.Constant;
import com.stn.carterminal.helper.TakeruHelper;

public class SettingActivity extends AppCompatActivity {

    private static final String MESSAGE_HOST_NOT_BLANK = "Field 'Host' must not be empty.";
    private static final String MESSAGE_INVALID_HOST = "Invalid Host/IP Address.";

    private GlobalVariable globalVariable;
    private String host;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        globalVariable = (GlobalVariable) getApplicationContext();
        setHost();
        setOnClickSubmitButton();
    }

    private void setHost() {
        if (globalVariable.getHost() != null && !globalVariable.getHost().isEmpty()) {
            this.host = globalVariable.getHost();
        }
        ((EditText) findViewById(R.id.host)).setText(this.host);
    }

    private void setOnClickSubmitButton() {
        Button save = findViewById(R.id.saveBtnSetting);
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String inputHost = ((EditText) findViewById(R.id.host)).getText().toString();
                if (inputHost.isEmpty()) {
                    Toast.makeText(getApplicationContext(), MESSAGE_HOST_NOT_BLANK, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!TakeruHelper.validateHost(inputHost)) {
                    Toast.makeText(getApplicationContext(), MESSAGE_INVALID_HOST, Toast.LENGTH_SHORT).show();
                    return;
                }

                globalVariable.setHost(inputHost);
                Toast.makeText(getApplicationContext(), Constant.SUCCESS_SAVE_DATA_MESSAGE, Toast.LENGTH_SHORT).show();

                Intent signIn = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(signIn);
            }
        });
    }
}
