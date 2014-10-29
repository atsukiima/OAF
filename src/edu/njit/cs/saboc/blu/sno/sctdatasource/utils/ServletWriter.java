package edu.njit.cs.saboc.blu.sno.sctdatasource.utils;

// ==================================
// AUTHOR: Scott McPherson
// EMAIL:  scottm@mochamail.com
// DATE:   October 1, 1999
// Copyright (C) 1999 Scott McPherson
// ==================================

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;

/**
 * This class provides a simple method of posting multiple
 * Serialized objects to a Java servlet and getting objects
 * in return. This code was inspired by code samples from
 * the book 'Java Servlet Programming' by Jason Hunter and
 * William Crawford (O'Reilly & Associates. 1998).
 */
public class ServletWriter {
    static public ObjectInputStream postObjects(URL servlet, Serializable objs[]) throws Exception {
        URLConnection con = servlet.openConnection();//opens the connection
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false);
        con.setDefaultUseCaches(false);
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        // Write the arguments as post data
        ObjectOutputStream out = new ObjectOutputStream(con.getOutputStream());

        for(Object obj : objs) {
            out.writeObject(obj);
        }

        out.flush();
        out.close();
        ObjectInputStream o = null;

        try {
            o = new ObjectInputStream(con.getInputStream());
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return o;
    }
}
