package backEnd;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;

import org.bytedeco.librealsense.intrinsics;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.indexing.NewAxis;
import org.nd4j.nativeblas.Nd4jCpu.boolean_and;
import org.nd4j.nativeblas.Nd4jCpu.listdiff;
import org.nd4j.nativeblas.Nd4jCpu.zero_fraction;

public class K_means 
{	
	public List<Cluster> clustersRelocate;
	public List<Book> lista;
	
	public K_means(String filePath) throws IOException
	{
		String path = filePath;
		File f = new File(path);
//		ReadFromFile toto = new ReadFromFile(path);
		BufferedReader br = new BufferedReader(new FileReader(f)); 
		String st; 
		st = br.readLine(); //salta la prima riga
		lista = new ArrayList<Book>();
		  
		while ((st = br.readLine()) != null) 
		{
			String[] valori = st.split(",");
			try 
			{ 
				Map<String, String> attributi = new HashMap<String,String>();
				attributi.put("Autore", valori[1]);
				attributi.put("Casa editrice", valori[3]);
				attributi.put("Genere", valori[4]);
				attributi.put("Lingua", valori[5]);
				attributi.put("Anno", valori[6]);
				int i = 0;
				boolean presente = false;
				while (i < lista.size())
				{
					presente = false;
					if((lista.get(i).getDescription().equals(valori[2])))
					{
						presente = true;
						break;
					}
					i++;
				}
				if (presente == false) 
				{
					lista.add(new Book(valori[2], attributi));
				}	
		    }
			catch(ArrayIndexOutOfBoundsException e) 
			{
				System.out.println(valori[0]);
			}
		}   
		br.close();
		
//		 try 
//			{
//			        FileInputStream fileIn = new FileInputStream("lista.ser");
//			        ObjectInputStream in = new ObjectInputStream(fileIn);
//			        lista = (List<Book>) in.readObject();
//			        in.close();
//			        fileIn.close();
//			    } 
//				catch (IOException i) 
//				{
//			        i.printStackTrace();
//			        return;
//			    } 
//				catch (ClassNotFoundException c) 
//				{			
//			        System.out.println("Model class not found");
//			        c.printStackTrace();
//			        return;
//			    }
		
		//clustering();

	}  
	
	public void serializza(String filePath)
	{
		try 
		{
	         FileOutputStream fileOut = new FileOutputStream(filePath);
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(lista);
	         out.close();
	         fileOut.close();
	         System.out.println("Serialized data is saved in Pet");
	    } 
		catch (IOException i) 
		{
	         i.printStackTrace();
	    }
	}
	
	public void clustering() throws IOException 
	{
		double min = Double.MAX_VALUE;
		int best = 0;
		int k = 2;
		List<Centroid> bestCentroids = new ArrayList<>();
		while(k < 100) 
		{
			List<Centroid> centroids = randomCentroids(lista, k);
			List<Cluster> clustersCurrent = fit(lista, k, new StringDistance(), 100, centroids);
		    double sse = squaredError(clustersCurrent, new StringDistance());
		    if(sse < min) 
		    {
		    	min = sse;
		    	best = k;
		    	bestCentroids = centroids;
		    }
		    k++;
		}
		
		clustersRelocate = fit(lista,best,new StringDistance(),100, bestCentroids);
		
//		BufferedWriter writer = new BufferedWriter(new FileWriter("Ambrogio.txt"));
//	    int i = 0;
//	    while (i < clustersRelocate.size())
//	    {
//	    	writer.write(clustersRelocate.get(i).toString() + "\n" + i + "\n");
//	    	i++;
//	    }
//	    writer.close();
	}

	private static Centroid nearestCentroid(Book record, List<Centroid> centroids, Distance distance) 
	{
	    double minimumDistance = Double.MAX_VALUE;
	    Centroid nearest = null;
	 
	    for (Centroid centroid : centroids) 
	    {
	        double currentDistance = distance.calculate(record.getFeatures(), centroid.getCoordinates());
	 
	        if (currentDistance < minimumDistance) 
	        {
	            minimumDistance = currentDistance;
	            nearest = centroid;
	        }
	    }
	 
	    return nearest;
	}
	
	private static void assignToCluster(List<Cluster> clusters, Book record, Centroid correctCentroid) 
	{	
		int i = 0;
		while (i < clusters.size())
		{
			if (correctCentroid.equals(clusters.get(i).getCentroid()))
			{
				clusters.get(i).getRecords().add(record);
			}
			i++;
		}
	}
	
	private static Centroid average(Centroid centroid, List<Book> records) // ricalcola centroide, tramite media
	{
	   if (records == null || records.isEmpty()) 
	   { 
	       return centroid;
	   }
	 
	   String autoreMaggiore = frequency("Autore",records);
	   String genereMaggiore = frequency("Genere",records);
	   String casaMaggiore = frequency("Casa editrice",records);
	   String linguaMaggiore = frequency("Lingua",records);
	   String annoMaggiore = frequency("Anno",records);
	   
	   Map<String, String> average = new HashMap<String,String>();
	   average.put("Autore", autoreMaggiore);
	   average.put("Genere", genereMaggiore);
	   average.put("Casa editrice", casaMaggiore);
	   average.put("Lingua", linguaMaggiore);
	   average.put("Anno", annoMaggiore);
	 
	    return new Centroid(average);
	}

	private static String frequency(String key, List<Book> records) 
	{
		List<Book> temp  = records;
		String migliore = new String();
		int best = 0;
		int i = 0;
		while(i < temp.size()) 
		{
			String a = temp.get(i).getFeatures().get(key);
			int count = 1;
			temp.remove(i);
			int j = i+1;
			while(j < temp.size())
			{
				if(a.equals(temp.get(j).getFeatures().get(key))) 
				{
					count++;
					temp.remove(j);
				}
				j++;
			}
			if(count > best) 
			{
				best = count;
				migliore = a;
			}
			i++;
		}	
		return migliore;
	}
	
	private static List<Cluster> fit(List<Book> records, int k, Distance distance, int maxIterations, List<Centroid> centroids)
	{
		List<Cluster> currentClusters = new ArrayList<Cluster>(k); // mancava la prima assegnazione dei casuali nei 
																   // centroidi di currentClusters
		int l = 0;
		while (l < centroids.size()) // qui centroids.size va bene perch� gi� inizializzati con randomCentroids 
		{
			Cluster firstCluster = new Cluster(centroids.get(l), new ArrayList<Book>());
			currentClusters.add(firstCluster);
			l++;
		}
	    List<Cluster> lastState = new ArrayList<Cluster>(k);
	 
	    // iterate for a pre-defined number of times
	    for (int i = 0; i < maxIterations; i++) 
	    {
	        boolean isLastIteration = i == maxIterations - 1;
	        
	        // in each iteration we should find the nearest centroid for each record
	        for (Book record : records) 
	        {
	            Centroid centroid = nearestCentroid(record, centroids, distance); // centroide corretto in cui inserire record, lo fa bene!
	            assignToCluster(currentClusters, record, centroid);
	        }
	 
	        // if the assignments do not change, then the algorithm terminates
	        boolean shouldTerminate = isLastIteration || currentClusters.equals(lastState);
	        lastState = currentClusters;
	        if (shouldTerminate)
	        { 
	            break; 
	        }
	 
	        // at the end of each iteration we should relocate the centroids
	        centroids = relocateCentroids(currentClusters, k); // aggiunto k
	        int j = 0;
	        while (j < k)
	        {
	        	Cluster clusterRelocate = new Cluster(centroids.get(j), currentClusters.get(j).getRecords());
	        	currentClusters.set(j, clusterRelocate);
	        	j++;
	        }
	    }
	    return lastState;
	}
	
	private static List<Centroid> randomCentroids(List<Book> records, int k) 
	{
	    List<Centroid> centroids = new ArrayList<>(k);
	    int i = 0;
	    while(i < k) // centroids.size mi da Ambrogio vuoto
	    {
	    	int n = (int)(Math.random()*records.size()-1)+1;
	    	centroids.add(new Centroid(records.get(n).getFeatures())); 
	    	i++;
	    }
	  
	    return centroids;
	}
	
	private static List<Centroid> relocateCentroids(List<Cluster> clusters, int k) 
	{
	    List<Centroid> centroidi = new ArrayList<>();
	    int i = 0;
	    while(i < k) 
	    {
	    	centroidi.add(average(clusters.get(i).getCentroid(),clusters.get(i).getRecords()));
	    	i++;
	    }
	    return centroidi;
	}
	
	public static double squaredError(List<Cluster> clustered, Distance distance) 
	{
	    double sum = 0;
	    for (Cluster entry : clustered) 
	    {
	        Centroid centroid = entry.getCentroid();
	        for (Book record : entry.getRecords()) 
	        {
	            double d = distance.calculate(centroid.getCoordinates(), record.getFeatures());
	            sum += Math.pow(d, 2);
	        }
	    }
	         
	    return sum;
	}
	
	public List<Book> predict(Book b, List<Cluster> listaCluster)
	{
		List<Centroid> listCentroid = new ArrayList<>();
		List<Book> predetti = new ArrayList<>();
		
		int i = 0;
		while(i < listaCluster.size()) // aggiunge i centroidi di listaCluster dentro listCentroid
		{
			listCentroid.add(listaCluster.get(i).getCentroid());
			i++;
		}
		
		Centroid centroide = nearestCentroid(b, listCentroid, new StringDistance());
		for(Cluster current : listaCluster) 
		{
			if(current.getCentroid().equals(centroide))
			{
				predetti = current.getRecords();
				break;
			}
		}
		
		predetti.sort(new CompTypeComparator());
		Iterator<Book> it = predetti.iterator();
		while (it.hasNext())
		{
			if(it.next().getDescription().equals(b.getDescription()))
			{
				it.remove();
				break;
			}
		}
		
		return predetti;
	}
	
	public Book aquisisciLibro(String titolo) 
	{
		for(Book b : lista)
		{
			if(titolo.equals(b.getDescription())) 
			{
				return b;
			}
		}
		
		return null;
	}
	
	public String[] getGeneri() throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader("generi.txt")); 
		String st; 
		String[] generi = new String[62];
		int i = 0;
		while ((st = br.readLine()) != null) 
		{
			generi[i] = st;
			i++;
		}
		br.close();
		return generi;
	}
	
	public boolean contiene(String titolo)
	{
		for(Book b : lista) 
		{
			if(b.getDescription().equals(titolo)) 
			{
				return true;
			}
		}
		return false;
	}
	
}