using System;

namespace ConsoleApp1
{
    public class Program
    {
        public static void Main(string[] args)
        {
            String game1 = "soccer";
            String game2 = "basketball";
            int x0 = 0;
            int x1 = 1;
            int x2 = 2;
            int x3 = 3;
            int x4 = 4;
            int x5 = 5;
            int x6 = 6;
            int x7 = 7;
            int x8 = 8;
            int x9 = 9;
            int x10 = 10;
            int x11 = 11;
            int x12 = 12;
            int x13 = 13;
            int x14 = 14;
            int x15 = 15;
            int x16 = 16;
            int y0 = 0;
            int y1 = 1;
            int y2 = 2;
            int y3 = 3;
            int y4 = 4;
            int y5 = 5;
            int y6 = 6;
            int y7 = 7;
            int y8 = 8;
            int y9 = 9;
            int y10 = 10;
            int y11 = 11;
            int y12 = 12;
            int y13 = 13;
            int y14 = 14;
            int y15 = 15;
            int y16 = 16;
            int[] post1 = {x0, y7, x3, y7, x3, y4, x0, y4 };
            int[] post2 = {x14, y7, x16, y14, x14, y4, x16, y4 };
            Goal ob = new Goal(x1, x2);
            ob.goalpost1(post1);
            ob.goalpost2(post2);
            Player oj = new Player(game1);
            oj.soccer(x5, y6);
            oj.soccer(x4, y9);
            oj.soccer(x6, y8);
            oj.soccer(x4, y2);
            oj.soccer(x6, y4);
            oj.soccer(x8, y5);

            Console.WriteLine("goal post is " +ob);
            Console.WriteLine("here are the players "+oj);
            //Console.WriteLine("$" + oj + " is in your savings");
        }
    }
}
