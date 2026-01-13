package sourav.sourav.zepto;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import sourav.sourav.zepto.R;

public class AddressManagementActivity extends AppCompatActivity implements AddressAdapter.OnAddressClickListener {

    private EditText etName, etPhone, etAddress;
    private Button btnSave;
    private RecyclerView rvAddresses;
    private DatabaseHelper dbHelper;
    private List<Address> addressList;
    private AddressAdapter adapter;
    private int selectedAddressId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_management);

        dbHelper = new DatabaseHelper(this);
        addressList = new ArrayList<>();

        etName = findViewById(R.id.et_name);
        etPhone = findViewById(R.id.et_phone);
        etAddress = findViewById(R.id.et_address);
        btnSave = findViewById(R.id.btn_save_address);
        rvAddresses = findViewById(R.id.rv_addresses);

        rvAddresses.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AddressAdapter(addressList, this);
        rvAddresses.setAdapter(adapter);

        btnSave.setOnClickListener(v -> saveAddress());

        loadAddresses();
    }

    private void loadAddresses() {
        addressList.clear();
        Cursor cursor = dbHelper.getAllAddresses();
        if (cursor.moveToFirst()) {
            do {
                addressList.add(new Address(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }

    private void saveAddress() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String detail = etAddress.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || detail.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedAddressId == -1) {
            dbHelper.addAddress(name, phone, detail);
            Toast.makeText(this, "Address Saved", Toast.LENGTH_SHORT).show();
        } else {
            dbHelper.updateAddress(selectedAddressId, name, phone, detail);
            Toast.makeText(this, "Address Updated", Toast.LENGTH_SHORT).show();
            selectedAddressId = -1;
            btnSave.setText("Save Address");
        }

        etName.setText("");
        etPhone.setText("");
        etAddress.setText("");
        loadAddresses();
    }

    @Override
    public void onEditClick(Address address) {
        selectedAddressId = address.getId();
        etName.setText(address.getName());
        etPhone.setText(address.getPhone());
        etAddress.setText(address.getDetail());
        btnSave.setText("Update Address");
    }

    @Override
    public void onDeleteClick(Address address) {
        dbHelper.deleteAddress(address.getId());
        Toast.makeText(this, "Address Deleted", Toast.LENGTH_SHORT).show();
        loadAddresses();
    }
}
