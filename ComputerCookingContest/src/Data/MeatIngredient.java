package Data;

import java.util.ArrayList;

public class MeatIngredient extends AnimalIngredient{
	
	private enum Meat{
		Bird, Fish, Lamb, Pig, Veal;
	};
	
	private Meat meatType;
	
	public static  Meat[] getMeatTypes()
	{
		return Meat.class.getEnumConstants();
	}
	
	public static  int getMeatTypesLength()
	{
		return Meat.class.getEnumConstants().length;
	}
	
	public boolean equals(Object s){		
		if(s instanceof Meat){
			if(s==meatType)
				return true;
			return false;			
		}else return false;
	}
	
	public Meat getMeat(){
		return meatType;
	}
	
	public String getCategory(){
		return "Meat";
	}
	
	public static ArrayList<String> getLastChildsInHierarchy(){
		ArrayList<String> ingredients = new ArrayList<String>();
		
		for(int i =2; i<getMeatTypesLength(); i++ ){
			ingredients.add(getMeatTypes()[i].toString());
		}		
		ingredients.addAll(BirdIngredient.getLastChildsInHierarchy());
		ingredients.addAll(FishIngredient.getLastChildsInHierarchy());
		
		return ingredients;
	}
}
