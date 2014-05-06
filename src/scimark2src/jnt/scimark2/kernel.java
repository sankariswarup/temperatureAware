package scimark2src.jnt.scimark2;
import org.apache.commons.math.stat.regression.SimpleRegression;
import java.util.List;
import java.util.ArrayList;
import org.apache.commons.math.stat.ranking.NaNStrategy;
public class kernel
{
	// each measurement returns approx Mflops
static List mflops= new ArrayList();
       static List temp = new ArrayList();

	public static double measureFFT(int N, double mintime, Random R)
	{
		// initialize FFT data as complex (N real/img pairs)
            long starttime = System.currentTimeMillis();
            System.out.println("task starttime"+starttime);
		double x[] = RandomVector(2*N, R);
		double oldx[] = NewVectorCopy(x);
		long cycles = 1;
		Stopwatch Q = new Stopwatch();
                FFT fft = new FFT();
                Thread t = new Thread(fft);
                t.start();
                ProbeTemp pt = new ProbeTemp();
		while(true)
		{
			Q.start();
			for (int i=0; i<cycles; i++)
			{
				FFT.transform(x);	// forward transform
				FFT.inverse(x);		// backward transform
			}
			Q.stop();
			if (Q.read() >= mintime)
				break;

			cycles *= 2;
		 mflops.add(FFT.num_flops(N)*cycles/ Q.read() * 1.0e-6);
		temp.add(pt.executeProcess("sensors"));
                double slope =checkSlope(mflops,temp);
                System.out.println(" the slope is" + slope);
               if(Constants.isRun)
               {
                 try
               {
                if(slope>0.25)
                {
                    System.out.println("thread is sleeping");
                    t.setPriority(t.MIN_PRIORITY);
                    t.sleep(Constants.TIMEOUT);
                    pt.executeProcess("sensors");
                }
                 }
               catch(InterruptedException ex)
               {
                   System.out.println(ex);
               }
               }
               }
		// approx Mflops

		final double EPS = 1.0e-10;
		if ( FFT.test(x) / N > EPS )
			return 0.0;
		t.stop();
                mflops.removeAll(mflops);
                temp.removeAll(mflops);
                double mflopsItem = FFT.num_flops(N)*cycles/ Q.read() * 1.0e-6;
                  long endtime = System.currentTimeMillis();
            System.out.println("task end time"+endtime);
            long duration = endtime - starttime;
            System.out.println("task Duration"+duration);
                return mflopsItem;
	
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
	public static double measureSOR(int N, double min_time, Random R)
	{
		double G[][] = RandomMatrix(N, N, R);
 long starttime = System.currentTimeMillis();
            System.out.println("task starttime"+starttime);
		Stopwatch Q = new Stopwatch();
		int cycles=1;
                SOR sor = new SOR();
                Thread t = new Thread(sor);
                t .start();
                ProbeTemp pt = new ProbeTemp();
                Thread pthread = new Thread(pt);
                pthread.start();
		while(true)
		{
			Q.start();
			SOR.execute(1.25, G, cycles);
			Q.stop();
			if (Q.read() >= min_time) break;

			cycles *= 2;
		mflops.add((SOR.num_flops(N, N, cycles) / Q.read() * 1.0e-6)+5);
		temp.add(pt.executeProcess("sensors"));

                double slope =checkSlope(mflops,temp);
                System.out.println(" the slope of SOR is" + slope);
                 if(Constants.isRun)
               {
               try
               {
                if(slope>0.25)
                {
                    System.out.println("thread is sleeping");
                    t.setPriority(t.MIN_PRIORITY);
                    t.sleep(Constants.TIMEOUT);
                    pt.executeProcess("sensors");
                }
               }
               catch(InterruptedException ex)
               {
                   System.out.println(ex);
               }}

			
		}
                  t.stop();
                pthread.stop();
                 mflops.removeAll(mflops);
                temp.removeAll(mflops);
              // approx Mflops
                  long endtime = System.currentTimeMillis();
            System.out.println("task end time"+endtime);
            long duration = endtime - starttime;
            System.out.println("task Duration"+duration);
		return SOR.num_flops(N, N, cycles) / Q.read() * 1.0e-6;
	
	}

	public static double measureMonteCarlo(double min_time, Random R)
	{
		Stopwatch Q = new Stopwatch();
            MonteCarlo mc = new MonteCarlo();
             long starttime = System.currentTimeMillis();
            System.out.println("task starttime"+starttime);
                Thread t = new Thread(mc);
                t .start();
                ProbeTemp pt = new ProbeTemp();
                Thread pthread = new Thread(pt);
                pthread.start();
		
		int cycles=1;
		while(true)
		{
			Q.start();
			MonteCarlo.integrate(cycles);
			Q.stop();
			if (Q.read() >= min_time) break;

			cycles *= 2;
		double numflops= MonteCarlo.num_flops(cycles) / Q.read() * 1.0e-6;
                        mflops.add(numflops);
		temp.add(pt.executeProcess("sensors"));
                System.out.println("size of mflops in mc"+numflops);

                double slope =checkSlope(mflops,temp);
                System.out.println(" the slope of montecarlo is" + slope);
                if(Constants.isRun)
               {try
               {
                if(slope > 0.25)
                {
                    System.out.println("montecarlo thread is sleeping");
                    t.setPriority(t.MIN_PRIORITY);
                    t.sleep(Constants.TIMEOUT);
                    pt.executeProcess("sensors");
                }
               }
               catch(InterruptedException ex)
               {
                   System.out.println(ex);
               }
               }
                }// approx Mflops
               double mflopsItem= MonteCarlo.num_flops(cycles) / Q.read() * 1.0e-6;
		  long endtime = System.currentTimeMillis();
            System.out.println("task end time"+endtime);
            long duration = endtime - starttime;
            System.out.println("task Duration"+duration);
               return mflopsItem;
		
	}


	public static double measureSparseMatmult(int N, int nz, 
			double min_time, Random R)
	{
		// initialize vector multipliers and storage for result
		// y = A*y;
            long starttime = System.currentTimeMillis();
            System.out.println("task starttime"+starttime);
		double x[] = RandomVector(N, R);
		double y[] = new double[N];

		// initialize square sparse matrix
		//
		// for this test, we create a sparse matrix wit M/nz nonzeros
		// per row, with spaced-out evenly between the begining of the
		// row to the main diagonal.  Thus, the resulting pattern looks
		// like
		//             +-----------------+
		//             +*                +
		//             +***              +
		//             +* * *            +
		//             +** *  *          +
		//             +**  *   *        +
		//             +* *   *   *      +
		//             +*  *   *    *    +
		//             +*   *    *    *  + 
		//             +-----------------+
		//
		// (as best reproducible with integer artihmetic)
		// Note that the first nr rows will have elements past
		// the diagonal.

		int nr = nz/N; 		// average number of nonzeros per row
		int anz = nr *N;   // _actual_ number of nonzeros

			
		double val[] = RandomVector(anz, R);
		int col[] = new int[anz];
		int row[] = new int[N+1];

		row[0] = 0;	
		for (int r=0; r<N; r++)
		{
			// initialize elements for row r

			int rowr = row[r];
			row[r+1] = rowr + nr;
			int step = r/ nr;
			if (step < 1) step = 1;   // take at least unit steps


			for (int i=0; i<nr; i++)
				col[rowr+i] = i*step;
				
		}

		Stopwatch Q = new Stopwatch();
                SparseCompRow scr = new SparseCompRow();
                Thread t = new Thread(scr);
                t .start();
                ProbeTemp pt = new ProbeTemp();
                Thread pthread = new Thread(pt);
                pthread.start();
		int cycles=1;
		while(true)
		{
			Q.start();
			SparseCompRow.matmult(y, val, row, col, x, cycles);
			Q.stop();
			if (Q.read() >= min_time) break;

			cycles *= 2;
		       mflops.add(SparseCompRow.num_flops(N, nz, cycles) / Q.read() * 1.0e-6);
		temp.add(pt.executeProcess("sensors"));

                double slope =checkSlope(mflops,temp);
                System.out.println(" the slope of sparsecomrow is" + slope);
               
                 if(Constants.isRun)
               {try
               {
                if(slope>0.25)
                {
                    System.out.println("sparsecomrow thread is sleeping");
                    t.setPriority(t.MIN_PRIORITY);
                    t.sleep(Constants.TIMEOUT);
                    pt.executeProcess("sensors");
                }
               }
               catch(InterruptedException ex)
               {
                   System.out.println(ex);
               }
               }	
		}
                double mflopsItem = SparseCompRow.num_flops(N, nz, cycles) / Q.read() * 1.0e-6;
                  t.stop();
                pthread.stop();
                  mflops.removeAll(mflops);
                temp.removeAll(mflops);
  long endtime = System.currentTimeMillis();
            System.out.println("task end time"+endtime);
            long duration = endtime - starttime;
            System.out.println("task Duration"+duration);
                // approx Mflops
		return mflopsItem;
		
	}


	public static double measureLU(int N, double min_time, Random R)
	{
		// compute approx Mlfops, or O if LU yields large errors
                 long starttime = System.currentTimeMillis();
            System.out.println("task starttime"+starttime);    
		double A[][] = RandomMatrix(N, N,  R);
		double lu[][] = new double[N][N];
		int pivot[] = new int[N];

		Stopwatch Q = new Stopwatch();
                LU luinst = new LU();
                Thread t = new Thread(luinst);
                t .start();
                ProbeTemp pt = new ProbeTemp();
                Thread pthread = new Thread(pt);
                pthread.start();
		int cycles=1;
		while(true)
		{
			Q.start();
			for (int i=0; i<cycles; i++)
			{
				CopyMatrix(lu, A);
				LU.factor(lu, pivot);
			}
			Q.stop();
			if (Q.read() >= min_time) break;

			cycles *= 2;
		 mflops.add(LU.num_flops(N) * cycles / Q.read() * 1.0e-6);
		temp.add(pt.executeProcess("sensors"));

                double slope =checkSlope(mflops,temp);
                System.out.println(" the slope of LU is" + slope);
               if(Constants.isRun)
               { try
               {
                if(slope>0.25)
                {
                    System.out.println("LU thread is sleeping");
                    t.setPriority(t.MIN_PRIORITY);
                    t.sleep(Constants.TIMEOUT);
                    pt.executeProcess("sensors");
                }
               }
               catch(InterruptedException ex)
               {
                   System.out.println(ex);
               }

               }
		}


		// verify that LU is correct
		double b[] = RandomVector(N, R);
		double x[] = NewVectorCopy(b);

		LU.solve(lu, pivot, x);

		final double EPS = 1.0e-12;
		if ( normabs(b, matvec(A,x)) / N > EPS )
			return 0.0;
                 double mflopsItem = LU.num_flops(N) * cycles / Q.read() * 1.0e-6;
                  t.stop();
                pthread.stop();
                  mflops.removeAll(mflops);
                temp.removeAll(mflops);        

		// else return approx Mflops
		//
                  long endtime = System.currentTimeMillis();
            System.out.println("task end time"+endtime);
            long duration = endtime - starttime;
            System.out.println("task Duration"+duration);
		return mflopsItem;
	}


  private static double[] NewVectorCopy(double x[])
  {
		int N = x.length;

		double y[] = new double[N];
		for (int i=0; i<N; i++)
			y[i] = x[i];

		return y;
  }
	
  private static void CopyVector(double B[], double A[])
  {
		int N = A.length;

		for (int i=0; i<N; i++)
			B[i] = A[i];
  }


  private static double normabs(double x[], double y[])
  {
		int N = x.length;
		double sum = 0.0;

		for (int i=0; i<N; i++)
			sum += Math.abs(x[i]-y[i]);

		return sum;
  }

  private static void CopyMatrix(double B[][], double A[][])
  {
        int M = A.length;
        int N = A[0].length;

		int remainder = N & 3;		 // N mod 4;

        for (int i=0; i<M; i++)
        {
            double Bi[] = B[i];
            double Ai[] = A[i];
			for (int j=0; j<remainder; j++)
                Bi[j] = Ai[j];
            for (int j=remainder; j<N; j+=4)
			{
				Bi[j] = Ai[j];
				Bi[j+1] = Ai[j+1];
				Bi[j+2] = Ai[j+2];
				Bi[j+3] = Ai[j+3];
			}
		}
  }

  private static double[][] RandomMatrix(int M, int N, Random R)
  {
  		double A[][] = new double[M][N];

        for (int i=0; i<N; i++)
			for (int j=0; j<N; j++)
            	A[i][j] = R.nextDouble();
		return A;
	}

	private static double[] RandomVector(int N, Random R)
	{
		double A[] = new double[N];

		for (int i=0; i<N; i++)
			A[i] = R.nextDouble();
		return A;
	}

	private static double[] matvec(double A[][], double x[])
	{
		int N = x.length;
		double y[] = new double[N];

		matvec(A, x, y);

		return y;
	}

	private static void matvec(double A[][], double x[], double y[])
	{
		int M = A.length;
		int N = A[0].length;

		for (int i=0; i<M; i++)
		{
			double sum = 0.0;
			double Ai[] = A[i];
			for (int j=0; j<N; j++)
				sum += Ai[j] * x[j];

			y[i] = sum;
		}
	}

}
