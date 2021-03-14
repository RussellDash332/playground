def leading_entry_col(A,r):
    row = A[r]
    i = 0
    while i < len(A[0]) and row[i] == 0:
        i += 1
    return i

def list_pivots(A):
    res = []
    for r in range(len(A)):
        k = leading_entry_col(A,r)
        if k not in res and k != len(A[0]):
            res.append((r,k))
    res.sort(reverse=True)
    return res

def col(A,i): # length = len(A)
    return list(map(lambda x:x[i],A))

def row(A,i): # length = len(A[0])
    return A[i]

def ero1(A,i,c): # cRi, modifies A
    for j in range(len(A[0])):
        A[i][j] *= c

def ero2(A,i,j,c): # Ri + cRj, modifies A
    for k in range(len(A[0])):
        A[i][k] += c*A[j][k]

def ero3(A,i,j): # swap Ri and Rj, modifies A
    for k in range(len(A[0])):
        A[i][k],A[j][k] = A[j][k],A[i][k]

def rref(A): # modifies A and returns the RREF of A
    # start from A_{1,1}
    curr_col = 0
    curr_row = 0

    while curr_col < len(A[0]) and curr_row < len(A): # while the pointer points to an entry in the matrix
        if A[curr_row][curr_col] == 0: # current entry is 0, we want to make it 1 by swapping with a non-zero entry
            check_col = col(A,curr_col)[curr_row+1:] # check all the entries below it
            i = 0
            for i in range(len(check_col)):
                if check_col[i] != 0:
                    break
                elif i == len(check_col)-1:
                    i += 1
            if i < len(check_col): # if found a non-zero entry in that column, swap with that row
                ero3(A,curr_row,curr_row+i+1)
            else: # otherwise, all the entries in that column is zero, move on to the next column
                """
                The case would be something like this
                0  1 2
                0 -1 3
                0  2 4
                """
                curr_col += 1
        else: # the entry is nonzero, do ero1 and/or ero2
            # make the current entry 1 first by doing ero1
            if A[curr_row][curr_col] != 1:
                ero1(A,curr_row,1/A[curr_row][curr_col])
            """
            Now we know the augmented matrix is something like this
            1  0 3 -> the leading entry must be 1
            4 -1 9
            3  7 5
            """
            # for all rows below it, do ero2
            for i in range(curr_row+1,len(A)):
                if A[i][curr_col] != 0:
                    ero2(A,i,curr_row,-A[i][curr_col]/A[curr_row][curr_col])
            """
            Using the previous example, we want the augmented matrix to be something like this
            1  0 3 -> the leading entry must be 1
            0 -1 -3
            0  7 -4
            """
            curr_col += 1
            curr_row += 1

    # Now that it's all REF, let's bring it to RREF!
    pivots = list_pivots(A) # work from bottom to top
    for i in range(len(pivots)-1):
        for j in range(pivots[i][0]-1,-1,-1): # j < pr[i][0]
            ero2(A,j,pivots[i][0],-A[j][pivots[i][1]])

    return A

def pp(A): # pretty print
    for r in A:
        """
        for c in r:
            print(c, end = " ")
        print()
        """
        print(r)
    print()

mat = [[1,2,3],[4,5,6],[7,8,31/7]]
pp(mat)
pp(rref(mat))