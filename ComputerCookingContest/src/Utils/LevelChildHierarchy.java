package Utils;


public class LevelChildHierarchy {
	private boolean last;
	private int index;
	
	public LevelChildHierarchy(boolean b, int i){
		last = b;
		index = i;
	}

	public boolean isLast() {
		return last;
	}

	public void setLast(boolean last) {
		this.last = last;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}	
}
