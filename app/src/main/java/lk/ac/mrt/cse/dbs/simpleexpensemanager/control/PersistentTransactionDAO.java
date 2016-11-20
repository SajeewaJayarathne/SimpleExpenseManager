package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by Sajeewa on 11/20/2016.
 */
public class PersistentTransactionDAO implements TransactionDAO {
//class which has the persistent memory of transactions details

    private SQLiteDatabase myDB;

    //Let the constructor store the database to avoid re-opening of the database everytime
    public PersistentTransactionDAO(SQLiteDatabase mydatabase) {
        this.myDB = mydatabase;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {

        //query to insert values into Transactions table
        String insertQuery = "INSERT INTO Transactions (accountNo, date, expenseType, amount) VALUES (?, ?, ?, ?)";

        //compiles the query
        SQLiteStatement sqLiteStatement = myDB.compileStatement(insertQuery);

        //bind necessary values to execute the query
        sqLiteStatement.bindString(1, accountNo);
        sqLiteStatement.bindLong(2, date.getTime());
        sqLiteStatement.bindLong(3, (expenseType == ExpenseType.EXPENSE) ? 0 : 1);
        sqLiteStatement.bindDouble(4, amount);

        //executes the query
        sqLiteStatement.executeInsert();
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {

        //store the results that are taken by executing the query as a iterative result set
        Cursor resultSet = myDB.rawQuery("SELECT * FROM Transactions", null);

        //initializing a list to keep the transaction details
        List<Transaction> transactions = new ArrayList<Transaction>();

        //loop until the result set reaches its end
        if (resultSet.moveToFirst()) {
            do {
                //creates a Transaction object using the taken details by executing the query
                Transaction transaction = new Transaction(new Date(resultSet.getLong(resultSet.getColumnIndex("date"))),
                        resultSet.getString(resultSet.getColumnIndex("accountNo")),
                        resultSet.getInt(resultSet.getColumnIndex("expenseType")) == 0 ? ExpenseType.EXPENSE : ExpenseType.INCOME,
                        resultSet.getDouble(resultSet.getColumnIndex("amount")));

                //add the created object to the list
                transactions.add(transaction);

            }while (resultSet.moveToNext());
        }

        //returns the list
        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {

        //store the results that are taken by executing the query as a iterative result set
        Cursor resultSet = myDB.rawQuery("SELECT * FROM Transactions LIMIT " + limit,null);

        //initializing a list to keep the transaction details
        List<Transaction> transactions = new ArrayList<Transaction>();

        //loop until the result set reaches its end
        if (resultSet.moveToFirst()) {
            do {
                //creates a Transaction object using the taken details by executing the query
                Transaction transaction = new Transaction(new Date(resultSet.getLong(resultSet.getColumnIndex("date"))),
                        resultSet.getString(resultSet.getColumnIndex("accountNo")),
                        resultSet.getInt(resultSet.getColumnIndex("expenseType")) == 0 ? ExpenseType.EXPENSE : ExpenseType.INCOME,
                        resultSet.getDouble(resultSet.getColumnIndex("amount")));

                //add the created object to the list
                transactions.add(transaction);

            } while (resultSet.moveToNext());
        }

        //returns the list
        return transactions;
    }
}
