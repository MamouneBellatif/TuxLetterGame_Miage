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

public class Profil {
    
    private String nom;
    private String avatar;
    private String dateNaissance;
    private ArrayList<Partie> parties;
    
    public Document _doc;

    public Profil() {
    }

    public Profil(String nom,String dateNaissance){
        this.nom=nom;
        this.dateNaissance=dateNaissance;
    }

 // Cree un DOM à partir d'un fichier XML
//     public Profil(String nomFichier) {
//     //TMP
//      this.nom="mamoune";
//      this.dateNaissance="04-10-1998";
//  }
    public Profil(String nomFichier) {
       //TMP
        this.nom="mamoune";
        this.dateNaissance="04-10-1998";
        //TMP
        // _doc = fromXML("Data/xml/"+nomFichier+".xml");

        // _doc = fromXML(nomFichier);
        // parsing à compléter
        // ?!#?!
    }
    // public Profil(String nom, String avatar, String anniversaire, ArrayList<Partie> parties) {
    //     this.nom = nom;
    //     this.avatar = avatar;
    //     this.annx²iversaire = anniversaire;
    //     this.parties = parties;
    // }

    public ArrayList<Partie> getParties(){
        return parties;
    }

    public void ajouterPartie(Partie p){
        parties.add(p);
    }
    

   

    // Cree un DOM à partir d'un fichier XML
    public Document fromXML(String nomFichier) {
        try {
            return XMLUtil.DocumentFactory.fromFile(nomFichier);
        } catch (Exception ex) {
            Logger.getLogger(Profil.class.getName()).log(Level.SEVERE, null, ex);
        }
         return null;
    }

    // Sauvegarde un DOM en XML
    public void toXML(String nomFichier) {
        try {
            XMLUtil.DocumentTransform.writeDoc(_doc, nomFichier);
        } catch (Exception ex) {
            Logger.getLogger(Profil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /// Takes a date in XML format (i.e. ????-??-??) and returns a date
    /// in profile format: dd/mm/yyyy
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

    /// Takes a date in profile format: dd/mm/yyyy and returns a date
    /// in XML format (i.e. ????-??-??)
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
