public class MergeSort {

    public static void main(String[] args) {
        int[] array = {12,11,1,35,6,7, 1, 5, 6, 53, 35, 53, 246, 45723, 2537, 253143, 4752, 5277345, 45272, 124321, 6743, 42514, 5256, 245, 647};

        MergeSort sort = new MergeSort();
        
        sort.Sort(array, 0, array.length - 1);

        for(int i:array)
        {
            System.out.print(i + " ");
        }
    }
    
    
    public void Sort(int[] array, int start, int stop)
    {
        if(start < stop)
        {
            int middle = (start + stop)/2;

            Sort(array, start, middle);
            Sort(array, middle + 1, stop);

            Merge(array, start, middle, stop);
        }
    }
    
    public void Merge(int[] array, int start, int middle, int stop)
    {

        int[] left = new int[middle - start + 1];
        int[] right = new int[stop - middle];

        int i = 0, j = 0;

        for(int x = 0; x < left.length; x++)
        {
            left[x] = array[start + x];
        }

        for(int x = 0; x < right.length; x++)
        {
            right[x] = array[middle + 1 + x];
        }
        

        int index = start;

        while((i < right.length) && (j < left.length))
        {
            if(right[i] >= left[j])
            {
                array[index] = left[j];
                j++;
            }
            else
            {
                array[index] = right[i];
                i++;
            }

            index++;
        }


        while((i < right.length))
        {
            array[index] = right[i];
            i++;
            index++;
        }

        while((j < left.length))
        {
            array[index] = left[j];
            j++;
            index++;
        }
    }
}
