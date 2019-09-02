package id.co.telkomsigma.Diarium.controller.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import id.co.telkomsigma.Diarium.R;

public class FullFotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_full_foto);
        Intent intent = getIntent();
        String avatar = intent.getStringExtra("avatar");
        ImageView ivProfile = findViewById(R.id.ivProfile);
        Picasso.get().load(avatar).error(R.drawable.profile).into(ivProfile);
    }
}
