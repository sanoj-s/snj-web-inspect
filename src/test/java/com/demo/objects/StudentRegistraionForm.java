package com.demo.objects;

public class StudentRegistraionForm {
    public static final String enterEmail = "//*[@placeholder='Enter Email' and @name='email']";

    public static final String enterPassword = "//*[@type='password' and @placeholder='Enter Password']";
    public static final String retypePassword = "//*[@type='password' and @placeholder='Retype Password']";
    public static final String firstname = "//*[@name='firstname' and @placeholder='Firstname']";
    public static final String middlename = "//*[@name='middlename' and @placeholder='Middlename']";
    public static final String lastname = "//*[@name='lastname' and @placeholder='Lastname']";
    public static final String male = "//*[@type='radio' and @value='Male']";
    public static final String female = "//*[@type='radio' and @value='Female']";
    public static final String other = "//*[@type='radio' and @value='Other']";
    public static final String countrycode = "//*[@name='country code' and @placeholder='Country Code']";
    public static final String phone = "//*[@name='phone' and @placeholder='phone no.']";
    public static final String currentAddress = "//*[@cols='80'and @rows='5'and @placeholder='Current Address']";
    public static final String objectName1 = "//*[@type='submit']";
    public static final String course = "//select[@name='course']";}
