def simpleSort(numbers):
    print("\nInitial sequence =",", ".join(str(n) for n in numbers))
    print("Sorting algorithm used : Simple Sort\n")
    for i in range(len(numbers)-1):
        for j in range(i+1,len(numbers)):
            if numbers[i] > numbers[j]: # should be an increasing sequence
                # swap = numbers[i]
                # numbers[i] = numbers[j]
                # numbers[j] = swap
                numbers[i],numbers[j] = numbers[j],numbers[i]
            print("i =",i,'\t',"j =",j,'\t\t',", ".join(str(n) for n in numbers))
        print()
    print("Sorted!")

def bubbleSort(numbers):
    print("\nInitial sequence =",", ".join(str(n) for n in numbers))
    print("Sorting algorithm used : Bubble Sort\n")
    for i in range(len(numbers)):
        for j in range(len(numbers)-i-1):
            if numbers[j] > numbers[j+1]: # should be an increasing sequence
                numbers[j],numbers[j+1] = numbers[j+1],numbers[j]
            print("i =",j,'\t',"j =",j+1,'\t\t',", ".join(str(n) for n in numbers))
        print()
    print("Sorted!")
    
def selectionSort(numbers):
    # Places the minimum of the unsorted data straight to the first place
    print("\nInitial sequence =",", ".join(str(n) for n in numbers))
    print("Sorting algorithm used : Selection Sort\n")
    for i in range(len(numbers)-1):
        numb = numbers[i:]
        a = min(numb)
        numbers.remove(a)
        numbers.insert(i,a)
        print("i =",i+1,'\t',", ".join(str(n) for n in numbers))
    print("Sorted!")
    
def selectionSort2(numbers):
    # Swaps the minimum of the unsorted data with the i-th term
    print("\nInitial sequence =",", ".join(str(n) for n in numbers))
    print("Sorting algorithm used : Selection Sort 2\n")
    for i in range(len(numbers)): 
        min_idx = i 
        for j in range(i+1, len(numbers)): 
            if numbers[min_idx] > numbers[j]: 
                min_idx = j 
        numbers[i], numbers[min_idx] = numbers[min_idx], numbers[i]
        print("i =",i,'\t',", ".join(str(n) for n in numbers))
    print("Sorted!")

def insertionSort(numbers):
    print("\nInitial sequence =",", ".join(str(n) for n in numbers))
    print("Sorting algorithm used : Insertion Sort\n")
    for i in range(1,len(numbers)):
        for j in range(i):
            if numbers[i] < numbers[j]:
                a = numbers[i]
                numbers.remove(a)
                numbers.insert(j,a)
        print("i =",i,'\t',", ".join(str(n) for n in numbers))
    print("Sorted!")

def quickSort(numbers):
    print("\nInitial sequence =",", ".join(str(n) for n in numbers))
    print("Sorting algorithm used : Quick Sort\n")
    def partition(numbers,low,high): 
        i = ( low-1 )         # index of smaller element 
        pivot = numbers[high]     # pivot 
      
        for j in range(low , high): 
      
            # If current element is smaller than the pivot 
            if   numbers[j] < pivot: 
              
                # increment index of smaller element 
                i = i+1 
                numbers[i],numbers[j] = numbers[j],numbers[i] 
      
        numbers[i+1],numbers[high] = numbers[high],numbers[i+1]
        print("i =",i,'\t',"j =",j,'\t\t',", ".join(str(n) for n in numbers))
        return ( i+1 ) 
      
    # The main function that implements QuickSort 
    # numbers[] --> Array to be sorted, 
    # low  --> Starting index, 
    # high  --> Ending index 
      
    # Function to do Quick sort 
    def qS(numbers,low,high): 
        if low < high: 
      
            # pi is partitioning index, numbers[p] is now 
            # at right place 
            pi = partition(numbers,low,high) 
      
            # Separately sort elements before 
            # partition and after partition 
            qS(numbers, low, pi-1) 
            qS(numbers, pi+1, high) 
            
    qS(numbers,0,len(numbers)-1)
    print("Sorted!")

def pigeonholeSort(numbers):
    print("\nInitial sequence =",", ".join(str(n) for n in numbers))
    print("Sorting algorithm used : Pigeonhole Sort\n")
    diff = max(numbers)-min(numbers)+1
    pigeonhole = []
    for i in range(diff):
        pigeonhole.append([])
    for i in range(len(numbers)):
        j = numbers[i]-min(numbers)
        pigeonhole[j].append(numbers[i])
    print(pigeonhole)
    
    result = []
    
    for i in range(diff):
        for j in range(len(pigeonhole[i])):
            result.append(str(pigeonhole[i][j]))
    
    print("\nResult =",", ".join(result))
    print("Sorted!")

def cocktailSort(numbers):
    print("\nInitial sequence =",", ".join(str(n) for n in numbers))
    print("Sorting algorithm used : Cocktail Sort\n")
    for i in range(len(numbers)): # forward
        for j in range(len(numbers)-i-1):
            if numbers[j] > numbers[j+1]: # should be an increasing sequence
                numbers[j],numbers[j+1] = numbers[j+1],numbers[j]
            print("i =",j,'\t',"j =",j+1,'\t\t',", ".join(str(n) for n in numbers))
        print()
        for j in range(len(numbers)-i-3,-1,-1): # backward
            if numbers[j] > numbers[j+1]: # should be an increasing sequence
                numbers[j],numbers[j+1] = numbers[j+1],numbers[j]
            print("i =",j,'\t',"j =",j+1,'\t\t',", ".join(str(n) for n in numbers))
        print()
    print("Sorted!")

def gnomeSort(numbers):
    print("\nInitial sequence =",", ".join(str(n) for n in numbers))
    print("Sorting algorithm used : Gnome Sort\n")
    position = 0
    while position < len(numbers):
        if position == 0:
            position += 1
        elif numbers[position] >= numbers[position-1]:
            position += 1
        else:
            numbers[position],numbers[position-1]=numbers[position-1],numbers[position]
            position -= 1
        print("p =",position,'\t',", ".join(str(n) for n in numbers))
    print("Sorted!")
    
def bogoSort(a):
    import random
    
    print("\nInitial sequence =",", ".join(str(n) for n in a))
    print("Sorting algorithm used : Bogo Sort\n")
    
    database = []
    
    # Sorts array a[0..n-1] using Bogo sort 
    def bS(a): 
        attempt = 1
        
        while (is_sorted(a)== False):
            shuffle(a)
            print("Attempt",attempt,'\t',", ".join(str(n) for n in a))
            database.append(", ".join(str(n) for n in a))
            attempt += 1
      
    # To check if array is sorted or not 
    def is_sorted(a): 
        for i in range(0, len(a)-1): 
            if (a[i] > a[i+1] ): 
                return False
        return True
      
    # To generate permutation of the array
    def shuffle(a):
        for i in range (0,len(a)): 
            r = random.randint(0,len(a)-1) 
            a[i], a[r] = a[r], a[i]
        joined = ", ".join(str(n) for n in a)
        if joined in database:
            shuffle(a) # reshuffle in order not to revisit the same deck
    
    bS(a)
    
    # print(database)
    # print(len(database))
    # print(len(set(database)))
    print("Sorted!") # very dangerous for more than 8 terms!
