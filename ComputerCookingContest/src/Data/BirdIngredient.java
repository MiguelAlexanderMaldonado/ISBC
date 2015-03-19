package Data;

import java.util.ArrayList;

public class BirdIngredient extends MeatIngredient{
	
	private enum Bird{
		Chicken, Duck, Turkey;
	};

	private Bird birdType;
	
	public static  Bird[] getBirdTypes()
	{
		return Bird.class.getEnumConstants();
	}
	
	public static  int getBirdTypesLength()
	{
		return Bird.class.getEnumConstants().length;
	}
	
	public boolean equals(Object s){		
		if(s instanceof Bird){
			if(s==birdType)
				return true;
			return false;			
		}else return false;
	}
	
	public Bird getBird(){
		return birdType;
	}
	
	public String getCategory(){
		return "Bird";
	}
	
	public static ArrayList<String> getLastChildsInHierarchy(){
		ArrayList<String> ingredients = new ArrayList<String>();
		
		for(int i=0; i<getBirdTypesLength(); i++ ){
			ingredients.add(getBirdTypes()[i].toString());
		}
		
		return ingredients;
	}
}
