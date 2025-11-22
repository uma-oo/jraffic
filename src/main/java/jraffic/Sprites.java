package jraffic;

import javafx.scene.image.Image;

public class Sprites {
    public final Image texStraight;
    public final Image texLeft;
    public final Image texRight;

    public static final double FRAME_W = 88.0;
    public static final double FRAME_H = 88.0;

    private Sprites(Image straight, Image left, Image right) {
        this.texStraight = straight;
        this.texLeft = left;
        this.texRight = right;
    }

    public static int getFrameCount(Lane dir) {
        switch (dir) {
            case SOUTH:
                return 8;
            case EAST:
                return 2;
            case WEST:
                return 2;
            case NORTH:
                return 2;
        }
        return 2;
    }

    public static Sprites load() {
        String base = "/assets/sprites/";
        Image straight = new Image(
                Sprites.class.getResourceAsStream(base + "car_00.png"),
                0, 0, true, false);
        Image left = new Image(
                Sprites.class.getResourceAsStream(base + "car_01.png"),
                0, 0, true, false);
        Image right = new Image(
                Sprites.class.getResourceAsStream(base + "car_02.png"),
                0, 0, true, false);

        return new Sprites(straight, left, right);
    }

    public static Pair<Double, Double> pixelsForLane(Lane lane) {
        return Lane.getPixelsToAdd(lane);
    }
}
