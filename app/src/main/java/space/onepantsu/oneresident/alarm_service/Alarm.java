package space.onepantsu.oneresident.alarm_service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class Alarm extends Service {
    public Alarm() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}