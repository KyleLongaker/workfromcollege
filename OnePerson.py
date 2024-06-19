import time
import threading
from threading import Thread, Lock, Condition
from random import Random

class OnePerson:
    # Static counter to track the order of departures from the restroom
    departure_counter = 1  

    def __init__(self, id, gender, usage_time):
        self.id = id  
        # Unique identifier for each person
        self.gender = gender  
        # Gender of the person, 0 for male, 1 for female
        self.usage_time = usage_time  
        # Time person will spend in the restroom, in seconds

    def use_restroom(self):
        # Log when a person enters the restroom with formatted timestamp
        print(f"@t={time.strftime('%H:%M:%S')}; OnePerson {self.id} ({'M' if self.gender == 0 else 'F'}) enters the facilities for {self.usage_time} seconds.")
        # Simulate the duration spent in the restroom by sleeping
        time.sleep(self.usage_time)

class Restroom:
    def __init__(self):
        self.queue = []  
        # List to manage people waiting for the restroom
        self.lock = Lock()  
        # Mutex lock to synchronize access to the restroom
        self.condition = Condition(self.lock)  
        # Condition variable for coordinating entry and exit
        self.current_gender = None  
        # Tracks the gender currently using the restroom
        self.count = 0  
        # Tracks the number of people currently in the restroom

    def enter(self, person):
        with self.condition:
            # Wait until the restroom is available for this person (correct gender and not full)
            while (self.current_gender is not None and self.current_gender != person.gender) or self.count >= 2:
                self.condition.wait()
            # Update restroom status to reflect this person's entry
            self.current_gender = person.gender
            self.count += 1
            self.queue.append(person)
        
        # Simulate the usage of restroom facilities
        person.use_restroom()

    def leave(self, person):
        with self.condition:
            # Update restroom status to reflect this person's departure
            self.count -= 1
            self.queue.remove(person)
            if self.count == 0:
                self.current_gender = None  # Reset gender tracking if restroom is empty
            # Manage the departure index for orderly exit reporting
            departure_index = OnePerson.departure_counter
            OnePerson.departure_counter += 1
            # Log when a person exits the restroom with formatted timestamp
            print(f"@t={time.strftime('%H:%M:%S')}; OnePerson {person.id} ({'M' if person.gender == 0 else 'F'}) exits (departure #{departure_index})")
            self.condition.notify_all()  
            # Notify all waiting threads that a change has occurred

def process_schedule(restroom, persons):
    threads = []
    for person in persons:
        # Create a thread for each person to enter and leave the restroom
        thread = Thread(target=lambda p=person: enter_and_leave(restroom, p))
        threads.append(thread)
        thread.start()
        # Stagger thread starts slightly to simulate more realistic arrivals
        time.sleep(0.1)  
    for thread in threads:
        thread.join()  
        # Wait for all threads to complete, ensuring all have finished their restroom visit

def enter_and_leave(restroom, person):
    restroom.enter(person)  
    # Person enters and uses the restroom
    restroom.leave(person)  
    # Person leaves the restroom

def create_schedule(num_people, gender_probability, rng):
    # Generate a list of OnePerson objects with random genders and restroom times
    return [OnePerson(id=i+1, gender=rng.random() < gender_probability, usage_time=rng.randint(3, 8)) for i in range(num_people)]

def main():
    random_seed = 61  
    # Seed for random number generator to ensure reproducible randomness
    rng = Random(random_seed)  
    # Random number generator instance
    restroom = Restroom()  
    # Create a restroom object
    schedules = [
        create_schedule(5, 0.6, rng),  
        # Generate first schedule of people
        create_schedule(10, 0.6, rng),  
        # Generate second schedule
        create_schedule(5, 0.6, rng)  
        # Generate third schedule
    ]
    
    for schedule in schedules:
        process_schedule(restroom, schedule)  
        # Process each schedule in sequence
        time.sleep(10)  
        # Simulate a delay of 10 seconds between schedules

if __name__ == '__main__':
    main()
