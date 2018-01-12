package com.example.sunilm.asyncdemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends Activity {
    Handler handler;
    TextView tv1;
    SeekBar numberOfpass;
    SeekBar lengthOfpass;
    TextView tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        numberOfpass = (SeekBar) findViewById(R.id.seekBar3);
        ;
        lengthOfpass = (SeekBar) findViewById(R.id.seekBar4);
        tv1 = (TextView) findViewById(R.id.NumberOfPasswords);


        tv2 = (TextView) findViewById(R.id.selectLengthOfpasswords);
        numberOfpass.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                                    @Override
                                                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                                                        tv1.setText("NumberOfPasswords: " + numberOfpass.getProgress());

                                                    }

                                                    @Override
                                                    public void onStartTrackingTouch(SeekBar seekBar) {

                                                    }

                                                    @Override
                                                    public void onStopTrackingTouch(SeekBar seekBar) {

                                                    }
                                                }
        );

        lengthOfpass.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                tv2.setText("NumberOfPasswords: " + lengthOfpass.getProgress());

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        handler = new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                if (msg.getData().containsKey("output")) {
                    msg.getData().getStringArrayList("output");
                }
                return false;
            }
        });

        Button asynctask = (Button) findViewById(R.id.button4);
        asynctask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int k = numberOfpass.getProgress();
                int l = lengthOfpass.getProgress();
                new DoWork().execute(k, l);

            }
        });

        Button threadTask = (Button) findViewById(R.id.button5);
        threadTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new DOWorkByThread());
                thread.start();

            }
        });

    }

    class DoWork extends AsyncTask<Integer, Integer, ArrayList> {
        static final String y = "sada";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected ArrayList doInBackground(Integer... params) {

            int kkkk = params[0];
            int lll = params[1];

            ArrayList<String> sum = new ArrayList<>();
            PasswordGenerator passwordGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
                    .useDigits(true)
                    .useLower(true)
                    .useUpper(true)
                    .build();

            for (int i = 1; i < kkkk; i++) {
                String password = passwordGenerator.generate(lll);
                sum.add(password);
                publishProgress(i + 1);
            }
            return sum;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            // progressd.setProgress(values[0]);

        }

        @Override
        protected void onPostExecute(ArrayList aVoid) {
            // progressd.dismiss();
            Log.d("demo", "result is " + aVoid);
            CharSequence[] cs = (CharSequence[]) aVoid.toArray(new CharSequence[aVoid.size()]);

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Pick a color").
                    setItems(cs, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            Log.d("demo", "");
                        }
                    });

            final AlertDialog singleItemAlert = builder.create();
            singleItemAlert.show();
            super.onPostExecute(aVoid);
        }

    }
    class DOWorkByThread implements Runnable {
        static final int STATUS_START = 0x00;
        static final int STATUS_STOP = 0x01;
        static final int STATUS_RUNNING = 0x02;

        @Override
        public void run() {
            PasswordGenerator passwordGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
                    .useDigits(true)
                    .useLower(true)
                    .useUpper(true)
                    .build();
            Bundle bundle = new Bundle();

            ArrayList<String> nnnn = new ArrayList<>();
            Message mg = new Message();
            mg.what = STATUS_START;

            handler.sendMessage(mg);
            for (int i = 0; i < 100; i++) {
                String password = passwordGenerator.generate(8);
                nnnn.add(password);

            }

            mg = new Message();
            mg.what = STATUS_STOP;
            handler.sendMessage(mg);
            bundle.putStringArrayList("output", nnnn);
            Log.d("demo", "xyz");
        }
    }
}
