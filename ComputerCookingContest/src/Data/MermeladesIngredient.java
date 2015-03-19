package Data;

import java.util.ArrayList;

public class MermeladesIngredient extends ProcessedFoodIngredient{

	private enum Mermelades{
		StrawberryMermelade;
	};
	
	private Mermelades mermeladeType;
	
	public static  Mermelades[] getMermeladesTypes()
	{
		return Mermelades.class.getEnumConstants();
	}
	
	public static  int getMermeladesTypesLength()
	{
		return Mermelades.class.getEnumConstants().length;
	}
	
	public boolean equals(Object s){		
		if(s instanceof Mermelades){
			if(s==mermeladeType)
				return true;
			return false;			
		}else return false;
	}
	
	public Mermelades getMermelade(){
		return mermeladeType;
	}
	
	public String getCategory(){
		return "Mermelade";
	}
	
	public static ArrayList<String> getLastChildsInHierarchy(){
		ArrayList<String> ingredients = new ArrayList<String>();
		
		for(int i=0; i<getMermeladesTypesLength(); i++ ){
			ingredients.add(getMermeladesTypes()[i].toString());
		}
		
		return ingredients;
	}
}
