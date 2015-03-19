package UI;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import java.awt.TextArea;
import javax.swing.JLabel;
import javax.swing.JCheckBox;

import Data.AnimalIngredient;
import Data.BirdIngredient;
import Data.BreadIngredient;
import Data.DairyIngredient;
import Data.DerivativesIngredient;
import Data.DressingIngredient;
import Data.FishIngredient;
import Data.FruitIngredient;
import Data.Ingredients;
import Data.MeatIngredient;
import Data.MermeladesIngredient;
import Data.PlantIngredient;
import Data.ProcessedFoodIngredient;
//import Data.SandwichIngredients;
import Data.SaucesIngredient;
import Data.VegetableIngredient;
import Data.VegetalFruitIngredient;
import Data.VegetalIngredient;
import OntoBridge.SandwichOntology;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.awt.Button;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.Panel;
import java.awt.Checkbox;

@SuppressWarnings("serial")
public class UI extends JFrame {

	private JPanel contentPane;
	
	/**Controles**/
	private JCheckBox IngredientCheckBox1;
	private JCheckBox IngredientCheckBox2;
	private JCheckBox IngredientCheckBox3;
	private JCheckBox IngredientCheckBox4;
	
	protected JCheckBox chckbxRestrictionsListMode;
	protected JCheckBox chckbxRestrictionsTreeMode;
	
	protected JComboBox<Object> BreadComboBox;
	protected JComboBox<Object> IngredientComboBox1a;
	protected JComboBox<Object> IngredientComboBox1b;
	protected JComboBox<Object> IngredientComboBox1c;
	protected JComboBox<Object> IngredientComboBox1d;
	protected JComboBox<Object> IngredientComboBox2a;
	protected JComboBox<Object> IngredientComboBox2b;
	protected JComboBox<Object> IngredientComboBox2c;
	protected JComboBox<Object> IngredientComboBox2d;
	protected JComboBox<Object> IngredientComboBox3a;
	protected JComboBox<Object> IngredientComboBox3b;
	protected JComboBox<Object> IngredientComboBox3c;
	protected JComboBox<Object> IngredientComboBox3d;
	protected JComboBox<Object> IngredientComboBox4a;
	protected JComboBox<Object> IngredientComboBox4b;
	protected JComboBox<Object> IngredientComboBox4c;
	protected JComboBox<Object> IngredientComboBox4d;
	
	protected Button GeneratorButton;
	protected Button CleanerButton;
	
	private ArrayList< JComboBox<Object> > IngredientComboBox1List = new ArrayList< JComboBox<Object> >();
	private ArrayList< JComboBox<Object> > IngredientComboBox2List = new ArrayList< JComboBox<Object> >();
	private ArrayList< JComboBox<Object> > IngredientComboBox3List = new ArrayList< JComboBox<Object> >();
	private ArrayList< JComboBox<Object> > IngredientComboBox4List = new ArrayList< JComboBox<Object> >();
	
	private Ingredients ingredients = new Ingredients();
	
	protected BreadIngredient bread = new BreadIngredient();
	
	protected AnimalIngredient animal = new AnimalIngredient();
	protected DerivativesIngredient derivatives = new DerivativesIngredient();
	protected MeatIngredient meat = new MeatIngredient();
	protected BirdIngredient bird = new BirdIngredient();
	protected FishIngredient fish = new FishIngredient();
	
	protected ProcessedFoodIngredient processedFood = new ProcessedFoodIngredient();
	protected DairyIngredient dairy = new DairyIngredient();
	protected DressingIngredient dressing = new DressingIngredient();
	protected MermeladesIngredient mermelades = new MermeladesIngredient();
	protected SaucesIngredient sauces = new SaucesIngredient();
	
	protected VegetalIngredient vegetal = new VegetalIngredient();
	protected FruitIngredient fruit = new FruitIngredient();
	protected VegetableIngredient vegetable = new VegetableIngredient();
	protected PlantIngredient plant = new PlantIngredient();
	protected VegetalFruitIngredient vegetalFruit = new VegetalFruitIngredient();
	
	
	protected String EmptyItem = "------------------------";
	
	protected TextArea textArea;

	private void UILoadCheckBoxes(JPanel contentPane2) {
		// TODO Auto-generated method stub
		JCheckBox BreadCheckBox = new JCheckBox("");
		BreadCheckBox.setEnabled(false);
		BreadCheckBox.setSelected(true);
		BreadCheckBox.setBounds(10, 123, 49, 23);
		contentPane.add(BreadCheckBox);
		
		IngredientCheckBox1 = new JCheckBox("");
		IngredientCheckBox1.setToolTipText("First Ingredient");
		IngredientCheckBox1.setBounds(10, 155, 49, 23);
		contentPane.add(IngredientCheckBox1);
		
		IngredientCheckBox2 = new JCheckBox("");
		IngredientCheckBox2.setToolTipText("Second Ingredient");
		IngredientCheckBox2.setBounds(10, 181, 49, 23);
		IngredientCheckBox2.setEnabled(false);
		contentPane.add(IngredientCheckBox2);
		
		IngredientCheckBox3 = new JCheckBox("");
		IngredientCheckBox3.setToolTipText("Third Ingredient");
		IngredientCheckBox3.setBounds(10, 207, 49, 23);
		IngredientCheckBox3.setEnabled(false);
		contentPane.add(IngredientCheckBox3);
		
		IngredientCheckBox4 = new JCheckBox("");
		IngredientCheckBox4.setToolTipText("Fourth Ingredient");
		IngredientCheckBox4.setBounds(10, 233, 49, 23);
		IngredientCheckBox4.setEnabled(false);
		contentPane.add(IngredientCheckBox4);
	}

	private void UILoadComboBoxes(JPanel contentPane2) {
		// TODO Auto-generated method stub
		IngredientComboBox1a = new JComboBox<Object>();
		IngredientComboBox1a.setEnabled(false);
		IngredientComboBox1a.setBounds(77, 158, 130, 20);
		contentPane.add(IngredientComboBox1a);
		
		IngredientComboBox2a = new JComboBox<Object>();
		IngredientComboBox2a.setEnabled(false);
		IngredientComboBox2a.setBounds(77, 184, 130, 20);
		contentPane.add(IngredientComboBox2a);
		
		IngredientComboBox3a = new JComboBox<Object>();
		IngredientComboBox3a.setEnabled(false);
		IngredientComboBox3a.setBounds(77, 210, 130, 20);
		contentPane.add(IngredientComboBox3a);		
		
		IngredientComboBox4a = new JComboBox<Object>();
		IngredientComboBox4a.setEnabled(false);
		IngredientComboBox4a.setBounds(77, 236, 130, 20);
		contentPane.add(IngredientComboBox4a);
		
		BreadComboBox = new JComboBox<Object>();
		BreadComboBox.setBounds(77, 127, 130, 20);
		contentPane.add(BreadComboBox);		
		
		IngredientComboBox1b = new JComboBox<Object>();
		IngredientComboBox1b.setEnabled(false);
		IngredientComboBox1b.setBounds(217, 158, 130, 20);
		contentPane.add(IngredientComboBox1b);
		
		IngredientComboBox1c = new JComboBox<Object>();
		IngredientComboBox1c.setEnabled(false);
		IngredientComboBox1c.setBounds(355, 158, 130, 20);
		contentPane.add(IngredientComboBox1c);
		
		IngredientComboBox1d = new JComboBox<Object>();
		IngredientComboBox1d.setEnabled(false);
		IngredientComboBox1d.setBounds(494, 158, 130, 20);
		contentPane.add(IngredientComboBox1d);
		
		IngredientComboBox2b = new JComboBox<Object>();
		IngredientComboBox2b.setEnabled(false);
		IngredientComboBox2b.setBounds(217, 184, 130, 20);
		contentPane.add(IngredientComboBox2b);
		
		IngredientComboBox2c = new JComboBox<Object>();
		IngredientComboBox2c.setEnabled(false);
		IngredientComboBox2c.setBounds(355, 184, 130, 20);
		contentPane.add(IngredientComboBox2c);
		
		IngredientComboBox2d = new JComboBox<Object>();
		IngredientComboBox2d.setEnabled(false);
		IngredientComboBox2d.setBounds(494, 184, 130, 20);
		contentPane.add(IngredientComboBox2d);
		
		IngredientComboBox3b = new JComboBox<Object>();
		IngredientComboBox3b.setEnabled(false);
		IngredientComboBox3b.setBounds(217, 210, 130, 20);
		contentPane.add(IngredientComboBox3b);
		
		IngredientComboBox3c = new JComboBox<Object>();
		IngredientComboBox3c.setEnabled(false);
		IngredientComboBox3c.setBounds(355, 210, 130, 20);
		contentPane.add(IngredientComboBox3c);
		
		IngredientComboBox3d = new JComboBox<Object>();
		IngredientComboBox3d.setEnabled(false);
		IngredientComboBox3d.setBounds(494, 210, 130, 20);
		contentPane.add(IngredientComboBox3d);
		
		IngredientComboBox4b = new JComboBox<Object>();
		IngredientComboBox4b.setEnabled(false);
		IngredientComboBox4b.setBounds(217, 236, 130, 20);
		contentPane.add(IngredientComboBox4b);
		
		IngredientComboBox4c = new JComboBox<Object>();
		IngredientComboBox4c.setEnabled(false);
		IngredientComboBox4c.setBounds(355, 236, 130, 20);
		contentPane.add(IngredientComboBox4c);
		
		IngredientComboBox4d = new JComboBox<Object>();
		IngredientComboBox4d.setEnabled(false);
		IngredientComboBox4d.setBounds(494, 236, 130, 20);
		contentPane.add(IngredientComboBox4d);
	}
	
	private void EnabledNextCheckBox(JCheckBox ingredientCheckBox) {
		// TODO Auto-generated method stub
		if(IngredientComboBox1a.getSelectedItem()!=null && !IngredientComboBox1a.getSelectedItem().toString().equals(EmptyItem)){
			IngredientCheckBox2.setEnabled(true);
		}else IngredientCheckBox2.setEnabled(false);
		
		if(IngredientComboBox2a.getSelectedItem()!=null && !IngredientComboBox2a.getSelectedItem().toString().equals(EmptyItem)){
			IngredientCheckBox3.setEnabled(true);
		}else IngredientCheckBox3.setEnabled(false);
		
		if(IngredientComboBox3a.getSelectedItem()!=null && !IngredientComboBox3a.getSelectedItem().toString().equals(EmptyItem)){
			IngredientCheckBox4.setEnabled(true);
		}else IngredientCheckBox4.setEnabled(false);
	}
	
	private void CleanDropDownListIngredientsHierarchy(String IDcheckBox) {
		// TODO Auto-generated method stub
		if(IDcheckBox.equals("First Ingredient"))
		{
			CleanUISecondDropDownListIngredientsHierarchy();
		}
		else if(IDcheckBox.equals("Second Ingredient"))
		{
			CleanUIThirdDropDownListIngredientsHierarchy();
		}
		else if(IDcheckBox.equals("Third Ingredient"))
		{
			CleanUIQuarteDropDownListIngredientsHierarchy();
		}
	}
	
	private void CleanUISecondDropDownListIngredientsHierarchy() {
		// TODO Auto-generated method stub
		if(IngredientCheckBox2.isSelected())
		{
			IngredientCheckBox2.setSelected(false);
			IngredientComboBox2List.get(0).setEnabled(false);
			IngredientComboBox2List.get(0).removeAllItems();
			IngredientComboBox2List.get(1).setEnabled(false);
			IngredientComboBox2List.get(1).removeAllItems();
			IngredientComboBox2List.get(2).setEnabled(false);
			IngredientComboBox2List.get(2).removeAllItems();
			IngredientComboBox2List.get(3).setEnabled(false);
			IngredientComboBox2List.get(3).removeAllItems();
			CleanUIThirdDropDownListIngredientsHierarchy();
		}
	}
	
	private void CleanUIThirdDropDownListIngredientsHierarchy() {
		// TODO Auto-generated method stub
		if(IngredientCheckBox3.isSelected())
		{
			IngredientCheckBox3.setSelected(false);
			IngredientComboBox3List.get(0).setEnabled(false);
			IngredientComboBox3List.get(0).removeAllItems();
			IngredientComboBox3List.get(1).setEnabled(false);
			IngredientComboBox3List.get(1).removeAllItems();
			IngredientComboBox3List.get(2).setEnabled(false);
			IngredientComboBox3List.get(2).removeAllItems();
			IngredientComboBox3List.get(3).setEnabled(false);
			IngredientComboBox3List.get(3).removeAllItems();
			CleanUIQuarteDropDownListIngredientsHierarchy();
		}
	}
	
	private void CleanUIQuarteDropDownListIngredientsHierarchy() {
		// TODO Auto-generated method stub
		if(IngredientCheckBox4.isSelected())
		{
			IngredientCheckBox4.setSelected(false);
			IngredientComboBox4List.get(0).setEnabled(false);
			IngredientComboBox4List.get(0).removeAllItems();
			IngredientComboBox4List.get(1).setEnabled(false);
			IngredientComboBox4List.get(1).removeAllItems();
			IngredientComboBox4List.get(2).setEnabled(false);
			IngredientComboBox4List.get(2).removeAllItems();
			IngredientComboBox4List.get(3).setEnabled(false);
			IngredientComboBox4List.get(3).removeAllItems();
		}
	}
	
	private void UIFirstDropDownListIngredientsHierarchy( final JCheckBox ingredientCheckBox, final ArrayList<JComboBox<Object>> ingredientComboBoxList) {
		ingredientCheckBox.addItemListener(new ItemListener() {			
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				
				if(e.getStateChange()==ItemEvent.SELECTED){				
									
					ingredientComboBoxList.get(0).setEnabled(true);
					ingredientComboBoxList.get(0).removeAllItems();
					ingredientComboBoxList.get(0).addItem(EmptyItem);
					ingredientComboBoxList.get(0).setSelectedItem(EmptyItem);					
					
					for(int i=1; i<ingredients.getIngredientTypesLength(); i++)
					{
						ingredientComboBoxList.get(0).addItem(ingredients.getIngredientTypes()[i]);
					}					
					
				}else{
					
					ingredientComboBoxList.get(0).setEnabled(false);
					ingredientComboBoxList.get(0).removeAllItems();
					ingredientComboBoxList.get(1).setEnabled(false);
					ingredientComboBoxList.get(1).removeAllItems();
					ingredientComboBoxList.get(2).setEnabled(false);
					ingredientComboBoxList.get(2).removeAllItems();
					ingredientComboBoxList.get(3).setEnabled(false);
					ingredientComboBoxList.get(3).removeAllItems();
					
					CleanDropDownListIngredientsHierarchy(ingredientCheckBox.getToolTipText());
				}
			}
			
		});		
	}
	
	private void UISecondDropDownListIngredientsHierarchy( final JCheckBox ingredientCheckBox, final ArrayList<JComboBox<Object>> ingredientComboBoxList) {
		ingredientComboBoxList.get(0).addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				//System.out.println(getIngredientsHierarchy1()); 
				
				EnabledNextCheckBox(ingredientCheckBox);
				
				ingredientComboBoxList.get(1).setEnabled(true);					
				ingredientComboBoxList.get(1).removeAllItems();
				ingredientComboBoxList.get(1).addItem(EmptyItem);
				ingredientComboBoxList.get(1).setSelectedItem(EmptyItem);
				
				switch(e.getItem().toString()){
				
					case "Animal":{ 
						for(int i=0; i<animal.getAnimalTypesLength(); i++){
							ingredientComboBoxList.get(1).addItem(animal.getAnimalTypes()[i]);
						}
						break;
					}
					case "ProcessedFood":{ 
						for(int i=0; i<ProcessedFoodIngredient.getProcessedFoodTypesLength(); i++){
							ingredientComboBoxList.get(1).addItem(ProcessedFoodIngredient.getProcessedFoodTypes()[i]);
						}
						break;
					}
					case "Vegetal":{
						for(int i=0; i<vegetal.getVegetalTypesLength(); i++){
							ingredientComboBoxList.get(1).addItem(vegetal.getVegetalTypes()[i]);
						}
						break;
					}
					
					default:{
						ingredientComboBoxList.get(1).removeAllItems();
						ingredientComboBoxList.get(1).setEnabled(false);
						break;					
					}				
				}
			}
		});
	}
	
	private void UIThirdDropDownListIngredientsHierarchy( final JCheckBox ingredientCheckBox, final ArrayList<JComboBox<Object>> ingredientComboBoxList) {
		ingredientComboBoxList.get(1).addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				//System.out.println(e.getItem().toString()); 
				
				ingredientComboBoxList.get(2).setEnabled(true);					
				ingredientComboBoxList.get(2).removeAllItems();
				ingredientComboBoxList.get(2).addItem(EmptyItem);
				ingredientComboBoxList.get(2).setSelectedItem(EmptyItem);
				
				switch(e.getItem().toString()){
				
					case "Derivatives":{ 
						for(int i=0; i<DerivativesIngredient.getDerivativesTypesLength(); i++){
							ingredientComboBoxList.get(2).addItem(DerivativesIngredient.getDerivativesTypes()[i]);
						}
						break;
					}
					case "Meat":{ 
						for(int i=0; i<MeatIngredient.getMeatTypesLength(); i++){
							ingredientComboBoxList.get(2).addItem(MeatIngredient.getMeatTypes()[i]);
						}
						break;
					}
					case "Dairy":{
						for(int i=0; i<DairyIngredient.getDairyTypesLength(); i++){
							ingredientComboBoxList.get(2).addItem(DairyIngredient.getDairyTypes()[i]);
						}
						break;
					}
					case "Dressing":{
						for(int i=0; i<DressingIngredient.getDressingTypesLength(); i++){
							ingredientComboBoxList.get(2).addItem(DressingIngredient.getDressingTypes()[i]);
						}
						break;
					}
					case "Mermelades":{
						for(int i=0; i<MermeladesIngredient.getMermeladesTypesLength(); i++){
							ingredientComboBoxList.get(2).addItem(MermeladesIngredient.getMermeladesTypes()[i]);
						}
						break;
					}
					case "Sauces":{
						for(int i=0; i<SaucesIngredient.getSaucesTypesLength(); i++){
							ingredientComboBoxList.get(2).addItem(SaucesIngredient.getSaucesTypes()[i]);
						}
						break;
					}
					case "Fruit":{
						for(int i=0; i<FruitIngredient.getFruitTypesLength(); i++){
							ingredientComboBoxList.get(2).addItem(FruitIngredient.getFruitTypes()[i]);
						}
						break;
					}
					case "Vegetable":{
						for(int i=0; i<vegetable.getVegetableTypesLength(); i++){
							ingredientComboBoxList.get(2).addItem(vegetable.getVegetableTypes()[i]);
						}
						break;
					}
					
					default:{
						ingredientComboBoxList.get(2).removeAllItems();
						ingredientComboBoxList.get(2).setEnabled(false);
						break;					
					}				
				}
			}
		});	
	}
	
	private void UIQuarteDropDownListIngredientsHierarchy( final JCheckBox ingredientCheckBox, final ArrayList<JComboBox<Object>> ingredientComboBoxList) {
		ingredientComboBoxList.get(2).addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				//System.out.println(e.getItem().toString()); 
				
				ingredientComboBoxList.get(3).setEnabled(true);					
				ingredientComboBoxList.get(3).removeAllItems();
				ingredientComboBoxList.get(3).addItem(EmptyItem);
				ingredientComboBoxList.get(3).setSelectedItem(EmptyItem);
				
				switch(e.getItem().toString()){
				
					case "Bird":{ 
						for(int i=0; i<BirdIngredient.getBirdTypesLength(); i++){
							ingredientComboBoxList.get(3).addItem(BirdIngredient.getBirdTypes()[i]);
						}
						break;
					}
					case "Fish":{ 
						for(int i=0; i<FishIngredient.getFishTypesLength(); i++){
							ingredientComboBoxList.get(3).addItem(FishIngredient.getFishTypes()[i]);
						}
						break;
					}
					case "Plant":{ 
						for(int i=0; i<PlantIngredient.getPlantTypesLength(); i++){
							ingredientComboBoxList.get(3).addItem(PlantIngredient.getPlantTypes()[i]);
						}
						break;
					}	
					case "VegetalFruit":{ 
						for(int i=0; i<VegetalFruitIngredient.getVegetalFruitTypesLength(); i++){
							ingredientComboBoxList.get(3).addItem(VegetalFruitIngredient.getVegetalFruitTypes()[i]);
						}
						break;
					}	
					default:{
						ingredientComboBoxList.get(3).removeAllItems();
						ingredientComboBoxList.get(3).setEnabled(false);
						break;					
					}				
				}
			}
		});
	}
	
	private void UIListenersControl( final JCheckBox ingredientCheckBox, final ArrayList<JComboBox<Object>> ingredientComboBoxList) {
		// TODO Auto-generated method stub		
		
		UIFirstDropDownListIngredientsHierarchy(ingredientCheckBox,ingredientComboBoxList);
		
		UISecondDropDownListIngredientsHierarchy(ingredientCheckBox,ingredientComboBoxList);
		
		UIThirdDropDownListIngredientsHierarchy(ingredientCheckBox,ingredientComboBoxList);
		
		UIQuarteDropDownListIngredientsHierarchy(ingredientCheckBox,ingredientComboBoxList);
	
	}

	private void UIListenersButtons() {
		CleanerButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Clean de Text area");
				textArea.setText(null);
			}
		});
	}
	
	private void UIListenersChecks() {
		chckbxRestrictionsListMode.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {

				chckbxRestrictionsTreeMode.setSelected(false);
				SandwichOntology.setVisibility(false);
				
			}
		});
		chckbxRestrictionsTreeMode.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				if(!chckbxRestrictionsTreeMode.isSelected()){
					SandwichOntology.setVisibility(false);
				}
				else{
					SandwichOntology.setVisibility(true);
					chckbxRestrictionsListMode.setSelected(false);
				}		

			}
		});
	}
	
	/**
	 * Create the frame.
	 */
	protected UI() {
		setResizable(false);
		setTitle("Recomendador de Sandwiches");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 650, 524);
		contentPane = new JPanel();
		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textArea = new TextArea();
		textArea.setEditable(false);
		textArea.setBounds(10, 282, 336, 203);
		contentPane.add(textArea);
		
		JLabel lblIngredients = new JLabel("INGREDIENTES:");
		lblIngredients.setBounds(10, 102, 92, 14);
		contentPane.add(lblIngredients);
		
		UILoadCheckBoxes(contentPane);
		UILoadComboBoxes(contentPane);
				
		IngredientComboBox1List.add(IngredientComboBox1a);
		IngredientComboBox1List.add(IngredientComboBox1b);
		IngredientComboBox1List.add(IngredientComboBox1c);
		IngredientComboBox1List.add(IngredientComboBox1d);
		
		IngredientComboBox2List.add(IngredientComboBox2a);
		IngredientComboBox2List.add(IngredientComboBox2b);
		IngredientComboBox2List.add(IngredientComboBox2c);
		IngredientComboBox2List.add(IngredientComboBox2d);
		
		IngredientComboBox3List.add(IngredientComboBox3a);
		IngredientComboBox3List.add(IngredientComboBox3b);
		IngredientComboBox3List.add(IngredientComboBox3c);
		IngredientComboBox3List.add(IngredientComboBox3d);
		
		IngredientComboBox4List.add(IngredientComboBox4a);
		IngredientComboBox4List.add(IngredientComboBox4b);
		IngredientComboBox4List.add(IngredientComboBox4c);
		IngredientComboBox4List.add(IngredientComboBox4d);
		
		//DataIngredients = new SandwichIngredients();
		
		BreadComboBox.setEnabled(true);
		for(int i=0; i<BreadIngredient.getBreadTypesLength(); i++)
		{
			BreadComboBox.addItem(BreadIngredient.getBreadTypes()[i]);
		}
		BreadComboBox.setSelectedItem(BreadIngredient.getBreadTypes()[0]);		
		
		//UIListenersControl(IngredientCheckBox1,IngredientComboBox1List);
		
		UIListenersControl(IngredientCheckBox1,IngredientComboBox1List);
		UIListenersControl(IngredientCheckBox2,IngredientComboBox2List);
		UIListenersControl(IngredientCheckBox3,IngredientComboBox3List);
		UIListenersControl(IngredientCheckBox4,IngredientComboBox4List);
		
		GeneratorButton = new Button("Generar");
		GeneratorButton.setBounds(364, 282, 92, 23);
		contentPane.add(GeneratorButton);
		
		CleanerButton = new Button("Limpiar");
		CleanerButton.setBounds(364, 323, 92, 23);
		contentPane.add(CleanerButton);
		
		JLabel lblSelctedMode = new JLabel("MODO DE SELECI\u00D3N:");
		lblSelctedMode.setBounds(10, 11, 110, 14);
		contentPane.add(lblSelctedMode);
		
		chckbxRestrictionsListMode = new JCheckBox("Listas");
		chckbxRestrictionsListMode.setSelected(true);
		chckbxRestrictionsListMode.setBounds(10, 32, 97, 23);
		contentPane.add(chckbxRestrictionsListMode);
		
		chckbxRestrictionsTreeMode = new JCheckBox("\u00C1rbol");
		chckbxRestrictionsTreeMode.setBounds(109, 32, 59, 23);
		contentPane.add(chckbxRestrictionsTreeMode);
		
		JPanel PanelTreeIngredientsSelected = new JPanel();
		PanelTreeIngredientsSelected.setBounds(10, 62, 613, 29);
		contentPane.add(PanelTreeIngredientsSelected);
		
		Checkbox checkboxDeleteItems = new Checkbox("Borrar");
		checkboxDeleteItems.setBounds(575, 34, 49, 22);
		contentPane.add(checkboxDeleteItems);
		
		UIListenersButtons();		
		UIListenersChecks();
	}
}
