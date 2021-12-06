package game;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.io.File;

public class Partie {
    private String date;
    private String mot;
    private int niveau;
    private int trouvé;
    private int temps;

    public Partie(String date, String mot, int niveau) {
        this.date=date;
        this.mot=mot;
        this.niveau=niveau;
    }
    
    public Partie(Element partieElt){
        //On recupere les elemnts néceissaires et on extrait la valeure
        String mot=partieElt.getElementsByTagName("nom").item(0).getTextContent();
        String temps=partieElt.getElementsByTagName("temps").item(0).getTextContent();
        String date=partieElt.getAttribute("date");
        String niveauString=partieElt.getAttributeNode("niveau").getTextContent();


        int niveau=Integer.parseInt(niveauString);
        
       
        this.mot=mot;
        this.temps=(int) Double.parseDouble(temps);
        this.date=date;
        this.niveau=niveau;
    }

    public Element getPartie(Document doc){
        //Element partie
        Element partieElt = doc.createElement("partie");

        //attribut date
        partieElt.setAttribute("date",this.date);

        //attribut trouvé (nécessaire ?)
        partieElt.setAttribute("trouvé", String.valueOf(this.trouvé));

        //element temps 
        Element tempsElt = doc.createElement("temps");
        Text tempsTxt = doc.createTextNode(String.valueOf(this.temps));
        tempsElt.appendChild(tempsTxt);

        //element mot et attribut niveau
        Element motElt = doc.createElement("mot");
        Text motTxt= doc.createTextNode(String.valueOf(this.mot));
        motElt.setAttribute("niveau", this.mot);
        motElt.appendChild(motTxt);

        //on ajoute les element au doc
        partieElt.appendChild(tempsElt);
        partieElt.appendChild(motElt);

        // doc.appendChild(partieElt);
        // doc.appendChild(partieElt);


        return partieElt;
        // return null;
    }
    public void setTrouve(int nbLettresRestantes){
        // trouvé=mot.length()-nbLettresRestantes;
        trouvé=(mot.length()-nbLettresRestantes)/mot.length()*100;
    }

    public void setTemps(int temps){
        this.temps=temps;
    }

    public int getNiveau(){
        return niveau;
    }

    public String getMot(){
        return mot;
    }

    public String toString(){
        return "Mot: "+mot+" Date: "+date+" Trouvé: "+trouvé+" Temps: "+temps;
    }
    
}
