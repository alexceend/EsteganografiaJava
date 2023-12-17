package Esteganografia;

public class Esteganografia {
    static final String caracteres =
            "abcdefghijklmnopqrstuvwxyzŕâéčęëîďôůűü˙ç" +
                    "ABCDEFGHIJKLMNOPQRSTUVWXYZŔÂČÉĘËÎĎÔŮŰÜÇ" +
                    "0123456789" +
                    " ,.;:!?'Ťť\"" +
                    "()+-*/=<>[]{}|%" +
                    "&~#@\\^" +
                    "\t\n\r" +
                    "§"; // <- Caracter final

    static int rango(char c) {
        int k = 0;
        while (k < caracteres.length() && caracteres.charAt(k) != c) {
            k++;
        }
        return k;
    }

    static PixelRGB escondeCaracter(PixelRGB pixel, char c) {
        int r = pixel.getR();
        int g = pixel.getG();
        int b = pixel.getB();

        int cc = rango(c); //pos de c en chars
        if (cc == caracteres.length()) {
            cc = rango('?');
        }

        //valor rango a base 5
        int cr = (cc / 25) % 5;
        int cg = (cc / 5) % 5;
        int cb = cc % 5;

        //cambiar el bit menos significativo
        int rr = (r / 5) * 5 + cr;
        int gg = (g / 5) * 5 + cg;
        int bb = (b / 5) * 5 + cb;

        //no se salga de límites:
        if (rr > 255) {
            rr = rr - 5;
        }
        if (gg > 255) {
            gg = gg - 5;
        }
        if (bb > 255) {
            bb = bb - 5;
        }

        return new PixelRGB(rr, gg, bb);
    }

    static char extraeCaracter(PixelRGB pixel) {
        char c;

        int cr = pixel.getR();
        int cg = pixel.getG();
        int cb = pixel.getB();

        int rango = (cr % 5) * 25 + (cg % 5) * 5 + cb % 5;

        c = caracteres.charAt(rango);

        return c;
    }

    static void escondeTexto(ImageRGB img, String texto) {
        int k = 0;
        texto = texto + "§";

        for (int i = 0; i < img.alto() && k < texto.length(); i++) {
            for (int j = 0; j < img.ancho() && k < texto.length(); j++) {
                PixelRGB p = escondeCaracter(img.getPixelRGB(i, j), texto.charAt(k));
                img.setPixelRGB(i, j, p);
                k++;
            }
        }
    }

    static String extraeTexto(ImageRGB img) {
        String texto = "";
        boolean b = true;

        for (int i = 0; i < img.alto() && b; i++) {
            for (int j = 0; j < img.ancho() && b; j++) {
                texto = texto + extraeCaracter(img.getPixelRGB(i,j));
                if (extraeCaracter(img.getPixelRGB(i, j)) == '§') {
                    b = false;
                }
            }
        }
        return texto;
    }
}
