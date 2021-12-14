package game;
import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import env3d.Env;
import env3d.advanced.EnvNode;

public class Tux extends EnvNode{
    
    Env env;
    Room room;


    private double slowSpeed; //coeficient pour ralentir Tux en cas de collision
    private boolean onTop; //flag pour indiquer que Tux est audessus d'une lettre
    private Letter collide; //tampon pour garder en mémoire la lettre qui a provoquée une collision
    private Letter lastLift; //tampon pour garder en mémoire la lettre soulevée par tux
    private int frame; //image clée de réference pour le saut
    private int flipFrame;//image clé d'animation réference pour le saut spécial
    private int idleFrame;//image clé d'animation 
    private double vecX; //déplacement de tux lors de la frame précedente 
    private double vecY; //déplacement de tux lors de la frame précédente 
    
    public Tux(Env env, Room room) {

        this.env = env;
        this.room = room;
        this.onTop = false;

        setScale(4.0);
        setX(room.getWidth()/2.0);// positionnement au milieu de la largeur de la room
        setY(getScale() * 1.1); // positionnement en hauteur basé sur la taille de Tux
        setZ(room.getDepth()/2); // positionnement au milieu de la profondeur de la room
        setTexture("models/tux/tux_angry.png");
        setModel("models/tux/tux.obj");        

        slowSpeed=0.0;

        collide=null;
        lastLift=null;

        frame=0;
        flipFrame=0;
        idleFrame=0;
    }

    
    /** 
     * test la collision de objet avec les murs
     * @param obj objet a tester
     * @param x position x de tux
     * @param z position y de tux
     * @return boolean si il ya collision
     */
    public boolean testeRoomCollision(EnvNode obj, double x,double z){
        if (obj.getZ()<=5){
            obj.setZ(3);
        }
        if (obj.getZ()>=room.getDepth()-5){
            obj.setZ(room.getDepth()-3);
        }
        if (obj.getX()<=5){
            obj.setX(3);
        }
        if (obj.getX()>=room.getWidth()-5){
            obj.setX(room.getWidth()-3);
        }
        
        return true;
        }
    



    
    /** 
     * evalue la distance avec obj
     * @param obj
     * @return double
     */
    public double distance(EnvNode obj) {
        double x = Math.pow(getX()-obj.getX(), 2);
        double y = Math.pow(getX()-obj.getX(), 2);
        return Math.sqrt(x+y);
    }

    /** 
     * Scan et gestion des collisions de tux avec les lettres
     * @param x deplacement de tux lors de la frame actuelle
     * @param z pareil que x
     * @param lettres liste des lettres (instances de Letter)
     */
    public void testLetterCollision(double x, double z, ArrayList<Letter> lettres){
        
        for (Letter lettre : lettres) {
            testeRoomCollision(lettre,  x, z);
            double shortHitBox=4.0;
            double longHitBox =9.0;

            //Tux est très proche de lettre
            boolean condX = getX()>lettre.getX()-shortHitBox&& getX()<lettre.getX()+shortHitBox; 
            boolean condY = getZ()>lettre.getZ()-shortHitBox&& getZ()<lettre.getZ()+shortHitBox;

            //Tux est assez proche de lettre
            boolean condXR = getX()>lettre.getX()-longHitBox && getX()<lettre.getX()+longHitBox;
            boolean condYR = getZ()>lettre.getZ()-longHitBox && getZ()<lettre.getZ()+longHitBox; //condition pour rotation si on est loin mais assez proche

            
            if (condXR && condYR && lettre.getScale()==4){ //si on est proche mais loins get scale verifie que tux ne bouge pas une lettre accrochée sur le mur
                collide=lettre; //on garde en mémoire le bloc de collsision

                slowSpeed=0.25; //coeficient de ralentissement
                
                if(getY()>=7 && !env.getKeyDown(Keyboard.KEY_SPACE) && !env.getKeyDown(Keyboard.KEY_B)){ //si tux saute 
                // if(getY()>=7 && frame!=0 && flipFrame!=0){ //essayer de debloquer le saut
                    setY(13); //on met tux au dessus de la lettre
                    onTop=true; //flag de tux sur une lettre
                }

                else if(!onTop){//si tux n'est pas sur une lettre, on ne veut pas faire bouger la lettre
                    if(env.getKeyDown(Keyboard.KEY_RCONTROL) || env.getKeyDown(Keyboard.KEY_LCONTROL)){ //si touche control tux ramasse la lettre
                        if(lastLift==null || lastLift.equals(lettre) ){
                            lift(lettre);
                            lastLift=lettre;
                        }
                        slowSpeed=0.25;
                    }
                    else {
                        lastLift=null;
                        lettre.setY(getScale() * 1.1); //si tux ne porte pas un bloc, le bloc revient a sa hauteur initiale
                        // slowSpeed=0.0;
                    }
                    
                    if (condX && condY ) { //si on est proche
                        slowSpeed=1.25; //coefficient qui ralenti Tux lorsqu'il pousse un bloc
                        // testeRoomCollision(lettre,  x, z);
                        lettre.deplace(x, z);
                    
                    }
                    else if(isMoving){ //loin
                        // slowSpeed=0.0; //on reset la vitesse de Tux
                        if(getX()>lettre.getX()){ //calcul du bon sens de la rotztion
                            lettre.setRotateY(lettre.getRotateY()-(z*3));
                            lettre.setX(lettre.getX()-0.1);
                        }
                        else{
                            lettre.setRotateY(lettre.getRotateY()+(z*3));
                            lettre.setX(lettre.getX()+0.1);
    
                        }
                        if(getZ()>lettre.getZ()){
                            lettre.setRotateY(lettre.getRotateY()+(x*3));
                            lettre.setZ(lettre.getZ()-0.1);
    
                        }
                        else{
                            lettre.setRotateY(lettre.getRotateY()-(x*2));
                            lettre.setZ(lettre.getZ()+0.2);
                        }
                    }
                }
                
            }
            else { //parce qu'on boucle les autres bloc le desactive a chaque fois
               
                if(onTop && lettre.equals(collide)){ //si on quitte le bloc sur lequel on est monté on revient a notre hauteur initiale
                    // System.out.println("collide exit");
                    setY(getScale() * 1.1);
                    onTop=false;
                  }
                if(lettre.equals(collide)){
                    slowSpeed=0.0;

                }

            }
            
        }
    }


    
    /** 
     * Gère la collision avec les murs de Room
     * @param x position x
     * @param z position z
     * @return boolean vrai si collision
     */
    public boolean testeRoomCollision( double x,double z){
        if (this.getZ()<=1){
            setZ(3);
        }
        if (this.getZ()>=room.getDepth()-1){
            setZ(room.getDepth()-3);
        }
        if (this.getX()<=1){
            setX(3);
        }
        if (this.getX()>=room.getWidth()-1){
            setX(room.getWidth()-3);
        }
        if(this.getY()<(this.getScale() * 1.1)){
            setY(getScale()*1.1);
        }
        return true;
    }

    
    /** 
     * Génère un entier entre min et max
     * @param min
     * @param max
     * @return int
     * @author "mkyong" https://mkyong.com/java/java-generate-random-integers-in-a-range/
     */
    private static int randomInRange(int min, int max) {
		if (min >= max) {
			throw new IllegalArgumentException("max > min");
		}
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
    

    
    /** 
     * Permet a tux de soulever et deplacer une lettre
     * @param lettre instance de lettre a soulever
     */
    public void lift(Letter lettre){
            lettre.setY(getY()+4); //le bloc est levé
            if(getRotateY()>0 && getRotateY()<180){ //si tux est orienté a droite, la lettre est portée a droite
                lettre.setX(this.getX()+5);//la position de la lettre est calée sur Tux
            }
            else {
                lettre.setX(this.getX()-5);
            }

            if(getRotateY()>90 && getRotateY()<270){
                lettre.setZ(this.getZ()-5);
                lettre.setX(lettre.getX());

            }
            else {
                lettre.setZ(this.getZ()+5);
                lettre.setX(lettre.getX());
            }
            testeRoomCollision(lettre,0,0); // on teste la collision de la lettre avec les murs que si Tux deplace une lettre pour eviter d'appeler la méthode a chaque frame

            
        
    }


    

    /** 
     * Saut spécial
     * Utilise un entier flipFrame comme image clé comme réference pour l'animation, on augmente la position 
     * vertical pour 15 frame et on la remet a la posiiton de base pour 15 frames et on reinitialise l'image clée
     */
    public void backflip(){
        if (flipFrame<15){
            setY(getY()+1);
        }
        else {
            setY(getY()-1);
        }
        setRotateX(getRotateX()-12);
        flipFrame++;
        if (flipFrame >= 30){
            flipFrame=0;
        }
    }


  

    /** 
     * animation de saut 
     * Utilise un entier flipFrame comme image clé comme réference pour l'animation, on augmente la position 
     * vertical pour 15 frame et on la remet a la posiiton de base pour 15 frames et on reinitialise l'image clée
     */
    public void jump(){
        if(env.getKeyDown(Keyboard.KEY_F1)){
            setY(getY()+0.5); //code secret pour voler //enlever
        }
        else {
            if (frame<15){
                setY(getY()+0.75);
            }
            else {
                setY(getY()-0.75);
            }
            frame++;
            if (frame >= 30){
                frame=0;
            }
        }
        
    }

    
    /** 
     * animation lorsque tux est au dessus d'une lettre 
     */
    public void idle(){
        if (idleFrame<15){
            setY(getY()+0.2);
        }
        else {
            setY(getY()-0.2);
        }
        idleFrame++;
        if (idleFrame >= 30){
            idleFrame=0;
        }
    }


    boolean isMoving=false;
    
    /** 
     * Déplacement de tux
     * @param lettres liste d'instance de Letter, sert au test de collision car c'est cette méthode qui appelle le scan de collision avec une lettre
     */
    public void deplace(ArrayList<Letter> lettres){ //ajouter collision

        testeRoomCollision(getX(), getZ());
        testLetterCollision(vecX, vecY, lettres);
        

        isMoving=false;
        if ((env.getKeyDown(Keyboard.KEY_SPACE) && (!env.getKeyDown(Keyboard.KEY_RCONTROL) || env.getKeyDown(Keyboard.KEY_LCONTROL))  || frame!=0)){
            jump();
        }

        if ((env.getKeyDown(Keyboard.KEY_B)) || flipFrame!=0){ //saut spécial
            backflip();
        }

        if (env.getKeyDown(Keyboard.KEY_D) && env.getKeyDown(Keyboard.KEY_S) || (env.getKeyDown(Keyboard.KEY_RIGHT)&&  env.getKeyDown(Keyboard.KEY_DOWN)) ){
                this.setRotateY(45);
                this.setX(this.getX() + 0.75 - slowSpeed);
                this.setZ(this.getZ() + 0.75 - slowSpeed);
                vecX=0.75;
                vecY=0.75;
                isMoving=true;
        }

        else if (env.getKeyDown(Keyboard.KEY_D) && env.getKeyDown(Keyboard.KEY_Z) || (env.getKeyDown(Keyboard.KEY_RIGHT)&&  env.getKeyDown(Keyboard.KEY_UP))){
                this.setRotateY(135);
                this.setX(this.getX() + 0.75 - slowSpeed);
                this.setZ(this.getZ() - 0.75 + slowSpeed);
                vecX=0.75;
                vecY=-0.75;
                isMoving=true;
           
        }
        else if (env.getKeyDown(Keyboard.KEY_Q) && env.getKeyDown(Keyboard.KEY_Z) ||( env.getKeyDown(Keyboard.KEY_LEFT)&&  env.getKeyDown(Keyboard.KEY_UP))){
                this.setRotateY(225);
                this.setX(this.getX() - 0.75 + slowSpeed);
                this.setZ(this.getZ() - 0.75 + slowSpeed);
                vecX=-0.75;
                vecY=-0.75;
            isMoving=true;
        }
        else if (env.getKeyDown(Keyboard.KEY_Q) && env.getKeyDown(Keyboard.KEY_S) || (env.getKeyDown(Keyboard.KEY_LEFT) && env.getKeyDown(Keyboard.KEY_DOWN))){
                this.setRotateY(-45);
                this.setX(this.getX() - 0.75 + slowSpeed);
                this.setZ(this.getZ() + 0.75 - slowSpeed);
                vecX=-0.75;
                vecY=+0.75;
             isMoving=true;
        }
        else {
            if (env.getKeyDown(Keyboard.KEY_Z) || env.getKeyDown(Keyboard.KEY_UP)) { // Fleche 'haut' ou Z
            // Haut
            
           

                    this.setRotateY(180);
                    this.setZ(this.getZ() - 1.0 + slowSpeed);
                    vecX=0;
                    vecY=-1;
                isMoving=true;
           
       }
        if (env.getKeyDown(Keyboard.KEY_Q) || env.getKeyDown(Keyboard.KEY_LEFT)) { // Fleche 'gauche' ou Q
            // Gauche
            

                this.setRotateY(270);
                this.setX(this.getX() - 1.0 + slowSpeed);
                vecX=-1;
                vecY=0;
             isMoving=true;

        }

        if (env.getKeyDown(Keyboard.KEY_S) || env.getKeyDown(Keyboard.KEY_DOWN)) { // Fleche 'bas' ou s
            // BAS
            // ...
            // ...
          

                this.setRotateY(0);
                this.setZ(this.getZ() + 1.0 - slowSpeed);
                vecX=0;
                vecY=1;
            isMoving=true;

          }

        if (env.getKeyDown(Keyboard.KEY_D) || env.getKeyDown(Keyboard.KEY_RIGHT)) { // Fleche 'DROITE' ou D
        // DROITE
        // ...²
        // ...
               isMoving=true;
                this.setRotateY(90);
                this.setX(this.getX() + 1.0 - slowSpeed);
                vecX=1;
                vecY=0;
        }

        }
        
    }
  
}