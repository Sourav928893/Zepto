package sourav.sourav.zepto;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import sourav.sourav.zepto.R;

public class PaymentActivity extends AppCompatActivity {
    private int totalAmount;
    private TextView tvTotalAmount;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        dbHelper = new DatabaseHelper(this);
        tvTotalAmount = findViewById(R.id.total_amount_textview);

        totalAmount = getIntent().getIntExtra("TOTAL_AMOUNT", 0);
        tvTotalAmount.setText("Total Amount: ₹" + totalAmount);
    }

    public void payWithGPay(View view) {
        processOrder();
        startUPIPayment("your_upi_id@okicici", "Google Pay");
    }

    public void payWithPhonePe(View view) {
        processOrder();
        startUPIPayment("your_upi_id@ybl", "PhonePe");
    }

    public void payWithPaytm(View view) {
        processOrder();
        startUPIPayment("your_upi@ptsbi", "Paytm");
    }

    private void processOrder() {
        // Save cart items as orders in SQLite
        List<Product> cartItems = CartManager.getInstance().getCartItems();
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        
        for (Product product : cartItems) {
            dbHelper.addOrder(
                    product.getName(),
                    product.getPrice(),
                    product.getImageResId(),
                    product.getQuantity(),
                    currentDate,
                    "Pending"
            );
        }
        
        // Clear cart after placing order
        CartManager.getInstance().clearCart();
        Toast.makeText(this, "Order Placed Successfully!", Toast.LENGTH_LONG).show();
    }

    private void startUPIPayment(String upiId, String appName) {
        try {
            Uri uri = Uri.parse("upi://pay?pa=" + upiId +
                    "&pn=Zepto User" +
                    "&mc=0000" +
                    "&tid=021254" +
                    "&tr=123456789" +
                    "&tn=Grocery Order" +
                    "&am=" + totalAmount +
                    "&cu=INR");

            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, appName + " app not found", Toast.LENGTH_SHORT).show();
        }
    }
}
