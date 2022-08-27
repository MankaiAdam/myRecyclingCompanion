package com.example.myrecyclingcompanion;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.security.acl.Group;

public class HomeActivity extends AppCompatActivity {

    ImageButton overview_btn,maps_btn;
    Button scan_btn;
    ConstraintLayout overview_grp,maps_grp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        overview_btn = findViewById(R.id.overview_btn);
        overview_btn.setImageDrawable(getDrawable(R.drawable.mo_activated));
        maps_btn = findViewById(R.id.challenges_btn);
        maps_btn.setImageDrawable(getDrawable(R.drawable.map_disabled));
        scan_btn = findViewById(R.id.scan_btn1);

        scan_btn = findViewById(R.id.scan_btn1);

        overview_btn.setOnClickListener(view -> {
        });
        maps_btn.setOnClickListener(view -> {
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

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(),result->{
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
            Log.d("tag",result.getContents());
        }
    });
}