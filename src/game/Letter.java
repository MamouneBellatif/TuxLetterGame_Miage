package game;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import env3d.Env;
import env3d.advanced.EnvNode;

public class Letter extends EnvNode{
    
    private char letter;
//calucler vecteru de mouvement a chaque deplacement

    private double vecX; //vecteur de mouvement
    private double vecZ; //vecteur de mouvement

    public Letter(char l, double x, double y){        
        letter=Character.toLowerCase(l);
        setScale(4.0);
        setX(x);
        setZ(y);
        setY(getScale() * 1.1); // positionnement en hauteur basé sur la taille de Tux
        setTexture("models/letter/"+letter+".png");
        setModel("models/letter/cube.obj");

        vecX=0;
        vecZ=0;
    }

    public char getLetter(){
        return letter;
    }

    
    
    
    
    // anime les lettres
    //On compte jusqua 20, entre 0 et 10 le mot monte,entre 10 et 20 il déscend et on recommence
    int frame =0;
    boolean found=false;
    public void idle(){
        // System.out.println(frame);
        if(!found){
            if (frame<15){
                setY(getY()+0.1);
            }
            else {
                setY(getY()-0.1);
            }
            frame++;
            if (frame >= 30){
                frame=0;
            }
        }
       
    }

    // public void deplace(double x, double z, ArrayList<Letter> lettres){
        public void deplace(double x, double z){

        setX(getX()+x);
        setZ(getZ()+z);
        vecX=x;
        vecZ=z;
        
        // collisionInterLettre(x, z, lettres);
    }

    public void collisionInterLettre(double x, double z, ArrayList<Letter> lettres){
        for (Letter lettre2 : lettres) {
            if (!lettre2.equals(this)){
            boolean condX = getX()>lettre2.getX()-7 && getX()<lettre2.getX()+7;
            boolean condY = getZ()>lettre2.getZ()-7 && getZ()<lettre2.getZ()+7;

            boolean condXR = getX()>lettre2.getX()-10 && getX()<lettre2.getX()+10;
            boolean condYR = getZ()>lettre2.getZ()-10 && getZ()<lettre2.getZ()+10; //condition pour rotation

                if (condX && condY) {
                    // lettre2.deplace(x, z, lettres);
                    lettre2.setX(lettre2.getX()+x);
                    lettre2.setZ(lettre2.getZ()+z);  
                    // collisionInterLettre(lettre2, x, z, lettres);    //corriger stack oberflow     
                }
            }
        }
    }
}
