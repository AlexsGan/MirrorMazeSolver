/*
_________________________________________________________________________________________________________________________
_________________________________________________________________________________________________________________________||
“MirrorMaze” Program
Alex Gan
4/01/2018
Java, Version: Neon.3 Release (4.6.3)
_________________________________________________________________________________________________________________________
_________________________________________________________________________________________________________________________||
Problem Definition:
			Given a 10x10 square grid, width some squares blocked and some occupied by mirrors, The task is to visit 
	all of the vacant cells of the game field by drawing a closed line which does not cross itself. 
	The rules are as follows:
	- You may not enter a blocked cell, which is filled with a solid square;
	- You may enter the square with a thin mirror (shown with a slash or a back slash) twice but the path cannot cross;
	- You may enter the half solid mirror (shown as a painted triangle) only once through a non-painted part of it.

Input:	
			In the mirrorMazeInput.txt file, enter a maze using the following syntax:
   			- empty cell: 0	- blocked cell: B
   			- thin mirror sloped:   - negatively: n   - positively: p
   			- half solid mirror facing:   - NorthEast: 1   - NW: 2   - SouthWest: 3   - SE: 4 
Output:
			The program outputs the solved maze in an applet with beautiful contemporary graphics.
Process:
			Use a recursion method along with depth first search to find a possible solution to the maze.
_________________________________________________________________________________________________________________________
_________________________________________________________________________________________________________________________||
List of Identifiers:

===Global:===

---Type int:---
	SIZE		- a constant, indicate the length of the maze (dimension would be SIZE by SIZE)
	totalCount	- indicates how many steps the path needs to take to complete 
	initY		- the y coordinate of where the calculation starts
	initX		- the x coordinate of where the calculation starts
	origX		- the x coordinate of starting point chosen by user
	origY		- the y coordinate of starting point chosen by user
	
---Other:---
	maze[][] <type char>	- the maze itself, with a character representing each cell of the maze
	complete <type boolean> - variable that signals when the maze is solved
_________________________________________________________________________________________________________________________
_________________________________________________________________________________________________________________________||
*/


package mirrorMaze;

import java.io.*;				//allow access to the coding within the io library 
import javax.swing.*;			//import swing
import java.applet.*;			//import applet
import java.awt.*;				//import Abstract Window Toolkit

public class MirrorMaze extends Applet{//start of MirrorMaze class

	static int SIZE = 6; //dimension of maze
	static char maze[][] = new char[SIZE][SIZE];
	static int totalCount = SIZE * SIZE, initY = 0, initX = 0, origX, origY;
	static boolean complete;
	
	/**init method
	 * this method set the original size of the applet 
	 * to give an initial user friendly view
	 * 
	 * @return void
	 */
	public void init(){
		setSize(SIZE * 100 + 120, SIZE * 100 + 100); // set size of applet
	}//end init method
	
	/**paint method
	 * This procedural method which is called automatically calls 4 methods:
	 * 		1.intro 2.input 3.DFS 4.drawMaze
	 * and serves as the main method of the program
	 * 
	 * @param g-graphics object used to call methods that draws the entire program
	 * 
	 * @return void
	 */
	public void paint(Graphics g){
		intro();//call intro method
		try {
			input();//call input method
		} catch (IOException e) {
		}
		DFS(initY, initX, 0);//call DFS method
		drawMaze(g);//call drawMaze method
	}//end paint (main) method
	
	/**drawMaze method
	 * this procedural method draws the solved maze on the applet
	 * and calls the drawLine function which helps draw 
	 * the path of the solution path 
	 * 
	 * @param g
	 * @return void
	 */
	public void drawMaze(Graphics g){
		System.out.println("solved");
		
		g.drawLine(0, 0, 100*SIZE, 0);
		g.drawLine(0, 1, 100*SIZE, 1);
		g.drawLine(0, 0, 0, 100*SIZE);
		g.drawLine(1, 0, 1, 100*SIZE);
		
		for (byte y = 0; y < SIZE; y++) {
			g.setColor(Color.black);
			g.drawLine(0, (y + 1) * 100, 100*SIZE, (y + 1) * 100);// draws horizontal line of grid
			g.drawLine(0, (y + 1) * 100 + 1, 100*SIZE, (y + 1) * 100 + 1);//thickens line
			
			g.drawLine((y + 1) * 100, 0, (y + 1) * 100, 100*SIZE);// draws vertical line of grid
			g.drawLine((y + 1) * 100 + 1, 0, (y + 1) * 100 + 1, 100*SIZE);
			
			g.drawOval(origX*100+20, origY*100+20, 60, 60);//draw a circle indicates where the user started
			g.drawOval(origX*100+21, origY*100+21, 58, 58);
			
			for (byte x = 0; x < SIZE; x++) {				
				g.setColor(Color.red);
				if (maze[y][x]=='B')
					g.fillRect(x*100, y*100, 100, 100);//draw a filled square for blocked cell
				else if (maze[y][x]=='n'){
					g.drawLine(x*100, y*100, x*100+100, y*100+100);//draw the thin mirror
					
					g.setColor(Color.blue);
					drawLine('>',x,y,g);//draw the path around the thin mirror by calling the drawLine method
					drawLine('<',x,y,g);
					drawLine('^',x,y,g);
					drawLine('v',x,y,g);
					g.setColor(Color.red);
				}
				else if (maze[y][x]=='p'){
					g.drawLine(x*100, (y*100)+100, (x*100)+100, y*100);
					
					g.setColor(Color.blue);
					drawLine('>',x,y,g);
					drawLine('<',x,y,g);
					drawLine('^',x,y,g);
					drawLine('v',x,y,g);
					g.setColor(Color.red);
				}
				else if (maze[y][x]=='4'){
					int xPoint[] = {x*100,x*100+100,x*100}, yPoint[] = {y*100,y*100,y*100+100};
					g.fillPolygon(xPoint, yPoint, 3);//draws a filled triangle facing SouthEast
					
					g.setColor(Color.blue);
					drawLine('v',x,y,g);//draw the path around the half solid mirror
					drawLine('>',x,y,g);
					g.setColor(Color.red);
				}
				else if (maze[y][x]=='3'){
					int xPoint[] = {x*100,x*100+100,x*100+100}, yPoint[] = {y*100,y*100,y*100+100};
					g.fillPolygon(xPoint, yPoint, 3);//draws a filled triangle facing SW
					
					g.setColor(Color.blue);
					drawLine('v',x,y,g);					
					drawLine('<',x,y,g);
					g.setColor(Color.red);
				}
				else if (maze[y][x]=='2'){
					int xPoint[] = {x*100,x*100+100,x*100+100}, yPoint[] = {y*100+100,y*100+100,y*100};
					g.fillPolygon(xPoint, yPoint, 3);//draws a filled triangle facing NW
					
					g.setColor(Color.blue);
					drawLine('^',x,y,g);					
					drawLine('<',x,y,g);
					g.setColor(Color.red);
				}
				else if (maze[y][x]=='1'){
					int xPoint[] = {x*100,x*100,x*100+100}, yPoint[] = {y*100,y*100+100,y*100+100};
					g.fillPolygon(xPoint, yPoint, 3);//draws a filled triangle facing NE
					
					g.setColor(Color.blue);
					drawLine('^',x,y,g);					
					drawLine('>',x,y,g);
					g.setColor(Color.red);
				}		
				g.setColor(Color.blue);
				
				if (maze[y][x]=='>')
					drawLine('>',x,y,g);//draws path right
				
				else if (maze[y][x]=='<')
					drawLine('<',x,y,g);//draws path left
				
				else if (maze[y][x]=='^')
					drawLine('^',x,y,g);//draws path up
				
				else if (maze[y][x]=='v')
					drawLine('v',x,y,g);//draws path down
			}//end inner for loop
		}//end outer for loop
	}//end drawMaze method

	/**drawLine method
	 * this procedural method draws line segments which make up 
	 * the maze solution path
	 * 
	 * @param direction - indicates the direction to be drawn
	 * @param x - x coordinate of the cell
	 * @param y - y coordinate of the cell
	 * @param g 
	 * 
	 * @return void
	 */
	public void drawLine(char direction, int x, int y, Graphics g){
		if (direction == '>')
			g.drawLine(100*x+50, 100*y+50, 100*x+50+100, 100*y+50);//draws a line connecting the centers of two cells
		else if (direction == '<')
			g.drawLine(100*x-50, 100*y+50, 100*x+50, 100*y+50);
		else if (direction == '^')
			g.drawLine(100*x+50, 100*y+50, 100*x+50, (100*y+50)-100);
		else if (direction == 'v')
			g.drawLine(100*x+50, 100*y+50, 100*x+50, (100*y+50)+100);
	}//end drawLine method
	
	/**DFS method
	 * this procedural method checks if the direction of a path is valid
	 * and chooses the paths to go using recursion. This method 
	 * also calls 4 other method which help decide which direction
	 * the path should go to.
	 * 
	 * @param y - y coordinate of the current cell
	 * @param x - x coordinate of the current cell
	 * @param count - current count of how many steps taken
	 * 
	 * @return void
	 */
	public static void DFS(int y, int x, int count) {
		boolean up = false, down = false, left = false, right = false;
		
		if (y == initY && x == initX && count == totalCount) { // base case, when path completed and all cells have being filled
			complete = true;	//set the complete variable to true
			return;
		}
		
		if (count != totalCount - 1) { // if path did not complete 
			if ((y + 1) < SIZE){	//if a path to the right is not outside of grid 
				if (maze[y + 1][x] == '0')//cell to the right is empty
					down = true;
				else if (maze[y + 1][x] == '2' && maze[y + 1][x - 1] == '0' || maze[y + 1][x] == 'p' && maze[y + 1][x - 1] == '0')
					down = true;											//half mirror or positively sloped slash mirror
				else if (maze[y + 1][x] == '1' && maze[y + 1][x + 1] == '0' || maze[y + 1][x] == 'n' && maze[y + 1][x + 1] == '0')
					down = true;											//half mirror or negatively sloped slash mirror
			}
			
			if ((y - 1) >= 0){
				if (maze[y - 1][x] == '0')
					up = true;
				else if (maze[y - 1][x] == '3' && maze[y - 1][x - 1] == '0' || maze[y - 1][x] == 'n' && maze[y - 1][x - 1] == '0')
					up = true;
				else if (maze[y - 1][x] == '4' && maze[y - 1][x + 1] == '0' || maze[y - 1][x] == 'p' && maze[y - 1][x + 1] == '0')
					up = true;												
			}
			
			if ((x - 1) >= 0){
				if (maze[y][x - 1] == '0')
					left = true;
				else if (maze[y][x - 1] == '1' && maze[y - 1][x - 1] == '0' || maze[y][x - 1] == 'n' && maze[y - 1][x - 1] == '0')
					left = true;
				else if (maze[y][x - 1] == '4' && maze[y + 1][x - 1] == '0' || maze[y][x - 1] == 'p' && maze[y + 1][x - 1] == '0')
					left = true;
			}
			
			if ((x + 1) < SIZE) {
				if (maze[y][x + 1] == '0')
					right = true;
				else if (maze[y][x + 1] == '2' && maze[y - 1][x + 1] == '0' || maze[y][x + 1] == 'p' && maze[y - 1][x + 1] == '0')
					right = true;
				else if (maze[y][x + 1] == '3' && maze[y + 1][x + 1] == '0' || maze[y][x + 1] == 'n' && maze[y + 1][x + 1] == '0')
					right = true;
			}
		}//end if statement for if path did not complete
		else {//else statement for when the path goes back to beginning
			if (y - 1 == initY && x == initX)
				up = true;
			else if (y + 1 == initY && x == initX)
				down = true;
			else if (y == initY && x - 1 == initX)
				left = true;
			else if (y == initY && x + 1== initX)
				right = true;
		}//end else statement 

		if (right && !complete)		
			solveRight(y, x, count);//calls the solveRight method
		
		if (down && !complete) 
			solveDown(y, x, count);		
		
		if (up && !complete) 
			solveUp(y, x, count);	
		
		if (left && !complete) 
			solveLeft(y, x, count);

		if (complete)//if the maze is solved then return
			return;
		else {//if this step is reached then method backtracks as the current path does not work
			maze[y][x] = '0';//set the current cell to empty cell
			return;
		}
	}//end DFS method
	
	/**solveRight method
	 * this procedural method is called when the right direction is valid,
	 * and it determines the directions the path should take 
	 * under different conditions. It then calls the DFS method
	 * to continually solve the maze recursively.
	 * 
	 * @param y - y coordinate of the current cell
	 * @param x - x coordinate of the current cell
	 * @param count - current count of how many steps taken
	 * 
	 * @return void
	 */
	public static void solveRight(int y, int x, int count) {
		if (maze[y][x+1] == '2' && maze[y - 1][x + 1] == '0' || maze[y][x + 1] == 'p' && maze[y - 1][x + 1] == '0'){
			maze[y][x] = '>'; // marks path right 
			maze[y - 1][x + 1] = '^'; // draws path up
			DFS(y - 1, x + 1, count + 2);//calls the DFS method, which will continue the recursion
		}
		else if (maze[y][x+1] == '3' && maze[y + 1][x + 1] == '0' || maze[y][x + 1] == 'n' && maze[y + 1][x + 1] == '0'){
			maze[y][x] = '>'; // marks path right 
			maze[y + 1][x + 1] = 'v'; // draws path down 
			DFS(y + 1, x + 1, count + 2);
		}
		else{
			maze[y][x] = '>'; // marks path right
			DFS(y, x + 1, count + 1);
		}
	}//end solveRight method
	
	/**solveDown method
	 * this procedural method is called when the down direction is valid,
	 * and it determines the directions the path should take 
	 * under different conditions. It then calls the DFS method
	 * to continually solve the maze recursively.
	 * 
	 * @param y - y coordinate of the current cell
	 * @param x - x coordinate of the current cell
	 * @param count - current count of how many steps taken
	 * 
	 * @return void
	 */
	public static void solveDown(int y, int x, int count) {
		if (maze[y + 1][x] == '2' && maze[y + 1][x - 1] == '0' || maze[y + 1][x] == 'p' && maze[y + 1][x - 1] == '0'){
			maze[y][x] = 'v';  // marks path
			maze[y + 1][x - 1] = '<';  // marks path
			DFS(y + 1, x - 1, count + 2);//calls the DFS method, which will continue the recursion
		}
		else if (maze[y + 1][x] == '1' && maze[y + 1][x + 1] == '0' || maze[y + 1][x] == 'n' && maze[y + 1][x + 1] == '0'){
			maze[y][x] = 'v';  // marks path
			maze[y + 1][x + 1] = '>';  // marks path
			DFS(y + 1, x + 1, count + 2);
		}
		else {
			maze[y][x] = 'v';  // marks path
			DFS(y + 1, x, count + 1);
		}
	}//end solveDown method

	/**solveUp method
	 * this procedural method is called when the up direction is valid,
	 * and it determines the directions the path should take 
	 * under different conditions. It then calls the DFS method
	 * to continually solve the maze recursively.
	 * 
	 * @param y - y coordinate of the current cell
	 * @param x - x coordinate of the current cell
	 * @param count - current count of how many steps taken
	 * 
	 * @return void
	 */
	public static void solveUp(int y, int x, int count) {
		if (maze[y - 1][x] == '3' && maze[y - 1][x - 1] == '0' || maze[y - 1][x] == 'n' && maze[y - 1][x - 1] == '0') {
			maze[y][x] = '^';  // marks path
			maze[y - 1][x - 1] = '<';  // marks path
			DFS(y - 1, x - 1, count + 2);//calls the DFS method, which will continue the recursion
		}
		else if (maze[y-1][x] == '4' && maze[y - 1][x + 1] == '0' || maze[y - 1][x] == 'p' && maze[y - 1][x + 1] == '0') {
			maze[y][x] = '^';  // marks path
			maze[y - 1][x + 1] = '>';  // marks path
			DFS(y - 1, x + 1, count + 2);
		} 
		else {
			maze[y][x] = '^';  // marks path
			DFS(y - 1, x, count + 1);
		}
	}//end solveUp method

	/**solveLeft method
	 * this procedural method is called when the left direction is valid,
	 * and it determines the directions the path should take 
	 * under different conditions. It then calls the DFS method
	 * to continually solve the maze recursively.
	 * 
	 * @param y - y coordinate of the current cell
	 * @param x - x coordinate of the current cell
	 * @param count - current count of how many steps taken
	 * 
	 * @return void
	 */
	public static void solveLeft(int y, int x, int count) {
		if (maze[y][x - 1] == '1' && maze[y - 1][x - 1] == '0' || maze[y][x - 1] == 'n' && maze[y - 1][x - 1] == '0'){
			maze[y][x] = '<'; // marks path
			maze[y - 1][x - 1] = '^'; // marks path
			DFS(y - 1, x - 1, count + 2);//calls the DFS method, which will continue the recursion
		}
		else if (maze[y][x - 1] == '4' && maze[y + 1][x - 1] == '0' || maze[y][x - 1] == 'p' && maze[y + 1][x - 1] == '0'){
			maze[y][x] = '<'; // marks path
			maze[y + 1][x - 1] = 'v'; // marks path
			DFS(y + 1, x - 1, count + 2);
		}
		else{
			maze[y][x] = '<'; // marks path
			DFS(y, x - 1, count + 1);
		}
	}//end solveLeft method
	
	/**input method
	 * This method reads the maze from the text file
	 * and copy them to the program's arrays. It also 
	 * changes the totalCount depending on the maze inputed
	 * 
	 * @throws IOException
	 * 
	 * @return void
	 */
	public static void input() throws IOException{
		FileReader fr = new FileReader("assignment1/mirrorMazeInput.txt");	//declare & instantiate FileReader
		BufferedReader br = new BufferedReader(fr);							//declare & instantiate BufferedReader

		String line = null;			//'line' <type String> as lines read from text file
		int index = 0;				//'index' <type int> variable to be incremented

		while ((line = br.readLine()) != null && index < SIZE) {
			for (byte i = 0; i < SIZE; i++) {
				maze[index][i] = line.charAt(i);//copy the character from the file to the maze array
				if (maze[index][i] == 'B')
					totalCount--;//if there is a blocked cell
				else if (maze[index][i] == 'n' || maze[index][i] == 'p')
					totalCount++;//since a path will come in contact with thin mirror twice
			}//end for loop
			index++;
		} // end while loop
		br.close();
		
		startingPoint();//calls startingPoint method
		System.out.println("\ncalculating...\n");
	}//end input method

	/**startingPoint method
	 * This method generates a JOptionsPane which lets 
	 * the user to enter a valid starting coordinate.
	 * 
	 * @return void
	 */
	public static void startingPoint() {
		do {//do while loop loops until user chooses to leave or enter valid starting coordinates
			JPanel popUp = new JPanel();			//custom make JOptionPanel
			JTextField xCord = new JTextField(3);
			JTextField yCord = new JTextField(3);
			popUp.add(new JLabel("x:"));
			popUp.add(xCord);
			popUp.add(new JLabel("y:"));
			popUp.add(yCord);

			try {
				int clicked = JOptionPane.showConfirmDialog(null, popUp, "Enter Starting Coordinates",
						JOptionPane.OK_CANCEL_OPTION);//gives user a JOptionPanel to enter coordinates
				if (clicked == JOptionPane.OK_OPTION) {
					origX = Integer.parseInt(xCord.getText());//set origX to user's input on x
					origY = Integer.parseInt(yCord.getText());
				} else
					System.exit(0);//terminates program
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Please enter a valid integer Coordinate");
				continue;
			}
			if (origX > SIZE || origX < 0 || origY > SIZE || origY < 0 || maze[origY][origX] != '0')//if user entered invalid starting coordinates
				JOptionPane.showMessageDialog(null, "Please enter a valid integer Coordinate");
			else
				break;//breaks out of loop when valid starting coordinates have being entered
		} while (true);//ends do while loop
	}//end startingPoint method

	/**intro method
	 * This method describes the problem that the program will solve, and
	 * also tells the user how they can input from a file
	 * 
	 */
	public static void intro() {
		System.out.println("			Mirror Maze Problem Program\n				by Alex Gan\n");
		System.out.println("	Given a "+SIZE+"x"+SIZE+" square grid, with some sqaures blocked and some occupied by mirrors.\n"
				+ "This program will visit all the vacant cells of the maze by drawing a closed line which \n"
				+ "does not cross itself by solving the pathway recursively. The starting point is to be \n"
				+ "chosen by the user.\n\n"
				+ "The rules are as follows:\n\n"
				+ "    - You may not enter a blocked cell, which is filled with a solid square;\n"
				+ "    - You may enter the square with a thin mirror(a slash or a backslash) twice \n"
				+ "	  but the path cannot cross;\n"
				+ "    - You may enter the half solid mirror(painted triangle) only once through a \n"
				+ "	  non-painted part of it.\n\n"
				+ "\nEnter the maze from the mirrorMazeInput.txt file using the follwing syntax:\n\n"
				+ "   - empty cell: 0	- blocked cell: B\n"
				+ "   - thin mirror sloped:   - negatively: n   - positively: p\n"
				+ "   - half solid mirror facing:   - NorthEast: 1   - NW: 2   - SouthWest: 3   - SE: 4\n");
	}//end intro method

}//end MirrorMaze class