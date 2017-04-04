//emp 테이블의 데이터를 처리하는 컨트롤러
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
	String[] column; 		//컬럼을 넣을 배열
	String[][] data;			//크기를 알 수 없으니 new하지 말고 선언만		//레코드를 넣을 배열
	
	public EmpModel(Connection con) {//생성자가 가장 빠르기 때문에 DB연동을 먼저해놓자		
		this.con=con;
		/*
		 1.드라이버로드
		 2.접속
		 3.쿼리문 수행
		 4.접속 닫기
		 */
		try {				
			if(con!=null){
				String sql = "select * from emp";
				
				//아래의 pstmt에 의해 생성되는 rs는 커서가 자유로울 수 있다.
				pstmt=con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				
				//결과 집합 반환
				rs = pstmt.executeQuery();
				
				//컬럼을 구해보자
				ResultSetMetaData meta = rs.getMetaData();		//MetaData는 그 자체에 대한 정보를 말한다. // 설정정보 얻어오기
				int count = meta.getColumnCount();//컬럼의 갯수
				
				column = new String[count];
				//컬럼명을 채우자
				for(int i=0;i<column.length;i++){
					column[i]=meta.getColumnName(i+1);		//얘는 1부터 시작하기 때문에 i라고 쓰면 안된다.
					
				}
				
				rs.last();		//제일 마지막으로 보냄
				int total = rs.getRow();	//레코드 번호
				rs.beforeFirst();
				
				//총 레코드 수를 알았으니 이차원 배열 생성해보자
				data = new String[total][column.length];
				
				//레코드를 이차원 배열인 data에 채워넣기
				for(int a=0;a<data.length;a++){		//층수
					rs.next();
					for(int i=0;i<data[a].length;i++){	//호수
						data[a][i] = rs.getString(column[i]);		//여기서만은 데이타베이스와 자료형이 일치하지 않아도 된다. String으로 받으면 된다.	
					}
				}
				
			} else{
				System.out.println("접속 실패");
			}
		} catch (SQLException e) {
			System.out.println("접속실패");
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
			
			/*if(con!=null){		//con은 이제 윈도우 창이 닫을 때 닫을 수 있게 한다. 여기서 닫지 말자
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
	
	public String getColumnName(int index) {//컬럼 제목 가져오기
		return column[index];
	}

	public int getRowCount() {
		return data.length;
	}

	public Object getValueAt(int row, int col) {
		return data[row][col];
	}

	
	
	
	
}
