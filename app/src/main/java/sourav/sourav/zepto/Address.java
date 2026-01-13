package sourav.sourav.zepto;

public class Address {
    private int id;
    private String name;
    private String phone;
    private String detail;

    public Address(int id, String name, String phone, String detail) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.detail = detail;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getDetail() { return detail; }
}
