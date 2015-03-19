package Data;


public class Ingredients implements BaseIngredientOperators{
	
	private enum Ingredient{
		Bread, Animal, ProcessedFood, Vegetal;
	}
	
	private Ingredient ingredientType;
	
	public  Ingredient[] getIngredientTypes()
	{
		return Ingredient.class.getEnumConstants();
	}
	
	public  int getIngredientTypesLength()
	{
		return Ingredient.class.getEnumConstants().length;
	}
	
	public boolean equals(Object s){		
		if(s instanceof Ingredient){
			if(s==ingredientType)
				return true;
			return false;			
		}else return false;
	}
	
	public Ingredient getIngredient(){
		return ingredientType;
	}
}
