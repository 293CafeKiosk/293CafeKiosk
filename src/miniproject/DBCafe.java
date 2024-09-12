package miniproject;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;


class Product{
  private int id;
  private String name;
  private double price;
  private String info;
  private String serving;
  private String allergy;

  public Product(String name, double price, String info, String serving, String allergy) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.info = info;
    this.serving = serving;
    this.allergy = allergy;
  }

  public int getId() {return id;}

  public void setId(int id) {this.id = id;}

  public String getName() {return name;}

  public void setName(String name) {this.name = name;}

  public double getPrice() {return price;}

  public void setPrice(double price) {this.price = price;}

  public String getInfo() {return info;}

  public void setInfo(String info) {this.info = info;}

  public String getServing() {return serving;}

  public void setServing(String serving) {this.serving = serving;}

  public String getAllergy() {return allergy;}

  public void setAllergy(String allergy) {this.allergy = allergy;}
}

public class DBCafe {
  // Main 메서드에서 Connection을 생성하고 데이터베이스 연결을 테스트합니다.
  public static void main(String[] args) throws SQLException {
    Connection conn = makeConnection();
    List<Product> products = getPersonInfos(conn);

    // 가져온 product 목록을 출력합니다.
    for (Product product : products) {
      System.out.println(product);
    }
  }

  // getPersonInfos 메서드: Product 객체 리스트 반환
  public static List<Product> getPersonInfos(Connection conn) throws SQLException {
    // productList를 명시적으로 초기화
    List<Product> productList = new ArrayList<>();
    String sql = "select product_Name, product_Price, product_Info, product_Serving, product_Allergy from Product";
    PreparedStatement ps = conn.prepareStatement(sql);
    ResultSet rs = ps.executeQuery();

    // ResultSet에서 데이터를 가져와 Product 객체로 만들고 리스트에 추가
    while (rs.next()) {
      String productName = rs.getString("product_Name");
      double productPrice = rs.getDouble("product_Price");
      String productInfo = rs.getString("product_Info");
      String productServing = rs.getString("product_Serving");
      String productAllergy = rs.getString("product_Allergy");
//      String productCalorie = rs.getString("product_Calorie");

      // Product 객체 생성 후 리스트에 추가
      Product product = new Product(productName, productPrice, productInfo,productServing,productAllergy);
      productList.add(product);
    }
    rs.close();
    return productList; // productList를 반환
  }

  // 데이터베이스 연결 메서드
  public static Connection makeConnection() {
    String url = "jdbc:mysql://localhost/cafe?serverTimezone=Asia/Seoul";
    Connection conn = null;
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      System.out.println("데이터베이스 연결중 ...");
      conn = DriverManager.getConnection(url, "root", "0000");
      System.out.println("데이터베이스 연결 성공");
    } catch (ClassNotFoundException e) {
      System.out.println("JDBC 드라이버 검색 오류");
    } catch (SQLException e) {
      System.out.println("데이터베이스 연결 실패");
    }
    return conn;
  }
}
