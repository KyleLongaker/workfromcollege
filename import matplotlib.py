import numpy as np
import random
from queue import Queue

def create_maze(dim):
    maze = np.ones((dim*2+1, dim*2+1), dtype=int)
    x, y = (0, 0)
    maze[2*x+1, 2*y+1] = 0
    stack = [(x, y)]
    while stack:
        x, y = stack.pop()
        directions = [(0, 1), (1, 0), (0, -1), (-1, 0)]
        random.shuffle(directions)
        for dx, dy in directions:
            nx, ny = x + dx, y + dy
            if 0 <= nx < dim and 0 <= ny < dim and maze[2*nx+1, 2*ny+1] == 1:
                maze[2*nx+1, 2*ny+1] = 0
                maze[x+dx+x+1, y+dy+y+1] = 0
                stack.append((nx, ny))
    maze[1, 0] = 0  # Entrance
    maze[-2, -1] = 0  # Exit
    return maze

def find_path_bfs(maze):
    directions = [(0, 1), (1, 0), (0, -1), (-1, 0)]
    start, end = (1, 0), (maze.shape[0]-2, maze.shape[1]-1)
    visited = np.zeros_like(maze, dtype=bool)
    queue = Queue()
    queue.put((start, [start]))
    while not queue.empty():
        (node, path) = queue.get()
        if node == end:
            return path
        for dx, dy in directions:
            next_node = (node[0]+dx, node[1]+dy)
            if 0 <= next_node[0] < maze.shape[0] and 0 <= next_node[1] < maze.shape[1] and maze[next_node] == 0 and not visited[next_node]:
                visited[next_node] = True
                queue.put((next_node, path + [next_node]))

def print_maze(maze, path=None):
    path_set = set(path) if path else set()
    for i in range(maze.shape[0]):
        for j in range(maze.shape[1]):
            if (i, j) in path_set:
                print("·", end="")  # Path
            elif maze[i, j] == 1:
                print("█", end="")  # Wall
            else:
                print(" ", end="")  # Open space
        print()  #

dim = 10  
maze = create_maze(dim)
path = find_path_bfs(maze)
print_maze(maze, path)
