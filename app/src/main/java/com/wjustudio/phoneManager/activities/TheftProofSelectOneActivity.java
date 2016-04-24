package com.wjustudio.phoneManager.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wjustudio.phoneManager.R;

/**
 * songwenju on 16-4-19 : 13 : 48.
 * 邮箱：songwenju@outlook.com
 */
public class TheftProofSelectOneActivity extends Activity {
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proof_setup_one);
        mContext = this;
        Button btn_next = (Button) findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,TheftProofSelectTwoActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


}
