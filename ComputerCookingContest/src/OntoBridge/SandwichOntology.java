package OntoBridge;

import java.util.ArrayList;

import javax.swing.JFrame;

import jcolibri.util.FileIO;
import es.ucm.fdi.gaia.ontobridge.OntoBridge;
import es.ucm.fdi.gaia.ontobridge.OntologyDocument;
import es.ucm.fdi.gaia.ontobridge.test.gui.PnlConceptsTree;

public class SandwichOntology {
	
	private static String FICHERO_OWL = "OntoBridge/SandwichOntology.owl";
	private static PnlConceptsTree tree = null;
	private static JFrame window = null;
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public SandwichOntology()
	{
		
		OntoBridge ob = new OntoBridge();
		ob.initWithPelletReasoner();
		
		OntologyDocument mainOnto = new OntologyDocument(FileIO.findFile(FICHERO_OWL).toExternalForm());
		ArrayList<OntologyDocument> subOntologies = new ArrayList<OntologyDocument>();
		
		ob.loadOntology(mainOnto, subOntologies, true);
		tree = new PnlConceptsTree(ob);		
		
		window = new javax.swing.JFrame("");
		window.getContentPane().add(tree);
		window.pack();
		window.setSize(300, 600);
	}
	
	public PnlConceptsTree getOntologyTree()
	{		
		return tree;		
	}
	
	public JFrame getOntologyTreeWindow()
	{		
		return window;		
	}
	
	public static void setVisibility(boolean visible)
	{
		window.setVisible(visible);
	}
	
//	public static void main(String args[]) 
//	{		
//		new SandwichOntology();		
//		javax.swing.JFrame window = new javax.swing.JFrame("");
//		window.getContentPane().add(tree);
//		window.pack();
//		window.setSize(300, 600);
//		window.setVisible(true);	
//	}
}
