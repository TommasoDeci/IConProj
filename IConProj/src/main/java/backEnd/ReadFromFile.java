package backEnd;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Set;

class ReadFromFile 
{
	Set<String> valoriAutore = new HashSet<String>();
	Set<String> valoriCasaEditrice = new HashSet<String>(); 
	Set<String> valoriLingua = new HashSet<String>(); 
	Set<String> valoriAnno = new HashSet<String>(); 
	Set<String> valoriGenere = new HashSet<String>(); 
	
	ReadFromFile(String filePath) throws IOException
	{
		File f = new File(filePath);
		BufferedReader br = new BufferedReader(new FileReader(f)); 
		String st; 
		st = br.readLine();  // salta la prima riga e legge i libri per riempire i 5 HashSet
		while ((st = br.readLine()) != null) 
		{
			String[] valori = st.split(",");
			try 
			{
				valoriAutore.add(valori[1]);
			    valoriCasaEditrice.add(valori[3]);
			    valoriGenere.add(valori[4]);
			    valoriLingua.add(valori[5]);
			    valoriAnno.add(valori[6]);
			}
			catch(ArrayIndexOutOfBoundsException e) 
			{
				System.out.println(valori[0]);				     
			}	
		}
		br.close();
	    
		// scrive su 5 file.txt il contenuto dei 5 HashSet
		BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\Utente\\Desktop\\Archivio\\generi.txt"));
	    for(String valore : valoriGenere) 
	    {
	    	writer.write(valore);
	    	writer.write("\n");
	    }
	    writer.close();
	    BufferedWriter writer1 = new BufferedWriter(new FileWriter("C:\\Users\\Utente\\Desktop\\Archivio\\lingue.txt"));
	    for(String valore : valoriLingua) 
	    {
	    	writer1.write(valore);
	    	writer1.write("\n");
	    }
	    writer1.close();
	    BufferedWriter writer2 = new BufferedWriter(new FileWriter("C:\\Users\\Utente\\Desktop\\Archivio\\caseEditrici.txt"));
	    for(String valore : valoriCasaEditrice) 
	    {
	    	writer2.write(valore);
	    	writer2.write("\n");
	    }
	    writer2.close();
	    BufferedWriter writer3 = new BufferedWriter(new FileWriter("C:\\Users\\Utente\\Desktop\\Archivio\\autori.txt"));
	    for(String valore : valoriAutore) 
	    {
	    	writer3.write(valore);
	    	writer3.write("\n");
	    }
	    writer3.close();
	    BufferedWriter writer4 = new BufferedWriter(new FileWriter("C:\\Users\\Utente\\Desktop\\Archivio\\anni.txt"));
	    for(String valore : valoriAnno) 
	    {
	    	writer4.write(valore);
	    	writer4.write("\n");
	    }
	    writer4.close();
	    
//	    legge dai 5 file.dmp il contenuto dei 5 HashSet
//		try 
//		{
//			FileInputStream fileIn = new FileInputStream("C:\\Users\\Utente\\eclipse-workspace\\Marvel\\autori.dmp");
//	        ObjectInputStream in = new ObjectInputStream(fileIn);
//	        valoriAutore = (Set<String>) in.readObject();
//	        in.close();
//	        fileIn.close();
//	         
//	        fileIn = new FileInputStream("C:\\Users\\Utente\\eclipse-workspace\\Marvel\\caseEditrici.dmp");
//	        in = new ObjectInputStream(fileIn);
//	        valoriCasaEditrice = (Set<String>) in.readObject();
//	        in.close();
//	        fileIn.close();
//	         
//	        fileIn = new FileInputStream("C:\\Users\\Utente\\eclipse-workspace\\Marvel\\lingue.dmp");
//	        in = new ObjectInputStream(fileIn);
//	        valoriLingua = (Set<String>) in.readObject();
//	        in.close();
//	        fileIn.close();
//
//	        fileIn = new FileInputStream("C:\\Users\\Utente\\eclipse-workspace\\Marvel\\anni.dmp");
//	        in = new ObjectInputStream(fileIn);
//	        valoriAnno = (Set<String>) in.readObject();
//	        in.close();
//	        fileIn.close();
//	         
//	        fileIn = new FileInputStream("C:\\Users\\Utente\\eclipse-workspace\\Marvel\\generi.dmp");
//	        in = new ObjectInputStream(fileIn);
//	        valoriGenere = (Set<String>) in.readObject();
//	        in.close();
//	        fileIn.close();
//	    } 
//		catch (IOException i) 
//		{
//	         i.printStackTrace();
//	    } 
//		catch (ClassNotFoundException c) 
//		{
//	         System.out.println("Employee class not found");
//	         c.printStackTrace();
//	    }
//		
//	    
	    // scrive su 5 file.dmp il contenuto dei 5 HashSet
	    try 
		{
	        FileOutputStream fileOut = new FileOutputStream("C:\\Users\\Utente\\eclipse-workspace\\Marvel\\autori.dmp");
	        ObjectOutputStream out = new ObjectOutputStream(fileOut);
	        out.writeObject(valoriAutore);
	        out.close();
	        fileOut.close();
	         
	        fileOut = new FileOutputStream("C:\\Users\\Utente\\eclipse-workspace\\Marvel\\caseEditrici.dmp");
	        out = new ObjectOutputStream(fileOut);
	        out.writeObject(valoriCasaEditrice);
	        out.close();
	        fileOut.close();
	         
	        fileOut = new FileOutputStream("C:\\Users\\Utente\\eclipse-workspace\\Marvel\\lingue.dmp");
	        out = new ObjectOutputStream(fileOut);
	        out.writeObject(valoriLingua);
	        out.close();
	        fileOut.close();
	         
	        fileOut = new FileOutputStream("C:\\Users\\Utente\\eclipse-workspace\\Marvel\\anni.dmp");
	        out = new ObjectOutputStream(fileOut);
	        out.writeObject(valoriAnno);
	        out.close();
	        fileOut.close();
	         
	        fileOut = new FileOutputStream("C:\\Users\\Utente\\eclipse-workspace\\Marvel\\generi.dmp");
	        out = new ObjectOutputStream(fileOut);
	        out.writeObject(valoriGenere);
	        out.close();
	        fileOut.close();
	         
	        System.out.printf("Serialized data is saved in C:\\Users\\Utente\\eclipse-workspace\\Marvel\n");
	     } 
		 catch (IOException i) 
		 {
	         i.printStackTrace();
	     }
	}
}