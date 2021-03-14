# https://www.youtube.com/watch?v=G_UYXzGuqvM

# Assume the grid is 9x9 in size
grid = [[4,0,0,0,3,0,0,0,8],
        [0,0,0,6,0,7,0,0,0],
        [5,0,2,0,0,0,3,0,4],
        [0,1,0,0,2,0,0,3,0],
        [2,0,0,9,0,8,0,0,5],
        [0,8,0,0,5,0,0,4,0],
        [6,0,9,0,0,0,2,0,7],
        [0,0,0,5,0,9,0,0,0],
        [3,0,0,0,1,0,0,0,0]]

def print_grid(grid):
    print(" "+"-"*25)
    for y in range(9):
        print(" | ", end="")
        for x in range(9):
            print(grid[y][x], end=" " if (x+1) % 3 else " | ")
        if (y+1) % 3:
            print()
        else:
            print("\n "+"-"*25)
    print()

def possible(y, x, n, grid):
    for i in range(0,9): # check columns
        if grid[y][i] == n:
            return False

    for i in range(0,9): # check rows
        if grid[i][x] == n:
            return False

    x0 = (x//3)*3
    y0 = (y//3)*3

    # check for 3x3 region
    for i in range(0,3):
        for j in range(0,3):
            if grid[y0+i][x0+j] == n:
                return False

    return True

def solve(grid, show_init = False):
    if show_init:
        print("Before:")
        print_grid(grid)
        print("After:")
        
    for y in range(9):
        for x in range(9):
            if grid[y][x] == 0: # check whether there is an empty space
                for n in range(1,10):
                    if possible(y, x, n, grid):
                        grid[y][x] = n
                        solve(grid)
                        grid[y][x] = 0 # backtrack if it fails
                return

    print_grid(grid) # print solved grid

solve(grid, True)
