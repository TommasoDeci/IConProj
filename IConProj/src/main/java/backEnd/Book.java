package backEnd;

import java.io.Serializable;
import java.util.Map;

public class Book implements Serializable
{
	 private final String description;
	 private final Map<String, String> features;
	 
	 public Book(String descrizione, Map<String, String> attributi)
	 {
		 description = descrizione;
		 features = attributi;
	 }
	 
	 public String getDescription() 
	 {
		 return description;
	 }
	 
	 Map<String, String> getFeatures()
	 {
		 return features;
	 }
	 
	 public String toString() 
	 {
		 return(description + " : Autore = " + features.get("Autore") + ", Casa editrice = " + features.get("Casa editrice") + ", Genere = " + features.get("Genere") + ", Lingua = " + features.get("Lingua") + ", Anno edizione = " + features.get("Anno") + "\n");
	 }
	 
	 public boolean equals(Book x) 
	 {
		 if(description.equals(x.description)) 
		 {
			 if(features.get("Autore").equals(x.features.get("Autore"))) 
			 {
				 if(features.get("Casa editrice").equals(x.features.get("Casa editrice"))) 
				 {
					 if(features.get("Genere").equals(x.features.get("Genere"))) 
					 {
						 if(features.get("Lingua").equals(x.features.get("Lingua")))
						 {
							 if(features.get("Anno").equals(x.features.get("Anno")))
							 {
								 return true;
							 }
						 }
					 }
				 }
			 }		 
		 }
		 return false;
	 }

}