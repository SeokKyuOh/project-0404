/*
	��ü���� ����� �ڹٿ����� ������ �繰�� Ŭ������ ����������
	Database������ ������ �繰�� Entity��� ��ü�������� ǥ���Ѵ�.
	�ᱹ ��ü�� ǥ���ϴ� ����� �ٸ� �� ������ ����.
	���� �ݿ��̶�� ������ ����.
	
	��ü���� ���� Ŭ������ �ν��Ͻ��� �����س��� ��Ǫ���̶��
	Database�о߿��� ���̺��� ���ڵ带 ������ �� �ִ� Ʋ�� ���� �����ϴ�.
	�̶� �ϳ��� ���ڵ�� �ᱹ �ϳ��� ��ü�� �����Ѵ�.
	���) ���̺� �����ϴ� ��ǰ ���ڵ� ���� �� 5�����, �����ڴ� �� ������ ���ڵ带 5���� �ν��Ͻ��� ���� ������ �ȴ�.
	
	�Ʒ��� Ŭ������ ���� �ۼ����� �ƴ� �Ѱ��� ���ڵ带 ������� ��������� �뵵�� ����� Ŭ�����̴�.
	���ø����̼� ����о߿��� �̷��� ������ Ŭ������ ������ VO, DTO�� �Ѵ�.
	VO : Value Object = ���� ��� ��ü
	DTO : Data Transfer Object = ���� �����ϱ� ���� ��ü
*/

//Ŭ������ �� ������ ��� ���� �ʾƵ� �ȴ�.

package book;
public class SubCategory {
	//������ ���̺� �ִ� �Ͱ� ���缭 ������
	//������ ���� Ŭ����. DummyŬ����. �׷��� �ڷ���� ��ȣ�ޱ� ���� private�� ��Ƶд�.
	//���� ������ ��� �׸���. �迭�� ��������?
	//�迭�� �ڷ����� ���� �� ����. ��ü�� �ƴϴ�.
	//�迭�� ��ü�� ������ �ٸ� ������� ���ߵǹǷ� ��ü�� ó���ϴ� ���� �ξ� �� �۾�����̳� ����� ����.
	
	private int subcategory_id;
	private int topcategory_id;
	private String category_name;
	
	//��Ŭ�� �� ctrl + shift + s ���� getter/setter �����ϸ� �Ʒ��� ���� �ָ��� ���´�.
	public int getSubcategory_id() {
		return subcategory_id;
	}
	public void setSubcategory_id(int subcategory_id) {
		this.subcategory_id = subcategory_id;
	}
	public int getTopcategory_id() {
		return topcategory_id;
	}
	public void setTopcategory_id(int topcategory_id) {
		this.topcategory_id = topcategory_id;
	}
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
	
	
}
