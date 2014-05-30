/*
 * Copyright (C) 2014 The Android Open Source Project.
 *
 *        yinglovezhuzhu@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.opensource.test;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.opensource.widget.MarkProgressBar;
import com.opensource.widget.R;

public class MarkProgressBarActivity extends ActionBarActivity implements View.OnClickListener{

    private static final int MSG_UPDATE_PROGRESS = 0x100;

    private MarkProgressBar mMpb;

    private Button mBtnStart;
    private Button mBtnPause;
    private Button mBtnBack;
    private Button mBtnReset;

    private MainHandler mHandler;

    private boolean mPausing = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_progress_bar);
        mMpb = (MarkProgressBar) findViewById(R.id.mpb_mark_progress);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        mMpb.setMaxProgress(dm.widthPixels);
        mMpb.setMinMask(dm.widthPixels / 4);

        mHandler = new MainHandler();

        mBtnStart = (Button) findViewById(R.id.btn_start);
        mBtnStart.setOnClickListener(this);
        mBtnPause = (Button) findViewById(R.id.btn_pause);
        mBtnPause.setOnClickListener(this);
        mBtnPause.setEnabled(false);
        mBtnBack = (Button) findViewById(R.id.btn_back);
        mBtnBack.setOnClickListener(this);
        mBtnBack.setEnabled(false);
        mBtnReset = (Button) findViewById(R.id.btn_reset);
        mBtnReset.setOnClickListener(this);
        mBtnReset.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                if(mPausing) {
                    mHandler.sendEmptyMessageDelayed(MSG_UPDATE_PROGRESS, 20);
                    mPausing = false;
                }
                break;
            case R.id.btn_pause:
                mHandler.removeMessages(MSG_UPDATE_PROGRESS);
                mMpb.pushSplit(mMpb.getProgress());
                mPausing = true;
                break;
            case R.id.btn_back:
                mMpb.deleteBack(true);
                break;
            case R.id.btn_reset:
                mMpb.setProgress(0);
                mPausing = true;
                break;
            default:
                break;
        }
        mBtnStart.setEnabled(mPausing);
        mBtnBack.setEnabled(mPausing && mMpb.getProgress() > 0);
        mBtnPause.setEnabled(!mPausing);
        mBtnReset.setEnabled(mPausing && mMpb.getProgress() > 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mark_progress_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class MainHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_PROGRESS:
                    int progress = mMpb.getProgress() + 1;
                    if(progress < mMpb.getMaxProgress()) {
                        mMpb.setProgress(progress);
                        mHandler.sendEmptyMessageDelayed(MSG_UPDATE_PROGRESS, 20);
                    } else {
                        mMpb.setProgress(mMpb.getMaxProgress());
                        mPausing = true;
                        mBtnBack.setEnabled(mPausing);
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
}
