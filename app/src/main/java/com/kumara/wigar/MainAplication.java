package com.kumara.wigar;

import android.app.Application;
import android.content.Context;

import com.kumara.wigar.Helper.LocalHelper;

public class MainAplication extends Application {

    //press Ctrl+o

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocalHelper.onAttach(base,"en"));
    }
}
