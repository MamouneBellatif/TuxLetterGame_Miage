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
    private String nom; //nom du joueur de cette partie, sert au classement
    private int score; //score de la partie, sert au classement

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
        String niveauString=motElt.getAttributeNode("niveau").getTextContent();

        int niveau=Integer.parseInt(niveauString);
        
       
        this.mot=mot;
        this.temps=(int) Double.parseDouble(temps);
        this.date=Profil.xmlDateToProfileDate(date);
        this.niveau=niveau;
        try{
            this.trouvé=Integer.parseInt(trouvé);
        }catch(Exception ex){//Si trouvé n'est pas initialisé on attrape l'exception et on initialise a 0
            this.trouvé=0;
        }
        this.score=getNiveau()*100*getTrouve()*mot.length()/getTemps();
    }

    
    /** 
     * Implémentation de l'interface Comparable
     * Comparaison de la partie avec une partie p, sert pour le classement (utilisation de Collection.sort)
     * @param p partie a comparer avec l'instane actuelle
     * @return int 0 si égale, entier positif si l'instance actuelle est plus petite
     */
    @Override
    public int compareTo(Partie p) {
        // int score=getNiveau()*100*getTrouve()/getTemps();
        // int pScore=((Partie) p).getNiveau()*100*((Partie) p).getTrouve()/((Partie) p).getTemps();
        return  p.getScore()-this.score;
    }

    
    /** 
     * Créée un Element pour doc a partir des attributs de l'instance
     * @param doc
     * @return Element
     */
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

    
    /** 
     * @param nbLettresRestantes
     */
    public void setTrouve(int nbLettresRestantes){
        double pourcentage = ((double) mot.length()- (double) nbLettresRestantes)/ (double) mot.length()*100;
        System.out.println("pourcentage: "+pourcentage);
        trouvé=(int) pourcentage;
    }



    
    /** 
     * @return int
     */
    public int getTrouve(){
        return trouvé;
    }

    
    /** 
     * @param score
     */
    public void setScore(int score){
        this.score = score;
    }

    
    /** 
     * @return int
     */
    public int getScore(){
        return score;
    }
    
    /** 
     * @param temps
     */
    public void setTemps(int temps){
        if(temps==0){//Gestion si la chrono n'a pas été compté, si temps est nul cela peut cuaser un problème lors du calcul du score
            this.temps=40;
        }
        else{
            this.temps=temps; 
        }
    }

    
    /** 
     * @param nom
     */
    public void setNom(String nom){
        this.nom=nom;
    }

    
    /** 
     * @return String
     */
    public String getNom(){
        return nom;
    }
    
    /** 
     * @return int
     */
    public int getTemps(){
        return temps;
    }

    
    /** 
     * @return int
     */
    public int getNiveau(){
        return niveau;
    }

    
    /** 
     * @return String
     */
    public String getMot(){
        return mot;
    }

    
    /** 
     * @return String
     */
    public String toString(){
        return "Mot: "+mot+" Date: "+date+" Trouvé: "+trouvé+" Temps: "+temps;
    }

 
    
}
