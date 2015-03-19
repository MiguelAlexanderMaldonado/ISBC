package Data;

import java.util.ArrayList;

public class FishIngredient extends MeatIngredient{

	private enum Fish{
		Salmon, Tuna;
	};
	
	private Fish fishType;
	
	public static  Fish[] getFishTypes()
	{
		return Fish.class.getEnumConstants();
	}
	
	public static  int getFishTypesLength()
	{
		return Fish.class.getEnumConstants().length;
	}
	
	public boolean equals(Object s){		
		if(s instanceof Fish){
			if(s==fishType)
				return true;
			return false;			
		}else return false;
	}
	
	public Fish getFish(){
		return fishType;
	}
	
	public String getCategory(){
		return "Fish";
	}
	
	public static ArrayList<String> getLastChildsInHierarchy(){
		ArrayList<String> ingredients = new ArrayList<String>();
		
		for(int i=0; i<getFishTypesLength(); i++ ){
			ingredients.add(getFishTypes()[i].toString());
		}
		
		return ingredients;
	}
}
