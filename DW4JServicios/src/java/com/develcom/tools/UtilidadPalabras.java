/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.tools;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

/**
 * Clase utilitaria para validar las palabras
 *
 * @author develcom
 */
public class UtilidadPalabras {

    //public static final String VALIDAR_CARACTERES_ESPECIALES="([0-9a-zA-Z *$#+-.])+";
    //public static final String VALIDAR_CARACTERES_ESPECIALES="([a-zA-Z áéíóú])+";
    /**
     * Expresion regular para validar solo letras y espacio
     */
    public static final String VALIDAR_LETRAS = "([a-zA-Z ])+";
    /**
     * Expresion regular para validar solo letras, numeros y espacio
     */
    public static final String VALIDAR_CARACTERES_ESPECIALES = "([0-9a-zA-Z ])+";
    /**
     * Expresion regular para validar solo letras, numeros espacio y underscore
     */
    public static final String VALIDAR_CARACTERES_ESPECIALES_INDICE = "([0-9a-zA-Z _])+";
    public static final String VALIDAR_NUMEROS = "([0-9])+";

    /**
     * Busca las letras acentuadas y las quitas
     *
     * @param palabra La palabras con acentos
     * @return La palabra sin acento
     */
    public static String buscarAcentos(String palabra) {

        return quitarTilde(palabra);
    }

    /**
     * Busca las letras acentuadas y las quitas
     *
     * @param token La palabras con acentos
     * @return La palabra sin acento
     */
    private static String quitarTilde(String token) {

        //StringBuffer sb = new StringBuffer(token);
        StringBuilder sb = new StringBuilder(token);

        // recorrer el StringBuffer para eliminar caracteres con tilde
        for (int i = 0; i < sb.length(); i++) {

            sb.setCharAt(i, quitarTilde(sb.charAt(i)));
        }

        return sb.toString();
    }

    /**
     * Sustituye la letra acentuada por una no acentuada
     *
     * @param car la letra acentuada
     * @return la letra no acentuada
     */
    private static char quitarTilde(char car) {

        char[] conTilde = {'á', 'é', 'í', 'ó', 'ú', 'Á', 'É', 'Í', 'Ó', 'Ú'};
        char[] sinTilde = {'a', 'e', 'i', 'o', 'u', 'A', 'E', 'I', 'O', 'U'};

        for (int i = 0; i < conTilde.length; i++) {
            if (car == conTilde[i]) {

                car = sinTilde[(i)];
                //car = sinTilde[(i / 2)];

                break;
            }
        }

        return car;
    }

    public static String acomodarApostrofe(String arg) {

        String data = null;
        StringBuilder s = new StringBuilder(arg.length());
        CharacterIterator it = new StringCharacterIterator(arg);
        
        
        int chr = arg.indexOf("'");
        
        if(chr != -1){
            
            data = arg.replaceAll("'", "\\\\'");
            
        }
        
//        StringBuilder s = new StringBuilder(arg.length());
//        //StringBuffer s = new StringBuffer(arg.length());
//
//        CharacterIterator it = new StringCharacterIterator(arg);
//        for (char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next()) {
//            switch (ch) {
//                case '&':
//                    s.append("&amp;");
//                    break;
//                case '<':
//                    s.append("&lt;");
//                    break;
//                case '>':
//                    s.append("&gt;");
//                    break;
//                default:
//                    s.append(ch);
//                    break;
//            }
//        }


        return data;
    }
//    public static void main(String[] arg){
//        boolean b = "Coordinación de Ingreso y Desarrollo Persona".matches(VALIDAR_CARACTERES_ESPECIALES);
//        System.out.println(b);
//    }
}
