package edu.umn.csci5801.database;

@SuppressWarnings("serial")
public class TargetNotFound extends Exception{
    TargetNotFound(String message){
        super(message);
    }

}
