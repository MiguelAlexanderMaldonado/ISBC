package Data;

import java.util.ArrayList;

public class FruitIngredient extends VegetalIngredient{
	
	private enum Fruit{
		Banana, Strawberry;
	};
	
	private Fruit fruitType;
	
	public static  Fruit[] getFruitTypes()
	{
		return Fruit.class.getEnumConstants();
	}
	
	public static  int getFruitTypesLength()
	{
		return Fruit.class.getEnumConstants().length;
	}
	
	public boolean equals(Object s){		
		if(s instanceof Fruit){
			if(s==fruitType)
				return true;
			return false;			
		}else return false;
	}
	
	public Fruit getFruit(){
		return fruitType;
	}
	
	public String getCategory(){
		return "Fruit";
	}
	
	public static ArrayList<String> getLastChildsInHierarchy(){
		ArrayList<String> ingredients = new ArrayList<String>();
		
		for(int i=0; i<getFruitTypesLength(); i++)
		{
			ingredients.add(getFruitTypes()[i].toString());
		}
		
		return ingredients;
	}
}
