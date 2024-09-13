package miniproject;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;


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

  // 테이블 생성
  public static void createTable(Connection conn) throws SQLException {
    // Product 테이블 생성
    String sql = "CREATE TABLE IF NOT EXISTS Product (\n" +
            "    id INT AUTO_INCREMENT PRIMARY KEY,       -- 상품의 고유 ID (자동 증가)\n" +
            "    product_Name VARCHAR(100) NOT NULL,      -- 상품 이름 (최대 100자)\n" +
            "    product_Price DECIMAL(10, 2) NOT NULL,   -- 상품 가격 (정수 8자리, 소수 2자리)\n" +
            "    product_Info TEXT,                       -- 상품 정보 (길이 제한이 없는 텍스트)\n" +
            "    product_Serving VARCHAR(50),             -- 서빙 정보 (최대 50자)\n" +
            "    product_Allergy VARCHAR(100),             -- 알레르기 정보 (최대 100자)\n" +
            "    product_Calorie VARCHAR(45)              -- 칼로리 정보 (최대 45자)\n" +
            ");\n";
    PreparedStatement ps = conn.prepareStatement(sql);
    ps.executeUpdate();

    // orderHeader 테이블 생성
    sql = "CREATE TABLE IF NOT EXISTS Order_Header (\n" +
            "    order_Id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,\n" +
            "    order_TotalQuantity INT,\n" +
            "    order_Price INT,\n" +
            "    order_Date DATE,\n" +
            "    member_Id INT\n" +
            ");\n";
    ps = conn.prepareStatement(sql);
    ps.executeUpdate();

    // orderDetail 테이블 생성
    sql = "CREATE TABLE IF NOT EXISTS Order_Detail (\n" +
            "    seq_No INT PRIMARY KEY NOT NULL AUTO_INCREMENT,\n" +
            "    order_Id INT,\n" +
            "    product_Id INT,\n" +
            "    order_Quantity INT\n" +
            ");\n";
    ps = conn.prepareStatement(sql);
    ps.executeUpdate();

    // member 테이블 생성
    sql = "CREATE TABLE IF NOT EXISTS Member (\n" +
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
            {"Ice Americano", "3000", "KOSTA커피 블렌드 원두로 추출한 에스프레소에 물을 더해, 풍부한 바디감을 느낄 수 있는 스탠다드 커피", "One size / 24oz", "고카페인 함유", "25"},
            {"Ice Cafelatte", "3500", "진한 에스프레소와 부드러운 우유가 어우러져 고소한 풍미를 완성한 라떼", "One size / 20oz", "우유, 고카페인 함유", "200"},
            {"Ice Tea", "4000", "깊은 맛의 홍차와의 은은한 향이 어우러진 시원한 여름철 인기 음료", "One size / 24oz", "복숭아, 아황산류", "100"},
            {"Americano", "2500", "KOSTA커피 블렌드 원두로 추출한 에스프레소에 물을 더해, 풍부한 바디감을 느낄 수 있는 스탠다드 커피", "One size / 20oz", "고카페인 함유", "25"},
            {"Cafelatte", "3500", "진한 에스프레소와 부드러운 우유가 어우러져 고소한 풍미를 완성한 라떼", "One size / 20oz", "우유, 고카페인 함유", "200"},
            {"Tea", "3500", "상큼한 레몬의 맛과 향을 오롯이 살린 비타민C 가득한 과일티", "One size / 20oz", "없음", "100"}
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

  // getPersonInfos 메서드: Product 객체 리스트 반환
  public static List<Product> getProductInfos(Connection conn) throws SQLException {
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
      Product product = new Product(productName, productPrice, productInfo, productServing, productAllergy, productCalorie);
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
  public static void addNewOrder(Connection conn, int order_TotalQty, int order_Price, Date order_Date){
    String sql = "INSERT INTO `cafe`.`Order_Header` (`order_TotalQuantity`, `order_Price`, `order_Date`) VALUES (?, ?, ?);";

    try {
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setInt(1, order_TotalQty);
      ps.setInt(2, order_Price);
      ps.setDate(3, order_Date);
      // 현재 에러 상태
//      ps.setInt(4, member_Id);
      ps.executeUpdate();
      System.out.println("Order successfully added.");
    }
    catch (SQLException e) {

      System.out.println("SQL error while running888: " + e.getMessage());
    }
  }
}


