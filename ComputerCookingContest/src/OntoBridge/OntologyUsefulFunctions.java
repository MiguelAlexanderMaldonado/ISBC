package OntoBridge;

import java.util.ArrayList;
import java.util.Iterator;

public class OntologyUsefulFunctions {
	/**
	 * Devuelve la clase padre de una clase en concreto
	 * @param concept
	 * @return
	 */
	public static String getSuperClass(String concept) {
		
		String superClass = "";
		
		if(!concept.equals("Thing")) {
		
	        superClass = SandwichOntology.getOntoBridge().listSuperClasses(concept, true).next();
	        superClass = superClass.substring(superClass.indexOf("#")+1);
	        
		}
		
		if(superClass.equals("Thing") || superClass.equals("Nothing"))	return "";
		
        return superClass;
        
	}
	
	
	/**
	 * Método que devuelve los hermanos de un ingrediente.
	 * @param ingredient
	 * @return
	 */
    public static ArrayList<String> getBrothers(String ingredient) {
    	
    	ingredient = deleteBlanks(ingredient);
        ArrayList<String> brothers = new ArrayList<String>();            

        //si el padre es la cadena vacia, entonces o es el elemento raiz de la ontologia
        //(no tiene hermanos) o el elemento no existe.
        if(!getSuperClass(ingredient).equals("")){                      
                
        	brothers = getChilds(getSuperClass(ingredient));
        	
        } 
        
        return brothers;
    }

    /**
     * Método que devuelve los hijos de un ingrediente.
     * @param ingredient
     * @return
     */
    public static ArrayList<String> getChilds(String ingredient){
    	
        ingredient = deleteBlanks(ingredient);
        ArrayList<String> childs = new ArrayList<String>();

        if(SandwichOntology.getOntoBridge().existsClass(ingredient)){
            //listamos las subclases del primer nivel
            Iterator<String> subClases = SandwichOntology.getOntoBridge().listSubClasses(ingredient,true);
            //  No es vacio
            if(subClases.hasNext()){
                String childIngredient;
                while(subClases.hasNext()){
                	childIngredient = SandwichOntology.getOntoBridge().getShortName(subClases.next());
                        if (!childIngredient.contains("Nothing")){
                                childs.add(childIngredient);
                        }
                }
            }
        }
        
        return childs;
            
    }

    /**
     * Método que devuelve las hojas de una rama. Hijos finales de un ingrediente.
     * @param ingrediente
     * @return
     */
    public static ArrayList<String> getLastChilds(String ingredient){
        //primero eliminamos los espacios de los ingredientes, convirtiendolos a "_"
    	ingredient = deleteBlanks(ingredient);
        ArrayList<String> hijos = new ArrayList<String>();

        //si existe la clase del ingrediente en la ontologia y no es una hoja
        if(SandwichOntology.getOntoBridge().existsClass(ingredient) && !isLastChild(ingredient)) {
        	
            Iterator<String> subClases = SandwichOntology.getOntoBridge().listSubClasses(ingredient,false);
            
            while (subClases.hasNext()) {
                    String ing = SandwichOntology.getOntoBridge().getShortName(subClases.next());
                    if (isLastChild(ing)) hijos.add(ing);
            }

        }
        
        return hijos;
        
    }
    

    /**
     * Indica si el ingrediente es una hoja.
     * @param ingredient
     * @return
     */
    public static boolean isLastChild(String ingredient) { 
    	
		ingredient = deleteBlanks(ingredient);
        boolean isLastChild = false;
        
        if (SandwichOntology.getOntoBridge().existsClass(ingredient)) { 
        	
                Iterator<String> childsList= SandwichOntology.getOntoBridge().listSubClasses(ingredient,true);
                // Es una hoja si no tiene hijos (es decir su hijo es Nothing)
                isLastChild = childsList.next().contains("Nothing");
        }
        
        return isLastChild;
            
    }

    public static String deleteBlanks (String ingredient){
	    if (ingredient.contains(" ")){
	            ingredient = ingredient.replaceAll(" ","_");
	    }
	    return ingredient;
    }

}
