/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scimark2src.jnt.scimark2;
import java.util.*;
import org.apache.commons.math.stat.*;
import java.io.*;
/**
 *
 * @author sza0033
 */
public class TrainingSetReductionAlgorithm {
    public TrainingSetReductionAlgorithm()
    {
        
            }
  /*  private List GreedyTrainingSetSelector(double[] s,double[] f)
    {
        int n = s.length;
        List A= new ArrayList();
        Examples a1 = new Examples();
        A.add(a1);
        int k =1;
        double minValue;
        for(int m= 2;m<= n; m++)
        {
          minValue = StatUtils.min(f);
          if(s[m] <= minValue )
          {
            A.add(a1);
            k = m;
         }
        }
        return A;
    }*/
    public void GreedyTrainingSetSelector()
    {
    String path ="/home/sza0033/svm_light_linux/Input/";
    String WritePath="/home/sza0033/svm_light_linux/data/";

    File file = new File(path);
    FilenameFilter filter = new FilenameFilter() {
    public boolean accept(File dir, String name) {
        boolean value =(!name.contains("final") && !name.contains("temp"));
        return value ;
    }
    };
    String[] children = file.list(filter);
    File tempFile1, finalFile1 = null;
   for(int k=0;k<children.length;k++)
   {
    String tempFileName = "temp"+ children[k];
    //File tempFile = new File( tempFileName);
    String finalfileName = "final"+ children[k];
    //File finalFile = new File( finalfileName);
    tempFile1 = new File(tempFileName);
    finalFile1 = new File(finalfileName);
   
    try{
     boolean tempSuccess = tempFile1.createNewFile();
    boolean finalSuccess = finalFile1.createNewFile();
    }catch(IOException ex)
    {
    }
    FileReader fileReader = null;
    BufferedReader bufferedReader = null;
    FileReader tempfileReader = null;
    BufferedReader tempbufferedReader = null;
    DataInputStream dataInputStream = null;
    FileWriter tempfileWriter = null;
    BufferedOutputStream tempbufferedOutputStream = null;
    DataOutputStream tempdataOutputStream = null;
    FileWriter finalfileWriter = null;
    BufferedOutputStream finalbufferedOutputStream = null;
    DataOutputStream finaldataOutputStream = null;
   
    double[] f =new double[47000];
    double[] s =new double[47000];
    int length =0;
    try {
     fileReader= new FileReader(path+children[k]);
      bufferedReader  = new BufferedReader(fileReader);
     
      // dataInputStream = new DataInputStream(butefferedInputStream);
       tempfileWriter= new FileWriter( WritePath+tempFile1);
        
      //tempbufferedReader  = new BufferedReader(tempfileReader);
    //tempbufferedOutputStream  = new BufferedOutputStream(tempfileWriter);
      //tempdataOutputStream = new DataOutputStream(tempbufferedOutputStream);
       finalfileWriter= new FileWriter( WritePath+finalFile1);
      //finalbufferedOutputStream  = new BufferedOutputStream(finalfileOutputStream);
       //finaldataOutputStream = new DataOutputStream(finalbufferedOutputStream);
       String line = new String();
       String templine = new String();
      double trainingTime, minValue;
      int setSize= 0;
       while ((line = bufferedReader.readLine())!=null) {

          
        //  if(line.startsWith("+1"))
         // {
          Random R = new Random(10,20);
              //String [] str =line.split(" ");
              //for(int i=0;i<str.length;i++)
              //{
                  setSize++;
                  //line.replace("\n","");
                  tempfileWriter.write(line+"\n");
                  if(setSize ==20)
                  {
                      tempfileWriter.close();
                  setSize = 0;
                  trainingTime = Double.valueOf(getTrainingTime("/home/sza0033/svm_light_linux/svm_learn /home/sza0033/svm_light_linux/data/"+tempFileName+" "+tempFileName+"model"));
                  //System.out.println(trainingTime);
                  
                  f[length] = trainingTime;
                  s[length] = R.nextDouble() ;
                   minValue = StatUtils.max(f)/3;
                   System.out.println(minValue);
                   System.out.println(s[length]);
                  
                    if(s[length] < minValue )
                    {
                        length++;
                        tempfileReader= new FileReader(WritePath+tempFile1);
                        tempbufferedReader  = new BufferedReader(tempfileReader);
                         while ((templine = tempbufferedReader.readLine())!=null)
                         {
                                //templine.replace("\n","");
                                finalfileWriter.write(templine+"\n");       }           
                    try{
                       
                       
                        tempFile1.delete();
                        boolean tempSuccess = tempFile1.createNewFile();
                      
                        
                    }catch(IOException ex)
                    {
                        }
                    }  tempfileWriter= new FileWriter( WritePath+tempFile1);
                  }
               //}
          //}
          
          //System.out.println(dataInputStream.readLine());
      }
      
      fileReader.close();
      bufferedReader.close();
      tempfileReader.close();
      tempbufferedReader.close();
      
       finalfileWriter.close();
      //dataInputStream.close();
     // tempfileWriter.close();
     // tempbufferedOutputStream.close();
      //tempdataOutputStream.close();
     // finalfileWriter.close();
      //finalbufferedOutputStream.close();
      //finaldataOutputStream.close();

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    
   }
   
    
    
    }
    private void FindBestExample()
    {
        if(FindTrainingTime()<=ChooseTrainingTime())
            AddExampleToFile();
                
    }
    private void AddExampleToFile()
    {
        
    }
    private double ChooseTrainingTime()
    {
        return Constants.TRAININGTIME;
    }
    private  double FindTrainingTime()
    {
        return Double.valueOf(getTrainingTime("/home/sza0033/svm_light_linux/svm_learn /home/sza0033/svm_light_linux/ComputingPower.dat model"));
  
    }
    private void MinimizeTrainingTime()
    {
        
    }
    public static void main(String args[])
        {
         TrainingSetReductionAlgorithm  ts= new TrainingSetReductionAlgorithm ();
         ts.GreedyTrainingSetSelector();

        }
    public double getTrainingTime(String processName)
  {
      try
        {
            Stopwatch Q = new Stopwatch();
            Q.start();
            Random R = new Random(1000,2000);
            double ttime =0.00;
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(processName);
            java.io.InputStream stderr = proc.getInputStream();
            java.io.InputStreamReader isr = new java.io.InputStreamReader(stderr);
            java.io.BufferedReader br = new java.io.BufferedReader(isr);
            String line = null;
            String trainingTime ="0.00";
            
            while ( (line = br.readLine()) != null)
            {
                if(line.indexOf("cpu-seconds:")>0)
                trainingTime =line.substring((line.indexOf("cpu-seconds:")+13),(line.indexOf("cpu-seconds:")+17));
                //System.out.println(line);
            }
            Q.stop();
            int exitVal = proc.waitFor();
            ttime = Q.read()* R.nextDouble();
            //System.out.println(ttime);
            return ttime;
        } catch (Throwable t)
          {
            t.printStackTrace();
            return 0.00;
          }
      
  }
    
}
