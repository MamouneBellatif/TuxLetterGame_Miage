package game;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import env3d.Env;
import env3d.advanced.EnvNode;

/**
 *
 * @author gladen
 */
public abstract class Jeu {

    enum MENU_VAL {
        MENU_SORTIE, MENU_CONTINUE, MENU_JOUE
    }

    private final Env env;
    private Tux tux;
    private final Room mainRoom;
    private final Room homeRoom;
    private final Room menuRoom;
    private Profil profil;
    private final Dico dico;
    protected EnvTextMap menuText;                         //text (affichage des texte du jeu)
    ArrayList<Letter> lettres;
    //private Mountain montagne;
    
    
    public Jeu() {

        // Crée un nouvel environnement
        env = new Env();

        // Instancie une Room
        mainRoom = new Room();
        homeRoom=new Room();
        homeRoom.setTextureBottom("textures/floor/planks.png");

        // Instancie une autre Room pour les menus
        menuRoom = new Room();
        menuRoom.setTextureEast("textures/black.png");
        menuRoom.setTextureWest("textures/black.png");
        menuRoom.setTextureNorth("textures/black.png");
        menuRoom.setTextureBottom("textures/black.png");

        // Règle la camera
        env.setCameraXYZ(50, 60, 175);
        env.setCameraPitch(-20);

        // Désactive les contrôles par défaut
        env.setDefaultControl(false);

        // Instancie un profil par défaut
        profil = new Profil();
        
        // Dictionnaire
        dico = new Dico("pathtodicofile");

        // instancie le menuText
        menuText = new EnvTextMap(env);
        
        // Textes affichés à l'écran
        menuText.addText("Voulez vous ?", "Question", 200, 300);
        menuText.addText("1. Commencer une nouvelle partie ?", "Jeu1", 250, 280);
        menuText.addText("2. Charger une partie existante ?", "Jeu2", 250, 260);
        menuText.addText("3. Sortir de ce jeu ?", "Jeu3", 250, 240);
        menuText.addText("4. Quitter le jeu ?", "Jeu4", 250, 220);
        menuText.addText("Choisissez un nom de joueur : ", "NomJoueur", 200, 300);
        menuText.addText("1. Charger un profil de joueur existant ?", "Principal1", 250, 280);
        menuText.addText("2. Créer un nouveau joueur ?", "Principal2", 250, 260);
        menuText.addText("4. Supprimer un profil ?", "Principal4", 250, 220);
        menuText.addText("6. Sortir du jeu ?", "Principal6", 250, 180);

        //Textes
        menuText.addText("Entrez un niveau entre 1 et 5: ", "niveau", 200, 300);
        menuText.addText("Erreur. Entrez un niveau entre 1 et 5: ", "erreurNiveau", 200, 300);
        
        menuText.addText("Choissiez l'indice de la partie souhaitée: ", "index", 200, 300);
        menuText.addText("Erreur. Choissiez l'indice de la partie souhaitée: ", "indexErreur", 200, 300);

        menuText.addText("5. Ajouter un mot ?", "Principal5", 250, 200);
        menuText.addText("Entrez le mot a ajouter: ", "ajoutMot", 250, 300);

        menuText.addText("3. Afficher le classement ?", "Principal3", 250, 240);




        //intancie les lettres
        lettres = new ArrayList<>();

    }

    /**
     * Gère le menu principal
     *
     */
    public void execute() {

        MENU_VAL mainLoop;
        mainLoop = MENU_VAL.MENU_SORTIE;
        do {
            mainLoop = menuPrincipal();
        } while (mainLoop != MENU_VAL.MENU_SORTIE);
        System.out.println("au revoir");
        this.env.setDisplayStr("Au revoir !", 300, 30);
        env.exit();
    }


    
    /** 
     *  fourni
     * @return String
     */
    private String getNomJoueur() {
        String nomJoueur = "";
        menuText.getText("NomJoueur").display();
        nomJoueur = menuText.getText("NomJoueur").lire(true);
        menuText.getText("NomJoueur").clean();
        return nomJoueur;
    }

    
    /** 
     * Entré utilisater avec un message
     * @param message message affiché a l'utilisateur
     * @return String
     */
    private String lireTexte(String message){
        String input="";
        menuText.getText(message).display();
        // input = menuText.getText(message).lire(true);
        input = menuText.getText(message).lire(true);
        menuText.getText(message).clean();
        return input;
    }

    
    /** 
     * Lis un entier entré par l'utilisateur avec un message affiché
     * @param message
     * @return String
     */
    private String lireEntier(String message){
        String input="";
        menuText.getText(message).display();
        input = menuText.getText(message).lireNombre(true);
        menuText.getText(message).clean();
        return input;
    }

    
    /** 
     * Recupère le niveau que l'utilisateur soubaire et s'assure que l'entrée est entre 1 et 5
     * @return int
     * @throws NumberFormatException
     */
    private int getNiveau() throws NumberFormatException {
        int niveau=0;
        String message ="niveau";
        String input="";
        Boolean success=false;
        do{
            try {
                niveau=Integer.parseInt(lireEntier(message));
                success=true;
            }
            catch (NumberFormatException e){
                message="erreurNiveau";
                success=false;
            }            
        } while(niveau<1 || niveau >5 || !success);
        return niveau;
    }



    
    /** 
     *  Affichage des parties jouées par le profil, la méthode récupère l'ensemble des parties dans une liste
     * et affiche un menu où l'utilisateur peut naviguer  d'une partie a l'autre
     * @return int
     */
    public int afficheParties(){
        ArrayList<Partie> alPartie= new ArrayList<Partie>();
        int nbParties = profil.getParties().size();
        
        for (Partie partie : profil.getParties()) {
            //index * constante / taille
            alPartie.add(partie);
        }

        
        int index=0;
        boolean menu=true;
        int choixPartie;
        if(!alPartie.isEmpty()){ //si il ya au moins une partie
            menuText.addText("(1.jouer 2.partie précedente 3.partie suivante 4.revenir","partieCtrl",100,200);
            menuText.getText("partieCtrl").display();
            menuText.addText(alPartie.get(index).toString(), "partieChoix",100,300);
            //index%nbPartie
            int moduloIndex;
            do{
                moduloIndex=Math.floorMod(index, alPartie.size()); //le modulo en java donne des resultats negatif, cette fonction est plus adaptée
                System.out.println("index: "+moduloIndex);
                menuText.getText("partieChoix").modifyTextAndDisplay((moduloIndex+1)+"/"+alPartie.size()+alPartie.get(moduloIndex).toString()); //navigue entre les parti et affiche le l'index modulo le nombre de partie pour ne pas sortir de la liste
                int touche=0;
                while (!(touche == Keyboard.KEY_1 || touche == Keyboard.KEY_2 ||touche == Keyboard.KEY_3  ||touche == Keyboard.KEY_4)){// ||touche == Keyboard.KEY_1 || touche == Keyboard.KEY_2 || touche == Keyboard.KEY_3 || touche == Keyboard.KEY_NUMPAD1 || touche ==  Keyboard.KEY_NUMPAD2 || touche ==  Keyboard.KEY_NUMPAD3)) {
                    touche = env.getKey();
                    env.advanceOneFrame();
                }
    
                switch (touche) {
                    case Keyboard.KEY_2:
                        index--;
                        break;
                    case Keyboard.KEY_3: 
                        index++;
                        break;
                    case Keyboard.KEY_1:
                        menu=false;
                        break;
                    case Keyboard.KEY_4:
                        moduloIndex=-1;
                        menu=false;
                        break;
                }
            }while(menu);
            choixPartie = moduloIndex;
            menuText.getText("partieChoix").clean();
            menuText.getText("partieCtrl").clean();


        }
        else{
            choixPartie=-1;
            menuText.addText("pas de partie (vide) appuiez sur 1 pour revenir", "partieChoix",100,300);
            menuText.getText("partieChoix").display();
            int touche=0;
            while (!(touche == Keyboard.KEY_1)){
                    touche = env.getKey();
                    env.advanceOneFrame();
                }
            menuText.getText("partieChoix").clean();

        }
        


        return choixPartie;
    }
    
    
    /** 
     * @return MENU_VAL
     */
    // fourni
    private MENU_VAL menuJeu() {

        MENU_VAL playTheGame;
        playTheGame = MENU_VAL.MENU_JOUE;
        Partie partie;
        do {
            // restaure la room du menu
            env.setRoom(menuRoom);
            // affiche menu
            menuText.getText("Question").display();
            menuText.getText("Jeu1").display();
            menuText.getText("Jeu2").display();
            menuText.getText("Jeu3").display();
            menuText.getText("Jeu4").display();
            
            // vérifie qu'une touche 1, 2, 3 ou 4 est pressée
            int touche = 0;
            while (!(touche == Keyboard.KEY_1 || touche == Keyboard.KEY_2 || touche == Keyboard.KEY_3 || touche == Keyboard.KEY_4 || touche == Keyboard.KEY_NUMPAD1 || touche ==  Keyboard.KEY_NUMPAD2 || touche ==  Keyboard.KEY_NUMPAD3 || touche ==  Keyboard.KEY_NUMPAD4)) {
                touche = env.getKey();
                env.advanceOneFrame();
            }

            // nettoie l'environnement du texte
            menuText.getText("Question").clean();
            menuText.getText("Jeu1").clean();
            menuText.getText("Jeu2").clean();
            menuText.getText("Jeu3").clean();
            menuText.getText("Jeu4").clean();

            // restaure la room du jeu
            env.setRoom(mainRoom);
            
            touche=checkNumpad(touche); //prise en compte du pavé numerique
            // et décide quoi faire en fonction de la touche pressée
            switch (touche) {
                // -----------------------------------------
                // Touche 1 : Commencer une nouvelle partie
                // -----------------------------------------                
                case Keyboard.KEY_1: // choisi un niveau et charge un mot depuis le dico
                    // .......... dico.******
                    initDico();
                    int niveau = getNiveau(); //on recupere le niveau sp4ouhaité
                    String mot = dico.getMotDepuisListeNiveaux(niveau); //on choisi un mot au hasard avec le niveau récupéré
                    //FIN DICO 

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");//on initalise un format de date
                    LocalDate date = LocalDate.now(); //on recupere la date du jour
                    // crée un nouvelle partie
                    partie = new Partie(formatter.format(date), mot, niveau);
         
                    // joue
                    joue(partie);
                    profil.ajouterPartie(partie);
                    profil.sauvegarder("Data/xml/profils/"+profil.getNom()+".xml");
                    playTheGame = MENU_VAL.MENU_JOUE;
                    break;

                // -----------------------------------------
                // Touche 2 : Charger une partie existante
                // -----------------------------------------                
                case Keyboard.KEY_2: // charge une partie existante
                //affichier liste des parties
                    int partieIndex= afficheParties();
                    if(partieIndex!=-1){
                        // Recupère le mot de la partie existante
                        // enregistre la partie dans le profil --> enregistre le profil
                        partie = profil.getParties().get(partieIndex);
                        joue(partie);
                        profil.ajouterPartie(partie);
                        profil.sauvegarder("Data/xml/profils/"+profil.getNom()+".xml");
                    }
                    playTheGame = MENU_VAL.MENU_JOUE;
                    break;

                // -----------------------------------------
                // Touche 3 : Sortie de ce jeu
                // -----------------------------------------                
                case Keyboard.KEY_3:
                    playTheGame = MENU_VAL.MENU_CONTINUE;
                    break;

                // -----------------------------------------
                // Touche 4 : Quitter le jeu
                // -----------------------------------------                
                case Keyboard.KEY_4:
                    playTheGame = MENU_VAL.MENU_SORTIE;
            }
        } while (playTheGame == MENU_VAL.MENU_JOUE);
        return playTheGame;
    }

    
    /** 
     * Pour que les chiffres du pavé numériques soient prise en compte
     * @param touche
     * @return int
     */
    //Fonction pour prendre en compte le paver numerique
    private int checkNumpad(int touche){
        if(touche==Keyboard.KEY_NUMPAD1){
            touche=Keyboard.KEY_1;
        }
        if(touche==Keyboard.KEY_NUMPAD2){
            touche=Keyboard.KEY_2;
        }
        if(touche==Keyboard.KEY_NUMPAD3){
            touche=Keyboard.KEY_3;
        }
        if(touche==Keyboard.KEY_NUMPAD4){
            touche=Keyboard.KEY_4;
        }
        if(touche==Keyboard.KEY_NUMPAD5){
            touche=Keyboard.KEY_5;
        }
        if(touche==Keyboard.KEY_NUMPAD6){
            touche=Keyboard.KEY_6;
        }
        return touche;
    }

    
    /** 
     * Vérifie qu'un profil existe et charge le profil
     * @param nom nom du profil
     * @return boolean, vrai si profil existe
     */
    private boolean loadProfil(String nom){
        boolean check=false;
        File fichierProfil = new File("Data/xml/profils/"+nom+".xml");
        if(fichierProfil.exists()){
            profil= new Profil("Data/xml/profils/"+nom+".xml");
            check=true;
        }
        else{
            System.out.println("Le profil n'existe pas");
        }
        return check;
    }

    
    /** 
     * Crée un profil si un profil n'existe pas
     * @param nom nom du profil a créer
     * @return boolean retourne vrai si profil n'existe pas
     */
    private boolean checkProfil(String nom){
        boolean check=false;
        File fichierProfil = new File("Data/xml/profils/"+nom+".xml");
        if(!fichierProfil.exists()){
            profil= new Profil(nom, "04/10/1998");
            check=true;
        }
        else{
            System.out.println("Le profil existe déjà");
        }
        return check;
    }

    
    /** 
     * Supprime un profil
     * @param nom
     * @return boolean
     */
    private boolean deleteProfil(String nom){
        boolean check=false;
        File fichierProfil = new File("Data/xml/profils/"+nom+".xml");
        if(fichierProfil.exists()){
            fichierProfil.delete();
            check=true;
        }
        else{
            System.out.println("Le profil existe déjà");
        }
        return check;
    }

     /** 
     * demande a l'utilisateur de saisir un mot et un niveau, puis ajoute ce mot dans le dictionnaire 
     * @param nom
     * @return boolean
     */
    private void menuAjout(){
        EditeurDico ed = new EditeurDico("Data/xml/dico.xml");
        String mot = lireTexte("ajoutMot"); //
        System.out.println("mot: "+mot);
        int niveau = getNiveau();
        System.out.println("niveau: "+niveau);
        // ed.ajouterMot(mot.toUpperCase(), niveau);
        ed.editer(mot, niveau);
        
    }
    
    /** 
     * @return MENU_VAL
     */
    private MENU_VAL menuPrincipal() {

        MENU_VAL choix = MENU_VAL.MENU_CONTINUE;
        String nomJoueur;

        // restaure la room du menu
        env.setRoom(menuRoom);

        menuText.getText("Question").display();
        menuText.getText("Principal1").display();
        menuText.getText("Principal2").display();
        menuText.getText("Principal3").display();
        menuText.getText("Principal4").display();
        menuText.getText("Principal5").display();
        menuText.getText("Principal6").display();
               
        // vérifie qu'une touche 1, 2 ou 3 est pressée
        int touche = 0;
        
        while (!(touche == Keyboard.KEY_NUMPAD6 || touche == Keyboard.KEY_6 ||touche == Keyboard.KEY_4 || touche == Keyboard.KEY_NUMPAD5 ||touche == Keyboard.KEY_5  ||touche == Keyboard.KEY_NUMPAD4 ||touche == Keyboard.KEY_1 || touche == Keyboard.KEY_2 || touche == Keyboard.KEY_3 || touche == Keyboard.KEY_NUMPAD1 || touche ==  Keyboard.KEY_NUMPAD2 || touche ==  Keyboard.KEY_NUMPAD3)) {
            touche = env.getKey();
            env.advanceOneFrame();
        }


        menuText.getText("Question").clean();
        menuText.getText("Principal1").clean();
        menuText.getText("Principal2").clean();
        menuText.getText("Principal3").clean();
        menuText.getText("Principal4").clean();
        menuText.getText("Principal5").clean();
        menuText.getText("Principal6").clean();


        touche=checkNumpad(touche); //prise en compte du pavé numerique
        // et décide quoi faire en fonction de la touche pressée
        switch (touche) {
            // -------------------------------------
            // Touche 1 : Charger un profil existant
            // -------------------------------------
            case Keyboard.KEY_1:
                // demande le nom du joueur existant
                nomJoueur = getNomJoueur();
                // // charge le profil de ce joueur si possible
                if(loadProfil(nomJoueur)){ 
                    choix = menuJeu();
                }
                 else {
                    choix = MENU_VAL.MENU_CONTINUE;//CONTINUE;
                }
                break;

            // -------------------------------------
            // Touche 2 : Créer un nouveau joueur
            // -------------------------------------
            case Keyboard.KEY_2:
                // demande le nom du nouveau joueur
                 nomJoueur = getNomJoueur();
                // // crée un profil avec le nom d'un nouveau joueur
                // profil = new Profil(nomJoueur);
                if(checkProfil(nomJoueur)){
                    choix=menuJeu();
                }
                else{
                    choix = MENU_VAL.MENU_CONTINUE;//CONTINUE;
                }
                // profil = new Profil(nomJoueur,"04-10-1998");
                break;

            // -------------------------------------
            // Touche 3 : Sortir du jeu
            // -------------------------------------
            case Keyboard.KEY_6:
            System.out.println("sortir");
                choix = MENU_VAL.MENU_SORTIE;
            break;
            case Keyboard.KEY_4:
                nomJoueur=getNomJoueur();
                if(deleteProfil(nomJoueur)){
                    System.out.println("Profil supprimé");
                }
                else{
                    System.out.println("profil n'existe pas");
                }
                choix = MENU_VAL.MENU_CONTINUE;
                break;
            case Keyboard.KEY_5:
                //ajout mot
                menuAjout();
                choix = MENU_VAL.MENU_CONTINUE;
                break;
            case Keyboard.KEY_3:
                Classement classement = new Classement();
                classement.toHTML();
                classement.affiche();
                choix = MENU_VAL.MENU_CONTINUE;
                break;
        }
        return choix;
    }

    
    /** 
     * Affiche le mot a orthographier pendant 5 secondes avec le decompte du chrono 
     * @param mot mot a afficher
     */
    public void wordPeek(String mot){
        Chronomètre chronoPeek=new Chronomètre(5000);
        chronoPeek.start();
        menuText.addText("Mot: "+mot, "peek", 200, 300);
        menuText.addText(chronoPeek.getRemaining()+" secondes", "chrono", 200, 280);
        menuText.getText("peek").display();
        menuText.getText("chrono").display();

        while(chronoPeek.remainsTime()){//tant quil reste du temps, on avance d'une frame et met a jour le timer 
            env.advanceOneFrame();
            menuText.getText("chrono").modifyTextAndDisplay(chronoPeek.getRemaining()+" secondes");
        }
        chronoPeek.stop();
        menuText.getText("peek").clean();
        menuText.getText("peek").destroy();
        menuText.getText("chrono").clean();
        menuText.getText("chrono").destroy();
    }

    /** 
     *Attends que l'utilisateur appuie sur la touche demandé, la méthode est utilisée par l'ecran de fin de partie
     * @param mot mot a afficher
     */
    protected void waitInput(){
        int touche = 0;
        while (!(touche == Keyboard.KEY_1 || touche == Keyboard.KEY_NUMPAD1)){
            touche = env.getKey();
            env.advanceOneFrame();
        }
    }
   
     /** 
     * Scan et gestion de collision lancé une fois par frame, chaque Lettre et comparé a la distance des autres lettres
    */
    public void collisionScan(){
        for (Letter lettre : lettres) {
            for (Letter lettre2 : lettres){
                if(!lettre.equals(lettre2) && !lettre.getFound() && !lettre2.getFound()){//pas de collision si la lettre est trouvée

                    double shortHitBox=7.0;
                    boolean condX = lettre.getX()>lettre2.getX()-shortHitBox&& lettre.getX()<lettre2.getX()+shortHitBox;//condiiton qui verifie la distance
                    boolean condY = lettre.getZ()>lettre2.getZ()-shortHitBox&& lettre.getZ()<lettre2.getZ()+shortHitBox;

                    if(condX&&condY){
                         /*les condition qui suivent
                         calculent  la position relative des cubes pour pouvoir les positionner dans le bon sens
                         */
                         if(lettre.getX()>lettre2.getX()){ //calcul de la position relative des cubes pour pouvoir les positionner dans le bon sens
                         
                            lettre.deplace(1,0 );
                            lettre2.deplace(-1,0);
                        }
                        else{
                            
                            lettre.deplace(-1,0 );
                            lettre2.deplace(1,0 );
                        }
                        if(lettre.getZ()>lettre2.getZ()){
                       
    
                            lettre.deplace(0,1 );
                            lettre2.deplace(0,-1 );
                        }
                        else{
                        

                            lettre.deplace(0,-1 );
                            lettre2.deplace(0,1 );
                        }
                    
                    }
                }
            }
        }
    }
    
    /** 
     * @param partie
     */
    public void joue(Partie partie) {

        // Instancie un Tux
        tux = new Tux(env, mainRoom);
        env.addObject(tux);

        //montagne = new Mountain(env, mainRoom);
        //env.addObject(montagne);
        
        //affichage 5 sec
        wordPeek(partie.getMot());

        // Ici, on peut ini     tialiser des valeurs pour une nouvelle partie
        démarrePartie(partie);

        // Boucle de jeu
        Boolean finished;
        finished = false;
        while (!finished) {

            

            // Contrôles des déplacements de Tux (gauche, droite, ...)
            tux.deplace(lettres);

            animLetters(); //animation idle des lettres
            collisionScan(); //scan de collision des lettres
            // Ici, on applique les regles
            appliqueRegles(partie);

            finished=isFinished();//récupre le flag de fin de partie

            // Contrôles globaux du jeu (sortie, ...)
            //1 is for escape key
            if (env.getKey() == 1 ) { //on place cette condition après  isFinished, car si la partie n'est pas fini, le flag finished est remi a false
                finished = true;
            }
            // Fait avancer le moteur de jeu (mise à jour de l'affichage, de l'écoute des événements clavier...)
            env.advanceOneFrame();
        }

        terminePartie(partie);
        
    }

    

     /** 
     * Initialisation du dictionnaire(parse dico.xml et cosntruit les listes de mot par niveau)
     */
    private void initDico(){
        try {
            dico.lireDictionnaire("Data/xml/dico.xml");
        }
        catch(Exception e){
            System.out.println(e);
        }

    }

    
    /** 
     * @return Env
     */
    protected Env getEnv(){
        return env;
    }

    
    /** 
     * @param lettre
     * @return double
     */
    private double distance(EnvNode lettre){
        double x = Math.pow(tux.getX()-lettre.getX(), 2);
        double y = Math.pow(tux.getX()-lettre.getX(), 2);
        return Math.sqrt(x+y);

    }

    
    /** 
     * inutilisé, demandé dans l'énoncé, on utilise la méthode collisopnScan a la placz
     * @param lettre
     * @return boolean
     */
    private boolean collision(Letter lettre){
        return distance(lettre)<3;
    }
    
    /** 
     * inutilisé on utilise la méthode collisopnScan a la placz
     * @param lettre 
     * @return boolean
     */
    private boolean nearCollision(Letter lettre){
        return distance(lettre)<7;
    }

  
    
    /** 
     * Génère un entier entre min et max
     * @param min
     * @param max
     * @return int
     * @author "mkyong" https://mkyong.com/java/java-generate-random-integers-in-a-range/
     */
    private static int randomInRange(int min, int max) {
        //géneration d'entier aléatoir, author: mkyon https://mkyong.com
		if (min >= max) {
			throw new IllegalArgumentException("max > min");
		}
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
         
    
    /** 
     * Place les lettres (instance de Letter) sdans l'environement, le positionnement est aléatoie
     * @param mot mot de la partie
     */
    public void spawnLetters(String mot){
        int x,y;
        for(int i =0; i<mot.length(); i++){
           
            do{
                x=randomInRange(3, 97);
                y=randomInRange(3, 97);
            }
            while(x>30 && x<73 && y>80 && y<100);
            // Letter lettre = new Letter(mot.charAt(i), randomInRange(0, 100), randomInRange(0, 100));
            lettres.add(new Letter(mot.charAt(i), x, y));
        }

        for (Letter letter : lettres) {
            env.addObject(letter);
        }
    }

    /** 
     * Enlève les instances Letter de l'environement
    */
    public void removeLetters(){
        for (Letter letter: lettres){
            env.removeObject(letter);
        }
        lettres = new ArrayList<Letter>();
    }
    
  
    /** 
     * Appele la methode d'animation idle pour toutes les instances de Letter
    */  
    public void animLetters(){
        
        for (Letter letter : lettres) {
                letter.idle();   
        }
    }

    /** 
     * @param initDico(
     * @return boolean
     */
    protected abstract boolean isFinished(); //indique a Jeu que la partie est fini(fin du chrono ou lettres trouvées)

    
    /** 
     * @param initDico(
     */
    protected abstract void démarrePartie(Partie partie);

    
    /** 
     * @param initDico(
     */
    protected abstract void appliqueRegles(Partie partie);

    
    /** 
     * @param initDico(
     */
    protected abstract void terminePartie(Partie partie);


}
