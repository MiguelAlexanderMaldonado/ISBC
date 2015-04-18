package OntoBridge;

import java.util.ArrayList;
import java.util.Random;

import CBRApplication.SandwichRecommender;

import jcolibri.exception.NoApplicableSimilarityFunctionException;
import jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;

public class OntologySimilarityFuntion implements LocalSimilarityFunction {

	private double computo;
	
	@Override
	@SuppressWarnings("unchecked")
	public double compute(Object caseObject, Object queryObject) throws NoApplicableSimilarityFunctionException {
		        
		ArrayList<String> cases = (ArrayList<String>) caseObject;		
        ArrayList<String> restrictionsQuery = (ArrayList<String>)queryObject;
        
		String ingredientsCase = "";
        
        int sizeCase = cases.size();

        if (restrictionsQuery.size() >= 1) {
            //Se añaden todos los ingredientes de cada caso en forma de cadena para la comparación
            for(int i = 0; i < sizeCase; i++) 
            	ingredientsCase = ingredientsCase + " " + cases.get(i);
            
            // Buscamos los ingredientes en la descripcion del sandwich
            searchIngredients(ingredientsCase, restrictionsQuery);
            
        } else computo= 0;

        return computo;
        
	}
	
	/**
	 * Método de similitud
	 * @param ingredientsCase
	 * @param restrictionsQuery
	 */
	private void searchIngredients(String ingredientsCase, ArrayList<String> restrictionsQuery) {

        boolean exists = false;
        int querySize = restrictionsQuery.size();
        
        computo = 0.0;
       
        // Incremento 
        double inc = 1.0/(double)querySize;        

        int k= 0;
        
        // Para cada ingrediente k de la consulta, comprobamos si está en el caso o no
        while (k < querySize ) {
        	
            // Sacamos el ingrediente k, eliminando los espacios
            String ingK = restrictionsQuery.get(k);
            
            // Se coge el padre
            String superClass = SandwichOntology.getOntoBridge().listSuperClasses(ingK, true).next();
            superClass = superClass.substring(superClass.indexOf("#")+1);
            
            if(superClass.equals("SandwichBase")) {
            	
            	exists = ingredientsCase.contains(ingK);
            	
            	if(!exists) {
            		
            		exists = false;
            		
            		ArrayList<String> brothersK = OntologyUsefulFunctions.getBrothers(ingK);
                    brothersK.remove(ingK);
            		
                	int i = 0;
                    
                    while (i < brothersK.size() && !exists) {
                        
                        exists = ingredientsCase.contains(brothersK.get(i));  
                        i++;
                    }
                    i--;
                    
                    if(exists) {
                    	
                    	 // Si se ha encontrado algún hermano, se suma el coste                                    
                        computo += inc;
                        ingredientsCase = ingredientsCase.replace(brothersK.get(i), ingK);
                    	
                    }
                    
            	}
            	
            // Verifica si es un tipo de sandwich
            }else if(superClass.equals("Sandwich")) { 
            	
            	sandwichTypeVerification(ingK, ingredientsCase);
            
        	// Si el ingrediente k es hoja (no tiene hijos)
            } else if(OntologyUsefulFunctions.isLastChild(ingK)) {
                // Comprobamos si está en el caso
            	exists = ingredientsCase.contains(ingK);
                // Si no está en este sandwich, miramos los hermanos
                if (!exists) {
                    
                	ArrayList<String> brothersK = OntologyUsefulFunctions.getBrothers(ingK);
                    brothersK.remove(ingK);
                	int i = 0;
                    
                    while (i < brothersK.size() && !exists) {
                        
                        exists = ingredientsCase.contains(brothersK.get(i));  
                        i++;
                    }                              
                    if (exists) {
                    	// Si es el último ingrediente implica que la sustitución puede eliminar algún
                    	// ingrediente hermano del caso que se encuentra en la consulta, por lo tanto,
                    	// se añade este ingrediente como uno más al caso.
                    	if(k+1 == querySize && !superClass.equals("SandwichBase")) {
                    		 computo += inc;
                    		ingredientsCase = ingredientsCase +" "+ ingK;
                    	}else{
	                    	i--;
	                    // Si se ha encontrado algún hermano, se suma el coste                                    
	                        computo += inc;
	                        ingredientsCase = ingredientsCase.replace(brothersK.get(i), ingK);
                    	}
                    }                    
                    //Sino se mira si contiene un hijo de algún hermano
                    else {
                    	
                    	ArrayList<String> _brothersK = OntologyUsefulFunctions.getBrothers(OntologyUsefulFunctions.getSuperClass(ingK));
                    	_brothersK.remove(OntologyUsefulFunctions.getSuperClass(ingK));
                    	
                    	for(int j=0; j<k; j++) {   
                    		if(_brothersK.contains(OntologyUsefulFunctions.getSuperClass(restrictionsQuery.get(j))))
                    			_brothersK.remove(OntologyUsefulFunctions.getSuperClass(restrictionsQuery.get(j)));
			           	} 
                    	
                    	ArrayList<String> childsBrothersK = new ArrayList<String>();
           	 
			           	for(int j=0; j<_brothersK.size(); j++) {            		    		
			           		childsBrothersK.addAll(OntologyUsefulFunctions.getLastChilds(_brothersK.get(j)));
			           	}      
                    	
			           	int l = 0;
		                exists = false;
		                
		                while (l < childsBrothersK.size() && !exists) {
		                    
		                    exists = ingredientsCase.contains(childsBrothersK.get(l));
		                    l++;
		                    
		                }
		                
		                if (exists) {
		                	l--;
		                // Si se ha encontrado algun hijo de sus hermanos, se suma el coste
		                    computo += inc;           
		                    ingredientsCase = ingredientsCase.replace(childsBrothersK.get(l), ingK);
		                }else {
	                	// Sino se decrementa el coste
	                    	computo -= inc;
	                    }
                    	
                    }
                }
                // Si se ha encontrado, se suma el coste
                else {                      
                	computo += inc;                        
                }
            }
            // Si no es hoja, se debe mirar si alguno de sus hijos está
            else {
            	exists = false;
            	ArrayList<String> childsk = OntologyUsefulFunctions.getLastChilds(ingK);
            	int m = 0;
            	while (m < childsk.size() && !exists) {
            		
                	// Comprobamos si está en el caso
                	exists = ingredientsCase.contains(childsk.get(m));
            		m++;
            	}
            	//Sino se mira si contiene un hijo de algún hermano
            	if(!exists) {
            		
	            	ArrayList<String> brothersK = OntologyUsefulFunctions.getBrothers(ingK);
	            	 brothersK.remove(ingK);      	 
	            	 	     
	            	 for(int i=0; i<k; i++) {            		    		
		            		if(brothersK.contains(restrictionsQuery.get(i)))
		            			brothersK.remove(restrictionsQuery.get(i));
		            	}
	            	 
	            	 ArrayList<String> childsBrothersK = new ArrayList<String>();
	            	 
	            	for(int i=0; i<brothersK.size(); i++) {            		    		
	            		childsBrothersK.addAll(OntologyUsefulFunctions.getLastChilds(brothersK.get(i)));
	            	}          	            	 
	                
	            	int i = 0;
	                exists = false;
	                
	                while (i < childsBrothersK.size() && !exists) {
	                    
	                    exists = ingredientsCase.contains(childsBrothersK.get(i));
	                    i++;
	                    
	                }
	                
	                if (exists) {
	                	i--;
	                // Si se ha encontrado algun hijo de sus hermanos, se suma el coste
	                    computo += inc;
	                    Random r = new Random();
	                    int index = r.nextInt(childsk.size());
	                    
	                    ingredientsCase = ingredientsCase.replace(childsBrothersK.get(i), childsk.get(index));
	                }
	                else {
	                	// Sino se decrementa el coste
	                	computo -= inc;
	                }
            	}
            	else
            	{
            		// Si se ha encontrado algun hijo, se suma el coste
            		computo += inc;
            	}
            }            
            k++;                
        }
        // Si se han hecho todas las sustitución, la medida se similitud será 1.0  
        if(computo>0.9 && SandwichRecommender.getNumOfRecordCase()<SandwichRecommender.k)
        	SandwichRecommender.recordCase(ingredientsCase);
	}

	/**
	 * Verifica el contenido del caso por tipo de sandwich.
	 * @param sandwichType
	 * @param ingredientsCase
	 */
	private void sandwichTypeVerification(String sandwichType, String ingredientsCase) {
		
		switch(sandwichType) {
    	
	    	case "VegetarianSandwich": {
	    		
	    		searchVegetarianSandwichIngredients(ingredientsCase);
	    		break;
	    		
	    	}
	    	case "VeganSandwich": {
	    		
	    		searchVeganSandwichIngredients(ingredientsCase);
	    		break;
	    		
	    	}
	    	case "NonDairySandwich": {
	    		
	    		searchNonLactoseIntolerantSandwichIngredients(ingredientsCase);
	    		break;
	    		
	    	}
	    	case "DairySandwich": {
	    		
	    		searchLactoseIntolerantSandwichIngredients(ingredientsCase);
	    		break;
	    		
	    	}
	    	case "CarnivorousSandwich": {
	    		
	    		searchCarnivorousSandwichIngredients(ingredientsCase);
	    		break;
	    		
	    	}
	
		}
		
	}
	
	/**
	 * Busca ingredientes para un sandwich Carnívoro en el caso.
	 * @param ingredientsCase
	 */
	private void searchCarnivorousSandwichIngredients(String ingredientsCase) {
		
		ArrayList<String> childs = new ArrayList<String>();
		ArrayList<String> birdChilds = OntologyUsefulFunctions.getLastChilds("Bird");
		ArrayList<String> fishChilds = OntologyUsefulFunctions.getLastChilds("Fish");
		ArrayList<String> meatFoodChilds = OntologyUsefulFunctions.getLastChilds("Meat");
		
		int i = 0;
		boolean contains = false;
		
		childs.addAll(birdChilds);
		childs.addAll(fishChilds);
		childs.addAll(meatFoodChilds);       
    
        while (i < childs.size() && !contains) {
                
            contains = ingredientsCase.contains(childs.get(i));
            i++;
        }
        
        if(contains) {
        	
        	computo = 1.0;
        }		
		
	}

	/***
	 * Busca ingredientes para un sandwich Con Lácteos en el caso.
	 * @param ingredientsCase
	 */
	private void searchLactoseIntolerantSandwichIngredients(String ingredientsCase) {
		
		ArrayList<String> childs = new ArrayList<String>();
		ArrayList<String> dairyChilds = OntologyUsefulFunctions.getLastChilds("Dairy");
		String mayonnaise = "Mayonnaise";
		
		int i = 0;
		boolean contains = false;
		
		childs.addAll(dairyChilds);
		childs.add(mayonnaise);
		
		while (i < childs.size() && !contains) {
            
            contains = ingredientsCase.contains(childs.get(i));
            i++;
        }
        
        if(contains) {
        	
        	computo = 1.0;
        }
	}

	/**
	 * Busca ingredientes para un sandwich Sin Lácteos en el caso.
	 * @param ingredientsCase
	 */
	private void searchNonLactoseIntolerantSandwichIngredients(String ingredientsCase) {
		
		ArrayList<String> childs = new ArrayList<String>();
		ArrayList<String> dairyChilds = OntologyUsefulFunctions.getLastChilds("Dairy");
		String mayonnaise = "Mayonnaise";
		
		int i = 0;
		boolean contains = false;
		
		childs.addAll(dairyChilds);
		childs.add(mayonnaise);
		
		while (i < childs.size() && !contains) {
            
            contains = ingredientsCase.contains(childs.get(i));
            i++;
        }
        
        if(!contains) {
        	
        	computo = 1.0;
        }
		
	}

	/**
	 * Busca ingredientes para un sandwich Vegano en el caso.
	 * @param ingredientsCase
	 */
	private void searchVeganSandwichIngredients(String ingredientsCase) {
		
		ArrayList<String> childs = new ArrayList<String>();
		ArrayList<String> animalsChilds = OntologyUsefulFunctions.getLastChilds("Animals");
		ArrayList<String> processedFoodChilds = OntologyUsefulFunctions.getLastChilds("ProcessedFoods");
		ArrayList<String> dairyChilds = OntologyUsefulFunctions.getLastChilds("Dairy");
		
		int i = 0;
		boolean contains = false;
		
		childs.addAll(animalsChilds);
		childs.addAll(processedFoodChilds);
		childs.addAll(dairyChilds);
		
		
        while (i < childs.size() && !contains) {
            
            contains = ingredientsCase.contains(childs.get(i));
            i++;
        }
        
        if(!contains) {
        	
        	computo = 1.0;
        }
		
	}

	/**
	 * Busca ingredientes para un sandwich Vegetariano en el caso.
	 * @param ingredientsCase
	 */
	private void searchVegetarianSandwichIngredients(String ingredientsCase) {
		
		ArrayList<String> childs = new ArrayList<String>();
		ArrayList<String> birdChilds = OntologyUsefulFunctions.getLastChilds("Bird");
		ArrayList<String> fishChilds = OntologyUsefulFunctions.getLastChilds("Fish");
		ArrayList<String> meatFoodChilds = OntologyUsefulFunctions.getLastChilds("Meat");
		
		int i = 0;
		boolean contains = false;
		
		childs.addAll(birdChilds);
		childs.addAll(fishChilds);
		childs.addAll(meatFoodChilds);       
    
        while (i < childs.size() && !contains) {
                
            contains = ingredientsCase.contains(childs.get(i));
            i++;
        }
        
        if(!contains) {
        	
        	computo = 1.0;
        }
		
	}

	@Override
	public boolean isApplicable(Object caseObject, Object queryObject) {
		// TODO Auto-generated method stub
		return false;
	}
	
	

}
