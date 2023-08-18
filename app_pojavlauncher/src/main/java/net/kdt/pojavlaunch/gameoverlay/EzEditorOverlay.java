package net.kdt.pojavlaunch.gameoverlay;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import net.kdt.pojavlaunch.MainActivity;
import net.kdt.pojavlaunch.R;
import net.kdt.pojavlaunch.prefs.LauncherPreferences;

public class EzEditorOverlay extends Overlay implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private final SeekBar gyroSensitivitySeek;
    private final SeekBar mouseSpeedSeek;
    private final ImageButton joystickButton;
    private final ImageButton keysButton;
    private final TextView gyroLabel;
    private final TextView sensLabel;
    private final int joystickButtonId;
    private final int keysButtonId;
    private final int gyroSeekId;
    private final int mouseSeekId;

    public EzEditorOverlay(MainActivity mainActivity, ViewGroup hostView) {
        super(mainActivity, hostView);
        joystickButton = overlayView.findViewById(R.id.ez_editor_joystickButton);
        keysButton = overlayView.findViewById(R.id.ez_editor_dpadButton);
        gyroSensitivitySeek = overlayView.findViewById(R.id.ez_editor_gyro_sensitivity);
        mouseSpeedSeek = overlayView.findViewById(R.id.ez_editor_mouse_speed);
        gyroLabel = overlayView.findViewById(R.id.ez_editor_gyro_label);
        sensLabel = overlayView.findViewById(R.id.ez_editor_mouse_label);
        if(!LauncherPreferences.PREF_ENABLE_GYRO) {
            gyroLabel.setVisibility(View.GONE);
            gyroSensitivitySeek.setVisibility(View.GONE);
        }
        joystickButtonId = joystickButton.getId();
        keysButtonId = keysButton.getId();
        gyroSeekId = gyroSensitivitySeek.getId();
        mouseSeekId = mouseSpeedSeek.getId();
        gyroSensitivitySeek.setOnSeekBarChangeListener(this);
        mouseSpeedSeek.setOnSeekBarChangeListener(this);
        joystickButton.setOnClickListener(this);
        keysButton.setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_ingame_ez_editor;
    }

    @Override
    public void overlayShown() {
        updateButtonViews();
        updateLabels();
        gyroSensitivitySeek.setProgress(Math.max((int)(LauncherPreferences.PREF_GYRO_SENSITIVITY*100f)-25,0));
        mouseSpeedSeek.setProgress(Math.max((int)(LauncherPreferences.PREF_MOUSESPEED*100f)-25,0));
    }

    @Override
    public void overlayHidden() {
        LauncherPreferences.DEFAULT_PREF.edit()
                .putInt("mousespeed", (int)(LauncherPreferences.PREF_MOUSESPEED*100f))
                .putInt("gyroSensitivity", (int)(LauncherPreferences.PREF_GYRO_SENSITIVITY*100f))
                .apply();
    }

    @Override
    public void overlayDestroyed() {

    }

    @Override
    public void onClick(View view) {
        try {
            int viewId = view.getId();
            if(viewId == joystickButtonId) {
                mainActivity.setControlType(true);
                updateButtonViews();
            }else if(viewId == keysButtonId) {
                mainActivity.setControlType(false);
                updateButtonViews();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateButtonViews() {
        joystickButton.setEnabled(!mainActivity.getControlType());
        keysButton.setEnabled(mainActivity.getControlType());
    }

    private void updateLabels() {
        if(LauncherPreferences.PREF_ENABLE_GYRO)
            gyroLabel.setText(mainActivity.getString(R.string.gyro_sens_overlay, (int)(LauncherPreferences.PREF_GYRO_SENSITIVITY*100f)));
        sensLabel.setText(mainActivity.getString(R.string.mouse_speed_overlay, (int)(LauncherPreferences.PREF_MOUSESPEED*100f)));
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if(b) {
            int viewId = seekBar.getId();
            float value = (i+25)/100f;
            if(viewId == mouseSeekId) {
                LauncherPreferences.PREF_MOUSESPEED = value;
                updateLabels();
            }else if(viewId == gyroSeekId) {
                LauncherPreferences.PREF_GYRO_SENSITIVITY = value;
                updateLabels();
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}
}
