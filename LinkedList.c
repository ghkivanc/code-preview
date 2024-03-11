#include <stdlib.h>
#include <stdio.h>

struct Node 
{
    int data;    
    struct Node* next_ptr;
};

typedef struct Node Node;
typedef Node* Node_ptr;

void insert(int data, Node_ptr* node);
int delete(int data, Node_ptr* node);
int isEmpty(Node_ptr node);
void printList(Node_ptr current_node);
void reverse(Node_ptr* node);

void main()
{
    Node_ptr head = NULL;

    printf("is empty: %d\n", isEmpty(head));

    insert(5, &head);
    insert(6, &head);
    insert(1, &head);
    insert(10, &head);

    printf("is empty: %d\n", isEmpty(head));
    printList(head);

    reverse(&head);

    printList(head);

    delete(6, &head);
    delete(1, &head);

    printf("is empty: %d\n", isEmpty(head));
    printList(head);
    

}

void reverse(Node_ptr* node)
{
    Node_ptr previous = NULL;
    Node_ptr current = *node;
    Node_ptr next = NULL;

    while(current != NULL)
    {
        next = current->next_ptr;

        current->next_ptr = previous;
        
        previous = current;

        current = next;
    }

    *node = previous;
}

void insert(int data, Node_ptr* node)
{
    Node_ptr new_ptr = malloc(sizeof(Node));

    if(new_ptr != NULL)
    {
        new_ptr->data = data;
        new_ptr->next_ptr = NULL;
    }

    Node_ptr previous = NULL;
    Node_ptr current = *node;

    while((current != NULL) && (data >= current->data))
    {
        previous = current;
        current = current->next_ptr;
    }

    if(previous == NULL)
    {
        new_ptr->next_ptr = current; 
        *node = new_ptr;
    }
    else if(current == NULL)
    {
        previous->next_ptr = new_ptr;
    }
    else
    {
        previous->next_ptr = new_ptr;
        new_ptr->next_ptr = current;
    }
}

int delete(int data, Node_ptr* head)
{
    if(data == (*head)->data)
    {
        Node_ptr temp = *head;

        *head = (*head)->next_ptr;
        free(temp);
        return 1;
    }
    else
    {
        Node_ptr previous = *head;
        Node_ptr current = previous->next_ptr;

        while(current != NULL && current->data != data)
        {
            previous = current;
            current = current->next_ptr;
        }

        if(current != NULL)
        {
            previous->next_ptr = current->next_ptr;
            free(current);
            return 1;
        }

        return 0;
    }
}

int isEmpty(Node_ptr head)
{
    return head == NULL;
}

void printList(Node_ptr head)
{
    if(isEmpty(head))
    {
        puts("list is empty\n");
    }
    else
    {
        puts("list is:\n");

        Node_ptr current = head;

        while(current != NULL)
        {
            printf("%d --> ", current->data);
            current= current->next_ptr;
        }
    }

    puts("END\n");
}
