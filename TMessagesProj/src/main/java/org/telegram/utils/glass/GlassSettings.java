package org.telegram.utils.glass;

import android.os.Build;

import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.SharedConfig;

import java.util.Objects;

public class GlassSettings {
    private static final String APP_WIDE_LIQUID_GLASS_KEY = "glassy_app_wide_liquid_glass";

    public final boolean isBlurEnabled;
    public final boolean isLiquidGlassEnabled;

    public GlassSettings(boolean isBlurEnabled, boolean isLiquidGlassEnabled) {
        this.isBlurEnabled = isBlurEnabled;
        this.isLiquidGlassEnabled = isLiquidGlassEnabled;
    }

    /** This is deliberately separate from Telegram's chat-only Lite Mode Liquid Glass setting. */
    public static boolean isGlassyBuild() {
        return ApplicationLoader.applicationContext != null
                && ApplicationLoader.applicationContext.getPackageName().endsWith(".glassy");
    }

    public static boolean isSupported() {
        return isGlassyBuild() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S;
    }

    public static boolean isAppWideLiquidGlassEnabled() {
        return isSupported() && MessagesController.getGlobalMainSettings()
                .getBoolean(APP_WIDE_LIQUID_GLASS_KEY, false);
    }

    public static void setAppWideLiquidGlassEnabled(boolean enabled) {
        MessagesController.getGlobalMainSettings().edit()
                .putBoolean(APP_WIDE_LIQUID_GLASS_KEY, enabled)
                .apply();
    }

    /**
     * Rendering is disabled while Lite Mode's power saver is active and on low-class devices.
     * The preference is retained, so the theme resumes when those temporary constraints lift.
     */
    public static boolean canRenderAppWideLiquidGlass() {
        return isAppWideLiquidGlassEnabled()
                && SharedConfig.getDevicePerformanceClass() > SharedConfig.PERFORMANCE_CLASS_LOW
                && !LiteMode.isPowerSaverApplied();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof GlassSettings)) {
            return false;
        }

        GlassSettings settings = (GlassSettings) object;
        return isBlurEnabled == settings.isBlurEnabled
                && isLiquidGlassEnabled == settings.isLiquidGlassEnabled;
    }

    @Override
    public int hashCode() {
        return Objects.hash(isBlurEnabled, isLiquidGlassEnabled);
    }
}
