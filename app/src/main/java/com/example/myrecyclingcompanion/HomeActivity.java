package com.example.myrecyclingcompanion;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.security.acl.Group;

public class HomeActivity extends AppCompatActivity {

    Button overview_btn,challenges_btn,scan_btn;
    ConstraintLayout overview_grp,challenges_grp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        overview_btn = findViewById(R.id.overview_btn);
        challenges_btn = findViewById(R.id.challenges_btn);
        scan_btn = findViewById(R.id.scan_btn);
        overview_grp = findViewById(R.id.overview_grp);
        challenges_grp = findViewById(R.id.challenges_grp);
        scan_btn = findViewById(R.id.scan_btn);

        overview_btn.setOnClickListener(view -> {
            overview_grp.setVisibility(View.VISIBLE);
            challenges_grp.setVisibility(View.INVISIBLE);
        });
        challenges_btn.setOnClickListener(view -> {
            overview_grp.setVisibility(View.INVISIBLE);
            challenges_grp.setVisibility(View.VISIBLE);
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