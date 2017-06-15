package com.leap;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Scanner;

public class Files {
	
	public static DataVector ReadVector()
	{
	
		DataVector v = new DataVector();
		try {
		
			Scanner s = new Scanner(new File("vector.txt"));
			while(s.hasNextLine())
				v.addCoordinate(s.nextDouble());
		s.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return v;
	
	}

}
