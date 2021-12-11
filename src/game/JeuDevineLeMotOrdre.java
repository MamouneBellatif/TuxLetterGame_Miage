package game;

public class JeuDevineLeMotOrdre extends Jeu {
    private int nbLettresRestantes;
    Chronomètre chrono; 
    private boolean finished;
    private boolean win;
    private boolean noTime;
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

    public void displayControls(){

    }

    public void finishScreen(Partie partie){ //affichage du score a la fin et attend l'utilisateur
        int trouve =partie.getMot().length()-nbLettresRestantes;
        int score=partie.getNiveau()*100*partie.getTrouve()*partie.getMot().length()/getTemps();
        partie.setScore(score);
        menuText.addText("Vous avez trouvé "+trouve+" lettres ("+partie.getTrouve()+"%)", "trouve", 200, 340);
        menuText.addText("en "+getTemps()+" secondes", "temps", 260, 320);
        menuText.addText("Vous avez gagné! ", "win", 260, 280);
        menuText.addText("Votre score: "+score,"score", 260, 260);
        menuText.addText("Appuiez sur 1 pour continuer", "prompt", 200, 240);

        menuText.getText("score").display();
        menuText.getText("trouve").display();
        menuText.getText("prompt").display();
        if(win){
            menuText.getText("win").display();
        }
        else{
            menuText.getText("win").modifyTextAndDisplay("Vous avez perdu...");
        }

        if(!noTime){
            menuText.getText("temps").display();
        }
        else{
            menuText.getText("temps").modifyTextAndDisplay("Temps écoulé...");
        }

        waitInput();
        menuText.getText("temps").clean();
        menuText.getText("trouve").clean();
        menuText.getText("prompt").clean();
        menuText.getText("win").clean();
        menuText.getText("score").clean();
        
    }
   
    public void démarrePartie(Partie partie){
        noTime=false;
        finished=false;
        win=false;
        spawnLetters(partie.getMot());
        nbLettresRestantes = lettres.size();
        
        chrono = new Chronomètre(40000);
        chrono.start();
        
        menuText.addText(chrono.getRemaining()+" secondes restantes", "timer", 20, 20);
        menuText.addText("touches directionnelles ou zqsd pour bouger | 'espace' et 'b' pour sauter |\n ctrl pour prendre une lettre","controls", 20, 450);
        menuText.getText("timer").display();
        menuText.getText("controls").display();
        
        // menuText.addText("perdu :(", "perdu", 200, 20);

    }

    public void terminePartie(Partie partie){
        chrono.stop();
        partie.setTemps(getTemps());
        menuText.getText("timer").clean();
        menuText.getText("controls").clean();

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
        finishScreen(partie);
        removeLetters();

    }

    //met a jor l'affichage du chrono
    protected void displayTimer(){
        menuText.getText("timer").modifyTextAndDisplay(chrono.getRemaining()+" secondes restantes");
    }


    protected void appliqueRegles(Partie partie){ //retourne si la partie est finie
        displayTimer();
        // System.out.println("Il reste "+chrono.getRemaining()+" secondes, LettresRestantes: "+nbLettresRestantes);
        if(!chrono.remainsTime()){
            // chrono.stop();
            // partie.setTemps(getTemps());
            this.finished=true;
            noTime=true;

        }
        else if(nbLettresRestantes==0){
            // chrono.stop();
            // partie.setTemps(getTemps());
            this.finished=true;
            this.win=true;
        }

        if(chrono.remainsTime() && nbLettresRestantes!=0 && tuxTrouveLettre() ){
            System.out.println("trouvé!");

            int index = lettres.size()-nbLettresRestantes;
            //faire disparaitre la bonne lettre (recuperer le bon index)
            lastFound.setScale(lastFound.getScale()*0.8);
            // lastFound.setScale(lastFound.getScale()*1.2);
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
    
    public boolean isFinished(){
        return finished;
    }
}
