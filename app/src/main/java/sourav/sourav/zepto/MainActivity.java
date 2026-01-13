package sourav.sourav.zepto;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.text.TextUtils;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import sourav.sourav.zepto.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN_ACTIVITY";
    public EditText phoneInput;
    public Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        askNotificationPermission();

        // Get FCM Token
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        Log.d(TAG, "FCM Token: " + token);
                        // Aap yahan toast bhi dikha sakte hain testing ke liye
                        // Toast.makeText(MainActivity.this, "Token generated", Toast.LENGTH_SHORT).show();
                    }
                });

        // Subscribe to promotions topic
        FirebaseMessaging.getInstance().subscribeToTopic("promotions")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribed to promotions";
                        if (!task.isSuccessful()) {
                            msg = "Subscribe failed";
                        }
                        Log.d(TAG, msg);
                    }
                });


        phoneInput = findViewById(R.id.phoneInput);
        continueButton = findViewById(R.id.continueButton);

        TextView termsText = findViewById(R.id.termText);
        String fullText = "By continuing, you agree to our \n Terms of Use & Privacy Policy";
        SpannableString spannable = new SpannableString(fullText);


        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#FF7050")),
                32, fullText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        termsText.setText(spannable);


        continueButton.setOnClickListener(v -> {

            String phoneNumber = phoneInput.getText().toString().trim();

            if (TextUtils.isEmpty(phoneNumber)) {

                Toast.makeText(MainActivity.this, "Please enter a phone number", Toast.LENGTH_SHORT).show();
            } else if (phoneNumber.length() < 10) {

                Toast.makeText(MainActivity.this, "Phone number must be at least 10 digits", Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(MainActivity.this, "Phone number entered: " + phoneNumber, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, ProductPage.class);
                intent.putExtra("phone_number", phoneNumber);
                startActivity(intent);

            }
        });


    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Toast.makeText(this, "Notifications allowed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Notifications denied", Toast.LENGTH_SHORT).show();
                }
            });

    private void askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // Permission already granted
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }
}
