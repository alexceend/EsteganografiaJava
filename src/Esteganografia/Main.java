package Esteganografia;

import static Esteganografia.Esteganografia.extraeTexto;

import java.io.File;
import java.util.Scanner;

/**
 * @autor Alex pero se lo he copiado a guillermo ahjaajaj
 */
public class Main {
    private static void leer(Scanner sc) {
        clearScreen();
        System.out.println("Escribe el nombre de la imagen sin el .bmp");
        String s = sc.nextLine();
        File f = new File(s);
        String path = f.getAbsolutePath();
        ImageRGB img = new ImageRGB(path);
        String texto = extraeTexto(img);
        System.out.println("Texto oculto:");
        System.out.println(texto);
    }

    private static void esconder(Scanner sc) {
        clearScreen();
        System.out.println("Escribe el nombre de la imagen sin el .bmp");
        String s = sc.nextLine();
        File f2 = new File(s);
        String path2 = f2.getAbsolutePath();
        ImageRGB img2 = new ImageRGB(path2);
        System.out.println("Escribe el texto que quieras esconder: ");
        String txtoculto = sc.nextLine();
        String textoEsconder = txtoculto;
        Esteganografia.escondeTexto(img2, textoEsconder);
        img2.guardaBitMap(path2);
    }

    private static void pngToBMPOp(Scanner sc) {
        clearScreen();
        System.out.println("Escribe el nombre de la imagen sin el .png");
        String s = sc.nextLine();
        ImageRGB.pngToBMP(s);
    }

    private static void jpgToBMPOp(Scanner sc) {
        clearScreen();
        System.out.println("Escribe el nombre de la imagen sin el .jpg");
        String s = sc.nextLine();
        ImageRGB.jpgToBMP(s);
    }

    public static void clearScreen() {
        try {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows")) {
                Runtime.getRuntime().exec("cls");
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (final Exception e) {
            //  Handle any exceptions.
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int op;
        do {
            System.out.println("Elige opción: \n" +
                    "1. Obtener texto de una imagen (BMP)\n" +
                    "2. Esconder texto en una imagen (BMP)\n" +
                    "3. PNG to BMP\n" +
                    "4. JPG to BMP\n" +
                    "0. Cerrar programa");
            op = sc.nextInt();
            while (op < 0 || op > 4) {
                System.out.println("Opción no válida (0/4)\n");
                op = sc.nextInt();
            }
            sc.nextLine();
            switch (op) {
                case 0:
                    break;
                case 1:
                    leer(sc);
                    break;
                case 2:
                    esconder(sc);
                    break;
                case 3:
                    pngToBMPOp(sc);
                    break;
                case 4:
                    jpgToBMPOp(sc);
                    break;
                default:
                    break;
            }
        } while (op != 0);
    }
}
