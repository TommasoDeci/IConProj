package backEnd;

import java.util.Map;

class StringDistance implements Distance
{
	public double calculate(Map<String, String> f1, Map<String, String> f2) // si potrebbe correggere con oggetti di 
																			// classe centroid
	{
		double sum = 0;
        for (String key : f1.keySet())
        {
            String v1 = f1.get(key);
            String v2 = f2.get(key);
            
            if(!(v1.equals(" ") || v2.equals(" ")))
            {
            	if (key.equals("Autore")) 
                {
                    if(!(v1.equals(v2))) 
                    {
                    	sum += 3;
                    }
                }
                
                if (key.equals("Casa editrice")) 
                {
                    if(!(v1.equals(v2))) 
                    {
                    	sum += 1;
                    }                        
                }
                
                if (key.equals("Anno")) 
                {
                    if(!(v1.equals(v2))) 
                    {
                    	sum += 1;
                    }
                }
                
                if (key.equals("Genere"))
                {
                    if(!(v1.equals(v2))) 
                    {
                    	sum += 4;
                    	if (v1.contains(v2) || v2.contains(v1))
                    	{
                    		sum -= 2;
                    	}
                    }
                }
                
                if (key.equals("Lingua")) 
                {
                    if(!(v1.equals(v2))) 
                    {
                    	sum += 2;
                    }
                }
            }     
        }
		return sum;
	}

}