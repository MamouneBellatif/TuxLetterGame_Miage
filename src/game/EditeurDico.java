package game;

import java.util.ArrayList;
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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


import java.util.logging.Level;
import java.util.logging.Logger;

public class EditeurDico {
    public Document _doc;
    public String filename;

    public EditeurDico(String filename){
        this.filename=filename;
    }

    public void editer(String mot, int niveau){
        lireDOM(filename);
        ajouterMot(mot.toUpperCase(), niveau);
        ecrireDOM(filename);
        // }
    }
    public void ajouterMot(String mot, int niveau){
        //on recupere la racine
        Element dicoElt=(Element) _doc.getDocumentElement();
        
        //on creer un element mot
        Element motElt=_doc.createElement("mot");

        //attribut niveau:
        motElt.setAttribute("niveau", String.valueOf(niveau));

        //text du mot
        Text motTxt = _doc.createTextNode(mot);
        motElt.appendChild(motTxt);

        dicoElt.appendChild(motElt);
        
    }

    public void lireDOM(String filename) {
 
        _doc = fromXML(filename);

    }

    public void ecrireDOM(String filename){
        toXML(filename);
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
            XMLUtil.DocumentTransform.writeDoc2(_doc, nomFichier);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
