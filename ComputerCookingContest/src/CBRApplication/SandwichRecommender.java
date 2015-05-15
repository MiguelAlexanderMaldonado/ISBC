package CBRApplication;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.apache.commons.logging.LogFactory;

import OntoBridge.OntologyUsefulFunctions;
import OntoBridge.SandwichOntology;
import OntoBridge.OntologySimilarityFuntion;
import UI.UIRecommender;
import Utils.StringEvaluator;

import jcolibri.casebase.LinealCaseBase;
import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.cbrcore.Connector;
import jcolibri.connector.DataBaseConnector;
import jcolibri.exception.InitializingException;
import jcolibri.method.retrieve.RetrievalResult;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import jcolibri.method.retrieve.selection.SelectCases;
import jcolibri.util.FileIO;

public class SandwichRecommender {

	private Connector _connector;
	private CBRCaseBase _caseBase;	
	private static UIRecommender _frame;
	private static SandwichOntology _sandwichOntology;
	private static SandwichRecommender srApp = null;
	private ArrayList<ArrayList<String>> finalSandwichs = new ArrayList<ArrayList<String>>();
    public static int k = 5;	//Número de casos que se devuelven como resultado

	
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
	
	/**
	 * PreCycle
	 * @return
	 * @throws ExecutionException
	 * @throws InitializingException
	 */
	public CBRCaseBase preCycle() throws ExecutionException, InitializingException{
		// TODO Auto-generated method stub
		//Carga los casos desde el conector a la base de casos
		_caseBase.init(_connector);
		
		java.util.Collection<CBRCase> cases = _caseBase.getCases();
		for(CBRCase c: cases)
			System.out.print(c);
		
		return _caseBase;
	}
	
	/**
	 * PostCycle
	 * @throws ExecutionException
	 */
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
								// Se limpia el registro de casos
								_cases.clear();
								
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
						
						//Crea una instancia del árbol (ontología)
						_sandwichOntology = new SandwichOntology();	
						
						_frame.configuredSandwichOntology(_sandwichOntology);
						
						_frame.setVisible(true);
						
					
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			});
		}catch(Exception e){
			LogFactory.getLog(SandwichRecommender.class).error(e);
		}
		
	}
	
	/**
	 * Cycle
	 * Método que ejecuta el ciclo de la aplicación CBR.
	 * @throws ExecutionException
	 */
	private static void executeCycle() throws ExecutionException {
				
		if(_frame.getChckbxRestrictionsListMode().isSelected())
			srApp.cycle(recordQuery());
		else if(_frame.getChckbxRestrictionsTreeMode().isSelected())
			srApp.cycleWithOntology(recordQueryOntology());
	}
	
	//-------------- Métodos para la ejecución del ciclo empleando la ontología ------------
	
	/**
	 * Construye la query con las restricciones del modo = Árbol (Ontología)
	 * @return
	 */
	private static CBRQuery recordQueryOntology() {
		
		CBRQuery query = new CBRQuery();
		JointSandwichDescription sd = new JointSandwichDescription();			
		
		sd.setIngredients(_sandwichOntology.getPositiveRestrictions());
			
		query.setDescription(sd);
		
		return query;	
	}
	
	/**
	 * Cambia el formato de la descripción de los casos para que estos puedan ser evaluados
	 * mediante el uso de la ontología.
	 * (BBDD) SandwichDescription -> JointSandwichDescription
	 * @param caseBase
	 * @return
	 */
	private Collection<CBRCase> getFormatedCases(Collection<CBRCase> caseBase) {
		
		Collection<CBRCase> auxCaseBase = caseBase;
		
		Iterator<CBRCase> iterator = caseBase.iterator();

		System.out.println("Nuevo formato de la descripción de los casos: Lista de ingredientes");
		
		iterator = auxCaseBase.iterator();
		
		while(iterator.hasNext()) {
						
			CBRCase _case = iterator.next();
			
			if(! (_case.getDescription() instanceof JointSandwichDescription)) {
			
				JointSandwichDescription _caseComponent = new JointSandwichDescription();
				_caseComponent.setIngredients(StringEvaluator.getWords(_case.getDescription().toString(), ";"));
				_case.setDescription(_caseComponent);			
			}
			
			System.out.println(((JointSandwichDescription)_case.getDescription()).getIngredients().toString());
			
		}
		return auxCaseBase;
	}
	
	/**
	 * Filtra los sándwiches con las restricciones negativas.
	 * @param sandwichs
	 */
	private String negativeRestrictionsFilter(String sandwich) {
		
		ArrayList<String> negativeRestrictions = _sandwichOntology.getNegativeRestrictions();
		String _sandwich = sandwich;	
		
		int index = 0;
		
		while(index < negativeRestrictions.size()) {
						
			//Si la restricción no es una hoja 
			if(!OntologyUsefulFunctions.isLastChild(negativeRestrictions.get(index))) { 
				
				//Ingredientes hoja de la restricción
				ArrayList<String> childsNegativeRestrictions = OntologyUsefulFunctions.getLastChilds(negativeRestrictions.get(index));
				//Hermanos de la restricción
				ArrayList<String> brothers = OntologyUsefulFunctions.getBrothers(negativeRestrictions.get(index));
				
				int m = 0;
				
				while(m<negativeRestrictions.size() && brothers.contains(negativeRestrictions.get(m))){
					
					brothers.remove(brothers.indexOf(negativeRestrictions.get(m)));
					m++;
					
				}
				
				//Compruebo que no se haya filtrado ya por un hermano de la actual restricción
				for(int p=0; p<negativeRestrictions.size(); p++) {
					
					if(brothers.contains(negativeRestrictions.get(p)))
						brothers.remove(brothers.indexOf(negativeRestrictions.get(p)));
					
				}					
				
				//Hojas de los hermanos de la restricción
				ArrayList<String> childsOfBrothers = new ArrayList<String>();
				
				for(int j=0; j<brothers.size(); j++) {
					
					childsOfBrothers.addAll(OntologyUsefulFunctions.getLastChilds(brothers.get(j)));
					
				}
				
				Random r = new Random();								
				int k = 0;
				
				//Se reemplaza el ingrediente del sandwich que cumple la restricción
				//con un ingrediente hoja de los hermanos de la restricción
				while(k < childsNegativeRestrictions.size()){
					
					if(sandwich.contains(childsNegativeRestrictions.get(k))) {
						
						_sandwich = _sandwich.replace(childsNegativeRestrictions.get(k), childsOfBrothers.get(r.nextInt(childsOfBrothers.size()))); 
					
					}	
					k++;
				}
				
			} else {
				
				//Ingredientes hoja de la restricción
				ArrayList<String> childsNegativeRestrictions = OntologyUsefulFunctions.getLastChilds(OntologyUsefulFunctions.getSuperClass(negativeRestrictions.get(index)));
				childsNegativeRestrictions.remove(negativeRestrictions.get(index));
				
				//Compruebo que no se haya filtrado ya por un hermano de la actual restricción
				for(int p=0; p<negativeRestrictions.size(); p++) {
					
					if(childsNegativeRestrictions.contains(negativeRestrictions.get(p)))
						childsNegativeRestrictions.remove(childsNegativeRestrictions.indexOf(negativeRestrictions.get(p)));
					
				}	
				
				Random r = new Random();	
				int k = 0;
				//Se reemplaza el ingrediente del sandwich que cumple la restricción
				//con un ingrediente hoja de los hermanos de la restricción
				while(k < negativeRestrictions.size()){
					if(sandwich.contains(negativeRestrictions.get(k))) {
													
						_sandwich = _sandwich.replace(negativeRestrictions.get(k), childsNegativeRestrictions.get(r.nextInt(childsNegativeRestrictions.size()))); 

					}
					k++;
				}
				
			}
			index++;
		}		
		
		return _sandwich;
		
	}
	
	/***
	 * Cycle adecuado para el empleo de la ontología.
	 * @param query
	 * @throws ExecutionException
	 */
	public void cycleWithOntology(CBRQuery query) throws ExecutionException
	{
        // Primero configuramos el KNN Retrieval Method
        NNConfig simConfig = new NNConfig();

        // Toma la media como funcion global de similitud del caso
        simConfig.setDescriptionSimFunction(new Average());
        
		simConfig.addMapping(new Attribute("ingredients", JointSandwichDescription.class), new OntologySimilarityFuntion());        
        
		//Casos
		Collection<CBRCase> cases = _caseBase.getCases();
		
		// Ejecutamos NN
        Collection <RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(getFormatedCases(cases),query,simConfig);

        // Se seleccionan los k casos de la recuperación
        eval = SelectCases.selectTopKRR(eval, k);      
        
        //Se limpia el array de resultados
        finalSandwichs.clear();
        	
        //Filtrado por restricciones negativas
    	if(_sandwichOntology.getNegativeRestrictions().size() > 0 && _cases.size() > 0) {
    		
    		ArrayList<String> _casesAux = new ArrayList<String>();
    		_casesAux.addAll(_cases);
    		_cases.clear();
    		
    		for(int i =0; i<_casesAux.size(); i++){
    		
				_cases.add(negativeRestrictionsFilter(_casesAux.get(i)));
    		
    		}
    		
    	}   
    	
    	_frame.getTextArea().append("_____________________________________________" + "\n");
   	 
    	if(_cases.size() > 0)
	    	for(int i=0; i<k; i++) {   			
				_frame.getTextArea().append(_cases.get(i).toString() + " ");
//	    		_frame.getTextArea().append(evals.get(i).toString());
	    		_frame.getTextArea().append("\n");
	    		_frame.getTextArea().append("_____________________________________________" + "\n"); 
	    	}  	                     
    	
	}
	
	private static ArrayList<String> _cases = new ArrayList<String>();
	
	public static void recordCase(String _case){
		
		_cases.add(_case);
		
	}
	
	public static int getNumOfRecordCase(){
		
		return _cases.size();
		
	}
	
	//-------------------------------------------------------------------------------------------
	//-------------- Métodos para la ejecución del ciclo sin la ontología -----------------------
	
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
		
		_frame.getTextArea().append("____________________________________" + "\n"); 
		
		for(CBRCase _case : cases) {				
				//System.out.println(_case);
			SandwichDescription description = new SandwichDescription();
						
			if(_case.getDescription() instanceof JointSandwichDescription){
				
				JointSandwichDescription _description = (JointSandwichDescription)_case.getDescription();
				ArrayList<String> ingredients = _description.getIngredients();
				
				_frame.getTextArea().append(ingredients.get(0) + " " + ingredients.get(1) + " " +
											ingredients.get(2) + " " + ingredients.get(3) + " " +
											ingredients.get(4) + "\n");
				_frame.getTextArea().append("____________________________________" + "\n"); 
			} else {
			
				_frame.getTextArea().append(_case.getDescription().toString() + "\n");
				_frame.getTextArea().append("____________________________________" + "\n"); 
				
			}
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
				
				SandwichDescription description = new SandwichDescription();
				
				if(currentCase.getDescription() instanceof JointSandwichDescription){
					
					JointSandwichDescription _description = (JointSandwichDescription)currentCase.getDescription();
					ArrayList<String> ingredients = _description.getIngredients();
					
					description.setIngredient1(ingredients.get(0));
					description.setIngredient2(ingredients.get(1));	
					description.setIngredient3(ingredients.get(2));
					description.setIngredient4(ingredients.get(3));
					description.setIngredient5(ingredients.get(4));
					
				} else {
					
					description = (SandwichDescription)currentCase.getDescription();
					
				}
				
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
