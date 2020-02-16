package com.example.android.newsapp;

import org.junit.Before;
import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.*;

public class QueryUtilsTest {

    QueryUtils SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new QueryUtils();
    }

    @Test
    public void createUrl() {
        URL expectedUrl = null;

        try {
            expectedUrl = new URL("http://www.google.ch");
        } catch (Exception e) {
            fail();
        }
        assertEquals(expectedUrl, SUT.createUrl("http://www.google.ch"));
    }
}