package ssu.cheesecake.blueberry;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

public class CameraActivity extends AppCompatActivity {

    private static final String TAG = "\n*****[ Blueberry : CameraFragment ]*****\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera);

        if (null == savedInstanceState) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, CameraFragment.newInstance())
                    .commit();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"CameraActivity onStop!!!");
    }
}
