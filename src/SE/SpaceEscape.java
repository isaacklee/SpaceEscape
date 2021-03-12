package SE;
/*
 * Isaac Lee
 * isaaclee@usc.edu
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;


public class SpaceEscape extends Application
{
	Random r;
	
	//list of alien image
	ArrayList<String>als=new ArrayList<String>();
	ArrayList<String>addedals=new ArrayList<String>();
	
	//list of alien objects
	LinkedList<Obstacle> aliens;
	LinkedList<Aliens>aliens2;
	LinkedList<star>stars;
	
	//image patterns
	ImagePattern backgroundimg;
	ImagePattern earthimg;
	ImagePattern shot;
	ImagePattern ss;
	ImagePattern al1;
	ImagePattern al2;
	ImagePattern explode;
	
	//File stream
	FileInputStream input;
	
	//hardness control button
	Button addAlien;
	Button removeAlien;
	Button hardness;
	//SpaceShip object
	SpaceShip spaceShip;
	
	//starting 4 aliens object
	Obstacle alien;
	//added aliens after button clicked
	Aliens ali;
	
	star star;
	
	//the Exit door 
	Door door;
	MidLine middleLine;
	
	//Lives
	int lives=5;
	int shoot=5;
	int point=0;
	//win
	Boolean win=false;
	
	Scene scene;
	long lasttime;
	boolean firsttime = true;
	
	Group root;

	
	Boolean added=false;
	
	final int STEP_SIZE = 50;
    final Duration DURATION = Duration.millis(50);
    Stage stage;
    //check for stage
    int count=0;
    int level=1;
	Label score = new Label("0");
	Label missicount = new Label("Missile : "+shoot);
	public static void main( String[] args )
	{ launch(args);}
	
    @Override
    public void start(Stage stage)
    { 
    	
    	
    	setImage();
    	r = new Random();
    	root = new Group();
        scene = new Scene(root, 900, 600);
 
       
        // set background image 
        scene.setFill(backgroundimg);
        stage.setTitle("Space Escape");
	    stage.setScene(scene);
	    stage.show();
	    
	    //List of aliens
	    aliens = new LinkedList<Obstacle>();
	    aliens2 = new LinkedList<Aliens>();
	    stars = new LinkedList<star>();
	    
	    Obstacle.setSpaceW(800);
	    Obstacle.setSpaceH(600);
	    
	    //create exit door
    	exitDoor();
    	//set starter obstacles
	    StarterOb();
	   
	    
	    controller();
	    
	    stars();
	    
	    //restart game
	    scene.addEventHandler(KeyEvent.KEY_PRESSED,(KeyEvent ke) ->
    	 {
    		 if(ke.getCode().equals(KeyCode.P)) {
	    		 System.out.println( "Restarting app!" );
	 			  stage.close();
	 			  start( new Stage() );
	 			  if(win) {
	 				  ++count;
	 				  ++level;
	 			  }
	 			  else {
	 				  level=1;
	 			  	  count=0;
	 			  }
    		 }
    	 });
	    //Accessibility mouse control
	    buttons();
	   
	    scene.addEventHandler(MouseEvent.MOUSE_DRAGGED,(MouseEvent m) ->
   	 	{
	    	
   	 		spaceShip.setX(spaceShip.getX());
	      	spaceShip.setY(spaceShip.getY());
   	 		
   	 	});
	    
	    flying_aliens();
	    
	    //create spaceship
	    spaceship();
 	    System.out.println(point);
    	Driver d = new Driver();
    	d.start();
    }
   

	public class Driver extends AnimationTimer
    {
    	@Override
    	public void handle( long now )
    	{
    		//System.out.println("timer="+now);
    		if ( firsttime ) { 
    			lasttime = now; 
    			firsttime = false;  
    		}
    		else{
    		    double deltat = (now-lasttime ) * 1.0e-9;
    			lasttime = now;
    			for(Obstacle alien:aliens) {
    				alien.move(deltat);
    			}
    			if(aliens2.size()!=0) {
	    			for(Aliens a:aliens2) {
	    				a.move(deltat);
	    			}
    			}
    			
	    		//get bounds 
				Bounds midlineBounds = middleLine.getBoundsInLocal();
				Bounds doorBounds = door.getBoundsInLocal();
				Bounds spaceshipBound = spaceShip.getBoundsInLocal();
				
				for(Aliens a:aliens2) {
					Bounds alienBounds = a.getBoundsInLocal();
					if(spaceshipBound.intersects(alienBounds)) {
						Hit();
						
						
					}		
				}
				for(Obstacle o:aliens) {
					Bounds obstacleBounds = o.getBoundsInLocal();
					if(spaceshipBound.intersects(obstacleBounds)) {
						Hit();
					
					}
				}
				if(midlineBounds.intersects(spaceshipBound)) {
					Hit();
					
				}
				if(spaceshipBound.intersects(doorBounds)) {
					Win();
				}
				
				for(star s:stars) {
					Bounds starBounds =s.getBoundsInLocal();
					if(spaceshipBound.intersects(starBounds)) {
						stars.remove(s);
						root.getChildren().remove(s);
						point+=50;
						if(shoot<5) {
							shoot++;
							missicount.setText("Missile : "+shoot);
						}
				 	    System.out.println(point);
				 	    String point1 = String.valueOf(point);
				       	score.setText(point1);

					}
				}
    		}
		}
    	
    }
	public void flying_aliens() {
		for(int i=0; i<count;i++) {
	    	try {
				added = true;
	 	    	FileInputStream input1;
	 	    	ali = new Aliens(r, r.nextInt(400)+100,r.nextInt(300)+20,r.nextInt(300)+60,r.nextInt(300)+60);
	 	    	int rand = r.nextInt(3); 
		    	String al = addedals.get(rand);
				input1 = new FileInputStream(al);
				Image image = new Image(input1); 
			    al2 = new ImagePattern(image); 
			    ali.setFill(al2);
			    ali.setStroke(Color.HOTPINK);
			    ali.setStrokeWidth(3);
	 	    	aliens2.add(ali);
	 	    	root.getChildren().add(ali);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    }
	}
	public void stars() {
		int num = r.nextInt(5);
	    for( int i=0; i<num;i++) {
		    int x = r.nextInt(600)+100;
		    int y = r.nextInt(500)+60;
		    star = new star(x, y);
		    stars.add(star);
		    try {
				input = new FileInputStream("image/star.jpg");
				Image image = new Image(input); 
			    star.setFill(new ImagePattern(image));
			    star.setStroke(Color.YELLOW);
			    star.setStrokeWidth(2);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
		    root.getChildren().add(star);
	    }
	}
    public void setImage() {
    	//add the image files to list
    	als.add("image/alien1.png");
    	als.add("image/alien2.png");
    	als.add("image/alien3.png");
    	als.add("image/alien4.png");
    	addedals.add("image/addedAlien1.png");
    	addedals.add("image/addedAlien2.png");
    	addedals.add("image/addedAlien3.png");
    	
    	//set Backgound image
		try {
			input = new FileInputStream("image/space.jpg");
			Image image = new Image(input); 
		    backgroundimg = new ImagePattern(image); 
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
    }
    
    public void spaceship() {
    	spaceShip = new SpaceShip();
    	spaceShip.toFront();
	    FileInputStream input3;
		try {
			input3 = new FileInputStream("image/spaceship.png");
			Image image = new Image(input3); 
		    ss = new ImagePattern(image); 
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		spaceShip.setStroke(Color.RED);
	    spaceShip.setStrokeWidth(4);
		spaceShip.setFill(ss);
	    root.getChildren().add(spaceShip);
    }
    
    public void StarterOb() {
    	//Generate 4 random balls for starter
	    for(int i=0; i<4;i++) {
	    	alien=new Obstacle(r,(110)+(200*i),r.nextInt(580)+10,r.nextInt(300)+60,r.nextInt(300)+150*(i+1));
	    	FileInputStream input1;
		    try {
		    	//int rand = r.nextInt(4); 
		    	String al = als.get(i);
				input1 = new FileInputStream(al);
				Image image = new Image(input1); 
			    al1 = new ImagePattern(image); 
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
		    
		    alien.setStroke(Color.PURPLE);
		    alien.setStrokeWidth(4);
		    alien.setFill(al1);
		    
	    	aliens.add(alien);
	    }
	   
	    root.getChildren().addAll(aliens);
    }
    public void buttons() {
    	VBox vbox = new VBox();
 	   
       	HBox hbox1 = new HBox();
       	String point1 = String.valueOf(point);
       	score.setText(point1);
       	score.setStyle( "-fx-font: 80px Tahoma;-fx-fill: linear-gradient(from 0% 0% to 100% 200%, repeat, aqua 0%, red 50%);-fx-stroke: white; -fx-stroke-width: 1;-fx-text-fill: white;");
       	hbox1.getChildren().add(score);
       	
       	
    	//hardness control
	    HBox hbox = new HBox();
	    hardness = new Button("Alien Control");
    	addAlien = new Button("+");
    	removeAlien = new Button("-");
    	
    	missicount.setStyle("-fx-font: 30px Tahoma;-fx-fill: linear-gradient(from 0% 0% to 100% 200%, repeat, aqua 0%, red 50%);-fx-stroke: white; -fx-stroke-width: 1;-fx-text-fill: white;");
    	
    	addAlien.setStyle("-fx-border-color:PURPLE;-fx-background-color:black;-fx-text-fill: white;");
    	removeAlien.setStyle("-fx-border-color:PURPLE;-fx-background-color:black;-fx-text-fill: white;");
    	hardness.setStyle("-fx-border-color:PURPLE;-fx-background-color:black;-fx-text-fill: white;");
    	
    	
    	
 	    hbox.getChildren().addAll(hardness,addAlien,removeAlien, missicount);
    	//root.getChildren().add(hbox);
 	    
    	removeAlien.setOnAction((ActionEvent e)->{
    		if(aliens2.size()!=0&&shoot>0) {
	    		Aliens removealien = aliens2.get(aliens2.size()-1);
	    		//removealien.setFill(Color.LIMEGREEN);
	    		try {
	    			input = new FileInputStream("image/shotAlien.png");
	    			Image image = new Image(input); 
	    		    shot = new ImagePattern(image);
	    		    removealien.setFill(shot);
	    		} catch (FileNotFoundException e1) {
	    			// TODO Auto-generated catch block
	    			e1.printStackTrace();
	    		} 
	    		aliens2.remove(removealien);
	    		root.getChildren().remove(removeAlien);
	    		shoot--;
	    		missicount.setText("Missile : "+shoot);
	    		
    		}
    	});
 	    addAlien.setOnAction((ActionEvent e)->{
			try {
				added = true;
	 	    	FileInputStream input1;
	 	    	ali = new Aliens(r, r.nextInt(400)+100,r.nextInt(300)+20,r.nextInt(300)+60,r.nextInt(300)+60);
	 	    	int rand = r.nextInt(3); 
		    	String al = addedals.get(rand);
				input1 = new FileInputStream(al);
				Image image = new Image(input1); 
			    al2 = new ImagePattern(image); 
			    ali.setFill(al2);
			    ali.setStroke(Color.HOTPINK);
			    ali.setStrokeWidth(3);
	 	    	aliens2.add(ali);
	 	    	root.getChildren().add(ali);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	
 	    }
 	    );
 	    vbox.getChildren().addAll(hbox,hbox1);
 	    root.getChildren().add(vbox);
    }
    
    public void controller() {
	    //controller for spaceship
	    scene.setOnKeyPressed( new EventHandler<KeyEvent>() {
	    	public void handle(KeyEvent ke)
           {
	    		final TranslateTransition transition = new TranslateTransition(DURATION, spaceShip);
        	   if ( ke.getCode().equals(KeyCode.A) )
        	   { 
        		   
        		   if(spaceShip.getX()<0) {
        			   spaceShip.setX(10);
        			   spaceShip.setY(spaceShip.getY());
        		   }
        		   else {
        			   spaceShip.move(-50,0); 
        		  /* transition.setFromX(spaceShip.getTranslateX());
                   transition.setFromY(spaceShip.getTranslateY());
                   transition.setToX(spaceShip.getTranslateX() - STEP_SIZE);
                   transition.setToY(spaceShip.getTranslateY());
                   transition.playFromStart();
                   spaceShip.check();*/
        		   }
        	   }
        	   else if ( ke.getCode().equals(KeyCode.D))
        	   { 
        		   spaceShip.move(50,0); 
        	   		//spaceShip.setVelX(5);
        		  /* transition.setFromX(spaceShip.getTranslateX());
                   transition.setFromY(spaceShip.getTranslateY());
                   transition.setToX(spaceShip.getTranslateX() + STEP_SIZE);
                   transition.setToY(spaceShip.getTranslateY());
                   transition.playFromStart();
                   spaceShip.check();*/

        	   }
        	   
        	   else if(ke.getCode().equals(KeyCode.S)) 
        	   { 	spaceShip.move(0,50); 
        		   /*transition.setFromX(spaceShip.getTranslateX());
                   transition.setFromY(spaceShip.getTranslateY());
                   transition.setToX(spaceShip.getTranslateX());
                   transition.setToY(spaceShip.getTranslateY() + STEP_SIZE);
                   transition.playFromStart();
                   spaceShip.check();*/

        	   }
        	   else if(ke.getCode().equals(KeyCode.W)) 
        	   {   spaceShip.move(0,-50); 
        		   /*transition.setFromX(spaceShip.getTranslateX());
                   transition.setFromY(spaceShip.getTranslateY());
                   transition.setToX(spaceShip.getTranslateX());
                   transition.setToY(spaceShip.getTranslateY() - STEP_SIZE);
                   transition.playFromStart();
                   spaceShip.check();*/

        	   }
        	  
        	   else if(ke.getCode().equals(KeyCode.SEMICOLON))
        	   {//lauch missle to kill alien
        		   if(aliens2.size()!=0&&shoot>0) {
       	    		Aliens removealien = aliens2.get(aliens2.size()-1);
       	    		//removealien.setFill(Color.LIMEGREEN);
       	    		try {
       	    			input = new FileInputStream("image/shotAlien.png");
       	    			Image image = new Image(input); 
       	    		    shot = new ImagePattern(image);
       	    		    removealien.setFill(shot);
       	    		} catch (FileNotFoundException e1) {
       	    			// TODO Auto-generated catch block
       	    			e1.printStackTrace();
       	    		} 
       	    		aliens2.remove(removealien);
       	    		root.getChildren().remove(removeAlien);
       	    		shoot--;
       	    		missicount.setText("Missile : "+shoot);
        		   }
        	   }
        	   else if(ke.getCode().equals(KeyCode.L)) {
        		   try {
       				added = true;
       	 	    	FileInputStream input1;
       	 	    	ali = new Aliens(r, r.nextInt(400)+100,r.nextInt(300)+20,r.nextInt(300)+60,r.nextInt(300)+60);
       	 	    	int rand = r.nextInt(3); 
       		    	String al = addedals.get(rand);
       				input1 = new FileInputStream(al);
       				Image image = new Image(input1); 
       			    al2 = new ImagePattern(image); 
       			    ali.setFill(al2);
       			    ali.setStroke(Color.HOTPINK);
       			    ali.setStrokeWidth(3);
       	 	    	aliens2.add(ali);
       	 	    	root.getChildren().add(ali);
       			} catch (FileNotFoundException e1) {
       				// TODO Auto-generated catch block
       				e1.printStackTrace();
       			}
        	   }
        	   
    	   }
	    	});
	    
	    scene.setOnKeyReleased (new EventHandler<KeyEvent>() {
		
	    	public void handle(KeyEvent ke) 
           {
        	   if ( ke.getCode().equals(KeyCode.A) )
        	   { 
        	   		spaceShip.setVelX(0);
        	   }
        	   else if ( ke.getCode().equals(KeyCode.D))
        	   { 
        		   //spaceShip.move(10,0); 
        	   		spaceShip.setVelX(0);
        	   }
        	   
        	   else if(ke.getCode().equals(KeyCode.S)) 
        	   { spaceShip.move(0,10); }
        	   else if(ke.getCode().equals(KeyCode.W)) 
        	   { spaceShip.move(0,-10); }
           }
	    });
    }
    
    public void exitDoor() {
    	//make the line
    	middleLine = new MidLine();
    	try {
			input = new FileInputStream("image/safeplace.png");
			Image image = new Image(input); 
		   // earthimg = new ImagePattern(image); 
		    middleLine.setFill(new ImagePattern(image));
		   
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
    	
    	root.getChildren().add(middleLine);
    	
    	//randomize the exit door position
	    int pdoor = r.nextInt(500)+50;
	    int pdoory = r.nextInt(100)+750;
    	//make the door
    	door = new Door(pdoor, pdoory);
    	door.setFill(Color.DARKRED);
    	
		try {
			input = new FileInputStream("image/blackhole2.jpg");
			Image image = new Image(input); 
		   // earthimg = new ImagePattern(image); 
		    door.setFill(new ImagePattern(image));
		    door.setStroke(Color.PURPLE);
		    door.setStrokeWidth(5);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
    	
    	root.getChildren().add(door);
    }
    public void Hit() {
    	Lose();
    }
    
    //Print out the ending message
    public void Win() {
    	Label label = new Label("Escaped!!\n"+"  Level "+level);
    	label.setFont(Font.font ("Verdana", 50));
    	label.setStyle("-fx-border-color:PURPLE;-fx-text-fill: white;");
		label.setLayoutX(250);
		label.setLayoutY(160);
		label.setPrefSize(300, 300);
		label.setBackground(new Background(new BackgroundFill(Color.DARKRED,new CornerRadii(20), new Insets(20))));
		label.setAlignment(Pos.CENTER);
		label.toFront();
		root.getChildren().add(label);
		win = true;
		
		if(point>500) {
			Label label1 = new Label("Back to Earth");
	    	label1.setFont(Font.font ("Verdana", 30));
	    	label1.setStyle("-fx-border-color:PURPLE;-fx-text-fill: white;");
			label1.setLayoutX(250);
			label1.setLayoutY(160);
			label1.setPrefSize(300, 300);
			label1.setBackground(new Background(new BackgroundFill(Color.DARKRED,new CornerRadii(20), new Insets(20))));
			label1.setAlignment(Pos.CENTER);
			label1.toFront();
			root.getChildren().add(label1);
		}
		
    }
    public void Lose() {
    	Label label = new Label("Captured");
    	label.setFont(Font.font ("Verdana", 50));
    	label.setStyle("-fx-border-color:PURPLE;-fx-text-fill: white;");
		label.setLayoutX(250);
		label.setLayoutY(160);
		label.setPrefSize(300, 300);
		label.setBackground(new Background(new BackgroundFill(Color.DARKRED,new CornerRadii(20), new Insets(20))));
		label.setAlignment(Pos.CENTER);
		label.toFront();
		root.getChildren().add(label);
		
		win=false;
		spaceShip.setDisable(true);
		try {
			input = new FileInputStream("image/explode1.png");
			Image image = new Image(input); 
		    explode = new ImagePattern(image); 
		    spaceShip.setFill(explode);
		    point=0;
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		
    }
}