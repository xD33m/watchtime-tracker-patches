package io.github.xd33m.watchtime.addon;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

/**
 * Sends one {@link Report} to the Watchtime Tracker app's provider (protocol v1) and returns whether the app wants
 * more. Blocking binder call: never on the main thread. Sends nothing but the fields below.
 */
final class ReportSender {
    private static final Uri PROVIDER = Uri.parse("content://io.github.xd33m.watchtimetracker.gaps");

    private final Context context;

    ReportSender(Context appContext) {
        this.context = appContext;
    }

    /** False when the app answered "not wanted", is not installed, or anything failed. */
    boolean send(Report report) {
        try {
            Bundle extras = new Bundle();
            extras.putInt("v", 1);
            extras.putString("kind", "shorts");
            extras.putString("state", report.state);
            if (report.contentId != null) extras.putString("contentId", report.contentId);
            Bundle reply = context.getContentResolver().call(PROVIDER, "report", null, extras);
            return reply != null && reply.getBoolean("wanted", false);
        } catch (Throwable t) {
            return false;
        }
    }
}
