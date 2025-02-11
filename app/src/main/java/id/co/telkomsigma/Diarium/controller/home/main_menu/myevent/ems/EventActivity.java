package id.co.telkomsigma.Diarium.controller.home.main_menu.myevent.ems;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import id.co.telkomsigma.Diarium.R;
import id.co.telkomsigma.Diarium.adapter.MenuAdapter;
import id.co.telkomsigma.Diarium.controller.home.main_menu.myevent.ems.content.ContentActivity;
import id.co.telkomsigma.Diarium.controller.home.main_menu.myevent.ems.feedback.FeedbackActivity;
import id.co.telkomsigma.Diarium.controller.home.main_menu.myevent.ems.gallery.GalleryActivity;
import id.co.telkomsigma.Diarium.controller.home.main_menu.myevent.ems.news.NewsActivity;
import id.co.telkomsigma.Diarium.controller.home.main_menu.myevent.ems.presence.PresenceActivity;
import id.co.telkomsigma.Diarium.controller.home.main_menu.myevent.ems.rundown.NewAgendaActivity;
import id.co.telkomsigma.Diarium.controller.home.main_menu.myevent.ems.venue.VenueActivity;

public class EventActivity extends AppCompatActivity {

    GridView gridview;
    ImageView ivBack, ivEvent;

    String[] menu ={
            "News",
            "Rundown",
            "Content",
            "Venue",
            "Gellery",
            "Feedback",
            "Presencce"
    };


    int[] image={
            R.drawable.news,
            R.drawable.rundown,
            R.drawable.content,
            R.drawable.venue,
            R.drawable.gellery,
            R.drawable.feedback,
            R.drawable.icon_presence
    };
    Typeface font,fontbold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
        String gambar = intent.getStringExtra("image");
        gridview = (GridView) findViewById(R.id.gridview);
//        ivBack = (ImageView) findViewById(R.id.ivBack);
        ivEvent = (ImageView) findViewById(R.id.ivEvent);
        if (gambar.isEmpty()) {
            ivEvent.setImageResource(R.drawable.profile);
        } else{
            Picasso.get().load(gambar).error(R.drawable.profile).into(ivEvent);
        }

        MenuAdapter gridAdapter = new MenuAdapter(EventActivity.this, image, menu);
        gridview.setAdapter(gridAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i;
                switch (position) {
                    case 0:
                        i = new Intent(EventActivity.this, NewsActivity.class);
                        startActivity(i);
                        break;
                    case 1:
                        i = new Intent(EventActivity.this, NewAgendaActivity.class);
                        startActivity(i);
                        break;
                    case 2:
                        i = new Intent(EventActivity.this, ContentActivity.class);
                        startActivity(i);
                        break;
                    case 3:
                        i = new Intent(EventActivity.this, VenueActivity.class);
                        startActivity(i);
                        break;
                    case 4:
                        i = new Intent(EventActivity.this, GalleryActivity.class);
                        startActivity(i);
                        break;
                    case 5:
                        i = new Intent(EventActivity.this, FeedbackActivity.class);
                        startActivity(i);
                        break;
                    case 6:
                        i = new Intent(EventActivity.this, PresenceActivity.class);
                        startActivity(i);
                        break;
                }
            }
        });
//
//        ivBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(EventActivity.this, MyEventActivity.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(i);
//                finish();
//            }
//        });

        font = Typeface.createFromAsset(getAssets(), "fonts/Nexa Light.otf");
        fontbold = Typeface.createFromAsset(getAssets(), "fonts/Nexa Bold.otf");

//        TextView txtTitle = findViewById(R.id.name);
//        txtTitle.setTypeface(fontbold);
//        txtTitle.setText(name);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(name);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
