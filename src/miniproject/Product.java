package miniproject;

class Product {
    private int id;
    private String name;
    private double price;
    private String info;
    private String serving;
    private String allergy;
    private String calorie;

    public Product(String name, double price, String info, String serving, String allergy, String calorie) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.info = info;
        this.serving = serving;
        this.allergy = allergy;
        this.calorie = calorie;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getServing() {
        return serving;
    }

    public void setServing(String serving) {
        this.serving = serving;
    }

    public String getAllergy() {
        return allergy;
    }

    public void setAllergy(String allergy) {
        this.allergy = allergy;
    }

    public String getCalorie() {
        return calorie;
    }

    public void setCalorie(String calorie) {
        this.calorie = calorie;
    }
}
