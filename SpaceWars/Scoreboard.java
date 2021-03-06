import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
/**
 * Scoreboard is an actor that will track only one stat for each team (eg money, kills, etc). Multiple instances of scoreboard can be created.
 * <p>
 * Its default background colour is light-gray and its default text colour is black, this cannot be changed. 
 * However, it does have a customizable width, height and font size.
 * 
 * @author Alex Li 
 * @version 1.1
 */
public class Scoreboard extends Actor
{
    //instance variables for the scoreboard class
    private GreenfootImage scoreBoard;
    private Color background;
    private Color foreground;
    private Font textFont;
    private int width, height, teamCount = 0;
    //an arraylist to store the info about the teams. 
    private ArrayList<String> scoreText = new ArrayList<String>();
    //and arraylist to store the team names
    private ArrayList<String> teamNames = new ArrayList<String>();
    /**
     * Creates a scoreboard with a custom width, height, and font size and default colours.
     * @param width     The desired width of the scoreboard
     * @param height    The desired height of the scoreboard
     * @param fontSize  The desired font size
     */
    public Scoreboard(int width, int height, int fontSize){
        //sets the scoreboard dimensions
        scoreBoard = new GreenfootImage (width, height);
        //sets the background colour to light gray-ish
        background = new Color (195, 195, 195);
        //sets the foreground colour to black
        foreground = new Color (0, 0, 0);
        //sets the font to calibri and the font size to 14
        textFont = new Font ("Calibri", fontSize);
        scoreBoard.setColor(background);
        scoreBoard.fill();
        this.setImage (scoreBoard);
        scoreBoard.setFont(textFont);
        this.width = width;
        this.height = height;
    }
    /**
     * Toggles the transparency of the scoreboard from transparent to opaque. If true, then the scoreboard will be opaque, otherwise it will be transparent
     * @param ShowScore     A boolean to indicate whether or not the scoreboard should be transparent or not   
     */
    public void showScore(boolean ShowScore){
        //if ShowScore is true, then the scoreboard will not be transparent
        if(ShowScore) getImage().setTransparency(255);
        //else it will be transparent
        else getImage().setTransparency(0);
    }
    /**
     * Evaluates and returns the name of the team based on whether isRed is true or false 
     * @param isRed          A boolean to track the colour of the team  
     * @retrun String        The name of the team based on whether isRed is true or false
     */
    private String evaluateTeamColour(boolean isRed){
        //if isRed is true, the team name is RED
        if(isRed) return "RED";
        //otherwise it is BLUE
        return "BLUE";
    }
    /**
     * Adds a stat of the board. 
     * @param isRed         If true, then the stat added will belong to team red, else it will add a stat belonging to team blue
     * @param statName      The name of the stat you want to add
     * @param Amount        The amount you want the starting value to be
     */
    public void addStat(boolean isRed, String statName, long Amount){  
        //Increase the number if teams by one
        teamCount++;
        //Finds and sets the team name
        String teamName = evaluateTeamColour(isRed);
        //adds the stat to an arraylist for storage
        scoreText.add(teamName + statName+ ": " + Amount);
        //stores the team name in a String arraylists so that it can be used as a pointer to determine the index where the team's info is stored
        teamNames.add(teamName);
        //this method makes sure all the teams are in the correct order
        //teams with a higher stat value will at the top of the scoreboard, while teams with a lower stat value will be at the bottom
        checkScore();
        //formats the info nicely and puts it on the scoreboard
        formatAndPrint(statName);
    }
    /**
     * Increments the stat of a team by one
     * @param isRed         If true, then the stat adjusted will belong to team red, else it will adjust a stat belonging to team blue
     * @param increment     If true, then it adds one to the stat, else it subtracts one
     */
    public void updateStats(boolean isRed, boolean increment){
        //Finds and sets the team name
        String teamName = evaluateTeamColour(isRed);
        //gets the index of where teams's info is located
        int index = teamNames.indexOf(teamName);
        //finds the current value of the stat that is going to be changed
        long newAmount = Integer.parseInt(scoreText.get(index).substring(scoreText.get(index).indexOf(": ")+2));
        //increases it by one if increment is true
        if(increment)newAmount++;
        //else it removes one
        else newAmount--;
        //finds the name of the stat
        String statName = scoreText.get(index).substring(teamName.length(), scoreText.get(index).indexOf(":")-1);
        //changes the team's stat to the new stat
        scoreText.set(index, teamName + statName+ ": " + newAmount);
        //Reorganizes the teams by their score
        checkScore();
        //formats the info nicely and puts it on the scoreboard
        formatAndPrint(statName);
    }
    /**
     * Increments the stat of a team by x
     * @param isRed          If true, then the stat adjusted will belong to red team, else it will adjust a stat belonging to team blue
     * @param changeAmount   The amount you want to add to the current statValue
     */
    public void updateStats(boolean isRed, long changeAmount){
        //Finds and sets the team name
        String teamName = evaluateTeamColour(isRed);
        //gets the index of where user's info is located
        int index = teamNames.indexOf(teamName);
        //finds the current value of the stat that is going to be changed
        long newAmount = Integer.parseInt(scoreText.get(index).substring(scoreText.get(index).indexOf(": ")+2));
        //changes the value by x
        newAmount += changeAmount;
        //finds the name of the stat
        String statName = scoreText.get(index).substring(teamName.length(), scoreText.get(index).indexOf(":"));
        //changes the team's stat to the new stat
        scoreText.set(index, teamName + statName+ ": " + newAmount);
        //Reorganizes the teams by their score
        checkScore();
        //formats the info nicely and puts it on the scoreboard
        formatAndPrint(statName);
    }
    /**
     * Reorganizes the rankings of all the teams in decending order according to their stat value in the scoreText arraylist. 
     */
    private void checkScore(){
        for(int i = 0; i < teamCount-1; i++){
            for(int j = 0; j < teamCount - i- 1; j++){
                //if the stat value is less than the stat value of the team below it, they swap places
                if(Integer.parseInt(scoreText.get(j).substring(scoreText.get(j).indexOf(": ")+2)) < Integer.parseInt(scoreText.get(j+1).substring(scoreText.get(j+1).indexOf(": ")+2))){
                    String temp = scoreText.get(j), temp2 = teamNames.get(j);
                    scoreText.set(j, scoreText.get(j+1)); teamNames.set(j, teamNames.get(j+1));
                    scoreText.set(j+1,temp); teamNames.set(j+1,temp2);
                }
            }
        }
    }
    /**
     * Formats and prints the stats of all the players on to the scoreboard.
     * @param statName      The name of the stat that the scoreboard is tracking
     */
    private void formatAndPrint(String statName){
        //clears the scoreboard of all the previous information 
        scoreBoard.clear();
        //sets the background to the background colour
        scoreBoard.setColor(background);
        //fills the scoreboard with the background colour
        scoreBoard.fill();
        //gives the text colour
        scoreBoard.setColor(foreground);
        //adds all the info
        if(!teamNames.isEmpty()){
            scoreBoard.drawString("Team", 0, height/3); scoreBoard.drawString(statName, 50, height/3);
            for(int i = 0; i < teamCount; i++){
                // Draws the text onto the scoreboard
                scoreBoard.drawString(scoreText.get(i).substring(scoreText.get(i).indexOf(teamNames.get(i)), teamNames.get(i).length()), 0, (i+2)*(height/3));
                scoreBoard.drawString(scoreText.get(i).substring(teamNames.get(i).length() + statName.length()+1), 50, (i+2)*(height/3));
            }
        }
    }
    /**
     * Returns the value of a stat that a team has
     * 
     * @param isRed     If true, then the stat returned will belong to team red, else it will belong to team blue
     * @return int      The stat value that a team has or -1 if the team name is invalid
     */
    public int getStat(boolean isRed){
        //finds and sets the team name
        String teamName = evaluateTeamColour(isRed);
        //returns the stat value if the team can be found
        if(teamNames.indexOf(teamName)!=-1) return Integer.parseInt(scoreText.get(teamNames.indexOf(teamName)).substring(scoreText.get(teamNames.indexOf(teamName)).indexOf(": ")+2));
        return -1;
    }
}