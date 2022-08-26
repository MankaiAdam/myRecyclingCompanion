package com.example.myrecyclingcompanion;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class HomeActivity extends AppCompatActivity {

    int total_points = 2564;
    int tokens = 1;
    int donated_plastic = 20;
    int points = 562;
    int hot_streak = 3;
    Button overview_btn,challenges_btn,scan_btn;
    ConstraintLayout overview_grp,challenges_grp;
    TextView tokens_text, total_points_text, donated_plastic_text, points_text, hot_streak_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        overview_btn = findViewById(R.id.overview_btn);
        challenges_btn = findViewById(R.id.map_btn);
        scan_btn = findViewById(R.id.scan_btn);
        overview_grp = findViewById(R.id.overview_grp);
        challenges_grp = findViewById(R.id.challenges_grp);
        tokens_text = findViewById(R.id.tokens_text);
        total_points_text = findViewById(R.id.total_points_text);
        donated_plastic_text = findViewById(R.id.donated_plastic_text);
        points_text = findViewById(R.id.points_text);
        hot_streak_text = findViewById(R.id.hot_streak_text);
        scan_btn = findViewById(R.id.scan_btn);

        update_info();

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
    public void update_info(){
        tokens_text.setText(tokens+"");
        total_points_text.setText(total_points+"");
        donated_plastic_text.setText(donated_plastic + "");
        points_text.setText(points+"");
        hot_streak_text.setText(hot_streak+"");
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
            String content = result.getContents();
            total_points +=  Integer.parseInt(content.substring(content.indexOf('_') + 1));
            donated_plastic +=  Integer.parseInt(content.substring(0,content.indexOf('_')));
            points +=  Integer.parseInt(content.substring(content.indexOf('_') + 1));
            update_info();
            Log.d("tag",result.getContents());
            //
        }
    });
}