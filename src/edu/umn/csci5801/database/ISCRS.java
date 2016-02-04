package edu.umn.csci5801.database;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import edu.umn.csci5801.database.ShibbolethAuth.Token;
import edu.umn.csci5801.model.Administrator;
import edu.umn.csci5801.model.Course;
import edu.umn.csci5801.model.Instructor;
import edu.umn.csci5801.model.InstructorAndCourse;
import edu.umn.csci5801.model.Student;
import edu.umn.csci5801.model.StudentAndCourse;

public class ISCRS implements SCRS{

    /*
     * Implement Interfaces
     */
    
    //1
    /**
     * This function have several responsibilities: 
     * 1. Can be used to perform generic class query; 
     * 2. Can be used to perform auto fill out relevant information for the users GUI;
     * @param courseID can be used by administrator for fast class query
     * @param courseName
     * @param location
     * @param term
     * @param department
     * @param classType
     * @param instructorName
     * @return A list of ArrayList, in the ArrayList, each entry stores one query result in the table property order.
     * E.g in Course Table, the proper order is ID, Name, Credits, etc. so in one entry of ArrayList, it should stores
     * the value in this order, which is ID, Name, Credits, etc.
     * Empty list will be returned if the query is failed. If no courseID is input, set it to -1.
     */
    @Override
    public List<ArrayList<String>> queryClass(int courseID, String courseName, String location, String term,
            String department, String classType, String instructorName){
        //check if instructor name is empty. If it is empty, search data from course table.
        if (instructorName == ""){
            List<ArrayList<String>> res = new ArrayList<>();
            Course newCourse = new Course();
            res = newCourse.queryClass(courseID, courseName, term, location, department, classType);
            for (int i=0; i<res.size(); i++){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                res.get(i).set(5, sdf.format(Long.parseLong(res.get(i).get(5))));
                res.get(i).set(6, sdf.format(Long.parseLong(res.get(i).get(6))));
            }
            return res;
        }else{
            Instructor newInstructor = new Instructor();
            ArrayList<Integer> ids = new ArrayList<>();
            ids = newInstructor.querybyName(instructorName);
            InstructorAndCourse newInstructorAndCourse = new InstructorAndCourse();
            List<ArrayList<String>> temp = new ArrayList<>();
            HashSet<String> courseSet = new HashSet<>();
            //search all courses taught by a instructor.
            for (int i=0;i<ids.size();i++){
                temp = newInstructorAndCourse.queryByCourseID(ids.get(i));
                for (int j=0; j<temp.size(); j++){
                    courseSet.add((temp.get(j).get(1)));
                }
            }
            List<ArrayList<String>> temp2 = new ArrayList<>();
            List<ArrayList<String>> res=new ArrayList<>();
            Course newCourse = new Course();
            temp2 = newCourse.queryClass(courseID, courseName, term, location, department, classType);
            //get matched course between InstructorAndCourse table and Course table.
            for (int i=0; i<temp2.size(); i++){
                if (courseSet.contains(temp2.get(i).get(0))){
                    res.add(temp2.get(i));
                }
            }
            for (int i=0; i<res.size(); i++){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                res.get(i).set(5, sdf.format(Long.parseLong(res.get(i).get(5))));
                res.get(i).set(6, sdf.format(Long.parseLong(res.get(i).get(6))));
            }
            return res;
        }
    }
    
    //2
    /**
     * This function is used for querying student personal data.
     * @param token
     * @param studentID
     * @return Student basic personal data, such as name, id, age, gender, degree, advisor(if applicable), total credits, etc.
     * Empty list will be returned if the query is failed.
     */
    @Override
    public List<ArrayList<String>> queryStudentPersonalData(Token token, int studentID){
        List<ArrayList<String>> res = new ArrayList<ArrayList<String>>();
        if (token == null){
            System.out.println("Not logged in");  
        }else if(token.type == ShibbolethAuth.Token.RoleType.ADMIN || token.type == ShibbolethAuth.Token.RoleType.UNDEFINED){
            System.out.println("Not a student");  
        }else{
            //student can only query his or her own personal data.
            if (token.id == studentID){
                Student temp = new Student();
                res = temp.querybyID(studentID);
                for (int i=0; i<res.size(); i++){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    res.get(i).set(3, sdf.format(Long.parseLong(res.get(i).get(3))));
                }
                return res;
            }
        }
        return res;
    }
    
    //3
    /**
     * This interface is used for querying student registration history
     * @param token
     * @param studentID
     * @return ClassID, ClassName, Registration Time(term), Credits
     * Empty list will be returned if the query is failed.
     */
    @Override
    public List<ArrayList<String>> queryStudentRegistrationHistory(Token token, int studentID){
        List<ArrayList<String>> res = new ArrayList<ArrayList<String>>();
        if (token == null){
            System.out.println("Not logged in");  
        }else if(token.type == ShibbolethAuth.Token.RoleType.ADMIN || token.type == ShibbolethAuth.Token.RoleType.UNDEFINED){
            System.out.println("Not a Student");  
        }else{
            //student can only query his or her own personal data.
            if (token.id == studentID){
            StudentAndCourse temp= new StudentAndCourse();
            res = temp.queryByStudentID(studentID);
            Course course = new Course();
            for (int i = 0; i < res.size(); i++){
                String courseID = res.get(i).get(1);
                ArrayList<Object> courses = course.queryByID(Integer.valueOf(courseID));
                int credits = (int) courses.get(2);
                res.get(i).add(Integer.toString(credits));
            }
            return res;
            }
        }
        return res;
    }
    
    //4
    /**
     * This function is used for querying admin basic information.
     * @param token Only Admin can invoke this function
     * @return Admin ID, Admin Name, Admin Department, etc.
     * Empty list will be returned if the query is failed.
     */
    @Override
    public List<ArrayList<String>> queryAdminPersonalData(Token token) {
        List<ArrayList<String>> res = new ArrayList<ArrayList<String>>();
        if (token == null){
            System.out.println("Not logged in");  
        }else if(token.type == ShibbolethAuth.Token.RoleType.STUDENT || token.type == ShibbolethAuth.Token.RoleType.UNDEFINED){
            System.out.println("Not an Admin");  
        }else{
            Administrator temp= new Administrator();
            res = temp.querybyID(token.id);
            for (int i=0; i<res.size(); i++){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                res.get(i).set(3, sdf.format(Long.parseLong(res.get(i).get(3))));
            }
            return res;
        }
        return res;
    }
    
    //5
    /**
     * This interface is used for querying instructors basic personal information
     * @param token
     * @param instructorID -1 if all instructors information need to be returned
     * @return Store a designated instructor's basic information in database table property order.
     * Empty list will be returned if the query is failed.
     */
    @Override
    public List<ArrayList<String>> queryInstructor(Token token, int instructorID){
        List<ArrayList<String>> res = new ArrayList<ArrayList<String>>();
        if (token == null){
            System.out.println("Not logged in");  
        }else if(token.type == ShibbolethAuth.Token.RoleType.STUDENT || token.type == ShibbolethAuth.Token.RoleType.UNDEFINED){
            System.out.println("Not an Admin");  
        }else{
            Instructor temp= new Instructor();
            res = temp.querybyID(instructorID);
            for (int i=0; i<res.size(); i++){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                res.get(i).set(3, sdf.format(Long.parseLong(res.get(i).get(3))));
            }
            return res;
        }
        return res;
    }
    
    //6
    /**
     * This interface should allow the students add one class to the database.
     * @param token
     * @param courseID
     * @param grading
     * @param courseTerm
     * @return Return true if the operation is successfully, false otherwise
     */
    @Override
    public boolean studentAddClass(Token token, int courseID, String grading, String courseTerm){
        try{
        //Date date = new Date();
        Course course = new Course();
      //  Calendar calendar = Calendar.getInstance();
        //long currentDate = date.getTime();
        //long dueDate = (long) course.queryByID(courseID).get(5);
        //Date due = new Date(dueDate);
        //calendar.setTime(due);
        //calendar.add(Calendar.MONTH, -2);        
       // long startDate = calendar.getTime().getTime();

        
        if (token == null){
            System.out.println("Not logged in");  
        }else if(token.type == ShibbolethAuth.Token.RoleType.ADMIN || token.type == ShibbolethAuth.Token.RoleType.UNDEFINED){
            System.out.println("Not an Student");
            //only when valid courseID, grading basis, courseTerm is provided, student can add class.
        }else if ((course.queryByID(courseID).size() != 0) && (!grading.isEmpty()) && (!courseTerm.isEmpty())){
            if (Integer.parseInt(token.timeStamp.substring(5, 7))>=9 || Integer.parseInt(token.timeStamp.substring(5, 7))<7){
                throw new TargetNotFound("not in your registration time...");
            }
            StudentAndCourse temp = new StudentAndCourse();
            List<ArrayList<String>> student_And_Course = temp.queryByStudentID(token.id);
            int totalCredits = 0;
            for (int i =0; i < student_And_Course.size(); i++){
                if (student_And_Course.get(i).get(3).equals(courseTerm)){
                    int tempCourseID = Integer.parseInt(student_And_Course.get(i).get(1));
                    totalCredits += (int) course.queryByID(tempCourseID).get(2);
                }
            }
            int capacity = temp.queryByCourseID(courseID).size();
            int reqCapacity = (int) course.queryByID(courseID).get(3);
            int thiscoursecredit= (int) course.queryByID(courseID).get(2);
            String dbCourseTerm = (String) course.queryByID(courseID).get(4);
            int if_register=temp.queryByCourseIDAndStudentID(courseID, token.id).size();
            
            //only when student's term total credits is less than 30, class seat is available ,and student has not registered the course, student can add class.
            if (totalCredits +thiscoursecredit<= 30 && capacity < reqCapacity  && (if_register == 0)){
                temp.insert(temp.querySize(), courseID, grading, courseTerm, token.id);
            return true;
            }
        }
        throw new InvalidOperation("invalid operation...");
        }catch (InvalidOperation e){
            return false;
        }catch (TargetNotFound e){
            return false;
        }
    }
    
    //7    
    /**
     * This interface should allow the students drop one class from the database.
     * @param token
     * @param courseID
     * @return Return true if the operation is successfully, false otherwise
     */
    @Override
    public boolean studentDropClass(Token token, int courseID) {
        try{


        if (token == null){
            System.out.println("Not logged in");  
        }else if(token.type == ShibbolethAuth.Token.RoleType.ADMIN || token.type == ShibbolethAuth.Token.RoleType.UNDEFINED){
            System.out.println("Not an Student");
        }else{
            if (Integer.parseInt(token.timeStamp.substring(5, 7))>=9 || Integer.parseInt(token.timeStamp.substring(5, 7))<7){
                throw new TargetNotFound("not in your registration time...");
            }
            StudentAndCourse temp= new StudentAndCourse();
            if( temp.queryByCourseIDAndStudentID(courseID, token.id).size() != 0){
                temp.deleteByStudentIDAndCourseID(token.id, courseID);
                return true;
            }
        }
        throw new InvalidOperation("invalid operation...");
        }catch (InvalidOperation e){
            return false;
        }catch (TargetNotFound e){
            return false;
        }
    }
    
    //8
    /**
     * This interface should allow the students edit one registered class in the database.
     * @param token
     * @param courseID
     * @param grading This parameter should just have one of "A/F", "Audit", "S/N" value
     * @param courseTerm
     * @return Return true if the operation is successfully, false otherwise
     */
    @Override
    public boolean studentEditClass(Token token, int courseID, String grading, String courseTerm){
        try{


        
        if (token == null){
            System.out.println("Not logged in");  
        }else if(token.type == ShibbolethAuth.Token.RoleType.ADMIN || token.type == ShibbolethAuth.Token.RoleType.UNDEFINED){
            System.out.println("Not an Student");  
        }else{
            if (Integer.parseInt(token.timeStamp.substring(5, 7))>=9 || Integer.parseInt(token.timeStamp.substring(5, 7))<7){
                throw new TargetNotFound("not in your registration time...");
            }
            StudentAndCourse temp= new StudentAndCourse();
            if (!temp.queryByCourseIDAndStudentID(courseID, token.id).isEmpty()){
                temp.update(token.id, courseID, grading, courseTerm);
                return true;
            }
        }
        throw new InvalidOperation("invalid operation...");
        }catch (InvalidOperation e){
            return false;
        }catch (TargetNotFound e){
            return false;
        }
    }

    //9
    /**
     * This interface should allow the admin to delete a class from the database if and only if there is not one register it.
     * @param token
     * @param courseID
     * @return Return true if the operation is successfully, false otherwise
     */
    @Override
    public boolean adminDeleteClass(Token token, int courseID) {
        try{
        if (token == null){
            System.out.println("Not logged in");  
            throw new InvalidOperation("invalid operation...");
        }else if(token.type == ShibbolethAuth.Token.RoleType.STUDENT || token.type == ShibbolethAuth.Token.RoleType.UNDEFINED){
            System.out.println("Not an Admin");
            throw new InvalidOperation("invalid operation...");
        }else{
            StudentAndCourse sac = new StudentAndCourse();
            if (sac.queryByCourseID(courseID).isEmpty()) {
                Course newCourse = new Course();
                if (!newCourse.queryByID(courseID).isEmpty()){
                    newCourse.deleteByID(courseID);
                    return true;   
                }
            }
            throw new InvalidOperation("invalid operation...");
        }
        }catch (InvalidOperation e){
            return false;
        }
    }

    //10
    /**
     * This interface should allow the admin to modify the existed class in the database.
     * The admin can only edit the class's description if at least one student registers this class
     * The admin can edit everything of the class if no one registers it
     * @param token
     * @param courseID
     * @param courseName
     * @param courseCredits
     * @param instructorID
     * @param firstDay The first day of the class in the new semester
     * @param lastDay The last day of the class in the new semester
     * @param classBeginTime E.g. 9:00
     * @param classEndTime  E.g. 16:00
     * @param weekDays E.g. Tu, Fri
     * @param location
     * @param type
     * @param prerequisite
     * @param description
     * @param department
     * @return Return true if the operation is successfully, false otherwise
     */
    @Override
    /*
    public boolean adminEditClass(Token token, int courseID, String courseName, int courseCredits, int instructorID,
            String firstDay, String lastDay, String classBeginTime, String classEndTime, String weekDays,
            String location, String type, String prerequisite, String description, String department) {
        try{
        //make sure only administrator token has been passed in to add the course
        if (token == null){
            System.out.println("Not logged in");  
            return false;
        }else if(token.type == ShibbolethAuth.Token.RoleType.STUDENT || token.type == ShibbolethAuth.Token.RoleType.UNDEFINED){
            System.out.println("Not an Admin");
        }else{
            StudentAndCourse sac = new StudentAndCourse();
            Course newCourse = new Course();

            if (sac.queryByCourseID(courseID).size()==0) {
                //update all entries of the course table if new one registered the course yet
                Instructor ins = new Instructor();
                if (!ins.querybyID(instructorID).isEmpty()){
                    //update the InstructorAndCourse table if given new instructorID
                    InstructorAndCourse temp = new InstructorAndCourse();
                        if (temp.queryByCourseIDAndInstructorID(courseID,instructorID).isEmpty()){
                            temp.updateByCourseID(courseID, instructorID);
                        }
                }
                if (!newCourse.queryByID(courseID).isEmpty()){
                    newCourse.update(courseID, courseName, courseCredits, -1, "", firstDay, lastDay, classBeginTime, classEndTime, "", location, type, prerequisite, description, department);
                    return true;
                    }
           }
            else{
                //only update the description entry in the course table if there are students in the course and new description is given
                if (!description.isEmpty()){
                    if (courseName.isEmpty() && (courseCredits < 0 || courseCredits > 4)
                            && firstDay.isEmpty() && lastDay.isEmpty() && classBeginTime.isEmpty() && classEndTime.isEmpty() && weekDays.isEmpty()
                            && location.isEmpty() && type.isEmpty() && prerequisite.isEmpty() && department.isEmpty()){
                        newCourse.update(courseID, "", -1, -1, "", "", "", "", "", "", "", "", "", description, "");
                        return true; 
                    }
                    else{
                        throw new InvalidInput("invalid input...");
                    }
                }
            }
        }
        throw new InvalidOperation("invalid operation...");
        }catch (InvalidOperation e){
            return false;
        }catch (InvalidInput e){
            return false;
        }
    }*/
    
    public boolean adminEditClass(Token token, int courseID, String courseName, int courseCredits, int instructorID,
            String firstDay, String lastDay, String classBeginTime, String classEndTime, String weekDays,
            String location, String type, String prerequisite, String description, String department) {
        try{
        //make sure only administrator token has been passed in to add the course
        if (token == null){
            System.out.println("Not logged in");  
            return false;
        }else if(token.type == ShibbolethAuth.Token.RoleType.STUDENT || token.type == ShibbolethAuth.Token.RoleType.UNDEFINED){
            System.out.println("Not an Admin");
        }else{
            
            Course newCourse = new Course();
            if (!newCourse.queryByID(courseID).isEmpty()){
                StudentAndCourse sac = new StudentAndCourse();
                if (sac.queryByCourseID(courseID).size()==0) {
                    //update all entries of the course table if new one registered the course yet
                    Instructor ins = new Instructor();
                    if (!ins.querybyID(instructorID).isEmpty()){
                        //update the InstructorAndCourse table if given new instructorID
                        InstructorAndCourse temp = new InstructorAndCourse();
                            if (temp.queryByCourseIDAndInstructorID(courseID,instructorID).isEmpty()){
                                temp.updateByCourseID(courseID, instructorID);
                            }                            
                            newCourse.update(courseID, courseName, courseCredits, -1, "", firstDay, lastDay, classBeginTime, classEndTime, "", location, type, prerequisite, description, department);
                            return true;
                    }
                }
           
            else{
                //only update the description entry in the course table if there are students in the course and new description is given
                if (!description.isEmpty()){
                    if (courseName.isEmpty() && (courseCredits < 0 || courseCredits > 4)
                            && firstDay.isEmpty() && lastDay.isEmpty() && classBeginTime.isEmpty() && classEndTime.isEmpty() && weekDays.isEmpty()
                            && location.isEmpty() && type.isEmpty() && prerequisite.isEmpty() && department.isEmpty()){
                        newCourse.update(courseID, "", -1, -1, "", "", "", "", "", "", "", "", "", description, "");
                        return true; 
                    }
                    else{
                        throw new InvalidInput("invalid input...");
                    }
                }
            }
        }}
        throw new InvalidOperation("invalid operation...");
        }catch (InvalidOperation e){
            return false;
        }catch (InvalidInput e){
            return false;
        }
    }


    //11
    /**
     * This function is used by administrators to add a student to a class, given the studentID, courseID, grading and courseTerm
     * @param token
     * @param studentID
     * @param courseID
     * @param grading
     * @param courseTerm
     * @return a boolean value indicating whether the student was successfully added to the course
     */
    @Override
    public boolean adminAddStudentToClass(Token token, int studentID, int courseID, String grading, String courseTerm) {
        try{
        Course course = new Course();
        Student student = new Student();
        //make sure only administrator token has been passed in to add the course
        if (token==null){
            System.out.println("not logged in");
        }else if (token.type==ShibbolethAuth.Token.RoleType.STUDENT||token.type==ShibbolethAuth.Token.RoleType.UNDEFINED){
            System.out.println("not an admin");
        }else if ((student.querybyID(studentID).size() != 0) && (course.queryByID(courseID).size() != 0) && (!grading.isEmpty()) && (!courseTerm.isEmpty())){         
            StudentAndCourse temp = new StudentAndCourse();
            List<ArrayList<String>> studentAndCourse = temp.queryByStudentID(token.id);
            //calculate the credits taken by the student in current semester
            int totalCredits = 0;
            for (int i =0; i < studentAndCourse.size(); i++){
                if (studentAndCourse.get(i).get(3).equals(courseTerm)){
                    int tempCourseID = Integer.parseInt(studentAndCourse.get(i).get(1));
                    totalCredits += (int) course.queryByID(tempCourseID).get(2);
                }
            }
            int capacity = temp.queryByCourseID(courseID).size();
            int reqCapacity = (int) course.queryByID(courseID).get(3);
            String dbCourseTerm = (String) course.queryByID(courseID).get(4);
            //make sure student's credits in one semesters does not exceed 30 and the course still has capacity 
            if (totalCredits < 30 && capacity < reqCapacity && dbCourseTerm.equals(courseTerm) && (temp.queryByCourseIDAndStudentID(courseID, token.id).isEmpty())) {
                temp.insert(temp.querySize(), courseID, grading, courseTerm, token.id);
                return true;
            }
        }
        throw new InvalidOperation("invalid operation...");
        }catch (InvalidOperation e){
            return false;
        }
        }

    //12
    /**
     * This function is used by administrators to edit a student registered class' information: grading or courseTerm, 
     * given student ID, course ID and new values of grading or courseTerm
     * @param token
     * @param studentID
     * @param courseID
     * @param grading
     * @param courseTerm
     * @return a boolean value indicating whether the course was successfully edited by the administrator
     */
    @Override
    public boolean adminEditStudentRegisteredClass(Token token, int studentID, int courseID, String grading,
            String courseTerm){
        try{
        //make sure only administrator token has been passed in to add the course
        if (token == null){
            System.out.println("Not logged in");  
        }else if(token.type == ShibbolethAuth.Token.RoleType.STUDENT || token.type == ShibbolethAuth.Token.RoleType.UNDEFINED){
            System.out.println("Not an Admin");
        }else{
            //update the StudentAndCourse table
            StudentAndCourse newCourse= new StudentAndCourse();
            newCourse.update(studentID, courseID, grading, courseTerm);
            return true;
        }
        throw new InvalidOperation("invalid operation...");
        }catch (InvalidOperation e){
            return false;
        }
    }

    //13
    /**
     * This function is used by administrators to drop a student registered class given the studentID and courseID
     * @param token
     * @param studentID
     * @param courseID
     * @return a boolean value indicating whether the course was successfully dropped from the student's schedule
     */
    @Override
    public boolean adminDropStudentRegisteredClass(Token token, int studentID, int courseID){
        try{
        //make sure only administrator token has been passed in to add the course
        if (token == null){
            System.out.println("Not logged in");  
        }else if(token.type == ShibbolethAuth.Token.RoleType.STUDENT || token.type == ShibbolethAuth.Token.RoleType.UNDEFINED){
            System.out.println("Not an Admin");
        }else{
            //delete the record from StudentAndCourse table 
            StudentAndCourse temp = new StudentAndCourse();
            if (!temp.queryByCourseIDAndStudentID(courseID, studentID).isEmpty()){
                temp.deleteByStudentIDAndCourseID(studentID, courseID);
                return true;
            }
        }
        throw new InvalidOperation("invalid operation...");
        }catch (InvalidOperation e){
            return false;
        }
    }

    //14
    /**
     * This function is used by administrators to add a new course to the database given attributes of the course
     * @param token
     * @param courseID
     * @param courseName
     * @param courseCredits
     * @param courseCapacity
     * @param term
     * @param instructorID
     * @param firstDay
     * @param lastDay
     * @param classBeginTime
     * @param classEndTime
     * @param weekDays
     * @param location
     * @param type
     * @param prerequisite
     * @param description
     * @param department
     * @return a boolean value indicating whether the new class was successfully added to the database
     */
    @Override
    public boolean adminAddClass(Token token, int courseID, String courseName, int courseCredits, int courseCapacity,
            String term, int instructorID, String firstDay, String lastDay, String classBeginTime, String classEndTime,
            String weekDays, String location, String type, String prerequisite, String description, String department){
        try{
        Instructor instructor = new Instructor();
        //make sure only administrator token has been passed in to add the course
        if (token == null){
            System.out.println("Not logged in");  
        }else if(token.type == ShibbolethAuth.Token.RoleType.STUDENT || token.type == ShibbolethAuth.Token.RoleType.UNDEFINED){
            System.out.println("Not an Admin");
        }else if ((courseID > 0) && (!courseName.isEmpty()) && (courseCredits > 0 && courseCredits <= 4) && (courseCapacity <= 30 && courseCapacity > 0)
                && (!term.isEmpty()) && (instructor.querybyID(instructorID).size() != 0) && (!firstDay.isEmpty()) && (!lastDay.isEmpty()) && (!classBeginTime.isEmpty()) && (!classEndTime.isEmpty())
                && (!weekDays.isEmpty()) && (!location.isEmpty()) && (!type.isEmpty()) && (!prerequisite.isEmpty()) && (!department.isEmpty())){
            Course newCourse= new Course();
            //update the Course table
            if (newCourse.queryByID(courseID).isEmpty()){
                newCourse.insert(courseID, courseName, courseCredits, courseCapacity, term, firstDay, lastDay, classBeginTime, classEndTime, weekDays, location, type, prerequisite, description, department);
            //update the InstructorAndCourse table if instructorID is provided    
                Instructor ins = new Instructor();
                if (!ins.querybyID(instructorID).isEmpty()){
                    InstructorAndCourse temp = new InstructorAndCourse();
                        if (temp.queryByCourseIDAndInstructorID(courseID,instructorID).isEmpty()){
                            temp.insert(temp.querySize(), courseID, instructorID);
                        }
                    }
                return true;
            }
            throw new InvalidInput("invalid input...");
        }
        throw new InvalidOperation("invalid operation...");
        }catch (InvalidInput e){
            return false;
        }catch (InvalidOperation e){
            return false;
        }
    }
}
