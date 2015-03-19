package Data;

public class BreadIngredient extends Ingredients{
	
	private enum Bread{
		Bread, Baguette, Wholemeal, Ciabatta;
	};
	
	private Bread breadType;
	
	public static Bread[] getBreadTypes(){
		return Bread.class.getEnumConstants();
	}
	
	public  static int getBreadTypesLength(){
		return Bread.class.getEnumConstants().length;
	}
	
	public boolean equals(Object s){
		if(s instanceof Bread){
			if((Bread)s==breadType)
				return true;
			return false;
		}else return false;
	}
	
	public Bread getBread(){
		return breadType;
	}
	
	public String getCategory(){
		return "Bread";
	}
}
