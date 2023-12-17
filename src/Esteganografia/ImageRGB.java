package Esteganografia;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImageRGB {

    private BufferedImage img;

    public ImageRGB(String f) {
        if (f.length() < 4 || !f.substring(f.length() - 4, f.length()).equals(".bmp")) {
            f = f + ".bmp";
        }
        try {
            DataInputStream inBMP = new DataInputStream(new FileInputStream(new File(f)));
            inBMP.skipBytes(18); //cabecera

            int ancho = leerEnetro32Bits(inBMP);
            int alto = leerEnetro32Bits(inBMP);
            inBMP.skipBytes(28); //salta datos no utiles de la cabecera
            img = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
            int tamanoRelleno = (4 - ((ancho * 3) % 4)) % 4;
            for (int y = alto - 1; y >= 0; y--) {
                for (int x = 0; x < ancho; x++) {
                    img.setRGB(x, y, leePixel124bits(inBMP));
                }
                inBMP.skipBytes(tamanoRelleno);
            }
            inBMP.close();
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }

    private ImageRGB(int ancho, int alto) {
        img = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
    }

    public static ImageRGB unir(int ancho, int alto, PixelRGB px) {
        ImageRGB iRGB = new ImageRGB(ancho, alto);
        for (int i = 0; i < alto; i++) {
            for (int j = 0; j < ancho; j++) {
                iRGB.setPixelRGB(i, j, px);
            }
        }
        return iRGB;
    }

    public static ImageRGB blanco(int ancho, int alto) {
        return unir(ancho, alto, PixelRGB.blanco());
    }

    public static ImageRGB negro(int ancho, int alto) {
        return unir(ancho, alto, PixelRGB.negro());
    }

    public static int leerEnetro32Bits(DataInputStream in) {
        byte[] b = new byte[4];
        int resul = 0;
        try {
            in.read(b);
            //0xFF least signif byte
            resul = b[0] & 0xFF;
            resul = resul + ((b[1] & 0xFF) << 8);
            resul = resul + ((b[2] & 0xFF) << 16);
            resul = resul + ((b[3] & 0xFF) << 24);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return resul;
    }

    public static int leePixel124bits(DataInputStream in) {
        byte[] b = new byte[3];
        int resul = 0;
        try {
            in.read(b);
            resul = b[0] & 0xFF;
            resul = resul + ((b[1] & 0xFF) << 8);
            resul = resul + ((b[2] & 0xFF) << 16);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return resul;
    }

    public boolean guardaBitMap(String f) {
        if (f.length() < 4 || !f.substring(f.length() - 4, f.length()).equals(".bmp")) {
            f = f + ".bmp";
        }
        try {
            DataOutputStream outBmp = new DataOutputStream(new FileOutputStream(f));
            outBmp.write(0x42); //tipo
            outBmp.write(0x4D); //tipo
            int ancho = img.getWidth();
            int alto = img.getHeight();
            int tamanoRelleno = (4 - ((ancho * 3) % 4)) % 4; //num de bytes de relleno al final d cada línea
            escribeEntero32Bits(outBmp, alto * (ancho * 3 + tamanoRelleno) + 54);
            escribeEntero32Bits(outBmp, 0);
            escribeEntero32Bits(outBmp, 54);
            escribeEntero32Bits(outBmp, 40);
            escribeEntero32Bits(outBmp, ancho);
            escribeEntero32Bits(outBmp, alto);
            outBmp.write(1);
            outBmp.write(0);
            outBmp.write(24);
            outBmp.write(0);
            escribeEntero32Bits(outBmp, 0);
            escribeEntero32Bits(outBmp, alto * (ancho * 3 + tamanoRelleno));
            escribeEntero32Bits(outBmp, 2851);
            escribeEntero32Bits(outBmp, 2851);
            escribeEntero32Bits(outBmp, 0);
            escribeEntero32Bits(outBmp, 0);
            for (int y = alto - 1; y >= 0; y--) {
                for (int x = 0; x < ancho; x++) {
                    escribePixel124Bits(outBmp, img.getRGB(x, y));
                }
                for (int j = 0; j < tamanoRelleno; j++) {
                    outBmp.write(0);
                }
            }
            outBmp.close();
            return true;
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
            return false;
        }
    }

    public static void escribeEntero32Bits(DataOutputStream salida, int n) {
        try {
            salida.write((n) & 0xFF);
            salida.write((n >> 8) & 0xFF);
            salida.write((n >> 16) & 0xFF);
            salida.write((n >> 24) & 0xFF);
        } catch (Exception e) {

        }
    }

    public static void escribePixel124Bits(DataOutputStream salida, int p) {
        try {
            salida.write((p) & 0xFF);
            salida.write((p >> 8) & 0xFF);
            salida.write((p >> 16) & 0xFF);
        } catch (Exception e) {

        }
    }

    public int alto() {
        return img.getHeight();
    }

    public int ancho() {
        return img.getWidth();
    }

    public PixelRGB getPixelRGB(int i, int j) {
        int p = img.getRGB(j, i);
        return new PixelRGB((p >> 16) & 0xFF, (p >> 8) & 0xFF, p & 0xFF);
    }

    public void setPixelRGB(int i, int j, PixelRGB px) {
        int p = px.getB() + (px.getG() << 8) + (px.getR() << 16);
        img.setRGB(j, i, p);
    }

    public static void pngToBMP(String f) {
        if (f.length() < 4 || !f.substring(f.length() - 4, f.length()).equals(".png")) {
            f = f + ".png";
        }
        try {
            File input = new File(f);
            BufferedImage image = ImageIO.read(input);
            File output = new File(f.substring(0, f.length() - 4) + ".bmp");
            System.out.println(output.toString());
            ImageIO.write(image, "bmp", output);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void jpgToBMP(String f) {
        if (f.length() < 4 || !f.substring(f.length() - 4, f.length()).equals(".jpg")) {
            f = f + ".jpg";
        }
        try {
            File input = new File(f);
            BufferedImage image = ImageIO.read(input);
            File output = new File(f.substring(0, f.length() - 4) + ".bmp");
            System.out.println(output.toString());
            ImageIO.write(image, "bmp", output);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
