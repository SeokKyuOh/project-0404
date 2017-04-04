/*
	JTable�� ������ �г�
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
	//Vector�� ArrayList�� �Ѵ� ����
	//������ ����ȭ ��������
	Vector list = new Vector();
	//Vector<String> columnName = new Vector<String>();
	
	//vector�� arraylist ��� Vector�� ����ȭ ����. �� �����尡 ���ÿ� ��������
	//������������ ����� �� ����.
	int cols;
	
	public TablePanel() {
		table = new JTable();
		scroll = new JScrollPane(table);
		this.setLayout(new BorderLayout());
		this.add(scroll);
		
		this.setBackground(Color.PINK);
		setPreferredSize(new Dimension(630, 550));
		
	}
	
	public void setConnection(Connection con){		//�����ڷ� �ֱ⿣ Ÿ�̹��� ���������� �޼���� ���� �θ���
		this.con=con;
		init();
		
		//���̺� ���� Jtable�� ����
		//���� �� �� ���;� �ϱ� ������ �̰����� �ű�
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
		
		//���̺� ������
		table.setModel(model);
	}
	
	//book ���̺��� ���ڵ� ��������
	public void init(){
		String sql = "select * from book order by book_id asc";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			cols = rs.getMetaData().getColumnCount();		//�÷��� �޾ƿ���
			
			list.removeAll(list);
				
			
			//rs�� ������ �÷������� �Űܴ���
			while(rs.next()){
				Vector<String> data = new Vector<String>();
				
				data.add(Integer.toString(rs.getInt("book_id")));
				data.add(rs.getString("book_name"));
				data.add(Integer.toString(rs.getInt("price")));
				data.add(rs.getString("img"));
				data.add(Integer.toString(rs.getInt("subcategory_id")));
				
				list.add(data);		//���� ���Ϳ� ���͸� �߰�
				
				//System.out.println(list);
				
				/* JTable�� ���� ����̶� dto�� �޾Ƶ鿩���� �ʴ´�. �׷��� vector�� �迭ȭ �ؼ� ����
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













