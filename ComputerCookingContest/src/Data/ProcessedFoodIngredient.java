package Data;

import java.util.ArrayList;


public class ProcessedFoodIngredient extends Ingredients{

	private enum ProcessedFood{
		Dairy, Dressing, Mermelades, Sauces;
	};
	
	private ProcessedFood processedFoodType;

	public static  ProcessedFood[] getProcessedFoodTypes(){
		return ProcessedFood.class.getEnumConstants();
	}
		
	public static  int getProcessedFoodTypesLength(){
		return ProcessedFood.class.getEnumConstants().length;
	}	
	
	public boolean equals(Object s){
		if(s instanceof ProcessedFood){
			if((ProcessedFood)s==processedFoodType)
				return true;
			return false;
		}else return false;
	}
	
	public ProcessedFood getProcessedFood(){
		return processedFoodType;
	}
	
	public String getCategory(){
		return "ProcessedFood";
	}
	
	public static ArrayList<String> getLastChildsInHierarchy(){
		ArrayList<String> ingredients = new ArrayList<String>();
			
		ingredients.addAll(DairyIngredient.getLastChildsInHierarchy());
		ingredients.addAll(DressingIngredient.getLastChildsInHierarchy());
		ingredients.addAll(MermeladesIngredient.getLastChildsInHierarchy());
		ingredients.addAll(SaucesIngredient.getLastChildsInHierarchy());
		
		return ingredients;
	}
}
