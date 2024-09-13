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
    createTable(conn);
    insertProducts(conn);
    List<Product> products = getProductInfos(conn);

    // 가져온 product 목록을 출력합니다.
    for (Product product : products) {
      System.out.println(product);
    }
  }

  // getPersonInfos 메서드: Product 객체 리스트 반환
  public static List<Product> getProductInfos(Connection conn) throws SQLException {
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

  // 테이블 생성
  public static void createTable(Connection conn) throws SQLException {
    // Product 테이블 생성
    String sql = "CREATE TABLE IF NOT EXISTS Product (\n" +
            "    id INT AUTO_INCREMENT PRIMARY KEY,       -- 상품의 고유 ID (자동 증가)\n" +
            "    product_Name VARCHAR(100) NOT NULL,      -- 상품 이름 (최대 100자)\n" +
            "    product_Price DECIMAL(10, 2) NOT NULL,   -- 상품 가격 (정수 8자리, 소수 2자리)\n" +
            "    product_Info TEXT,                       -- 상품 정보 (길이 제한이 없는 텍스트)\n" +
            "    product_Serving VARCHAR(50),             -- 서빙 정보 (최대 50자)\n" +
            "    product_Allergy VARCHAR(100)             -- 알레르기 정보 (최대 100자)\n" +
            ");\n";
    PreparedStatement ps = conn.prepareStatement(sql);
    ps.executeUpdate();

    // orderheader 테이블 생성
    sql = "CREATE TABLE IF NOT EXISTS orderheader (\n" +
            "    seq_No INT PRIMARY KEY NOT NULL AUTO_INCREMENT,\n" +
            "    order_Id INT,\n" +
            "    product_Id INT,\n" +
            "    order_Qty INT\n" +
            ");\n";
    ps = conn.prepareStatement(sql);
    ps.executeUpdate();

    // orderdetail 테이블 생성
    sql = "CREATE TABLE IF NOT EXISTS orderdetail (\n" +
            "    order_Id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,\n" +
            "    order_TotalQty INT,\n" +
            "    order_Price INT,\n" +
            "    order_Date DATE,\n" +
            "    member_Id INT\n" +
            ");\n";
    ps = conn.prepareStatement(sql);
    ps.executeUpdate();

    // member 테이블 생성
    sql = "CREATE TABLE IF NOT EXISTS member (\n" +
            "    member_Id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,\n" +
            "    member_Name VARCHAR(45) NOT NULL,\n" +
            "    member_Phone VARCHAR(45),\n" +
            "    member_Point INT DEFAULT '1000'\n" +
            ");\n";
    ps = conn.prepareStatement(sql);
    ps.executeUpdate();
  }


  // Product 목록 추가
  public static void insertProducts(Connection conn) throws SQLException {
    String selectSql = "SELECT COUNT(*) FROM Product WHERE product_Name = ?";
    String insertSql = "INSERT INTO Product (product_Name, product_Price, product_Info, product_Serving, product_Allergy) " +
            "VALUES (?, ?, ?, ?, ?)";

    PreparedStatement selectPs = conn.prepareStatement(selectSql);
    PreparedStatement insertPs = conn.prepareStatement(insertSql);

    // 제품 데이터 배열
    String[][] products = {
            {"Ice Americano", "3000", "KOSTA커피 블렌드 원두로 추출한 에스프레소에 물을 더해, 풍부한 바디감을 느낄 수 있는 스탠다드 커피", "One size / 24oz", "고카페인 함유"},
            {"Ice Cafelatte", "3500", "진한 에스프레소와 부드러운 우유가 어우러져 고소한 풍미를 완성한 라떼", "One size / 20oz", "우유, 고카페인 함유"},
            {"Ice Tea", "4000", "깊은 맛의 홍차와의 은은한 향이 어우러진 시원한 여름철 인기 음료", "One size / 24oz", "복숭아, 아황산류"},
            {"Americano", "2500", "KOSTA커피 블렌드 원두로 추출한 에스프레소에 물을 더해, 풍부한 바디감을 느낄 수 있는 스탠다드 커피", "One size / 20oz", "고카페인 함유"},
            {"Cafelatte", "3500", "진한 에스프레소와 부드러운 우유가 어우러져 고소한 풍미를 완성한 라떼", "One size / 20oz", "우유, 고카페인 함유"},
            {"Tea", "3500", "상큼한 레몬의 맛과 향을 오롯이 살린 비타민C 가득한 과일티", "One size / 20oz", "없음"}
    };

    for (String[] product : products) {
      // 제품 이름으로 존재 여부 확인
      selectPs.setString(1, product[0]);
      ResultSet rs = selectPs.executeQuery();
      rs.next();
      int count = rs.getInt(1);

      // 제품이 존재하지 않을 경우 삽입
      if (count == 0) {
        insertPs.setString(1, product[0]);
        insertPs.setDouble(2, Double.parseDouble(product[1]));
        insertPs.setString(3, product[2]);
        insertPs.setString(4, product[3]);
        insertPs.setString(5, product[4]);
        insertPs.executeUpdate();
      }
    }

    System.out.println("Products inserted successfully");
  }



  // 데이터베이스 연결 메서드
  public static Connection makeConnection() {
//    String url = "jdbc:mysql://localhost/cafe?serverTimezone=Asia/Seoul";
    String url = "jdbc:mysql://localhost:3306/cafe?serverTimezone=Asia/Seoul";
    Connection conn = null;
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      System.out.println("데이터베이스 연결중 ...");
      conn = DriverManager.getConnection(url, "root", "1111");
      System.out.println("데이터베이스 연결 성공");
    } catch (ClassNotFoundException e) {
      System.out.println("JDBC 드라이버 검색 오류");
    } catch (SQLException e) {
      System.out.println("데이터베이스 연결 실패");
    }
    return conn;
  }
}
