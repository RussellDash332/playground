def linearSearch(n,toFind):
    from random import randint
    numberslist = [0]*n
    for i in range(n):
        numberslist[i] = randint(0,n)
        
    found = False
    
    for i in range(n):
        if numberslist[i] == toFind:
            print(toFind, "found at index", i)
            found = True
    
    if (not found):
        print("Not found")

def binarySearch(n,toFind):
    numberslist = [0]*n
    for i in range(n):
        numberslist[i] = i # MUST BE A SORTED DATA
        
    f,l = 0,n-1
    found = False
    
    while f<=l and not found:
        m = (f+l)//2
        if toFind > numberslist[m]:
            f=m+1
        elif toFind < numberslist[m]:
            l=m-1
        elif toFind == numberslist[m]:
            found = True
    
    if found:
        print(toFind, "found at index", m)
    else:
        print("Not found")
