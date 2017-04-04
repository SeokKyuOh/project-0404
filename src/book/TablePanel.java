/*
	JTable이 얹혀질 패널
*/
package book;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class TablePanel extends JPanel{
	Connection con;
	JTable table;
	JScrollPane scroll;
	TableModel model;
	
	//ArrayList<Book> list = new ArrayList<Book>();
	//Vector와 ArrayList는 둘다 같다
	//차이점 동기화 지원여부
	Vector list = new Vector();
	//Vector<String> columnName = new Vector<String>();
	
	//vector는 arraylist 대신 Vector만 동기화 지원. 두 쓰레드가 동시에 수정가능
	//안정적이지만 고속일 수 없다.
	int cols;
	
	public TablePanel() {
		table = new JTable();
		scroll = new JScrollPane(table);
		this.setLayout(new BorderLayout());
		this.add(scroll);
		
		this.setBackground(Color.PINK);
		setPreferredSize(new Dimension(630, 550));
		
	}
	
	public void setConnection(Connection con){		//생성자로 넣기엔 타이밍이 맞지않으니 메서드로 만들어서 부르자
		this.con=con;
		init();
		
		//테이블 모델을 Jtable에 적용
		//연결 후 에 나와야 하기 때문에 이곳으로 옮김
		model = new AbstractTableModel() {
			public int getRowCount() {
				return list.size();
			}
			public int getColumnCount() {
				return cols;
			}
			public Object getValueAt(int row, int col){
				Vector vec = (Vector)list.get(row);
				return vec.elementAt(col);
			}
		};
		
		//테이블에 모델적용
		table.setModel(model);
	}
	
	//book 테이블의 레코드 가져오기
	public void init(){
		String sql = "select * from book order by book_id asc";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			cols = rs.getMetaData().getColumnCount();		//컬럼값 받아오기
			
			list.removeAll(list);
				
			
			//rs의 정보를 컬렉션으로 옮겨담자
			while(rs.next()){
				Vector<String> data = new Vector<String>();
				
				data.add(Integer.toString(rs.getInt("book_id")));
				data.add(rs.getString("book_name"));
				data.add(Integer.toString(rs.getInt("price")));
				data.add(rs.getString("img"));
				data.add(Integer.toString(rs.getInt("subcategory_id")));
				
				list.add(data);		//기존 벡터에 벡터를 추가
				
				//System.out.println(list);
				
				/* JTable이 예전 기술이라 dto로 받아들여지지 않는다. 그래서 vector로 배열화 해서 받자
				Book dto = new Book();
				dto.setBook_id(rs.getInt("book_id"));
				dto.setBook_name(rs.getString("book_name"));
				dto.setPrice(rs.getInt("price"));
				dto.setImg(rs.getString("img"));
				dto.setSubcategory_id(rs.getInt("subcategory_id"));
				
				list.add(dto);
				*/
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
	
	
}













