package Utils;

public class StringEvaluator {

	public static String getLastWord(String wordsList, String separation) {
		
		String[] words =  wordsList.split(separation);
		
		for(int i=0; i<words.length; i++){
			System.out.println(words[i]);
		}
			
		String[] wordsAux = words[words.length-1].split("]");
		
		return wordsAux[wordsAux.length-1];
	}
	
//	/**
//	 * @param wordsList
//	 * @return
//	 */
//	public static String[] getSeparatedWords(String wordsList, String separation) {
//		
//		String[] words =  wordsList.split(separation);
//		
//		return words;
//	}
//	
//	public static void main(String args[]) {
//		
//		String[] words = getSeparatedWords("[Thing, SandwichIngredients, ProcessedFoods, Dressing, Oil]", ", ");
//		
//		for(int i=0; i<words.length; i++){
//			System.out.println(words[i]);
//		}
//			
//		String[] wordsAux = words[words.length-1].split("]");
//		
//		for(int i=0; i<wordsAux.length; i++){
//			System.out.println(wordsAux[i]);
//		}
//		
//	}
}
