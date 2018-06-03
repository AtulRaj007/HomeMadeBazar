package com.munchmash.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.munchmash.R;
import com.munchmash.util.Constants;


public class YouTubePlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, View.OnClickListener {

    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private final String TAG = ">>>>>YOUTUBE_ACTIVITY=";
    // YouTube player view
    private YouTubePlayerView youTubeView;
    private MyPlayerStateChangeListener playerStateChangeListener;
    private MyPlaybackEventListener playbackEventListener;
    private String YOUTUBE_VIDEO_CODE = "";
    private ImageView img_close;
//    https://www.youtube.com/watch?v=WoNAt9bQZtY

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_youtube);

            Intent intent = getIntent();
            String videoUrl = intent.getStringExtra("video_link");
            String title = intent.getStringExtra("news_title");
            YOUTUBE_VIDEO_CODE = getYouTubeVideoId(videoUrl);


            youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
            img_close = (ImageView) findViewById(R.id.img_close);
            img_close.setOnClickListener(this);

            // Initializing video player with developer key
            if (YOUTUBE_VIDEO_CODE != null && !YOUTUBE_VIDEO_CODE.isEmpty()) {
                try {
                    youTubeView.initialize(Constants.YOUTUBE_KEY, YouTubePlayerActivity.this);
                } catch (Exception e) {
                    Log.e("youtube exception inner", " " + e.getMessage());
                }
            }
            playerStateChangeListener = new MyPlayerStateChangeListener();
            playbackEventListener = new MyPlaybackEventListener();
        } catch (Exception e) {
            Log.e("youtube exception ", "" + e.getMessage());
            // Toast.makeText(getApplicationContext(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
//            Utils.handleError(e,this);
        }
    }

    public String getYouTubeVideoId(String Url) {
        if (Url != null && !Url.isEmpty()) {
            String delimiter = "=";
            String temp[] = Url.split(delimiter);
            if (temp.length > 0)
                return temp[temp.length - 1];
            return "";
        } else {
            return "";
        }


    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
        youTubePlayer.setPlaybackEventListener(playbackEventListener);
        if (!b) {
            // loadVideo() will auto play video
            // Use cueVideo() method, if you don't want to play it automatically
            if (YOUTUBE_VIDEO_CODE != null && !YOUTUBE_VIDEO_CODE.isEmpty()) {
                try {
                    youTubePlayer.loadVideo(YOUTUBE_VIDEO_CODE);
                    // Hiding player controls
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                } catch (Exception e) {
                    Log.e("youtube exception inner", "" + e.getMessage());
                }
            }

        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult != null && youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            if (youTubeInitializationResult != null) {
                String errorMessage = String.format(
                        getString(R.string.error_player), youTubeInitializationResult.toString());
                if (errorMessage == null) {
                    errorMessage = "";
                }
                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            if (YOUTUBE_VIDEO_CODE != null && !YOUTUBE_VIDEO_CODE.isEmpty()) {
                try {
                    getYouTubePlayerProvider().initialize(Constants.YOUTUBE_KEY, YouTubePlayerActivity.this);
                } catch (Exception e) {
                    Log.e("youtube exceptio actres", "" + e.getMessage());
                }
            }
        }
    }

    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {

        finish();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void showMessage(String message) {
        Log.e(TAG, message);
    }

    private final class MyPlaybackEventListener implements YouTubePlayer.PlaybackEventListener {

        @Override
        public void onPlaying() {
            // Called when playback starts, either due to user action or call to play().
            showMessage("Playing");
        }

        @Override
        public void onPaused() {
            // Called when playback is paused, either due to user action or call to pause().
            showMessage("Paused");
        }

        @Override
        public void onStopped() {
            // Called when playback stops for a reason other than being paused.
            showMessage("Stopped");
        }

        @Override
        public void onBuffering(boolean b) {
            // Called when buffering starts or ends.
        }

        @Override
        public void onSeekTo(int i) {
            // Called when a jump in playback position occurs, either
            // due to user scrubbing or call to seekRelativeMillis() or seekToMillis()
        }
    }

    private final class MyPlayerStateChangeListener implements YouTubePlayer.PlayerStateChangeListener {

        @Override
        public void onLoading() {
            // Called when the player is loading a video
            // At this point, it's not ready to accept commands affecting playback such as play() or pause()
        }

        @Override
        public void onLoaded(String s) {
            // Called when a video is done loading.
            // Playback methods such as play(), pause() or seekToMillis(int) may be called after this callback.
        }

        @Override
        public void onAdStarted() {
            // Called when playback of an advertisement starts.
        }

        @Override
        public void onVideoStarted() {
            // Called when playback of the video starts.
        }

        @Override
        public void onVideoEnded() {
            // Called when the video reaches its end.
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {
            // Called when an error occurs.
        }
    }
}