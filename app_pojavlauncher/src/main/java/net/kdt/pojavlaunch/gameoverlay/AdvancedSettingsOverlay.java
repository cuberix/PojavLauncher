package net.kdt.pojavlaunch.gameoverlay;

import android.view.View;
import android.view.ViewGroup;

import net.kdt.pojavlaunch.MainActivity;
import net.kdt.pojavlaunch.R;

public class AdvancedSettingsOverlay extends Overlay implements View.OnClickListener {
    private final int openEditorViewId;
    private final int logOutputViewId;
    private final int sendKeycodeId;
    private final int forceCloseId;
    public AdvancedSettingsOverlay(MainActivity mainActivity, ViewGroup hostView) {
        super(mainActivity, hostView);
        View openEditor = overlayView.findViewById(R.id.button_advanced_open_editor);
        openEditorViewId = openEditor.getId();
        openEditor.setOnClickListener(this);
        View logOutput = overlayView.findViewById(R.id.button_advanced_log_output);
        logOutputViewId = logOutput.getId();
        logOutput.setOnClickListener(this);
        View sendKeycode = overlayView.findViewById(R.id.button_advanced_send_custom_keycode);
        sendKeycodeId = sendKeycode.getId();
        sendKeycode.setOnClickListener(this);
        View forceClose = overlayView.findViewById(R.id.button_advanced_force_close);
        forceCloseId = forceClose.getId();
        forceClose.setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_ingame_advanced;
    }

    @Override
    public void overlayShown() {

    }

    @Override
    public void overlayHidden() {

    }

    @Override
    public void overlayDestroyed() {

    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if(viewId == openEditorViewId) {
            mainActivity.openCustomControls();
        }else if(viewId == logOutputViewId) {
            mainActivity.openLogOutput();
        }else if(viewId == sendKeycodeId) {
            mainActivity.dialogSendCustomKey();
        }else if(viewId == forceCloseId) {
            MainActivity.dialogForceClose(mainActivity);
        }
    }
}
