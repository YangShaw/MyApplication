package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.R;

public class TestActivity extends AppCompatActivity {

//    Button infoBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
//        infoBtn = findViewById(R.id.btn_test_info);

        Intent intent = getIntent();
//        String we = intent.getStringExtra("count");
//
//        infoBtn.setText(we);
//
//
////        final int data2 = data++;
//        infoBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent2 = new Intent(TestActivity.this, MainActivity.class);
//                intent2.putExtra("data_return", "from btn for main");
//                setResult(RESULT_OK, intent2);
//                finish();
////                startActivity(intent2);
//            }
//        });

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent3 = new Intent();
        intent3.putExtra("data_return", "from back for main");
        setResult(RESULT_OK, intent3);
        finish();
    }
}
