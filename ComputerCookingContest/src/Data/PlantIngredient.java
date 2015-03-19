package Data;

import java.util.ArrayList;

public class PlantIngredient extends VegetableIngredient{

	private enum Plant{
		Lettuce, Spinach;
	};
	
	private Plant plantType;
	
	public static  Plant[] getPlantTypes()
	{
		return Plant.class.getEnumConstants();
	}
	
	public static  int getPlantTypesLength()
	{
		return Plant.class.getEnumConstants().length;
	}
	
	public boolean equals(Object s){		
		if(s instanceof Plant){
			if(s==plantType)
				return true;
			return false;			
		}else return false;
	}
	
	public Plant getPlant(){
		return plantType;
	}
	
	public String getCategory(){
		return "Plant";
	}
	
	public static ArrayList<String> getLastChildsInHierarchy(){
		ArrayList<String> ingredients = new ArrayList<String>();
		
		for(int i=0; i<getPlantTypesLength(); i++)
		{
			ingredients.add(getPlantTypes()[i].toString());
		}
		
		return ingredients;
	}
}
