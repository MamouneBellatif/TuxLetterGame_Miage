package game;
import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import env3d.Env;
import env3d.advanced.EnvNode;

public class Tux extends EnvNode{
    
    Env env;
    Room room;
    double slowSpeed;    
    public Tux(Env env, Room room) {

        this.env = env;
        this.room = room;
       

        setScale(4.0);
        setX(room.getWidth()/2.0);// positionnement au milieu de la largeur de la room
        setY(getScale() * 1.1); // positionnement en hauteur basé sur la taille de Tux
        setZ(room.getDepth()/2); // positionnement au milieu de la profondeur de la room
        // setTexture("models/tux ...");
        setTexture("models/tux/tux_angry.png");
        setModel("models/tux/tux.obj");        

        slowSpeed=0.0;
    }

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
    
    boolean onTop=false;
    double collideX=0.0;
    double collideZ=0.0;


    public double distance(EnvNode obj) {
        double x = Math.pow(getX()-obj.getX(), 2);
        double y = Math.pow(getX()-obj.getX(), 2);
        return Math.sqrt(x+y);
    }
    Letter collide=null;
    Letter lastLift=null;
    public void testLetterCollision(double x, double z, ArrayList<Letter> lettres){
        
        for (Letter lettre : lettres) {
            testeRoomCollision(lettre,  x, z);
            double shortHitBox=4.0;
            double longHitBox =9.0;

            boolean condX = getX()>lettre.getX()-shortHitBox&& getX()<lettre.getX()+shortHitBox;
            boolean condY = getZ()>lettre.getZ()-shortHitBox&& getZ()<lettre.getZ()+shortHitBox;

            boolean condXR = getX()>lettre.getX()-longHitBox && getX()<lettre.getX()+longHitBox;
            boolean condYR = getZ()>lettre.getZ()-longHitBox && getZ()<lettre.getZ()+longHitBox; //condition pour rotation si on est loin mais assez proche

            
            if (condXR && condYR && lettre.getScale()==4){ //si on est proche mais loins
                collide=lettre; //on garde en mémoire le bloc de collsision

                slowSpeed=0.25;
                
                if(getY()>=7 && !env.getKeyDown(Keyboard.KEY_SPACE) && !env.getKeyDown(Keyboard.KEY_B)){ //essayer de debloquer le saut
                // if(getY()>=7 && frame!=0 && flipFrame!=0){ //essayer de debloquer le saut
                    setY(13);
                    onTop=true;
                }

                else if(!onTop){
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
            
            // slowSpeed=0.0;

        }
    }


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
        // return (this.getZ()<1 || this.getZ()>room.getDepth()-1 || this.getX() < 1 || this.getX() > room.getWidth()-1);
    }

    /* oX orientationX oY orientationY*/
    private static int randomInRange(int min, int max) {
		if (min >= max) {
			throw new IllegalArgumentException("max > min");
		}
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
    

    public void lift(Letter lettre){
            lettre.setY(getY()+4); //le bloc est levé
            if(getRotateY()>0 && getRotateY()<180){ //si tux est orienté a droite, la lettre est portée a droite
                lettre.setX(this.getX()+5);
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
            testeRoomCollision(lettre,0,0);

            
        
    }


    int flipFrame=0;
    public void backflip(){
        if (flipFrame<15){
            // setY(getY()+0.75);
            setY(getY()+1);
        }
        else {
            setY(getY()-1);
            // setY(getY()-0.75);
        }
        setRotateX(getRotateX()-12);
        flipFrame++;
        if (flipFrame >= 30){
            flipFrame=0;
        }
    }
  
    int frame=0;
    boolean doubleJump=false;
    public void jump(){
        // env.getKeyDown(Keyboard.)
        if(env.getKeyDown(Keyboard.KEY_F1)){
            setY(getY()+0.5); //code secret pour voler
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
            // else if((frame>5 && frame <30 && env.getKeyDown(Keyboard.KEY_SPACE)) || flipFrame!=0){
            //     backflip();
            // }
        }
        
    }

    int idleFrame=0;
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
    double vecX;
    double vecY;

    boolean isMoving=false;
    public void deplace(ArrayList<Letter> lettres){ //ajouter collision
        // System.out.println("onTop: "+onTop);
        // System.out.println("X: "+getX()+" Z: "+getZ() +" Y: "+getY());
        // System.out.println("SlowSpeed: "+slowSpeed);
        testeRoomCollision(getX(), getZ());
        testLetterCollision(vecX, vecY, lettres);
        

        // if(onTop){
        //     idle();
        // }
        // if ((env.getKeyDown(Keyboard.KEY_SPACE)  || frame!=0 ) && (!onTop)){
        isMoving=false;
        if ((env.getKeyDown(Keyboard.KEY_SPACE) && (!env.getKeyDown(Keyboard.KEY_RCONTROL) || env.getKeyDown(Keyboard.KEY_LCONTROL))  || frame!=0)){
            jump();
            // if((env.getKeyDown(Keyboard.KEY_SPACE) && frame!=0)||){

            // } 
        }
        if (doubleJump && env.getKeyDown(Keyboard.KEY_SPACE)){
            
        }
        if ((env.getKeyDown(Keyboard.KEY_B)) || flipFrame!=0){
            backflip();
        }
        if (env.getKeyDown(Keyboard.KEY_D) && env.getKeyDown(Keyboard.KEY_S) || (env.getKeyDown(Keyboard.KEY_RIGHT)&&  env.getKeyDown(Keyboard.KEY_DOWN)) ){
                this.setRotateY(45);
                this.setX(this.getX() + 0.75 - slowSpeed);
                this.setZ(this.getZ() + 0.75 - slowSpeed);
                vecX=0.75;
                vecY=0.75;
                // collisonLettre(true,0.75, 0.75, lettres);
                isMoving=true;
        }

        else if (env.getKeyDown(Keyboard.KEY_D) && env.getKeyDown(Keyboard.KEY_Z) || (env.getKeyDown(Keyboard.KEY_RIGHT)&&  env.getKeyDown(Keyboard.KEY_UP))){
                this.setRotateY(135);
                this.setX(this.getX() + 0.75 - slowSpeed);
                this.setZ(this.getZ() - 0.75 + slowSpeed);
                vecX=0.75;
                vecY=-0.75;
                isMoving=true;
                // collisionLettre(true, 0.75, -0.75, lettres);
           
        }
        else if (env.getKeyDown(Keyboard.KEY_Q) && env.getKeyDown(Keyboard.KEY_Z) ||( env.getKeyDown(Keyboard.KEY_LEFT)&&  env.getKeyDown(Keyboard.KEY_UP))){
                this.setRotateY(225);
                this.setX(this.getX() - 0.75 + slowSpeed);
                this.setZ(this.getZ() - 0.75 + slowSpeed);
                vecX=-0.75;
                vecY=-0.75;
                // collisionLettre(true, -0.75, -0.75, lettres);
            isMoving=true;
        }
        else if (env.getKeyDown(Keyboard.KEY_Q) && env.getKeyDown(Keyboard.KEY_S) || (env.getKeyDown(Keyboard.KEY_LEFT) && env.getKeyDown(Keyboard.KEY_DOWN))){
                this.setRotateY(-45);
                this.setX(this.getX() - 0.75 + slowSpeed);
                this.setZ(this.getZ() + 0.75 - slowSpeed);
                vecX=-0.75;
                vecY=+0.75;
                // collisionLettre(true, -0.75, 0.75, lettres);
             isMoving=true;
        }
        else {
            if (env.getKeyDown(Keyboard.KEY_Z) || env.getKeyDown(Keyboard.KEY_UP)) { // Fleche 'haut' ou Z
            // Haut
            
           

                    this.setRotateY(180);
                    this.setZ(this.getZ() - 1.0 + slowSpeed);
                    vecX=0;
                    vecY=-1;
                    // collisionLettre(false, 0.0, -1.0, lettres);
                isMoving=true;
           
       }
        if (env.getKeyDown(Keyboard.KEY_Q) || env.getKeyDown(Keyboard.KEY_LEFT)) { // Fleche 'gauche' ou Q
            // Gauche
            

                this.setRotateY(270);
                this.setX(this.getX() - 1.0 + slowSpeed);
                vecX=-1;
                vecY=0;
                // collisionLettre(false, -1.0, 0.0, lettres);
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
                // collisionLettre(false, 0.0, 1.0, lettres);
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
                // collisionLettre(false, 1.0, 0.0, lettres);      
        }

        }
        
    }

    // public void collisionLettre(boolean rotate, double x, double z, ArrayList<Letter> lettres){
    //     //hitbox: on verifie que tux est proche d'un mot puis on deplace le mot dans la meme direction
    //     for (Letter lettre : lettres) {
    //         double shortHitBox=4.0;
    //         double longHitBox =9.0;

    //         boolean condX = getX()>lettre.getX()-shortHitBox&& getX()<lettre.getX()+shortHitBox;
    //         boolean condY = getZ()>lettre.getZ()-shortHitBox&& getZ()<lettre.getZ()+shortHitBox;

    //         boolean condXR = getX()>lettre.getX()-longHitBox && getX()<lettre.getX()+longHitBox;
    //         boolean condYR = getZ()>lettre.getZ()-longHitBox && getZ()<lettre.getZ()+longHitBox; //condition pour rotation si on est loin mais assez proche

            
    //         if (condXR && condYR){ //si on est proche mais loins
    //             if (condX && condY ) { //si on est proche
    //                 // if(!testeRoomCollision(lettre,  x, z)){
    //                     if(!testeRoomCollision(lettre,  x, z));
    //                     lettre.deplace(x, z);

    //                     // lettre.setX(lettre.getX()+x); //la lettre bouge
    //                     // lettre.setZ(lettre.getZ()+z);
                      
    //                     collisionInterLettre( lettre, x, z, lettres);
    //                 // }
    //                 // else{
    //                 //     if(lettre.getX()<=0){ // on debloque la lettre
    //                 //         lettre.setX(lettre.getX()+1);
    //                 //     }
    //                 //     else if(lettre.getX()>=room.getWidth()){
    //                 //         lettre.setX(lettre.getX()-1);
    //                 //     }
    //                 //     if(lettre.getZ()<=0){ // on debloque la lettre
    //                 //         lettre.setZ(lettre.getZ()+1);
    //                 //     }
    //                 //     else if(lettre.getZ()>=room.getDepth()){
    //                 //         lettre.setZ(lettre.getZ()-1);
    //                 //     }
    //                 // }
                 
    //             }
    //             else { //loin
    //                 if(getX()>lettre.getX()){ //calcul du bon sens de la rotztion
    //                     lettre.setRotateY(lettre.getRotateY()-(z*2));
    //                     lettre.setX(lettre.getX()-0.05);
    //                 }
    //                 else{
    //                     lettre.setRotateY(lettre.getRotateY()+(z*2));
    //                     lettre.setX(lettre.getX()+0.05);

    //                 }
    //                 if(getZ()>lettre.getZ()){
    //                     lettre.setRotateY(lettre.getRotateY()+(x*2));
    //                     lettre.setZ(lettre.getZ()-0.05);

    //                 }
    //                 else{
    //                     lettre.setRotateY(lettre.getRotateY()-(x*2));
    //                     lettre.setZ(lettre.getZ()+0.05);
    //                 }
    //             }
    //         }
    //             // lettre.collisionInterLettre( x, z, lettres);
    //     }
    // }

    // public void collisionInterLettre(Letter lettre, double x, double z, ArrayList<Letter> lettres){
    //     for (Letter lettre2 : lettres) {
    //         if (!lettre2.equals(lettre)){
    //         boolean condX = lettre.getX()>lettre2.getX()-7 && lettre.getX()<lettre2.getX()+7;
    //         boolean condY = lettre.getZ()>lettre2.getZ()-7 && lettre.getZ()<lettre2.getZ()+7;

    //         boolean condXR = lettre.getX()>lettre2.getX()-10 && lettre.getX()<lettre2.getX()+10;
    //         boolean condYR = lettre.getZ()>lettre2.getZ()-10 && lettre.getZ()<lettre2.getZ()+10; //condition pour rotation

    //             if (condX && condY) {
    //                 lettre2.setX(lettre2.getX()+x);
    //                 lettre2.setZ(lettre2.getZ()+z);  
                    
    //                 // collisionInterLettre(lettre2, x, z, lettres);    //corriger stack oberflow     
    //             }
    //         }
    //     }
   // }
  
}