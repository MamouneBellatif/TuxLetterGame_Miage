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

    private ArrayList<ArrayList<String>> liste; //ArrayList qui contient les listes par niveau, on peut donc facilement accéder a une liste d'un certain niveau avec l'indice

    private String cheminFichierDico;

    private String currentMot; //tampon une fois le mot construit
    private int currentNiveau; //tampon niveau
    private boolean inMot; //flag pour avoir qu'on est dans l'élément
    private StringBuilder currentValue;//tampon pour construire le mot
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

        currentValue=new StringBuilder();
        
    }

    

    
    /** 
     * Evenement début d'un élement: on reinitialise le buffer currentValue, et on recupere l'attribut niveau
     * @param uri
     * @param localName
     * @param qName
     * @param attributes
     * @throws SAXException
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        currentValue.setLength(0);
        if(qName.equals("mot")){
            currentMot = new String();
            inMot=true;
            int niveau=Integer.parseInt(attributes.getValue("niveau"));
            currentNiveau=niveau;
        }

    }
    
    
    /** 
     * Evenement fin d'un élement: on ajoute le mot construit dans le buffer currentValue ainsi que le niveau en appelant dans
     * en appelant la méthode ajouteMotADico
     * @param uri
     * @param localName
     * @param qName
     * @throws SAXException
     */
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
    
    /** 
     * Construction du mot, on concatene le caractère au tampon
     * @param ch
     * @param start
     * @param length
     * @throws SAXException
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        // String lecture = new String(ch, start, length);
        if(inMot){
            currentValue.append(ch, start, length);
        }
        
    }
    
    /** 
     * @throws SAXException
     */
    @Override
    public void startDocument() throws SAXException {
        System.out.println("startDocument()");
    }

    
    /** 
     * @throws SAXException
     */
    @Override
    public void endDocument() throws SAXException {
        System.out.println("endDocument()");

    } 

    
    /** 
     * Appel du parser SAX
     * @param filename
     * @throws SAXException
     */
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


    /** 
     * Pioche un mot depuis la liste en paramètre
     * @param list une des 5 listes par niveau
     * @return String retourne le mot pioché
     */
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
    
    /** 
     * Appel getMotDepuisListe avec la liste qui correspond au niveau
     * @param niveau niveau du mot
     * @return String retourne un mot au hasard avec le bon niveau
     */
    public String getMotDepuisListeNiveaux(int niveau){

        //pioche dans la liste des liste avec l'index retournée par vérifie niveau
        return getMotDepuisListe(liste.get(vérifieNiveau(niveau)));
    }

    
    /** 
     * Vérifie que le niveau existe (appel a vérifieNiveau) puis ajoute le mot a la bonne liste
     * @param niveau
     * @param mot
     */
    public void ajouteMotADico(int niveau, String mot){
        int index = vérifieNiveau(niveau);
        liste.get(index).add(mot);
    }

    
    /** 
     * @return String
     */
    public String getCheminFichierDico(){
        return cheminFichierDico;
    }

    
    /** 
     * Encapsulation, vérifie que le niveau appelé existe et le retourne (sinon retourne 1)
     * @param niveau eniter a verifier
     * @return int niveau
     */
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

    


    
    /** 
     * lecture DOM du dictionnaire, recuperation des mots et niveau a partir du Document 
     * @param cheminRep chemin du repertoire 
     * @param nomFichier nom du fichier
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public void lireDictionnaireDOM(String cheminRep, String nomFichier)  throws ParserConfigurationException, SAXException {

        try {
            File file = new File(cheminRep+nomFichier);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(file);
            document.getDocumentElement().normalize();
            NodeList nodeListeMots = document.getElementsByTagName("mot");
            for (int temp = 0; temp < nodeListeMots.getLength(); temp++) {
                Node nodeMot = nodeListeMots.item(temp);
                if (nodeMot.getNodeType() == Node.ELEMENT_NODE) {
                    Element motElt = (Element) nodeMot;
                    String mot =motElt.getTextContent();
                    String diff = motElt.getAttribute("niveau");
                    int dif = Integer.parseInt(diff);
                    ajouteMotADico(dif, mot);
                }
            }
        }
        catch(IOException e) {
            System.out.println(e);
        } 
    }

    
    /** 
     * géneration d'entier aléatoir entre min et max
     * @param min
     * @param max
     * @return int
     * @author  mkyon https://mkyong.com
     */
    private static int randomInRange(int min, int max) {
		if (min >= max) {
			throw new IllegalArgumentException("max > min");
		}
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}

    
}
