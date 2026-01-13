package sourav.sourav.zepto;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import sourav.sourav.zepto.R;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {

    private List<Address> addressList;
    private OnAddressClickListener listener;

    public interface OnAddressClickListener {
        void onEditClick(Address address);
        void onDeleteClick(Address address);
    }

    public AddressAdapter(List<Address> addressList, OnAddressClickListener listener) {
        this.addressList = addressList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address, parent, false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        Address address = addressList.get(position);
        holder.name.setText(address.getName());
        holder.phone.setText(address.getPhone());
        holder.detail.setText(address.getDetail());

        holder.edit.setOnClickListener(v -> listener.onEditClick(address));
        holder.delete.setOnClickListener(v -> listener.onDeleteClick(address));
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public static class AddressViewHolder extends RecyclerView.ViewHolder {
        TextView name, phone, detail, edit, delete;

        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_item_name);
            phone = itemView.findViewById(R.id.tv_item_phone);
            detail = itemView.findViewById(R.id.tv_item_detail);
            edit = itemView.findViewById(R.id.tv_edit);
            delete = itemView.findViewById(R.id.tv_delete);
        }
    }
}
