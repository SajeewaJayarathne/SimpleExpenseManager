package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;

/**
 * Created by Sajeewa on 11/20/2016.
 */

public class PersistentExpenseManager extends ExpenseManager {

    private Context context;

    public PersistentExpenseManager(Context context){
        //directs the compiler towards the setup() function
        this.context = context;
        setup();
    }


    @Override
    public void setup(){
        //create the database(if it already exists then open that database)
        //DB name - 140710N
        SQLiteDatabase mydatabase = context.openOrCreateDatabase("140710N", context.MODE_PRIVATE, null);

        //creates the table to keep the data of accounts if it doesn't exist
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Account(accountNo VARCHAR PRIMARY KEY, bankName VARCHAR, accountHolderName VARCHAR, initialBalance REAL);");

        //creates the table to keep the data of transactions if it doesn't exist
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Transactions(transactionID INTEGER PRIMARY KEY, accountNo VARCHAR, date DATE, expenseType INT2, amount REAL, FOREIGN KEY (accountNo) REFERENCES Account(accountNo));");

        //Holds our DAO instances in memory till the program exists
        PersistentAccountDAO accountDAO = new PersistentAccountDAO(mydatabase);
        setAccountsDAO(accountDAO);

        setTransactionsDAO(new PersistentTransactionDAO(mydatabase));


    }
}
