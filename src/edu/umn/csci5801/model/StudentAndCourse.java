package edu.umn.csci5801.model;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import edu.umn.csci5801.database.Constants;
import edu.umn.csci5801.database.DBCoordinator;

public class StudentAndCourse {
    /*
     * Functions related to StudentAndCourse Table
     */
    
    /**
     * This function should query the number of rows in StudentAndCourse Table.
     * @return the number of rows in StudentAndCourse Table.
     * -1 will be returned if the query is failed.
     */
    public int querySize(){
        DBCoordinator A = new DBCoordinator();
        try{
            List<ArrayList<Object>> queryResult = A.queryData("SELECT * FROM STUDENTANDCOURSE");
            int totalSize = queryResult.size();
            int size = (int) queryResult.get(totalSize-1).get(0);
            return size + 1;
        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
        return -1;
    }
    
    /**
     * This function should query all rows in StudentAndCourse Table by courseID.
     * @param courseID
     * @return information related to a course (such as grading, course term, studentID)
     * Empty list will be returned if the query is failed.
     */
   public List<ArrayList<String>> queryByCourseID(int courseID){
       List<ArrayList<String>> res = new ArrayList<>();
       List<ArrayList<String>> empty = new ArrayList<>();
       try{
       DBCoordinator A = new DBCoordinator();
       List<ArrayList<Object>> queryResult = A.queryData("SELECT * FROM STUDENTANDCOURSE WHERE COURSEID=" + courseID);
       for (int i = 0; i < queryResult.size(); i++){
           ArrayList<Object> rows = queryResult.get(i);
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

   /**
    * This function should query all rows in StudentAndCourse Table by courseID and studentID.
    * @param courseID
    * @param studentID
    * @return information related to a course and a student (such as grading, course term)
    * Empty list will be returned if the query is failed.
    */
   public List<ArrayList<String>> queryByCourseIDAndStudentID(int courseID, int studentID){
       List<ArrayList<String>> res = new ArrayList<>();
       List<ArrayList<String>> empty = new ArrayList<>();
       try{
           DBCoordinator A = new DBCoordinator();
           List<ArrayList<Object>> queryResult = A.queryData("SELECT * FROM STUDENTANDCOURSE WHERE COURSEID='" + courseID + "' AND STUDENTID='" + studentID + "'");       
       for (int i = 0; i < queryResult.size(); i++){
           ArrayList<Object> rows = queryResult.get(i);
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
    * This function should query all rows in StudentAndCourse Table by studentID.
    * @param studentID
    * @return information related to a student (such as grading, course term, courseID)
    * Empty list will be returned if the query is failed.
    */
   public List<ArrayList<String>> queryByStudentID(int studentID){
       List<ArrayList<String>> res = new ArrayList<>();
       List<ArrayList<String>> empty = new ArrayList<>();
       try{
           DBCoordinator A = new DBCoordinator();
           List<ArrayList<Object>> queryResult = A.queryData("SELECT * FROM STUDENTANDCOURSE WHERE STUDENTID=" + studentID);   
           
           for (int i = 0; i < queryResult.size(); i++){
               ArrayList<Object> rows = queryResult.get(i);
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
   
   /**
    * This function should insert an new entry to StudentAndCourse table.
    * @param ID
    * @param courseID
    * @param grading
    * @param courseTerm
    * @param studentID
    */   
   public void insert(int ID, int courseID, String grading, String courseTerm, int studentID){
       DBCoordinator A = new DBCoordinator();
       ArrayList<String> dataList = new ArrayList<String>(); 
       dataList.add(Integer.toString(ID));
       dataList.add(Integer.toString(courseID));
       dataList.add(grading);
       dataList.add(courseTerm); 
       dataList.add(Integer.toString(studentID));
       ArrayList<Constants.PrimitiveDataType> typeList = new ArrayList<Constants.PrimitiveDataType>();
       typeList.add(Constants.PrimitiveDataType.INT);
       typeList.add(Constants.PrimitiveDataType.INT);
       typeList.add(Constants.PrimitiveDataType.STRING);
       typeList.add(Constants.PrimitiveDataType.STRING);
       typeList.add(Constants.PrimitiveDataType.INT);      
       try{
           A.insertData("INSERT INTO STUDENTANDCOURSE VALUES (?,?,?,?,?)", dataList, typeList);
       }catch (ClassNotFoundException e){
           e.printStackTrace();
       }catch (SQLException e){
           e.printStackTrace();
       }catch (ParseException e){
           e.printStackTrace();
       }   
   }
 
   /**
    * This function should update an existing entry in StudentAndCourse table.
    * @param studentID
    * @param courseID
    * @param grading
    * @param courseTerm
    */   
   public void update(int studentID, int courseID, String grading, String courseTerm) {
       DBCoordinator A = new DBCoordinator();
       try{
           ArrayList<String> dataList = new ArrayList<String>();
               dataList.add(grading);
           // check if the courseTerm is empty.
           if (!courseTerm.isEmpty()){
               dataList.add(courseTerm);   
           }
           ArrayList<Constants.PrimitiveDataType> typeList = new ArrayList<Constants.PrimitiveDataType>();
           typeList.add(0,Constants.PrimitiveDataType.STRING); 
           
           if (!courseTerm.isEmpty()){
               typeList.add(0,Constants.PrimitiveDataType.STRING);  
           }
           //check the grading basis.
           if (grading == "A-F" || grading == "S/N" || grading == "AUD"){
               A.updateData("UPDATE STUDENTANDCOURSE SET GRADING=?, COURSETERM=?" + " WHERE COURSEID = " + courseID + " AND STUDENTID=" + studentID , dataList, typeList);
           }
       }catch (ParseException e){
            e.printStackTrace();
       }catch (ClassNotFoundException | SQLException e){
        e.printStackTrace();
       }
   }

   /**
    * This function should delete an entry from StudentAndCourse by studentID and courseID.
    * @param studentID
    * @param courseID
    */   
   public void deleteByStudentIDAndCourseID(int studentID, int courseID){
       DBCoordinator A = new DBCoordinator();
       try{    
           ArrayList<String> dataList = new ArrayList<String>();
           ArrayList<Constants.PrimitiveDataType> typeList = new ArrayList<Constants.PrimitiveDataType>();
           A.deleteData("DELETE FROM STUDENTANDCOURSE WHERE STUDENTID=" + studentID + " AND COURSEID=" + courseID, dataList, typeList);
       }catch (ParseException e){
                e.printStackTrace();
       }catch (ClassNotFoundException | SQLException e1){
        e1.printStackTrace();
       }
   }
}
