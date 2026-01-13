package sourav.sourav.zepto;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orderList;

    public OrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.productName.setText(order.getProductName());
        holder.productPrice.setText(order.getProductPrice());
        holder.productImage.setImageResource(order.getProductImage());
        holder.quantity.setText(order.getQuantity());
        holder.orderDate.setText("Ordered on: " + order.getOrderDate());
        
        String status = order.getStatus();
        holder.status.setText(status);

        // Apply dynamic color-coded status backgrounds
        if (status.equalsIgnoreCase("Pending")) {
            holder.status.setBackgroundResource(R.drawable.bg_status_pending);
        } else if (status.equalsIgnoreCase("Shipped")) {
            holder.status.setBackgroundResource(R.drawable.bg_status_shipped);
        } else if (status.equalsIgnoreCase("Delivered")) {
            holder.status.setBackgroundResource(R.drawable.bg_status_delivered);
        }

        holder.itemView.setOnClickListener(v -> {
            String nextStatus;
            String current = order.getStatus();
            
            if (current.equals("Pending")) nextStatus = "Shipped";
            else if (current.equals("Shipped")) nextStatus = "Delivered";
            else return; 

            DatabaseHelper db = new DatabaseHelper(v.getContext());
            db.updateOrderStatus(order.getId(), nextStatus);
            
            NotificationHelper.sendNotification(v.getContext(), 
                    "Order Update", 
                    "Your order for " + order.getProductName() + " is now " + nextStatus + "!");
            
            Toast.makeText(v.getContext(), "Status updated to " + nextStatus, Toast.LENGTH_SHORT).show();
            
            orderList.set(position, new Order(order.getId(), order.getProductName(), order.getProductPrice(), 
                    order.getProductImage(), order.getQuantity(), order.getOrderDate(), nextStatus));
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice, quantity, orderDate, status;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.order_product_image);
            productName = itemView.findViewById(R.id.order_product_name);
            productPrice = itemView.findViewById(R.id.order_product_price);
            quantity = itemView.findViewById(R.id.order_quantity);
            orderDate = itemView.findViewById(R.id.order_date);
            status = itemView.findViewById(R.id.order_status);
        }
    }
}
