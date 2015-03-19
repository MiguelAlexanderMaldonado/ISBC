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
	private static ArrayList<String> restrictions = new ArrayList<String>();//Array en el que se registran las restricciones
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public SandwichOntology() {
		
		OntoBridge ob = new OntoBridge();
		ob.initWithPelletReasoner();
		
		OntologyDocument mainOnto = new OntologyDocument(FileIO.findFile(FICHERO_OWL).toExternalForm());
		ArrayList<OntologyDocument> subOntologies = new ArrayList<OntologyDocument>();
		
		ob.loadOntology(mainOnto, subOntologies, true);
		
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
	
	/**
	 * 
	 * @param visible
	 * False:	Se inhabilita la selecci�n en el �rbol de restricciones.
	 * True:	Se habilita la selecci�n.
	 */
	public static void setEnabled(boolean visible){
		
		if(visible) {
			
			treeRestrictions.setMinIndexOfSelections(0);
			
		} else {
			//Se cambia el indice m�nimo de selecci�n a infinito (Esto inhabilita la selecci�n)
			treeRestrictions.setMinIndexOfSelections(Integer.MAX_VALUE);
			//Se limpia el array de restricciones
			restrictions.clear();
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
		for(int i=0; i <restrictions.size(); i++) {
			
			final JButton restrictionButton = new JButton(restrictions.get(i));
			restrictionButton.setBackground(Color.LIGHT_GRAY);
			
			restrictionButton.addMouseListener(new MouseAdapter() {
								
				@Override
				public void mousePressed(MouseEvent arg0) {
					// TODO Auto-generated method stub
					int dialogResult = JOptionPane.showConfirmDialog (null, "�Desea borrarlo?","Warning",JOptionPane.YES_NO_OPTION);
					
					if(dialogResult == JOptionPane.YES_OPTION) {
						
						restrictions.remove(restrictions.indexOf(restrictionButton.getText()));
						addRestrictions();
					}
				}
			});
			
			restrictionButton.setVisible(true);
			restrictionButton.validate();			
			panelSelectedRestrictions.add(restrictionButton, BorderLayout.EAST);
			panelSelectedRestrictions.setVisible(true);
			
			panelContainerRestrictions.repaint();
			panelContainerRestrictions.validate();
			
		}	
		
		panelContainerRestrictions.add(panelSelectedRestrictions, BorderLayout.WEST);
		panelContainerRestrictions.repaint();
		panelContainerRestrictions.validate();
		
	}
	
	/**
	 * A�ade al array de restricciones y al panel de restricciones
	 * el elemento seleccionado en el �rbol.
	 * @param concept
	 */
	public static void getSelectedConcept(String concept) {
		
		int dialogResult = JOptionPane.showConfirmDialog (null, "�Desea a�adirlo?","Warning",JOptionPane.YES_NO_OPTION);
	
		if(dialogResult == JOptionPane.YES_OPTION) {
			
			concept = StringEvaluator.getLastWord(treeRestrictions.getSelectedConcept(),", ");
			restrictions.add(concept);
			addRestrictions();
			
			System.out.println( concept +  " a�adido");			
			
		}else {			
			
			System.out.println( concept +  " no a�adido");
			
		}
		
	}
}
