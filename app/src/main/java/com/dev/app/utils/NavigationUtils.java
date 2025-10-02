package com.dev.app.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class NavigationUtils {

    public static void redirectToActivity(Context context, Class<?> targetActivity, Bundle extras) {
        Intent intent = new Intent(context, targetActivity);

        if (extras != null) {
            intent.putExtras(extras);
        }

        context.startActivity(intent);
    }
}