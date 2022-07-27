package seamcarving;

import edu.princeton.cs.algs4.Picture;
import java.util.Arrays;


public class SeamCarver {
    private Picture picture;
    private int width, height;
    private double[][] energy;
    private boolean isTransposed;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();

        this.picture = new Picture(picture);
        this.width = picture.width();
        this.height = picture.height();
        this.energy = new double[width][height];
        this.isTransposed = false;

        for (double[] row : energy) Arrays.fill(row, -1);
    }

    // current picture
    public Picture picture() {
        if (isTransposed) transpose();
        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        if (isTransposed) transpose();
        return width;
    }

    // height of current picture
    public int height() {
        if (isTransposed) transpose();
        return height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (isTransposed) transpose();
        return energyNoTrans(x, y);
    }

    private double energyNoTrans(int x, int y) {
        try {
            if (energy[x][y] != -1) return energy[x][y];
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException();
        }

        double e;

        if (x == 0 || x == width-1 || y == 0 || y == height-1)
            e = 1000;
        else {
            int left = picture.getRGB(x - 1, y), right = picture.getRGB(x + 1, y),
                    upper = picture.getRGB(x, y - 1), lower = picture.getRGB(x, y + 1);
            double deltaXSQ = sqr(r(left) - r(right))
                    + sqr(g(left) - g(right))
                    + sqr(b(left) - b(right));
            double deltaYSQ = sqr(r(upper) - r(lower))
                    + sqr(g(upper) - g(lower))
                    + sqr(b(upper) - b(lower));
            e = Math.sqrt(deltaXSQ + deltaYSQ);
        }
        energy[x][y] = e;
        return e;
    }
    
    private double sqr(int x) {
        return Math.pow(x, 2);
    }

    private int r(int rgb) {
        return (rgb >> 16) & 0xFF;
    }

    private int g(int rgb) {
        return (rgb >>  8) & 0xFF;
    }

    private int b(int rgb) {
        return (rgb) & 0xFF;
    }

    private int[] findSeam() {
        int[] seam = new int[width];

        double[][] energyTo = new double[width][height];
        int[][] from = new int[width][height];

        for (double[] row : energyTo) Arrays.fill(row, Double.MAX_VALUE);
        Arrays.fill(energyTo[0], 1000);

        for (int col = 1; col < width; col++) {
            for (int row = 1; row < height-1; row++) {
                for (int offset = -1; offset <= 1; offset++) {
                    double newE = energyTo[col-1][row+offset] + energyNoTrans(col-1, row+offset);
                    if (newE < energyTo[col][row]) {
                        energyTo[col][row] = newE;
                        from[col][row] = row+offset;
                    }
                }
            }
        }

        double min = Double.MAX_VALUE;
        int minI = 0;
        for (int i = 1; i < height-1; i++) {
            double e = energyTo[width-1][i];
            if (e < min) {
                min = e;
                minI = i;
            }
        }

        for (int i = 0; i < width-1; i++) {
            seam[width-1-i] = minI;
            minI = from[width-1-i][minI];
        }

        if (seam.length > 1) seam[0] = Math.max(0, seam[1]-1);
        return seam;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        if (isTransposed) transpose();
        return findSeam();
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        if (!isTransposed) transpose();
        return findSeam();

    }

    private void removeSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException();
        if (height <= 1) throw new IllegalArgumentException();
        if (seam.length != width) throw new IllegalArgumentException();
        height--;
        Picture srcPic = picture;
        picture = new Picture(width, height);
        double[][] srcE = energy;
        energy = new double[width][height];

        for (int col = 0; col < width; col++) {
            int offset = 0;
            if (seam[col] < 0 || seam[col] >= height+1) throw new IllegalArgumentException();
            if (col > 0) {
                if (Math.abs(seam[col] - seam[col-1]) > 1) throw new IllegalArgumentException();
            }
            for (int row = 0; row < height; row++) {
                if (row == seam[col]) offset = 1;
                picture.setRGB(col, row, srcPic.getRGB(col, row + offset));
                energy[col][row] = srcE[col][row + offset];
            }
            if (seam[col] > 0) energy[col][seam[col]-1] = -1;
            if (seam[col] < height) energy[col][seam[col]] = -1;
        }
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (isTransposed) transpose();
        removeSeam(seam);
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (!isTransposed) transpose();
        removeSeam(seam);
    }

    private void transpose() {
        Picture tPic = new Picture(height, width);
        double[][] tEnergy = new double[height][width];

        for (int col = 0; col < height; col++) {
            for (int row = 0 ;row < width; row++) {
                tPic.setRGB(col, row, picture.getRGB(row, col));
                tEnergy[col][row] = energy[row][col];
            }
        }

        int temp = height;
        height = width;
        width = temp;

        picture = tPic;
        energy = tEnergy;

        isTransposed = !isTransposed;
    }

    //  unit testing (optional)
    public static void main(String[] args) {

    }

}