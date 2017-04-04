//emp ���̺��� �����͸� ó���ϴ� ��Ʈ�ѷ�
package oracle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.table.AbstractTableModel;

public class EmpModel extends AbstractTableModel{
	Connection con;
	
	PreparedStatement pstmt;
	ResultSet rs;
	String[] column; 		//�÷��� ���� �迭
	String[][] data;			//ũ�⸦ �� �� ������ new���� ���� ����		//���ڵ带 ���� �迭
	
	public EmpModel(Connection con) {//�����ڰ� ���� ������ ������ DB������ �����س���		
		this.con=con;
		/*
		 1.����̹��ε�
		 2.����
		 3.������ ����
		 4.���� �ݱ�
		 */
		try {				
			if(con!=null){
				String sql = "select * from emp";
				
				//�Ʒ��� pstmt�� ���� �����Ǵ� rs�� Ŀ���� �����ο� �� �ִ�.
				pstmt=con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				
				//��� ���� ��ȯ
				rs = pstmt.executeQuery();
				
				//�÷��� ���غ���
				ResultSetMetaData meta = rs.getMetaData();		//MetaData�� �� ��ü�� ���� ������ ���Ѵ�. // �������� ������
				int count = meta.getColumnCount();//�÷��� ����
				
				column = new String[count];
				//�÷����� ä����
				for(int i=0;i<column.length;i++){
					column[i]=meta.getColumnName(i+1);		//��� 1���� �����ϱ� ������ i��� ���� �ȵȴ�.
					
				}
				
				rs.last();		//���� ���������� ����
				int total = rs.getRow();	//���ڵ� ��ȣ
				rs.beforeFirst();
				
				//�� ���ڵ� ���� �˾����� ������ �迭 �����غ���
				data = new String[total][column.length];
				
				//���ڵ带 ������ �迭�� data�� ä���ֱ�
				for(int a=0;a<data.length;a++){		//����
					rs.next();
					for(int i=0;i<data[a].length;i++){	//ȣ��
						data[a][i] = rs.getString(column[i]);		//���⼭���� ����Ÿ���̽��� �ڷ����� ��ġ���� �ʾƵ� �ȴ�. String���� ������ �ȴ�.	
					}
				}
				
			} else{
				System.out.println("���� ����");
			}
		} catch (SQLException e) {
			System.out.println("���ӽ���");
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
			
			/*if(con!=null){		//con�� ���� ������ â�� ���� �� ���� �� �ְ� �Ѵ�. ���⼭ ���� ����
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}*/
		}
		
	}
	
	public int getColumnCount() {
		return column.length;
	}
	
	public String getColumnName(int index) {//�÷� ���� ��������
		return column[index];
	}

	public int getRowCount() {
		return data.length;
	}

	public Object getValueAt(int row, int col) {
		return data[row][col];
	}

	
	
	
	
}
