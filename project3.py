import random

# Function to get user inputs
def get_user_input():
   seed = int(input("Enter a seed value: "))
   num_processes = int(input("Enter number of processes (2, 10): "))
   last_arrival_time = int(input("Enter last possible arrival time (0, 99): "))
   max_burst_time = int(input("Enter max burst time (1, 100): "))
   quantum = int(input("Enter quantum size (1, 100): "))
   latency = int(input("Enter latency (0, 10): "))
   return seed, num_processes, last_arrival_time, max_burst_time, quantum, latency

# Function to generate processes
def generate_processes(num_processes, last_arrival_time, max_burst_time):
   processes = []
   for _ in range(num_processes):
      arrival = random.randint(0, last_arrival_time)
      burst = random.randint(1, max_burst_time)
      processes.append({'arrival': arrival, 'burst': burst, 'remaining': burst})
   processes.sort(key=lambda x: x['arrival'])  # Sort by arrival time
   return processes

# Placeholder for scheduling algorithms
def fcfs(processes, latency):
   time, total_waiting_time, total_turnaround_time = 0, 0, 0
   start_times, end_times = [], []
   for p in processes:
      if time < p['arrival']:
         time = p['arrival']  # CPU waits for the process to arrive
      start_times.append(time)
      time += p['burst'] + latency  # Add burst time and latency
      end_times.append(time)
      waiting_time = start_times[-1] - p['arrival']
      turnaround_time = waiting_time + p['burst']
      total_waiting_time += waiting_time
      total_turnaround_time += turnaround_time
   print_results(processes, start_times, end_times, total_waiting_time, total_turnaround_time)


def sjf(processes, latency):
   time, completed = 0, 0
   start_times, end_times = [0] * len(processes), [0] * len(processes)
   waiting_time, turnaround_time = [0] * len(processes), [0] * len(processes)
   while completed < len(processes):
      available_processes = [p for p in processes if p['arrival'] <= time and 'start_time' not in p]
      if available_processes:
         shortest_job = min(available_processes, key=lambda x: x['burst'])
         shortest_job_index = processes.index(shortest_job)
         start_times[shortest_job_index] = time
         time += shortest_job['burst'] + latency
         end_times[shortest_job_index] = time
         waiting_time[shortest_job_index] = start_times[shortest_job_index] - shortest_job['arrival']
         turnaround_time[shortest_job_index] = waiting_time[shortest_job_index] + shortest_job['burst']
         shortest_job['start_time'] = start_times[shortest_job_index]  # Mark as started
         completed += 1
      else:
         time += 1  # Idle time, increment time until a process arrives
   total_waiting_time = sum(waiting_time)
   total_turnaround_time = sum(turnaround_time)
   print_results(processes, start_times, end_times, total_waiting_time, total_turnaround_time)


def rr(processes, quantum, latency):
   time = 0
   queue = []
   start_times = [-1] * len(processes)  # -1 indicates not started
   end_times = [0] * len(processes)
   remaining_times = [p['burst'] for p in processes]  # Track remaining burst time for each process
   total_waiting_time = 0
   total_turnaround_time = 0

   while True:
        # Add newly arrived processes to the queue
      for i, p in enumerate(processes):
         if p['arrival'] <= time and i not in queue:
            queue.append(i)
            if start_times[i] == -1:  # Process starts for the first time
               start_times[i] = time
   
      if not queue:
            # If no process is ready, increment time
         time += 1
         continue
   
        # Process the first process in the queue
      current_process = queue.pop(0)
      if remaining_times[current_process] > quantum:
            # Process doesn't finish within the quantum
         remaining_times[current_process] -= quantum
         time += quantum + latency  # Include latency for context switch
            # Re-add the process to the end of the queue
         queue.append(current_process)
      else:
            # Process finishes
         time += remaining_times[current_process]
         remaining_times[current_process] = 0
         end_times[current_process] = time + latency  # Include latency after process finishes
         time += latency  # Move time forward for the next context switch
         waiting_time = (end_times[current_process] - latency) - processes[current_process]['arrival'] - processes[current_process]['burst']
         turnaround_time = (end_times[current_process] - latency) - processes[current_process]['arrival']
         total_waiting_time += waiting_time
         total_turnaround_time += turnaround_time
   
        # Check if all processes are completed
      if all(remaining == 0 for remaining in remaining_times):
         break

    # Calculate and print the results
   print_results(processes, start_times, end_times, total_waiting_time, total_turnaround_time)

def print_results(processes, start_times, end_times, total_waiting_time, total_turnaround_time):
   n = len(processes)
   print("Process\tStart Time\tEnd Time")
   for i, (start, end) in enumerate(zip(start_times, end_times), start=1):
      print(f"{i}\t{start}\t\t{end}")
   print(f"Average waiting time: {total_waiting_time / n:.2f}")
   print(f"Average turnaround time: {total_turnaround_time / n:.2f}\n")


import random

def random_pick(processes, latency):
   time = 0
   completed_processes = 0
   start_times = [-1] * len(processes)
   end_times = [0] * len(processes)
   remaining_times = [p['burst'] for p in processes]
   total_waiting_time = 0
   total_turnaround_time = 0

   while completed_processes < len(processes):
        # Determine which processes have arrived and are not completed
      available_processes = [i for i, p in enumerate(processes) if p['arrival'] <= time and remaining_times[i] > 0]
        
      if not available_processes:
         time += 1  # Increment time if no process is ready
         continue
   
        # Select a random process from available ones
      current_process = random.choice(available_processes)
        
      if start_times[current_process] == -1:  # Process starts for the first time
         start_times[current_process] = time
   
        # Execute the process
      if remaining_times[current_process] > 0:
         execute_time = remaining_times[current_process]
         remaining_times[current_process] = 0
         time += execute_time
         time += latency  # Include latency after execution
         end_times[current_process] = time
         completed_processes += 1
            
         waiting_time = end_times[current_process] - processes[current_process]['arrival'] - processes[current_process]['burst'] - latency
         turnaround_time = end_times[current_process] - processes[current_process]['arrival'] - latency
         total_waiting_time += waiting_time
         total_turnaround_time += turnaround_time

    # Calculate and print the results
   print_results(processes, start_times, end_times, total_waiting_time, total_turnaround_time)

def print_results(processes, start_times, end_times, total_waiting_time, total_turnaround_time):
   n = len(processes)
   print("Random Scheduling Results:")
   print("Process\tStart Time\tEnd Time")
   for i, (start, end) in enumerate(zip(start_times, end_times), start=1):
      print(f"{i}\t{start}\t\t{end}")
   print(f"Average waiting time: {total_waiting_time / n:.2f}")
   print(f"Average turnaround time: {total_turnaround_time / n:.2f}\n")



# Function to simulate and display results for each scheduling algorithm
def simulate_scheduling():
   seed, num_processes, last_arrival_time, max_burst_time, quantum, latency = get_user_input()
   random.seed(seed)
   processes = generate_processes(num_processes, last_arrival_time, max_burst_time)

   print("\nGenerated Processes:")
   for i, p in enumerate(processes, start=1):
      print(f"Process {i}: Arrival Time = {p['arrival']}, Burst Time = {p['burst']}")

    # Define a dictionary to map scheduler functions to their additional arguments
   scheduler_args = {
        fcfs: (latency,),
        sjf: (latency,),
        rr: (quantum, latency),
        random_pick: (latency,)
      }

    # Iterate through the scheduler functions and call each with the correct arguments
   for scheduler, args in scheduler_args.items():
      scheduler_name = scheduler.__name__.upper()
      print(f"\n{scheduler_name} Scheduling:")
      scheduler(processes.copy(), *args)  # Use argument unpacking to pass the correct args

if __name__ == "__main__":
   simulate_scheduling()


def print_results(processes, start_times, end_times, total_waiting_time, total_turnaround_time):
   n = len(processes)
   print("Process\tStart Time\tEnd Time")
   for i, (start, end) in enumerate(zip(start_times, end_times)):
      print(f"{i + 1}\t{start}\t\t{end}")
   print(f"Average waiting time: {total_waiting_time / n:.2f}")
   print(f"Average turnaround time: {total_turnaround_time / n:.2f}\n")

