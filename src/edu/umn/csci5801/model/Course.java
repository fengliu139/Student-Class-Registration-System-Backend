package edu.umn.csci5801.model;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import edu.umn.csci5801.database.Constants;
import edu.umn.csci5801.database.DBCoordinator;

public class Course{
    /*
     * Functions related to Course Table
     */
    
    /**
     * This function is used to query a list of qualified classes given user specified constraints: location, name, term, etc.
     * @param ID 
     * @param name
     * @param term
     * @param location
     * @param department
     * @param classType
     * @return A list of ArrayList, in the ArrayList, each entry stores one query result in the table property order.
     * In Course Table, the proper order is ID, Name, Credits, etc. so in one entry of ArrayList, it should store
     * the values in this order as strings , which is ID, Name, Credits, etc.
     * Empty list will be returned if the query is failed.
     */
	public List<ArrayList<String>> queryClass(int ID, String name, String term, String location, String department, String classType){ 
	    List<ArrayList<String>> res = new ArrayList<>();
	    List<ArrayList<String>> empty = new ArrayList<>();
	    StringBuilder sqlcmd = new StringBuilder();
        //construct the sql command for selecting classes from course table
	    sqlcmd.append("SELECT * FROM COURSE WHERE 0=0 ");
        if (ID>=0){
            sqlcmd.append("AND ID=" + ID + " ");
        }
        if (!name.isEmpty()){
            sqlcmd.append("AND NAME=\"" + name + "\" ");
        }
        if (!term.isEmpty()){
            sqlcmd.append("AND TERM=\"" + term + "\" ");
        }
        if (!location.isEmpty()){
            sqlcmd.append("AND LOCATION=\"" + location + "\" ");
        }
        if (!department.isEmpty()){
            sqlcmd.append("AND DEPARTMENT=\"" + department + "\" ");
        }
        if(!classType.isEmpty()){
            sqlcmd.append("AND TYPE=\"" + classType + "\" ");
        }
	    try{
            //convert each entry of each qualified class to string and then store into return list
	        DBCoordinator A = new DBCoordinator();
	        List<ArrayList<Object>> queryResult = A.queryData(sqlcmd.toString());
	        for (int i = 0; i < queryResult.size(); i++){
	            ArrayList<Object> rows = queryResult.get(i);
	            ArrayList<String> tempList = new ArrayList<>();
	            for (int j = 0; j < rows.size(); j++) {
	                if (rows.get(j)!=null){
	                   tempList.add(rows.get(j).toString());
	                }
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
     * This function is used to query a list of classes by courseID.
     * @param courseID
     * @return A list of Object, each object corresponds to a record in the Course table with given courseID.
     * Empty list will be returned if the query is failed.
     */
	public ArrayList<Object> queryByID(int courseID){
	    ArrayList<Object> temp = new ArrayList<Object>();
        try{
            DBCoordinator A = new DBCoordinator();
            List<ArrayList<Object>> res = A.queryData("SELECT * FROM COURSE WHERE ID=" + courseID);
            if (res.size()!=0){
                return res.get(0);
            }
            return temp;
        }catch (ClassNotFoundException e){
            System.out.println("Class Not Found");
            return temp;
        }catch (SQLException e){
            System.out.println("SQLException");
            return temp;
        }
    }

    /**
     * This function is used to insert a new class to the Course table with specified ID, name, credits, capacity, etc
     * It will print the stack trace if an exception was raised.
     * @param ID 
     * @param name
     * @param credits
     * @param capacity
     * @param term
     * @param firstDay
     * @param lastDay
     * @param classBeginTime
     * @param classEndTime
     * @param routines
     * @param location
     * @param type 
     * @param prerequisite
     * @param description
     * @param department
     */	
	public void insert(int ID, String name, int credits, int capacity, String term, String firstDay, String lastDay, String classBeginTime, String classEndTime, String routines, String location, String type, String prerequisite, String description, String department){
	    
        //construct the dataList, store the values of attributes of a course to the dataList
        ArrayList<String> dataList = new ArrayList<String>();
	    dataList.add(Integer.toString(ID));
	    dataList.add(name);
	    dataList.add(Integer.toString(credits));
	    dataList.add(Integer.toString(capacity));
	    dataList.add(term);
	    dataList.add(firstDay);
	    dataList.add(lastDay);
	    dataList.add(classBeginTime);
	    dataList.add(classEndTime);
	    dataList.add(routines);
	    dataList.add(location);
	    dataList.add(type);
	    dataList.add(prerequisite);
	    if (!description.isEmpty()){
            dataList.add(description);   
	    }
	    dataList.add(department);
	    
        //construct the typeList, store the types of attributes of a course to the typeList
	    ArrayList<Constants.PrimitiveDataType> typeList = new ArrayList<Constants.PrimitiveDataType>();
	    typeList.add(0,Constants.PrimitiveDataType.INT);
	    typeList.add(1, Constants.PrimitiveDataType.STRING);
	    typeList.add(2,Constants.PrimitiveDataType.INT);
	    typeList.add(3,Constants.PrimitiveDataType.INT);
	    typeList.add(4, Constants.PrimitiveDataType.STRING);
	    typeList.add(5, Constants.PrimitiveDataType.DATE);
	    typeList.add(6, Constants.PrimitiveDataType.DATE);
	    typeList.add(7, Constants.PrimitiveDataType.STRING);
	    typeList.add(8, Constants.PrimitiveDataType.STRING);
	    typeList.add(9, Constants.PrimitiveDataType.STRING);
	    typeList.add(10, Constants.PrimitiveDataType.STRING);
	    typeList.add(11, Constants.PrimitiveDataType.STRING);
	    typeList.add(12, Constants.PrimitiveDataType.STRING);
	    if (!description.isEmpty()){
            typeList.add(13, Constants.PrimitiveDataType.STRING);  
	    }
	    typeList.add(14, Constants.PrimitiveDataType.STRING);
	    
	    DBCoordinator A = new DBCoordinator();
	    try {
            A.insertData("INSERT INTO COURSE VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", dataList, typeList);
        }catch (ClassNotFoundException | SQLException | ParseException e){
            e.printStackTrace();
        }
	}

    /**
     * This function is used to update an existing class information according to the new values given, including ID, name, credits, capacity, etc
     * It will print the stack trace if an exception was raised.
     * @param ID 
     * @param name
     * @param credits
     * @param capacity
     * @param term
     * @param firstDay
     * @param lastDay
     * @param classBeginTime
     * @param classEndTime
     * @param routines
     * @param location
     * @param type 
     * @param prerequisite
     * @param description
     * @param department
     */	
	/*
	public void update(int ID, String name, int credits, int capacity, String term, String firstDay, String lastDay, String classBeginTime, String classEndTime, String routines, String location, String type, String prerequisite, String description, String department){
        
       ArrayList<String> dataList = new ArrayList<String>();
       ArrayList<Constants.PrimitiveDataType> typeList = new ArrayList<Constants.PrimitiveDataType>();
       DBCoordinator A = new DBCoordinator();

       //check consttraints and construct sql command for each entry needs to be updated and execute
       try{
        if (!name.isEmpty()){
            dataList.add(name);
            typeList.add(Constants.PrimitiveDataType.STRING);
            //A.updateData("UPDATE COURSE SET NAME=? WHERE ID='" + ID + "'", dataList, typeList);
        }
        if (credits > 0 && credits <= 4){
            dataList.add(String.valueOf(credits));
            typeList.add(Constants.PrimitiveDataType.INT);
            //A.updateData("UPDATE COURSE SET CREDITS=? WHERE ID='" + ID + "'", dataList, typeList);
        }
        if (capacity>0 && capacity<=30){
            dataList.add(String.valueOf(capacity));
            typeList.add(Constants.PrimitiveDataType.INT);
            //A.updateData("UPDATE COURSE SET CAPACITY=? WHERE ID='" + ID + "'", dataList, typeList);
        }
        if (!term.isEmpty()){
            dataList.add(term);
            typeList.add(Constants.PrimitiveDataType.STRING);
            //A.updateData("UPDATE COURSE SET TERM=? WHERE ID='" + ID + "'", dataList, typeList);
        }
        if (!firstDay.isEmpty()){
            dataList.add(firstDay);
            typeList.add(Constants.PrimitiveDataType.STRING);
            //A.updateData("UPDATE COURSE SET FIRSTDAY=? WHERE ID='" + ID + "'", dataList, typeList);
        }
        if (!lastDay.isEmpty()){
            dataList.add(lastDay);
            typeList.add(Constants.PrimitiveDataType.STRING);
            //A.updateData("UPDATE COURSE SET LASTDAY=? WHERE ID='" + ID + "'", dataList, typeList);
        }
        if (!classBeginTime.isEmpty()){
            dataList.add(classBeginTime);
            typeList.add(Constants.PrimitiveDataType.STRING);
            //A.updateData("UPDATE COURSE SET CLASSBEGINTIME=? WHERE ID='" + ID + "'", dataList, typeList);
        }
        if (!classEndTime.isEmpty()){
            dataList.add(classEndTime);
            typeList.add(Constants.PrimitiveDataType.STRING);
            //A.updateData("UPDATE COURSE SET CLASSENDTIME=? WHERE ID='" + ID + "'", dataList, typeList);
        }
        if (!routines.isEmpty()){
            dataList.add(routines);
            typeList.add(Constants.PrimitiveDataType.STRING);
            //A.updateData("UPDATE COURSE SET ROUTINES=? WHERE ID='" + ID + "'", dataList, typeList);
        }
        if (!location.isEmpty()){
            dataList.add(location);
            typeList.add(Constants.PrimitiveDataType.STRING);
            //A.updateData("UPDATE COURSE SET LOCATION=? WHERE ID='" + ID + "'", dataList, typeList);
        }
        if (type == "Seminar" || type == "Lecture"){
            dataList.add(type);
            typeList.add(Constants.PrimitiveDataType.STRING);
            //A.updateData("UPDATE COURSE SET TYPE=? WHERE ID='" + ID + "'", dataList, typeList);
        }
        if (!prerequisite.isEmpty()){
            dataList.add(prerequisite);
            typeList.add(Constants.PrimitiveDataType.STRING);
            //A.updateData("UPDATE COURSE SET PREREQUISITE=? WHERE ID='" + ID + "'", dataList, typeList);
        }
        if (!description.isEmpty()){
            dataList.add(description);
            typeList.add(Constants.PrimitiveDataType.STRING);
            //A.updateData("UPDATE COURSE SET DESCRIPTION=? WHERE ID='" + ID + "'", dataList, typeList);
        }
        if (department == "CS"){
            dataList.add(department);
            typeList.add(Constants.PrimitiveDataType.STRING);
            //A.updateData("UPDATE COURSE SET DEPARTMENT=? WHERE ID='" + ID + "'", dataList, typeList);
        }
        A.updateData("UPDATE COURSE SET DEPARTMENT=? WHERE ID='" + ID + "'", dataList, typeList);
    }catch (ClassNotFoundException | SQLException | ParseException e){
        e.printStackTrace();
        }
    }
*/
	public void update(int ID, String name, int credits, int capacity, String term, String firstDay, String lastDay, String classBeginTime, String classEndTime, String routines, String location, String type, String prerequisite, String description, String department){
	     
	       DBCoordinator A = new DBCoordinator();

	       //check consttraints and construct sql command for each entry needs to be updated and execute
	       try{
	        if (!name.isEmpty()){
	            ArrayList<String> dataList = new ArrayList<String>();
	            ArrayList<Constants.PrimitiveDataType> typeList = new ArrayList<Constants.PrimitiveDataType>();
	            dataList.add(name);
	            typeList.add(Constants.PrimitiveDataType.STRING);
	            A.updateData("UPDATE COURSE SET NAME=? WHERE ID='" + ID + "'", dataList, typeList);
	        }
	        if (credits > 0 && credits <= 4){   
	            ArrayList<String> dataList = new ArrayList<String>();
	            ArrayList<Constants.PrimitiveDataType> typeList = new ArrayList<Constants.PrimitiveDataType>();
	            dataList.add(String.valueOf(credits));
	            typeList.add(Constants.PrimitiveDataType.INT);
	            A.updateData("UPDATE COURSE SET CREDITS=? WHERE ID='" + ID + "'", dataList, typeList);
	        }
	        if (capacity>0 && capacity<=30){   
	            ArrayList<String> dataList = new ArrayList<String>();
	            ArrayList<Constants.PrimitiveDataType> typeList = new ArrayList<Constants.PrimitiveDataType>();
	            dataList.add(String.valueOf(capacity));
	            typeList.add(Constants.PrimitiveDataType.INT);
	            A.updateData("UPDATE COURSE SET CAPACITY=? WHERE ID='" + ID + "'", dataList, typeList);
	        }
	        if (!term.isEmpty()){   
	            ArrayList<String> dataList = new ArrayList<String>();
	            ArrayList<Constants.PrimitiveDataType> typeList = new ArrayList<Constants.PrimitiveDataType>();
	            dataList.add(term);
	            typeList.add(Constants.PrimitiveDataType.STRING);
	            A.updateData("UPDATE COURSE SET TERM=? WHERE ID='" + ID + "'", dataList, typeList);
	        }
	        if (!firstDay.isEmpty()){   
	            ArrayList<String> dataList = new ArrayList<String>();
	            ArrayList<Constants.PrimitiveDataType> typeList = new ArrayList<Constants.PrimitiveDataType>();
	            dataList.add(firstDay);
	            typeList.add(Constants.PrimitiveDataType.STRING);
	            A.updateData("UPDATE COURSE SET FIRSTDAY=? WHERE ID='" + ID + "'", dataList, typeList);
	        }
	        if (!lastDay.isEmpty()){   
	            ArrayList<String> dataList = new ArrayList<String>();
	            ArrayList<Constants.PrimitiveDataType> typeList = new ArrayList<Constants.PrimitiveDataType>();
	            dataList.add(lastDay);
	            typeList.add(Constants.PrimitiveDataType.STRING);
	            A.updateData("UPDATE COURSE SET LASTDAY=? WHERE ID='" + ID + "'", dataList, typeList);
	        }
	        if (!classBeginTime.isEmpty()){   
	            ArrayList<String> dataList = new ArrayList<String>();
	            ArrayList<Constants.PrimitiveDataType> typeList = new ArrayList<Constants.PrimitiveDataType>();
	            dataList.add(classBeginTime);
	            typeList.add(Constants.PrimitiveDataType.STRING);
	            A.updateData("UPDATE COURSE SET CLASSBEGINTIME=? WHERE ID='" + ID + "'", dataList, typeList);
	        }
	        if (!classEndTime.isEmpty()){   
	            ArrayList<String> dataList = new ArrayList<String>();
	            ArrayList<Constants.PrimitiveDataType> typeList = new ArrayList<Constants.PrimitiveDataType>();
	            dataList.add(classEndTime);
	            typeList.add(Constants.PrimitiveDataType.STRING);
	            A.updateData("UPDATE COURSE SET CLASSENDTIME=? WHERE ID='" + ID + "'", dataList, typeList);
	        }
	        if (!routines.isEmpty()){   
	            ArrayList<String> dataList = new ArrayList<String>();
	            ArrayList<Constants.PrimitiveDataType> typeList = new ArrayList<Constants.PrimitiveDataType>();
	            dataList.add(routines);
	            typeList.add(Constants.PrimitiveDataType.STRING);
	            A.updateData("UPDATE COURSE SET ROUTINES=? WHERE ID='" + ID + "'", dataList, typeList);
	        }
	        if (!location.isEmpty()){   
	            ArrayList<String> dataList = new ArrayList<String>();
	            ArrayList<Constants.PrimitiveDataType> typeList = new ArrayList<Constants.PrimitiveDataType>();
	            dataList.add(location);
	            typeList.add(Constants.PrimitiveDataType.STRING);
	            A.updateData("UPDATE COURSE SET LOCATION=? WHERE ID='" + ID + "'", dataList, typeList);
	        }
	        if (type == "Seminar" || type == "Lecture"){   
	            ArrayList<String> dataList = new ArrayList<String>();
	            ArrayList<Constants.PrimitiveDataType> typeList = new ArrayList<Constants.PrimitiveDataType>();
	            dataList.add(type);
	            typeList.add(Constants.PrimitiveDataType.STRING);
	            A.updateData("UPDATE COURSE SET TYPE=? WHERE ID='" + ID + "'", dataList, typeList);
	        }
	        if (!prerequisite.isEmpty()){   
	            ArrayList<String> dataList = new ArrayList<String>();
	            ArrayList<Constants.PrimitiveDataType> typeList = new ArrayList<Constants.PrimitiveDataType>();
	            dataList.add(prerequisite);
	            typeList.add(Constants.PrimitiveDataType.STRING);
	            A.updateData("UPDATE COURSE SET PREREQUISITE=? WHERE ID='" + ID + "'", dataList, typeList);
	        }
	        if (!description.isEmpty()){   
	            ArrayList<String> dataList = new ArrayList<String>();
	            ArrayList<Constants.PrimitiveDataType> typeList = new ArrayList<Constants.PrimitiveDataType>();
	            dataList.add(description);
	            typeList.add(Constants.PrimitiveDataType.STRING);
	            A.updateData("UPDATE COURSE SET DESCRIPTION=? WHERE ID='" + ID + "'", dataList, typeList);
	        }
	        if (department == "CS"){   
	            ArrayList<String> dataList = new ArrayList<String>();
	            ArrayList<Constants.PrimitiveDataType> typeList = new ArrayList<Constants.PrimitiveDataType>();
	            dataList.add(department);
	            typeList.add(Constants.PrimitiveDataType.STRING);
	            A.updateData("UPDATE COURSE SET DEPARTMENT=? WHERE ID='" + ID + "'", dataList, typeList);
	        }
	    }catch (ClassNotFoundException | SQLException | ParseException e){
	        e.printStackTrace();
	        }
	    }
    /**
     * This function is used to delete a class by its ID.
     * It will print the stack trace if an exception was raised.
     * @param ID
     */	
	public void deleteByID(int ID){
	    
        ArrayList<String> dataList = new ArrayList<String>();
        
        ArrayList<Constants.PrimitiveDataType> typeList = new ArrayList<Constants.PrimitiveDataType>();
        
        DBCoordinator A = new DBCoordinator();
        try {
            A.deleteData("DELETE FROM COURSE WHERE ID=" + ID , dataList, typeList);
        }catch (ClassNotFoundException | SQLException | ParseException e){
            e.printStackTrace();
        }
    }
	
}