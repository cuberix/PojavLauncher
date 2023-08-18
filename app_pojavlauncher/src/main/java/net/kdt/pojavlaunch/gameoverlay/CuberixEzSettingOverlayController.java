package net.kdt.pojavlaunch.gameoverlay;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import net.kdt.pojavlaunch.MainActivity;
import net.kdt.pojavlaunch.R;

import java.lang.reflect.Constructor;

public class CuberixEzSettingOverlayController implements View.OnClickListener{
    private ViewGroup mainContentView;
    private View overlayView;
    private FrameLayout overlayViewHost;
    private ViewGroup.LayoutParams matchParentLayoutParams;
    private static final int OVERLAY_COUNT = 2;
    private int[] overlayButtonIds;
    private Overlay[] overlays;
    private Constructor<? extends Overlay>[] overlayConstructors;
    private int exitButtonId;
    private int lastVisibleOverlay = -1;
    private MainActivity mainActivity;
    @SuppressWarnings("unchecked") //  для строки 32, потому что джава момент
    public void initView(MainActivity mainActivity) {
        if(overlays != null) dropAllOverlays();
        this.mainActivity = mainActivity;
        mainContentView = mainActivity.findViewById(R.id.content_frame);
        overlayView = LayoutInflater.from(mainActivity).inflate(R.layout.layout_ingame_setting, mainContentView, false);
        overlayViewHost = overlayView.findViewById(R.id.frameLayoutr_ingameSetting_container);
        matchParentLayoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        overlayButtonIds = new int[OVERLAY_COUNT];
        overlays = new Overlay[OVERLAY_COUNT];
        overlayConstructors = new Constructor[OVERLAY_COUNT];
        registerOverlay(0, R.id.button_ingameSetting_controlsEz, EzEditorOverlay.class);
        registerOverlay(1, R.id.button_ingameSetting_advanced, AdvancedSettingsOverlay.class);
        View view = overlayView.findViewById(R.id.button_ingameSetting_exit);
        exitButtonId = view.getId();
        view.setOnClickListener(this);
    }

    private void registerOverlay(int arrIndex, int buttonId, Class<? extends Overlay> overlayClass) {
        try {
            View buttonView = overlayView.findViewById(buttonId);
            buttonView.setOnClickListener(this);
            overlayButtonIds[arrIndex] = buttonView.getId();
            overlayConstructors[arrIndex] = overlayClass.getConstructor(MainActivity.class, ViewGroup.class);
        }catch (NoSuchMethodException e) {
            Log.i("OverlayController", "Failed to register overlay "+overlayClass.getCanonicalName(), e);
            System.exit(1);
        }
    }

    private void displayOverlay(int index) {
        Overlay overlay = overlays[index];
        if(overlay == null) {
            try {
                overlay = overlayConstructors[index].newInstance(mainActivity, overlayViewHost);
                overlays[index] = overlay;
            } catch (Exception e) {
                Log.i("OverlayController", "Failed to insantiate overlay "+overlayConstructors[index].getDeclaringClass().getCanonicalName(), e);
                System.exit(1);
            }
        }
        if(overlay == null) {
            Log.i("OverlayController", "WTF: switching to null overlay???");
            return;
        }
        if(overlayViewHost.getChildCount() > 0)
            overlayViewHost.removeAllViews();
        if(lastVisibleOverlay != -1 && overlays[lastVisibleOverlay] != null)
            overlays[lastVisibleOverlay].overlayHidden();
        lastVisibleOverlay = index;
        overlayViewHost.addView(overlay.getView(), matchParentLayoutParams);
        overlay.overlayShown();
    }

    private void dropAllOverlays() {
        for(Overlay overlay : overlays) {
            if(overlay != null) overlay.overlayDestroyed();
        }
        overlays = new Overlay[OVERLAY_COUNT];
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == exitButtonId) {
            hideOverlay();
        }
        for(int i = 0; i < OVERLAY_COUNT; i++) {
            if(view.getId() == overlayButtonIds[i]) {
                displayOverlay(i);
                break;
            }
        }
    }

    public void showOverlay() {
        if(lastVisibleOverlay != -1 && overlays[lastVisibleOverlay] != null) overlays[lastVisibleOverlay].overlayShown();
        mainContentView.addView(overlayView, mainContentView.getChildCount()-1, matchParentLayoutParams);
    }

    public void hideOverlay() {
        if(lastVisibleOverlay != -1 && overlays[lastVisibleOverlay] != null) overlays[lastVisibleOverlay].overlayHidden();
        mainContentView.removeView(overlayView);
    }
}
