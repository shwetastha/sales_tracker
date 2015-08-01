package com.teletalk.salestrackerdata.salestrackerdata;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ToggleButton;

import java.io.IOException;


public class MainActivity extends Activity {
    ToggleButton tb1;
    EditText timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button = (Button) findViewById(R.id.button);
        String msgSentStatus = AndroidUtils.getfileContent(getApplicationContext(), AndroidUtils.MSG_STATUS_FILE, AndroidUtils.MSG_STATUS_N);
        String toggleStatus = AndroidUtils.getfileContent(getApplicationContext(), AndroidUtils.TOGGLE_STATUS_FILE, AndroidUtils.TOGGLE_STATUS_ENABLED);

        tb1 = (ToggleButton) findViewById(R.id.tg1);
        timer = (EditText) findViewById(R.id.timer);

        timer.setText(AndroidUtils.getTimerHome(getApplicationContext()));

        tb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    timer.setEnabled(true);
                    button.setEnabled(true);
                    try {
                        AndroidUtils.writeToFile(getApplication(), AndroidUtils.TOGGLE_STATUS_FILE, AndroidUtils.TOGGLE_STATUS_ENABLED);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // The toggle is disabled
                    timer.setEnabled(false);
                    button.setEnabled(false);
                    try {
                        AndroidUtils.writeToFile(getApplication(), AndroidUtils.TOGGLE_STATUS_FILE, AndroidUtils.TOGGLE_STATUS_DISABLED);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        if (tb1.isChecked()) {
            timer.setEnabled(true);
            button.setEnabled(true);

        } else {
            timer.setEnabled(false);
            button.setEnabled(false);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent service = new Intent(getApplicationContext(), Network.class);
                getApplicationContext().startService(service);
//                AndroidUtils.getPhNo(getApplication());
            }
        });


        timer.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                try {
                    AndroidUtils.writeToFile(getApplicationContext(), "timer", String.valueOf(s));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });

    }


    @Override
    protected void onResume() {
        String toggle_status = AndroidUtils.getfileContent(getApplicationContext(), AndroidUtils.TOGGLE_STATUS_FILE, "");
        Log.w("SalesTracker:HOME", "Toggle statuss:" + toggle_status);
        if (toggle_status.equalsIgnoreCase(AndroidUtils.TOGGLE_STATUS_ENABLED)) {
            tb1.setChecked(true);
        } else if (toggle_status.equalsIgnoreCase(AndroidUtils.TOGGLE_STATUS_DISABLED)) {
            tb1.setChecked(false);
        }
        super.onResume();
    }
}
