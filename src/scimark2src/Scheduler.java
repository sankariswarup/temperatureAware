/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scimark2src;

import java.sql.Time;
import org.apache.commons.math.stat.regression.SimpleRegression;
import java.util.List;
import java.util.ArrayList;
import java.util.Timer;
import javax.xml.datatype.Duration;
/**
 *
 * @author swaroop
 */
public class Scheduler {
    public void TaskScheduler()
    {
        System.out.println("starting scheduler");
        
    }
    	public static void main(String args[])
        {
         List taskList = TasksList();
         System.out.println("starting scheduler");
         SlopeGradientScheduler(taskList,0,TasksList().size());
         System.out.println("ENDing scheduler");

        }
    private static void SlopeGradientScheduler(List taskList,int p, int r)   {
       List mflops= new ArrayList();
       List temp = new ArrayList();
       ProbeTemp pt = new ProbeTemp();
        Random generator = new Random();
        double mflopsItem ;
        List tasksList =TasksList();
        if(p < r)
        {
            int q = Math.abs((p+r)/2);
            SlopeGradientScheduler(taskList, p, q);
            SlopeGradientScheduler(taskList, q+1,r );
        }
        else
        {
           double min_time = Constants.RESOLUTION_DEFAULT;

		int FFT_size = Constants.FFT_SIZE;
		int SOR_size =  Constants.SOR_SIZE;
		int Sparse_size_M = Constants.SPARSE_SIZE_M;
		int Sparse_size_nz = Constants.SPARSE_SIZE_nz;
		int LU_size = Constants.LU_SIZE;
                Random R = new Random(Constants.RANDOM_SEED);
            long starttime = System.currentTimeMillis();
            System.out.println("task starttime"+starttime);
            //kernel.measureFFT( FFT_size, min_time, R);
            //kernel.measureSOR( SOR_size, min_time, R);
            kernel.measureMonteCarlo(min_time, R);
            //kernel.measureSparseMatmult( Sparse_size_M,
					//Sparse_size_nz, min_time, R);
            //kernel.measureLU( LU_size, min_time, R);
            long endtime = System.currentTimeMillis();
            System.out.println("task end time"+endtime);
            long duration = endtime - starttime;
            System.out.println("task Duration"+duration);
             /*long starttime = System.currentTimeMillis();
        mflops.add(kernel.measureFFT( FFT_size, min_time, R));
        temp.add(pt.executeProcess("sensors"));
        double slope = checkSlope(mflops,temp);
        if(i==0)
        {
            try
            {
            Thread taskThread = new Thread();
            System.out.println("thread is sleeping");
            taskThread.setPriority(taskThread.MIN_PRIORITY);
            taskThread.sleep(Constants.TIMEOUT);
            } catch(InterruptedException ex)
               {
                   System.out.println(ex);
               }
        }
        if(slope>0.25)
                {
            try
            {
            //long starttime = System.currentTimeMillis();
            Task task = (Task)tasksList.get(i);
            System.out.println("task starttime"+starttime);
            System.out.println("task scheduled start"+task.startTime);
            System.out.println("task scheduled end"+task.endTime);

            Thread taskThread = new Thread();
            System.out.println("thread is sleeping");
            taskThread.setPriority(taskThread.MIN_PRIORITY);
            taskThread.sleep(Constants.TIMEOUT);
            long endtime = System.currentTimeMillis();
            System.out.println("task starttime"+endtime);
            long duration = endtime - starttime;
            pt.executeProcess("sensors");
            System.out.println("task Duration"+duration);
               
                }
               catch(InterruptedException ex)
               {
                   System.out.println(ex);
               }
            }
        else
        {
             Task task = (Task)tasksList.get(i);
            // long starttime = System.currentTimeMillis();
            System.out.println("task starttime"+starttime);
           
            long endtime = System.currentTimeMillis();
            System.out.println("task end time"+endtime);
            long duration = endtime - starttime;
            System.out.println("task Duration"+duration);
        }*/
        
        }
    }
    private static List TasksList()
    {
        List taskList = new ArrayList();
        Task t1 = new Task();
        t1.startTime =1;
        t1.endTime = 3;
        taskList.add(t1);
        Task t2 = new Task();
        t2.startTime =2;
        t2.endTime = 5;;
        taskList.add(t2);
        Task t3 = new Task();
        t3.startTime =4;
        t3.endTime = 10;
         taskList.add(t3);
        Task t4 = new Task();
        t4.startTime =5;
        t4.endTime = 11;
         taskList.add(t4);
        Task t5 = new Task();
        t1.startTime =7;
        t1.endTime = 12;
         taskList.add(t5);
        Task t6 = new Task();
        t6.startTime =8;
        t6.endTime = 16;
         taskList.add(t6);
        Task t7 = new Task();
        t7.startTime =9;
        t7.endTime = 18;
         taskList.add(t7);
        Task t8 = new Task();
        t8.startTime =10;
        t8.endTime = 13;
         taskList.add(t8);
        Task t9 = new Task();
        t9.startTime =11;
        t9.endTime = 19;
         taskList.add(t9);
        Task t10 = new Task();
        t10.startTime =12;
        t1.endTime = 23;
         taskList.add(t10);
         return taskList;
        }
    private static double checkSlope(List mFlops, List temp)
        {
            SimpleRegression sr = new SimpleRegression();
            for(int i=0;i<mFlops.size();i++)
            try{
                double d = Double.valueOf(temp.get(i).toString().trim()).doubleValue();
                System.out.println("double d = " + d);

                System.out.println("Mflops = " + Double.valueOf(mFlops.get(i).toString()));

            sr.addData(d,Double.valueOf(mFlops.get(i).toString()));

   }
              catch (NumberFormatException e){
            System.out.println("NumberFormatException: " + e.getMessage());
              }
            Double slope = sr.getSlope();
            if(Double.valueOf(slope).equals(Double.NaN))
                slope =0.26;
            double regression = sr.getIntercept();
            double confidence = sr.getN();
            double relatedness= sr.getRSquare();
            return slope;
           }
}
