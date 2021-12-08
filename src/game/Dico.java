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
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Dico extends DefaultHandler{
    
    private ArrayList<String> listeNiveau1;
    private ArrayList<String> listeNiveau2;
    private ArrayList<String> listeNiveau3;
    private ArrayList<String> listeNiveau4;
    private ArrayList<String> listeNiveau5;

    private ArrayList<ArrayList<String>> liste;

    private String cheminFichierDico;

    private StringBuffer buffer;

    public Dico(String cheminFichierDico){
        super();
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

    private String currentMot;
    private int currentNiveau;
    boolean inMot;
    private StringBuilder currentValue = new StringBuilder();

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        currentValue.setLength(0);
        // if(qName.equals("dictionnaire")){
        //     // currentMot=
        // }
        if(qName.equals("mot")){
            // currentValue =new StringBuilder();
            currentMot = new String();
            inMot=true;
            int niveau=Integer.parseInt(attributes.getValue("niveau"));
            currentNiveau=niveau;
        }

    }
    
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(qName.equals("mot")){
            currentMot=currentValue.toString();
            ajouteMotADico(currentNiveau, currentMot);
            currentMot=null;
            // currentValue=null;
            inMot=false;
        }
    }
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        // String lecture = new String(ch, start, length);
        if(inMot){
            currentValue.append(ch, start, length);
        }
        
    }
    @Override
    public void startDocument() throws SAXException {
        System.out.println("startDocument()");
    }

    @Override
    public void endDocument() throws SAXException {
        System.out.println("endDocument()");

    } 

    public void lireDictionnaire(String filename) throws SAXException{
        try{
            SAXParserFactory fabrique = SAXParserFactory.newInstance();
            SAXParser parseur = fabrique.newSAXParser(); 
            File fichier = new File(filename); 
            // DefaultHandler gestionnaire = new PersonneHandler(); 
            parseur.parse(fichier, this);
        }
        catch(Exception e){
            System.out.println("erreur lecture sax: "+e);
        }
        
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
