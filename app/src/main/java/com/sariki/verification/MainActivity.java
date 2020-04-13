package com.sariki.verification;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.sariki.fastverification.VerificationLayout;
import com.sariki.fastverification.VerificationType;
import com.sariki.fastverification.VerificationTypeListener;

public class MainActivity extends AppCompatActivity {
    private VerificationLayout test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        test = findViewById(R.id.test);
        test.init(this, VerificationType.LENGTH_LONG);
        test.setEndListener(new VerificationTypeListener() {
            @Override
            public void onFinish(String result) {
                    test.setErrType();
            }
        });
    }
}
