/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tree.generator.java8;

/**
 *
 * @author Mehran
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 *
 * @author Mehran Alidoost Nia
 */
public class SetReader {
    private Scanner input;






    public void OpenFile(String name)
    {
     try{
            File f2=new File(name);
            input=new Scanner(f2);
        }
        catch(FileNotFoundException fileNotFoundException){
          System.err.println("Error opening file !");
          System.exit(1);
        }
    }



    public int[][] ReadFile()
    {
       String s;
       String[] split;
       split = new String [10];
       int a[][]=new int[10][10];
       int i=0;
       
      try{
           while(input.hasNext()){
               s=input.nextLine();
               split=s.split("\t");
              // System.out.println(s);
                                 
              
                 // System.out.println(split[4]);
                  for(int l=0; l<split.length ;l++)
                  {
                  a[i][l]=Integer.valueOf(split[l]);
                  //System.out.println(a[i][l]);
                  }
               //System.out.println(a[i-2][0]);
               //System.out.println(a[i-2][1]);
              
              
               i++;
              
               
               
           }
        }catch(NoSuchElementException elementException){
         System.err.println("file is peroperly forms");
         input.close();
         System.exit(1);
        }
      return a;
      }





    public void CloseFile()
    {

    }

}

