package com.novallc.foothillappmobile.activity.ListViewAssets;

import android.support.annotation.NonNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Contact_Model {

    public static ArrayList<Contact_Model> CONTACTS = new ArrayList<>();

    //Hard code teacher directory size for consistency
    public static Contact_Model[] CONTACTS_MODEL = CONTACTS.toArray(new Contact_Model[CONTACTS.size()]);

    //The fields associated to the person
    //TODO PHONE is a constant for now, integrate 'remind.com' number or alt
    private final String mName, mEmail, mClass_location, mDivision, mDescription, mTag0, mTag1, mfp_Location;

    Contact_Model(String name, String email, String class_location, String division, String description, String tag0, String tag1, String fp_location) {
        this.mName=name; this.mEmail=email; this.mClass_location=class_location; this.mDivision=division; this.mDescription=description; this.mTag0=tag0; this.mTag1=tag1; this.mfp_Location=fp_location;
    }

    //Add another id field for mName and mEmail uniqueness
    public int getId() {
        return mName.hashCode();
        //+ mEmail.hashCode(); //for more uniqueness
    }

    //Add another id field for mName and mEmail uniqueness
    public int getId_tagPARAMS0() {
        return mTag0.hashCode();
        //+ mEmail.hashCode(); //for more uniqueness
    }

    //Add another id field for mName and mEmail uniqueness
    public int getId_tagPARAMS1() {
        return mTag1.hashCode();
        //+ mEmail.hashCode(); //for more uniqueness
    }

    /*
      getItem method for name id
     */
    public static Contact_Model getItem(int id) {
        for (Contact_Model item : CONTACTS) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    /*
        getItem method for tag id
     */
    public static Contact_Model getItem_tagPARAMS0(int id) {
        for (Contact_Model item : CONTACTS) {
            if (item.getId_tagPARAMS0() == id) {
                return item;
            }
        }
        return null;
    }

    /*
        getItem method for tag id
     */
    public static Contact_Model getItem_tagPARAMS1(int id) {
        for (Contact_Model item : CONTACTS) {
            if (item.getId_tagPARAMS1() == id) {
                return item;
            }
        }
        return null;
    }

    public static enum Field {
        NAME, EMAIL, CLASS, DIVISION, DESCRIPTION, TAG0TEXT, TAG1TEXT, FLOATLOC
    }
    public String get(Field f) {
        switch (f) {
            case CLASS: return mClass_location;
            case DIVISION: return mDivision;
            case EMAIL: return mEmail;
            case DESCRIPTION: return mDescription;
            case TAG0TEXT: return mTag0;
            case TAG1TEXT: return mTag1;
            case FLOATLOC: return mfp_Location;
            case NAME: default: return mName;
        }
    }

}
