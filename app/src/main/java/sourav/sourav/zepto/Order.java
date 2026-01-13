package sourav.sourav.zepto;

public class Order {
    private int id;
    private String productName;
    private String productPrice;
    private int productImage;
    private String quantity;
    private String orderDate;
    private String status;

    public Order(int id, String productName, String productPrice, int productImage, String quantity, String orderDate, String status) {
        this.id = id;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImage = productImage;
        this.quantity = quantity;
        this.orderDate = orderDate;
        this.status = status;
    }

    public int getId() { return id; }
    public String getProductName() { return productName; }
    public String getProductPrice() { return productPrice; }
    public int getProductImage() { return productImage; }
    public String getQuantity() { return quantity; }
    public String getOrderDate() { return orderDate; }
    public String getStatus() { return status; }
}
