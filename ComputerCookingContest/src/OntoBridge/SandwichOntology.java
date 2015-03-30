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
	private static PnlConceptsTree treeRestrictions;						//�rbol de restricciones	
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
	 * False:	Se inhabilita la selecci�n en el �rbol de restricciones.
	 * True:	Se habilita la selecci�n.
	 */
	public static void setEnabled(boolean visible) {
		
		if(visible) {
			
			treeRestrictions.setMinIndexOfSelections(0);
			
		} else {
			//Se cambia el indice m�nimo de selecci�n a infinito (Esto inhabilita la selecci�n)
			treeRestrictions.setMinIndexOfSelections(Integer.MAX_VALUE);
			//Se limpian los arrays de restricciones
			positiveRestrictions.clear();
			negativeRestrictions.clear();
			addRestrictions();
		}
		
	}
	
	/**
	 * A�ade las restricciones del array al panel que contiene las restricciones.
	 */
	public static void addRestrictions() {
		
		panelSelectedRestrictions.removeAll();
		panelSelectedRestrictions = new JPanel();
		panelSelectedRestrictions.setLayout(new GridLayout(10, 1));
		
		// Por cada restricci�n del array se crea un bot�n que se a�ade al panel de restricciones
		for(int i=0; i <positiveRestrictions.size(); i++) {
			
			final JButton positiveRestrictionButton = new JButton(positiveRestrictions.get(i));
			positiveRestrictionButton.setBackground(Color.green);
			
			positiveRestrictionButton.addMouseListener(new MouseAdapter() {
								
				@Override
				public void mousePressed(MouseEvent arg0) {
					// TODO Auto-generated method stub
					int dialogResult = JOptionPane.showConfirmDialog (null, "�Desea borrar la restricci�n?","",JOptionPane.YES_NO_OPTION);
					
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
					int dialogResult = JOptionPane.showConfirmDialog (null, "�Desea borrar la restricci�n?","",JOptionPane.YES_NO_OPTION);
					
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
	
	/***
	 * Compruba si el Array de restricciones contiene el ingrediente concepto o alguno de sus hijos.
	 * @param restrictions
	 * @param concept
	 * @return
	 */
	private static boolean contains(ArrayList<String> restrictions, String concept){
		boolean contain = false;
		if(restrictions.size()>0){
			// Si el ingrediente concept es hoja (no tiene hijos)
			if(OntologyUsefulFunctions.isLastChild(concept)) {
				
				contain =  restrictions.contains(concept);
				
			}else{
						
				ArrayList<String> childs = OntologyUsefulFunctions.getLastChilds(concept);			
				int i =0;
				
				while(i < childs.size() && !contain){
					
					contain = restrictions.contains(childs.get(i));
					i++;
				}
				
			}	
		}
		return contain;
	}
	
	
	/**
	 * A�ade a los arrays de restricciones y al panel de restricciones
	 * el elemento seleccionado en el �rbol.
	 * @param concept
	 */
	public static void getSelectedConcept(String concept) {
		
		int dialogResult = -1;
		
		concept = StringEvaluator.getLastWord(treeRestrictions.getSelectedConcept(),", ");
		
		if(!concept.equals("SandwichIngredients") && !concept.equals("Sandwich") && !concept.equals("SandwichBase")) {
			
			if(OntologyUsefulFunctions.getSuperClass(concept).equals("Sandwich"))
				dialogResult = JOptionPane.showConfirmDialog (null, "�Desea a�adir el tipo de s�ndwich?","",JOptionPane.YES_NO_OPTION);
			else
				dialogResult = JOptionPane.showConfirmDialog (null, "�Desea que el s�ndwich contenga ese ingrediente?","",JOptionPane.YES_NO_OPTION);						
				
			if(dialogResult == JOptionPane.YES_OPTION) {
						
				if(positiveRestrictions.size()<2 && !negativeRestrictions.contains(concept) && !positiveRestrictions.contains(concept) &&
					!contains(negativeRestrictions,concept) && !contains(positiveRestrictions,concept)) {
				
					if(OntologyUsefulFunctions.getSuperClass(concept).equals("SandwichBase") && 
					!contains(positiveRestrictions,"SandwichBase"))	{	
						
						positiveRestrictions.add(concept);
						addRestrictions();
						
					}
					else if(OntologyUsefulFunctions.getSuperClass(concept).equals("Sandwich")){
						
						positiveRestrictions.add(concept);
						addRestrictions();
						
					}
					else if(!OntologyUsefulFunctions.getSuperClass(concept).equals("SandwichBase")){
						
						positiveRestrictions.add(concept);
						addRestrictions();
						
					} else JOptionPane.showMessageDialog(null, "No se pueden a�adir m�s");
					
				}else {
					
					if(contains(negativeRestrictions,concept)) JOptionPane.showMessageDialog(null, "No se puede a�adir");
					else JOptionPane.showMessageDialog(null, "No se pueden a�adir m�s");		
					
				}
				
				if(OntologyUsefulFunctions.getSuperClass(concept).equals("Sandwich"))
					isTpeSandwichAdded = true;					
				
			}else {
				
				if(negativeRestrictions.size()<2 &&  !negativeRestrictions.contains(concept) && !positiveRestrictions.contains(concept) &&
					!contains(positiveRestrictions,concept) && !contains(negativeRestrictions,concept)) {
				
					if(!OntologyUsefulFunctions.getSuperClass(concept).equals("Sandwich")) {
						
						if(OntologyUsefulFunctions.getSuperClass(concept).equals("SandwichBase") && 
								!contains(positiveRestrictions,"SandwichBase") && !contains(negativeRestrictions,"SandwichBase")){
							negativeRestrictions.add(concept);
							addRestrictions();
						}
						else if(!OntologyUsefulFunctions.getSuperClass(concept).equals("SandwichBase"))
						{	
							negativeRestrictions.add(concept);
							addRestrictions();
						}else JOptionPane.showMessageDialog(null, "No se pueden a�adir m�s");	
						
					}else JOptionPane.showMessageDialog(null, "No se puede a�adir, tipo no v�lido");							
					
				}else {
					if(contains(positiveRestrictions,concept)) JOptionPane.showMessageDialog(null, "No se puede a�adir de esta forma puesto que es un tipo de s�ndwich");
					else JOptionPane.showMessageDialog(null, "No se pueden a�adir m�s");					
				}
				
			}	
			
			if(isTpeSandwichAdded) {
				
				if(positiveRestrictions.size()>1) {
					
					positiveRestrictions.clear();
					positiveRestrictions.add(concept);
					addRestrictions();
					
				}
				
			}			
			
		} else if (concept.equals("SandwichIngredients") ) JOptionPane.showMessageDialog(null, "No se puede a�adir como ");	
		else JOptionPane.showMessageDialog(null, "No se puede a�adir, tipo no v�lido");	
		
	}

}
