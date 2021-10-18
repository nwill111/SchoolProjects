# Nathan Williams ITCS 3153-051

# Imports
import numpy as np

# Variables
restarts = 0
state_changes = 0
board = []
neighbor_states = []


# Calculates number of 1s on the same row as the queen's row.
# This is not needed for columns as the queens are placed 1 per column  on board creation.
def calc_horizontal(q_row, q_col, curr_board):
    conflicts = 0
    for col in range(8):
        if col != q_col:
            if curr_board[q_row][col] == 1:
                conflicts += 1

    return conflicts


# Checks the diagonal facing \ by going down and right by 1 until the boarder of the board is hit.
# It then goes up and over left by one counting queens that are not the original
def calc_diagonal_left(q_row, q_col, curr_board):
    conflicts = 0
    # leaning \
    x = q_row
    y = q_col
    while x != 7 and y != 7:
        x += 1
        y += 1
    while x >= 0 and y >= 0:
        if x != q_row and y != q_col:
            if curr_board[x][y] == 1:
                conflicts += 1
        x -= 1
        y -= 1

    return conflicts


# Checks the diagonal facing / by going down and left by 1 until the boarder of the board is hit.
# Then, goes up and right by one counting queens that are not the original, repeating until the other boarder is hit.
def calc_diagonal_right(q_row, q_col, curr_board):
    conflicts = 0
    # leaning /
    x = q_row
    y = q_col
    while x != 7 and y != 0:
        x += 1
        y -= 1
    while x >= 0 and y <= 7:
        if x != q_row and y != q_col:
            if curr_board[x][y] == 1:
                conflicts += 1
        x -= 1
        y += 1

    return conflicts


# Loops through the board looking for queens and using previous counting functions.
def calc_heuristic(curr_board):
    conflicts = 0
    for row in range(8):
        for col in range(8):
            if curr_board[row][col] == 1:
                conflicts += calc_horizontal(row, col, curr_board) + calc_diagonal_left(row, col, curr_board) + \
                             calc_diagonal_right(row, col, curr_board)

    # Final count is divided by two to avoid double counting of conflicts.
    return conflicts / 2


# Moves queen to a new spot.
# Takes in row and col of the queen as well as new row value to move the queen to.
def move_queen(row, col, new_row):
    if board[row][col] == 1:
        board[row][col] = 0
        board[new_row][col] = 1


# Creates a new board
def create_board():
    # Create Empty 8x8 Grid
    global board
    board = np.empty((8, 8), int)

    # Fill 8x8 with Zeros
    for x in range(8):
        for y in range(8):
            board[x, y] = 0

    # Randomly place a queen in each column
    for y in range(8):
        random = np.random.randint(0, 8)
        board[random, y] = 1


# Prints info on the current board.
def print_board():
    print("current h: " + str(calc_heuristic(board)))
    print("Current State")
    print(board)
    print('Neighbors found with lower h: ' + str(calc_better_states()))


# Generates an array of all possible neighbor states.
def generate_neighbor_states():
    global neighbor_states
    neighbor_states = []
    for row in range(8):
        for col in range(8):
            if board[row][col] == 1:
                for newRow in range(8):
                    move_queen(row, col, newRow)
                    neighbor_states.append(np.array(board))
                    move_queen(newRow, col, row)


# Goes through neighbor_states array the to find the one with the lowest heuristic value.
def find_best_neighbor():
    smallest = 9999
    for boards in neighbor_states:
        curr_board_heuristic = calc_heuristic(boards)
        if curr_board_heuristic < smallest:
            smallest = curr_board_heuristic
            smallest_board = boards

    return smallest_board


# Goes through the neighbor_states array and counts neighbor states with lower heuristic values.
def calc_better_states():
    better_neighbor_count = 0
    for boards in neighbor_states:
        curr_board_heuristic = calc_heuristic(board)
        new_board_heuristic = calc_heuristic(boards)
        if new_board_heuristic < curr_board_heuristic:
            better_neighbor_count += 1

    return better_neighbor_count


# Sets the current board to the best neighbor state that was found with find_best_neighbor()
def set_new_state():
    global board
    global state_changes
    board = find_best_neighbor()
    state_changes += 1


# Uses the calc_better_states() function to determine if the current state is the minima.
def is_minima():
    global board
    if calc_better_states() == 0:
        return True
    else:
        return False


# Main
create_board()
while True:
    needs_restart = False
    generate_neighbor_states()
    if is_minima():
        if calc_heuristic(board) == 0:
            print_board()
            print('SOLUTION FOUND!')
            print('State changes: ' + str(state_changes))
            print('Restarts:' + str(restarts))

            break
        else:
            print_board()
            create_board()
            print('RESTART' + '\n')
            restarts += 1
            needs_restart = True

    if not needs_restart:
        print_board()
        print("Setting new current state" + '\n')
        set_new_state()
