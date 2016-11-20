package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by Sajeewa on 11/20/2016.
 */
public class PersistentAccountDAO implements AccountDAO {
//class which has the persistent memory of account details

    private SQLiteDatabase myDB;

    //Let the constructor store the database to avoid re-opening of the database everytime
    public PersistentAccountDAO(SQLiteDatabase mydatabase) {
        this.myDB = mydatabase;
    }

    @Override
    public List<String> getAccountNumbersList() {

        //store the results that are taken by executing the query as a iterative result set
        Cursor resultSet = myDB.rawQuery("SELECT accountNo FROM Account",null);


        //initializing a list to keep the account numbers
        List<String> accounts = new ArrayList<String>();

        //loop until the result set reaches its end
        if(resultSet.moveToFirst()){
            do {
                //add the current account numbers to the previously initialized list
                accounts.add(resultSet.getString(resultSet.getColumnIndex("accountNo")));
            } while(resultSet.moveToNext());
        }
        //returns the list
        return accounts;
    }

    @Override
    public List<Account> getAccountsList() {

        //store the results that are taken by executing the query as a iterative result set
        Cursor resultSet = myDB.rawQuery("SELECT * FROM Account",null);

        //initializing a list to keep the account numbers
        List<Account> accounts = new ArrayList<Account>();

        //loop until the result set reaches its end
        if (resultSet.moveToFirst()){
            do {
                //creates an Account using the taken details by executing the query
                Account account = new Account(resultSet.getString(resultSet.getColumnIndex("accountNo")),
                        resultSet.getString(resultSet.getColumnIndex("bankName")),
                        resultSet.getString(resultSet.getColumnIndex("accountHolderName")),
                        resultSet.getDouble(resultSet.getColumnIndex("initialBalance")));

                //add the created Account object to the previously initialized list
                accounts.add(account);
            } while(resultSet.moveToNext());
        }

        //returns the list
        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        //store the results that are taken by executing the query as a iterative result set
        Cursor resultSet = myDB.rawQuery("SELECT * FROM Account WHERE accountNo = " + accountNo, null);

        //initialize an Account object
        Account account = null;

        //loop until the result set reaches its end
        if(resultSet.moveToFirst()){
            do {
                //creates the Account object using the taken data by executing the query
                account = new Account(resultSet.getString(resultSet.getColumnIndex("accountNo")),
                        resultSet.getString(resultSet.getColumnIndex("bankName")),
                        resultSet.getString(resultSet.getColumnIndex("accountHolderName")),
                        resultSet.getDouble(resultSet.getColumnIndex("initialBalance")));
            } while (resultSet.moveToNext());
        }

        //returns the Account object
        return account;
    }

    @Override
    public void addAccount(Account account) {

        //query to insert values into Account table
        String insertQuery = "INSERT INTO Account (accountNo, bankName, accountHolderName, initialBalance) VALUES (?,?,?,?)";

        //compiles the query
        SQLiteStatement sqLiteStatement = myDB.compileStatement(insertQuery);

        //bind necessary values to execute the query
        sqLiteStatement.bindString(1, account.getAccountNo());
        sqLiteStatement.bindString(2, account.getBankName());
        sqLiteStatement.bindString(3, account.getAccountHolderName());
        sqLiteStatement.bindDouble(4, account.getBalance());

        //executes the query
        sqLiteStatement.executeInsert();
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {

        //query to delete some values into Account table
        String deleteQuery = "DELETE FROM Account WHERE accountNo = ?";

        //compiles the query
        SQLiteStatement sqLiteStatement = myDB.compileStatement(deleteQuery);

        //bind necessary values to execute the query
        sqLiteStatement.bindString(1, accountNo);

        //executes the query
        sqLiteStatement.executeUpdateDelete();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

        //query to update some values into Account table
        String updateQuery = "UPDATE Account SET initialBalance = initialBalance + ? WHERE accountNo = ?";

        //compiles the query
        SQLiteStatement sqLiteStatement = myDB.compileStatement(updateQuery);

        //bind necessary values to execute the query

        sqLiteStatement.bindString(2, accountNo);

        if (expenseType == ExpenseType.EXPENSE){
            sqLiteStatement.bindDouble(1, -amount);
        } else {
            sqLiteStatement.bindDouble(1, amount);
        }

        //executes the query
        sqLiteStatement.executeUpdateDelete();
    }
}
