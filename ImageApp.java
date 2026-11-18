/*
 * ImageApp
 *
 * PLTW CSA 3.3.4 Image App
 * Team: Abhinav and Yash
 *
 * Shows nine images:
 *   0. the original picture
 *   1. a recolored picture ("sunset" tint)
 *   2. a photographic negative
 *   3. a grayscale picture
 *   4. the picture rotated 180 degrees
 *   5. the picture rotated 90 degrees counterclockwise
 *   6. the picture rotated 90 degrees clockwise
 *   7. a small picture pasted on the large one with its colors completely changed
 *   8. a small picture pasted on the large one, blended with the large picture's colors
 *
 * All rotations use matrix multiplication (see Vector1by2 and Matrix2by2).
 * Every finished image is also saved to the "output" folder so we can put
 * it in our slides.
 */
import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.io.File;

public class ImageApp
{
    /** Folder that finished images are saved to. */
    public static final String OUTPUT_FOLDER = "output";

    /** How far (in RGB distance) a pixel can be from the background color and still count as background. */
    public static final double BACKGROUND_TOLERANCE = 60.0;

    /** How much the "sunset" recolor adds to red and takes away from blue. */
    public static final int SUNSET_SHIFT = 60;

    public static void main(String[] args)
    {
        // use any file from the lib folder
        String pictureFile = "lib/beach.jpg";

        // the small picture to paste (from lib2) and where its top-left corner goes
        String smallFile = "lib2/bird.png";
        int pasteRow = 60;
        int pasteCol = 80;

        new File(OUTPUT_FOLDER).mkdirs();

        // Get an image, get 2d array of pixels, show a color of a pixel, and display the image
        Picture origImg = new Picture(pictureFile);
        Pixel[][] origPixels = origImg.getPixels2D();
        System.out.println(origPixels[0][0].getColor());
        showAndSave(origImg, "0-original");

        // Image #1 Using the original image and pixels, recolor an image by changing the RGB color of each Pixel
        Picture recoloredImg = new Picture(pictureFile);
        Pixel[][] recoloredPixels = recoloredImg.getPixels2D();
        recolor(recoloredPixels);
        showAndSave(recoloredImg, "1-recolor");

        // Image #2 Using the original image and pixels, create a photographic negative of the image
        Picture negImg = new Picture(pictureFile);
        Pixel[][] negPixels = negImg.getPixels2D();
        negate(negPixels);
        showAndSave(negImg, "2-negative");

        // Image #3 Using the original image and pixels, create a grayscale version of the image
        Picture grayscaleImg = new Picture(pictureFile);
        Pixel[][] grayscalePixels = grayscaleImg.getPixels2D();
        grayscale(grayscalePixels);
        showAndSave(grayscaleImg, "3-grayscale");

        // Image #4 Using the original image and pixels, rotate it 180 degrees
        Picture upsidedownImage = rotate(origImg, Matrix2by2.rotate180());
        showAndSave(upsidedownImage, "4-rotate180");

        // Image #5 Using the original image and pixels, rotate image 90 (counterclockwise)
        // Note: a 90 degree turn swaps width and height, so rotate() builds a new canvas
        // instead of reusing a copy of the original (which would be the wrong shape).
        Picture rotateImg = rotate(origImg, Matrix2by2.rotate90CounterClockwise());
        showAndSave(rotateImg, "5-rotate90ccw");

        // Image #6 Using the original image and pixels, rotate image -90 (clockwise)
        Picture rotateImg2 = rotate(origImg, Matrix2by2.rotate90Clockwise());
        showAndSave(rotateImg2, "6-rotate90cw");

        // Final Image: Add a small image to a larger one
        Picture small = new Picture(smallFile);

        //   Version 1: the small image's colors are completely changed (made negative)
        Picture pasteChanged = new Picture(pictureFile);
        pasteWithNewColors(pasteChanged, small, pasteRow, pasteCol);
        showAndSave(pasteChanged, "7-paste-changed-colors");

        //   Version 2: the small image's colors are blended with the large image's colors
        Picture pasteBlended = new Picture(pictureFile);
        pasteBlended(pasteBlended, small, pasteRow, pasteCol);
        showAndSave(pasteBlended, "8-paste-blended");

        // for testing 2D algorithms
        int[][] test1 = { { 1, 2, 3, 4 },
                          { 5, 6, 7, 8 },
                          { 9, 10, 11, 12 },
                          { 13, 14, 15, 16 } };
        int[][] test2 = rotate(test1, Matrix2by2.rotate90Clockwise());
        print2D(test2);
    }

    // =====================================================================
    //  Recolor
    // =====================================================================

    /**
     * Recolors every pixel with a warm "sunset" tint:
     * more red, a little more green, and less blue.
     * (Pixel.setRed / setBlue keep values between 0 and 255.)
     *
     * @param pixels the pixels to change (changed in place)
     */
    public static void recolor(Pixel[][] pixels)
    {
        for (Pixel[] row : pixels)
        {
            for (Pixel p : row)
            {
                p.setRed(p.getRed() + SUNSET_SHIFT);
                p.setGreen(p.getGreen() + SUNSET_SHIFT / 4);
                p.setBlue(p.getBlue() - SUNSET_SHIFT);
            }
        }
    }

    /**
     * Turns the picture into a photographic negative:
     * each color value v becomes 255 - v.
     *
     * @param pixels the pixels to change (changed in place)
     */
    public static void negate(Pixel[][] pixels)
    {
        for (Pixel[] row : pixels)
        {
            for (Pixel p : row)
            {
                p.setColor(negativeOf(p.getColor()));
            }
        }
    }

    /**
     * Turns the picture gray by setting red, green and blue
     * to the average of the three.
     *
     * @param pixels the pixels to change (changed in place)
     */
    public static void grayscale(Pixel[][] pixels)
    {
        for (Pixel[] row : pixels)
        {
            for (Pixel p : row)
            {
                int avg = (int) Math.round(p.getAverage());
                p.setColor(new Color(avg, avg, avg));
            }
        }
    }

    /**
     * Returns the negative of a color (255 minus each value).
     *
     * @param c the original color
     * @return the negative color
     */
    public static Color negativeOf(Color c)
    {
        return new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue());
    }

    // =====================================================================
    //  Rotate (matrix multiplication)
    // =====================================================================

    /**
     * Rotates a picture using matrix multiplication.
     *
     * Steps for every pixel at (row, col):
     *   1. make the location vector v = [row, col]
     *   2. multiply by the rotation matrix: v' = v * m
     *   3. shift v' so the smallest row and column are 0 (the rotation
     *      spins the picture around the top-left corner, which pushes
     *      it to negative indexes)
     *   4. copy the pixel's color to that spot in the new picture
     *
     * The size of the new picture comes from rotating the four corners,
     * so 90 degree turns correctly swap the width and height.
     *
     * @param source the picture to rotate (not changed)
     * @param m the rotation matrix
     * @return a new, rotated picture
     */
    public static Picture rotate(Picture source, Matrix2by2 m)
    {
        Pixel[][] from = source.getPixels2D();
        int rows = from.length;
        int cols = from[0].length;

        // find where the corners land to get the shift and the new size
        int[] box = boundingBox(rows, cols, m);
        int minRow = box[0], minCol = box[1], maxRow = box[2], maxCol = box[3];
        Vector1by2 shift = new Vector1by2(-minRow, -minCol);

        // note: Picture(height, width)
        Picture result = new Picture(maxRow - minRow + 1, maxCol - minCol + 1);
        Pixel[][] to = result.getPixels2D();

        for (int r = 0; r < rows; r++)
        {
            for (int c = 0; c < cols; c++)
            {
                Vector1by2 moved = newLocation(r, c, m, shift);
                to[moved.getRowIndex()][moved.getColIndex()].setColor(from[r][c].getColor());
            }
        }
        return result;
    }

    /**
     * Rotates a 2D int array the same way (used to test the algorithm on small numbers).
     *
     * @param grid the array to rotate (not changed)
     * @param m the rotation matrix
     * @return a new, rotated array
     */
    public static int[][] rotate(int[][] grid, Matrix2by2 m)
    {
        int rows = grid.length;
        int cols = grid[0].length;
        int[] box = boundingBox(rows, cols, m);
        Vector1by2 shift = new Vector1by2(-box[0], -box[1]);
        int[][] result = new int[box[2] - box[0] + 1][box[3] - box[1] + 1];

        for (int r = 0; r < rows; r++)
        {
            for (int c = 0; c < cols; c++)
            {
                Vector1by2 moved = newLocation(r, c, m, shift);
                result[moved.getRowIndex()][moved.getColIndex()] = grid[r][c];
            }
        }
        return result;
    }

    /**
     * Where does the cell (r, c) go?  [r, c] * m + shift
     *
     * @param r the original row
     * @param c the original column
     * @param m the rotation matrix
     * @param shift the shift that moves the result back to 0-based indexes
     * @return the new location
     */
    private static Vector1by2 newLocation(int r, int c, Matrix2by2 m, Vector1by2 shift)
    {
        Vector1by2 location = new Vector1by2(r, c);
        Vector1by2 rotated = Matrix2by2.multiply(location, m);
        return Vector1by2.add(rotated, shift);
    }

    /**
     * Rotates the four corners of a rows x cols grid and finds the
     * smallest and largest row and column they land on.
     *
     * @param rows number of rows
     * @param cols number of columns
     * @param m the rotation matrix
     * @return {minRow, minCol, maxRow, maxCol}
     */
    private static int[] boundingBox(int rows, int cols, Matrix2by2 m)
    {
        int[][] corners = { {0, 0}, {0, cols - 1}, {rows - 1, 0}, {rows - 1, cols - 1} };
        int minRow = Integer.MAX_VALUE, minCol = Integer.MAX_VALUE;
        int maxRow = Integer.MIN_VALUE, maxCol = Integer.MIN_VALUE;

        for (int[] corner : corners)
        {
            Vector1by2 v = Matrix2by2.multiply(new Vector1by2(corner[0], corner[1]), m);
            minRow = Math.min(minRow, v.getRowIndex());
            minCol = Math.min(minCol, v.getColIndex());
            maxRow = Math.max(maxRow, v.getRowIndex());
            maxCol = Math.max(maxCol, v.getColIndex());
        }
        return new int[] { minRow, minCol, maxRow, maxCol };
    }

    // =====================================================================
    //  Cut and paste a small image onto a large image
    // =====================================================================

    /**
     * Version 1: pastes the small picture with its colors completely changed
     * (each pixel becomes its negative). The small picture's background is removed.
     *
     * @param large the picture to paste onto (changed)
     * @param small the picture to paste (not changed)
     * @param startRow the row in large for the small picture's top-left corner
     * @param startCol the column in large for the small picture's top-left corner
     */
    public static void pasteWithNewColors(Picture large, Picture small, int startRow, int startCol)
    {
        paste(large, small, startRow, startCol, false);
    }

    /**
     * Version 2: pastes the small picture blended with the large picture
     * (each color is the average of the two). The small picture's background is removed.
     *
     * @param large the picture to paste onto (changed)
     * @param small the picture to paste (not changed)
     * @param startRow the row in large for the small picture's top-left corner
     * @param startCol the column in large for the small picture's top-left corner
     */
    public static void pasteBlended(Picture large, Picture small, int startRow, int startCol)
    {
        paste(large, small, startRow, startCol, true);
    }

    /**
     * Shared paste loop used by both versions.
     *
     * The small picture's background color is taken from its top-left corner.
     * Any pixel close to that color (or see-through) is skipped, which "cuts out"
     * the object. Parts that would fall off the large picture are skipped too.
     *
     * @param large the picture to paste onto (changed)
     * @param small the picture to paste (not changed)
     * @param startRow the row for the top-left corner
     * @param startCol the column for the top-left corner
     * @param blend true to average with the large picture, false to use the negative color
     */
    private static void paste(Picture large, Picture small, int startRow, int startCol, boolean blend)
    {
        Pixel[][] to = large.getPixels2D();
        Pixel[][] from = small.getPixels2D();
        Color background = from[0][0].getColor();

        for (int r = 0; r < from.length; r++)
        {
            for (int c = 0; c < from[0].length; c++)
            {
                int toRow = startRow + r;
                int toCol = startCol + c;
                boolean onCanvas = toRow >= 0 && toRow < to.length && toCol >= 0 && toCol < to[0].length;

                if (onCanvas && !isBackground(from[r][c], background))
                {
                    Color smallColor = from[r][c].getColor();
                    Color newColor = blend ? average(smallColor, to[toRow][toCol].getColor())
                                           : negativeOf(smallColor);
                    to[toRow][toCol].setColor(newColor);
                }
            }
        }
    }

    /**
     * Decides whether a pixel is part of the background we want to remove.
     *
     * @param p the pixel to check
     * @param background the background color
     * @return true if the pixel is see-through or close to the background color
     */
    public static boolean isBackground(Pixel p, Color background)
    {
        return p.getAlpha() < 128 || p.colorDistance(background) < BACKGROUND_TOLERANCE;
    }

    /**
     * Averages two colors (used for blending).
     *
     * @param c1 the first color
     * @param c2 the second color
     * @return the color halfway between them
     */
    public static Color average(Color c1, Color c2)
    {
        return new Color((c1.getRed() + c2.getRed()) / 2,
                         (c1.getGreen() + c2.getGreen()) / 2,
                         (c1.getBlue() + c2.getBlue()) / 2);
    }

    // =====================================================================
    //  Helpers
    // =====================================================================

    /**
     * Opens the picture in the Picture Explorer (when a screen is available)
     * and saves a copy to output/name.jpg.
     *
     * @param pic the picture to show
     * @param name the file name to save as (no extension)
     */
    private static void showAndSave(Picture pic, String name)
    {
        pic.setTitle(name);
        pic.write(OUTPUT_FOLDER + File.separator + name + ".jpg");
        if (!GraphicsEnvironment.isHeadless())
        {
            pic.explore();
        }
    }

    /**
     * Prints a 2D int array, one row per line.
     *
     * @param grid the array to print
     */
    public static void print2D(int[][] grid)
    {
        for (int[] row : grid)
        {
            for (int value : row)
            {
                System.out.printf("%4d", value);
            }
            System.out.println();
        }
    }
}
