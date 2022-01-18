package org.openjfx;

import java.util.Arrays;

public class Splicer {
    /**
     * Test kommentar
     *
     * @param input
     * @return
     */
	 public static float[][] returnMatrix(String input){
         float [][] matrix = new float[4][4];
         String[] rowWise = input.split(" ");
         if(input.contains("y")){
             int i = 2;
             for(int j= 0; j <3; j++ ){
                 for(int l =0; l<4; l++){
                     //System.out.println(rowWise[i]);
                     matrix[j][l] = Float.parseFloat(rowWise[i]);
                     //System.out.println(matrix[j][l]);
                     i ++;
                 }
             }

         } else if(!input.contains("n")) {
             int i = 0;
             for(int j= 0; j <3; j++ ){
                 for(int l =0; l<4; l++){
                     //System.out.println(rowWise[i]);
                     matrix[j][l] = Float.parseFloat(rowWise[i]);
                     i ++;
                 }
             }
         }
         for(int l = 0; l < 3; l++){
             matrix[3][l] = 0;
         }
         matrix[3][3] = 1;
         
         for(int i = 0; i < matrix.length; i++) {
        	 float[] row = matrix[i];
        	 
        	 System.out.println(Arrays.toString(row));
         }
         
         return matrix;
     }

}
