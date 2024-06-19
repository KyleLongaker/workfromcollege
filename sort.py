#Group members: Katelyn Juhl, Kyle Longaker

#Katelyn
def insertion_sort(list_of_items):
     for i in range(1, len(list_of_items)):
        cur_val = list_of_items[i]
        cur_pos = i

        while cur_pos > 0 and list_of_items[cur_pos - 1] > cur_val:
            list_of_items[cur_pos] = list_of_items[cur_pos - 1]
            cur_pos = cur_pos - 1
        list_of_items[cur_pos] = cur_val
          
#Big O runtime: O(n^2), worst case O(n^3) due to possible extra O(n) time it takes to look thoguh values. 

##Test code:
list_of_items = [4, 2, 3, 17, 7, 1, 34, 5, 20]
insertion_sort(list_of_items)
print("After insertion sort:", list_of_items)

#Katelyn
def bubble_sort(list_of_items):
    n = len(list_of_items)
    for i in range(n):
        for j in range(0, n-i-1):
            if list_of_items[j] > list_of_items[j+1]:
                list_of_items[j], list_of_items[j+1] = list_of_items[j+1], list_of_items[j]
    return list_of_items

# Big O runtime: O(n^2), worst case O(n^3) due to every comparison causing an exchange. 

##Test code:
list_of_items = [7, 32, 33, 17, 47]
bubble_sort(list_of_items)
print(list_of_items)

###########################################
#Kyle
    
def selectionSort(list_of_items):
    """
    This function implements the selection sort algorithm.
    It iterates through the list, and for each position, it finds the minimum element from the unsorted part
    and swaps it with the element at the current position.
    
    Time Complexity: O(n^2), where n is the number of items in the list.
    """
    size = len(list_of_items)
    for ind in range(size):
        min_index = ind
        for j in range(ind + 1, size):
            if list_of_items[j] < list_of_items[min_index]:
                min_index = j
        # swapping the elements to sort the array
        list_of_items[ind], list_of_items[min_index] = list_of_items[min_index], list_of_items[ind]

# Example usage
arr = [-2, 45, 0, 11, -9, 88, -97, -202, 747]
selectionSort(arr)
arr_selection = [-2, 45, 0, 11, -9, 88, -97, -202, 747]
print()
print("Given array for selection sort is", arr)
print(arr_selection)
selectionSort(arr_selection)
print("\nSorted array by selection sort is ")
print(arr_selection)
print()
###############################################
#Kyle
def mergeSort(arr):
    """
    This function implements the merge sort algorithm using recursion.
    It divides the list into halves, sorts each half, and merges them back together.
    
    Time Complexity: O(n log n), where n is the number of items in the list.
    """
    if len(arr) > 1:
        mid = len(arr) // 2
        L = arr[:mid]
        R = arr[mid:]

        mergeSort(L)
        mergeSort(R)

        i = j = k = 0

        while i < len(L) and j < len(R):
            if L[i] < R[j]:
                arr[k] = L[i]
                i += 1
            else:
                arr[k] = R[j]
                j += 1
            k += 1

        while i < len(L):
            arr[k] = L[i]
            i += 1
            k += 1

        while j < len(R):
            arr[k] = R[j]
            j += 1
            k += 1

def printList(arr):
    """
    Utility function to print the list elements.
    """
    for i in arr:
        print(i, end=" ")
    print()

# Driver Code
if __name__ == '__main__':
    arr = [12, 11, 13, 5, 6, 7]
    mergeSort(arr)
   

arr_merge = [12, 11, 13, 5, 6, 7]
print("Given array for merge sort is")
printList(arr_merge)
mergeSort(arr_merge)
print("\nSorted array by merge sort is ")
printList(arr_merge)