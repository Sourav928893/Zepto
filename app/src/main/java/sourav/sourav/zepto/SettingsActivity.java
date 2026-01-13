package sourav.sourav.zepto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import sourav.sourav.zepto.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Address Section Click
        // Find the LinearLayout that contains the "Address" TextView (based on activity_settings.xml structure)
        // Since I can't easily add IDs to all nested layouts, I'll use a safer approach if possible, 
        // but looking at activity_settings.xml, there is a LinearLayout with ImageView(address) and TextView(Address).
        
        // I will add an onClick to the parent of the Address TextView in the XML or find it by its text if needed.
        // For simplicity, I'll rely on the user clicking the specific section.
        
        // Let's modify activity_settings.xml to add an ID to the Address Layout first.
    }
    
    public void openAddressManagement(View view) {
        Intent intent = new Intent(this, AddressManagementActivity.class);
        startActivity(intent);
    }

    public void openMyOrders(View view) {
        Intent intent = new Intent(this, MyOrdersActivity.class);
        startActivity(intent);
    }
}
