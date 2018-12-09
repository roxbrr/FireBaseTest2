package app.calcounter.com.firebasetest2;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class LoginFireBase extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_fire_base);
        FirebaseAuth auth = FirebaseAuth.getInstance(); // this go into each intent?
        if (auth.getCurrentUser() != null) {
            // already signed in
        } else {
            // AuthUI is built in but does not exist yet?
            startActivityForResult(
                    // Get an instance of AuthUI based on the default app
                    AuthUI.getInstance().createSignInIntentBuilder().build(),
                    RC_SIGN_IN);
        }

        locationManager = (LocationManager) // not normal context
                getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new MyLocationListener();

        // not sure about user request code
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 5000, 10, locationListener);

    }


    public void onClickLogin(View view)
    {

    }

    private void validateLogin()
    {
//        startActivityForResult(
//                // Get an instance of AuthUI based on the default app
//                AuthUI.getInstance().createSignInIntentBuilder().build(),
//                RC_SIGN_IN);




    }

}
