package com.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.driver.hire_me.BuildConfig;
import com.driver.hire_me.ITrackRecordingService;
import com.driver.hire_me.R;


/**
 * Created by grepixinfotech on 09/11/17.
 */

public class TrackRecordingServiceConnection {
    private static final String TAG = TrackRecordingServiceConnection.class.getSimpleName();
    private final IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            Log.d(TAG, "Service died.");
            setTrackRecordingService(null);
        }
    };


    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.i(TAG, "Connected to the service.");
            try {
                service.linkToDeath(deathRecipient, 0);
            } catch (RemoteException e) {
                Log.e(TAG, "Failed to bind a death recipient.", e);
            }
            setTrackRecordingService(ITrackRecordingService.Stub.asInterface(service));
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            Log.i(TAG, "Disconnected from the service.");
            setTrackRecordingService(null);
        }
    };


    private final Context context;
    private final Runnable callback;
    private ITrackRecordingService trackRecordingService;


    /**
     * Constructor.
     *
     * @param context  the context
     * @param callback the callback to invoke when the service binding changes
     */
    public TrackRecordingServiceConnection(Context context, Runnable callback) {
        this.context = context;
        this.callback = callback;
    }


    /**
     * Starts and binds the service.
     */
    public void startAndBind() {
        PreferencesUtils.setBoolean(context, R.string.is_recording_start, true);
        PreferencesUtils.setFloat(context, R.string.recorded_distance, 0);
        bindService(true);
    }

    /**
     * Binds the service if it is started.
     */
    public void bindIfStarted() {
        bindService(false);
    }

    /**
     * Unbinds and stops the service.
     */
    public void unbindAndStop() {
        unbind();
        context.stopService(new Intent(context, TrackRecordingService.class));
    }

    /**
     * Unbinds the service (but leave it running).
     */
    public void unbind() {
        try {
            context.unbindService(serviceConnection);
        } catch (IllegalArgumentException e) {
            // Means not bound to the service. OK to ignore.
        }
        setTrackRecordingService(null);
    }

    /**
     * Gets the track recording service if bound. Returns null otherwise
     */
    public ITrackRecordingService getServiceIfBound() {
        if (trackRecordingService != null && !trackRecordingService.asBinder().isBinderAlive()) {
            setTrackRecordingService(null);
            return null;
        }
        return trackRecordingService;
    }

    /**
     * Binds the service if it is started.
     *
     * @param startIfNeeded start the service if needed
     */
    private void bindService(boolean startIfNeeded) {
        if (trackRecordingService != null) {
            // Service is already started and bound.
            return;
        }



        if (startIfNeeded) {
            Log.i(TAG, "Starting the service.");
            context.startService(new Intent(context, TrackRecordingService.class));
        }

        Log.i(TAG, "Binding the service.");
        int flags = BuildConfig.DEBUG ? Context.BIND_DEBUG_UNBIND : 0;
        context.bindService(new Intent(context, TrackRecordingService.class), serviceConnection, flags);
    }

    /**
     * Sets the trackRecordingService.
     *
     * @param value the value
     */
    private void setTrackRecordingService(ITrackRecordingService value) {
        trackRecordingService = value;
        if (callback != null) {
            callback.run();
        }
    }


}

