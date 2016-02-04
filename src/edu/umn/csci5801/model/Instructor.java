package edu.umn.csci5801.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.umn.csci5801.database.DBCoordinator;

public class Instructor {
    
    /*
     * Functions related to Instructor Table.
     */
    
    /**
     * This function query instructor information from Instructor Table by user ID.
     * @param userID
     * @return Instructor basic personal data, such as name, id, age, gender, department, salary, etc.
     * Empty list will be returned if the query is failed.
     */
    public List<ArrayList<String>> querybyID(int userID){
        List<ArrayList<String>> empty = new ArrayList<>();
        List<ArrayList<String>> res = new ArrayList<>();
        try{
            DBCoordinator A = new DBCoordinator();
            List<ArrayList<Object>> temp = A.queryData("SELECT * FROM INSTRUCTOR WHERE ID=" + userID);
            //if userID is -1, return all instructors' personal data.
            if (userID == -1){
                temp = A.queryData("SELECT * FROM INSTRUCTOR");
            }
            for (int i = 0; i < temp.size(); i++){
                ArrayList<Object> rows = temp.get(i);
                ArrayList<String> tempList = new ArrayList<>();
                for (int j = 0; j < rows.size(); j++) {
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

    /**
     * This function query instructor information from instructor Table by instructor's name.
     * @param name
     * @return Instructor basic personal data, such as name, id, age, gender, department, salary, etc.
     * Empty list will be returned if the query is failed.
     */
    public  ArrayList<Integer> querybyName(String name){
        String[] names=name.split(" ");
        ArrayList<Integer> res = new ArrayList<>();
        DBCoordinator newDC = new DBCoordinator();
        List<ArrayList<Object>> queryRes= new ArrayList<>();
        try{
            queryRes = newDC.queryData("SELECT ID FROM INSTRUCTOR WHERE FIRSTNAME='" +names[0]+"'"+" AND LASTNAME='" +names[1]+"'");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }catch (SQLException e){
            e.printStackTrace();
        }
        for (int i=0;i<queryRes.size();i++){
            res.add((int) queryRes.get(i).get(0));
        }
        return res;

    }
}
