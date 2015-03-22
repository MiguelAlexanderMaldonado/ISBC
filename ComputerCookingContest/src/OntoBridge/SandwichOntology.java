package OntoBridge;

import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import jcolibri.util.FileIO;
import es.ucm.fdi.gaia.ontobridge.OntoBridge;
import es.ucm.fdi.gaia.ontobridge.OntologyDocument;

import Utils.StringEvaluator;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BoxLayout;

public class SandwichOntology {
	
	private String FICHERO_OWL = "OntoBridge/SandwichOntology.owl";
	private static PnlConceptsTree treeRestrictions;						//Árbol de restricciones	
	private static JPanel panelContainerRestrictions = new JPanel();		//Panel contenedor
	private static JPanel panelSelectedRestrictions = new JPanel();			//Panel para las restricciones que se seleccionan
	private static ArrayList<String> positiveRestrictions = new ArrayList<String>();//Array en el que se registran las restricciones positivas
	private static ArrayList<String> negativeRestrictions = new ArrayList<String>();//Array en el que se registran las restricciones negativas
	private static OntoBridge ob;
	private static boolean isTpeSandwichAdded = false;

	/**
	 * @wbp.parser.entryPoint
	 */
	public SandwichOntology() {
		
		ob = new OntoBridge();
		ob.initWithPelletReasoner();
		
		OntologyDocument mainOnto = new OntologyDocument(FileIO.findFile(FICHERO_OWL).toExternalForm());
		ArrayList<OntologyDocument> subOntologies = new ArrayList<OntologyDocument>();
		
		ob.loadOntology(mainOnto, subOntologies, false);
				
		treeRestrictions = new PnlConceptsTree(ob);		
		
		panelContainerRestrictions.setLayout(new BoxLayout(panelContainerRestrictions, BoxLayout.X_AXIS));
		panelContainerRestrictions.add(treeRestrictions);
		
		panelContainerRestrictions.add(panelSelectedRestrictions);
		panelSelectedRestrictions.setLayout(new BorderLayout(0, 0));
		
	}
	
	public PnlConceptsTree getOntologyTree()
	{		
		return treeRestrictions;		
	}
	
	public JPanel getPanelContainerRestrictions()
	{		
		return panelContainerRestrictions;		
	}
	
	public ArrayList<String> getPositiveRestrictions(){
		
		return positiveRestrictions;
		
	}
	
	public ArrayList<String> getNegativeRestrictions(){
		
		return negativeRestrictions;
		
	}
	
	public static OntoBridge getOntoBridge() { 
		
		return ob;
		
	}
	
	/**
	 * 
	 * @param visible
	 * False:	Se inhabilita la selección en el árbol de restricciones.
	 * True:	Se habilita la selección.
	 */
	public static void setEnabled(boolean visible) {
		
		if(visible) {
			
			treeRestrictions.setMinIndexOfSelections(0);
			
		} else {
			//Se cambia el indice mínimo de selección a infinito (Esto inhabilita la selección)
			treeRestrictions.setMinIndexOfSelections(Integer.MAX_VALUE);
			//Se limpian los arrays de restricciones
			positiveRestrictions.clear();
			negativeRestrictions.clear();
			addRestrictions();
		}
		
	}
	
	/**
	 * Añade las restricciones del array al panel que contiene las restricciones.
	 */
	public static void addRestrictions() {
		
		panelSelectedRestrictions.removeAll();
		panelSelectedRestrictions = new JPanel();
		panelSelectedRestrictions.setLayout(new GridLayout(10, 1));
		
		// Por cada restricción del array se crea un botón que se añade al panel de restricciones
		for(int i=0; i <positiveRestrictions.size(); i++) {
			
			final JButton positiveRestrictionButton = new JButton(positiveRestrictions.get(i));
			positiveRestrictionButton.setBackground(Color.green);
			
			positiveRestrictionButton.addMouseListener(new MouseAdapter() {
								
				@Override
				public void mousePressed(MouseEvent arg0) {
					// TODO Auto-generated method stub
					int dialogResult = JOptionPane.showConfirmDialog (null, "¿Desea borrar la restricción?","",JOptionPane.YES_NO_OPTION);
					
					if(dialogResult == JOptionPane.YES_OPTION) {
						
						positiveRestrictions.remove(positiveRestrictions.indexOf(positiveRestrictionButton.getText()));
						addRestrictions();
					}
				}
			});
			
			positiveRestrictionButton.setVisible(true);
			positiveRestrictionButton.validate();			
			panelSelectedRestrictions.add(positiveRestrictionButton, BorderLayout.EAST);
			panelSelectedRestrictions.setVisible(true);
			
			panelContainerRestrictions.repaint();
			panelContainerRestrictions.validate();
			
		}	
		
		for(int j=0; j <negativeRestrictions.size(); j++) {
			
			final JButton negativeRestrictionButton = new JButton(negativeRestrictions.get(j));
			negativeRestrictionButton.setBackground(Color.red);
			
			negativeRestrictionButton.addMouseListener(new MouseAdapter() {
								
				@Override
				public void mousePressed(MouseEvent arg0) {
					// TODO Auto-generated method stub
					int dialogResult = JOptionPane.showConfirmDialog (null, "¿Desea borrar la restricción?","",JOptionPane.YES_NO_OPTION);
					
					if(dialogResult == JOptionPane.YES_OPTION) {
						
						negativeRestrictions.remove(negativeRestrictions.indexOf(negativeRestrictionButton.getText()));
						addRestrictions();
					}
				}
			});
			
			negativeRestrictionButton.setVisible(true);
			negativeRestrictionButton.validate();			
			panelSelectedRestrictions.add(negativeRestrictionButton, BorderLayout.EAST);
			panelSelectedRestrictions.setVisible(true);
			
			panelContainerRestrictions.repaint();
			panelContainerRestrictions.validate();
			
		}
		
		
		panelContainerRestrictions.add(panelSelectedRestrictions, BorderLayout.WEST);
		panelContainerRestrictions.repaint();
		panelContainerRestrictions.validate();
		
	}
	
	/**
	 * Añade a los arrays de restricciones y al panel de restricciones
	 * el elemento seleccionado en el árbol.
	 * @param concept
	 */
	public static void getSelectedConcept(String concept) {
		
		int dialogResult = JOptionPane.showConfirmDialog (null, "¿Desea añadirla?","",JOptionPane.YES_NO_OPTION);
	
		concept = StringEvaluator.getLastWord(treeRestrictions.getSelectedConcept(),", ");
		
		if(positiveRestrictions.size()<1)
			isTpeSandwichAdded = false;
		
		
		if(!concept.equals("SandwichIngredients") && !concept.equals("Sandwich") && !concept.equals("SandwichBase")) {
				
			if(dialogResult == JOptionPane.YES_OPTION) {
				
				int dialogResult2 = JOptionPane.showConfirmDialog (null, "¿Desea que el sándwich contenga esa restricción?","",JOptionPane.YES_NO_OPTION);
										
				if(dialogResult2 == JOptionPane.YES_OPTION ) {
					
					if(!isTpeSandwichAdded && positiveRestrictions.size()<5 && !negativeRestrictions.contains(concept)) {
					
						positiveRestrictions.add(concept);
						addRestrictions();
						
					}else {
						
						if(negativeRestrictions.contains(concept)) JOptionPane.showMessageDialog(null, "No se pueden añadir");
						else JOptionPane.showMessageDialog(null, "No se pueden añadir más");		
						
					}
					
					if(OntologyUsefulFunctions.getSuperClass(concept).equals("Sandwich"))
						isTpeSandwichAdded = true;					
					
				}else {
					
					if(negativeRestrictions.size()<2 && !positiveRestrictions.contains(concept)) {
					
						if(!OntologyUsefulFunctions.getSuperClass(concept).equals("Sandwich")) {
						
							negativeRestrictions.add(concept);
							addRestrictions();
													
						}else JOptionPane.showMessageDialog(null, "No se puede añadir, tipo no válido");							
						
					}else {
						if(positiveRestrictions.contains(concept)) JOptionPane.showMessageDialog(null, "No se pueden añadir");
						else JOptionPane.showMessageDialog(null, "No se pueden añadir más");					
					}
					
				}	
				
				if(isTpeSandwichAdded) {
					
					if(positiveRestrictions.size()>1) {
						
						positiveRestrictions.clear();
						positiveRestrictions.add(concept);
						addRestrictions();
						
					}
					
				}
				
			}				
			
		} else JOptionPane.showMessageDialog(null, "No se puede añadir, tipo no válido");	
		
	}

}
