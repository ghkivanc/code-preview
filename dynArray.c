#include <stdio.h>

void main()
{
    int** const array = NULL;

    create_array(&array, 5, 5);

    for(int i = 0; i < 5; i++)
    {
        for(int j = 0; j < 5; j++)
        {
            array[i][j] = 1;
        }
    }

    print_array(array, 5, 5);

    array[2][2] = 5;

    print_array(array, 5, 5);

    free_array(array, 5);

    //print_array(&array, sizeof(array)/sizeof(array[0]), sizeof(array[0])/sizeof(int));
}

void create_array(int*** array, int rows, int cols)
{
    *array = (int**)malloc(rows*sizeof(int*));

    for(int i = 0; i < cols; i++)
    {
        (*array)[i] = (int*)malloc(cols*sizeof(int));
    }
}

void free_array(int** array, int rows)
{
    for(int i = 0; i < rows; i++)
    {
        free(array[i]);
    }

    free(array);
}

void print_array(int** array, int rows, int cols)
{
    for(int i = 0; i < rows; i++)
    {
        for(int j = 0; j < cols; j++)
        {
            printf("%d ", array[i][j]);
        }
        printf("\n");
    }

    printf("-----------------------------------------\n");
}