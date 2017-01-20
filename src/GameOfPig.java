import javax.swing.JOptionPane;

public class GameOfPig {
	//Global Variable Declarations
	static int P_turnScore = 0;
	static int P_totalScore = 0;
	static int C_turnScore = 0;
	static int C_totalScore = 0;
	
	static String scoreMessage = "";
	static String rollMessage = "";
	static String computerMessage = "";
	
	public static void main(String[] args){
		//Variables to control game conditions
		boolean pTurn = true;
		boolean cTurn = false;
		boolean quitGame = false;
		int option = 0;
		int startOption = 0;
		int status;
		int CompturnCount = 0;
		
		//Game rules and start prompt
		startOption = JOptionPane.showConfirmDialog(null, "Welcome to the game of Pig!\nThe rules of the game are:\n\t\t\u2022The first player to accumulate a score of 100 wins.\n"
				+ "\t\t\u2022You roll two dice, If both dice are ones, then your turn is over and your accumulated score is set to zero (ouch!).\n"
				+ "\t\t\u2022If one dice is one, then your turn is over and your turn score is set to zero.\n"
				+"\t\t\u2022If both dice match (doubles) then you must roll again.\n"
				+ "\t\t\u2022For any other dice combination, you just add the dice total to your turn score and you have the choice of rolling again.\n"
				+ "When your turn is over, either through your choice or you rolled a one, then your turn sum is added to your accumulated score.\n\n\n"
				+ "<html><b>Ready to play?</html></b>");
		if(startOption != JOptionPane.YES_OPTION)
			quitGame = true;
			
		//overall game loop, continue until game is complete
		gameLoop: while(P_totalScore < 100 && C_totalScore < 100 && quitGame == false){
			//Player Turn
			P_turnScore = 0;
			C_turnScore = 0;
			option = 0;
			//Player turn loop
			while(pTurn && option == JOptionPane.YES_OPTION){
				if(P_totalScore >= 100) break gameLoop;
				status = playerTurn();
				if(status == 0){
					option = JOptionPane.showConfirmDialog(null, rollMessage + "\n" +scoreMessage + "\nDo you want to roll again?");
					if(option == JOptionPane.YES_OPTION){
						continue;
					}
					else if(option != JOptionPane.YES_OPTION && option != JOptionPane.NO_OPTION){
						quitGame = true;
						break gameLoop;
					}	
					else{
						pTurn = false;
						cTurn = true;
						if(P_turnScore + P_totalScore >= 100){
							P_totalScore += P_turnScore;//---------------Fixing issue of non-updating total score
							break gameLoop;
						}
					}
				}	
				else if(status == -1){
					
					pTurn = false;
					cTurn = true;
				}
				else if(status == 0);
					continue;
					
			}
			//Update Player's Score
			P_totalScore += P_turnScore;
			
			computerMessage = "";//reset computer turn message
			//Computer turn loop
			CompturnCount = 0;//reset turn counter
			while(cTurn){
				
				if(C_totalScore >= 100) break gameLoop;
				
				status = computerTurn();
				CompturnCount++;
				if(status == 0){
					if(C_turnScore >= 15 || (C_turnScore+C_totalScore >= 100 || CompturnCount > 4)){
						pTurn = true;
						cTurn = false;
						int tempScore = C_turnScore+C_totalScore;
						computerMessage += "\n\n<html><b>BleepBloopBleep Gamblin' finna hold on this one</b></html>"
								+ "\n                                I got "+tempScore+" ;)";
					}
					else{
						continue;
					}
				}	
				else if(status == -1){
					pTurn = true;
					cTurn = false;
					
				}
				else if(status == 0);
					continue;
					
			}
			
			//Update Computer's Score
			C_totalScore += C_turnScore;
			JOptionPane.showMessageDialog(null, "Results of computer's turn\n" + computerMessage +"\n\nYour Turn!");
			
		}//end game loop
		//show win condition
		/*if(P_turnScore != 0)
			P_totalScore += P_turnScore;
		if(C_turnScore != 0)
			C_totalScore += C_turnScore; 
			*/
		if(P_totalScore > C_totalScore)
			JOptionPane.showMessageDialog(null, ("Congratulations!  You win. Final Score:\nPlayer: "+P_totalScore+"\nComputer: "+C_totalScore));
		else
			JOptionPane.showMessageDialog(null, ("Bad Luck!  You lose. Final Score:\nPlayer: "+P_totalScore+"\nComputer: "+C_totalScore));
		
	}
	//-----end main--------
	
	//Player's turn method: returns integer representing turn result
	// -1 means turn over
	// 1 means double, force another turn
	//0 means choice of another turn
	static int playerTurn(){
		int result = 0;
		int [] roll = rollDice();
		String[] rolls = numToString(roll);
		int d1 = roll[0];
		int d2 = roll[1];
		rollMessage = "You rolled: "+rolls[0]+" & "+rolls[1];
		//check roll
		if(d1 == 1 && d2 == 1){
			P_turnScore = 0;
			P_totalScore = 0;
			result = -1;
			printStanding(1);
			JOptionPane.showMessageDialog(null,rollMessage + "\n<html><b>OHHH NOOOO! You got two ONE's!</b></html>\n\n---Turn Score reset---\n---Total Score reset---\n\n"
					+ "                 0   0\n"
					+ "					          ______\n\n"
					+ "<html><b>   BleepBloopBleep LOL. . .</b></html>\n"
					+"<html><b>   BleepBloopBleep So unlikely</b></html>\n"
					+scoreMessage +"\nTime for the computer to take some rolls\n");
		}
		else if(d1 == 1 || d2 == 1){
			P_turnScore = 0;
			result = -1;
			printStanding(1);
			JOptionPane.showMessageDialog(null,rollMessage + "\n<html><b>Uh oh! Looks like you got a ONE!</b></html>\n\n---Turn Score reset---\n\n"+scoreMessage +"\nTime for the computer to take some rolls\n");
		}
		else if(d1 == d2){
			result = 1;
		}
		else{
			P_turnScore += (d1 + d2);
			result = 0;
		}
		printStanding(1);
		return result;
	}
	
	//Computer's turn method: returns integer representing turn result
		// -1 means turn over
		// 1 means double, force another turn
		//0 means choice of another turn
		static int computerTurn(){
			int result = 0;
			int [] roll = rollDice();
			String[] rolls = numToString(roll);
			int d1 = roll[0];
			int d2 = roll[1];
			computerMessage +="Computer rolled: "+rolls[0]+" & "+rolls[1] +"\n";
		
			//check roll
			if(d1 == 1 && d2 == 1){
				C_turnScore = 0;
				C_totalScore = 0;
				result = -1;
				printStanding(2);
				computerMessage += "\n\n"+"                 0   0\n"
						+ "					          ______\n\n"
						+"\n\n<html> <b>BleepBloopBleep Fatal Calculation Error on that one</b></html>";
			}
			else if(d1 == 1 || d2 == 1){
				C_turnScore = 0;
				result = -1;
				printStanding(2);
				computerMessage += "\n\n<html> <b>BleepBloopBleep... Math.random  let me down once again</b></html>";
			}
			else if(d1 == d2){
				result = 1;
			}
			else{
				C_turnScore += (d1 + d2);
				result = 0;
			}
			printStanding(2);
			return result;
		}
	
	
	 //Rolls two 6 sided dice, returns 1x2 array for two dice rolled
	static int [] rollDice(){
		int roll_1 = 1 + (int)(Math.random() * 6);
		int roll_2 = 1 + (int)(Math.random() * 6);
		
		int [] roll = {roll_1,roll_2};
		return roll;
	}
	//Takes array of integers and returns numbers in word form
	static String [] numToString(int[] roll){
		String [] wordRoll = {"",""};
		for(int i = 0; i < 2; i ++){
			if(roll[i] == 1)
				wordRoll[i] = "One";
			else if(roll[i] == 2)
				wordRoll[i] = "Two";
			else if(roll[i] == 3)
				wordRoll[i] = "Three";
			else if(roll[i] == 4)
				wordRoll[i] = "Four";
			else if(roll[i] == 5)
				wordRoll[i] = "Five";
			else if(roll[i] == 6)
				wordRoll[i] = "Six";
		}
			return wordRoll;
		}
	
	//prints current standing: 1 = player, 2 = computer.  Prints into String variables used in JOptionPane Dialog Boxes
	static void printStanding(int player){
		if(player == 1)
			scoreMessage = ("Your Turn Score: "+P_turnScore+", Your Total Score: "+P_totalScore);
		
		else if(player == 2)
			computerMessage += "Computer Turn Score: "+C_turnScore+", Computer Total Score: "+C_totalScore +"\n";
			
	}
	
	
	}