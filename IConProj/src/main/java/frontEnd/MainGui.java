package frontEnd;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;

import backEnd.Book;
import backEnd.K_means;
import net.sourceforge.javaocr.ocrPlugins.CharacterExtractor;

class MainGui 
{	
	Container contenuto = null;
	Home homePage;
	Fotografia fotoPage;
	Inserimento insertPage;
	Corretto correctPage;
	JPanel tabbedPane = new JPanel();
	K_means kMeans; 
	JButton back = new JButton("Indietro",new ImageIcon("arrow.png"));
	List<Book> predetti = new ArrayList<Book>();
	
	MainGui()
	{
		try 
		{
			kMeans = new K_means("ACCHIAPPALIBRI.CSV");

		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		tabbedPane.setLayout(new BoxLayout(tabbedPane, BoxLayout.PAGE_AXIS));
		JFrame frame = new JFrame("Acchiappalibri");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(500, 100, 600, 500);
		contenuto = frame.getContentPane();
		homePage = new Home();
		homePage.setLayout(new BoxLayout(homePage, BoxLayout.PAGE_AXIS));
		tabbedPane.setLayout(new BorderLayout());
		tabbedPane.add(homePage);
		contenuto.add(tabbedPane);
		frame.setVisible(true);
		frame.setResizable(false);
	}
	
	class Home extends JPanel
	{
		Home() 
		{
			JLabel titolo = new JLabel(new ImageIcon("acchiappalibri.png"));
			JPanel upper = new JPanel();
			JPanel bottoni = new JPanel();
			JLabel descrizione = new JLabel("<html><font size = 4>Benvenuto! Questo sistema e' capace di riconoscere<br>"
					+ "il titolo del tuo libro e consigliartene un altro<br>"
					+ "sperando che ti piaccia!</font></html>");
			JButton fotografia = new JButton("Fotografa il tuo libro", new ImageIcon("camera.png"));
			fotografia.addActionListener(new Foto());
			JButton inserimento = new JButton("Inserisci il tuo libro", new ImageIcon("addBook.png"));
			inserimento.addActionListener(new Carica());
			
			upper.add(titolo);
			upper.add(descrizione);
			bottoni.add(fotografia);
			bottoni.add(inserimento);

			add(upper);
			add(bottoni);
		}
		
		private class Foto implements ActionListener
		{
			public void actionPerformed(ActionEvent evento)
			{
				homePage.setVisible(false);
				fotoPage = new Fotografia();
				fotoPage.setLayout(new BoxLayout(fotoPage, BoxLayout.PAGE_AXIS));
				tabbedPane.add(fotoPage);
				fotoPage.setVisible(true);
			}
		}
		
		private class Carica implements ActionListener 
		{
			public void actionPerformed(ActionEvent evento)
			{
				homePage.setVisible(false);
				try 
				{
					insertPage = new Inserimento();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
				tabbedPane.add(insertPage);
				insertPage.setVisible(true);
			}
		}
	}
	
	class Fotografia extends JPanel 
	{
		private JPanel central;
		private JLabel intestazione;
		private JPanel bottoni;
		private JPanel down;
		private JTextArea title;
	//	private Webcam webcam;
		
		Fotografia() 
		{
			
			JLabel titolo = new JLabel(new ImageIcon("acchiappalibri.png"));
			intestazione = new JLabel("Scatta la foto del libro oppure importala!");
			JPanel titoli = new JPanel();
			bottoni = new JPanel();
			central = new JPanel();
			down = new JPanel();
			JButton importa = new JButton("Importa", new ImageIcon("import.png"));
			importa.addActionListener(new Importa());
			JButton scatta = new JButton("Scatta", new ImageIcon("camera.png"));
			scatta.addActionListener(new Scatta());
			bottoni.add(scatta);
			bottoni.add(importa);
			bottoni.setVisible(true);
			
//			webcam = Webcam.getDefault();
//			webcam.setViewSize(new Dimension(320, 240)); 
//			WebcamPanel panelCamera = new WebcamPanel(webcam);
//			panelCamera.setFPSDisplayed(true);
//			panelCamera.setDisplayDebugInfo(true);
//			panelCamera.setImageSizeDisplayed(true);
//			panelCamera.setMirrored(false);
//			JButton scattaFoto = new JButton(new ImageIcon("bigCamera.png"));
//			scattaFoto.addActionListener(new ScattaFoto());
//			scattaFoto.setSize(50, 42);
//			
//			central.add(panelCamera);
//			central.add(scattaFoto);
//			central.setVisible(false);
//			
			JLabel isTitle = new JLabel("Ho capito questo titolo, correggilo se non e' quello che ti aspettavi, dopodiche' premi Cerca");
			JSeparator s = new JSeparator();
			s.setOrientation(SwingConstants.HORIZONTAL);
			title = new JTextArea();
			title.setPreferredSize(new Dimension(500,20));
			JButton cerca = new JButton("Cerca", new ImageIcon("lente.png"));
			cerca.addActionListener(new Cerca());
			down.add(isTitle);
			down.add(s);
			down.add(title);
			down.add(cerca);
			down.setVisible(false);
		
			add(back);
			back.addActionListener(new Back());
			titoli.add(titolo);
			titoli.add(intestazione);
			add(titoli);
			add(bottoni);
			add(central);
			add(down);
			
		}

		private class Importa implements ActionListener
		{
			public void actionPerformed(ActionEvent evento)
			{
	//			webcam.close();
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File(System.getProperty("user.home") + System.getProperty("file.separator")));
				chooser.setDialogTitle("Scegli il File desiderato");
				FileFilter imageFilter = new FileNameExtensionFilter("Image files", "tif", "jpg", "tiff", "bmp" , "gif", "png", "wbmp", "jpeg");
				chooser.addChoosableFileFilter(imageFilter);
				chooser.setAcceptAllFileFilterUsed(false);
				 
				if (chooser.showOpenDialog(central) == JFileChooser.APPROVE_OPTION)
				{ 
					CharacterExtractor slicer = new CharacterExtractor();
				    slicer.slice(chooser.getSelectedFile(), new File("."), 50, 50);
				   
				    	
					    title.setText("Brav");
					    central.setVisible(false);
					    intestazione.setVisible(false);
					    bottoni.setVisible(false);
					    down.setVisible(true);
				    
				}
			}
		}
		
		private class Scatta implements ActionListener 
		{
			public void actionPerformed(ActionEvent evento)
			{
				intestazione.setVisible(false);
				bottoni.setVisible(false);
				central.setVisible(true);
			}
		}
		
		private class ScattaFoto implements ActionListener
		{
			public void actionPerformed(ActionEvent evento)
			{
//				webcam.open();
//				File fotoTitolo = new File("titolo.png");
//				try 
//				{
//					ImageIO.write(webcam.getImage(), "PNG", fotoTitolo);
//				} 
//				catch (IOException e) 
//				{
//					JOptionPane.showMessageDialog(null , "Impossibile scattare foto");
//				}
//				central.setVisible(false);
//				webcam.close();
				CharacterExtractor slicer = new CharacterExtractor();
		//	    slicer.slice(fotoTitolo, new File("."), 50, 50);
			    
					
				    title.setText("Brav");
			
				down.setVisible(true);
			}
		}
		
		private class Cerca implements ActionListener
		{
			public void actionPerformed(ActionEvent evento)
			{
				if (kMeans.contiene(title.getText()) == true ) 
				{
					try 
					{
						kMeans.clustering();
					} 
					catch (IOException e) 
					{
						e.printStackTrace();
					}
					predetti = kMeans.predict(kMeans.aquisisciLibro(title.getText()), kMeans.clustersRelocate);
					fotoPage.setVisible(false);
					correctPage = new Corretto();
					tabbedPane.add(correctPage, BorderLayout.CENTER);
					correctPage.setVisible(true);
				}
				else 
				{
					JOptionPane.showMessageDialog(null, "Non e' stato trovato alcun libro con questo titolo, riprova");
				}
				
			}
		}
		
		private class Back implements ActionListener 
		{
			public void actionPerformed(ActionEvent evento)
			{
				fotoPage.setVisible(false);
				homePage.setVisible(true);
		//		webcam.close();
			}
		}
		
	}
	
	class Inserimento extends JPanel
	{
		private JTextField textTitolo;
		private JTextField textAutore;
		private JTextField textCasaEditrice;
		private JTextField textLingua;
		private JTextField textAnno;
		private JComboBox<String> generiList;
		
		Inserimento() throws IOException 
		{
			JPanel upper = new JPanel();
			upper.setLayout(new BoxLayout(upper, BoxLayout.PAGE_AXIS));
			JLabel titolo = new JLabel("Inserisci il titolo:");
			textTitolo = new JTextField();
			JLabel autore = new JLabel("Inserisci l'autore:");
			textAutore = new JTextField();
			JLabel casaEditrice = new JLabel("Inserisci la casa editrice:");
			textCasaEditrice = new JTextField();
			JLabel genere = new JLabel("Scegli il genere:");
			generiList = new JComboBox<String>(kMeans.getGeneri());
			JLabel lingua = new JLabel("Inserisci la lingua:");
			textLingua = new JTextField();
			JLabel anno = new JLabel("Inserisci l'anno di edizione:");
			textAnno = new JTextField();
			JButton aggiungi = new JButton("Aggiungi");
			aggiungi.addActionListener(new Aggiungi());
			upper.add(titolo);
			upper.add(textTitolo);
			upper.add(autore);
			upper.add(textAutore);
			upper.add(casaEditrice);
			upper.add(textCasaEditrice);
			upper.add(genere);
			upper.add(generiList);
			upper.add(lingua);
			upper.add(textLingua);
			upper.add(anno);
			upper.add(textAnno);
			upper.add(aggiungi);
			
			back.addActionListener(new Back());
			add(back);
			add(upper);
		}
		
		private class Aggiungi implements ActionListener
		{
			public void actionPerformed(ActionEvent evento)
			{
				if (textAutore.getText().isEmpty() || textTitolo.getText().isEmpty() ||
					textCasaEditrice.getText().isEmpty() || textAnno.getText().isEmpty()
					|| textLingua.getText().isEmpty())
				{
					JOptionPane.showMessageDialog(insertPage, "Inserisci tutti i campi del nuovo libro che vuoi inserire!\n");
				}
				else 
				{
					Map<String, String> features = new HashMap<String,String>();
					features.put("Autore", textAutore.getText().toLowerCase());
					features.put("Casa editrice", textCasaEditrice.getText().toLowerCase());
					features.put("Genere", (String) generiList.getSelectedItem());
					features.put("Lingua", textLingua.getText().toLowerCase());
					features.put("Anno", textAnno.getText().toLowerCase());
					kMeans.lista.add(new Book(textTitolo.getText().toLowerCase(), features));
					kMeans.serializza("lista.ser");
					insertPage.setVisible(false);
					try 
					{
						kMeans.clustering();
					} 
					catch (IOException e) 
					{
						e.printStackTrace();
					}
					predetti = kMeans.predict(kMeans.aquisisciLibro(textTitolo.getText().toLowerCase()), kMeans.clustersRelocate);
					correctPage = new Corretto();
					tabbedPane.add(correctPage, BorderLayout.CENTER);
					correctPage.setVisible(true);
				}
			}
		}
		
		private class Back implements ActionListener 
		{
			public void actionPerformed(ActionEvent evento)
			{
				insertPage.setVisible(false);
				homePage.setVisible(true);
			}
		}
	}
	
	class Corretto extends JPanel
	{
		int i = 0;
		JTextArea risultato = new JTextArea();
	
		Corretto()
		{
			JLabel titolo = new JLabel(new ImageIcon("acchiappalibri.png"));
			JPanel panel = new JPanel();
			JLabel descrizione = new JLabel("<html><font size = 4>Ho trovato il seguente libro simile a quello inserito.<br>"
					+ "Se non e' di tuo gradimento premi Successivo per visualizzarne un altro,<br>"
					+ "altrimenti premi Fine</font></html>");
			risultato.setPreferredSize(new Dimension(500,20));
			JSeparator s = new JSeparator();
			s.setOrientation(SwingConstants.HORIZONTAL);
			
			isFinished(i);
			risultato.setEditable(false);
			
			JButton successivo = new JButton("Successivo", new ImageIcon("forward.png"));
			successivo.addActionListener(new Successivo());
			
			JButton fine = new JButton("Fine", new ImageIcon("fine.png"));
			fine.addActionListener(new Fine());
			
			add(titolo);
			add(descrizione);
			add(s);
			add(risultato);
			add(successivo);
			add(fine);
			
		}
		
		private void isFinished(int x)
		{
			if(x < predetti.size())
			{
				risultato.setText(predetti.get(i).getDescription());
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Non sono stati trovati altri libri simili");
				correctPage.setVisible(false);
				homePage.setVisible(true);
			}
		}
		
		private class Fine implements ActionListener 
		{
			public void actionPerformed(ActionEvent evento)
			{
				correctPage.setVisible(false);
				homePage.setVisible(true);
			}
		}
		
		private class Successivo implements ActionListener 
		{
			public void actionPerformed(ActionEvent evento)
			{
				i++;
				isFinished(i);
			}
		}
	}
	
	public static void main(String args[])
	{
		MainGui gui = new MainGui();
	}
	
}
