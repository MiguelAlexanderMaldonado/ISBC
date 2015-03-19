package Data;

import java.util.ArrayList;

public class DairyIngredient extends ProcessedFoodIngredient{

	private enum Dairy{
		Cheese, Margarine;
	};
	
	private Dairy dairyType;
	
	public static  Dairy[] getDairyTypes()
	{
		return Dairy.class.getEnumConstants();
	}
	
	public static  int getDairyTypesLength()
	{
		return Dairy.class.getEnumConstants().length;
	}
	
	public boolean equals(Object s){		
		if(s instanceof Dairy){
			if(s==dairyType)
				return true;
			return false;			
		}else return false;
	}
	
	public Dairy getDairy(){
		return dairyType;
	}
	
	public String getCategory(){
		return "Dairy";
	}
	
	public static ArrayList<String> getLastChildsInHierarchy(){
		ArrayList<String> ingredients = new ArrayList<String>();
		
		for(int i=0; i<getDairyTypesLength(); i++ ){
			ingredients.add(getDairyTypes()[i].toString());
		}
		
		return ingredients;
	}
}
