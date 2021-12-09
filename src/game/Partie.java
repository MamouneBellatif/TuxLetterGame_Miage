package game;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.io.File;

public class Partie implements Comparable<Partie> {
    private String date;
    private String mot;
    private int niveau;
    private int trouvé;
    private int temps;
    private String nom;

    public Partie(String date, String mot, int niveau) {
        this.date=date;
        this.mot=mot;
        this.niveau=niveau;
    }
    
    public Partie(Element partieElt){
        //On recupere les elemnts néceissaires et on extrait la valeure
        Element motElt =(Element) partieElt.getElementsByTagName("mot").item(0);
        Element tempsElt = (Element) partieElt.getElementsByTagName("temps").item(0);

        String mot=motElt.getTextContent();
        String temps=tempsElt.getTextContent();
        String date=partieElt.getAttribute("date");
        String trouvé=partieElt.getAttribute("trouvé");
        System.out.println("trouv "+trouvé);
        // int trouvé=Integer.parseInt(partieElt.getAttribute("trouvé"));
        String niveauString=motElt.getAttributeNode("niveau").getTextContent();

        //initaliser trouvé ?
        int niveau=Integer.parseInt(niveauString);
        
       
        this.mot=mot;
        this.temps=(int) Double.parseDouble(temps);
        this.date=Profil.xmlDateToProfileDate(date);
        this.niveau=niveau;
        try{
            this.trouvé=Integer.parseInt(trouvé);
        }catch(Exception ex){
            this.trouvé=0;
        }
        
    }

    public Element getPartie(Document doc){
        //Element partie
        Element partieElt = doc.createElement("partie");

        //attribut date
        String date=Profil.profileDateToXmlDate(this.date);
        partieElt.setAttribute("date",date);

        //attribut trouvé (nécessaire ?)
        partieElt.setAttribute("trouvé", String.valueOf(this.trouvé));

        //element temps 
        Element tempsElt = doc.createElement("temps");
        Text tempsTxt = doc.createTextNode(String.valueOf(this.temps));
        tempsElt.appendChild(tempsTxt);

        //element mot et attribut niveau
        Element motElt = doc.createElement("mot");
        Text motTxt= doc.createTextNode(String.valueOf(this.mot));
        motElt.setAttribute("niveau", String.valueOf(this.niveau));
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
        // System.out.println("longeur: "+mot.length());
        // System.out.println("restant: "+nbLettresRestantes);
        double pourcentage = ((double) mot.length()- (double) nbLettresRestantes)/ (double) mot.length()*100;
        System.out.println("pourc: "+pourcentage);
        trouvé=(int) pourcentage;
    }

    public int getTrouve(){
        return trouvé;
    }
    public void setTemps(int temps){
        this.temps=temps;
    }

    public void setNom(String nom){
        this.nom=nom;
    }

    public String getNom(){
        return nom;
    }
    public int getTemps(){
        return temps;
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

    @Override
    public int compareTo(Partie p) {
        int score=getNiveau()*100*getTrouve()/getTemps();
        int pScore=((Partie) p).getNiveau()*100*((Partie) p).getTrouve()/((Partie) p).getTemps();
        // int pScore=p.getNiveau()*100/p.getTemps();
        return pScore-score;
    }
    
}
