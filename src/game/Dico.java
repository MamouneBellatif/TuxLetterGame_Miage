package game;

import java.util.ArrayList;
import java.util.Random;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.io.File;


import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Dico {
    
    private ArrayList<String> listeNiveau1;
    private ArrayList<String> listeNiveau2;
    private ArrayList<String> listeNiveau3;
    private ArrayList<String> listeNiveau4;
    private ArrayList<String> listeNiveau5;

    private ArrayList<ArrayList<String>> liste;

    private String cheminFichierDico;

    public Dico(String cheminFichierDico){
        this.cheminFichierDico=cheminFichierDico;
        
        listeNiveau1 = new ArrayList<>();
        listeNiveau2 = new ArrayList<>();
        listeNiveau3 = new ArrayList<>();
        listeNiveau4 = new ArrayList<>();
        listeNiveau5 = new ArrayList<>();

        liste = new ArrayList<>();

        liste.add(listeNiveau1);
        liste.add(listeNiveau2);
        liste.add(listeNiveau3);
        liste.add(listeNiveau4);
        liste.add(listeNiveau5);

        
    }

    public String getMotDepuisListeNiveaux(int niveau){

        //pioche dans la liste des liste avec l'index retournée par vérifie niveau
        return getMotDepuisListe(liste.get(vérifieNiveau(niveau)));
    }

    public void ajouteMotADico(int niveau, String mot){
        int index = vérifieNiveau(niveau);
        liste.get(index).add(mot);
    }

    public String getCheminFichierDico(){
        return cheminFichierDico;
    }

    private int vérifieNiveau(int niveau){
        //retourne l'index pour la liste de liste correpondant au bon niveau
        int index=0;
        if (niveau>=1 && niveau <=5){
            index = niveau-1;
        }
        else {
            index=1;
        }
        // System.out.println("resltat: "+index);
        return index;
    }

    private String getMotDepuisListe(ArrayList<String> list){
        //mot aléatoire
        int indexMax=list.size()-1;
        String mot ="vide";
        if(indexMax==0 && !list.isEmpty()){
            mot = list.get(0);
        }
        else if(!list.isEmpty()){
            mot = list.get(randomInRange(0, indexMax));
        }
        return mot;
    }

    public void lireDictionnaireDOM(String path, String filename)  throws ParserConfigurationException, SAXException {
        // DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        // DocumentBuilder db = dbf.newDocumentBuilder();
        //   Document doc = db.parse(new File(FILENAME));
        // Document document = db.parse(new File(path+filename));
        try {
            File file = new File(path+filename);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(file);
            document.getDocumentElement().normalize();
            System.out.println("Root Element :" + document.getDocumentElement().getNodeName());
            NodeList nList = document.getElementsByTagName("mot");
            System.out.println("----------------------------");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                System.out.println("\nCurrent Element :" + nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    System.out.println("Mot: " + eElement.getTextContent());
                    String mot =eElement.getTextContent();
                    System.out.println("Difficulté : " + eElement.getAttribute("niveau"));
                    String diff = eElement.getAttribute("niveau");
                    int dif = Integer.parseInt(diff);
                    ajouteMotADico(dif, mot);
                }
            }
        }
        catch(IOException e) {
            System.out.println(e);
        } 
    }

    private static int randomInRange(int min, int max) {
        //géneration d'entier aléatoir, author: mkyon https://mkyong.com
		if (min >= max) {
			throw new IllegalArgumentException("max > min");
		}
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}

    
}
