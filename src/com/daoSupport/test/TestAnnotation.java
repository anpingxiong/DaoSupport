package com.daoSupport.test;

 
import com.daoSupport.annotation.EntityAnnotation;

public class TestAnnotation {
  public static void main(String args[]){
    A a = new A();
    
    System.out.println(a.getClass().getAnnotation(EntityAnnotation.class));
  }
}
