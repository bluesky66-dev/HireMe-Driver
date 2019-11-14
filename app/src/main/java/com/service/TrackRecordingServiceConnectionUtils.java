package com.service;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;

import com.driver.hire_me.ITrackRecordingService;
import com.driver.hire_me.R;
import com.driver.hire_me.SlideMainActivity;

import java.util.List;

/**
 * Created by grepixinfotech on 09/11/17.
 */

public class TrackRecordingServiceConnectionUtils {
    private static final String TAG = TrackRecordingServiceConnectionUtils.class.getSimpleName();

    private TrackRecordingServiceConnectionUtils() {
    }

    /**
     * Returns true if the recording service is running.
     *
     * @param context the current context
     */
    public static boolean isRecordingServiceRunning(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

        for (ActivityManager.RunningServiceInfo serviceInfo : services) {
            ComponentName componentName = serviceInfo.service;
            String serviceName = componentName.getClassName();
            if (TrackRecordingService.class.getName().equals(serviceName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isRecordingServiceRunning(Context context, Class serviceClass) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

        for (ActivityManager.RunningServiceInfo serviceInfo : services) {
            ComponentName componentName = serviceInfo.service;
            String serviceName = componentName.getClassName();
            if (serviceClass.getName().equals(serviceName)) {
                return true;
            }
        }
        return false;
    }
    /**
     * Resumes the recording track.
     *
     * @param trackRecordingServiceConnection the track recording service
     */
    public static void resumeTrack(TrackRecordingServiceConnection trackRecordingServiceConnection) {
        try {
            ITrackRecordingService service = trackRecordingServiceConnection.getServiceIfBound();
            if (service != null) {
                service.resumeCurrentTrack();
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Unable to resume track.", e);
        }
    }

    /**
     * Pauses the recording track.
     *
     * @param trackRecordingServiceConnection the track recording service
     *                                        connection
     */
    public static void pauseTrack(TrackRecordingServiceConnection trackRecordingServiceConnection) {
        try {
            ITrackRecordingService service = trackRecordingServiceConnection.getServiceIfBound();
            if (service != null) {
                service.pauseCurrentTrack();
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Unable to resume track.", e);
        }
    }

    /**
     * Stops the recording.
     *
     * @param context                         the context
     * @param trackRecordingServiceConnection the track recording service
     *                                        connection
     * @param showEditor                      true to show the editor
     */
    public static void stopRecording(Context context,
                                     TrackRecordingServiceConnection trackRecordingServiceConnection, boolean showEditor) {
        ITrackRecordingService trackRecordingService = trackRecordingServiceConnection
                .getServiceIfBound();
        if (trackRecordingService != null) {
            try {

                if (showEditor) {
          /*
           * Need to remember the recordingTrackId before calling
           * endCurrentTrack. endCurrentTrack sets the value to -1L.
           */
                    long recordingTrackId = PreferencesUtils.getLong(
                            context, R.string.recording_track_id_key);
                    trackRecordingService.endCurrentTrack();
                    if (recordingTrackId != PreferencesUtils.RECORDING_TRACK_ID_DEFAULT) {
                        Intent intent = new Intent(context, SlideMainActivity.class)
                                .putExtra("EXTRA_TRACK_ID", recordingTrackId)
                                .putExtra("EXTRA_NEW_TRACK", true);
                        context.startActivity(intent);
                    }
                } else {
                    trackRecordingService.endCurrentTrack();
                }
            } catch (Exception e) {
                Log.e(TAG, "Unable to stop recording.", e);
            }
        } else {
            resetRecordingState(context);
        }
        trackRecordingServiceConnection.unbindAndStop();
        PreferencesUtils.setBoolean(context, R.string.is_recording_start, false);
        PreferencesUtils.setFloat(context, R.string.recorded_distance, 0);
    }

    private static void resetRecordingState(Context context) {
        long recordingTrackId = PreferencesUtils.getLong(context, R.string.recording_track_id_key);
        if (recordingTrackId != PreferencesUtils.RECORDING_TRACK_ID_DEFAULT) {
            PreferencesUtils.setLong( context, R.string.recording_track_id_key, PreferencesUtils.RECORDING_TRACK_ID_DEFAULT);
        }
        boolean recordingTrackPaused = PreferencesUtils.getBoolean(context,  R.string.recording_track_paused_key, PreferencesUtils.RECORDING_TRACK_PAUSED_DEFAULT);
        if (!recordingTrackPaused) { PreferencesUtils.setBoolean(context, R.string.recording_track_paused_key,
                    PreferencesUtils.RECORDING_TRACK_PAUSED_DEFAULT);
        }
    }


    /**
     * Resumes the track recording service connection.
     *
     * @param context                         the context
     * @param trackRecordingServiceConnection the track recording service
     *                                        connection
     */
    public static void startConnection(
            Context context, TrackRecordingServiceConnection trackRecordingServiceConnection) {
        trackRecordingServiceConnection.bindIfStarted();
        if (!isRecordingServiceRunning(context)) {
            resetRecordingState(context);
        }
    }


}
