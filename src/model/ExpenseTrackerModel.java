package model;

import java.util.ArrayList;
import java.util.List;
import org.tinylog.Logger;

/**
 * Represents the data model as a list of transactions.
 * 
 * NOTE) Represents the Model in the MVC architecture pattern.
 */
public class ExpenseTrackerModel {

	private List<Transaction> transactions = new ArrayList<>();
	
	public ExpenseTrackerModel() {
		super();
		Logger.info("ExpenseTrackerModel initialized with an empty transaction list");
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void addTransaction(Transaction t) {
		Logger.info("Adding transaction to model");
		transactions.add(t);
		Logger.info("Transaction added to model");
	}

	public boolean removeTransaction(int transactionID) {
  	  // Perform input validation
  	  if ((transactionID < 0) || (transactionID > this.getTransactions().size() - 1)) {
  		  return false;
  	  }
  	  else {
		Logger.info("Removing transaction from model");
  		transactions.remove(transactionID);
		Logger.info("Transaction removed from model");
  		return true;
  	  }
	}

	public double computeTransactionsTotalCost() {
		Logger.info("Computing total cost for transactions");
		double totalCost=0;
		for(Transaction t : transactions) {
			totalCost+=t.getAmount();
		}
		Logger.info("Computed transaction total cost");
		return totalCost;
	}
}
