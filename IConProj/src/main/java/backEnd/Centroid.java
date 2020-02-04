package backEnd;

import java.util.Map;

class Centroid 
{
	private final Map<String, String> coordinates;

	Centroid(Map<String,String> coordinate)
	{
		coordinates = coordinate;
	}
	
	public Map<String, String> getCoordinates() 
	{
		return coordinates;
	}
	
	public String toString() 
	 {
		 return("Autore = " + coordinates.get("Autore") + ", Casa editrice = " + coordinates.get("Casa editrice") + ", Genere = " + coordinates.get("Genere") + ", Lingua = " + coordinates.get("Lingua") + ", Anno edizione = " + coordinates.get("Anno"));
	 }
	 
	public boolean equals(Centroid x) 
	 {
		if(coordinates.get("Autore").equals(x.coordinates.get("Autore"))) 
		 {
			 if(coordinates.get("Casa editrice").equals(x.coordinates.get("Casa editrice"))) 
			 {
				 if(coordinates.get("Genere").equals(x.coordinates.get("Genere"))) 
				 {
					 if(coordinates.get("Lingua").equals(x.coordinates.get("Lingua")))
					 {
						 if(coordinates.get("Anno").equals(x.coordinates.get("Anno")))
						 {
							 return true;
						 }
					 }
				 }
			 }
		 }		  
		 return false;
	 }
}