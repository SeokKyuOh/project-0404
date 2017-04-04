package book;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

public class BookMain extends JFrame implements ItemListener, ActionListener{
	DBManager manager = DBManager.getInstance();
	Connection con;
	
	JPanel p_west;		//좌측 등록폼
	JPanel p_content;	//우측 영역 전체
	JPanel p_north;		//우측 선택 모드 영역
	JPanel p_center;	//Flow가 적용되어 p_table, p_grid를
	JPanel p_table;		//JTable이 붙여질 패널
	JPanel p_grid;		//그리드 방식으로 보여질 패널
	Choice ch_top;
	Choice ch_sub;
	JTextField t_name;
	JTextField t_price;
	Canvas can;
	JButton bt_regist;
	CheckboxGroup group;
	Checkbox ch_table, ch_grid;
	Toolkit kit = Toolkit.getDefaultToolkit();		//툴킷은 추상클래스여서 new 안된다. 대신 static으로 올릴 수 있다.
	Image img;
	JFileChooser chooser;
	File file;
	
	
	//html option과는 다르므로, Choice 컴포넌트의 값을 미리 받아놓자
	//이 컬렉션은 rs를 대체할 것이다.
	//그럼으로써 얻는 장점
	//더이상 ra.last, rs.getRow로 고생하지 말자
	ArrayList<SubCategory> subcategory = new ArrayList<SubCategory>();
	
	public BookMain() {
		//setLayout(new FlowLayout());
		p_west = new JPanel();	
		p_content = new JPanel();
		p_north = new JPanel();
		p_center = new JPanel();
		
		p_table = new TablePanel();
		p_grid = new GridPanel();
		
		ch_top = new Choice();
		ch_sub = new Choice();
		t_name = new JTextField(12);
		t_price = new JTextField(12);
		URL url = this.getClass().getResource("/default.png");		//원하는 이미지가 있는 폴더에서 bulid path로 use at source로 만들어두고 사용
		
		try {
			img = ImageIO.read(url);
			//System.out.println(img);
		} catch (IOException e) {	
			e.printStackTrace();
		}
		
		can = new Canvas(){
			public void paint(Graphics g) {
				g.drawImage(img, 0, 0, 140, 140, this);
			}
		};
		can.setPreferredSize(new Dimension(150, 150));
		
		bt_regist = new JButton("등록");
		group = new CheckboxGroup();
		ch_table = new Checkbox("테이블", true, group);
		ch_grid = new Checkbox("그리드", false, group);
		
		//파일 추저 올리기
		chooser = new JFileChooser("C:/html_workspace/images");
		
		
		//ch_top.add("상위");
		ch_top.setPreferredSize(new Dimension(130, 45));
		//ch_sub.add("하위");
		ch_sub.setPreferredSize(new Dimension(130, 45));
		
		p_west.setPreferredSize(new Dimension(150, 600));
		p_west.setBackground(Color.WHITE);
		p_west.add(ch_top);
		p_west.add(ch_sub);
		p_west.add(t_name);
		p_west.add(t_price);
		p_west.add(can);
		p_west.add(bt_regist);

		p_content.setLayout(new BorderLayout());
		
		//컨텐츠 북쪽
		p_north.add(ch_table);
		p_north.add(ch_grid);
		
		p_center.setBackground(Color.YELLOW);
		p_center.add(p_table);
		p_center.add(p_grid);
		
		p_content.add(p_north, BorderLayout.NORTH);
		p_content.add(p_center);
		
		add(p_west, BorderLayout.WEST);
		add(p_content);
		
		init();
		
		ch_top.addItemListener(this);
		can.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				openFile();		
			}
		});
		
		bt_regist.addActionListener(this);
		
		//초이스 컴포넌트와 리스너 연결
		ch_table.addItemListener(this);
		ch_grid.addItemListener(this);
		
		setSize(800, 600);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}
	
	public void init(){
		//초이스 컴포넌트에 최상위 목록 보이기
		con = manager.getConnection();
		String sql = "select * from topcategory order by topcategory_id asc";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try{
			pstmt = con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			
			while(rs.next()){
				ch_top.add(rs.getString("category_name"));
			}
		} catch (SQLException e){
			e.printStackTrace();
		} finally{
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		//테이블 패널과 그리드 패널에게 Connection 전달
		((TablePanel)p_table).setConnection(con);		//부모이기 때문에 자식의 메서드를 받기 위해 자식형으로 변신
		((GridPanel)p_grid).setConnection(con);
		
	}
	
	//하위 카테고리 가져오기
	public void getSub(String v){
		//기존에 이미 채워진 아이템이 있다면, 먼저 싹~~지운다.
		ch_sub.removeAll(); //기존 카테고리들 지우기
		
		StringBuffer sb = new StringBuffer();
		sb.append("select * from subcategory ");
		sb.append(" where topcategory_id=(");	//where 전에 한칸 띄자.. 앞글과 붙어버리니까
		sb.append(" select topcategory_id from");
		sb.append(" topcategory where category_name='"+v+"') order by subcategory_id asc");
		
		//System.out.println(sb.toString());
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt=con.prepareStatement(sb.toString());		//sb가 stringbuffer 형이어서 형변환
			rs=pstmt.executeQuery();
			
			//서브 카테고리의 정보를  담기 + 출력
			//rs에 담겨진 레코드 1개는 SubCategory클래스의 인스턴스 1개로 받자
			while(rs.next()){
				SubCategory dto = new SubCategory();
				dto.setSubcategory_id(rs.getInt("subcategory_id"));
				dto.setCategory_name(rs.getString("category_name"));
				dto.setTopcategory_id(rs.getInt("topcategory_id"));
				
				subcategory.add(dto);		//컬렉션에 담기
				ch_sub.add(dto.getCategory_name());
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	//상품 등록 메서드
	public void regist(){
		//내가 지금 선택한 서브 카테고리 초이스의 index를 구해서, 
		//그 index로 ArrayList를 접근하여 객체를 반환받으면 정보를 유용하게 쓸 수 있다.
		int index = ch_sub.getSelectedIndex();
		SubCategory dto = subcategory.get(index);
		
		String book_name = t_name.getText();//책이름 
		int price = Integer.parseInt(t_price.getText());		//String으로 해도 되지만 자료형을 정확히 명시해서 int로 함. 나중에 계산할 경우 등을 위해서..
		String img = file.getName();		//파일명 아래에서 받아온 파일을 메인변수로 선언하여 받아온다.
		
		StringBuffer sb = new StringBuffer();
		sb.append("insert into book(book_id, subcategory_id, book_name, price, img)");
		sb.append(" values(seq_book.nextval,"+dto.getSubcategory_id()+", '"+book_name+"', "+price+", '"+img+"')");
		
		//System.out.println(sb.toString());
		
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(sb.toString());
			
			//SQL문이 DML(insert, delete, update)일때 업에이트 메서드 사용
			int result = pstmt.executeUpdate();
			
			//위의 메서드는 숫자값을 반환하며, 이 숫자값은 이 쿼리에 의해 영향을 받는 레코드 수를 반환한다.
			//insert의 경우 언제나 1이 반환된다.
			if(result !=0){
				//System.out.println(book_name+"등록성공");
				copy();
				((TablePanel)p_table).init();						//조회 일으킴
				((TablePanel)p_table).table.updateUI();		//갱신. 컴포넌트를 새로고치는 것.
				((GridPanel)p_grid).loadData();						//조회 일으킴
				((GridPanel)p_grid).updateUI();
				
			} else{
				System.out.println(book_name+"등록실패");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void itemStateChanged(ItemEvent e) {
		Object obj = e.getSource();
		if(obj == ch_top){
			Choice ch = (Choice)e.getSource();		//checkbox도 아이템리스너로 했기 때문에 이걸 if문 조건 걸지 않고 그대로 쓰면 위험.
			getSub(ch.getSelectedItem());	//한글이 넘어오기 위해서 index가 아닌 item으로 한다.
		} else if(obj == ch_table){
			//System.out.println("테이블 볼래?");
			p_table.setVisible(true);
			p_grid.setVisible(false);
		} else if(obj == ch_grid){
			//System.out.println("그리드 볼래?");
			p_table.setVisible(false);
			p_grid.setVisible(true);
		}
	}
	
	//그림파일 불러오기
	public void openFile(){
		int result = chooser.showOpenDialog(this);		//사용자가 취소눌렀는지 확인 눌렀는지 알기위해 값을 받는다
		
		if(result == JFileChooser.APPROVE_OPTION){//선택했을시
			//선택한 이미지를 canvas에 그릴 것이다.
			file = chooser.getSelectedFile();		//반환형이 file이다.
			img = kit.getImage(file.getAbsolutePath());
			can.repaint();
		}
	}
	
	/*
		이미지 복사하기
		유저가 선택한 이미지를 개발자가 지정한 위치로 복사를 해놓자
	*/
	public void copy(){
		FileInputStream fis = null;
		FileOutputStream fos = null;
		
		try {
			fis = new FileInputStream(file);
			
			String filename = file.getName();		//원본파일 이름 가져오기
			String dest = "C:/java_workspace2/DBProject2/data/"+filename;		//저장경로+원본파일 이름
			fos = new FileOutputStream(dest);	//뱉기
			
			int data;	//읽어들인 데이터가 들어있지 않다. 갯수만 존재한다. //데이타의 유무 확인용
			byte[] b = new byte[1024];		//실제 데이타는 여기에 모여있다.
			while(true){
				data = fis.read(b);
				
				if(data == -1) break;
				
				fos.write(b);		//fos.write(data)가 아닌 이유. 현재 data는 데이타는 없고 갯수만 존재. 데이타는 b에 존재하고 있다.
			}
			JOptionPane.showMessageDialog(this, "등록완료");
		} catch (FileNotFoundException e) {	
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(fos !=null){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(fis !=null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	
	public void actionPerformed(ActionEvent e) {
		e.getSource();	//object로 반환받는 이유. 버튼일수도 사진일수도 있기 때문
		//System.out.println("누름?");
		regist();
	}
	
	public static void main(String[] args) {
		new BookMain();
	 

	}

}
