package Data;

import java.util.ArrayList;

public class SaucesIngredient extends ProcessedFoodIngredient{

	private enum Sauces{
		GarlicSauce, Ketchup, Mayonnaise;
	};
	
	private Sauces saucesType;
	
	public static  Sauces[] getSaucesTypes()
	{
		return Sauces.class.getEnumConstants();
	}
	
	public static  int getSaucesTypesLength()
	{
		return Sauces.class.getEnumConstants().length;
	}
	
	public boolean equals(Object s){		
		if(s instanceof Sauces){
			if(s==saucesType)
				return true;
			return false;			
		}else return false;
	}
	
	public Sauces getSauce(){
		return saucesType;
	}
	
	public String getCategory(){
		return "Sauces";
	}
	
	public static ArrayList<String> getLastChildsInHierarchy(){
		ArrayList<String> ingredients = new ArrayList<String>();
		
		for(int i=0; i<getSaucesTypesLength(); i++ ){
			ingredients.add(getSaucesTypes()[i].toString());
		}
		
		return ingredients;
	}
}
