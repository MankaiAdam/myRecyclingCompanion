package com.example.myrecyclingcompanion;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class ShopActivity extends AppCompatActivity {

    int total_points = 2564;
    int tokens = 1;

    ImageButton overview_btn,maps_btn;
    Button scan_btn;
    ImageButton back_btn;
    TextView tokens_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        back_btn = findViewById(R.id.back_btn);
        scan_btn = findViewById(R.id.scan_btn);
        tokens_text = findViewById(R.id.tokens_text);

        update_info();
        back_btn.setOnClickListener(view -> {
            startActivity(new Intent(ShopActivity.this, ShopActivity.class));
        });
        scan_btn.setOnClickListener(view -> {
            scan_code();
        });
    }
    public void scan_code(){
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(false);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }
    public void update_info(){
        tokens_text.setText(tokens+"");
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result->{
        if(result.getContents() != null){
            /*AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            builder.setTitle("result");
            builder.setMessage(result.getContents());
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();*/
            String content = result.getContents();
            total_points +=  Integer.parseInt(content.substring(content.indexOf('_') + 1));
            update_info();
            Log.d("tag",result.getContents());
        }
    });
}
