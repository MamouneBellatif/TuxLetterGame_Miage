package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.io.File;


import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import game.BrowserUtil;
import game.Partie;
import game.Profil;
import game.XMLUtil;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Classement {
    private Document doc_score;
    private Document _doc;
    private ArrayList<Partie> partieAl;
    private File[] listeFichier;

    public Classement(){
        partieAl = new ArrayList<Partie>();

        // Directory where the files are located
        
        parseProfils();
        for (Partie partie : partieAl) {
           System.out.println("nom :"+partie.getNom()+" "+partie.toString());
           System.out.println(partie.getNiveau()*100/partie.getTemps());
        }
        
        Collections.sort(partieAl);
        System.out.println("TOP 10:");
        for (int i = 0; i < 10; i++) {
            System.out.println(i+". nom :"+partieAl.get(i).getNom()+" "+partieAl.get(i).toString());
            System.out.println(partieAl.get(i).getNiveau()*100/partieAl.get(i).getTemps());
        }
        newXML();
    }
    
	public static void convertXMLToHTML(Source xml, Source xslt) {
		StringWriter sw = new StringWriter();

		try {

			FileWriter fw = new FileWriter("Data/xml/score.html");
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer trasform = tFactory.newTransformer(xslt);
			trasform.transform(xml, new StreamResult(sw));
			fw.write(sw.toString());
			fw.close();

			System.out.println("score.html géneré avec succès dans Data/xml ");

		} catch (IOException | TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}

		
	}

    public void toHTML(){
        Source xml = new StreamSource(new File("Data/xml/score.xml"));
		Source xslt = new StreamSource("Data/xml/xslt/score.xsl");

		convertXMLToHTML(xml, xslt);

        // try{
        //     Document doc_html=XMLUtil.DocumentFactory.fromXSLTransformation("Data/xml/xslt/score.xsl", doc_score);
        //     // XMLUtil.DocumentFactory.fromXSLTransformation("Data/xml/xslt/score.xsl", doc_score);
        //     XMLUtil.DocumentTransform.writeDoc2(doc_html, "Data/xml/html/score.html");
        // }catch(Exception e){
        //     System.out.println("to hmtl"+e);
        // }
    }
    public void affiche(){
        BrowserUtil.launch("Data/xml/score.html");
    }
    public void newXML(){
        try{
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc_score = db.newDocument();

            //racine et espaces de nom
            Element scoreElt = doc_score.createElement("classement");
            scoreElt.setAttribute("xmlns", "http://myGame/tux");
            // profilElt.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            // profilElt.setAttribute("xsi:schemaLocation", "http://myGame/tux xsd/profil.xsd");
            doc_score.appendChild(scoreElt);
            
            for (int i = 0; i < 10; i++) {
                // int score=partieAl.get(i).getNiveau()*100/partieAl.get(i).getTemps();
                int score=partieAl.get(i).getScore();
                Element partieElt =doc_score.createElement("partie");
                Element nomElt=doc_score.createElement("nom");
                Text nomTxt=doc_score.createTextNode(partieAl.get(i).getNom());
                nomElt.appendChild(nomTxt);
                partieElt.appendChild(nomElt);

                Element motElt=doc_score.createElement("mot");
                Text motTxt=doc_score.createTextNode(partieAl.get(i).getMot());
                motElt.appendChild(motTxt);
                partieElt.appendChild(motElt);

                Element valElt = doc_score.createElement("score");
                Text scoreTxt=doc_score.createTextNode(String.valueOf(score));
                valElt.appendChild(scoreTxt);
                partieElt.appendChild(valElt);

                partieElt.setAttribute("position", String.valueOf(i+1));

                scoreElt.appendChild(partieElt);
                // System.out.println(i+". nom :"+partieAl.get(i).getNom()+" "+partieAl.get(i).toString());
                // System.out.println(partieAl.get(i).getNiveau()*100/partieAl.get(i).getTemps());
            }
            toXML("Data/xml/score.xml");
        }
        catch (Exception e){
            System.out.println("Erreur creation profil: "+e);
        }
    }


    public void parseProfils(){
            File folder = new File("Data/xml/profils");
            File[] listeFichier = folder.listFiles();
        //on parcours les parties, on compare chaque partie avec le top10 si un est superieru on le remplace et on ecrit le nom avec
 
            String path ="Data/xml/profils/";
            for (int i = 0; i < listeFichier.length; i++) {
                
                _doc=fromXML(path+listeFichier[i].getName());
                Element profilElt = _doc.getDocumentElement();
                String nom=profilElt.getElementsByTagName("nom").item(0).getTextContent();
                NodeList partieList = profilElt.getElementsByTagName("partie");
                for (int j = 0; j < partieList.getLength(); j++) {
                    // Node pNode = partieList.item(j);
                    Element partieElt =(Element) partieList.item(j);
                    Partie partie = new Partie(partieElt);
                    partie.setNom(nom);
                    partieAl.add(partie);
                
            }
        }   
    }
    

    public Document fromXML(String nomFichier) {
        try {
            return XMLUtil.DocumentFactory.fromFile(nomFichier);
        } catch (Exception ex) {
            System.out.println(ex);
        }
         return null;
    }

    public void toXML(String nomFichier) {
        try {
            // XMLUtil.DocumentTransform.writeDoc(_doc, nomFichier);
            XMLUtil.DocumentTransform.writeDoc2(doc_score, nomFichier);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
