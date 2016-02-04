package edu.umn.csci5801.database;

@SuppressWarnings("serial")
public class InvalidOperation extends Exception{
    InvalidOperation(String message){
        super(message);
    }

}
