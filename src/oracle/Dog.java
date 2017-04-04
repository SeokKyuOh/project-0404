/*
 	강아지 클래스의 인스턴스를 오직 1개만 만들기

	SingTon pattern - 개발 패턴 중 하나임
							 객체의 인스턴스를 오직 1개만 만드는 패턴

	JavaSE
	JavaEE	고급기술(javaSE를 통하여
*/
package oracle;

public class Dog {
	static private Dog instance;
	
	//new에 의한 중복 생성을 막자
	private Dog(){
	}
	
	static public Dog getInstance() {	//이 메서드를 통해서만 생성하자
		if(instance == null){
			instance = new Dog();
			
		}
		return instance;
	}
	
	
}
