package backEnd;

import java.util.Comparator;

class CompTypeComparator implements Comparator
{
	public int compare(Object o1, Object o2)
	{
		int result;
		Book primo = ((Book)o1);
		Book secondo = ((Book)o2);
		
		result = (int)new StringDistance().calculate(primo.getFeatures(), secondo.getFeatures());
		
		return result;
	}

}