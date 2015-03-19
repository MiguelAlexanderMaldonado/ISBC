package Data;

import java.util.ArrayList;

public class VegetalIngredient extends Ingredients{

	private enum Vegetal{
		Fruit, Vegetable;
	};
	
	private Vegetal vegetalType;

	public  Vegetal[] getVegetalTypes(){
		return Vegetal.class.getEnumConstants();
	}
	
	public  int getVegetalTypesLength(){
		return Vegetal.class.getEnumConstants().length;
	}		

	public boolean equals(Object s){
		if(s instanceof Vegetal){
			if((Vegetal)s==vegetalType)
				return true;
			return false;
		}else return false;
	}
	
	public Vegetal getVegetal(){
		return vegetalType;
	}
	
	public String getCategory(){
		return "Vegetal";
	}
	
	public static ArrayList<String> getLastChildsInHierarchy(){
		ArrayList<String> ingredients = new ArrayList<String>();
			
		ingredients.addAll(FruitIngredient.getLastChildsInHierarchy());
		ingredients.addAll(VegetableIngredient.getLastChildsInHierarchy());
		
		return ingredients;
	}
}
