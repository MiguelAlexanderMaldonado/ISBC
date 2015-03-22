package UI;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.TextArea;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;

import OntoBridge.SandwichOntology;
import Utils.LevelChildHierarchy;

import Data.AnimalIngredient;
import Data.BirdIngredient;
import Data.DairyIngredient;
import Data.DerivativesIngredient;
import Data.DressingIngredient;
import Data.FishIngredient;
import Data.FruitIngredient;
import Data.MeatIngredient;
import Data.MermeladesIngredient;
import Data.PlantIngredient;
import Data.ProcessedFoodIngredient;
import Data.SaucesIngredient;
import Data.VegetableIngredient;
import Data.VegetalFruitIngredient;
import Data.VegetalIngredient;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class UIRecommender extends UI {
	
	public UIRecommender() {		
		
		new UI();	

	}
	
	public void configuredSandwichOntology(SandwichOntology sandwichOntology) {
		
		panelTreeRestrictionsContainer.setBounds(634, 32, 390, 463);
		
		panelTreeRestrictionsContainer.setLayout(new BoxLayout(panelTreeRestrictionsContainer, BoxLayout.LINE_AXIS));
		panelTreeRestrictionsContainer.add(sandwichOntology.getPanelContainerRestrictions(), BorderLayout.CENTER);
		
		JLabel label = new JLabel("Restricciones: ");
		label.setBounds(913, 7, 92, 14);
		getContentPane().add(label);

		SandwichOntology.setEnabled(false);
		
	}
	
	
	public Object getBreadIngredient()
	{
		return BreadComboBox.getSelectedItem();
	}
	
	public ArrayList<Object> getIngredientsHierarchy1(){
		
		ArrayList<Object> hierarchy =  new ArrayList<Object>();
		
		hierarchy.add(IngredientComboBox1a.getSelectedItem());
		hierarchy.add(IngredientComboBox1b.getSelectedItem());
		hierarchy.add(IngredientComboBox1c.getSelectedItem());
		hierarchy.add(IngredientComboBox1d.getSelectedItem());
		
		return hierarchy;
	}
	
	public ArrayList<Object> getIngredientsHierarchy2(){
		
		ArrayList<Object> hierarchy =  new ArrayList<Object>();
		
		hierarchy.add(IngredientComboBox2a.getSelectedItem());
		hierarchy.add(IngredientComboBox2b.getSelectedItem());
		hierarchy.add(IngredientComboBox2c.getSelectedItem());
		hierarchy.add(IngredientComboBox2d.getSelectedItem());
		
		return hierarchy;
	}
	
	public ArrayList<Object> getIngredientsHierarchy3(){
		
		ArrayList<Object> hierarchy =  new ArrayList<Object>();
		
		hierarchy.add(IngredientComboBox3a.getSelectedItem());
		hierarchy.add(IngredientComboBox3b.getSelectedItem());
		hierarchy.add(IngredientComboBox3c.getSelectedItem());
		hierarchy.add(IngredientComboBox3d.getSelectedItem());
		
		return hierarchy;
	}
	
	public ArrayList<Object> getIngredientsHierarchy4(){
		
		ArrayList<Object> hierarchy =  new ArrayList<Object>();
		
		hierarchy.add(IngredientComboBox4a.getSelectedItem());
		hierarchy.add(IngredientComboBox4b.getSelectedItem());
		hierarchy.add(IngredientComboBox4c.getSelectedItem());
		hierarchy.add(IngredientComboBox4d.getSelectedItem());
		
		return hierarchy;
	}
	

	public Button getGeneratorButton(){
		return GeneratorButton;
	}	
	
	public int getNumberOfIngredients()
	{
		int i = 1;
		
		if(getIngredientsHierarchy1().get(0)!=null && !getIngredientsHierarchy1().get(0).toString().contains("-"))
			i++;
		if(getIngredientsHierarchy2().get(0)!=null && !getIngredientsHierarchy2().get(0).toString().contains("-"))
			i++;
		if(getIngredientsHierarchy3().get(0)!=null && !getIngredientsHierarchy3().get(0).toString().contains("-"))
			i++;
		if(getIngredientsHierarchy4().get(0)!=null && !getIngredientsHierarchy4().get(0).toString().contains("-"))
			i++;
		
		return i;
	}

	public LevelChildHierarchy isLastIngredientInHierarchy(int i)
	{
		int j = 0;
		
		switch(i)
		{
		case 1:			
			while(j<4){
				if(getIngredientsHierarchy1().get(j)==null && j<4)
					return new LevelChildHierarchy(true, j);	
				if(getIngredientsHierarchy1().get(j)==null)
					return new LevelChildHierarchy(false, j);			
				if(getIngredientsHierarchy1().get(j).equals(EmptyItem))
					return new LevelChildHierarchy(false, j);
				j++;
			}
			break;			
		case 2:
			while(j<4){	
				if(getIngredientsHierarchy2().get(j)==null && j<4)
					return new LevelChildHierarchy(true, j);
				if(getIngredientsHierarchy2().get(j)==null)
					return new LevelChildHierarchy(false, j);				
				if(getIngredientsHierarchy2().get(j).equals(EmptyItem))
					return new LevelChildHierarchy(false, j);
				j++;
			}
			break;			
		case 3:
			while(j<4){	
				if(getIngredientsHierarchy3().get(j)==null && j<4)
					return new LevelChildHierarchy(true, j);
				if(getIngredientsHierarchy3().get(j)==null)
					return new LevelChildHierarchy(false, j);				
				if(getIngredientsHierarchy3().get(j).equals(EmptyItem))
					return new LevelChildHierarchy(false, j);
				j++;
			}
			break;			
		case 4:
			while(j<4){	
				if(getIngredientsHierarchy4().get(j)==null && j<4)
					return new LevelChildHierarchy(true, j);
				if(getIngredientsHierarchy4().get(j)==null)
					return new LevelChildHierarchy(false, j);				
				if(getIngredientsHierarchy4().get(j).equals(EmptyItem))
					return new LevelChildHierarchy(false, j);
				j++;
			}
			break;		
		}		
		return new LevelChildHierarchy(true, j);
	}

	public ArrayList<String> getLastIngredientsInHierarchy(String ingredient)
	{
		ArrayList<String> ingredients = new ArrayList<String>();
		
		//Meat Hierarchy
		if(animal.getCategory().equals(ingredient)){
			ingredients = AnimalIngredient.getLastChildsInHierarchy();
		}
		else if(derivatives.getCategory().equals(ingredient)){
			ingredients = DerivativesIngredient.getLastChildsInHierarchy();
		}
		else if(meat.getCategory().equals(ingredient)){
			ingredients = MeatIngredient.getLastChildsInHierarchy();
		}
		else if(bird.getCategory().equals(ingredient)){
			ingredients = BirdIngredient.getLastChildsInHierarchy();
		}
		else if(fish.getCategory().equals(ingredient)){
			ingredients = FishIngredient.getLastChildsInHierarchy();
		}
		//ProcessedFood Hierarchy
		else if(processedFood.getCategory().equals(ingredient)){
			ingredients = ProcessedFoodIngredient.getLastChildsInHierarchy();
		}
		else if(dairy.getCategory().equals(ingredient)){
			ingredients = DairyIngredient.getLastChildsInHierarchy();
		}
		else if(dressing.getCategory().equals(ingredient)){
			ingredients = DressingIngredient.getLastChildsInHierarchy();
		}
		else if(mermelades.getCategory().equals(ingredient)){
			ingredients = MermeladesIngredient.getLastChildsInHierarchy();
		}
		else if(sauces.getCategory().equals(ingredient)){
			ingredients = SaucesIngredient.getLastChildsInHierarchy();
		}
		//Vegetal Hierarchy
		else if(vegetal.getCategory().equals(ingredient)){
			ingredients = VegetalIngredient.getLastChildsInHierarchy();
		}
		else if(fruit.getCategory().equals(ingredient)){
			ingredients = FruitIngredient.getLastChildsInHierarchy();
		}
		else if(vegetable.getCategory().equals(ingredient)){
			ingredients = VegetableIngredient.getLastChildsInHierarchy();
		}
		else if(plant.getCategory().equals(ingredient)){
			ingredients = PlantIngredient.getLastChildsInHierarchy();
		}
		else if(vegetalFruit.getCategory().equals(ingredient)){
			ingredients = VegetalFruitIngredient.getLastChildsInHierarchy();
		}		
		
		return ingredients;
	}
	
	public TextArea getTextArea()
	{
		return textArea;
	}
	
	public JCheckBox getChckbxRestrictionsListMode(){
		
		return chckbxRestrictionsListMode;
		
	}
	
	public JCheckBox getChckbxRestrictionsTreeMode(){
		
		return chckbxRestrictionsTreeMode;
		
	}
	
}
