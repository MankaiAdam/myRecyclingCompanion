package com.example.myrecyclingcompanion;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.RequiresApi;
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
    
    ImageButton overview_btn,maps_btn;
    Button scan_btn, shop_btn;
    ConstraintLayout overview_grp,challenges_grp;
    TextView tokens_text, total_points_text, donated_plastic_text, points_text, hot_streak_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Window w = getWindow();
        w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        w.setStatusBarColor(Color.rgb(252,252,252));
        overview_btn = findViewById(R.id.overview_btn);
        maps_btn = findViewById(R.id.map_btn);
        shop_btn = findViewById(R.id.shop_btn);
        scan_btn = findViewById(R.id.scan_btn);
        tokens_text = findViewById(R.id.tokens_text);
        total_points_text = findViewById(R.id.total_points_text);
        donated_plastic_text = findViewById(R.id.donated_plastic_text);
        points_text = findViewById(R.id.points_text);
        ImageView widget = (ImageView) findViewById(R.id.mainwidget);
        maps_btn = findViewById(R.id.map_btn);
        overview_btn.setImageDrawable(getDrawable(R.drawable.mo_activated));
        widget.setImageDrawable(getDrawable(R.drawable.mo));
        donated_plastic_text.setVisibility(View.VISIBLE);
        points_text.setVisibility(View.VISIBLE);

        maps_btn.setImageDrawable(getDrawable(R.drawable.map_disabled));

        update_info();

        overview_btn.setOnClickListener(view -> {
            overview_btn.setImageDrawable(getDrawable(R.drawable.mo_activated));
            widget.setImageDrawable(getDrawable(R.drawable.mo));
            maps_btn.setImageDrawable(getDrawable(R.drawable.map_disabled));
            donated_plastic_text.setVisibility(View.VISIBLE);
            points_text.setVisibility(View.VISIBLE);
        });
        maps_btn.setOnClickListener(view -> {
            overview_btn.setImageDrawable(getDrawable(R.drawable.mo_disabled));
            widget.setImageDrawable(getDrawable(R.drawable.map));
            maps_btn.setImageDrawable(getDrawable(R.drawable.map_activated));
            donated_plastic_text.setVisibility(View.INVISIBLE);
            points_text.setVisibility(View.INVISIBLE);
        });
        shop_btn.setOnClickListener(view -> {
            startActivity(new Intent(HomeActivity.this, ShopActivity.class));
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
        //hot_streak_text.setText(hot_streak+"");
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
        }
    });
}