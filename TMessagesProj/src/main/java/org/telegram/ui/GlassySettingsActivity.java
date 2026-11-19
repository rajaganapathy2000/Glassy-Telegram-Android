package org.telegram.ui;

import static org.telegram.messenger.LocaleController.getString;

import android.content.Context;
import android.view.View;

import org.telegram.messenger.R;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;
import org.telegram.ui.Components.UniversalFragment;
import org.telegram.utils.glass.GlassEngine;
import org.telegram.utils.glass.GlassSettings;

import java.util.ArrayList;

/** Settings owned by the Glassy variant; Telegram's Lite Mode chat effects are intentionally not changed here. */
public class GlassySettingsActivity extends UniversalFragment {
    private static final int ID_APP_WIDE_LIQUID_GLASS = 1;

    @Override
    protected boolean useSimpleActionBar() {
        return true;
    }

    @Override
    public View createView(Context context) {
        setHasOwnBackground(true);
        fragmentView = super.createView(context);
        listView.setSections();
        listView.setSectionsDrawBackground(true);
        return fragmentView;
    }

    @Override
    protected RecyclerListView getListViewForSimpleGlass() {
        return listView;
    }

    @Override
    public boolean isSupportEdgeToEdge() {
        return true;
    }

    @Override
    protected CharSequence getTitle() {
        return getString(R.string.GlassySettings);
    }

    @Override
    protected void fillItems(ArrayList<UItem> items, UniversalAdapter adapter) {
        final boolean supported = GlassSettings.isSupported();
        items.add(UItem.asHeader(getString(R.string.GlassySettings)));
        items.add(UItem.asCheck(ID_APP_WIDE_LIQUID_GLASS, getString(R.string.GlassyLiquidGlass))
                .setChecked(GlassSettings.isAppWideLiquidGlassEnabled())
                .setEnabled(supported));
        items.add(UItem.asShadow(getString(supported ? R.string.GlassyLiquidGlassInfo : R.string.GlassyLiquidGlassUnsupported)));
    }

    @Override
    protected void onClick(UItem item, View view, int position, float x, float y) {
        if (item.id != ID_APP_WIDE_LIQUID_GLASS || !GlassSettings.isSupported()) {
            return;
        }
        GlassSettings.setAppWideLiquidGlassEnabled(!GlassSettings.isAppWideLiquidGlassEnabled());
        GlassEngine.resetGlassSettings();
        GlassEngine.getCurrentGlassSettings();
        if (listView != null && listView.adapter != null) {
            listView.adapter.update(true);
        }
    }

    @Override
    protected boolean onLongClick(UItem item, View view, int position, float x, float y) {
        return false;
    }
}
