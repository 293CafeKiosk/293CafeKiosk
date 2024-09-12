package miniproject;

import miniproject.Product;

import java.sql.SQLException;
import java.util.List;

public class ProductManager {
  public static void main(String[] args) throws SQLException {
    // DBDemo 클래스에서 Product 리스트 가져오기
    List<Product> products = DataBase.getPersonInfos(DataBase.makeConnection());

    // Product 데이터를 처리
    for (Product product : products) {
      System.out.println("Product Name: " + product.getName());
      System.out.println("Product Price: " + product.getPrice());
    }
  }
}
