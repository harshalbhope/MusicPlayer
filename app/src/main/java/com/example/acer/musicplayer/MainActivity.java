package com.example.acer.musicplayer;

import android.content.ContentResolver;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    ListView lvMusic;
    Button btnStop, btnPause;
    ImageButton ibtnPrevious, ibtnForward, ibtnNext, ibtnBack;
    String[] name, path;
    MediaPlayer mp;
    static int cs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvMusic = (ListView)findViewById(R.id.lvMusic);
        btnStop = (Button)findViewById(R.id.btnStop);
        btnPause = (Button)findViewById(R.id.btnPause);
        ibtnBack = (ImageButton)findViewById(R.id.ibtnBack);
        ibtnForward = (ImageButton)findViewById(R.id.ibtnForward);
        ibtnNext = (ImageButton)findViewById(R.id.ibtnNext);
        ibtnPrevious = (ImageButton)findViewById(R.id.ibtnPrevious);
        mp = new MediaPlayer();

        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);

        name = new String[cursor.getCount()];
        path = new String[cursor.getCount()];

        int i = 0;

        while (cursor.moveToNext()){

            name[i] = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
            path[i] = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            i++;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, name);

        lvMusic.setAdapter(adapter);

        lvMusic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                try {
                    cs = 1;
                    String p = path[i];
                    mp.reset();
                    mp.setDataSource(p);
                    mp.prepare();
                    mp.start();

                }catch (IOException e){
                    e.printStackTrace();
                }


            }


        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.stop();
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mp.isPlaying()){
                    mp.pause();
                    btnPause.setText("Resume");
                }
                else {
                    mp.start();
                    btnPause.setText("Pause");
                }
            }
        });

        ibtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.seekTo(mp.getCurrentPosition() - 3000);
            }
        });

        ibtnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.seekTo(mp.getCurrentPosition() + 3000);
            }
        });

        ibtnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int ns = cs- 1;
                if (ns == -1){
                    Toast.makeText(MainActivity.this, "First Song", Toast.LENGTH_SHORT).show();
                    return;
                }
                 try {
                        cs = ns;
                        String p = path[cs];
                        mp.reset();
                        mp.setDataSource(p);
                        mp.prepare();
                        mp.start();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

            }
        });

        ibtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int ns = cs + 1;
                if (ns == name.length){
                    Toast.makeText(MainActivity.this, "Last Song", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    cs = ns;
                    String p = path[cs];
                    mp.reset();
                    mp.setDataSource(p);
                    mp.prepare();
                    mp.start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
