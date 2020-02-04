package backEnd;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nd4j.nativeblas.Nd4jCpu.listdiff;
import org.nd4j.nativeblas.Nd4jCpu.to_double;

class Cluster 
{
	private Centroid centroide;
	private List<Book> records;
	
	Cluster(Centroid cent, List<Book> libri)
	{
		centroide = cent;
		records = libri;
	}
	
	public Centroid getCentroid()
	{
		return centroide;
	}
	
	public List<Book> getRecords()
	{
		return records;
	}
	
	public boolean equals(Cluster x) 
	 {
		if(centroide.equals(x.getCentroid())) 
		 {
			 if(records.equals(x.getRecords())) 
			 {
				 return true;
			 }
		 }		  
		 return false;
	 }
	
	public String toString()
	{
		String result;
		result = ("\n{Cluster: " + centroide + "\nElementi nel cluster:" + records.size() + "\n" + "Libri:");
		int i = 0;
		while (i < records.size())
		{
			result = result + ("\n" + records.get(i));
			i++;
		}
		result = result + "}\n";
		return result;
	}
}
