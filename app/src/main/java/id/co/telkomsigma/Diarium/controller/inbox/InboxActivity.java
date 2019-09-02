package id.co.telkomsigma.Diarium.controller.inbox;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import id.co.telkomsigma.Diarium.R;


public class InboxActivity extends AppCompatActivity {

    Typeface font,fontbold;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        font = Typeface.createFromAsset(getApplication().getAssets(),"fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getApplication().getAssets(),"fonts/Nexa Bold.otf");

        TextView a = (TextView) findViewById(R.id.tanggal);
        a.setTypeface(font);
        TextView a1 = (TextView) findViewById(R.id.judul);
        a1.setTypeface(fontbold);
        TextView a2 = (TextView) findViewById(R.id.tvDate);
        a2.setTypeface(font);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}

