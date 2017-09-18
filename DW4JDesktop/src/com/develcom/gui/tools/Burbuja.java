/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.gui.tools;

//Librería para los cuadros de Dialogos
import javax.swing.*;

//inicio de la Clase Burbuja
public class Burbuja {

//el vector para insertar los datos
    public int[] vector;

//metodo Constructor
    public Burbuja() {

//definimos el tamaño del arreglo através del metodo tamaño()
        vector = new int[tamaño()];

    }//Fin Constructor

//metodo para obtener el tamaño del vector
    public int tamaño() {

//leectura del tamaño
        int a = Integer.parseInt(JOptionPane.showInputDialog("Tamaño del Vector??:"));
        System.out.println("tamaño del vector "+a);
//retorno del tamaño
        return a;

    }//fin tamaño

//metodo para la lectura de los datos
    public void leerDatos() {

//ciclo para leer todos los datos
        for (int i = 0; i < vector.length; i++)//llenamos las posiciones del vector
        {
            int valor = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el dato:"));
            System.out.println("valor ingresado "+valor);
            vector[i] = valor;
        }

    }//fin leerDatos

//método de ordenamiento Burbuja
    public void ordenar() {

//variable auxiliar
        int aux;

//ciclo1 para el ordenamiento
        for (int i = 0; i < vector.length; i++) {
            System.out.println("primer ciclo "+i);
//ciclo2 para el ordenamiento
            for (int j = 0; j < vector.length - 1; j++) {
                System.out.println("segundo ciclo "+j);
//condición, si el valor en posicion actual es mayor
//que el valor de la siguiente posicion, realiza acciones
                System.out.println("valor actual "+vector[j]);
                System.out.println("valor actual (+1) "+vector[j + 1]);
                if (vector[j] > vector[j + 1]) {

//variable auxiliar toma el valor de la posicion actual
                    aux = vector[j];
                    System.out.println("valor auxiliar "+aux);
//vector en la posicion actual toma el valor de la siguiente posicion
                    vector[j] = vector[j + 1];

//vector en la siguiente posición toma el valor de la posición actual
                    vector[j + 1] = aux;

                }//fin if

            }//fin ciclo2

        }//fin ciclo1

    }//fin metodo ordenar

//metodo imprimir
    public String imprimir() {

//variable para el retorno
        String salida = "";

//ciclo para agregar todos los datos a la variable de retorno
        for (int i = 0; i < vector.length; i++) //agregando datos y retorno de carro a la variable de retorno
        {
            salida += vector[i] + "\n";
        }

//retorno de la variable
        return salida;

    }//fin metodo imprimir

//metodo main
    public static void main(String args[]) {

//instancia de la clase Burbuja
        Burbuja burbuja = new Burbuja();
//invocar metodo leerDatos
        burbuja.leerDatos();
//invocar metodo ordenar
        burbuja.ordenar();
//invocar metodo imprimir y mostrar la salida en un cuadro de diálogo
        JOptionPane.showMessageDialog(null, burbuja.imprimir());

//salir de la aplicación
        System.exit(0);

    }//fin del metodo main
}//fin clase Burbuja
