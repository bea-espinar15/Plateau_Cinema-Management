package utilities;

public class Pair<K, V> {
	private K obj1;
	private V obj2;
	
	public Pair(){
	}
	
	public Pair(K obj1, V obj2){
		this.obj1 = obj1;
		this.obj2 = obj2;
	}
	
	public K getFirst(){
		return this.obj1;
	}
	
	public V getSecond(){
		return this.obj2;
	}

	public void setObj1(K obj1) {
		this.obj1 = obj1;
	}

	public void setObj2(V obj2) {
		this.obj2 = obj2;
	}
	
}
