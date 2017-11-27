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
public class TreeGeneratorJava8 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        int proc=0;
        SetReader s=new SetReader();
        s.OpenFile("Tree.txt");
        int a[][]=s.ReadFile();
        s.CloseFile();
        for( int i=0; i<10;i++)
            for(int j=0;j<10;j++)
        {
            if(j==a[i][3]+4)//4+number of children 
            {
                proc ++;
                break;
            }
            else if(a[i][j]==-3)//end of tree
            {
                i=9;
            break;
            }
            System.out.println(a[i][j]);
        }
        
        PIFWriter p=new PIFWriter ();
        p.OpenFile("PIF.txt");
        p.WriteTree(proc, a);
        p.CloseFile();
        
        
    }
    
}

