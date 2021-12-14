package game;

public class JeuDevineLeMotOrdre extends Jeu {

    private int nbLettresRestantes;
    private Chronomètre chrono; 
    private boolean finished;//flag partie terminée
    private boolean win; //flag de victoire
    private boolean noTime; //falg temps écoulé
    private Letter lastFound; //tampon de la dernière lettre trouvée 

    public JeuDevineLeMotOrdre() {
        super();
        lastFound=null;
    }



    
    /** 
     * @return boolean renvoie vrai si la lettre a trouvé est dans la zone rouge
     */
    private boolean tuxTrouveLettre(){

        //coordonnées de la zone de dépot
        double x1=37;
        double x2=60;
        double z1=85;
        double z2=100;

        boolean found = false;
        double lettreX;
        double lettreZ;

        int indexLettre = lettres.size()-nbLettresRestantes;//recupère l'indice de la lettre a recuperer en calculant la difference entre le nombre de lettre totale et restantes
        char letterToFind = lettres.get(indexLettre).getLetter(); //recupère le caractère de la lettre depuis l'instance de Letter

        int i=0;
        Letter lettre;
        

        //on cherche la lettre a trouver (jusqu'a trouver une correspondance)
        /* si on fait un parcours au lieu de tester directement la lettre, 
        c'est au cas ou le mot contient plusieurs fois la meme lettre
        on peut donc mettre n'importe quelle instance de cette lettre sans se soucier de l'prdre*/
        while(i<lettres.size() && !found){
            lettre=lettres.get(i);
            lettreX=lettres.get(i).getX();
            lettreZ=lettres.get(i).getZ();
            //Si la lettre est dans la zone et que la lettre n'a jamais été validé, on flag la lettre comme trouvée
            //et on la garde en mémoire pur ne pas la revalider imédiatement après
            if(lettre.getLetter()==letterToFind && !lettre.equals(lastFound) && lettreX>x1 && lettreX<x2 && lettreZ>z1 && lettreZ <z2){
                found=true;
                lastFound=lettres.get(i);
            }
            i++;
        }

     
        return found;
    }

   
    
    /** 
     * initalise les flags, appel spawnLetters pour mettre en place les lettres dans l'environement
     * puis affiche les commandes et le chrono
     * @param partie
     */
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
        

    }

    /** 
     * mis a jour de l'affichage du chrono, puis vérifie si il reste du temps ou si la partie est fini, et met a jour les flags en consequence
     * si il reste du temps et des lettres a trouver, vérifie si la lettre a trouvé est dans la zone de dépot,
     * si la lettre est trouvé, on met a jour les compteurs, et la lettre n'est plus accesible et positionnée 
     * au fond de l'environment pour afficher le mot
     * @param partie partie actuelle
     */
    protected void appliqueRegles(Partie partie){ //retourne si la partie est finie
        
        displayTimer();

        if(!chrono.remainsTime()){
            this.finished=true;
            noTime=true;
        }
        else if(nbLettresRestantes==0){
            this.finished=true;
            this.win=true;
        }

        if(chrono.remainsTime() && nbLettresRestantes!=0 && tuxTrouveLettre() ){

            System.out.println("trouvé!");
            int index = lettres.size()-nbLettresRestantes; //indice de la lettre trouvée
            lastFound.setScale(lastFound.getScale()*0.8); //change l'echelle de la lettre pour la placer au fond
            lastFound.setRotateY(0); //rotation pour avoir la lettre lisible de face
            lastFound.setZ(0); //Poistionnement au fond de l'environneme,t
            lastFound.setY(45); //positionnement en hauteur pour qu'elle soit visible et inaccessible
            lastFound.setX(((80)+(index*80))/lettres.size());//la lettre est positionnée horizontalement (relatif selon sa place dans le mot et le nombre de lettre)
            lastFound.setFound(true);//mis a jour de l'attribut boolean found de la lettre          
            nbLettresRestantes--;//on et a jour le nombre de lettre a trouver
        }
    }
    
    
    /** 
     * Arrete le chrono, met a jour le temps trouvé dans la partie 
     * et lance l'affichage de l'écran de fin de partie
     *  puis enlève les lettres de l'environement
     * @param partie
     */
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

    /** 
     * Ecran de fin pour afficher les stats de la partie, puis aattend l'utilisateur pour revenir au menu
     * @param partie partie en cour
     */
    public void finishScreen(Partie partie){ //affichage du score a la fin et attend l'utilisateur
        int trouve =partie.getMot().length()-nbLettresRestantes;
        int score=partie.getNiveau()*100*partie.getTrouve()*partie.getMot().length()/partie.getTemps();
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

    /* met a jour le timer (affichage)*/
    protected void displayTimer(){
        menuText.getText("timer").modifyTextAndDisplay(chrono.getRemaining()+" secondes restantes");
    }

    /** 
     * retourne vrai si la partie est terminée
     * @return boolean 
     */
    public boolean isFinished(){
        return finished;
    }

        
    /** 
     * inutilisé
     * @return int
     */
    private int getNbLettresRestantes() {
        return this.nbLettresRestantes;
    }

    
    /** 
     * @return int
     */
    private int getTemps(){
        return chrono.getSeconds();
    }

}
