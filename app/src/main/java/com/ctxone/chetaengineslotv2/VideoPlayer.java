package com.ctxone.chetaengineslotv2;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.view.Surface;
import android.view.TextureView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import java.io.IOException;

public class VideoPlayer implements TextureView.SurfaceTextureListener {

    private Context context;
    private TextureView textureView;
    private MediaPlayer mediaPlayer;

    public VideoPlayer(Context context, TextureView textureView) {
        this.context = context;
        this.textureView = textureView;
        this.textureView.setSurfaceTextureListener(this);
    }

    @Override
    public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surfaceTexture, int width, int height) {
        Surface surface = new Surface(surfaceTexture);
        try {
            AssetFileDescriptor afd = context.getAssets().openFd("ot.webm"); // Pastikan "ot.webm" memiliki channel alpha
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mediaPlayer.setSurface(surface);
            mediaPlayer.setLooping(true);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(mp -> {
                mediaPlayer.start();
            });
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error loading video", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surfaceTexture, int width, int height) {
        // Handle size change if needed
    }

    @Override
    public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surfaceTexture) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surfaceTexture) {
        // Update your UI if needed
    }
}
