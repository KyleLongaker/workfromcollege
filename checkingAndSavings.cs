using System;

namespace ConsoleApp1
{
    public class Program
    {
        public static void Main(string[] args)
        {
            String Firstname = "John";
            String surname = "Smith";
            double checkingmoney = 1000000;
            double savingsmoney = 100000;
            CheckingAccount ob = new CheckingAccount(Firstname, surname);
            ob.PrintBalance(checkingmoney);
            SavingsAccount oj = new SavingsAccount(Firstname, surname);
            oj.PrintBalance(savingsmoney);
            Console.WriteLine("$"+ob+" is in your checking account");
            Console.WriteLine("$"+oj+" is in your savings");
        }
    }
}
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ConsoleApp1
{
    public class SavingsAccount : Account, ITransaction
    {
        public SavingsAccount(string firstName, string lastName) : base(firstName, lastName, "Savings")
        {

        }
        public void showTransaction()
        {
        }
        public double getAmount()
        {
            return 0;
        }
    }
}
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ConsoleApp1
{
    public class Account
    {
        public Account(string firstName, string lastName, string type)
        {
            this.FirstName = firstName;
            this.LastName = lastName;
            this.Type = type;
        }
        public string FirstName { get; set; }
        public string LastName { get; set; }
        public string Type { get; set; }
        public void PrintBalance(double money) {
            Console.WriteLine("Your amount is: $" + money);
        }
    }
}
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ConsoleApp1
{
    public class CheckingAccount : Account, ITransaction
    {
        public CheckingAccount(string firstName, string lastName) : base(firstName, lastName, "Checking")
        {

        }
        public void showTransaction(double purchase, double money)
        {
            money = money - purchase;
            Console.WriteLine("You bought this item for $"+purchase);
        }
        public double getAmount(double amount)
        {
            Console.WriteLine("You have ");
            return amount;
        }
        public decimal interestRate { get; set; }
        public void applyInterestRate(double money)
        {
            money = money * 1.2;
        }


        void ITransaction.showTransaction()
        {
            throw new NotImplementedException();
        }

        double ITransaction.getAmount()
        {
            throw new NotImplementedException();
        }
    }
}
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ConsoleApp1
{
    public interface ITransaction
    {
        public void showTransaction();
        public double getAmount();
    }
}
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ConsoleApp1
{
    public interface IInterest
    {
        decimal interestRate { get; set; }
        void applyInterestRate();
    }
}
