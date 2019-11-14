package com.driver.hire_me;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

public class ThankyouActivity extends FragmentActivity {
    private Controller controller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thankyou);
        getActionBar().hide();
        controller= (Controller) getApplication();
        findViewById(R.id.submit_but).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.pref.setTRIP_STATUS2("no");
                Intent intent=new Intent(ThankyouActivity.this,SlideMainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
