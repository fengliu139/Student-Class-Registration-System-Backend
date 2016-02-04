package edu.umn.csci5801.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.umn.csci5801.database.DBCoordinator;

public class Student {
    /*
     * Functions related to Student Table
     */
    
    /**
     * This function query student information from Student Table by student ID.
     * @param userID
     * @return Student basic personal data, such as name, id, age, gender, degree, total credits, etc.
     * Empty list will be returned if the query is failed.
     */
	public List<ArrayList<String>> querybyID(int userID){
        List<ArrayList<String>> empty = new ArrayList<>();
        List<ArrayList<String>> res = new ArrayList<>();
	    try{
	        DBCoordinator A = new DBCoordinator();
	        List<ArrayList<Object>> temp = A.queryData("SELECT * FROM STUDENT WHERE ID=" + userID);
	        for (int i = 0; i < temp.size(); i++){
	            ArrayList<Object> rows = temp.get(i);
	            ArrayList<String> tempList = new ArrayList<>();
	            for (int j = 0; j < rows.size(); j++){
	                tempList.add(rows.get(j).toString());
	            }
	            res.add(tempList);
	        }
            return res;
        }catch (ClassNotFoundException e){
            System.out.println("Class Not Found");
            return empty;
        }catch (SQLException e){
            System.out.println("SQLException");
            return empty;
        }
	}
}
