package game;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import env3d.Env;
import env3d.advanced.EnvNode;

public class Letter extends EnvNode{
    
    private char letter;
    
    private int frame;//image clé d'aniation pour idle()
    private boolean found;//flag d'une lettre trouvée

    public Letter(char l, double x, double y){  

        letter=Character.toLowerCase(l);
        setScale(4.0);
        setX(x);
        setZ(y);
        setY(getScale() * 1.1); // positionnement en hauteur basé sur la taille de Tux
        setTexture("models/letter/"+letter+".png");
        setModel("models/letter/cube.obj");

        frame=0;
        found=false;
    }

    
    /** 
     * @return char
     */
    public char getLetter(){
        return letter;
    }


    /** 
     * Animation lettres: la lettre monte sur le plan vertical pour 15 frames et redescent pour 15 frame en boucle
     * @param x distance x a deplacer
     * @param z distance y a deplacer
     */
    public void idle(){
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

    
    /** 
     * Déplace une lettre d'une distance x et z
     * @param x distance x a deplacer
     * @param z distance y a deplacer
     */
    public void deplace(double x, double z){
        setX(getX()+x);
        setZ(getZ()+z);

    }

    public void setFound(boolean found){
        this.found=found;
    }

    public boolean getFound(){
        return found;
    }
    
 
}
