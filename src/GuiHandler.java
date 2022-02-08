import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GuiHandler
{


    private Pane pane;
    /* Elementen voor het antwoord */
    private Label lblSetAnswer;
    private TextField txtSetAnswer;
    private Button btnSetAnswer;
    private Rectangle shapeLine;
    /* Elementen voor het spel */
    private Label lblRules, lblTryALetter, lblTryAWord, lblLettersCorrect, lblLettersWrong, gameEndText, guesWordLenght;
    private TextField txtTryALetter, txtTryAWord;
    private Button btnTryALetter, btnTryAWord, reset;
    private ImageView imgHangman;

    /* Variabelen */
    private String answer;
    private int mistakes;
    private boolean gameRunningStatus;
    private String correctLetters;

    public GuiHandler()
    {
        /* Initialiseer de variabelen */
        answer = "";
        mistakes = 0;
        gameRunningStatus = false;

        pane = new Pane();
        pane.setPrefWidth(500);
        pane.setPrefHeight(450);

        /* Elementen voor het antwoord */
        lblSetAnswer = new Label();
        lblSetAnswer.setText("The answer to guess:");
        lblSetAnswer.setLayoutX(10);
        lblSetAnswer.setLayoutY(10);

        txtSetAnswer = new TextField();
        txtSetAnswer.setLayoutX(130);
        txtSetAnswer.setLayoutY(10);

        btnSetAnswer = new Button();
        btnSetAnswer.setText("SET ANSWER");
        btnSetAnswer.setLayoutX(400);
        btnSetAnswer.setLayoutY(10);
        btnSetAnswer.setOnAction(event -> setAnswer() );

        shapeLine = new Rectangle();
        shapeLine.setFill(Color.BLACK);
        shapeLine.setHeight(1);
        shapeLine.setWidth(480);
        shapeLine.setLayoutX(10);
        shapeLine.setLayoutY(50);

        /* Elementen voor het spel */
        lblRules = new Label();
        lblRules.setText("Can you guess the answer? \nYou can guess letters or words, but be careful! \nDon't make too many mistakes or it's you who will end up at the gallows!");
        lblRules.setLayoutY(65);
        lblRules.setLayoutX(10);

        lblTryALetter = new Label();
        lblTryALetter.setText("Try a letter:");
        lblTryALetter.setLayoutY(150);
        lblTryALetter.setLayoutX(10);

        txtTryALetter = new TextField();
        txtTryALetter.setPrefWidth(150);
        txtTryALetter.setLayoutY(150);
        txtTryALetter.setLayoutX(80);

        btnTryALetter = new Button();
        btnTryALetter.setText("LET'S TRY THIS LETTER");
        btnTryALetter.setPrefWidth(150);
        btnTryALetter.setLayoutY(180);
        btnTryALetter.setLayoutX(80);
        btnTryALetter.setOnAction(event -> checkLetter() );

        lblTryAWord = new Label();
        lblTryAWord.setText("Try a word:");
        lblTryAWord.setLayoutY(220);
        lblTryAWord.setLayoutX(10);

        txtTryAWord = new TextField();
        txtTryAWord.setPrefWidth(150);
        txtTryAWord.setLayoutY(220);
        txtTryAWord.setLayoutX(80);

        btnTryAWord = new Button();
        btnTryAWord.setText("LET'S TRY THIS WORD");
        btnTryAWord.setPrefWidth(150);
        btnTryAWord.setLayoutY(250);
        btnTryAWord.setLayoutX(80);
        btnTryAWord.setOnAction(event -> checkWord());
        
        lblLettersCorrect = new Label();
        lblLettersCorrect.setText("Letters correct: ");
        lblLettersCorrect.setLayoutY(350);
        lblLettersCorrect.setLayoutX(10);

        lblLettersWrong = new Label();
        lblLettersWrong.setText("Letters wrong: ");
        lblLettersWrong.setLayoutY(380);
        lblLettersWrong.setLayoutX(10);

        imgHangman = new ImageView();
        imgHangman.setImage( new Image( getClass().getResource("img/hangman_0.jpg").toString() ) );
        imgHangman.setFitWidth(110);
        imgHangman.setPreserveRatio(true);
        imgHangman.setLayoutY(150);
        imgHangman.setLayoutX(350);
        
        gameEndText = new Label("");
        gameEndText.setLayoutY(285);
        gameEndText.setLayoutX(325);
        gameEndText.setVisible(false);
        
        guesWordLenght = new Label("");
        guesWordLenght.setLayoutY(285);
        guesWordLenght.setLayoutX(325);
        guesWordLenght.setVisible(false);
        
        reset = new Button("reset");
        reset.setLayoutY(325);
        reset.setLayoutX(325);
        reset.setVisible(false);
        reset.setOnAction(event -> reset());

        correctLetters = "";
        
        //Voeg de controls toe aan de pane
        pane.getChildren().addAll(lblSetAnswer, txtSetAnswer, btnSetAnswer, shapeLine);
        pane.getChildren().addAll(lblRules, lblTryALetter, txtTryALetter, btnTryALetter, lblTryAWord, txtTryAWord, btnTryAWord, lblLettersCorrect, lblLettersWrong, imgHangman, gameEndText, reset, guesWordLenght);
    }

    public Pane getPane()
    {
        return pane;
    }

    /**
     * Alles voor de logica (Controller)
     */
    public void setAnswer()
    {
    	if(txtSetAnswer.getText().matches(".*\\d.*")) {errorPopupHandler(0, "", "Het woord mag alleen letters bevatten"); return;}
    	
    	int answerLenght = txtSetAnswer.getText().length();
    	if(answerLenght < 3) 
    	{
    		errorPopupHandler(0, "", "Het woord moet minimaal 3 letters hebben");
    		return;
    	}
    	else if(answerLenght > 8)
    	{
    		errorPopupHandler(8, "Het woord mag maximaal 8 letters lang zijn", "");
    		return;
    	}
    	
    	gameRunningStatus = true;
    	
        answer = txtSetAnswer.getText().toLowerCase();
        guesWordLenght.setText("De lengte van het woord is: " + answerLenght);
        guesWordLenght.setVisible(true);
        
        setCorrectLettersLength();
        
        lblSetAnswer.setVisible(false);
        txtSetAnswer.setVisible(false);
        btnSetAnswer.setVisible(false);
        
        txtSetAnswer.clear();
    }
    
    public void checkLetter()
    {
    	if(gameRunningStatus == false) {errorPopupHandler(0, "", "De game is niet meer actief. \nVoer een nieuw woord in om de game te starten."); return;}
    	if(txtTryALetter.getText().matches(".*\\d.*")) {errorPopupHandler(0, "", "Het woord mag alleen letters bevatten"); return;}
    	if(answer.length() <= 0) {errorPopupHandler(0, "", "Voer een woord in om te raden"); return;}
    	
        String letter = txtTryALetter.getText();
        int inputLenght = letter.length();
        
        try 
        {
        	if(inputLenght > 1 || inputLenght <= 0) throw new IllegalArgumentException();
        	
        	if(answer.indexOf(letter) < 0) {wrongGuesHandler(letter); return;}
        	correctGuesHandler(letter);
        }
        catch(IllegalArgumentException ilex)
        {
        	errorPopupHandler(inputLenght, "Voer maximaal 1 letter in", "Voer mininaal 1 letter in");
        }
    }

    public void checkWord()
    {
    	if(gameRunningStatus == false) {errorPopupHandler(0, "", "De game is niet meer actief. \nVoer een nieuw woord in om de game te starten."); return;}
    	if(txtTryAWord.getText().matches(".*\\d.*")) {errorPopupHandler(0, "", "Het woord mag alleen letters bevatten"); return;}
    	if(answer.length() <= 0) {errorPopupHandler(0, "", "Voer een woord in om te raden"); return;}

    	String word = txtTryAWord.getText();
        int inputLenght = word.length();
        
        try 
        {
        	if(inputLenght < 3) throw new IllegalArgumentException();
        	
        	if(!word.equalsIgnoreCase(answer)) {loseGameHandler(""); return;}
        	winGameHandler();
        }
        catch(IllegalArgumentException ilex)
        {
        	errorPopupHandler(inputLenght, "Voer minimaal 1 woord in van 3 letters", "Voer minimaal 1 woord in van 3 letters");
        }
    }
    
    //Helper functions
    private void correctGuesHandler(String letter)
    {
    	StringBuilder fillCorrectLetter = new StringBuilder(correctLetters);
    	char correctCharLetter = letter.charAt(0);
    	
    	long amountCharInString = answer.length() - (correctLetters.chars().filter(ch -> ch != correctCharLetter).count());
    	int correctLetterIndex = answer.indexOf(letter);

    	if(correctLetters.indexOf(letter) >= 0)
    	{
    		
    		for(int i = 0; i < amountCharInString + 1; i++)
        	{
        		if(i == 0)
        		{
        			correctLetterIndex = answer.indexOf(letter);
        		}
        		else if(i > 0)
        		{
        			correctLetterIndex = answer.indexOf(letter, answer.indexOf(letter) + i);
        		}
        	}
    	}

    	if(correctLetterIndex < 0) {wrongGuesHandler(letter); return;}
    	fillCorrectLetter.setCharAt(correctLetterIndex, correctCharLetter);
    	correctLetters = fillCorrectLetter.toString();
    	
    	lblLettersCorrect.setText("Letters correct: " + correctLetters);
		txtTryALetter.clear();
    }
    
    private void wrongGuesHandler(String letter)
    {
		mistakes++;
		if(mistakes >= 11) {loseGameHandler("lastImg"); return;}
		
    	String currentWrongLetters = lblLettersWrong.getText();
		lblLettersWrong.setText(currentWrongLetters + " " + letter);
		
		imgHangman.setImage( new Image( getClass().getResource("img/hangman_" + mistakes +".jpg").toString()));
		
		txtTryALetter.clear();
    }
    
    private void errorPopupHandler(int inputLenght, String tekst1, String tekst2)
    {
    	Alert alert = new Alert(AlertType.ERROR);
    	alert.setTitle("ERROR");
    	
    	if(inputLenght == 0)
    	{
    		alert.setContentText(tekst2);
    		alert.showAndWait();
    		return;
    	}
    	
    	if(inputLenght == 8)
    	{
    		alert.setContentText(tekst1);
    		alert.showAndWait();
    		return;
    	} 
    	else if(inputLenght < 3)
    	{
    		alert.setContentText(tekst1);
    		alert.showAndWait();
    		return;
    	}
    	
    	if(inputLenght > 1)
    	{
    		alert.setContentText(tekst1);
    	}
    	else
    	{
    		alert.setContentText(tekst2);
    	}
    	alert.showAndWait();
    }
    
    private void loseGameHandler(String imgState)
    {
    	if(imgState.contentEquals("lastImg"))
    	{
    		imgHangman.setImage( new Image( getClass().getResource("img/hangman_11.jpg").toString()));
    	}
    	
    	gameRunningStatus = false;
    	guesWordLenght.setVisible(false);
    	
    	gameEndText.setText("Game over! \nHet juiste woord was: " + answer);
    	gameEndText.setVisible(true);
    	
    	txtTryAWord.clear();
    	reset.setVisible(true);
    }
    
    private void winGameHandler()
    {
    	guesWordLenght.setVisible(false);
    	
    	gameRunningStatus = false;

    	gameEndText.setText("Je hebt gewonnen! \nhet woord is: " + answer);
    	gameEndText.setVisible(true);

    	txtTryAWord.clear();
    	reset.setVisible(true);
    }
    
    private void setCorrectLettersLength()
    {
    	int answerLength = answer.length();
		String currentText = lblLettersCorrect.getText();

    	for(int i = 0; i < answerLength; i++) {correctLetters += " ";}
    	lblLettersCorrect.setText(currentText + correctLetters);
    }
    
    private void reset()
    {
    	answer = "";
        mistakes = 0;
        
        gameEndText.setVisible(false);
        reset.setVisible(false);
        
        gameRunningStatus = false;
        
        lblLettersCorrect.setText("Letters correct: ");
        lblLettersWrong.setText("Letters wrong: ");
        
        imgHangman.setImage( new Image( getClass().getResource("img/hangman_0.jpg").toString()));
        
        correctLetters = "";
        
        lblSetAnswer.setVisible(true);
        txtSetAnswer.setVisible(true);
        btnSetAnswer.setVisible(true);
    }
}
