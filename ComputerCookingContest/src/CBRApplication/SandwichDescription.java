package CBRApplication;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CaseComponent;

public class SandwichDescription implements CaseComponent {

	 private String caseID;
	 private String ingredient1;
	 private String ingredient2;
	 private String ingredient3;
	 private String ingredient4;
	 private String ingredient5;
	
	public SandwichDescription()
	{
		caseID = null;
		ingredient1= null;
		ingredient2= null;
		ingredient3= null;
		ingredient4= null;
		ingredient5= null;
	}
	 
	public String toString()
	{
		return "("+caseID+";"+ingredient1+";"+ingredient2+";"+ingredient3+";"+ingredient4+";"+ingredient5+")";
	}
	
	@Override
	public Attribute getIdAttribute() {
		// TODO Auto-generated method stub
		return new Attribute("caseID", SandwichDescription.class);
		
	}
	
	public String getCaseID() {
		return caseID;
	}

	public void setCaseID(String caseID) {
		this.caseID = caseID;
	}
	
	public String getIngredient1() {
		return ingredient1;
	}

	public void setIngredient1(String ingredient1) {
		this.ingredient1 = ingredient1;
	}
	
	public String getIngredient2() {
		return ingredient2;
	}

	public void setIngredient2(String ingredient2) {
		this.ingredient2 = ingredient2;
	}

	public String getIngredient3() {
		return ingredient3;
	}

	public void setIngredient3(String ingredient3) {
		this.ingredient3 = ingredient3;
	}

	public String getIngredient4() {
		return ingredient4;
	}

	public void setIngredient4(String ingredient4) {
		this.ingredient4 = ingredient4;
	}

	public String getIngredient5() {
		return ingredient5;
	}

	public void setIngredient5(String ingredient5) {
		this.ingredient5 = ingredient5;
	}
	
	public void setIngredient(int i, String ingredient)
	{
		switch(i)
		{		
		case 1:
			setIngredient1(ingredient);
			break;			
		case 2:
			setIngredient2(ingredient);
			break;
		case 3:
			setIngredient3(ingredient);
			break;
		case 4:
			setIngredient4(ingredient);
			break; 
		case 5:
			setIngredient5(ingredient);
			break;
		}
	}
}
