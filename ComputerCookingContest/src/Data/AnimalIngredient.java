package Data;

import java.util.ArrayList;

public class AnimalIngredient extends Ingredients{
	
	private enum Animal{
		Derivatives, Meat;
	};
	
	private Animal animalType;
	
	public  Animal[] getAnimalTypes()
	{
		return Animal.class.getEnumConstants();
	}
	
	public  int getAnimalTypesLength()
	{
		return Animal.class.getEnumConstants().length;
	}
	
	public boolean equals(Object s){
		if(s instanceof Animal){
			if((Animal)s==animalType)
				return true;
			return false;
		}else return false;
	}
	
	public Animal getAnimal(){
		return animalType;
	}
	
	public String getCategory(){
		return "Animal";
	}
	
	public static ArrayList<String> getLastChildsInHierarchy(){
		ArrayList<String> ingredients = new ArrayList<String>();
		
		ingredients.addAll(DerivativesIngredient.getLastChildsInHierarchy());
		ingredients.addAll(MeatIngredient.getLastChildsInHierarchy());
		
		return ingredients;
	}
}
