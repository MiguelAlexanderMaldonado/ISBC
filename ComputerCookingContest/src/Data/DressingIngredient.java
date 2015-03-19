package Data;

import java.util.ArrayList;

public class DressingIngredient extends ProcessedFoodIngredient{
	
	private enum Dressing{
		Curry, Oil, PepperDressing;
	};
	
	private Dressing dressingType;
	
	public static  Dressing[] getDressingTypes()
	{
		return Dressing.class.getEnumConstants();
	}
	
	public static  int getDressingTypesLength()
	{
		return Dressing.class.getEnumConstants().length;
	}
	
	public boolean equals(Object s){		
		if(s instanceof Dressing){
			if(s==dressingType)
				return true;
			return false;			
		}else return false;
	}
	
	public Dressing getDressing(){
		return dressingType;
	}
	
	public String getCategory(){
		return "Dressing";
	}
	
	public static ArrayList<String> getLastChildsInHierarchy(){
		ArrayList<String> ingredients = new ArrayList<String>();
		
		for(int i=0; i<getDressingTypesLength(); i++ ){
			ingredients.add(getDressingTypes()[i].toString());
		}
		
		return ingredients;
	}
}
