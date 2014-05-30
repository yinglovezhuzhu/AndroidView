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

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.opensource.widget.CircleProgressBar;
import com.opensource.widget.R;


/**
 * Use:
 * Created by yinglovezhuzhu@gmail.com on 2014-05-23.
 */
public class CircleProgressBarActivity extends Activity {

    public static final int MSG_UPDAGE_PROGRESS = 0x100;

    private CircleProgressBar mCpb;
    private MainHandler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_progressbar);

        mCpb = (CircleProgressBar) findViewById(R.id.cpb_progress);
        mCpb.setProgress(0);
        mCpb.setMaxProgress(100);

        mHandler = new MainHandler();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mHandler != null) {
            mHandler = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeMessages(MSG_UPDAGE_PROGRESS);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.sendEmptyMessageDelayed(MSG_UPDAGE_PROGRESS, 100);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    class MainHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDAGE_PROGRESS:
                    int progress = mCpb.getProgress();
                    int maxProgress = mCpb.getMaxProgress();
                    if(progress == maxProgress) {
                        progress = 0;
                        mCpb.setProgress(progress);
                        mHandler.sendEmptyMessageDelayed(MSG_UPDAGE_PROGRESS, 1000);
                        return;
                    } else if(progress + 1 > maxProgress) {
                        progress = maxProgress;
                        mCpb.setProgress(progress);
                    } else {
                        mCpb.setProgress(progress + 1);
                    }
                        mHandler.sendEmptyMessageDelayed(MSG_UPDAGE_PROGRESS, 100);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }

}
