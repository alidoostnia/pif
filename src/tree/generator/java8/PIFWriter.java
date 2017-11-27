/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tree.generator.java8;

/**
 *
 * @author Mehran Alidoost Nia
 */
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.Formatter;

/**
 *
 * @author Mehran Alidoost Nia
 */
public class PIFWriter {
          private Formatter out;



   public void OpenFile(String a){

      try{
            out=new Formatter(a);
        }
        catch(FileNotFoundException fileNotFoundException){
          System.err.println("Error opening file!");
          System.exit(1);
        }
    }


   public void WriteTree(int proc, int a[][]){
       Date date = new Date();
  
     // System.out.printf("%1$s %2$tB %2$td, %2$tY", "Due date:", date);
       out.format("//Synchronous PIF self stabilising algorithm for a tree with %d processes%n"+
       "// Written by Mehran Alidoost Nia on %2$tB %2$td, %2$tY %n%ndtmc%n%n",proc, date);
       
       for(int i=0; i<proc; i++)
       {
           out.format("const double r%d=0.5;%n", i);
       }
       for(int i=1; i<a[0][3];i++)
       {
            out.format("const double q%d=0.2;%n", i);
       }
       
       for(int i=0; i<proc; i++)
       {
           if(a[i][1]==2)
           out.format("const bool up%d=false;%n",i);
       }
        out.format("const bool up0=true;%n");
       /**************root process***************/
       out.format("%nmodule process0%n" +"%n" 
               +"	x0 : bool ; //state variable%n" 
               +"	l0 : [-1..%d];  //id of process%n" 
               +"	a0 : bool; //active%n" 
               +"	p0 : bool; //privilege%n%n",proc-1);
       
       //command1
       out.format("	//token-2a for root%n");
       out.format("	[] (x0)&((x0=x%d & !up%d)",a[0][4],a[0][4]);
       for(int i=1; i<a[0][3];i++)
       {
            out.format("|(x0=x%d & !up%d)", a[0][i+4],a[0][i+4]);
       }
       out.format(")  -> r0:(l0'=1)& (x0'=!x0)");
       for(int i=1; i<a[0][3];i++)
       {
            out.format("+q%d:(l0'=%d) & (x0'=!x0)", i,a[0][i+4]);
       }
       out.format("+1-r0");
       for(int i=1; i<a[0][3];i++)
       {
            out.format("-q%d", i);
       }
       out.format(":(l0'=-1) & (x0'=!x0);%n");
       
       //command2
       /*out.format("%n	//token-2b for root%n");
       out.format("	[] (x0)&((x0=x%d & !up%d)& l%d=-1)",a[0][4],a[0][4],a[0][4]);
       
       for(int i=1; i<a[0][3];i++)
       {
            out.format("&((x0=x%d & !up%d)& l%d=-1) ", a[0][i+4],a[0][i+4],a[0][i+4]);
       }
        out.format("-> (l0'=-1) & (x0'=!x0);%n");*/
     
            
        
        //command3
        out.format("%n	//token-4 and active%n");
        out.format("	[] (!x0)&(x0=x%d &!up%d)",a[0][4],a[0][4]);
        for(int i=1; i<a[0][3];i++)
       {
            out.format("&(x0=x%d &!up%d) ", a[0][i+4],a[0][i+4]);
       }
        out.format("&a0 -> (p0'=true)&(l0'=-1) & (x0'=!x0);%n");
       
        //command4
        out.format("%n	//token-4 and not active%n");
        out.format("	[] (!x0)&(x0=x%d &!up%d)",a[0][4],a[0][4]);
        for(int i=1; i<a[0][3];i++)
       {
            out.format("&(x0=x%d &!up%d) ", a[0][i+4],a[0][i+4]);
       }
        out.format("&!a0 -> (l0'=-1) & (x0'=!x0);%n");
        
        out.format("%nendmodule%n");

        
       for(int i=1; i<proc; i++)
       {
           out.format("%nmodule process%d%n" +"%n" 
               +"	x%d : bool ; //state variable%n" 
               +"	l%d : [-1..%d];  //id of process%n" 
               +"	a%d : bool; //active%n" 
               +"	p%d : bool; //privilege%n",i,i,i,proc-1,i,i,i);
           if(a[i][1]==1)//it's intermediate node
           {
               out.format("	up%d : bool ; //up variable%n",i);
               //command 1
               out.format("%n	//[] (x%d!=x%d)&(!x%d)&(!a%d) -> (l%d'=-1) & (up%d'=true) & (x%d'=!x%d); //token1 and not active%n",a[i][2],i,i,i,i,i,i,i);
               //command 2
               out.format("	//[] (x%d!=x%d)&(!x%d)&(a%d) -> (l%d'=%d) & (up%d'=false) & (x%d'=!x%d); //token1 and active%n",a[i][2],i,i,i,i,i,i,i,i);
               out.format("	[] (x%d!=x%d)&(!x%d) -> r%d: (l%d'=-1) & (up%d'=true) & (x%d'=!x%d)+ 1-r%d: (l%d'=%d) & (up%d'=false) & (x%d'=!x%d);%n%n",a[i][2],i,i,i,i,i,i,i,i,i,i,i,i,i);
               //command 3
               for(int j=1; j<=a[i][3];j++)
               out.format("	[] (up%d)&(x%d)&(x%d=x%d  & l%d!=-1 & up%d=false )&(x%d=x%d) -> (l%d'=%d) & (up%d'=false); //token-2a and parent x %n",i,i,i,a[i][j+3],a[i][j+3],a[i][j+3],a[i][2],i,i,a[i][j+3],i);
               //command 4
               out.format("	[] (up%d)&(x%d)&( (x%d=x%d  & l%d=-1 & up%d=false)",i,i,i,a[i][4],a[i][4],a[i][4]);
               
               for(int j=2; j<=a[i][3];j++)
               out.format("|(x%d=x%d  & l%d=-1 & up%d=false)",i,a[i][j+3],a[i][j+3],a[i][j+3]);
               
               out.format(") &(x%d=x%d) -> (up%d'=false); //token-2b%n",a[i][2],i,i);
               //command 5
               out.format("	[] (x%d!=x%d)&(x%d)&(l%d=%d)&(l%d=%d) -> (p%d'=true)&(up%d'=false) & (x%d'=!x%d);//token3 and id%n",a[i][2],i,i,a[i][2],i,i,i,i,i,i,i);
               //command 6
               out.format("	[] (x%d!=x%d)&(x%d)&(l%d=%d)&((l%d=%d)",a[i][2],i,i,a[i][2],i,i,a[i][4]);
               for(int j=2; j<=a[i][3];j++)
                   out.format("|(l%d=%d)",i,a[i][j+3]);
               out.format(")-> (up%d'=true) & (x%d'=!x%d); //token 3 and children%n",i,i,i);
                //command 7
               out.format("	[] (x%d!=x%d)&(x%d)&((l%d!=%d)|((l%d!=%d)",a[i][2],i,i,a[i][2],i,i,i);
               for(int j=1; j<=a[i][3];j++)
                   out.format("&(l%d!=%d)",i,a[i][j+3]);
                out.format("))-> (l%d'=-1)&(up%d'=false) & (x%d'=!x%d);%n",i,i,i,i);
                //command 8
                out.format("	[] (up%d)&(!x%d)&(x%d=x%d)",i,i,i,a[i][4]);
                for(int j=2; j<=a[i][3];j++)
                   out.format("&(x%d=x%d)",i,a[i][j+3]);
                out.format("&(x%d=x%d) -> (up%d'=false) ; //token4%n%n",a[i][2],i,i);
                out.format("endmodule%n%n");
                
           }
           else if(a[i][1]==2)//it's leaf node
           {
               //command 1
               out.format("%n	//[] (x%d!=x%d)&(!x%d)&(!a%d) -> (l%d'=-1) & (x%d'=!x%d); //token1 and not active%n",a[i][2],i,i,i,i,i,i);
               //command 2
               out.format("	//[] (x%d!=x%d)&(!x%d)&(a%d) -> (l%d'=%d) & (x%d'=!x%d); //token1 and active%n",a[i][2],i,i,i,i,i,i,i);
               
               out.format("	[] (x%d!=x%d)&(!x%d)-> r%d:(l%d'=-1) & (x%d'=!x%d)+ 1-r%d: (l%d'=%d) & (x%d'=!x%d);%n",a[i][2],i,i,i,i,i,i,i,i,i,i,i);
               //command 3
               out.format("	[] (x%d!=x%d)&(x%d)&(l%d=%d)&(l%d=%d) -> (p%d'=true)&(x%d'=!x%d);//token3 and id%n",a[i][2],i,i,a[i][2],i,i,i,i,i,i);
               //command 4
               out.format("	[] (x%d!=x%d)&(x%d)& ((l%d!=%d)|(l%d!=%d)) -> (x%d'=!x%d);//token3 and not id%n",a[i][2],i,i,a[i][2],i,i,i,i,i);
               out.format("%n%nendmodule%n%n");
           }
       }
       out.format("%n%n%n//test formula%n");
       //first formula
       out.format("formula f1=");
       int counter=0;
       for(int i=1; i<proc; i++)
       {
            if(a[i][1]==1&counter==0)//it's intermediate node
            {
                out.format("(up%d&x%d&l%d=-1&a%d)",i,i,i,a[i][4]);
                 counter ++;
            }
            else if(a[i][1]==1&counter!=0)//it's intermediate node
            {
                out.format("&(up%d&x%d&l%d=-1&a%d)",i,i,i,a[i][4]);
                 counter ++;
            }
           
            
       }
       out.format(";%n");
       

//formula 2
        int counter2=0;       
        out.format("formula f2=");
        for(int i=1; i<proc; i++)
       {
           
            if(a[i][1]==1)//it's intermediate node
            {
                 if(counter2>0)
               out.format("|");
                out.format("(!up%d&x%d&l%d=%d)",i,i,a[i][2],i);
                out.format("|((!up%d&x%d&l%d=%d)",i,i,a[i][2],i);
                for(int j=0;j<a[i][3];j++)
                    out.format("&(x%d&l%d=%d)",a[i][4+j],i,a[i][4+j]);
                out.format(")");
                 counter2 ++;
            }
       }
        
        out.format("|");
        counter2=0;
        for(int i=1; i<proc; i++)
       {
           
            if(a[i][1]==1)//it's intermediate node with children
            {
                 if(counter2>0)
               out.format("|");
                out.format("((!up%d&x%d&l%d=-1&!a%d)",i,i,i,i);
                for(int j=0;j<a[i][3];j++)
                    out.format("&(x%d&l%d=-1&!a%d)",a[i][4+j],a[i][4+j],a[i][4+j]);
                out.format(")");
                
                 counter2 ++;
            }
       }
         for(int i=1; i<proc; i++)
        {
            if(a[i][1]==2&a[i][2]==0)//a leaf node which has root as its parent
            {
                out.format("|(x%d&l%d=-1&!a%d)",i,i,i);
            }
        }
        out.format(";%n");
        
        
        //formula 3
        out.format("formula f3=(x0)&((x0&l0=-1)");
        for(int i=1; i<proc; i++)
        {
            if(a[i][3]!=0)//it has children and up
            {
                for(int j=0;j<a[i][3];j++)
                 out.format("|(x%d&up%d&l%d=-1)",a[i][4+j],a[i][4+j],a[i][4+j]);
            }
        }
        out.format(")&(");
        counter2=0;
        for(int i=1; i<proc; i++)
        {
            if(counter2==0)
                 out.format("(x%d&l%d=%d)",i,a[i][2],i);
            else
                out.format("|(x%d&l%d=%d&x%d)",i,a[i][2],i,a[i][2]);
            counter2++;
            
        }
         out.format(");%n");
        
        
        
       //formula 4
       counter2=0;
       out.format("formula f4=(!x0)&(");
       for(int i=1; i<proc; i++)
        {
            if(a[i][1]==1&counter2==0)
            {
                out.format("(a%d",i);
                for(int j=0;j<a[i][3];j++)
                 out.format("&!a%d",a[i][4+j]);
                out.format("&l%d=%d)",a[i][2],i);
                for(int j=0;j<a[i][3];j++)
                 out.format("|(a%d&l%d=%d&l%d=%d)",a[i][4+j],i,a[i][4+j],a[i][2],i);
                counter2++;
            }
            else if(a[i][1]==1&counter2!=0)
            {
                out.format("|(a%d",i);
                for(int j=0;j<a[i][3];j++)
                 out.format("&!a%d",a[i][4+j]);
                out.format("&l%d=%d)",a[i][2],i);
                for(int j=0;j<a[i][3];j++)
                 out.format("|(a%d&l%d=%d&l%d=%d)",a[i][4+j],i,a[i][4+j],a[i][2],i);
                counter2++;
            }
            
        }
       for(int i=1; i<proc; i++)
        {
            if(a[i][1]==2&a[i][2]==0)//a leaf node which has root as its parent
            {
                out.format("|(a%d&l0=%d)",i,i);
            }
        }
               out.format(");%n");
       out.format("formula stability=f4|f3|f2|f1;%n%n");
       
       /*out.format("%n%n%n//test formula%n");
       out.format("formula f1=(up1&x1&l1=-1&a2); //it is true only for intermediate nodes (a3->not last active)%n");
       out.format("formula f2=(!up1&x1&l0=1)|((!up1&x1&l0=1)&(x2&l1=2))|((!up1&x1&l1=-1&(!a1|a2))& (x2&l2=-1&!a2) );//(!a2|a3),&!a3 doubt it!%n");
       out.format("formula f3=((x0&l0=-1)|(x1&up1&l1=-1))&((x0&x1&l0=1)|(x0&x2&l1=2&x1));//perfect%n");
       out.format("formula f4=(!x0)&((a1&!a2&l0=1)|(a2&l1=2&l0=1));%n");
       out.format("formula stability=f4|f3|f2|f1;%n%n");*/
       
       out.format("%n%n// rewards (to calculate expected number of steps)%nrewards \"steps\"%n  true : 1;%nendrewards%n%ninit%n   true%nendinit%n%n// label - stable configurations%nlabel \"stable\" = stability;%n%n");
       
     
   }
  
   

   public void CloseFile(){
       out.close();

   }


}
