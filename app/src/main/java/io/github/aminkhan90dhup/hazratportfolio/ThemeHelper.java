package io.github.aminkhan90dhup.hazratportfolio;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

/** Light/Dark support without any external library:
 *  the whole app reads @color values, and res/values-night provides the dark set.
 *  The user's choice is applied by overriding the UI mode configuration. */
public class ThemeHelper {
    public static final int MODE_SYSTEM = 0, MODE_LIGHT = 1, MODE_DARK = 2;
    private static final String PREFS = "ui_prefs", KEY = "ui_mode";

    public static int getMode(Context c) {
        return c.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getInt(KEY, MODE_SYSTEM);
    }
    public static void setMode(Context c, int mode) {
        SharedPreferences sp = c.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        sp.edit().putInt(KEY, mode).apply();
    }
    public static boolean isDark(Context c) {
        int m = getMode(c);
        if (m == MODE_DARK) return true;
        if (m == MODE_LIGHT) return false;
        int cur = c.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return cur == Configuration.UI_MODE_NIGHT_YES;
    }
    public static Context wrap(Context base) {
        int m = getMode(base);
        if (m == MODE_SYSTEM) return base;
        Configuration cfg = new Configuration(base.getResources().getConfiguration());
        cfg.uiMode = (cfg.uiMode & ~Configuration.UI_MODE_NIGHT_MASK)
            | (m == MODE_DARK ? Configuration.UI_MODE_NIGHT_YES : Configuration.UI_MODE_NIGHT_NO);
        return base.createConfigurationContext(cfg);
    }
}
