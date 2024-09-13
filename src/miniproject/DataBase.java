package miniproject;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Product{
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

  public String getCalorie() {return calorie;}

  public void setCalorie(String calorie) {this.calorie = calorie;}
}

public class DataBase {


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
    String sql = "select product_Name, product_Price, product_Info, product_Serving, product_Allergy, product_Calorie from Product";
    PreparedStatement ps = conn.prepareStatement(sql);
    ResultSet rs = ps.executeQuery();

    // ResultSet에서 데이터를 가져와 Product 객체로 만들고 리스트에 추가
    while (rs.next()) {
      String productName = rs.getString("product_Name");
      double productPrice = rs.getDouble("product_Price");
      String productInfo = rs.getString("product_Info");
      String productServing = rs.getString("product_Serving");
      String productAllergy = rs.getString("product_Allergy");
      String productCalorie = rs.getString("product_Calorie");

      // Product 객체 생성 후 리스트에 추가
      Product product = new Product(productName, productPrice, productInfo,productServing,productAllergy, productCalorie);
      productList.add(product);
    }
    rs.close();
    return productList; // productList를 반환
  }
  ////// 자카님 작업
  public static void addNewMember(Connection conn, String member_Name, String member_Phone) {
    String sql = "INSERT INTO `cafe`.`Member` (`member_Name`, `member_Phone`) VALUES (?, ?);";
    try {
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1, member_Name);
      ps.setString(2, member_Phone);
      ps.executeUpdate();
      System.out.println("Member successfully added.");
    }
    catch (SQLException e) {

      System.out.println("SQL error while running");
    }
  }

  public static ArrayList<String> getMemberInfo(Connection conn, String member_Phone) {
    String sql = "select member_Name, member_Point from Member where member_Phone = ?;";
    ArrayList<String> result = new ArrayList<>();
    try {
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1, member_Phone);
      ResultSet rs = ps.executeQuery();

      if (!rs.next()) {
        return null;
      }

      else {  // Move cursor to the first row
        result.add(rs.getString(1));// Get the value of the first column (point)
        result.add(rs.getString(2));
      }
      return result;
    }
    catch (SQLException e) {
      e.printStackTrace();  // SQL 예외 스택 트레이스를 출력하여 원인 확인
      System.out.println("SQL error while running999: " + e.getMessage()); // 예외 메시지 출력
      return null;
    }
  }

  // 오더 테이블에 들어가기
  public static int addNewOrder(Connection conn, int order_TotalQty, int order_Price, Date order_Date){
    int id = -1;
    String sql = "INSERT INTO `cafe`.`Order_Header` (`order_TotalQty`, `order_Price`, `order_Date`) VALUES (?, ?, ?);";

    try {
      PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setInt(1, order_TotalQty);
      ps.setInt(2, order_Price);
      ps.setDate(3, order_Date);
      // 현재 에러 상태
//      ps.setInt(4, member_Id);
      int affectedRows = ps.executeUpdate();

      if (affectedRows > 0) {
        try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
          if (generatedKeys.next()) {
            id = generatedKeys.getInt(1); // 생성된 ID 값
            System.out.println("id : " + id);
          }
        }
      }
      System.out.println("Order successfully added.");

    }
    catch (SQLException e) {
      System.out.println("SQL error while running888: " + e.getMessage());
    }
    return id;
  }


  // 오더 디테일에 들어가기
  public static void addNewOrderDetail(Connection conn, int order_Id, int product_Id, int order_Qty ){
    String sql = "INSERT INTO `cafe`.`Order_Detail` (`order_Id`, `product_Id`, `order_Qty`) VALUES (?, ?, ?);";

    try {
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setInt(1, order_Id);
      ps.setInt(2, product_Id);
      ps.setInt(3, order_Qty);
      ps.executeUpdate();
    }catch (SQLException e) {
      e.printStackTrace();
      System.out.println("sql에러 : " + e.getMessage());
    }
  }


  public static Connection makeConnection() throws SQLException {
    String url = "jdbc:mysql://localhost/cafe?serverTimezone=Asia/Seoul";
    Connection conn = null;
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      System.out.println("Database connecting...");
      conn = DriverManager.getConnection(url, "root", "1234");
      System.out.println("Database connection successful");
    }
    catch (ClassNotFoundException e) {
      System.out.println("JDBC driver search error");
    }
    catch (SQLException e) {
      System.out.println("Database connection failed");
    }
    return conn;
  }
}