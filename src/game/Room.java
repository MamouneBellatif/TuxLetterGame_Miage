package game;

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


    public Room() {
        this.depth=100;
        this.height=60;
        this.width=100;

        this.textureBottom="textures/floor/grass1Mark.jpg";
        // this.textureBottom="textures/floor/grass1.png";
        this.textureNorth="textures/floor/planks.png";
        this.textureEast="textures/floor/planks.png";
        this.textureWest="textures/floor/planks.png";

        // this.textureBottom="textures/floor/photo1.jpg";
        // this.textureNorth="textures/floor/photo1.jpg";
        // this.textureEast="textures/floor/photo1.jpg";
        // this.textureWest="textures/floor/photo1.jpg";
        
        
        
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
