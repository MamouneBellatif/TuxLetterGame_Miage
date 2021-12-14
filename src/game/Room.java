package game;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.io.File;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class Room {
    private int depth;  
    private int height;
    private int width;
    private String textureBottom;
    private String textureNorth;
    private String textureEast;
    private String textureWest;
    private String textureTop;
    private String textureSouth;

   

    //parsing DOM du plateau
    public Room() {
        try{
            Document _doc=XMLUtil.DocumentFactory.fromFile("Data/xml/plateau.xml");
            this.height=Integer.parseInt(_doc.getElementsByTagName("height").item(0).getTextContent());
            this.width=Integer.parseInt(_doc.getElementsByTagName("width").item(0).getTextContent());
            this.depth=Integer.parseInt(_doc.getElementsByTagName("depth").item(0).getTextContent());
    
    
            this.textureBottom=_doc.getElementsByTagName("textureBottom").item(0).getTextContent();
            this.textureNorth=_doc.getElementsByTagName("textureNorth").item(0).getTextContent();
            this.textureEast=_doc.getElementsByTagName("textureEast").item(0).getTextContent();
            this.textureWest=_doc.getElementsByTagName("textureWest").item(0).getTextContent();
        }
        catch(Exception e){
            Logger.getLogger(Profil.class.getName()).log(Level.SEVERE, null, e);
        }
        // _doc=fromXML("Data/xml/plateau.xml");
        
    }
    

    public int getDepth() {
        return this.depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getTextureBottom() {
        return this.textureBottom;
    }

    public void setTextureBottom(String textureBottom) {
        this.textureBottom = textureBottom;
    }

    public String getTextureNorth() {
        return this.textureNorth;
    }

    public void setTextureNorth(String textureNorth) {
        this.textureNorth = textureNorth;
    }

    public String getTextureEast() {
        return this.textureEast;
    }

    public void setTextureEast(String textureEast) {
        this.textureEast = textureEast;
    }

    public String getTextureWest() {
        return this.textureWest;
    }

    public void setTextureWest(String textureWest) {
        this.textureWest = textureWest;
    }

    public String getTextureTop() {
        return this.textureTop;
    }

    public void setTextureTop(String textureTop) {
        this.textureTop = textureTop;
    }

    public String getTextureSouth() {
        return this.textureSouth;
    }

    public void setTextureSouth(String textureSouth) {
        this.textureSouth = textureSouth;
    }

}
