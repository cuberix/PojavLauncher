package net.kdt.pojavlaunch.gameoverlay;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.kdt.pojavlaunch.MainActivity;

public abstract class Overlay {
    protected View overlayView;
    protected MainActivity mainActivity;
    public Overlay(MainActivity mainActivity, ViewGroup hostView) {
        this.mainActivity = mainActivity;
        overlayView = LayoutInflater.from(mainActivity).inflate(getLayoutId(), hostView, false);
    }
    public View getView() {
        return overlayView;
    }
    public abstract int getLayoutId();
    public abstract void overlayShown();
    public abstract void overlayHidden();
    public abstract void overlayDestroyed();
}
