package miniproject;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.SQLException;

import java.time.LocalDate;

import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

public class FirstScreen {

  static Connection conn;
  static int total = 0;
  static int totalCount = 0;
  static int pid = 0;
  static int qty = 0;
  static List<Integer> pidList = new ArrayList<>();
  static List<Integer> qtyList = new ArrayList<>();
  // 첫 번째 화면 (인트로 화면)
  public static void createIntroFrame() {
    JFrame introFrame = new JFrame("KOSTA CAFE");

    // 프레임 크기 설정
    introFrame.setSize(800, 1050);
    introFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    introFrame.setLocationRelativeTo(null);

    // back 이미지 생성 (이미지 패널)
    JPanel introPanel = new JPanel() {
      private Image bgImage = new ImageIcon(getClass().getResource("/miniproject/back9.jpg")).getImage();

      @Override
      public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
      }
    };
    introPanel.setLayout(new BorderLayout());

    // 센터 라벨
    JLabel clickLabel = new JLabel("화면을 클릭하세요", SwingConstants.CENTER);
    clickLabel.setFont(new Font("Dialog", Font.BOLD, 50));
    clickLabel.setForeground(Color.WHITE);

    introPanel.add(clickLabel, BorderLayout.CENTER);

    // 회원등록 버튼
    JButton introbutton = new JButton("회원등록");
    introbutton.setBounds(300, 700, 200, 50);
    introbutton.setFont(new Font("Dialog", Font.BOLD, 25)); // 버튼 폰트 설정
    introFrame.add(introbutton);

    introbutton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        createMemberFrame();
      }
    });


    // 인트로화면 클릭 이벤트
    introPanel.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        // 첫 번째 프레임을 닫고 두 번째 프레임을 열기

        try {
          createMainFrame();  // 두 번째 프레임 생성
        } catch (SQLException ex) {
          throw new RuntimeException(ex);
        }

      }
    });

    introFrame.add(introPanel);
    introFrame.setVisible(true);
  }
  // 회원 등록 프레임
  public static void createMemberFrame(){
    JFrame mfrm = new JFrame("회원 등록");
    mfrm.setSize(800, 1050);
    mfrm.setLocationRelativeTo(null);
    mfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    mfrm.setLayout(new BorderLayout());

    // 텍스트 패널 생성
    JPanel mtextPanel = new JPanel();


    // 텍스트 수동배치
    mtextPanel.setLayout(null);
    // 텍스트 패널 위치, 크기
    mtextPanel.setBounds(0,0,800,1050);

    // 라벨 생성
    JLabel nameLabel = new JLabel("이 름");
    JLabel phoneLabel = new JLabel("핸드폰번호");

    // 라벨 위치, 크기, 폰트 설정
    nameLabel.setBounds(200,280,200,50);
    nameLabel.setFont(new Font("Dialog", Font.BOLD, 20));
    phoneLabel.setBounds(200,400,200,50);
    phoneLabel.setFont(new Font("Dialog", Font.BOLD, 20));

    // 버튼 생성
    JButton okButton = new JButton("등 록");
    JButton cancelButton = new JButton("취 소");

    // 버튼 위치, 크기, 폰트 설정
    okButton.setBounds(200, 550, 180, 50);
    okButton.setFont(new Font("Dialog", Font.BOLD, 20));
    cancelButton.setBounds(420, 550, 180, 50);
    cancelButton.setFont(new Font("Dialog", Font.BOLD, 20));

    // 버튼 추가
    mtextPanel.add(okButton);
    mtextPanel.add(cancelButton);

    // 라벨 추가
    mtextPanel.add(nameLabel);
    mtextPanel.add(phoneLabel);

    // 텍스트에어리어 생성
    JTextArea nameTextArea = new JTextArea(1,5);
    JTextArea phoneTextArea = new JTextArea(1, 5);

    nameTextArea.setBounds(200,330,400,30);
    nameTextArea.setFont(new Font("Dialog", Font.BOLD, 20));
    phoneTextArea.setBounds(200,450,400,30);
    phoneTextArea.setFont(new Font("Dialog", Font.BOLD, 20));

    nameTextArea.setEditable(true);
    phoneTextArea.setEditable(true);



    // 패널에 JTextArea 추가
    mtextPanel.add(nameTextArea);
    mtextPanel.add(phoneTextArea);

    mfrm.add(mtextPanel, BorderLayout.CENTER);

    // 등록 클릭 이벤트
    okButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // textArea, textArea2 의 데이터를 DB member 로 이동시키는 코드 작성
        String inputName = nameTextArea.getText();
        String inputPhone = phoneTextArea.getText();

        try {
          conn = DataBase.makeConnection();
        } catch (SQLException ex) {
          throw new RuntimeException(ex);
        }

        if (!inputName.matches("[a-zA-Z가-힣 ]+")) {
          JOptionPane.showMessageDialog(mfrm, "이름에 한글 및 영문자만 입력 가능합니다");
        }
        else if ((!inputPhone.matches("\\d+"))) {
          JOptionPane.showMessageDialog(mfrm, "핸드폰 번호에 숫자만 입력 가능합니다");
        }
        else if (DataBase.getMemberInfo(conn, inputPhone) != null) {
          JOptionPane.showMessageDialog(mfrm, "해당 번호가 이미 등록되어 있습니다");
        }
        else {
          DataBase.addNewMember(conn, inputName, inputPhone);
          JOptionPane.showMessageDialog(mfrm, "회원이 된 것을 축하드립니다. 1000포인트 지급해드렸습니다.");
          mfrm.dispose();
        }
      }
    });

    // 취소 클릭 이벤트
    cancelButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        mfrm.dispose();
      }
    });

    mfrm.setVisible(true);
  } // 회원 등록 프레임 끝
  // 두 번째 화면
  public static void createMainFrame() throws SQLException {
    JFrame frm = new JFrame("KOSTA CAFE");
    frm.setSize(800, 1050);
    frm.setLocationRelativeTo(null);
    frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frm.setLayout(new BorderLayout());

    List<Product> products = DataBase.getPersonInfos(DataBase.makeConnection());

    // 배열 크기를 product 리스트 크기로 지정
    String[] menuItems = new String[products.size()];
    int[] prices = new int[products.size()];
    String[] info = new String[products.size()];
    String[] serving = new String[products.size()];
    String[] allergy = new String[products.size()];
    String[] calorie = new String[products.size()];


    // 리스트 데이터를 배열에 저장
    for (int i = 0; i < products.size(); i++) {
      menuItems[i] = products.get(i).getName();
      prices[i] = (int) products.get(i).getPrice();
      info[i] = products.get(i).getInfo();
      serving[i] = products.get(i).getServing();
      allergy[i] = products.get(i).getAllergy();
      calorie[i] = products.get(i).getCalorie();
    }

    int[] counts = new int[menuItems.length];
    // 이미지 패널 생성
    ImagePanel imagePanel1 = new ImagePanel("/miniproject/cafe1.png");
    ImagePanel imagePanel2 = new ImagePanel("/miniproject/cafe2.png");
    ImagePanel imagePanel3 = new ImagePanel("/miniproject/cafe3.png");
    ImagePanel imagePanel4 = new ImagePanel("/miniproject/cafe4.png");
    ImagePanel imagePanel5 = new ImagePanel("/miniproject/cafe5.png");
    ImagePanel imagePanel6 = new ImagePanel("/miniproject/cafe6.png");

    // 이미지 패널 배치
    imagePanel1.setBounds(50, 40, 200, 200);
    imagePanel1.setToolTipText("<html>" + menuItems[0] + "<br>" + prices[0] + "원" + "<br><br> 1회 제공량 : " + serving[0] + "<br><br>" + info[0] + "<br><br>" + allergy[0]);
    imagePanel2.setBounds(300, 40, 200, 200);
    imagePanel2.setToolTipText("<html>" + menuItems[1] + "<br>" + prices[1] + "원" + "<br><br> 1회 제공량 : " + serving[1] + "<br><br>" + info[1] + "<br><br>" + allergy[1]);
    imagePanel3.setBounds(550, 40, 200, 200);
    imagePanel3.setToolTipText("<html>" + menuItems[2] + "<br>" + prices[2] + "원" + "<br><br> 1회 제공량 : " + serving[2] + "<br><br>" + info[2] + "<br><br>" + allergy[2]);
    imagePanel4.setBounds(50, 340, 200, 200);
    imagePanel4.setToolTipText("<html>" + menuItems[3] + "<br>" + prices[3] + "원" + "<br><br> 1회 제공량 : " + serving[3] + "<br><br>" + info[3] + "<br><br>" + allergy[3]);
    imagePanel5.setBounds(300, 340, 200, 200);
    imagePanel5.setToolTipText("<html>" + menuItems[4] + "<br>" + prices[4] + "원" + "<br><br> 1회 제공량 : " + serving[4] + "<br><br>" + info[4] + "<br><br>" + allergy[4]);
    imagePanel6.setBounds(550, 340, 200, 200);
    imagePanel6.setToolTipText("<html>" + menuItems[5] + "<br>" + prices[5] + "원" + "<br><br> 1회 제공량 : " + serving[5] + "<br><br>" + info[5] + "<br><br>" + allergy[5]);

    // 버튼 생성 및 배치
    // 뭔가 따로따로 버튼을 생성하는 것 보다 데이터를 배열로 만들어서
    // 카운트 편하게, 데이터 삽입 편하게 하려고 수정했는데 맞나? 맞것지 뭐
//    JButton btn1 = new JButton("Ice Americano");
//    JButton btn2 = new JButton("Ice CafeLatte");
//    JButton btn3 = new JButton("Ice Tea");
//    JButton btn4 = new JButton("Americano");
//    JButton btn5 = new JButton("CafeLatte");
//    JButton btn6 = new JButton("Tea");
    // 결제하기 취소하기 버튼
    JButton btn7 = new JButton("결제하기");
    JButton btn8 = new JButton("취소하기");
    JButton btn9 = new JButton("포인트사용");

    // 하단 내역보기 (JTextArea)
    JTextArea orderDetails = new JTextArea(20, 30);
    orderDetails.setEditable(false);
    JScrollPane scroll = new JScrollPane(orderDetails);
    scroll.setBounds(50, 700, 450, 250);

    // total 라벨
    JLabel totalLabel = new JLabel("TOTAL : 0원");
    totalLabel.setBounds(50,650,600,50);
    totalLabel.setFont(new Font("Dialog", Font.BOLD, 15));

    // 오더리스트 업데이트 메서드
    ActionListener orderListener = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JButton clickedButton = (JButton) e.getSource();
        String buttonText = clickedButton.getText();
        for (int i = 0; i < menuItems.length; i++) {
          if (buttonText.equals(menuItems[i])) {
            counts[i]++;
            updateOrder();
            break;
          }
        }
      }
      // 오더리스트 업데이트
      private void updateOrder(){
        StringBuilder sb = new StringBuilder();
//        String inputName = nameTextArea.getText();
//        String inputPhone = phoneTextArea.getText();
        total = 0;
        totalCount = 0;
        for (int i = 0; i < menuItems.length; i++) {
          if(counts[i] > 0){
            sb.append(menuItems[i]).append(" X ").append(counts[i]).append(" = ")
                .append(counts[i] * prices[i]).append("원\n");
            total += counts[i]*prices[i];
            totalCount += counts[i];
            pidList.add(i+1);  // 각 pid를 리스트에 저장
            qtyList.add(counts[i]);  // 각 qty를 리스트에 저장
          }
        }
        System.out.println(pid);
        System.out.println(qty);
        orderDetails.setText(sb.toString());
        totalLabel.setText("TOTAL : " + total + "원");

      }
    };
    // 버튼 생성 및 액션 리스너 추가
    JButton[] buttons = new JButton[menuItems.length];
    for (int i = 0; i < menuItems.length; i++) {
      buttons[i] = new JButton(menuItems[i]);
      buttons[i].setToolTipText("<html>" + menuItems[i] + "<br>" + prices[i] + "원" + "<br><br> 1회 제공량 : " + serving[i] + "<br><br>" + info[i] + "<br><br>" + allergy[i]);

      buttons[i].setBounds(50 + (i % 3) * 250, 250 + (i / 3) * 300, 200, 50);
      buttons[i].setFont(new Font("Dialog", Font.BOLD, 15));
      buttons[i].addActionListener(orderListener);
      frm.getContentPane().add(buttons[i]);
    }


    btn7.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        //
        int inputTotalQty = totalCount;
        int inputPrice = total;
        Date inputDate = Date.valueOf(LocalDate.now());

//        int inputMemberId = ;
      // JOptionPane.showConfirmDialog 호출
        int result = JOptionPane.showConfirmDialog(null, totalLabel.getText() + "\n결제 부탁드립니다.");
        try {
          conn = DataBase.makeConnection();

          System.out.println("연결!!!!!");
        } catch (SQLException ex) {
          throw new RuntimeException(ex);
        }

        // 사용자가 확인 버튼을 클릭한 경우에만 clearOrder 호출
        if (result == JOptionPane.OK_OPTION) {
          int newId = DataBase.addNewOrder(conn, inputTotalQty, inputPrice, inputDate);
          if (newId > -1) {
            for (int i = 0; i < pidList.size(); i++) {
              DataBase.addNewOrderDetail(conn, newId, pidList.get(i), qtyList.get(i));
            }
          } else {
            System.out.println("제대로 orderHeader ID가 생성되지 않았습니다.");
          }
          clearOrder();
        }
      }
      private void clearOrder() {
        for (int i = 0; i < menuItems.length; i++) {
          counts[i] = 0;
        }
        orderDetails.setText("");
        totalLabel.setText("TOTAL : 0 원");
      }
    });

    // 취소하기 버튼 이벤트
    btn8.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        clearOrder();
      }
      private void clearOrder() {
        for (int i = 0; i < counts.length; i++) {
          counts[i] = 0;
        }
        orderDetails.setText("");
        totalLabel.setText("TOTAL : 0 원");
      }
    });

    btn7.setBounds(550, 700, 200, 50);
    btn7.setFont(new Font("Dialog", Font.BOLD, 15));
    btn8.setBounds(550, 900, 200, 50);
    btn8.setFont(new Font("Dialog", Font.BOLD, 15));
    btn8.setToolTipText("버튼 클릭시 주문내역이 초기화됩니다.");
    btn9.setBounds(550, 800, 200, 50);
    btn9.setFont(new Font("Dialog", Font.BOLD, 15));
    btn9.setToolTipText("핸드폰번호 입력시 포인트 사용 가능합니다.");


    // 포인트 사용
    btn9.addActionListener(new ActionListener() {
      boolean memberInfoExists = false;
      int availablePointAmt = 0;
      @Override
      public void actionPerformed(ActionEvent e) {

        JFrame pointFrame = new JFrame("포인트 사용");

        // 포인트창 사이즈 설정
        pointFrame.setSize(600, 400);
        pointFrame.setLocationRelativeTo(null);
        pointFrame.setLayout(null);
        pointFrame.setVisible(true);

        // 패널 생성, 수동 배치 위치, 크기 조정 완료
        JPanel pointPanel = new JPanel();

        pointPanel.setLayout(null);
        pointPanel.setBounds(0, 0, 600, 400);

        // 라벨 생성, 폰트, 위치, 사이즈 설정
        JLabel phoneLabel = new JLabel("핸드폰 번호");
        JLabel pointLabel = new JLabel();
        phoneLabel.setFont(new Font("Dialog", Font.BOLD, 15));
        pointLabel.setFont(new Font("Dialog", Font.BOLD, 15));
        phoneLabel.setBounds(100, 60, 400, 30);
        pointLabel.setBounds(100, 140, 400, 30);

        // 텍스트 에어리어 생성, 폰트, 위치, 사이즈 설정
        JTextArea phoneText = new JTextArea();
        JTextArea pointText = new JTextArea();
        phoneText.setEditable(true);
        pointText.setEditable(false);
        phoneText.setBounds(100,100,350,30);
        pointText.setBounds(100, 170, 350,30);
        phoneText.setFont(new Font("Dialog", Font.BOLD, 20));
        pointText.setFont(new Font("Dialog", Font.BOLD, 20));

        // 버튼 추가 , 폰트, 사이즈, 배치 설정
        JButton checkButton = new JButton("조 회");
        JButton okButton = new JButton("사 용");
        JButton cancelButton = new JButton("취 소");
        checkButton.setFont(new Font("Dialog", Font.BOLD, 15));
        okButton.setFont(new Font("Dialog", Font.BOLD, 15));
        cancelButton.setFont(new Font("Dialog", Font.BOLD, 15));
        checkButton.setBounds(100, 220, 110, 30);
        okButton.setBounds(220, 220, 110, 30);
        cancelButton.setBounds(340, 220, 110, 30);

        // 조회 버튼 이벤트
        checkButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            // DB member_Phone 비교 후 중복 되지 않으면 member_Point 조회 후
            // pointLabel에 출력
            try {
              conn = DataBase.makeConnection();
              ArrayList<String> memberInfo = DataBase.getMemberInfo(conn, phoneText.getText());

              if (memberInfo == null) {
                memberInfoExists = false;
                pointLabel.setText("해당 번호로 등록된 회원 정보가 없습니다");
              } else {
                memberInfoExists = true;

                // 포인트 값이 null인지 확인
                String pointString = memberInfo.get(1);
                if (pointString != null && !pointString.isEmpty()) {
                  availablePointAmt = Integer.parseInt(pointString);
                  pointLabel.setText(memberInfo.get(0) + " 회원님의 사용가능한 포인트: " + availablePointAmt);
                } else {
                  pointLabel.setText("회원님의 포인트 정보가 없습니다");
                }
              }
            } catch (SQLException ex) {
              throw new RuntimeException(ex);
            } catch (NumberFormatException ex) {
              pointLabel.setText("포인트 값을 숫자로 변환할 수 없습니다.");
            }
            pointText.setEditable(true);
          }
        });

        // 사용 버튼 이벤트

        okButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            // pointLabel에 출력된 금액을 mainFrame의 Total에서
            String text = pointText.getText();


            int inputPointAmt = (text == null || text.equals("")) ? 0 : Integer.parseInt(text);

            if (!memberInfoExists) {
              pointLabel.setText("회원 정보를 조회해주세요");
            }
            else if (inputPointAmt == 0) {
              pointLabel.setText("아래칸에 사용하실 포인트 값을 입력해주세요");
            }
            else if (inputPointAmt % 1000 != 0) {
              pointLabel.setText("1000 단위로 입력해주세요");
            }
            else if (inputPointAmt > availablePointAmt) {
              pointLabel.setText("사용 가능한 포인트가 부족합니다");
            }
            else {
              totalLabel.setText(totalLabel.getText() + " - " + inputPointAmt + "원(포인트) = " + (total - inputPointAmt) + "원");
              pointFrame.dispose();
            }
          }
        });

        // 취소 버튼 이벤트
        cancelButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            memberInfoExists = false;
            availablePointAmt = 0;
            pointFrame.dispose();
          }
        });

        // 패널에 JTextAtrea 추가
        pointPanel.add(phoneText);
        pointPanel.add(pointText);
        // 패널에 버튼추가
        pointPanel.add(checkButton);
        pointPanel.add(okButton);
        pointPanel.add(cancelButton);
        // 패널에 라벨 추가
        pointPanel.add(phoneLabel);
        pointPanel.add(pointLabel);
        pointFrame.add(pointPanel);
      }
    });

    frm.getContentPane().add(btn7);
    frm.getContentPane().add(btn8);
    frm.getContentPane().add(btn9);


    // 텍스트 패널에 스크롤 패널 추가
    JPanel textPanel = new JPanel();

    textPanel.setLayout(null);
    textPanel.add(scroll);
    textPanel.add(totalLabel);


    // 이미지 및 텍스트 패널 추가
    frm.add(imagePanel1);
    frm.add(imagePanel2);
    frm.add(imagePanel3);
    frm.add(imagePanel4);
    frm.add(imagePanel5);
    frm.add(imagePanel6);
    frm.add(textPanel);

    frm.setVisible(true);
  }
//  // 결제 페이지 만들기
//  public static void createOutroFrame() {
//    JFrame outroFrame = new JFrame("KOSTA CAFE");
//
//    // 프레임 크기 설정
//    outroFrame.setSize(800, 1050);
//    outroFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    outroFrame.setLocationRelativeTo(null);
//
//    // back 이미지 생성 (이미지 패널)
//    JPanel outroPanel = new JPanel() {
//      private Image bgImage = new ImageIcon(getClass().getResource("/miniproject/outback.png")).getImage();
//
//      @Override
//      public void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
//      }
//    };
//    outroPanel.setLayout(new BorderLayout());
//
//    // 센터 라벨
//    JLabel clickLabel = new JLabel("카드를 넣어주세요", SwingConstants.CENTER);
//    clickLabel.setFont(new Font("Dialog", Font.BOLD, 50));
//    clickLabel.setForeground(Color.BLACK);
//
//    outroPanel.add(clickLabel, BorderLayout.CENTER);
//
//    outroPanel.add(outroPanel);
//    outroPanel.setVisible(true);
//  }

  // 메인 메서드
  public static void main(String[] args) {
    // 인트로 프레임 생성
    createIntroFrame();

//    createMemberFrame();
//    createOutroFrame();

  }

  // 이미지 패널 클래스
  static class ImagePanel extends JPanel {
    private Image image;

    public ImagePanel(String imagePath) {
      image = new ImageIcon(getClass().getResource(imagePath)).getImage();
    }

    @Override
    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      g.drawImage(image, 0, 0, 200, 200, this);
    }
  }
}