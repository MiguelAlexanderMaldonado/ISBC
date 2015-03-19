package Data;

import java.util.ArrayList;

public class VegetalFruitIngredient extends VegetableIngredient{

	private enum VegetalFruit{
		Avocado, Chile, Pepper, Tomato;
	};
	
	private VegetalFruit vegetalFruitType;
	
	public static  VegetalFruit[] getVegetalFruitTypes()
	{
		return VegetalFruit.class.getEnumConstants();
	}
	
	public static  int getVegetalFruitTypesLength()
	{
		return VegetalFruit.class.getEnumConstants().length;
	}
	
	public boolean equals(Object s){		
		if(s instanceof VegetalFruit){
			if(s==vegetalFruitType)
				return true;
			return false;			
		}else return false;
	}
	
	public VegetalFruit getVegetalFruit(){
		return vegetalFruitType;
	}
	
	public String getCategory(){
		return "VegetalFruit";
	}
	
	public static ArrayList<String> getLastChildsInHierarchy(){
		ArrayList<String> ingredients = new ArrayList<String>();
		
		for(int i=0; i<getVegetalFruitTypesLength(); i++)
		{
			ingredients.add(getVegetalFruitTypes()[i].toString());
		}
		
		return ingredients;
	}
}
