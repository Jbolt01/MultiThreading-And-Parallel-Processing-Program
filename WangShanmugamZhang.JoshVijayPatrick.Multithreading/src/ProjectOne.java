import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ProjectOne {
    public static void main(String[] args) {
        final int sizeOfList = 200000000;
        int[] parallelList = new int[sizeOfList];
        int[] sequentialList = new int[sizeOfList];
      
        for (int i = 0; i < parallelList.length; i++) {
            parallelList[i] = sequentialList[i] = (int) (Math.random() * 10000000);
        }
        
        long startTime = System.currentTimeMillis();
        parallelQuickSort(parallelList);
        long endTime = System.currentTimeMillis();
        System.out.println("\nParallel quicksort took " + (endTime - startTime) + " milliseconds.");
      
        startTime = System.currentTimeMillis();
        sequentialQuickSort(sequentialList);
        endTime = System.currentTimeMillis();
        System.out.println("\nSequential quicksort took " + (endTime - startTime) + " milliseconds");
    }
     
    public static void sequentialQuickSort(int[] list) {
        sequentialQuickSort(list, 0, list.length - 1);
    }
      
    private static void sequentialQuickSort(int[] list, int start, int end) {
        if (end > start) {
            int splitIndex = split(list, start, end);
            sequentialQuickSort(list, start, splitIndex - 1);
            sequentialQuickSort(list, splitIndex + 1, end);
        }
    }
      
    private static int split(int[] list, int start, int end) {
        int pivot = list[start];
        int small = start + 1;
        int big = end;
      
        while (big > small) {
            while (small <= big && list[small] <= pivot)
                small++;
      
            while (small <= big && list[big] > pivot)
                big--;
      
            if (big > small) {
                int temp = list[big];
                list[big] = list[small];
                list[small] = temp;
            }
        }
      
        while (big > start && list[big] >= pivot)
            big--;
      
        if (pivot > list[big]) {
            list[start] = list[big];
            list[big] = pivot;
            return big;
        } else {
            return start;
        }
    }

    public static void parallelQuickSort(int[] list) {
        RecursiveAction mainTask = new SortTask(list, 0, list.length - 1);
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(mainTask);
    }
      
    private static class SortTask extends RecursiveAction {
        private int[] list;
        private int start;
        private int end;
      
        SortTask(int[] list, int start, int end) {
            this.list = list;
            this.start = start;
            this.end = end;
        }
      
        @Override
        protected void compute() {
            if (end > start) {    
                int splitIndex = split(list, start, end);
                invokeAll(new SortTask(list, start, splitIndex - 1), new SortTask(list, splitIndex + 1, end));
            }
        }
    }
}