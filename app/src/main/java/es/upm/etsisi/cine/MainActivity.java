package es.upm.etsisi.cine;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.push.DeviceRegistrationResult;
import com.google.firebase.FirebaseApp;
import es.upm.etsisi.cine.backend.Claves;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        Backendless.initApp(this, Claves.APP_IP, Claves.ANDROID_API_KEY);

        BackendlessUser user = new BackendlessUser();
        user.setProperty("email", "lfmingo@gmail.com");
        user.setPassword("1234");


        if (Backendless.UserService.CurrentUser()==null) {

            /*Backendless.UserService.register(
                    user,
                    new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser response) {
                            Log.e("OK", response.getEmail());
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Log.e("FALLO", fault.toString());
                        }
                    }
            );*/
            Backendless.UserService.login("lfmingo@gmail.com",
                    "1234",
                    new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser response) {
                            Log.e("OK", response.getEmail());
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Log.e("FALLO", fault.toString());
                        }
                    });
        //} else {


        }

        Log.e("KkKKKK", "registrando");
        List<String> channels = new ArrayList<String>();
        channels.add( "default" );
        Backendless.Messaging.registerDevice(channels, new AsyncCallback<DeviceRegistrationResult>() {
            @Override
            public void handleResponse(DeviceRegistrationResult response) {
                Toast.makeText( getApplicationContext(), "Device registered!",
                        Toast.LENGTH_LONG).show();
                Log.e("ok", "registrando");
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText( getApplicationContext(), "Error registering " + fault.getMessage(),
                        Toast.LENGTH_LONG).show();
                Log.e("error", "registrando");
            }
        });

    }
}
