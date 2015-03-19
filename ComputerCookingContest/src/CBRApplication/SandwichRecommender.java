package CBRApplication;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import org.omg.stub.java.rmi._Remote_Stub;

import OntoBridge.SandwichOntology;
import UI.UIRecommender;

import jcolibri.casebase.LinealCaseBase;
import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.Connector;
import jcolibri.connector.DataBaseConnector;
import jcolibri.exception.InitializingException;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import jcolibri.util.FileIO;

public class SandwichRecommender {

	private Connector _connector;
	private CBRCaseBase _caseBase;	
	private static UIRecommender _frame;
	private static SandwichRecommender srApp = null;
	
	public void configure() throws ExecutionException{
		try{
			//Crear conector con la base de datos
			_connector = new DataBaseConnector();
			//Inicializar el conector con su archivo xml de configuración
			
			_connector.initFromXMLfile(FileIO.findFile("src/DBConfig/databaseconfig.xml"));
			//La organización en memoria será lineal
			_caseBase = new LinealCaseBase();	
			
		}catch (Exception e){
			throw new ExecutionException(e);
		}
	}
	
	public CBRCaseBase preCycle() throws ExecutionException, InitializingException{
		// TODO Auto-generated method stub
		//Carga los casos desde el conector a la base de casos
		_caseBase.init(_connector);
		
		java.util.Collection<CBRCase> cases = _caseBase.getCases();
		for(CBRCase c: cases)
			System.out.print(c);
		
		return _caseBase;
	}
	
	public void postCycle() throws ExecutionException{
		this._caseBase.close();
	}
	
	public static void main(String[] args){
		
		//Lanzar el SGBD
		DBConnection.HSQLDBserver.init();
		
		//Crea el objeto que implementa la aplicación CBR
		//final SandwichRecommender srApp = new SandwichRecommender();
		srApp = new SandwichRecommender();
		
		try{
			//Configuración
			srApp.configure();
			//Preciclo
			srApp.preCycle();
			
			//Crear objeto que almacena la consulta
			final CBRQuery query = new CBRQuery();
			query.setDescription(new SandwichDescription());			
					
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						//Crea una instancia de la interfaz
						_frame = new UIRecommender();
						//Oyente para el botón 'Generar' de la interfaz
						_frame.getGeneratorButton().addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent arg0){
								// TODO Auto-generated method stub
								_frame.getTextArea().setText(null);
								
								if(_frame.getTextArea().getRows()==0)
									_frame.getTextArea().append("Results:\n");
								
								System.out.println("Ejecuta el ciclo");

								try {
									executeCycle();
								} catch (ExecutionException e) {
									// TODO Auto-generated catch block
									org.apache.commons.logging.LogFactory.getLog(SandwichRecommender.class).error(e);
								}
							}
						});			
						
						_frame.setVisible(true);
						
					
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			});
		}catch(Exception e){
			org.apache.commons.logging.LogFactory.getLog(SandwichRecommender.class).error(e);
		}
		
	}
	
	private static void executeCycle() throws ExecutionException {
		// TODO Auto-generated method stub
		//ObtainQueryWithFormMethod.obtainQueryWithoutInitialValues(query, null, null);
		srApp.cycle(recordQuery());
	}
	
	/**
	 * Devuelve todas las consultas que se pueden generar atendiendo al número y a los tipos de 
	 * ingredientes (posición en la jerarquía de alimentos) que el usuario ha seleccionado en la 
	 * interfaz.
	 * @return
	 **/
	private static ArrayList<CBRQuery>  recordQuery() {
		// TODO Auto-generated method stub
		ArrayList<CBRQuery> querys = new ArrayList<CBRQuery>();
		
		switch(_frame.getNumberOfIngredients())
		{
		case 1:
			querys = OneIngredientQueryGenerator();			
			break;
		
		case 2:
			querys = TwoIngredientsQueryGenerator();		
			break;
			
		case 3:
			querys = ThreeIngredientsQueryGenerator();
			break;
			
		case 4:
			querys = FourIngredientsQueryGenerator();
			break;
			
		case 5:
			querys = FiveIngredientsQueryGenerator();
			break;
		}
		
		return querys;
	}
	
	/**
	 * Cycle
	 * @param querys
	 * @throws ExecutionException
	 */
	public void cycle(ArrayList<CBRQuery> querys) throws ExecutionException
	{
		NNConfig simConfig = new NNConfig();
		
		simConfig.setDescriptionSimFunction(new Average());
		
		simConfig.addMapping(new Attribute("ingredient1",SandwichDescription.class), new Equal());
		simConfig.addMapping(new Attribute("ingredient2",SandwichDescription.class), new Equal());
		simConfig.addMapping(new Attribute("ingredient3",SandwichDescription.class), new Equal());
		simConfig.addMapping(new Attribute("ingredient4",SandwichDescription.class), new Equal());
		simConfig.addMapping(new Attribute("ingredient5",SandwichDescription.class), new Equal());
					
		ArrayList<CBRCase> cases =	CasesFilter(querys,_caseBase.getCases());
		
		for(CBRCase _case : cases){
				
				System.out.println(_case);				
				_frame.getTextArea().append(_case.getDescription().toString() + "\n");
		}
		
		System.out.println("Número de querys: " + Integer.toString(querys.size()));
	}

	/**
	 * Filtra los casos devolviendo sólo aquellos que concuerdan con las consultas.
	 * @param querys
	 * @param cases
	 * @return
	 */
	private ArrayList<CBRCase> CasesFilter(ArrayList<CBRQuery> querys, Collection<CBRCase> cases) {
		
		ArrayList<CBRCase> casesFiltered = new ArrayList<CBRCase>();
		
		for(int i=0; i<querys.size(); i ++){			
		
			SandwichDescription query = (SandwichDescription)querys.get(i).getDescription();
			String[] queryIngredients = query.toString().split("(\\(null;)|(null;)|(null\\))|\\)");
			ArrayList<String> _queryIngredients = new ArrayList<String>();
			
			for(int l=0; l<queryIngredients.length; l++){
				if(!queryIngredients[l].isEmpty()){
					_queryIngredients.add(queryIngredients[l]);
				}
			}		
			
			Iterator<CBRCase> it = cases.iterator();
			
			while(it.hasNext()){
				
				CBRCase currentCase = it.next();
				
				SandwichDescription description = (SandwichDescription)currentCase.getDescription();
				String[] ingredients = description.toString().split("\\(|(;)|\\)");
				
				ArrayList<String> caseIngredients = new ArrayList<String>();
				
				for(int a=2; a<ingredients.length; a++){
					caseIngredients.add(ingredients[a]);
				}
				
				queryIngredients =  _queryIngredients.get(0).split(";");
				
				int numOfIngredients=0;
				
				for(int b=0; b<queryIngredients.length; b++){
					//Comprueba si los ingredientes del caso (extraído de la BBDD) concuerdan con los 
					//de la consulta (user query)
					if(caseIngredients.indexOf(queryIngredients[b])!=-1){
						caseIngredients.set(caseIngredients.indexOf(queryIngredients[b]), "null");
						numOfIngredients++;
					}
				}
				
				if(numOfIngredients==queryIngredients.length && !casesFiltered.contains(currentCase))
					casesFiltered.add(currentCase);				
			}		
		}
		
		return casesFiltered;
	}
	
	/**
	 * Devulve la consulta del primer ingrediente, el cual es fijo (Pan)
	 * @return
	 */
	private static ArrayList<CBRQuery> OneIngredientQueryGenerator(){
		
		ArrayList<CBRQuery> querys = new ArrayList<CBRQuery>();
		CBRQuery query = new CBRQuery();
		SandwichDescription sd = new SandwichDescription();			
		sd.setIngredient(1, _frame.getBreadIngredient().toString());
		query.setDescription(sd);
		querys.add(query);
		return querys;		
	}
	
	/**
	 * Devuelve las consultas que se pueden generar con dos ingredientes
	 * (Pan + ingrediente1), el número puede variar puesto que se puede seleccionar
	 * cualquier nivel de la jerarquía de alimentos. 
	 * @return
	 */
	private static ArrayList<CBRQuery> TwoIngredientsQueryGenerator(){
		
		ArrayList<CBRQuery> querys = new ArrayList<CBRQuery>();
		boolean isLastIngredient = _frame.isLastIngredientInHierarchy(1).isLast();
		int indexIngredient = _frame.isLastIngredientInHierarchy(1).getIndex() - 1;
		
		if(isLastIngredient){

			CBRQuery query = new CBRQuery();
			SandwichDescription sd = new SandwichDescription();
			sd.setIngredient(1, _frame.getBreadIngredient().toString());
			sd.setIngredient(2, _frame.getIngredientsHierarchy1().get(indexIngredient).toString());
			query.setDescription(sd);
			querys.add(query);
			
		}else{
			ArrayList<String> childIngredients = new ArrayList<String>();
			childIngredients = _frame.getLastIngredientsInHierarchy(_frame.getIngredientsHierarchy1().get(indexIngredient).toString());

			for(int i=0; i<childIngredients.size(); i++){

				CBRQuery query = new CBRQuery();
				SandwichDescription sd = new SandwichDescription();
				sd.setIngredient(1, _frame.getBreadIngredient().toString());
				sd.setIngredient(2, childIngredients.get(i));
				query.setDescription(sd);
				querys.add(query);				
			}
		}				
		return querys;
	}
	
	/**
	 * Devuelve las consultas que se pueden generar con tres ingredientes
	 * (Pan + ingrediente1 + ingrediente2), el número puede variar puesto 
	 * que se puede seleccionar cualquier nivel de la jerarquía de alimentos. 
	 * @return
	 */
	private static ArrayList<CBRQuery> ThreeIngredientsQueryGenerator(){
		
		ArrayList<CBRQuery> querys = new ArrayList<CBRQuery>();		
		boolean isLastIngredient1 = _frame.isLastIngredientInHierarchy(1).isLast();
		int indexIngredient1 = _frame.isLastIngredientInHierarchy(1).getIndex() - 1;			
		boolean isLastIngredient2 = _frame.isLastIngredientInHierarchy(2).isLast();
		int indexIngredient2 = _frame.isLastIngredientInHierarchy(2).getIndex() - 1;
		
		ArrayList<String[]> elements = new ArrayList<String[]>();
		
		if(isLastIngredient1 && isLastIngredient2){					
						
			String _elemets[] = {_frame.getIngredientsHierarchy1().get(indexIngredient1).toString(),
								_frame.getIngredientsHierarchy2().get(indexIngredient2).toString(),
								null,
								null};

			elements.add(_elemets);

		}else if(!isLastIngredient1 && isLastIngredient2){
	
			ArrayList<String> childIngredients = new ArrayList<String>();
			childIngredients = _frame.getLastIngredientsInHierarchy(_frame.getIngredientsHierarchy1().get(indexIngredient1).toString());
			
			for(int i=0; i<childIngredients.size(); i++){

				String _elemets[] = {childIngredients.get(i),
									_frame.getIngredientsHierarchy2().get(indexIngredient2).toString(),
									null,
									null};
				elements.add(_elemets);	
			}
			
		}else if(isLastIngredient1 && !isLastIngredient2){
			ArrayList<String> childIngredients = new ArrayList<String>();
			childIngredients = _frame.getLastIngredientsInHierarchy(_frame.getIngredientsHierarchy2().get(indexIngredient2).toString());
			
			for(int i=0; i<childIngredients.size(); i++){

				String _elemets[] = {_frame.getIngredientsHierarchy1().get(indexIngredient1).toString(),
									childIngredients.get(i),
									null,
									null};
				elements.add(_elemets);
			}
		}else {
			ArrayList<String> childIngredients1 = new ArrayList<String>();
			childIngredients1 = _frame.getLastIngredientsInHierarchy(_frame.getIngredientsHierarchy1().get(indexIngredient1).toString());
			
			ArrayList<String> childIngredients2 = new ArrayList<String>();
			childIngredients2 = _frame.getLastIngredientsInHierarchy(_frame.getIngredientsHierarchy2().get(indexIngredient2).toString());
			
			for(int i=0; i<childIngredients1.size(); i++){
				for(int j=0; j<childIngredients2.size(); j++){
					String _elemets[] = {childIngredients1.get(i),
										childIngredients2.get(j),
										null,
										null};
					elements.add(_elemets);
				}
			}
		}
		
		for(int i=0; i<elements.size(); i++){
			CBRQuery query = new CBRQuery();
			SandwichDescription sd = new SandwichDescription();
			sd.setIngredient(1, _frame.getBreadIngredient().toString());
			sd.setIngredient(2, elements.get(i)[0]);
			sd.setIngredient(3, elements.get(i)[1]);
			sd.setIngredient(4, elements.get(i)[2]);
			sd.setIngredient(5, elements.get(i)[3]);
			query.setDescription(sd);
			querys.add(query);
			System.out.println(query.toString());	
		}
		
		return querys;
	}

	/**
	 * Devuelve las consultas que se pueden generar con cuatro ingredientes
	 * (Pan + ingrediente1 + ingrediente2 + ingrediente3), el número puede variar puesto 
	 * que se puede seleccionar cualquier nivel de la jerarquía de alimentos. 
	 * @return
	 */
	private static ArrayList<CBRQuery> FourIngredientsQueryGenerator(){
		
		ArrayList<CBRQuery> querys = new ArrayList<CBRQuery>();		
		boolean isLastIngredient1 = _frame.isLastIngredientInHierarchy(1).isLast();
		int indexIngredient1 = _frame.isLastIngredientInHierarchy(1).getIndex() - 1;			
		boolean isLastIngredient2 = _frame.isLastIngredientInHierarchy(2).isLast();
		int indexIngredient2 = _frame.isLastIngredientInHierarchy(2).getIndex() - 1;
		boolean isLastIngredient3 = _frame.isLastIngredientInHierarchy(3).isLast();
		int indexIngredient3 = _frame.isLastIngredientInHierarchy(3).getIndex() - 1;
		
		ArrayList<String[]> elements = new ArrayList<String[]>();
		
		if(isLastIngredient1 && isLastIngredient2 && isLastIngredient3){					
					
			String _elemets[] = {_frame.getIngredientsHierarchy1().get(indexIngredient1).toString(),
								_frame.getIngredientsHierarchy2().get(indexIngredient2).toString(),
								_frame.getIngredientsHierarchy3().get(indexIngredient3).toString(),
								null};
			elements.add(_elemets);

		}else if(!isLastIngredient1 && isLastIngredient2 && isLastIngredient3){
	
			ArrayList<String> childIngredients = new ArrayList<String>();
			childIngredients = _frame.getLastIngredientsInHierarchy(_frame.getIngredientsHierarchy1().get(indexIngredient1).toString());
			
			for(int i=0; i<childIngredients.size(); i++){
				String _elemets[] = {childIngredients.get(i),
									_frame.getIngredientsHierarchy2().get(indexIngredient2).toString(),
									_frame.getIngredientsHierarchy3().get(indexIngredient3).toString(),
									null};
				elements.add(_elemets);
			}
			
		}else if(isLastIngredient1 && !isLastIngredient2 && isLastIngredient3){
	
			ArrayList<String> childIngredients = new ArrayList<String>();
			childIngredients = _frame.getLastIngredientsInHierarchy(_frame.getIngredientsHierarchy2().get(indexIngredient2).toString());
			
			for(int i=0; i<childIngredients.size(); i++){
				String _elemets[] = {_frame.getIngredientsHierarchy1().get(indexIngredient1).toString(),
									childIngredients.get(i),									
									_frame.getIngredientsHierarchy3().get(indexIngredient3).toString(),
									null};
				elements.add(_elemets);
			}
			
		}else if(isLastIngredient1 && isLastIngredient2 && !isLastIngredient3){
	
			ArrayList<String> childIngredients = new ArrayList<String>();
			childIngredients = _frame.getLastIngredientsInHierarchy(_frame.getIngredientsHierarchy3().get(indexIngredient3).toString());
			
			for(int i=0; i<childIngredients.size(); i++){
				String _elemets[] = {_frame.getIngredientsHierarchy1().get(indexIngredient1).toString(),									
									_frame.getIngredientsHierarchy2().get(indexIngredient2).toString(),
									childIngredients.get(i),
									null};
				elements.add(_elemets);
			}
			
		}else if(!isLastIngredient1 && !isLastIngredient2 && isLastIngredient3){
	
			ArrayList<String> childIngredients1 = new ArrayList<String>();
			childIngredients1 = _frame.getLastIngredientsInHierarchy(_frame.getIngredientsHierarchy1().get(indexIngredient1).toString());
			
			ArrayList<String> childIngredients2 = new ArrayList<String>();
			childIngredients2 = _frame.getLastIngredientsInHierarchy(_frame.getIngredientsHierarchy2().get(indexIngredient2).toString());
			
			for(int i=0; i<childIngredients1.size(); i++){
				for(int j=0; j<childIngredients2.size(); j++){
					String _elemets[] = {childIngredients1.get(i),
										 childIngredients2.get(j),									
										_frame.getIngredientsHierarchy3().get(indexIngredient3).toString(),
										null};
					elements.add(_elemets);
				}
			}
			
		}else if(!isLastIngredient1 && isLastIngredient2 && !isLastIngredient3){
	
			ArrayList<String> childIngredients1 = new ArrayList<String>();
			childIngredients1 = _frame.getLastIngredientsInHierarchy(_frame.getIngredientsHierarchy1().get(indexIngredient1).toString());
			
			ArrayList<String> childIngredients3 = new ArrayList<String>();
			childIngredients3 = _frame.getLastIngredientsInHierarchy(_frame.getIngredientsHierarchy3().get(indexIngredient3).toString());
			
			for(int i=0; i<childIngredients1.size(); i++){
				for(int j=0; j<childIngredients3.size(); j++){
					String _elemets[] = {childIngredients1.get(i),										 									
										_frame.getIngredientsHierarchy2().get(indexIngredient2).toString(),
										childIngredients3.get(j),
										null};
					elements.add(_elemets);
				}
			}
			
		}else if(isLastIngredient1 && !isLastIngredient2 && !isLastIngredient3){
	
			ArrayList<String> childIngredients2 = new ArrayList<String>();
			childIngredients2 = _frame.getLastIngredientsInHierarchy(_frame.getIngredientsHierarchy2().get(indexIngredient2).toString());
			
			ArrayList<String> childIngredients3 = new ArrayList<String>();
			childIngredients3 = _frame.getLastIngredientsInHierarchy(_frame.getIngredientsHierarchy3().get(indexIngredient3).toString());

			for(int i=0; i<childIngredients2.size(); i++){
				for(int j=0; j<childIngredients3.size(); j++){
					String _elemets[] = {_frame.getIngredientsHierarchy1().get(indexIngredient1).toString(),
										childIngredients2.get(i),
										childIngredients3.get(j),
										null};
					elements.add(_elemets);
				}
			}
			
		}else {
			ArrayList<String> childIngredients1 = new ArrayList<String>();
			childIngredients1 = _frame.getLastIngredientsInHierarchy(_frame.getIngredientsHierarchy1().get(indexIngredient1).toString());
			
			ArrayList<String> childIngredients2 = new ArrayList<String>();
			childIngredients2 = _frame.getLastIngredientsInHierarchy(_frame.getIngredientsHierarchy2().get(indexIngredient2).toString());
			
			ArrayList<String> childIngredients3 = new ArrayList<String>();
			childIngredients3 = _frame.getLastIngredientsInHierarchy(_frame.getIngredientsHierarchy3().get(indexIngredient3).toString());
			
			for(int i=0; i<childIngredients1.size(); i++){
				for(int j=0; j<childIngredients2.size(); j++){
					for(int k=0; k<childIngredients3.size(); k++){
						String _elemets[] = {childIngredients1.get(i),
											childIngredients2.get(j),
											childIngredients3.get(k),
											null};
						elements.add(_elemets);
					}
				}
			}
		}
		
		for(int i=0; i<elements.size(); i++){
			CBRQuery query = new CBRQuery();
			SandwichDescription sd = new SandwichDescription();
			sd.setIngredient(1, _frame.getBreadIngredient().toString());
			sd.setIngredient(2, elements.get(i)[0]);
			sd.setIngredient(3, elements.get(i)[1]);
			sd.setIngredient(4, elements.get(i)[2]);
			sd.setIngredient(5, elements.get(i)[3]);
			query.setDescription(sd);
			querys.add(query);
			System.out.println(query.toString());	
		}
		
		return querys;
	}
	
	/**
	 * Devuelve las consultas que se pueden generar con cinco ingredientes
	 * (Pan + ingrediente1 + ingrediente2 + ingrediente3 + ingrediente4), 
	 * el número puede variar puesto que se puede seleccionar cualquier 
	 * nivel de la jerarquía de alimentos. 
	 * @return
	 */
	private static ArrayList<CBRQuery> FiveIngredientsQueryGenerator(){
		
		ArrayList<CBRQuery> querys = new ArrayList<CBRQuery>();		
		boolean isLastIngredient1 = _frame.isLastIngredientInHierarchy(1).isLast();
		int indexIngredient1 = _frame.isLastIngredientInHierarchy(1).getIndex() - 1;			
		boolean isLastIngredient2 = _frame.isLastIngredientInHierarchy(2).isLast();
		int indexIngredient2 = _frame.isLastIngredientInHierarchy(2).getIndex() - 1;
		boolean isLastIngredient3 = _frame.isLastIngredientInHierarchy(3).isLast();
		int indexIngredient3 = _frame.isLastIngredientInHierarchy(3).getIndex() - 1;
		boolean isLastIngredient4 = _frame.isLastIngredientInHierarchy(4).isLast();
		int indexIngredient4 = _frame.isLastIngredientInHierarchy(4).getIndex() - 1;
		
		ArrayList<String[]> elements = new ArrayList<String[]>();
		
		if(isLastIngredient1 && isLastIngredient2 && isLastIngredient3 && isLastIngredient4){					
					
			String _elemets[] = {_frame.getIngredientsHierarchy1().get(indexIngredient1).toString(),
								_frame.getIngredientsHierarchy2().get(indexIngredient2).toString(),
								_frame.getIngredientsHierarchy3().get(indexIngredient3).toString(),
								_frame.getIngredientsHierarchy4().get(indexIngredient4).toString()};
			elements.add(_elemets);

		}else if(!isLastIngredient1 && isLastIngredient2 && isLastIngredient3 && isLastIngredient4){
	
			ArrayList<String> childIngredients = new ArrayList<String>();
			childIngredients = _frame.getLastIngredientsInHierarchy(_frame.getIngredientsHierarchy1().get(indexIngredient1).toString());
			
			for(int i=0; i<childIngredients.size(); i++){
				String _elemets[] = {childIngredients.get(i),
									_frame.getIngredientsHierarchy2().get(indexIngredient2).toString(),
									_frame.getIngredientsHierarchy3().get(indexIngredient3).toString(),
									_frame.getIngredientsHierarchy4().get(indexIngredient4).toString()};
				elements.add(_elemets);
			}
			
		}else if(isLastIngredient1 && !isLastIngredient2 && isLastIngredient3 && isLastIngredient4){
	
			ArrayList<String> childIngredients = new ArrayList<String>();
			childIngredients = _frame.getLastIngredientsInHierarchy(_frame.getIngredientsHierarchy2().get(indexIngredient2).toString());
			
			for(int i=0; i<childIngredients.size(); i++){
				String _elemets[] = {_frame.getIngredientsHierarchy1().get(indexIngredient1).toString(),
									childIngredients.get(i),									
									_frame.getIngredientsHierarchy3().get(indexIngredient3).toString(),
									_frame.getIngredientsHierarchy4().get(indexIngredient4).toString()};
				elements.add(_elemets);
			}
			
		}else if(isLastIngredient1 && isLastIngredient2 && !isLastIngredient3 && isLastIngredient4){
	
			ArrayList<String> childIngredients = new ArrayList<String>();
			childIngredients = _frame.getLastIngredientsInHierarchy(_frame.getIngredientsHierarchy3().get(indexIngredient3).toString());
			
			for(int i=0; i<childIngredients.size(); i++){
				String _elemets[] = {_frame.getIngredientsHierarchy1().get(indexIngredient1).toString(),									
									_frame.getIngredientsHierarchy2().get(indexIngredient2).toString(),
									childIngredients.get(i),
									_frame.getIngredientsHierarchy4().get(indexIngredient4).toString()};
				elements.add(_elemets);
			}
			
		}else if(isLastIngredient1 && isLastIngredient2 && isLastIngredient3 && !isLastIngredient4){
	
			ArrayList<String> childIngredients = new ArrayList<String>();
			childIngredients = _frame.getLastIngredientsInHierarchy(_frame.getIngredientsHierarchy4().get(indexIngredient4).toString());
			
			for(int i=0; i<childIngredients.size(); i++){
				String _elemets[] = {_frame.getIngredientsHierarchy1().get(indexIngredient1).toString(),									
									_frame.getIngredientsHierarchy2().get(indexIngredient2).toString(),
									_frame.getIngredientsHierarchy3().get(indexIngredient3).toString(),
									childIngredients.get(i)};
				elements.add(_elemets);
			}
			
		}else if(!isLastIngredient1 && !isLastIngredient2 && isLastIngredient3 && isLastIngredient4){
	
			ArrayList<String> childIngredients1 = new ArrayList<String>();
			childIngredients1 = _frame.getLastIngredientsInHierarchy(_frame.getIngredientsHierarchy1().get(indexIngredient1).toString());
			
			ArrayList<String> childIngredients2 = new ArrayList<String>();
			childIngredients2 = _frame.getLastIngredientsInHierarchy(_frame.getIngredientsHierarchy2().get(indexIngredient2).toString());
			
			for(int i=0; i<childIngredients1.size(); i++){
				for(int j=0; j<childIngredients2.size(); j++){
					String _elemets[] = {childIngredients1.get(i),
										 childIngredients2.get(j),									
										_frame.getIngredientsHierarchy3().get(indexIngredient3).toString(),
										_frame.getIngredientsHierarchy4().get(indexIngredient4).toString()};
					elements.add(_elemets);
				}
			}
			
		}else if(!isLastIngredient1 && isLastIngredient2 && !isLastIngredient3 && isLastIngredient4){
	
			ArrayList<String> childIngredients1 = new ArrayList<String>();
			childIngredients1 = _frame.getLastIngredientsInHierarchy(_frame.getIngredientsHierarchy1().get(indexIngredient1).toString());
			
			ArrayList<String> childIngredients3 = new ArrayList<String>();
			childIngredients3 = _frame.getLastIngredientsInHierarchy(_frame.getIngredientsHierarchy3().get(indexIngredient3).toString());
			
			for(int i=0; i<childIngredients1.size(); i++){
				for(int j=0; j<childIngredients3.size(); j++){
					String _elemets[] = {childIngredients1.get(i),										 									
										_frame.getIngredientsHierarchy2().get(indexIngredient2).toString(),
										childIngredients3.get(j),
										_frame.getIngredientsHierarchy4().get(indexIngredient4).toString()};
					elements.add(_elemets);
				}
			}
			
		}else if(isLastIngredient1 && !isLastIngredient2 && !isLastIngredient3 && isLastIngredient4){
	
			ArrayList<String> childIngredients2 = new ArrayList<String>();
			childIngredients2 = _frame.getLastIngredientsInHierarchy(_frame.getIngredientsHierarchy2().get(indexIngredient2).toString());
			
			ArrayList<String> childIngredients3 = new ArrayList<String>();
			childIngredients3 = _frame.getLastIngredientsInHierarchy(_frame.getIngredientsHierarchy3().get(indexIngredient3).toString());

			for(int i=0; i<childIngredients2.size(); i++){
				for(int j=0; j<childIngredients3.size(); j++){
					String _elemets[] = {_frame.getIngredientsHierarchy1().get(indexIngredient1).toString(),
										childIngredients2.get(i),
										childIngredients3.get(j),
										_frame.getIngredientsHierarchy4().get(indexIngredient4).toString()};
					elements.add(_elemets);
				}
			}
			
		}else if(!isLastIngredient1 && isLastIngredient2 && isLastIngredient3 && !isLastIngredient4){
	
			ArrayList<String> childIngredients1 = new ArrayList<String>();
			childIngredients1 = _frame.getLastIngredientsInHierarchy(_frame.getIngredientsHierarchy1().get(indexIngredient1).toString());
			
			ArrayList<String> childIngredients4 = new ArrayList<String>();
			childIngredients4 = _frame.getLastIngredientsInHierarchy(_frame.getIngredientsHierarchy4().get(indexIngredient4).toString());

			for(int i=0; i<childIngredients1.size(); i++){
				for(int j=0; j<childIngredients4.size(); j++){
					String _elemets[] = {childIngredients1.get(i),
										_frame.getIngredientsHierarchy2().get(indexIngredient2).toString(),
										_frame.getIngredientsHierarchy3().get(indexIngredient3).toString(),
										childIngredients4.get(j)};
					elements.add(_elemets);
				}
			}
			
		}else if(isLastIngredient1 && !isLastIngredient2 && isLastIngredient3 && !isLastIngredient4){
	
			ArrayList<String> childIngredients2 = new ArrayList<String>();
			childIngredients2 = _frame.getLastIngredientsInHierarchy(_frame.getIngredientsHierarchy2().get(indexIngredient2).toString());
			
			ArrayList<String> childIngredients4 = new ArrayList<String>();
			childIngredients4 = _frame.getLastIngredientsInHierarchy(_frame.getIngredientsHierarchy4().get(indexIngredient4).toString());

			for(int i=0; i<childIngredients2.size(); i++){
				for(int j=0; j<childIngredients4.size(); j++){
					String _elemets[] = {_frame.getIngredientsHierarchy1().get(indexIngredient1).toString(),
										childIngredients2.get(i),
										_frame.getIngredientsHierarchy3().get(indexIngredient3).toString(),
										childIngredients4.get(j)};
					elements.add(_elemets);
				}
			}
			
		}else if(isLastIngredient1 && isLastIngredient2 && !isLastIngredient3 && !isLastIngredient4){
	
			ArrayList<String> childIngredients3 = new ArrayList<String>();
			childIngredients3 = _frame.getLastIngredientsInHierarchy(_frame.getIngredientsHierarchy3().get(indexIngredient3).toString());
			
			ArrayList<String> childIngredients4 = new ArrayList<String>();
			childIngredients4 = _frame.getLastIngredientsInHierarchy(_frame.getIngredientsHierarchy4().get(indexIngredient4).toString());

			for(int i=0; i<childIngredients3.size(); i++){
				for(int j=0; j<childIngredients4.size(); j++){
					String _elemets[] = {_frame.getIngredientsHierarchy1().get(indexIngredient1).toString(),
										_frame.getIngredientsHierarchy2().get(indexIngredient2).toString(),
										childIngredients3.get(i),
										childIngredients4.get(j)};
					elements.add(_elemets);
				}
			}
			
		}else if(isLastIngredient1 && !isLastIngredient2 && !isLastIngredient3 && !isLastIngredient4){
	
			ArrayList<String> childIngredients2 = new ArrayList<String>();
			childIngredients2 = _frame.getLastIngredientsInHierarchy(_frame.getIngredientsHierarchy2().get(indexIngredient2).toString());
			
			ArrayList<String> childIngredients3 = new ArrayList<String>();
			childIngredients3 = _frame.getLastIngredientsInHierarchy(_frame.getIngredientsHierarchy3().get(indexIngredient3).toString());
			
			ArrayList<String> childIngredients4 = new ArrayList<String>();
			childIngredients4 = _frame.getLastIngredientsInHierarchy(_frame.getIngredientsHierarchy4().get(indexIngredient4).toString());

			for(int i=0; i<childIngredients2.size(); i++){
				for(int j=0; j<childIngredients3.size(); j++){
					for(int k=0; k<childIngredients4.size(); k++){
						String _elemets[] = {_frame.getIngredientsHierarchy1().get(indexIngredient1).toString(),
											childIngredients3.get(i),
											childIngredients3.get(j),
											childIngredients4.get(k)};
						elements.add(_elemets);
					}
				}
			}
			
		}else if(!isLastIngredient1 && isLastIngredient2 && !isLastIngredient3 && !isLastIngredient4){
	
			ArrayList<String> childIngredients1 = new ArrayList<String>();
			childIngredients1 = _frame.getLastIngredientsInHierarchy(_frame.getIngredientsHierarchy1().get(indexIngredient1).toString());
			
			ArrayList<String> childIngredients3 = new ArrayList<String>();
			childIngredients3 = _frame.getLastIngredientsInHierarchy(_frame.getIngredientsHierarchy3().get(indexIngredient3).toString());
			
			ArrayList<String> childIngredients4 = new ArrayList<String>();
			childIngredients4 = _frame.getLastIngredientsInHierarchy(_frame.getIngredientsHierarchy4().get(indexIngredient4).toString());

			for(int i=0; i<childIngredients1.size(); i++){
				for(int j=0; j<childIngredients3.size(); j++){
					for(int k=0; k<childIngredients4.size(); k++){
						String _elemets[] = {childIngredients1.get(i),
											_frame.getIngredientsHierarchy2().get(indexIngredient2).toString(),											
											childIngredients3.get(j),
											childIngredients4.get(k)};
						elements.add(_elemets);
					}
				}
			}
			
		}else if(!isLastIngredient1 && !isLastIngredient2 && isLastIngredient3 && !isLastIngredient4){
	
			ArrayList<String> childIngredients1 = new ArrayList<String>();
			childIngredients1 = _frame.getLastIngredientsInHierarchy(_frame.getIngredientsHierarchy1().get(indexIngredient1).toString());
			
			ArrayList<String> childIngredients2 = new ArrayList<String>();
			childIngredients2 = _frame.getLastIngredientsInHierarchy(_frame.getIngredientsHierarchy2().get(indexIngredient2).toString());
			
			ArrayList<String> childIngredients4 = new ArrayList<String>();
			childIngredients4 = _frame.getLastIngredientsInHierarchy(_frame.getIngredientsHierarchy4().get(indexIngredient4).toString());

			for(int i=0; i<childIngredients1.size(); i++){
				for(int j=0; j<childIngredients2.size(); j++){
					for(int k=0; k<childIngredients4.size(); k++){
						String _elemets[] = {childIngredients1.get(i),
											childIngredients2.get(j),
											_frame.getIngredientsHierarchy3().get(indexIngredient3).toString(),																						
											childIngredients4.get(k)};
						elements.add(_elemets);
					}
				}
			}
			
		}else if(!isLastIngredient1 && !isLastIngredient2 && !isLastIngredient3 && isLastIngredient4){
	
			ArrayList<String> childIngredients1 = new ArrayList<String>();
			childIngredients1 = _frame.getLastIngredientsInHierarchy(_frame.getIngredientsHierarchy1().get(indexIngredient1).toString());
			
			ArrayList<String> childIngredients2 = new ArrayList<String>();
			childIngredients2 = _frame.getLastIngredientsInHierarchy(_frame.getIngredientsHierarchy2().get(indexIngredient2).toString());
			
			ArrayList<String> childIngredients3 = new ArrayList<String>();
			childIngredients3 = _frame.getLastIngredientsInHierarchy(_frame.getIngredientsHierarchy3().get(indexIngredient3).toString());

			for(int i=0; i<childIngredients1.size(); i++){
				for(int j=0; j<childIngredients2.size(); j++){
					for(int k=0; k<childIngredients3.size(); k++){
						String _elemets[] = {childIngredients1.get(i),
											childIngredients2.get(j),
											childIngredients3.get(k),
											_frame.getIngredientsHierarchy4().get(indexIngredient4).toString()};
						elements.add(_elemets);
					}
				}
			}
			
		}else {
			ArrayList<String> childIngredients1 = new ArrayList<String>();
			childIngredients1 = _frame.getLastIngredientsInHierarchy(_frame.getIngredientsHierarchy1().get(indexIngredient1).toString());
			
			ArrayList<String> childIngredients2 = new ArrayList<String>();
			childIngredients2 = _frame.getLastIngredientsInHierarchy(_frame.getIngredientsHierarchy2().get(indexIngredient2).toString());
			
			ArrayList<String> childIngredients3 = new ArrayList<String>();
			childIngredients3 = _frame.getLastIngredientsInHierarchy(_frame.getIngredientsHierarchy3().get(indexIngredient3).toString());
			
			ArrayList<String> childIngredients4 = new ArrayList<String>();
			childIngredients4 = _frame.getLastIngredientsInHierarchy(_frame.getIngredientsHierarchy4().get(indexIngredient4).toString());
			
			for(int i=0; i<childIngredients1.size(); i++){
				for(int j=0; j<childIngredients2.size(); j++){
					for(int k=0; k<childIngredients3.size(); k++){
						for(int l=0; l<childIngredients4.size(); l++){
							String _elemets[] = {childIngredients1.get(i),
												childIngredients2.get(j),
												childIngredients3.get(k),
												childIngredients4.get(l)};
							elements.add(_elemets);
						}
					}
				}
			}
		}
		
		for(int i=0; i<elements.size(); i++){
			CBRQuery query = new CBRQuery();
			SandwichDescription sd = new SandwichDescription();
			sd.setIngredient(1, _frame.getBreadIngredient().toString());
			sd.setIngredient(2, elements.get(i)[0]);
			sd.setIngredient(3, elements.get(i)[1]);
			sd.setIngredient(4, elements.get(i)[2]);
			sd.setIngredient(5, elements.get(i)[3]);
			query.setDescription(sd);
			querys.add(query);
			System.out.println(query.toString());	
		}
		
		return querys;
	}
}
