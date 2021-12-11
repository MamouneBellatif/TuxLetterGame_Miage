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
    private Letter letter;
    private Profil profil;
    private final Dico dico;
    protected EnvTextMap menuText;                         //text (affichage des texte du jeu)
    ArrayList<Letter> lettres;
    private Mountain montagne;
    
    
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


    // fourni
    private String getNomJoueur() {
        String nomJoueur = "";
        menuText.getText("NomJoueur").display();
        nomJoueur = menuText.getText("NomJoueur").lire(true);
        menuText.getText("NomJoueur").clean();
        return nomJoueur;
    }

    //Lis texte entrée par utilisaeur avec un message affiché
    private String lireTexte(String message){
        String input="";
        menuText.getText(message).display();
        // input = menuText.getText(message).lire(true);
        input = menuText.getText(message).lire(true);
        menuText.getText(message).clean();
        return input;
    }
    private String lireEntier(String message){
        String input="";
        menuText.getText(message).display();
        // input = menuText.getText(message).lire(true);
        input = menuText.getText(message).lireNombre(true);
        menuText.getText(message).clean();
        return input;
    }

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
            
        // } while((niveau<1 || niveau >5) && !success);
        } while(niveau<1 || niveau >5 || !success);
        return niveau;
    }

    private int getIndex(int max) throws NumberFormatException{
        int index=0;
        String message ="index";
        String input="";
        Boolean success=false;
        do{
            try {
                index=Integer.parseInt(lireTexte(message));
                success=true;
            }
            catch (NumberFormatException e){
                message="indexErreur";
                success=false;
            }
            
        // } while((niveau<1 || niveau >5) && !success);
        } while(index<0 || index >max || !success);
        return index;
    
    }
//a

    public int affichePartiesOld(){

        //alterntive si pas de place, pages ou naviguer entre partie
        int nbParties = profil.getParties().size();
        
        for (Partie partie : profil.getParties()) {
            //index * constante / taille
            int index =profil.getParties().indexOf(partie);
            menuText.addText(index+"."+partie.toString(), "partie"+index,200, 80+(index*200)/nbParties);
            menuText.getText("partie"+index).display();
        }

        int choixPartie = getIndex(nbParties-1);

        for (int i = 0; i < nbParties; i++) {
            menuText.getText("partie"+i).clean();
        }
        return choixPartie;
    }
    public int afficheParties(){
        //alterntive si pas de place, pages ou naviguer entre partie
        ArrayList<Partie> alPartie= new ArrayList<Partie>();
        int nbParties = profil.getParties().size();
        
        for (Partie partie : profil.getParties()) {
            //index * constante / taille
            alPartie.add(partie);
        }

        menuText.addText("(1.jouer 2.partie précedente 3.partie suivante 4.revenir","partieCtrl",100,200);
        menuText.getText("partieCtrl").display();

        
        int index=0;
        boolean menu=true;
        menuText.addText(alPartie.get(index).toString(), "partieChoix",100,300);
        //index%nbPartie
        int moduloIndex;
        do{
            moduloIndex=Math.floorMod(index, alPartie.size()); //le modulo en java donne des resultats negatif, cette fonction est plus adaptée
            // moduloIndex=Math.abs(index%alPartie.size());
            System.out.println("index: "+moduloIndex);
            menuText.getText("partieChoix").modifyTextAndDisplay((moduloIndex+1)+"/"+alPartie.size()+alPartie.get(moduloIndex).toString()); //navigue entre les parti et affiche le l'index modulo le nombre de partie pour ne pas sortir de la liste
            // menuText.getText("partieChoix").display();
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

        int choixPartie = moduloIndex;

        menuText.getText("partieChoix").clean();
        menuText.getText("partieCtrl").clean();
        return choixPartie;
    }
    
    public void hiScore(){

    }
    // fourni, à compléter
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
                    int niveau = getNiveau();
                    String mot = dico.getMotDepuisListeNiveaux(niveau);
                    //FIN DICO 

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate date = LocalDate.now();
                    // crée un nouvelle partie
                    partie = new Partie(formatter.format(date), mot, niveau);
         
                    // joue
                    joue(partie);
                    // enregistre la partie dans le profil --> enregistre le profil
                    // .......... profil.******
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
                        // profil.ajouterPartie(partie);
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
        return touche;
    }

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

    private void menuAjout(){
        EditeurDico ed = new EditeurDico("Data/xml/dico.xml");
        String mot = lireTexte("ajoutMot"); //
        System.out.println("mot: "+mot);
        int niveau = getNiveau();
        System.out.println("niveau: "+niveau);
        // ed.ajouterMot(mot.toUpperCase(), niveau);
        ed.editer(mot, niveau);
        
    }
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

    public void wordPeek(String mot){
        Chronomètre chronoPeek=new Chronomètre(5000);
        chronoPeek.start();
        menuText.addText("Mot: "+mot, "peek", 200, 300);
        menuText.addText(chronoPeek.getRemaining()+" secondes", "chrono", 200, 280);
        menuText.getText("peek").display();
        menuText.getText("chrono").display();

        while(chronoPeek.remainsTime()){
            env.advanceOneFrame();
            menuText.getText("chrono").modifyTextAndDisplay(chronoPeek.getRemaining()+" secondes");
        }
        chronoPeek.stop();
        menuText.getText("peek").clean();
        menuText.getText("peek").destroy();
        menuText.getText("chrono").clean();
        menuText.getText("chrono").destroy();
    }

    protected void waitInput(){
        int touche = 0;
        while (!(touche == Keyboard.KEY_1 || touche == Keyboard.KEY_NUMPAD1)){
            touche = env.getKey();
            env.advanceOneFrame();
        }
    }
   
    public void joue(Partie partie) {

        // Instancie un Tux
        tux = new Tux(env, mainRoom);
        env.addObject(tux);

        montagne = new Mountain(env, mainRoom);
        env.addObject(montagne);
        
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
            animLetters();
            collisionScan();
            // Ici, on applique les regles
            appliqueRegles(partie);
            // appliqueRegles(partie);
            finished=isFinished();
            // Contrôles globaux du jeu (sortie, ...)
            //1 is for escape key
            if (env.getKey() == 1 ) {
                finished = true;
            }
            // Fait avancer le moteur de jeu (mise à jour de l'affichage, de l'écoute des événements clavier...)
            env.advanceOneFrame();
        }

        // Ici on peut calculer des valeurs lorsque la partie est terminée
        terminePartie(partie);
        // profil.ajouterPartie(partie);
        // profil.sauvegarder("Data/xml/"+profil.getNom()+".xml");
    }

    protected abstract boolean isFinished(); //indique a Jeu que la partie est fini(fin du chrono ou lettres trouvées)

    protected abstract void démarrePartie(Partie partie);

    protected abstract void appliqueRegles(Partie partie);

    protected abstract void terminePartie(Partie partie);

    private void initDico(){
        try {
            // dico.lireDictionnaireDOM("Data/xml/profils/", "dico.xml");
            dico.lireDictionnaire("Data/xml/dico.xml");
        }
        catch(Exception e){
            
        }
        // dico.ajouteMotADico(3, "Qotsa");
        // dico.ajouteMotADico(3, "Mamoune");
    }

    protected Env getEnv(){
        return env;
    }

    private double distance(EnvNode lettre){
        double x = Math.pow(tux.getX()-lettre.getX(), 2);
        double y = Math.pow(tux.getX()-lettre.getX(), 2);
        return Math.sqrt(x+y);

    }

    private boolean collision(Letter lettre){
        return distance(lettre)<3;
    }
    private boolean nearCollision(Letter lettre){
        return distance(lettre)<7;
    }

    Letter collide;
    double slowSpeed;
    boolean onTop=false;
    public void letterCollisionScan(double x, double z, ArrayList<Letter> lettres){
        for (Letter lettre : lettres) {
            double shortHitBox=4.0;
            double longHitBox =9.0;

            boolean condXR = tux.getX()>lettre.getX()-longHitBox && tux.getX()<lettre.getX()+longHitBox;
            boolean condYR = tux.getZ()>lettre.getZ()-longHitBox && tux.getZ()<lettre.getZ()+longHitBox; //condition pour rotation si on est loin mais assez proche

            
            if (nearCollision(lettre) && lettre.getScale()!=4){ //si on est proche mais loins
                collide=lettre; //on garde en mémoire le bloc de collsision

                slowSpeed=0.25;
                
                if(tux.getY()>=7 && !env.getKeyDown(Keyboard.KEY_SPACE)){ //essayer de debloquer le saut
                    tux.setY(13);
                    // setY(getScale() * 3.1);
                    onTop=true;
                }

                else {
                    if(env.getKeyDown(Keyboard.KEY_RCONTROL) || env.getKeyDown(Keyboard.KEY_LCONTROL)){ //si touche control tux ramasse la lettre
                        tux.lift(lettre);
                        slowSpeed=0.25;
                    }
                    else {
                        lettre.setY(tux.getScale() * 1.1); //si tux ne porte pas un bloc, le bloc revient a sa hauteur initiale
                        // slowSpeed=0.0;
                    }
                    
                    if (collision(lettre)) { //si on est proche
                        slowSpeed=1.5; //coefficient qui ralenti Tux lorsqu'il pousse un bloc
                        tux.testeRoomCollision(lettre,  x, z);
                        lettre.deplace(x, z);
                    
                    }
                    else { //loin
                        // slowSpeed=0.0; //on reset la vitesse de Tux
                        if(tux.getX()>lettre.getX()){ //calcul du bon sens de la rotztion
                            lettre.setRotateY(lettre.getRotateY()-(z*2));
                            lettre.setX(lettre.getX()-0.02);
                        }
                        else{
                            lettre.setRotateY(lettre.getRotateY()+(z*2));
                            lettre.setX(lettre.getX()+0.02);
    
                        }
                        if(tux.getZ()>lettre.getZ()){
                            lettre.setRotateY(lettre.getRotateY()+(x*2));
                            lettre.setZ(lettre.getZ()-0.02);
    
                        }
                        else{
                            lettre.setRotateY(lettre.getRotateY()-(x*2));
                            lettre.setZ(lettre.getZ()+0.02);
                        }
                    }
                }
                
            }

            else { //parce qu'on boucle les autres bloc le desactive a chaque fois
               
                if(onTop && lettre.equals(collide)){ //si on quitte le bloc sur lequel on est monté on revient a notre hauteur initiale
                    // System.out.println("collide exit");
                    tux.setY(tux.getScale() * 1.1);
                    onTop=false;
                  }
                if(lettre.equals(collide)){
                    slowSpeed=0.0;

                }

            }
            // slowSpeed=0.0;

        }
    }
  
    private static int randomInRange(int min, int max) {
        //géneration d'entier aléatoir, author: mkyon https://mkyong.com
		if (min >= max) {
			throw new IllegalArgumentException("max > min");
		}
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
         
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
    public void removeLetters(){
        for (Letter letter: lettres){
            env.removeObject(letter);
        }
        lettres = new ArrayList<Letter>();
    }
    
    public void animLetters(){
        
        for (Letter letter : lettres) {
                letter.idle();   
        }
    }

    public void collisionScan(){
        //une fois par frame, chaque lettre copare sa position avec les autres lettres et bouge en consequence
        for (Letter lettre : lettres) {
            for (Letter lettre2 : lettres){
                if(!lettre.equals(lettre2) && !lettre.found && !lettre2.found){

                    double shortHitBox=7.0;
                    boolean condX = lettre.getX()>lettre2.getX()-shortHitBox&& lettre.getX()<lettre2.getX()+shortHitBox;
                    boolean condY = lettre.getZ()>lettre2.getZ()-shortHitBox&& lettre.getZ()<lettre2.getZ()+shortHitBox;

                    if(condX&&condY){
                         if(lettre.getX()>lettre2.getX()){ //calcul du bon sens de la rotztion
                            // lettre.setX(lettre.getX()+1);
                            // lettre2.setX(lettre2.getX()-1);
                            lettre.deplace(1,0 );
                            lettre2.deplace(-1,0);
                        }
                        else{
                            // lettre.setX(lettre.getX()-1);
                            // lettre2.setX(lettre2.getX()+1);
                            lettre.deplace(-1,0 );
                            lettre2.deplace(1,0 );
                        }
                        if(lettre.getZ()>lettre2.getZ()){
                            // lettre.setZ(lettre.getZ()+1);
                            // lettre2.setZ(lettre2.getZ()-1);
    
                            lettre.deplace(0,1 );
                            lettre2.deplace(0,-1 );
                        }
                        else{
                            // lettre.setZ(lettre.getZ()-1);
                            // lettre2.setZ(lettre2.getZ()+1);

                            lettre.deplace(0,-1 );
                            lettre2.deplace(0,1 );
                        }
                    
                    }
                }
            }
        }
    }

}
