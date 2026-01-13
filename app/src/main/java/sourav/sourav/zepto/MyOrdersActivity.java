package sourav.sourav.zepto;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyOrdersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView emptyText;
    private OrderAdapter adapter;
    private List<Order> orderList;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        recyclerView = findViewById(R.id.rv_orders);
        emptyText = findViewById(R.id.tv_empty_orders);
        dbHelper = new DatabaseHelper(this);
        orderList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        // Customizing adapter to handle clicks for status simulation
        adapter = new OrderAdapter(orderList);
        recyclerView.setAdapter(adapter);

        loadOrders();
        
        Toast.makeText(this, "Tip: Click an order to simulate status change", Toast.LENGTH_LONG).show();
    }

    private void loadOrders() {
        orderList.clear();
        Cursor cursor = dbHelper.getAllOrders();
        if (cursor.getCount() == 0) {
            emptyText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            if (cursor.moveToFirst()) {
                do {
                    orderList.add(new Order(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getInt(3),
                            cursor.getString(4),
                            cursor.getString(5),
                            cursor.getString(6)
                    ));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }
    
    // Logic for simulation of status change is inside OrderAdapter for convenience
}
