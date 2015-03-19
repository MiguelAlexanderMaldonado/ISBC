package Data;

import java.util.ArrayList;

public class VegetableIngredient extends VegetalIngredient{

	private enum Vegetable{
		Plant, VegetalFruit;
	};
	
	private Vegetable vegetableType;
	
	public  Vegetable[] getVegetableTypes()
	{
		return Vegetable.class.getEnumConstants();
	}
	
	public  int getVegetableTypesLength()
	{
		return Vegetable.class.getEnumConstants().length;
	}
	
	public boolean equals(Object s){		
		if(s instanceof Vegetable){
			if(s==vegetableType)
				return true;
			return false;			
		}else return false;
	}
	
	public Vegetable getVegetable(){
		return vegetableType;
	}
	
	public String getCategory(){
		return "Vegetable";
	}
	
	public static ArrayList<String> getLastChildsInHierarchy(){
		ArrayList<String> ingredients = new ArrayList<String>();
			
		ingredients.addAll(PlantIngredient.getLastChildsInHierarchy());
		ingredients.addAll(VegetalFruitIngredient.getLastChildsInHierarchy());
		
		return ingredients;
	}
}
