package game;

public class JeuDevineLeMotOrdre extends Jeu {
    private int nbLettresRestantes;
    Chronomètre chrono; 

    public JeuDevineLeMotOrdre() {
        super();

    }


    private int getNbLettresRestantes() {
        return this.nbLettresRestantes;
    }

    private int getTemps(){
        return chrono.getSeconds();
    }
 
    Letter lastFound=null; //pour empecher de valiser deux fois la meme lettre si deux lettres se suivent
    private boolean tuxTrouveLettre(){
        //empecher deux valider deux lettres a la fois
        //renvoie vrai si la lettre a trouvé est dans la zone rouge
        //implémenter redondance de lettre
        //empecher une lettre detre validée deux fois
        double x1=37;
        double x2=60;
        double z1=85;
        double z2=100;

        boolean found = false;
        double lettreX;
        double lettreZ;

        int indexLettre = lettres.size()-nbLettresRestantes;
        char letterToFind = lettres.get(indexLettre).getLetter();

        int i=0;
        Letter lettre;
        while(i<lettres.size() && !found){
            lettre=lettres.get(i);
            lettreX=lettres.get(i).getX();
            lettreZ=lettres.get(i).getZ();
            if(lettre.getLetter()==letterToFind && !lettre.equals(lastFound) && lettreX>x1 && lettreX<x2 && lettreZ>z1 && lettreZ <z2){
                found=true;
                lastFound=lettres.get(i);
            }
            i++;
        }

     
        return found;
    }

   
    public void démarrePartie(Partie partie){
        
        
        spawnLetters(partie.getMot());
        nbLettresRestantes = lettres.size();

        chrono = new Chronomètre(40000);
        chrono.start();

    }

    public void terminePartie(Partie partie){
        if(nbLettresRestantes==0 && chrono.remainsTime()){
            partie.setTrouve(nbLettresRestantes);
            System.out.println("gagné!");
            // partie.setTemps(chrono.getSeconds());
        }
        else {
            System.out.println("perdu!");
        }
        // int pourcentage =(lettres.size()-nbLettresRestantes)/lettres.size()*100;
        System.out.println("restantes");
        partie.setTrouve(nbLettresRestantes);
        System.out.println("Trouvé: "+partie.getTrouve()+"% en "+getTemps()+" secondes");
        removeLetters();
    }

    protected void appliqueRegles(Partie partie){
        // System.out.println("Il reste "+chrono.getRemaining()+" secondes, LettresRestantes: "+nbLettresRestantes);
        if(!chrono.remainsTime()){
            System.out.println("no time");
            chrono.stop();
            partie.setTemps(getTemps());

        }
        else if(nbLettresRestantes==0){
            chrono.stop();
            partie.setTemps(getTemps());
        }

        if(chrono.remainsTime() && nbLettresRestantes!=0 && tuxTrouveLettre() ){
            System.out.println("trouvé!");

            int index = lettres.size()-nbLettresRestantes;
            //faire disparaitre la bonne lettre (recuperer le bon index)
            // lastFound.setScale(lastFound.getScale()*0.8);
            lastFound.setScale(lastFound.getScale()*1.2);
            lastFound.setRotateY(0);
            // lastFound.setZ(100);
            lastFound.setZ(0);
            lastFound.setY(45);
            // lastFound.setY(30);
            // lastFound.setX((80/lettres.size())+(index*80/lettres.size()));
            lastFound.setX(((80)+(index*80))/lettres.size());
            lastFound.found=true;
            // getEnv().removeObject(lastFound);
            
            nbLettresRestantes--;
        }

    }
    
}
