package game;

import java.util.ArrayList;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.io.File;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import game.XMLUtil.DocumentTransform;

public class Profil {
    
    private String nom;
    private String avatar;
    private String dateNaissance;
    private ArrayList<Partie> parties;
    
    public Document _doc;

    public Profil() {
    }

    //le premier constructeur créer un nouveau profil et un nouveau fichier xml a partir du nom et la date
    public Profil(String nom,String dateNaissance){
        // _doc = new Document();
        this.nom=nom;
        this.dateNaissance=dateNaissance;
        this.avatar="";
        parties = new ArrayList<Partie>();

        createXmlProfil(); 
        
        //creere nouveau profil xml
    }


    /** 
    * Constructeur pour charger un profil a partir de son ficheir xml
    */
    public Profil(String nomFichier) {
       
         _doc = fromXML(nomFichier);

        //racine
        Element profilElt = _doc.getDocumentElement();

        //recuperation des attributs
        String nom=profilElt.getElementsByTagName("nom").item(0).getTextContent();
        String avatar=profilElt.getElementsByTagName("avatar").item(0).getTextContent();
        String dateNaissanceXML=profilElt.getElementsByTagName("anniversaire").item(0).getTextContent();

        this.nom=nom;
        this.avatar=avatar;
        this.dateNaissance=xmlDateToProfileDate(dateNaissanceXML); //on convertie la date au bon format

        System.out.println("Initialisation profil: nom="+nom+" avatar="+avatar+" date="+dateNaissance);

        parties= new ArrayList<Partie>();
        NodeList partieList = profilElt.getElementsByTagName("partie");
        for (int i = 0; i < partieList.getLength(); i++) { //on parcours la liste des parties
            Element partieElt =(Element) partieList.item(i); //on recupere l'element de la partie
            Partie partie = new Partie(partieElt);
            parties.add(partie);
            System.out.println("Partie initialisée: "+partie);
        }

    }

        /** 
     * Crée un nouveau Document et le cnstruit a partir des attribut de l'instance, puis sauvegarde 
     * le profil dans un nouveau ficheir xml
    */
    private void createXmlProfil(){
        try{
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            _doc = db.newDocument();

            //racine et espaces de nom
            Element profilElt = _doc.createElement("profil");
            profilElt.setAttribute("xmlns", "http://myGame/tux");
            profilElt.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            profilElt.setAttribute("xsi:schemaLocation", "http://myGame/tux xsd/profil.xsd");
            _doc.appendChild(profilElt);
            
            //nom
            Element nomElt=_doc.createElement("nom");
            Text nomTxt=_doc.createTextNode(nom);
            nomElt.appendChild(nomTxt);
            profilElt.appendChild(nomElt);
            
            //avatat
            Element avatarElt=_doc.createElement("avatar");
            Text avatarTxt=_doc.createTextNode(avatar);
            avatarElt.appendChild(avatarTxt);
            profilElt.appendChild(avatarElt);
            
            //date
            Element dateElt=_doc.createElement("anniversaire");
            Text dateTxt=_doc.createTextNode(profileDateToXmlDate(dateNaissance));
            dateElt.appendChild(dateTxt);
            profilElt.appendChild(dateElt);
            
            Element partiesElt=_doc.createElement("parties");
            profilElt.appendChild(partiesElt);
            
            // DocumentTransform.writeDoc(_doc, "Data/xml/profils/"+nom+".xml");
            sauvegarder("Data/xml/profils/"+nom+".xml");
            System.out.println("Nouveau profil créé!");
        }
        catch (Exception e){
            System.out.println("Erreur creation profil: "+e);
        }
    }

   
   /** 
    * @return String
    */
   public String getNom(){
       return this.nom;
   }

    
    /** 
     * @return ArrayList<Partie>
     */
    public ArrayList<Partie> getParties(){
        return parties;
    }

    
    /** 
     * Ajoute la partie a la liste des parties puis ajoute un élement DOM au document principal
     * @param p partie a ajouter
     */
    public void ajouterPartie(Partie p){
        parties.add(p);
        Element partieElt=p.getPartie(_doc);
        Element partiesElt= (Element) _doc.getElementsByTagName("parties").item(0);
        partiesElt.appendChild(partieElt);
    }
    
    
    /** 
     * @param filename fichier de sauvegarde
     */
    public void sauvegarder(String filename){
        toXML(filename);
    }
   

    
    /** 
     *   Cree un DOM à partir d'un fichier XML
     * @param nomFichier
     * @return Document
     */
    public Document fromXML(String nomFichier) {
        try {
            return XMLUtil.DocumentFactory.fromFile(nomFichier);
        } catch (Exception ex) {
            Logger.getLogger(Profil.class.getName()).log(Level.SEVERE, null, ex);
        }
         return null;
    }

    
    /** 
     * Sauvegarde un DOM en XML
     * @param nomFichier
     */
    public void toXML(String nomFichier) {
        try {
            // XMLUtil.DocumentTransform.writeDoc(_doc, nomFichier);
            XMLUtil.DocumentTransform.writeDoc2(_doc, nomFichier);
        } catch (Exception ex) {
            Logger.getLogger(Profil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    /** 
     *  Takes a date in XML format (i.e. ????-??-??) and returns a date
     * in profile format: dd/mm/yyyy
     * @param xmlDate
     * @return String
     */
 
    public static String xmlDateToProfileDate(String xmlDate) {
        String date;
        // récupérer le jour
        date = xmlDate.substring(xmlDate.lastIndexOf("-") + 1, xmlDate.length());
        date += "/";
        // récupérer le mois
        date += xmlDate.substring(xmlDate.indexOf("-") + 1, xmlDate.lastIndexOf("-"));
        date += "/";
        // récupérer l'année
        date += xmlDate.substring(0, xmlDate.indexOf("-"));

        return date;
    }

    
    /** 
     * Takes a date in profile format: dd/mm/yyyy and returns a date
     * in XML format (i.e. ????-??-??)
     * @param profileDate
     * @return String
     */
    
    public static String profileDateToXmlDate(String profileDate) {
        String date;
        // Récupérer l'année
        date = profileDate.substring(profileDate.lastIndexOf("/") + 1, profileDate.length());
        date += "-";
        // Récupérer  le mois
        date += profileDate.substring(profileDate.indexOf("/") + 1, profileDate.lastIndexOf("/"));
        date += "-";
        // Récupérer le jour
        date += profileDate.substring(0, profileDate.indexOf("/"));

        return date;
    }
}
