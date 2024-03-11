public class quickSort {
    public static void QuickSort(int[] array, int left, int right)
    {
        if(right > left)
        {
            int middle = partition(array, left, right);

            QuickSort(array, left, middle - 1);
            QuickSort(array, middle + 1, right);
        }
    }

    private static int partition(int[] array, int left, int right)
    {
        int left1 = array[left];
        int right1 = array[right];
        int middle = array[left + (right - left)/2];
        int pivotElement = middle + right1 + left1 - (int)(Math.max(right1, Math.max(left1, middle)) + Math.min(right1, Math.min(left1, middle)));
        int pivot;

        if(pivotElement == left1)
        {
            pivot = left;
        }
        else if(pivotElement == right1)
        {
            pivot = right;
        }
        else
        {
            pivot = left + (right - left)/2;
        }

        exchangeElements(array, right, pivot);

        pivot = right;
        
        right -= 1;
        boolean greater = false;
        boolean smaller = false;
    
        while(left <= right)
        {
            if(array[right] < pivotElement)
            {
                smaller = true;
            }
            else
            {
                right -= 1;
            }

            if(array[left] > pivotElement)
            {
                greater = true;
            }
            else
            {
                left += 1;
            }

            if(greater && smaller)
            {
                exchangeElements(array, left, right);
                right -= 1;
                left += 1;
            }

            greater = false;
            smaller = false;
        }

        /*System.out.println(pivotElement);
        System.out.println(left);*/

        exchangeElements(array, left, pivot);

        return left;
    }

    private static void exchangeElements(int[] array, int i, int j)
    {
        int temp = array[i];
        
        array[i] = array[j];
        array[j] = temp;
    }
}
