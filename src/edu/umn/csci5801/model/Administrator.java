package edu.umn.csci5801.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.umn.csci5801.database.DBCoordinator;

public class Administrator{
    /*
     * Functions related to Administrator Table
     */
    
    /**
     * This function is used for query the information of an administrator(s) by the ID
     * @param userID specifies the ID of the administrator stored in the database
     * @return A list of ArrayList, in the ArrayList, each entry stores one query result in the table property order as a string.
     * In Administrator Table, the proper order is ID, FirstName, LastName, DateOfBirth, Gender, Department. So in 
     * one entry of ArrayList, it should store the value in this order, which is ID, FirstName, LastName, etc.
     * Empty list will be returned if the query is failed.
     */
    public List<ArrayList<String>> querybyID(int userID){
        List<ArrayList<String>> empty = new ArrayList<>();
        try{
            DBCoordinator A = new DBCoordinator();
            List<ArrayList<String>> res = new ArrayList<>();
            List<ArrayList<Object>> temp = A.queryData("SELECT * FROM ADMINISTRATOR WHERE ID=" + userID);
            //convert every entry of the admin to string and then store into the return list
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