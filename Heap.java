public class Heap{
    
    int size = 0;

    int[] heapArray;

    public Heap(int size)
    {
        heapArray = new int[size];
    }

    public Heap(){}


    private int left(int i)
    {
        return 2*i + 1;
    }

    private int right(int i)
    {
        return (i + 1)*2;
    }

    private int up(int i)
    {
        return ((int)Math.ceil(((double)i)/2) == 0) ? 0:((int)Math.ceil((((double)i)/2) - 1));
    }

    public void buildMaxHeap(int[] array)
    {
        heapArray = array;
        size = heapArray.length;
        
        for(int i = (int)Math.floor(size/2); i >= 0; i--)
        {
            maxHeapify(i);   
        }
    }

    private void exchangeElements(int i, int j)
    {
        int temp = heapArray[i];
        
        heapArray[i] = heapArray[j];
        heapArray[j] = temp;
    }

    private void maxHeapify(int i)
    {
        int largest = i;

        while(true)
        {
            if((left(i) < size) && (heapArray[left(i)] > heapArray[largest]))
            {
                largest = left(i);
            }


            if((right(i) < size) && (heapArray[right(i)] > heapArray[largest]))
            {
                largest = right(i);
            }

            if(largest != i)
            {
                exchangeElements(i, largest);
                i = largest;
            }
            else
            {
                break;
            }
        }
    }

    public void heapSort()
    {
        int size1 = size;

        for(int i = size1 - 1; i >= 0; i--)
        {
            exchangeElements(0, i);
            size--;
            maxHeapify(0);
        }
    }

    public void changeElement(int i, int to)
    {
        if(i < size && i >= 0)
        {
            if(heapArray[i] < to)
            {
                heapArray[i] = to;
                siftUp(i);
            }
            else
            {
                heapArray[i] = to;
                maxHeapify(i);
            }
        }
        else
        {
            System.out.println("invalid index");
        }
    }

    public int removeElement(int index)
    {
        if(index < size && index >= 0)
        {
            int returnValue = heapArray[index];
            heapArray[index] = 0;
            maxHeapify(index);
            size--;
            return returnValue;
        }

        return -1;
    }

    public int getElement(int index)
    {
        if(index < size && index >= 0)
        {
            return heapArray[index];
        }

        return -1;
    }

    public int findMinimum()
    {
        int layers = (int)(Math.log(size)/Math.log(2));
        int index = 0;
        int minimum = Integer.MAX_VALUE;

        index = (int)Math.pow(2, layers) - 1; 

        for(int i = index; i < size; i++)
        {
            if(minimum > heapArray[i])
            {
                minimum = heapArray[i];
            }
        }


        return minimum;

    }

    public int[] printAndReturnBranch(int start, int end, int direction)
    {
        int[] returnArray = new int[end - start];

        
        
        if((start < size && start >= 0) && (end < size && end >= 0))
        {

            int x = start;

            if(direction == 1)
            {
                for(int i = start; i < end; i++)
                {
                    returnArray[i - start] = heapArray[x];
                    x = right(x);
                }
            }
            else if(direction == 0)
            {
                for(int i = start; i < end; i++)
                {
                    returnArray[i - start] = heapArray[x];
                    x = left(x);
                }
            }
        }

        System.out.print("\n[ ");
        for(int i = 0; i < returnArray.length; i++)
        {
            System.out.print(returnArray[i] + " ");
        }
        System.out.print("]\n");

        return returnArray;
    }

    public void insertElement(int element)
    {
        if(size == heapArray.length)
        {
            extendHeap();
        }

        heapArray[size] = element;

        siftUp(size);

        size++;
    }

    private void siftUp(int i)
    {

        while(heapArray[up(i)] < heapArray[i])
        {
            exchangeElements(up(i), i);
            i = up(i);
        }
    }

    public void print()
    {   
        System.out.print("\n[ ");
        for(int i = 0; i < size; i++)
        {
            System.out.print(heapArray[i] + " ");
        }
        System.out.println("]\n");
    }

    private void extendHeap()
    {
        int[] temp = new int[size*2];


        for(int i = 0; i < size; i++)
        {
            temp[i] = heapArray[i];
        }

        for(int i = size; i < size*2; i++)
        {
            temp[i] = 0;
        }

        heapArray = temp;
    }

    
}
