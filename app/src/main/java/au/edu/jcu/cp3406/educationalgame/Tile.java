package au.edu.jcu.cp3406.educationalgame;

class Tile {
    private int image;
    private int lightedImage;
    private int activeTile;

    Tile(int imageResource) {
        image = imageResource;
        lightedImage = imageResource + 1;
        activeTile = imageResource;
    }

    int getImg() {
        return activeTile;
    }

    void lightUp() {
        activeTile = lightedImage;
    }

    void lightOff() {
        activeTile = image;
    }
}
