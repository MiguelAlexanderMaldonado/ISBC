package Data;

import java.util.ArrayList;

public class DerivativesIngredient extends AnimalIngredient{

	private enum Derivatives{
		Egg, Honey;
	};
	
	private Derivatives derivativesType;
	
	public static  Derivatives[] getDerivativesTypes()
	{
		return Derivatives.class.getEnumConstants();
	}
	
	public static  int getDerivativesTypesLength()
	{
		return Derivatives.class.getEnumConstants().length;
	}
	
	public boolean equals(Object s){		
		if(s instanceof Derivatives){
			if(s==derivativesType)
				return true;
			return false;			
		}else return false;
	}
	
	public Derivatives getDerivative(){
		return derivativesType;
	}
	
	public String getCategory(){
		return "Derivatives";
	}
	
	public static ArrayList<String> getLastChildsInHierarchy(){
		ArrayList<String> ingredients = new ArrayList<String>();
		
		for(int i=0; i<getDerivativesTypesLength(); i++)
		{
			ingredients.add(getDerivativesTypes()[i].toString());
		}
		
		return ingredients;
	}
}
