package controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import model.CSVExporter;
import model.CSVImporter;
import model.ExpenseTrackerModel;
import model.InputValidation;
import model.Transaction;
import view.ExpenseTrackerView;
import org.tinylog.Logger;

/**
 * Provides the application programming layer to support the
 * following interface: addTransaction, delete, import, export.
 * 
 * NOTE) Represents the Controller in the MVC architecture pattern.
 */
public class ExpenseTrackerController {
	private ExpenseTrackerModel model = new ExpenseTrackerModel();    
    private ExpenseTrackerView view = new ExpenseTrackerView(model);
    
    public ExpenseTrackerController() {
    	super();
    	
    	// Hook up the view and controller
    	
        // Handle add transaction button clicks
        view.getDataPanelView().getAddTransactionBtn().addActionListener(e -> {
			Logger.info("Add Transaction button clicked");
        	addTransaction();
        });
        
        // Handle "Delete" menu item clicks
        view.getDeleteMenuItem().addActionListener(e -> {
			Logger.info("Delete item clicked");
        	delete();
        });
        
        // Handle "Open File..." menu item clicks
        view.getOpenFileMenuItem().addActionListener(e -> {
			Logger.info("Open File clicked.");
        	openFile();
        });
        
        // Handle "Save" menu item clicks
        view.getSaveAsMenuItem().addActionListener(e -> {	
			Logger.info("Save As menu item clicked");  
        	saveAs();
        });
        
        // Handle "Analyze" button clicks
        view.getAnalysisPanelView().getAnalyzeButton().addActionListener(e -> {
			Logger.info("Analyze button clicked");
        	performDataAnalysis();
        });
        
        // Initialize view
        view.setVisible(true);
		Logger.info("Expense Tracker view is visible");
    }
    
    public ExpenseTrackerModel getModel() {
    	// For testing purposes
    	return this.model;
    }
    
    public ExpenseTrackerView getView() {
    	// For testing purposes
    	return this.view;
    }
    
    public void addTransaction() { 
    	Logger.info("Starting add transaction operation");
		try {
    		// Get transaction data from view
    		double amount = view.getDataPanelView().getAmount(); 
    		String category = view.getDataPanelView().getCategory();

    		// Create transaction object
    		Transaction t = new Transaction(amount, category);

    		// Call controller to add transaction
    		model.addTransaction(t);
			Logger.info("Transaction added successfully");
    		view.refresh();
    	}
    	catch (NumberFormatException nfe) {
    		view.displayErrorMessage("The amount cannot be parsed as a double number.");
    	}
    	catch (IllegalArgumentException iae) {
    		view.displayErrorMessage(iae.getMessage());
    	}
    }
    
    public void delete() {
		Logger.info("Starting delete transaction operation");
        int selectedTransactionID = view.getDataPanelView().getSelectedTransactionID();
    	boolean removed = model.removeTransaction(selectedTransactionID);
    	if (! removed) {
    		view.displayErrorMessage("A valid transaction was not selected to be removed.");
    	}
    	else {
			Logger.info("Transaction deleted successfully");
    		view.refresh();
    	}
    }
    
    public void openFile() {
		Logger.info("Starting open file operation");
    	String inputFileName = view.showFileChooser(true);
    	if (inputFileName != null) {  	    
    		int transactionCount = model.getTransactions().size();
    		for (int i = 0; i < transactionCount; i++) {
    			model.removeTransaction(0);
    		}

    		try {
    			CSVImporter csvImporter = new CSVImporter();
    			List<Transaction> importedTransactionsList = csvImporter.importTransactions(inputFileName);
    			for (Transaction importedTransaction : importedTransactionsList) {				
    				model.addTransaction(importedTransaction);
    			}
				Logger.info("Open file operation completed successfully.");	
    		}
    		catch (IOException ioe) {
    			view.displayErrorMessage(ioe.getMessage());
    		}
    		view.refresh();
			Logger.info("View refreshed after open file operation.");
    	}
    }
    
    public void saveAs() {
		Logger.info("Starting save as operation");
    	String outputFileName = view.showFileChooser(false);
    	if (outputFileName != null) {
    		CSVExporter csvExporter = new CSVExporter();
    		String errorMessage = csvExporter.exportTransactions(model.getTransactions(), outputFileName);
    		if (errorMessage != null) {
    			view.displayErrorMessage(errorMessage);
    		}
    	}
    }
    
    public void performDataAnalysis() {
    	view.getAnalysisPanelView().performDataAnalysis(model);
    }
}
