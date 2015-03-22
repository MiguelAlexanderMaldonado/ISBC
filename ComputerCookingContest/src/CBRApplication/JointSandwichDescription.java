package CBRApplication;

import java.util.ArrayList;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CaseComponent;

/***
 * Clase particular de descripción para los sándwiches necesaria para usar 
 * la ontología.
 * @author Miguel
 *
 */
public class JointSandwichDescription implements CaseComponent{

	private String caseID;
	private ArrayList<String> ingredients;
	
	public JointSandwichDescription() {
		
		caseID = null;
		ingredients = null;
	}
	
	@Override
	public Attribute getIdAttribute() {
		// TODO Auto-generated method stub
		return new Attribute("caseID", JointSandwichDescription.class);
	}
	public String getCaseID() {
		return caseID;
	}
	public void setCaseID(String caseID) {
		this.caseID = caseID;
	}
	public ArrayList<String> getIngredients() {
		return ingredients;
	}
	public void setIngredients(ArrayList<String> ingredients) {
		this.ingredients = ingredients;
	}
}
