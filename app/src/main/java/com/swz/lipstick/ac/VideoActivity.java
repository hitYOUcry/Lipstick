package com.swz.lipstick.ac;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.swz.lipstick.R;
import com.swz.lipstick.ui.CustomLoadControl;
import com.swz.lipstick.ui.CustomPlayerView;

import static com.google.android.exoplayer2.ui.AspectRatioFrameLayout.RESIZE_MODE_FIT;

public class VideoActivity extends Activity {

    private String mUrl = "https://app.ibluesand.cn/attachment/audios/main.mp4";

    private CustomPlayerView mVideoPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        playByExoPlayer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mVideoPlayerView != null && mVideoPlayerView.getPlayer() != null) {
            mVideoPlayerView.getPlayer().setPlayWhenReady(true);
        }
    }

    /**
     * 使用exoplayer播放
     */
    private void playByExoPlayer() {
        mVideoPlayerView = findViewById(R.id.video_player_view);
        initPlayer();
    }

    private void initPlayer() {
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelector trackSelector = new DefaultTrackSelector(bandwidthMeter);

        LoadControl loadControl = new DefaultLoadControl();

        RenderersFactory renderersFactory = new DefaultRenderersFactory(this);


        DefaultHttpDataSourceFactory httpDataSourceFactory = new DefaultHttpDataSourceFactory(
                Util.getUserAgent(this, "VideoPlayer"),
                null,
                DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
                true
        );

        ExtractorMediaSource.Factory f = new ExtractorMediaSource.Factory(httpDataSourceFactory);

        MediaSource videoSource = f.createMediaSource(Uri.parse(mUrl));
        //                HlsMediaSource.Factory f = new HlsMediaSource.Factory(httpDataSourceFactory);
        //
        //                HlsMediaSource videoSource = f.createMediaSource(Uri.parse(RTMP_URL));
        LoopingMediaSource loopingMediaSource = new LoopingMediaSource(videoSource);

        final SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);
        player.prepare(loopingMediaSource);
        playVideo(player);

    }

    private void playVideo(SimpleExoPlayer player) {
        mVideoPlayerView.setPlayer(player);
        mVideoPlayerView.setResizeMode(RESIZE_MODE_FIT);
        mVideoPlayerView.hideController();
        mVideoPlayerView.getPlayer().addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == Player.STATE_ENDED) {
                    mVideoPlayerView.getPlayer().seekTo(mVideoPlayerView.getPlayer().getCurrentPosition() - 50);
                    mVideoPlayerView.getPlayer().setPlayWhenReady(false);
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });

        player.setPlayWhenReady(true);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVideoPlayerView != null) {
            mVideoPlayerView.getPlayer().release();
        }
    }
}
