package com.thefinestartist.ytpa.sample;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.crashlytics.android.Crashlytics;
import com.google.android.youtube.player.YouTubePlayer;
import com.squareup.picasso.Picasso;
import com.thefinestartist.ytpa.YouTubePlayerActivity;
import com.thefinestartist.ytpa.enums.Orientation;
import com.thefinestartist.ytpa.enums.Quality;
import com.thefinestartist.ytpa.utils.YouTubeThumbnail;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.Bind;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.thumbnail)
    ImageView thumbnail;
    @Bind(R.id.play_bt)
    ImageButton play;
    @Bind(R.id.player_style_bt)
    View playerStyleBt;
    @Bind(R.id.player_style_tv)
    TextView playerStyleTv;
    @Bind(R.id.screen_orientation_bt)
    View screenOrientationBt;
    @Bind(R.id.screen_orientation_tv)
    TextView screenOrientationTv;
    @Bind(R.id.volume_bt)
    View volumeBt;
    @Bind(R.id.volume_tv)
    TextView volumeTv;
    @Bind(R.id.animation_bt)
    View animationBt;
    @Bind(R.id.animation_tv)
    TextView animationTv;

    YouTubePlayer.PlayerStyle playerStyle;
    Orientation orientation;
    boolean showAudioUi;
    boolean showFadeAnim;

        //O ID do video  que é responsavel pela especificaçao de qual video sera reproduzido
    private static String VIDEO_ID = "N2Ionf4E7iw";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        playerStyle = YouTubePlayer.PlayerStyle.DEFAULT;
        orientation = Orientation.AUTO;
        showAudioUi = true;
        showFadeAnim = true;

        Picasso.with(this)
                .load(YouTubeThumbnail.getUrlFromVideoId(VIDEO_ID, Quality.HIGH))
                .fit()
                .centerCrop()
                .into(thumbnail);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, YouTubePlayerActivity.class);
                intent.putExtra(YouTubePlayerActivity.EXTRA_VIDEO_ID, VIDEO_ID);
                intent.putExtra(YouTubePlayerActivity.EXTRA_PLAYER_STYLE, playerStyle);
                intent.putExtra(YouTubePlayerActivity.EXTRA_ORIENTATION, orientation);
                intent.putExtra(YouTubePlayerActivity.EXTRA_SHOW_AUDIO_UI, showAudioUi);
                intent.putExtra(YouTubePlayerActivity.EXTRA_HANDLE_ERROR, true);
                if (showFadeAnim) {
                    intent.putExtra(YouTubePlayerActivity.EXTRA_ANIM_ENTER, R.anim.fade_in);
                    intent.putExtra(YouTubePlayerActivity.EXTRA_ANIM_EXIT, R.anim.fade_out);
                } else {
                    intent.putExtra(YouTubePlayerActivity.EXTRA_ANIM_ENTER, R.anim.modal_close_enter);
                    intent.putExtra(YouTubePlayerActivity.EXTRA_ANIM_EXIT, R.anim.modal_close_exit);
                }
                startActivityForResult(intent, 1);
            }
        });

        playerStyleBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(MainActivity.this)
                        .title(getString(R.string.player_style))
                        .items(getPlayerStyleNames())
                        .itemsCallbackSingleChoice(playerStyle.ordinal(), new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog materialDialog, View view, int which, CharSequence charSequence) {
                                playerStyle = YouTubePlayer.PlayerStyle.values()[which];
                                playerStyleTv.setText(playerStyle.name());
                                return true;
                            }
                        })
                        .positiveText(getString(R.string.choose))
                        .show();
            }
        });
            //As opções de orientação de tela será exibido quando o user seleciona essa alternativa
        screenOrientationBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(MainActivity.this)
                        .title(getString(R.string.screen_orientation))
                        .items(getScreenOrientationNames())
                        .itemsCallbackSingleChoice(orientation.ordinal(), new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog materialDialog, View view, int which, CharSequence charSequence) {
                                orientation = Orientation.values()[which];
                                screenOrientationTv.setText(orientation.name());
                                return true;
                            }
                        })
                        .positiveText(getString(R.string.choose))
                        .show();
            }
        });

        //Qual o user selecionar essa alternativa, opções relativas ao controle do audio serão exibidas.
        volumeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(MainActivity.this)
                        .title(getString(R.string.volume_ui_control))
                        .items(new String[]{getString(R.string.show), getString(R.string.dont_show)})
                        .itemsCallbackSingleChoice(showAudioUi ? 0 : 1, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog materialDialog, View view, int which, CharSequence charSequence) {
                                showAudioUi = which == 0;
                                volumeTv.setText(showAudioUi ? getString(R.string.show) : getString(R.string.dont_show));
                                return true;
                            }
                        })
                        .positiveText(getString(R.string.choose))
                        .show();
            }
        });

        animationBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(MainActivity.this)
                        .title(getString(R.string.animation_on_close))
                        .items(new String[]{getString(R.string.fade), getString(R.string.modal)})
                        .itemsCallbackSingleChoice(showFadeAnim ? 0 : 1, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog materialDialog, View view, int which, CharSequence charSequence) {
                                showFadeAnim = which == 0;
                                animationTv.setText(showFadeAnim ? getString(R.string.fade) : getString(R.string.modal));
                                return true;
                            }
                        })
                        .positiveText(getString(R.string.choose))
                        .show();
            }
        });
    }

    private String[] getScreenOrientationNames() {
        Orientation[] states = Orientation.values();
        String[] names = new String[states.length];
        for (int i = 0; i < states.length; i++)
            names[i] = states[i].name();
        return names;
    }

    private String[] getPlayerStyleNames() {
        YouTubePlayer.PlayerStyle[] states = YouTubePlayer.PlayerStyle.values();
        String[] names = new String[states.length];
        for (int i = 0; i < states.length; i++)
            names[i] = states[i].name();
        return names;
    }




}
