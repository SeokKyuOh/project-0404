/*
 	������ Ŭ������ �ν��Ͻ��� ���� 1���� �����

	SingTon pattern - ���� ���� �� �ϳ���
							 ��ü�� �ν��Ͻ��� ���� 1���� ����� ����

	JavaSE
	JavaEE	��ޱ��(javaSE�� ���Ͽ�
*/
package oracle;

public class Dog {
	static private Dog instance;
	
	//new�� ���� �ߺ� ������ ����
	private Dog(){
	}
	
	static public Dog getInstance() {	//�� �޼��带 ���ؼ��� ��������
		if(instance == null){
			instance = new Dog();
			
		}
		return instance;
	}
	
	
}
