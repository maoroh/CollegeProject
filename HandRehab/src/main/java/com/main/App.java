package com.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.leap.AnglesVector;
import com.leap.BoneType;
import com.leap.Files;
import com.leap.AnalyzeData;
import com.leap.SampleBuilder;
import com.leap.test;
import com.leapmotion.leap.Vector;
import com.tools.JAXBTools;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    
    	test Test = new test();
    	JAXBTools.saveSampleSetXML(Test);
    }
}
