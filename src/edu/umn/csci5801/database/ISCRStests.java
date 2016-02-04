package edu.umn.csci5801.database;

import edu.umn.csci5801.model.*;

import static org.junit.Assert.*;

import org.junit.Test;
import edu.umn.csci5801.database.ShibbolethAuth.*; 
import edu.umn.csci5801.database.ShibbolethAuth.Token.RoleType;  


public class ISCRStests {
    Token empty=null;
    //student that will pass credit limit
    Token stu1=new ShibbolethAuth.Token(1,RoleType.STUDENT,"2015.08.01.17.25.01");
    //Studnet that will pass time frame
    Token stu2=new ShibbolethAuth.Token(1,RoleType.STUDENT,"2015.10.01.17.25.01");
    Token stu3=new ShibbolethAuth.Token(2,RoleType.STUDENT,"2015.10.01.17.25.01");
    Token stu_3=new ShibbolethAuth.Token(3,RoleType.STUDENT,"2015.08.01.17.25.01");

    Token stu4=new ShibbolethAuth.Token(4,RoleType.STUDENT,"2015.08.01.17.25.01");
    Token stu4time=new ShibbolethAuth.Token(4,RoleType.STUDENT,"2015.10.01.17.25.01");
    Token stu_will_pass_credit=new ShibbolethAuth.Token(4,RoleType.STUDENT,"2015.08.01.17.25.01");
    Token nul=null;
    
    //Admin
    Token adm=new Token(1,RoleType.ADMIN,"2015.08.01.17.25.01");
    
    //BOth
    Token both=new Token(1,RoleType.BOTH,"2015.08.01.17.25.01");
    
    //UNDEFIEND
    Token unde=new Token(1,RoleType.UNDEFINED,"2015.08.01.17.25.01");
    ISCRS scrs=new ISCRS();

    @Test
    public void test_ShibbolethAuth() {
        try{
            ShibbolethAuth s=new ShibbolethAuth();
        Token test1=s.tokenGenerator("Abc1001","1000");
        assert(test1.type==RoleType.STUDENT);
        Token test2=s.tokenGenerator("Abc1002","2000");
        assert(test2.type==RoleType.STUDENT);
        Token test3=s.tokenGenerator("Abc1005","5000");
        assert(test3.type==RoleType.STUDENT);
        Token test4=s.tokenGenerator("Abc1001","6000");
        assert(test4.type==RoleType.STUDENT);
        Token test5=s.tokenGenerator("Abc1001er2","609900");
        assert(test5.type==RoleType.UNDEFINED);
        
        
        }
        catch(Exception e){
            fail("exception");
        }
    }
    @Test
    public void test_queryclass(){
        //int courseID, String courseName, String location, String term, String department,
        //String classType, String instructorName'
      
       
        assertEquals(1,scrs.queryClass(-1, "", "", "Fall2015", "", "", "").size());
        assertEquals(0,scrs.queryClass(-1, "Advanced Algorithm", "East Campus", "Fall2015", "", "", "").size());
        assertEquals(1,scrs.queryClass(1, "", "", "", "", "", "").size());
        
        assertEquals(0,scrs.queryClass(-1, "Advanced Algorithm", "East Campus", "Fall2015", "", "", "Daniel Mack").size());
        assertEquals(0,scrs.queryClass(1, "Advanced Algorithm", "East Campus", "Fall2015", "", "", "Daniel Mack").size());
        assertEquals(1,scrs.queryClass(-1, "", "", "", "", "", "Daniel Mack").size());
        assertEquals(1,scrs.queryClass(-1, "", "", "", "CS", "Lecture", "Daniel Mack").size());       
       
//
        
    }
    @Test    
    public void test_queryStudentPersonalData(){
      //List<ArrayList<String>> queryStudentPersonalData(Token token, int studentID)
        assertEquals(0,scrs.queryStudentPersonalData(empty, 1).size());
        assertEquals(1,scrs.queryStudentPersonalData(stu1, 1).size());
        assertEquals(0,scrs.queryStudentPersonalData(stu1, 2).size());
        assertEquals(0,scrs.queryStudentPersonalData(adm, 1).size());
        assertEquals(0,scrs.queryStudentPersonalData(unde, 1).size());
    }
    @Test
    public void test_queryStudentRegistrationHistory(){
        //List<ArrayList<String>> queryStudentRegistrationHistory(Token token, int studentID)
        assertEquals(0,scrs.queryStudentRegistrationHistory(empty, 1).size());
        assertEquals(2,scrs.queryStudentRegistrationHistory(stu3, 2).size());
        assertEquals(0,scrs.queryStudentRegistrationHistory(stu3, 4).size());
        assertEquals(0,scrs.queryStudentRegistrationHistory(adm, 2).size());
        assertEquals(0,scrs.queryStudentRegistrationHistory(unde, 2).size());
        
    }
    @Test
    public void test_queryAdminPersonalData(){
        assertEquals(1, scrs.queryAdminPersonalData(adm).size());
        assertEquals(0, scrs.queryAdminPersonalData(stu1).size());
        assertEquals(0, scrs.queryAdminPersonalData(empty).size());
        assertEquals(0, scrs.queryAdminPersonalData(unde).size());
    }
   @Test
    public void test_queryInstructor(){
        assertEquals(0, scrs.queryInstructor(nul, 0).size());
        assertEquals(0, scrs.queryInstructor(stu1, 0).size());
        assertEquals(0, scrs.queryInstructor(unde, 0).size());
        assertEquals(1, scrs.queryInstructor(adm,0).size());
        assertEquals(1, scrs.queryInstructor(adm,1).size());
        assertEquals(3, scrs.queryInstructor(adm,-1).size());
    }
    
    @Test
    public void test_studentAddClass(){
        assertFalse(scrs.studentAddClass(nul, 0, "A-F", "Fall2015"));
        assertFalse(scrs.studentAddClass(adm, 0, "A-F", "Fall2015"));
        assertFalse(scrs.studentAddClass(unde, 0, "A-F", "Fall2015"));
        assertTrue(scrs.studentAddClass(stu_3, 2, "A-F", "Fall2015"));
        assertFalse(scrs.studentAddClass(stu2, 0, "A-F", "Fall2015"));
        assertFalse(scrs.studentAddClass(stu_will_pass_credit, 5, "A-F", "Fall2015"));
    }
    @Test
    public void test_studentDropClass(){
        assertFalse(scrs.studentDropClass(nul, 0));
        assertFalse(scrs.studentDropClass(adm, 0));
        assertFalse(scrs.studentDropClass(unde, 0));
        assertFalse(scrs.studentDropClass(stu1, 0));
        assertTrue(scrs.studentDropClass(stu4, 1));
        assertFalse(scrs.studentDropClass(stu2, 0));
    }
    @Test
    public void test_studentEditClass(){
        assertFalse(scrs.studentEditClass(nul, 0, "A-F", "Fall2015"));
        assertFalse(scrs.studentEditClass(adm, 0, "A-F", "Fall2015"));
        assertFalse(scrs.studentEditClass(unde, 0, "A-F", "Fall2015"));
        assertFalse(scrs.studentEditClass(stu1, 0, "A-F", "Fall2015"));
        assertFalse(scrs.studentEditClass(stu2, 4, "A-F", "Fall2015"));
        assertFalse(scrs.studentEditClass(stu4time, 4, "A-F", "Fall2015"));
        assertTrue(scrs.studentEditClass(stu_3, 0, "A-F", "Fall2015"));
        
    }
    
  @Test
    public void test_adminAddClass(){
        //Token token, int courseID, String courseName, int courseCredits, int courseCapacity,
        //String term, int instructorID, String firstDay, String lastDay, String classBeginTime, String classEndTime,
        //String weekDays, String location, String type, String prerequisite, String description, String department

        assertFalse(scrs.adminAddClass(nul, 0, "", 4, 30, "", 3, "", "", "", "", "", "", "", "", "", ""));
        assertFalse(scrs.adminAddClass(stu1, 0, "", 4, 30, "", 3, "", "", "", "", "", "", "", "", "", ""));
        assertTrue(scrs.adminAddClass(adm, 3, "Operating Systems", 2, 30, "Fall 2013", 2, "09/08/2012", "12/08/2012", "09:30", "10:45", 
                "Tu Th", "ME 308", "Lecture", "no", "good course", "CS"));
        assertFalse(scrs.adminAddClass(adm, 1, "Operating Systems", 2, 30, "Fall 2013", 2, "09/08/2012", "12/08/2012", "09:30", "10:45", 
                "Tu Th", "ME 308", "Lecture", "no", "good course", "CS"));
        
    }
    
    @Test
    public void test_adminDeleteClass() {
        assertFalse(scrs.adminDeleteClass(nul, 0));
        assertFalse(scrs.adminDeleteClass(stu1, 0));
        
        assertTrue(scrs.adminDeleteClass(adm, 0));
        
        assertFalse(scrs.adminDeleteClass(adm, 9));
    }
    
    @Test
    public void test_adminEditClass() {
//        Token token, int courseID, String courseName, int courseCredits, int instructorID,
//        String firstDay, String lastDay, String classBeginTime, String classEndTime, String weekDays,
//        String location, String type, String prerequisite, String description, String department
        assertFalse(scrs.adminEditClass(nul, 0, "", 2, 2, "", "", "", "", "", "", "", "", "", ""));
        assertFalse(scrs.adminEditClass(stu1, 0, "", 2, 2, "", "", "", "", "", "", "", "", "", ""));
        assertTrue(scrs.adminEditClass(adm, 0, "", -1, -1, "", "", "", "", "", "", "", "", "good course", ""));
        assertFalse(scrs.adminEditClass(adm, 0, "", -1, -1, "", "", "", "", "", "", "", "", "good course", "CS"));
        assertTrue(scrs.adminEditClass(adm, 6, "", -1, -1, "09/01/2015", "12/01/2015", "", "", "", "", "", "", "good course", "CS"));
        assertTrue(scrs.adminEditClass(adm, 6, "operating systems", 4, 2, "09/01/2015", "12/01/2015", "09:30", "10:45", "Tu", "ME 209", "Lecture", "no", "haha", "CS"));
        
    }
    @Test
    public void test_adminDropStudentRegisteredClass(){
        
        assertTrue(scrs.adminDropStudentRegisteredClass(adm, 3, 0));
        assertFalse(scrs.adminDropStudentRegisteredClass(adm, 2, 5));
        assertFalse(scrs.adminDropStudentRegisteredClass(adm, 10, 10));
        assertFalse(scrs.adminDropStudentRegisteredClass(stu1, 0, 0));
        assertFalse(scrs.adminDropStudentRegisteredClass(stu2, 0, 0));
        assertFalse(scrs.adminDropStudentRegisteredClass(unde, 0, 0));
        assertFalse(scrs.adminDropStudentRegisteredClass(nul, 0, 0));
    }
    
    @Test
    public void test_adminEditStudentRegisteredClass(){
        assertTrue(scrs.adminEditStudentRegisteredClass(adm, 3, 0, "A-F", "Fall2015"));
        assertTrue(scrs.adminEditStudentRegisteredClass(adm, 3, 0, "a", "Fall2015"));
        assertTrue(scrs.adminEditStudentRegisteredClass(adm, 10, 10, "a", "Fall2015"));
        
        
        assertFalse(scrs.adminEditStudentRegisteredClass(stu1,  0, 0, "", ""));
        assertFalse(scrs.adminEditStudentRegisteredClass(unde,  0, 0, "", ""));
        assertFalse(scrs.adminEditStudentRegisteredClass(nul, 0, 0, "", ""));
    }
    
    @Test
    public void test_adminAddStudentToClass(){
        assertTrue(scrs.adminAddStudentToClass(adm, 0, 9, "A-F", "Fall2013"));
        assertFalse(scrs.adminAddStudentToClass(adm, 100, 1000, "", ""));
        assertTrue(scrs.adminAddStudentToClass(adm, 1, 2, "A-F", "Fall2015"));
        
        assertFalse(scrs.adminAddStudentToClass(stu1, 0, 0, "", ""));
        assertFalse(scrs.adminAddStudentToClass(stu1, 2, 0, "", "Fall2015"));
        assertFalse(scrs.adminAddStudentToClass(unde, 0, 0, "", ""));
        assertFalse(scrs.adminAddStudentToClass(nul, 0, 0, "", "")); 
    }
    

}
