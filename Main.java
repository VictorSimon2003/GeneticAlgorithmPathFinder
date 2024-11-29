import java.io.File;

public class Main {
   static PMXThread[] PMXThreads;

   public static void main(String[] args) throws InterruptedException {
      if (args.length == 0) {
         System.out.println("Invalid argument number.");
         System.exit(0);
      }

      AppInfo.shortesPath = Integer.MAX_VALUE;
      AppInfo.killThreads = false;
      AppInfo.noThreads = Integer.parseInt(args[1]);
      AppInfo.runTime = Integer.parseInt(args[2]);
      AppInfo.totalNoPopulation = Integer.parseInt(args[3]);
      AppInfo.mutationProbability = Double.parseDouble(args[4]);
      AppInfo.mergerFrequency = Integer.parseInt(args[5]);
      AppInfo.noIterations = 0;
      System.out.println("Arguments are:\nnoThreads: " + AppInfo.noThreads + "\nrunTime: " + AppInfo.runTime + "s\npopulation: " + AppInfo.totalNoPopulation + "\nmutationProbability: " + AppInfo.mutationProbability + "%\nMerger frequency:" + AppInfo.mergerFrequency);
      if (AppInfo.noThreads <= 0 || AppInfo.runTime <= 0 || AppInfo.totalNoPopulation <= 0 || AppInfo.mutationProbability <= 0.0D || AppInfo.mergerFrequency <= 0) {
         System.out.println("Invalid arguments");
         System.exit(0);
      }

      File fptr = new File(args[0]);
      ReadFile readFile = new ReadFile(fptr);
      AppInfo.matrixSize = readFile.getMatrixSize();
      readFile.setMatrix();
      AppInfo.matrix = readFile.getMatrix();
      readFile.closeScanner();
      if (AppInfo.matrix == null) {
         System.out.println("Matrix is null. Exiting.");
         System.exit(0);
      }

      int threadPopulation;
      for(int i = 0; i < readFile.getMatrixSize(); ++i) {
         for(threadPopulation = 0; threadPopulation < readFile.getMatrixSize(); ++threadPopulation) {
            System.out.print(AppInfo.matrix[i][threadPopulation] + " ");
         }

         System.out.println();
      }

      boolean populationDividableByThreads = false;
      threadPopulation = AppInfo.totalNoPopulation / AppInfo.noThreads;
      int remainingThreadPopulation = false;
      int remainingThreadPopulation = AppInfo.totalNoPopulation % AppInfo.noThreads;
      if (remainingThreadPopulation == 0) {
         populationDividableByThreads = true;
      }

      AppInfo.threadNoPopulation = new int[AppInfo.noThreads];

      int i;
      for(i = 0; i < AppInfo.noThreads; ++i) {
         if (populationDividableByThreads) {
            AppInfo.threadNoPopulation[i] = threadPopulation;
         } else if (!populationDividableByThreads && i + 1 == AppInfo.noThreads) {
            AppInfo.threadNoPopulation[i] = threadPopulation + remainingThreadPopulation;
         } else {
            AppInfo.threadNoPopulation[i] = threadPopulation;
         }
      }

      System.out.println("threadNoPopulation: ");

      for(i = 0; i < AppInfo.noThreads; ++i) {
         int var10001 = AppInfo.threadNoPopulation[i];
         System.out.print(var10001 + ",");
      }

      System.out.println();
      System.out.println("ITERATIONS: " + AppInfo.noIterations);
      AppInfo.PMXProcessingThreads = new PMXThread[AppInfo.noThreads];

      for(i = 0; i < AppInfo.noThreads; ++i) {
         AppInfo.PMXProcessingThreads[i] = new PMXThread(AppInfo.threadNoPopulation[i]);
         AppInfo.PMXProcessingThreads[i].start();
      }

      PMXPopulationMergerThread mergerThread = new PMXPopulationMergerThread();

      try {
         mergerThread.start();
      } catch (Exception var16) {
         var16.printStackTrace();
      }

      AppInfo.mergePopulation = false;
      PMXThread[] PMXProcessingThreads = new PMXThread[AppInfo.noThreads];
      AppInfo.mergerFrequencyFloat = (float)((double)AppInfo.runTime / ((double)AppInfo.mergerFrequency * 0.1D));

      int var10;
      for(int i = 0; i < AppInfo.runTime; i = (int)((float)i + AppInfo.mergerFrequencyFloat)) {
         Thread.sleep((long)(1000.0F * AppInfo.mergerFrequencyFloat));
         PMXThread[] var9 = AppInfo.PMXProcessingThreads;
         var10 = var9.length;

         int var11;
         PMXThread var10000;
         for(var11 = 0; var11 < var10; ++var11) {
            var10000 = var9[var11];
            PMXThread.releaselMerger();
         }

         var9 = AppInfo.PMXProcessingThreads;
         var10 = var9.length;

         for(var11 = 0; var11 < var10; ++var11) {
            var10000 = var9[var11];

            try {
               PMXThread.aquireMerger();
            } catch (InterruptedException var15) {
               throw new RuntimeException(var15);
            }
         }

         AppInfo.mergePopulation = true;
      }

      Thread.sleep((long)(1000.0F * ((float)AppInfo.runTime % AppInfo.mergerFrequencyFloat)));
      AppInfo.killThreads = true;
      PMXThread[] var20 = AppInfo.PMXProcessingThreads;
      int var21 = var20.length;

      for(var10 = 0; var10 < var21; ++var10) {
         PMXThread currentPMXThread = var20[var10];

         try {
            currentPMXThread.join();
         } catch (SecurityException var14) {
            throw new RuntimeException(var14);
         }
      }

      System.out.println("No iterations: " + AppInfo.noIterations);
      System.out.println("All threads terminated. Shortest path: " + AppInfo.shortesPath);
   }
}
