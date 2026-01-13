package sourav.sourav.zepto;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import sourav.sourav.zepto.R;

public class ProductDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        ImageView img = findViewById(R.id.detail_image);
        TextView name = findViewById(R.id.detail_name);
        TextView price = findViewById(R.id.detail_price);
        TextView desc = findViewById(R.id.detail_desc);
        Button addBtn = findViewById(R.id.detail_add_btn);

        String productName = getIntent().getStringExtra("name");
        int productImage = getIntent().getIntExtra("image", 0);
        String productPrice = getIntent().getStringExtra("price");
        String productMrp = getIntent().getStringExtra("mrp");
        String productQty = getIntent().getStringExtra("qty");

        name.setText(productName);
        price.setText(productPrice);
        img.setImageResource(productImage);
        desc.setText("Fresh " + productName + " available at the best price. Quality guaranteed.");

        addBtn.setOnClickListener(v -> {
            CartManager.getInstance().addToCart(new Product(productName, productImage, productPrice, productMrp, productQty));
            Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show();
        });
    }
}
