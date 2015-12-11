package com.blueyleader.synacorchallenge;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SynacorChallange {

	public static void main(String[] args) throws IOException {
		String fileName = "challenge.bin";
		Path path = Paths.get(fileName);
		byte[] bytes = Files.readAllBytes(path);

		for(int x=0;x<bytes.length;x+=2){
			switch(bytes[x]){
			case 0:
				System.exit(0);
				break;
			case 1:
				
				break;
			case 2:
				
				break;	
			case 3:
					
				break;
			case 4:
					
				break;
			case 5:
					
				break;
			case 6:
				
				break;
			case 7:
				
				break;
			case 8:
				
				break;	
			case 9:
					
				break;
			case 10:
					
				break;
			case 11:
					
				break;
			case 12:
				
				break;	
			case 13:
					
				break;
			case 14:
					
				break;
			case 15:
					
				break;
			case 16:
				
				break;
			case 17:
				
				break;
			case 18:
				
				break;	
			case 19:
				x+=2;
				System.out.print((char)bytes[x]);
				break;
			case 20:
					
				break;
			case 21:	
				break;
			}
		}
		
	}

}
