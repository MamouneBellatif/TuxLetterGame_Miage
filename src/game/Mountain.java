package game;
import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import env3d.Env;
import env3d.advanced.EnvNode;

public class Mountain extends EnvNode {
    Env env;
    Room room;
    double slowSpeed;    
    public Mountain(Env env, Room room) {

        this.env = env;
        this.room = room;
       

        setScale(10);
        setRotateY(-90);
        setX(room.getWidth()-10);// positionnement au milieu de la largeur de la room
        setY(getScale() * 1.1); // positionnement en hauteur basé sur la taille de Tux
        setZ(10); // positionnement au milieu de la profondeur de la room
        // setTexture("models/tux ...");
        setTexture("models/longHouse/HouseLong.png");
        setModel("models/longHouse/LongHouse.obj");        

        slowSpeed=0.0;
    }
}
