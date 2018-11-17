package com.mrhuman.levi9.ageofarmies;

import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.google.ar.core.Frame;
import com.google.ar.core.Plane;
import com.google.ar.core.Session;
import com.google.ar.core.TrackingState;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.core.exceptions.UnavailableException;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.rendering.ModelRenderable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class GameActivity extends AppCompatActivity {

    public static final String WIDTH_ARG = "WIDTH_ARG";
    public static final String HEIGHT_ARG = "HEIGHT_ARG";
    public static final String SCALE_ARG = "SCALE_ARG";

    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler hideHandler = new Handler();

    private static final int RC_PERMISSIONS = 0x123;
    private boolean installRequested;

    private ArSceneView arSceneView;

    // 3D renderable models
    private ModelRenderable bulletRenderable;
    private ModelRenderable cannon1Renderable;
    private ModelRenderable cannon2Renderable;
    private ModelRenderable castle1Renderable;
    private ModelRenderable castle2Renderable;
    private ModelRenderable farm1Renderable;
    private ModelRenderable farm2Renderable;
    private ModelRenderable tileRenderable;

    // True once scene is loaded
    private boolean hasFinishedLoading = false;

    private Snackbar loadingMessageSnackbar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        arSceneView = findViewById(R.id.ar_view);

        CompletableFuture<ModelRenderable> bulletStage =
                ModelRenderable.builder().setSource(this, Uri.parse("bullet.sfb")).build();
        CompletableFuture<ModelRenderable> cannon1Stage =
                ModelRenderable.builder().setSource(this, Uri.parse("cannon1.sfb")).build();
        CompletableFuture<ModelRenderable> cannon2Stage =
                ModelRenderable.builder().setSource(this, Uri.parse("Cannon2.sfb")).build();
        CompletableFuture<ModelRenderable> castle1Stage =
                ModelRenderable.builder().setSource(this, Uri.parse("castle1.sfb")).build();
        CompletableFuture<ModelRenderable> castle2Stage =
                ModelRenderable.builder().setSource(this, Uri.parse("castle2.sfb")).build();
        CompletableFuture<ModelRenderable> farm1Stage =
                ModelRenderable.builder().setSource(this, Uri.parse("farm1.sfb")).build();
        CompletableFuture<ModelRenderable> farm2Stage =
                ModelRenderable.builder().setSource(this, Uri.parse("farm2.sfb")).build();
        CompletableFuture<ModelRenderable> tileStage =
                ModelRenderable.builder().setSource(this, Uri.parse("tile.sfb")).build();

        CompletableFuture.allOf(
                bulletStage,
                cannon1Stage,
                cannon2Stage,
                castle1Stage,
                castle2Stage,
                farm1Stage,
                farm2Stage,
                tileStage
        ).handle(
                (notUsed, throwable) -> {
                    if (throwable != null) {
                        ARCoreUtils.displayError(this, "Unable to load renderable", throwable);
                        return null;
                    }

                    try {
                        bulletRenderable = bulletStage.get();
                        cannon1Renderable = cannon1Stage.get();
                        cannon2Renderable = cannon2Stage.get();
                        castle1Renderable = castle1Stage.get();
                        castle2Renderable = castle2Stage.get();
                        farm1Renderable = farm1Stage.get();
                        farm2Renderable = farm2Stage.get();
                        tileRenderable = tileStage.get();

                        // Everything finished loading successfully.
                        hasFinishedLoading = true;

                    } catch (InterruptedException | ExecutionException ex) {
                        ARCoreUtils.displayError(this, "Unable to load renderable", ex);
                    }

                    return null;
                }
        );

        arSceneView
                .getScene()
                .addOnUpdateListener(
                        frameTime -> {
                            if (loadingMessageSnackbar == null) { return; }

                            Frame frame = arSceneView.getArFrame();
                            if (frame == null) { return; }

                            if (frame.getCamera().getTrackingState() != TrackingState.TRACKING) {
                                return;
                            }

                            for (Plane plane : frame.getUpdatedTrackables(Plane.class)) {
                                if (plane.getTrackingState() == TrackingState.TRACKING) {
                                    hideLoadingMessage();
                                }
                            }
                        });

        ARCoreUtils.requestCameraPermission(this, RC_PERMISSIONS);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (arSceneView == null) {
            return;
        }

        if (arSceneView.getSession() == null) {
            // If the session wasn't created yet, don't resume rendering.
            // This can happen if ARCore needs to be updated or permissions are not granted yet.
            try {
                Session session = ARCoreUtils.createArSession(this, installRequested);
                if (session == null) {
                    installRequested = ARCoreUtils.hasCameraPermission(this);
                    return;
                } else {
                    arSceneView.setupSession(session);
                }
            } catch (UnavailableException e) {
                ARCoreUtils.handleSessionException(this, e);
            }
        }

        try {
            arSceneView.resume();
        } catch (CameraNotAvailableException ex) {
            ARCoreUtils.displayError(this, "Unable to get camera", ex);
            finish();
            return;
        }

        if (arSceneView.getSession() != null) {
            showLoadingMessage();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        hideHandler.postDelayed(() -> {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }

            hideHandler.postDelayed(() -> arSceneView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION), UI_ANIMATION_DELAY);
        }, 100);
    }

    private void showLoadingMessage() {
        if (loadingMessageSnackbar != null && loadingMessageSnackbar.isShownOrQueued()) {
            return;
        }

        loadingMessageSnackbar =
                Snackbar.make(
                        GameActivity.this.findViewById(android.R.id.content),
                        R.string.plane_finding,
                        Snackbar.LENGTH_INDEFINITE);
        loadingMessageSnackbar.getView().setBackgroundColor(0xbf323232);
        loadingMessageSnackbar.show();
    }

    private void hideLoadingMessage() {
        if (loadingMessageSnackbar == null) {
            return;
        }

        loadingMessageSnackbar.dismiss();
        loadingMessageSnackbar = null;
    }

}
