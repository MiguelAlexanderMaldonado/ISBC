package Utils;

import java.util.ArrayList;

public class ArrayUtils {
	
    public static void concatenaArrayList(ArrayList<String> a1, ArrayList<String> a2) {      
    	
        for(int i= 0; i< a2.size(); i++)
                a1.add(a2.get(i));
    }

}
