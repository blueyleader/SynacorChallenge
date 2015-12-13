package com.blueyleader.synacorchallenge;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Stack;

public class SynacorChallange {

	public final int REGSTART =32768;
	public final int REGEND =32775;
	public static void main(String[] args) throws IOException {
		try{
		new SynacorChallange().run();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void run() throws Exception{
		String fileName = "challenge.bin";
		StringBuilder temp=  new StringBuilder();
		Path path = Paths.get(fileName);
		byte[] bytes = Files.readAllBytes(path);
		int[] data = new int[bytes.length/2];
		int[] regs= new int[8];	
		Scanner scan = new Scanner(System.in);
		String input = "",binary;
		char c;
		//combine the 2 bytes for easy use
		Stack<Integer> stack = new Stack<Integer>();
		for(int x=0;x<bytes.length-1;x+=2){
			data[x/2] = ((bytes[x+1] & 0xFF) << 8) | (bytes[x] & 0xFF);
		}
		int v1,v2,v3;
		boolean reg;
		int x=0;
		while(x<data.length){
			//System.out.println(data[x]+" "+x);
		//for(int x=0;x<data.length;x++){
			if(x==1800){
				System.out.print("");
			}
			switch(data[x]){
			case 0:
				//halt: 0
				//stop execution and terminate the program
				//System.out.print("\n"+temp);
				System.exit(0);
				break;
				
			case 1:
				//set: 1 a b
				//set register <a> to the value of <b>
				x++;
				v1=data[x]-REGSTART;
				x++;
				v2=data[x];
				if(reg(v2)){
					v2=regs[v2-REGSTART];
				}
				regs[v1]=v2;
				break;
				
			case 2:
				//push: 2 a
				//push <a> onto the stack
				x++;
				v1=data[x];
				if(reg(v1)){
					v1=regs[v1-REGSTART];
				}
				stack.add(v1);
				break;	
				
			case 3:
				//pop: 3 a
				//remove the top element from the stack and write it into <a>; empty stack = error
				if(stack.isEmpty()){
					throw new Exception();
				}
				x++;
				v1=data[x]-REGSTART;
				regs[v1]=stack.pop();
				break;
				
			case 4:
				//eq: 4 a b c
				//set <a> to 1 if <b> is equal to <c>; set it to 0 otherwise	
				x++;
				v1=data[x]-REGSTART;
				x++;
				v2=data[x];
				if(reg(v2)){
					v2=regs[v2-REGSTART];
				}
				x++;
				v3=data[x];
				if(reg(v3)){
					v3=regs[v3-REGSTART];
				}
				
				if(v2==v3){
					regs[v1]=1;
				}
				else{
					regs[v1]=0;
				}
				
				break;
				
			case 5:
				//gt: 5 a b c
				//set <a> to 1 if <b> is greater than <c>; set it to 0 otherwise	
				x++;
				v1=data[x]-REGSTART;
				x++;
				v2=data[x];
				if(reg(v2)){
					v2=regs[v2-REGSTART];
				}
				x++;
				v3=data[x];
				if(reg(v3)){
					v3=regs[v3-REGSTART];
				}
				
				if(v2>v3){
					regs[v1]=1;
				}
				else{
					regs[v1]=0;
				}
				break;
				
			case 6:
				//jmp: 6 a
				//jump to <a>
				x++;
				v1=data[x];
				if(reg(v1)){
					v1=regs[v1-REGSTART];
				}
				x=v1;
				continue;
				
			case 7:
				//jt: 7 a b
				//if <a> is nonzero, jump to <b>
				x++;
				v1=data[x];
				if(reg(v1)){
					v1=regs[v1-REGSTART];
				}
				x++;
				v2=data[x];
				if(reg(v2)){
					v2=regs[v2-REGSTART];
				}
				if(v1!=0){
					x=v2;
					continue;
				}
				break;
				
			case 8:
				//jf: 8 a b
				//if <a> is zero, jump to <b>
				x++;
				v1=data[x];
				if(reg(v1)){
					v1=regs[v1-REGSTART];
				}
				x++;
				v2=data[x];
				if(reg(v2)){
					v2=regs[v2-REGSTART];
				}
				if(v1==0){
					x=v2;
					continue;
				}
				break;
				
			case 9:
				//add: 9 a b c
				//assign into <a> the sum of <b> and <c> (modulo 32768)	
				x++;
				v1=data[x]-REGSTART;
				x++;
				v2=data[x];
				if(reg(v2)){
					v2=regs[v2-REGSTART];
				}
				x++;
				v3=data[x];
				if(reg(v3)){
					v3=regs[v3-REGSTART];
				}
				regs[v1]=(v2+v3)%REGSTART;
				break;
				
			case 10:
				//mult: 10 a b c
				//store into <a> the product of <b> and <c> (modulo 32768)	
				x++;
				v1=data[x]-REGSTART;
				x++;
				v2=data[x];
				if(reg(v2)){
					v2=regs[v2-REGSTART];
				}
				x++;
				v3=data[x];
				if(reg(v3)){
					v3=regs[v3-REGSTART];
				}
				regs[v1]=(v2*v3)%REGSTART;
				break;
				
			case 11:
				//mod: 11 a b c
				//store into <a> the remainder of <b> divided by <c>	
				x++;
				v1=data[x]-REGSTART;
				x++;
				v2=data[x];
				if(reg(v2)){
					v2=regs[v2-REGSTART];
				}
				x++;
				v3=data[x];
				if(reg(v3)){
					v3=regs[v3-REGSTART];
				}
				regs[v1]=v2%v3;
				break;
				
			case 12:
				//and: 12 a b c
				//stores into <a> the bitwise and of <b> and <c>
				x++;
				v1=data[x]-REGSTART;
				x++;
				v2=data[x];
				if(reg(v2)){
					v2=regs[v2-REGSTART];
				}
				x++;
				v3=data[x];
				if(reg(v3)){
					v3=regs[v3-REGSTART];
				}
				regs[v1]=v2&v3;
				break;	
				
			case 13:
				//or: 13 a b c
				//stores into <a> the bitwise or of <b> and <c>	
				x++;
				v1=data[x]-REGSTART;
				x++;
				v2=data[x];
				if(reg(v2)){
					v2=regs[v2-REGSTART];
				}
				x++;
				v3=data[x];
				if(reg(v3)){
					v3=regs[v3-REGSTART];
				}
				regs[v1]=v2|v3;
				break;
				
			case 14:
				//not: 14 a b
				//stores 15-bit bitwise inverse of <b> in <a>
				x++;
				v1=data[x]-REGSTART;
				x++;
				v2=data[x];
				if(reg(v2)){
					v2=regs[v2-REGSTART];
				}
				binary=Integer.toBinaryString(v2);
				v3=binary.length();
				for(int z=15;z>v3;z--){
					binary="0"+binary;
				}
				for(int y=0;y<binary.length();y++){
					if(binary.charAt(y)=='0'){
						binary=binary.substring(0,y)+'1'+binary.substring(y+1);
					}
					else{
						binary=binary.substring(0,y)+'0'+binary.substring(y+1);
					}	
				}
				
				regs[v1]=Integer.parseInt(binary,2);
				//regs[v1]=~v2;
				break;
				
			case 15:
				//rmem: 15 a b
				//read memory at address <b> and write it to <a>
				x++;
				v1=data[x]-REGSTART;
				x++;
				v2=data[x];
				if(reg(v2)){
					v2=regs[v2-REGSTART];
				}
				//get data at address b
				v2=data[v2];
				
				if(reg(v2)){
					v2=regs[v2-REGSTART];
				}
				regs[v1]=v2;
				
				break;
				
			case 16:
				//wmem: 16 a b
				//write the value from <b> into memory at address <a>
				
				//a location
				x++;
				v1=data[x];
				if(reg(v1)){
					v1=regs[v1-REGSTART];
				}
				
				//b data
				x++;
				v2=data[x];
				if(reg(v2)){
					v2=regs[v2-REGSTART];
				}
				// b data -> a location
				data[v1]=v2;
				break;
				
			case 17:
				//call: 17 a
				//write the address of the next instruction to the stack and jump to <a>
				
				//a location
				x++;
				v1=data[x];
				if(reg(v1)){
					v1=regs[v1-REGSTART];
				}
				x++;
				stack.add(x);
				
				x=v1;
				continue;
				
			case 18:
				//ret: 18
				//remove the top element from the stack and jump to it; empty stack = halt
				
				if(stack.isEmpty()){
					System.exit(0);
				}
				
				v1=stack.pop();
				if(reg(v1)){
					v1=regs[v1-REGSTART];
				}
				x=v1;
				
				continue;	
				
			case 19:
				//out: 19 a
				//write the character represented by ascii code <a> to the terminal
				x++;
				v1=data[x];
				if(reg(v1)){
					v1=regs[v1-REGSTART];
				}
				System.out.print((char)v1);
				if(data[x]=='?')
					System.out.println("");
				break;
				
			case 20:
				//in: 20 a
				//read a character from the terminal and write its ascii code to <a>; it can be assumed that once input starts, it will continue until a newline is encountered; this means that you can safely read whole lines from the keyboard and trust that they will be fully read
				
				x++;
				v1=data[x]-REGSTART;
				if(input.equals(""))
					input=scan.nextLine()+'\n';

				c= input.charAt(0);
				input=input.substring(1);
				regs[v1]=c;
				break;
				
			case 21:
				//noop: 21
				//no operation
				break;
			}
			x++;
		}
		System.out.println("hit");
		
	}
	
	public boolean reg(int data){
		if(data>=REGSTART  && data<=REGEND)
			return true;
		
		return false;
	}
	

}
