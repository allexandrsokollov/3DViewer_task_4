package com.cgvsu.render_engine;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Coloring {
    public static int getPixelColor(
            final float shade,
            final float x,
            final float y,
            BufferedImage img) {
        int color = img.getRGB((int) ((x * ((float) img.getWidth()))), (int) (y * (float) (img.getHeight())));
        int r = (int) (((color >> 16) & 0xff) * shade);
        int g = (int) (((color >> 8) & 0xff) * shade);
        int b = (int) (((color) & 0xff) * shade);
        return new Color(r, g, b).getRGB();
    }
    public static int getPixelColor(
            final float x,
            final float y,
            BufferedImage img) {
        return img.getRGB((int) ((x * ((float) img.getWidth()))), (int) (y * (float) (img.getHeight())));
    }

    public static int getPixelColor(
            final float shade,
            int color) {
        int r = (int) (((color >> 16) & 0xff) * shade);
        int g = (int) (((color >> 8) & 0xff) * shade);
        int b = (int) (((color) & 0xff) * shade);
        return new Color(r, g, b).getRGB();
    }

    public static Color convertColorToAWT(javafx.scene.paint.Color col) {
        int r = (int) (col.getRed() * 255);
        int g = (int) (col.getGreen() * 255);
        int b = (int) (col.getBlue() * 255);
        int a = (int) (col.getOpacity() * 255);

        return new java.awt.Color(r, g, b, a);
    }
}
