package com.ctxone.chetaengineslotv2;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.View;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.firebase.FirebaseApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;

import okhttp3.OkHttpClient;
import okhttp3.Request;


public class AutoSpin extends Activity {
    private WebView webdash;
    private boolean touchEnabled = false;
    private boolean switch1, switch2, swit3 = false;
    private Handler tahanduluBreh;
    private float dX, dY;
    private CardView floatingpopup, floatingpopuppg;
    private LinearLayout geser;
    private Switch saklarpp, saklarpg;
    private Dialog jackpotPopup;
    private ValueCallback<Uri[]> mUploadMessage;
    private final static int FILECHOOSER_RESULTCODE = 1;
    private ProgressBar loadingbar;
    private ImageView masuk, tombolpower, aktivasi, aktivasihome, logodalem, tombolpowerzeus, poweron;
    private static final String PREFS_NAME = "MyPrefs";
    private Handler handler = new Handler();
    private Runnable runnable;
    private ImageView slider,slideron;
    private RelativeLayout sliderof;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private ImageView icon;

    private boolean isSliderOpen = false;
    private FrameLayout dragable;
    private float dx, dy;
    private boolean isDragging = false;
    private OkHttpClient client, client2;
    private SimpleExoPlayer player;
    private MyGLSurfaceView glSurfaceView;
    private GestureDetector gestureDetector;
    private Runnable longPressRunnable;
    private boolean isLongPress = false;
    private float initialX;
    private ImageView logomuter;
    private float initialY;
    private TextView textPola;


    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mediaPlayer = MediaPlayer.create(this, R.raw.suaraai);

        initializeViews();
        textPola = findViewById(R.id.textpola);

        // Initialize WebView Settings
        initializeWebViewSettings();

        // Initialize Firebase
        FirebaseApp.initializeApp(this);

        // Initialize OkHttpClient
        initializeOkHttpClient();

        // Set OnClickListeners
        setOnClickListeners();

        // Set Switch Listener
        setSwitchListener();

        setInitialSliderPosition();

    }

    private void initializeViews() {
        webdash = findViewById(R.id.webView);
        saklarpp = findViewById(R.id.kondisisaklar);
        dragable = findViewById(R.id.draggableGroup);
        tombolpower = findViewById(R.id.powermulai);
        tombolpowerzeus = findViewById(R.id.powermulaizeus);
        loadingbar = findViewById(R.id.progressBar);
        masuk = findViewById(R.id.logobull);
        slider = findViewById(R.id.powerstop);
        icon = findViewById(R.id.icon);
        sliderof = findViewById(R.id.sliderof);
        slideron = findViewById(R.id.slideron);
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (!isLongPress) {
                    // Fungsi untuk satu kali tap
                    handleSingleTap();
                }
                if (!isLongPress) {
                    // Fungsi untuk satu kali tap
                    handleSingleTap();
                }
                return true;
            }
        });
        dragable.post(new Runnable() {
            @Override
            public void run() {
                initialX = dragable.getX();
                initialY = dragable.getY();
            }
        });
        dragable.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    isLongPress = false;
                    dX = v.getX() - event.getRawX();
                    dY = v.getY() - event.getRawY();
                    longPressRunnable = new Runnable() {
                        @Override
                        public void run() {
                            isLongPress = true;
                            // Fungsi untuk long press
                            handleLongPress();
                        }
                    };
                    handler.postDelayed(longPressRunnable, 200); // 1 detik untuk long press
                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    handler.removeCallbacks(longPressRunnable);
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (isLongPress) {
                        dragable.animate()
                                .x(event.getRawX() + dX)
                                .y(event.getRawY() + dY)
                                .setDuration(0)
                                .start();
                    }
                }
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    private void initializeWebViewSettings() {
        WebSettings webSettings = webdash.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
    }

    private void initializeOkHttpClient() {
        client = new OkHttpClient();
        client2 = new OkHttpClient();
        String kl = getString(R.string.kcs);
        makeRequest2(kl);
    }



    private void setOnClickListeners() {

        tombolpower.setOnClickListener(v -> startSpibiasa());
        tombolpowerzeus.setOnClickListener(v -> startSpinAnimation());
        slider.setOnClickListener(v -> resetPower());
    }


    private void handleLongPress() {}
    private void handleSingleTap() {

        if (isSliderOpen) {
            animateSliderClose();
        } else {
            animateSliderOpen();
        }

    }


    private void setSwitchListener() {
        saklarpp.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                switch1 = false;
                switch2 = true;
                manual();
            } else {
                switch1 = false;
                switch2 = false;
                webdash.loadUrl("javascript:XT.SetBool(Vars.AutoplayContinuousSpin, !XT.GetBool(Vars.AutoplayContinuousSpin));");
                webdash.loadUrl("javascript:XT.SetBool(Vars.FastPlay, !XT.GetBool(Vars.FastPlay));");
            }
        });
    }

    private void startSpibiasa() {
        saklarpp.setChecked(true);
        slider.setVisibility(View.VISIBLE);
        tombolpower.setVisibility(View.GONE);
        tombolpowerzeus.setVisibility(View.GONE);
        if (!isPlaying) {
            mediaPlayer.start(); // Mulai pemutaran
            isPlaying = true;
        } else {
            mediaPlayer.pause(); // Jeda pemutaran
            isPlaying = false;
        }
        icon.setImageResource(R.drawable.icon);

    }
    private void startSpinAnimation() {
        logomuter = findViewById(R.id.animasi1);

        // Hentikan Glide dan bersihkan sumber daya sebelum memuat animasi baru
        Glide.with(this).clear(logomuter);
        logomuter.setImageDrawable(null); // Bersihkan ImageView

        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);

        // Kontrol pemutaran media (jika diperlukan)
        if (!isPlaying) {
            mediaPlayer.start(); // Mulai pemutaran
            isPlaying = true;
        } else {
            mediaPlayer.pause(); // Jeda pemutaran
            isPlaying = false;
        }



        Glide.with(this)
                .asDrawable()
                .load(R.drawable.animasi)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (resource instanceof com.bumptech.glide.integration.webp.decoder.WebpDrawable) {
                            com.bumptech.glide.integration.webp.decoder.WebpDrawable webpDrawable = (com.bumptech.glide.integration.webp.decoder.WebpDrawable) resource;
                            webpDrawable.setLoopCount(1);
                        }
                        return false;
                    }
                })
                .into(logomuter);

        // Animasi lainnya atau manipulasi tampilan lainnya
        dragable.setVisibility(View.GONE);
        dragable.startAnimation(fadeOut);

        new Handler().postDelayed(() -> {
            logomuter.startAnimation(fadeOut);
            dragable.startAnimation(fadeIn);
            fadeOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    logomuter.setVisibility(View.GONE);
                    dragable.setVisibility(View.VISIBLE);
                    slider.setVisibility(View.VISIBLE);
                    tombolpowerzeus.setVisibility(View.GONE);
                    saklarpp.setChecked(true);
                    icon.setImageResource(R.drawable.icon);

                    // Bersihkan Glide setelah animasi selesai
                    Glide.with(AutoSpin.this).clear(logomuter);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }, 10000);
    }



    private void resetPower() {
        saklarpp.setChecked(false);
        tombolpower.setVisibility(View.VISIBLE);
        sliderof.setVisibility(View.VISIBLE);
//        slideron.setVisibility(View.GONE);
        slider.setVisibility(View.GONE);
        icon.setImageResource(R.drawable.sebelumaktif); // Ganti dengan nama resource gambar untuk icon

    }

    private boolean handleDragEvent(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isTouchInsideView(event, aktivasi)) {
                    isDragging = false;
                    return false;
                } else {
                    isDragging = true;
                    dx = v.getX() - event.getRawX();
                    dy = v.getY() - event.getRawY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isDragging) {
                    v.animate()
                            .x(event.getRawX() + dx)
                            .y(event.getRawY() + dy)
                            .setDuration(0)
                            .start();
                }
                break;
            default:
                return false;
        }
        return true;
    }

    private void disableTouchDetection() {
        touchEnabled = false;
    }
    private void enableTouchDetection() {
        touchEnabled = true;
    }
    private void runFunctionRepeatedly() {
        tahanduluBreh = new Handler();
        tahanduluBreh.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (switch1 && touchEnabled) {
                    webdash.loadUrl("javascript:XT.SetBool(Vars.FastPlay, !XT.GetBool(Vars.FastPlay));");
                    int lonte = randomputaran();
                    webdash.loadUrl("javascript:XT.SetInt(Vars.AutoplaySpinsLeft, " + lonte + ")");
                    cepat();
                    String message = "MODE: TURBO SPIN";
                    Toast.makeText(AutoSpin.this, message, Toast.LENGTH_LONG).show();

                }
                if (switch2 && touchEnabled) {
                    webdash.loadUrl("javascript:XT.SetBool(Vars.FastPlay, !XT.GetBool(Vars.FastPlay));");
                    int lonte = randomputaran();
                    webdash.loadUrl("javascript:XT.SetInt(Vars.AutoplaySpinsLeft, " + lonte + ")");
                    cepat();
                    String message = "MODE: TURBO SPIN";
                    Toast.makeText(AutoSpin.this, message, Toast.LENGTH_LONG).show();
                }
            }
        }, 500);
    }
    private void addJavaScriptToWebView() {
        String javascript = "var spinLeftValue = XT.GetInt(Vars.AutoplaySpinsLeft);" +
                "spinLeftValue;";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            webdash.evaluateJavascript(javascript, value -> {
                if (value != null && !value.isEmpty()) {
                    int spinLeft = Integer.parseInt(value);
                    if (spinLeft == -1) {
                    }
                }
            });
        }
    }


    private int randomputaran() {
        int[] possibleValues = {10, 20, 30, 50};
        Random random = new Random();
        int randomIndex = random.nextInt(possibleValues.length);
        return possibleValues[randomIndex];
    }


    private void restartpola() {
        tahanduluBreh = new Handler();
        tahanduluBreh.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (switch2 && touchEnabled) {
                    webdash.loadUrl("javascript:XT.TriggerEvent(Vars.Evt_DataToCode_Pressed_Spin);");
                    tahanduluBreh.postDelayed(this, 500);
                    saklarpp.setChecked(false);
                    String message = "MODE: MANUAL SPIN";
                    Toast.makeText(AutoSpin.this, message, Toast.LENGTH_LONG).show();
                    loop();

                }
                if (switch1 && touchEnabled) {
                    webdash.loadUrl("javascript:XT.TriggerEvent(Vars.Evt_DataToCode_Pressed_Spin);");
                    tahanduluBreh.postDelayed(this, 500);
                    String message = "MODE: MANUAL SPIN";
                    Toast.makeText(AutoSpin.this, message, Toast.LENGTH_LONG).show();

                }
            }
        }, 120000);
    }
    private void loop() {
        tahanduluBreh = new Handler();
        tahanduluBreh.postDelayed(new Runnable() {
            @Override
            public void run() {
                saklarpp.setChecked(true);
//                showJackpotPopup();
            }
        }, 500);
    }

    private void cepat() {
        tahanduluBreh = new Handler();
        tahanduluBreh.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (switch1 && touchEnabled) {
                    webdash.loadUrl("javascript:XT.SetBool(Vars.AutoplayContinuousSpin, !XT.GetBool(Vars.AutoplayContinuousSpin));");
                    webdash.loadUrl("javascript:XT.SetInt(Vars.AutoplaySpinsLeft, 20);");
                    String message = "MODE: QUICK SPIN";
                    Toast.makeText(AutoSpin.this, message, Toast.LENGTH_LONG).show();
                    restartpola();
                }
                if (switch2 && touchEnabled) {
                    webdash.loadUrl("javascript:XT.SetBool(Vars.AutoplayContinuousSpin, !XT.GetBool(Vars.AutoplayContinuousSpin));");
                    webdash.loadUrl("javascript:XT.SetInt(Vars.AutoplaySpinsLeft, 20);");
                    String message = "MODE: QUICK SPIN";
                    Toast.makeText(AutoSpin.this, message, Toast.LENGTH_LONG).show();
                    restartpola();
                }

            }
        }, 30000);
    }

    private void manual() {
        String message = "MODE: MANUAL SPIN";
        Toast.makeText(AutoSpin.this, message, Toast.LENGTH_LONG).show();
        tahanduluBreh = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (switch1 && touchEnabled) {
                    webdash.loadUrl("javascript:XT.TriggerEvent(Vars.Evt_DataToCode_Pressed_Spin);");
                    tahanduluBreh.postDelayed(this, 500);

                }
                if (switch2 && touchEnabled) {
                    webdash.loadUrl("javascript:XT.TriggerEvent(Vars.Evt_DataToCode_Pressed_Spin);");
                    tahanduluBreh.postDelayed(this, 500);
                }
            }
        };

        tahanduluBreh.post(runnable);
        tahanduluBreh.postDelayed(new Runnable() {
            @Override
            public void run() {
                runFunctionRepeatedly();
            }
        }, 10000);
    }



    private void startKeyPressSimulation() {
        if (swit3) {
            if (runnable == null) { // Cek jika runnable sudah dibuat sebelumnya
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        simulateSpaceKeyPress();
                        handler.postDelayed(this, 1000); // Simulasi akan dijalankan setiap 1 detik
                    }
                };
                handler.post(runnable); // Memulai eksekusi runnable
            }
        } else {
            if (runnable != null) {
                handler.removeCallbacks(runnable); // Menghapus runnable dari Handler
                runnable = null; // Atur ulang nilai runnable
            }
        }
    }

    private void simulateSpaceKeyPress() {
        KeyEvent eventDown = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_SPACE);
        dispatchKeyEvent(eventDown);
        KeyEvent eventUp = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_SPACE);
        dispatchKeyEvent(eventUp);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Hentikan simulasi ketika Activity dihancurkan
        handler.removeCallbacks(runnable);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (mUploadMessage == null) return;
            mUploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
            mUploadMessage = null;
        }
    }
    @Override
    public void onBackPressed() {
        if (webdash.canGoBack()) {
            webdash.goBack();
            switch2 = false;
            switch1 = false;
            icon.setImageResource(R.drawable.sebelumaktif);
            swit3 = false;
            dragable.setVisibility(View.GONE);
//            sliderof.setVisibility(View.GONE);
            slider.setVisibility(View.GONE);
            if (isSliderOpen) {
                animateSliderClose();
            } else {
//                animateSliderOpen();
            }
            resetPower();
            dragable.animate()
                    .x(initialX)
                    .y(initialY)
                    .setDuration(300)
                    .start();
            if (logomuter != null) {
                try {
                    Glide.with(this).clear(logomuter);
                    logomuter.setImageDrawable(null);
                } catch (Exception e) {
                    Log.e("MainActivity", "Error clearing image", e);
                }
            } else {
                Log.w("MainActivity", "logomuter is null");
            }
            textPola.setText("AI ENGINE\nPENCARI POLA");

        } else {
            super.onBackPressed();
        }
    }


    private void makeRequest2(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        client2.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        String url = jsonObject.getString("gameUrl");
                        // Panggil metode WebView di thread utama
                        runOnUiThread(() -> {
                            webdash.setWebViewClient(new WebViewClient() {
                                @Override
                                public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
                                    loadingbar.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onPageFinished(WebView view, String url) {
                                    if (url.contains("html5Game.do")) {
                                        enableTouchDetection();
                                        loadingbar.setVisibility(View.GONE);
                                        tombolpower.setVisibility(View.VISIBLE);
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                dragable.setVisibility(View.VISIBLE);
                                            }
                                        }, 10000); // 10000 milidetik = 10 detik
                                    } else {
                                        disableTouchDetection();
                                    }

                                    if (url.contains("Gates%20of%20Olympus%201000")) {
                                        ImageView logomuter = findViewById(R.id.animasi1);
                                        logomuter.setVisibility(View.VISIBLE);
                                        tombolpower.setVisibility(View.GONE);
                                        tombolpowerzeus.setVisibility(View.VISIBLE);
                                        textPola.setText("AI ENGINE\nPENCARI PETIR");

                                    } else {
                                    }
                                }



                                @Override
                                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                    if (url.startsWith("https://play.google.com/store/apps/")) {
                                        try {
                                            view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                                            return true;
                                        } catch (Exception e) {
                                            // Log the error or handle it
                                        }
                                    }
                                    return super.shouldOverrideUrlLoading(view, url);
                                }
                            });

                            webdash.setWebChromeClient(new WebChromeClient() {
                                public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                                    mUploadMessage = filePathCallback;
                                    Intent intent = fileChooserParams.createIntent();
                                    try {
                                        startActivityForResult(intent, FILECHOOSER_RESULTCODE);
                                    } catch (ActivityNotFoundException e) {
                                        mUploadMessage = null;
                                        return false;
                                    }
                                    return true;
                                }

                                @Override
                                public void onProgressChanged(WebView view, int newProgress) {
                                    loadingbar.setProgress(newProgress);
                                }
                            });

                            webdash.loadUrl(url);

//                            webdash.loadUrl("https://d3pvfi6m7bxu71.cloudfront.net/gs2c/openGame.do?lang=id&cur=IDR&gameSymbol=vs20olympx&websiteUrl=https%3A%2F%2Fdemogamesfree.pragmaticplay.net&jurisdiction=99");
                        });

                    } catch (JSONException e) {
                        // Handle JSON parsing error
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                // Log the error or handle it
                // Log.e("MainActivity", "Request Failed", e);
            }
        });
    }
    private void setInitialSliderPosition() {
        float marginLeftInPx = (25); // Konversi 25dp ke piksel
        sliderof.setX(icon.getX() - sliderof.getWidth() + marginLeftInPx);
        slideron.setX(icon.getX() - slideron.getWidth() + marginLeftInPx);
    }


    private void animateSliderOpen() {
        sliderof.setVisibility(View.VISIBLE);
        ObjectAnimator animator = ObjectAnimator.ofFloat(sliderof, "translationX", -sliderof.getWidth(), 0);
//        ObjectAnimator animator2 = ObjectAnimator.ofFloat(slideron, "translationX", -slideron.getWidth(), 0);
//
//        animator2.setDuration(1000);
//        animator2.start();
//        animator2.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                isSliderOpen = true;
//            }
//        });
        animator.setDuration(500);
        animator.start();

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isSliderOpen = true;
//                tombolpower.setVisibility(View.VISIBLE);

            }
        });
    }

    private void animateSliderClose() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(sliderof, "translationX", 0, -sliderof.getWidth());
//        ObjectAnimator animator2 = ObjectAnimator.ofFloat(sliderof, "translationX", 0, -sliderof.getWidth());
//
//        animator2.setDuration(1000);
//        animator2.start();
//        animator2.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                sliderof.setVisibility(View.GONE);
//                slideron.setVisibility(View.GONE);
//                tombolpower.setVisibility(View.GONE);
//                slider.setVisibility(View.GONE);
//                isSliderOpen = false;
//            }
//        });
//        tombolpower.setVisibility(View.GONE);
//        slider.setVisibility(View.GONE);
        animator.setDuration(500);
        animator.start();
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
//                sliderof.setVisibility(View.GONE);
//                slideron.setVisibility(View.GONE);
//                tombolpower.setVisibility(View.GONE);
//                slider.setVisibility(View.GONE);
                isSliderOpen = false;
            }
        });
    }
    private boolean isTouchInsideView(MotionEvent event, View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        float x = event.getRawX();
        float y = event.getRawY();
        if (x >= location[0] && x <= location[0] + view.getWidth() &&
                y >= location[1] && y <= location[1] + view.getHeight()) {
            return true;
        }
        return false;
    }
    }
