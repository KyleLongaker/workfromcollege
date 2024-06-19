using System;
using System.Diagnostics;
using System.Threading;

namespace ConsoleApp2
{
    class Program
    {
        static void Main(string[] args)
        {
            // Measure the elapsed time for creating and destroying 100,000 threads
            Stopwatch stopwatch = new Stopwatch();
            stopwatch.Start();

            CreateAndDestroyThreads(100000);

            stopwatch.Stop();
            Console.WriteLine($"Elapsed time for creating and destroying 100,000 threads: {stopwatch.ElapsedMilliseconds} milliseconds");

            stopwatch.Restart();

            CreateAndDestroyParentAndChildThreads(100, 1000);

            stopwatch.Stop();
            Console.WriteLine($"Elapsed time for creating and destroying parent and child threads: {stopwatch.ElapsedMilliseconds} milliseconds");

            stopwatch.Restart();

            CreateAndDestroyGrandparentParentChildThreads(10, 100, 100);

            stopwatch.Stop();
            Console.WriteLine($"Elapsed time for creating and destroying grandparent, parent, and child threads: {stopwatch.ElapsedMilliseconds} milliseconds");

            Console.WriteLine("System Information:");
            Console.WriteLine($"Processor Count: {Environment.ProcessorCount}");
            Console.WriteLine($"OS Version: {Environment.OSVersion}");

            RunThreadPoolExample();

            Console.WriteLine("Main thread is terminating");
            Console.ReadKey();
        }

        static void CreateAndDestroyThreads(int numThreads)
        {
            for (int i = 0; i < numThreads; i++)
            {
                int threadNum = i; // Local copy to capture the loop variable correctly
                Thread thread = new Thread(() =>
                {
                    Console.WriteLine($"Thread {threadNum} is running");
                });

                thread.Start();
                thread.Join(); // Wait for the thread to complete
            }
        }


        static void CreateAndDestroyParentAndChildThreads(int numParentThreads, int numChildThreads)
        {
            for (int i = 0; i < numParentThreads; i++)
            {
                ThreadPool.QueueUserWorkItem(_ =>
                {
                    Console.WriteLine($"Parent Thread {Thread.CurrentThread.ManagedThreadId} started.");

                    for (int j = 0; j < numChildThreads; j++)
                    {
                        ThreadPool.QueueUserWorkItem(__ =>
                        {
                            Thread.Sleep(10);
                        });
                    }

                    Console.WriteLine($"Parent Thread {Thread.CurrentThread.ManagedThreadId} completed.");
                });
            }

            Thread.Sleep(2000); // Adjust based on your scenario
        }

        static void CreateAndDestroyGrandparentParentChildThreads(int numGrandparentThreads, int numParentThreads, int numChildThreads)
        {
            for (int i = 0; i < numGrandparentThreads; i++)
            {
                ThreadPool.QueueUserWorkItem(_ =>
                {
                    Console.WriteLine($"Grandparent Thread {Thread.CurrentThread.ManagedThreadId} started.");

                    CreateAndDestroyParentAndChildThreads(numParentThreads, numChildThreads);

                    Console.WriteLine($"Grandparent Thread {Thread.CurrentThread.ManagedThreadId} completed.");
                });
            }

            Thread.Sleep(2000); // Adjust based on your scenario
        }

        static void RunThreadPoolExample()
        {
            const int MAX_T = 3;

            CustomTask[] tasks = new CustomTask[5];
            for (int i = 0; i < tasks.Length; i++)
            {
                tasks[i] = new CustomTask($"task {i + 1}");
            }

            ThreadPool.SetMaxThreads(MAX_T, MAX_T);

            foreach (var task in tasks)
            {
                ThreadPool.QueueUserWorkItem(_ => task.Run());
            }

            Thread.Sleep(7000); // Wait for tasks to complete
        }
    }

    class CustomTask
    {
        private string name;

        public CustomTask(string name)
        {
            this.name = name;
        }

        public void Run()
        {
            for (int i = 0; i <= 5; i++)
            {
                DateTime d = DateTime.Now;
                string ft = d.ToString("hh:mm:ss");
                if (i == 0)
                {
                    Console.WriteLine($"Initialization Time for task name - {name} = {ft}");
                }
                else
                {
                    Console.WriteLine($"Executing Time for task name - {name} = {ft}");
                }
                Thread.Sleep(1000);
            }
            Console.WriteLine($"{name} complete");
        }
    }
}
