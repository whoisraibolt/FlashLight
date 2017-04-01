package flashlight.raibolttechnology.com.flashlight;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import flashlight.raibolttechnology.com.flashlight.R;


public class MainActivity extends ActionBarActivity {

    private ImageButton flashLight;
    private Camera camera;
    private Parameters parameter;
    private boolean deviceHasFlash;
    private boolean isFlashLightOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        flashLight = (ImageButton) findViewById(R.id.flash_light);
        deviceHasFlash = getApplication().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if (!deviceHasFlash) {
            Toast.makeText(MainActivity.this, "Sorry, you device does not have any camera", Toast.LENGTH_LONG).show();
            return;
        } else {
            this.camera = Camera.open(0);
            parameter = this.camera.getParameters();
        }

        flashLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFlashLightOn) {
                    turnOnTheFlash();
                } else {
                    turnOffTheFlash();
                }
            }
        });
    }

    private void turnOffTheFlash() {
        parameter.setFlashMode(Parameters.FLASH_MODE_OFF);
        this.camera.setParameters(parameter);
        this.camera.stopPreview();
        isFlashLightOn = false;
        flashLight.setImageResource(R.drawable.btn_off);
    }

    private void turnOnTheFlash() {
        if (this.camera != null) {
            parameter = this.camera.getParameters();
            parameter.setFlashMode(Parameters.FLASH_MODE_TORCH);
            this.camera.setParameters(parameter);
            this.camera.startPreview();
            isFlashLightOn = true;
            flashLight.setImageResource(R.drawable.btn_on);
        }
    }

    private void getCamera() {
        if (camera == null) {
            try {
                camera = Camera.open();
                parameter = camera.getParameters();
            } catch (RuntimeException e) {
                System.out.println("Error: Failed to Open: " + e.getMessage());
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (this.camera != null) {
            this.camera.release();
            this.camera = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        turnOffTheFlash();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (deviceHasFlash) {
            turnOffTheFlash();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getCamera();
    }
}