package edu.umn.csci5801.model;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import edu.umn.csci5801.database.Constants;
import edu.umn.csci5801.database.DBCoordinator;

public class InstructorAndCourse{
    /*
     * Functions related to InstructorAndCourse Table
     */
    
    /**
     * This function is used for getting the next available ID to add to table InstructorAndCourse since IDs must be
     * unique
     * @return an integer that is the next available ID to insert
     * -1 is returned if en exception is raised
     */
    public int querySize(){
        DBCoordinator A = new DBCoordinator();

        try{
            List<ArrayList<Object>> queryResult = A.queryData("SELECT * FROM INSTRUCTORANDCOURSE");
            int totalSize = queryResult.size();
            //get the last ID of the entry in table InstructorAndCourse
            int temp = (int) queryResult.get(totalSize-1).get(0);
            //return the integer that is one more than the current biggest ID, so the next ID is always unique
            return temp + 1;
        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * This function is used for query the information in InstructorAndCourse table by the courseID 
     * @param courseID
     * @return  A list of ArrayList, in the ArrayList, each entry stores one query result in the table property order as a string.
     * In InstructorAndCourse Table, the proper order is ID, courseID, instructorID. So in 
     * one entry of ArrayList, it should store the value in this order, which is ID, courseID, instructorID.
     * Empty list will be returned if the query is failed.
     */   
    public List<ArrayList<String>> queryByCourseID(int courseID){
        List<ArrayList<String>> res = new ArrayList<>();
        List<ArrayList<String>> empty = new ArrayList<>();
        try{
            DBCoordinator A = new DBCoordinator();
            //select from table for qualifying records
            List<ArrayList<Object>> queryResult = A.queryData("SELECT * FROM INSTRUCTORANDCOURSE WHERE COURSEID=" + courseID);
            for (int i = 0; i < queryResult.size(); i++){
                ArrayList<Object> rows = queryResult.get(i);
                ArrayList<String> tempList = new ArrayList<>();

                //convert each entry to a string and add to the return list
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
    
    /**
     * This function is used for query the information in IntructorAndCourse table by the courseID and instructorID 
     * @param courseID
     * @param instructorID
     * @return  A list of ArrayList, in the ArrayList, each entry stores one query result in the table property order as a string.
     * In InstructorAndCourse Table, the proper order is ID, courseID, instructorID. So in 
     * one entry of ArrayList, it should store the value in this order, which is ID, courseID, instructorID.
     * Empty list will be returned if the query is failed.
     */
    public List<ArrayList<String>> queryByCourseIDAndInstructorID(int courseID, int instructorID){
        List<ArrayList<String>> res = new ArrayList<>();
        List<ArrayList<String>> empty = new ArrayList<>();
        try{
            DBCoordinator A = new DBCoordinator();
            //select from table for qualifying records
            List<ArrayList<Object>> queryResult = A.queryData("SELECT * FROM INSTRUCTORANDCOURSE WHERE COURSEID='" + courseID + "' AND INSTRUCTORID='" + instructorID + "'");
            for (int i = 0; i < queryResult.size(); i++){
                ArrayList<Object> rows = queryResult.get(i);
                ArrayList<String> tempList = new ArrayList<>();

                //convert each entry to a string and add to the return list
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

    /**
     * This function is used for query the information in InstructorAndCourse table by the instructorID 
     * @param instructorID
     * @return  A list of ArrayList, in the ArrayList, each entry stores one query result in the table property order as a string.
     * In InstructorAndCourse Table, the proper order is ID, courseID, instructorID. So in 
     * one entry of ArrayList, it should store the value in this order, which is ID, courseID, instructorID.
     * Empty list will be returned if the query is failed.
     */    
    public List<ArrayList<String>> queryByInstructorID(int instructorID){
        List<ArrayList<String>> res = new ArrayList<>();
        List<ArrayList<String>> empty = new ArrayList<>();
        try{
            DBCoordinator A = new DBCoordinator();
            //select from table for qualifying records
            List<ArrayList<Object>> queryResult = A.queryData("SELECT * FROM INSTRUCTORANDCOURSE WHERE INSTRUCTORID=" + instructorID);
            for (int i = 0; i < queryResult.size(); i++){
                ArrayList<Object> rows = queryResult.get(i);
                ArrayList<String> tempList = new ArrayList<>();
                //convert each entry to a string and add to the return list
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

    /**
     * This function is used to insert a new record to the InstructorAndCourse table given ID, courseID and instructorID
     * @param ID
     * @param courseID
     * @param instructorID
     */    
    public void insert(int ID, int courseID, int instructorID){
        
        DBCoordinator A = new DBCoordinator();
        //construct the datalist with all values of the entries stored
        ArrayList<String> dataList = new ArrayList<String>(); 
        dataList.add(Integer.toString(ID));
        dataList.add(Integer.toString(courseID));
        dataList.add(Integer.toString(instructorID));
        //construct the typeList with all types of the entries stored
        ArrayList<Constants.PrimitiveDataType> typeList = new ArrayList<Constants.PrimitiveDataType>();
        typeList.add(Constants.PrimitiveDataType.INT);
        typeList.add(Constants.PrimitiveDataType.INT);
        typeList.add(Constants.PrimitiveDataType.INT);
        
        try{
            //construct and execute the sql insert command
            A.insertData("INSERT INTO INSTRUCTORANDCOURSE VALUES (?,?,?)", dataList, typeList);
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }catch (SQLException e){
            e.printStackTrace();
        }catch (ParseException e){
            e.printStackTrace();
        }
        
    }
    
    /**
     * This function is used to update the instructorID of a class given the course ID and new value of the instructorID
     * @param courseID
     * @param instructorID
     */
    public void updateByCourseID(int courseID, int instructorID){
        DBCoordinator A = new DBCoordinator();
        try{
            ArrayList<String> dataList = new ArrayList<String>();
            dataList.add(Integer.toString(instructorID));
            ArrayList<Constants.PrimitiveDataType> typeList = new ArrayList<Constants.PrimitiveDataType>();
            typeList.add(0,Constants.PrimitiveDataType.INT);
            
            A.updateData("UPDATE INSTRUCTORANDCOURSE SET INSTRUCTORID = ?" + " WHERE COURSEID = " + courseID, dataList, typeList);
            
        }catch (ParseException e){
             e.printStackTrace();
        }catch (ClassNotFoundException | SQLException e){
         e.printStackTrace();
        }
    }

    /**
     * This function is used to update the courseID of a class given the instructor ID and new value of the courseID
     * @param courseID
     * @param instructorID
     */    
    public void updateByInstructorID(int courseID, int instructorID){
        DBCoordinator A = new DBCoordinator();
        try{
            ArrayList<String> dataList = new ArrayList<String>();
            dataList.add(Integer.toString(courseID));
            ArrayList<Constants.PrimitiveDataType> typeList = new ArrayList<Constants.PrimitiveDataType>();
            typeList.add(0,Constants.PrimitiveDataType.INT);
            
            A.updateData("UPDATE INSTRUCTORANDCOURSE SET COURSEID = ?" + " WHERE INSTRUCTORID = " + instructorID, dataList, typeList);
            
        }catch (ParseException e){
             e.printStackTrace();
        }catch (ClassNotFoundException | SQLException e){
         e.printStackTrace();
        }
    }
    

     /**
     * This function is used to delete the records that contain courses taught by the instructor given instructorID
     * @param instructorID
     */
    public void deleteByInstructorID(int instructorID){
        DBCoordinator A = new DBCoordinator();
        try{
            ArrayList<String> dataList = new ArrayList<String>();
            ArrayList<Constants.PrimitiveDataType> typeList = new ArrayList<Constants.PrimitiveDataType>();
            //get the list of records that represent courses taught by the instructor with instructorID
            List<ArrayList<Object>> queryResult = A.queryData("SELECT ID FROM INSTRUCTORANDCOURSE WHERE INSTRUCTORID=" + instructorID);
            //delete each selected record
            for (int i = 0; i < queryResult.size(); i ++){
                int ID = (int) queryResult.get(i).get(0);
                A.deleteData("DELETE FROM INSTRUCTORANDCOURSE WHERE ID = " + ID, dataList, typeList);
            }
        }catch (ParseException e){
            e.printStackTrace();
        }catch (ClassNotFoundException | SQLException e1){
            e1.printStackTrace();
        }
    }
    
    /**
     * This function is used to delete the records that contain courses with courseID
     * @param courseID
     */
    public void deleteByCourseID(int courseID){
        DBCoordinator A = new DBCoordinator();
        try{
            ArrayList<String> dataList = new ArrayList<String>();
            ArrayList<Constants.PrimitiveDataType> typeList = new ArrayList<Constants.PrimitiveDataType>();
            //get the list of records that represent courses with courseID
            List<ArrayList<Object>> queryResult = A.queryData("SELECT ID FROM INSTRUCTORANDCOURSE WHERE COURSEID=" + courseID);
            //delete each selected record
            for (int i = 0; i < queryResult.size(); i ++){
                int ID = (int) queryResult.get(i).get(0);
                A.deleteData("DELETE FROM INSTRUCTORANDCOURSE WHERE ID = " + ID, dataList, typeList);
            }
        }catch (ParseException e){
            e.printStackTrace();
        }catch (ClassNotFoundException | SQLException e1){
            e1.printStackTrace();
        }
    }

}