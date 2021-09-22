package game;

import exceptions.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sounds.Sounds;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class BattleShipGame extends Application {

    private Stage stage;
    private boolean turn; //if turn = true then player's turn, else pc's turn
    private Board enemy_board, player_board;
    private Label enemy_update,enemy_percentage,enemy_alive_ships,enemy_points;
    private Label label,player_update,player_percentage, player_alive_ships,player_points;
    private Player player;
    private Enemy enemy;
    private Button for_start_only,shootButton;
    private Spinner<Integer> coord_x, coord_y; //spinners for choosing the coordinates of next target
    private boolean play = true;    //indicates whether the game is on
    private static File file_player = new File("scenarios/player_default.txt"); //scenarios
    private static File file_enemy = new File("scenarios/enemy_default.txt");
    private static final Image folder = new Image("game/envelope.jpg");
    private static final Image question_mark = new Image("game/question_mark.jpg");
    private static final Image table = new Image("game/table.png");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        stage.setResizable(false);
        stage.setOnCloseRequest(e -> {e.consume(); handle_exit();});
        Pane temp = setup();
        if (temp != null) {             //if there was invalid input and the user didn't want to go on
            Scene scene = new Scene(temp);
            stage.getIcons().add(new Image("game/battleship.jpg"));
            stage.setTitle("MediaLab Battleship");
            stage.setScene(scene);
            stage.show();
        }
    }

    private void make_turn(){
        //sets the turn on true or false randomly, in order to decide who is gonna play first
        double rand = Math.random();
        turn = rand < 0.5;
    }

    private Pane setup() {
        //returns the layout of the main window
        //contains at most code for styling the layout
        BorderPane root = new BorderPane();

        root.setPrefSize(1100, 650);
        root.setBackground(new Background(new BackgroundFill(Color.LIGHTSKYBLUE,new CornerRadii(5.0),  new Insets(-5.0))));

        root.setTop(setup_top());

        //player setup
        player = new Player();
        player_board = new Board(false);

        VBox player_grid = new VBox(10);
        HBox player_stats = new HBox(10);
        player_stats.setPadding(new Insets(10, 10, 10, 25));
        player_percentage = new Label("Percentage: 0.0 %");
        player_alive_ships = new Label("Alive: 5");
        player_points = new Label("Points: 0");

        player_stats.getChildren().addAll(player_percentage,player_points,player_alive_ships);

        player_grid.getChildren().addAll(player_stats,player_board);

        //enemy setup
        enemy = new Enemy();
        enemy_board = new Board(true);

        VBox enemy_grid = new VBox(10);
        HBox enemy_stats = new HBox(10);
        enemy_stats.setPadding(new Insets(10, 10, 10, 25));
        enemy_percentage = new Label("Percentage: 0.0 %");
        enemy_alive_ships = new Label("Alive: 5");
        enemy_points = new Label("Points: 0");

        enemy_stats.getChildren().addAll(enemy_percentage,enemy_points,enemy_alive_ships);

        enemy_grid.getChildren().addAll(enemy_stats,enemy_board);

        make_turn();

        //middle for updates
        VBox updates = new VBox(10);
        updates.setAlignment(Pos.CENTER);
        updates.setPrefWidth(300);
        label = new Label();
        label.setWrapText(true);
        label.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 16));
        label.setText("Let's begin!\n" + (turn ? "You start!" : "Enemy starts!"));
        for_start_only =  new Button("Ok!");
        for_start_only.setOnAction(e -> {
            play = true;
            for_start_only.setDisable(true);
            for_start_only.setVisible(false);
            if(!turn) enemy_move();
            shootButton.setDisable(false);
        });
        Label title1 = new Label("Update on you:");
        Label title2 = new Label("Update on enemy:");

        player_update = new Label();
        player_update.setWrapText(true);
        player_update.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 16));
        player_update.setBackground(new Background(new BackgroundFill(Color.CYAN,new CornerRadii(5.0),  new Insets(-5.0))));

        enemy_update = new Label();
        enemy_update.setWrapText(true);
        enemy_update.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 16));
        enemy_update.setBackground(new Background(new BackgroundFill(Color.RED,new CornerRadii(5.0),  new Insets(-5.0))));

        updates.getChildren().addAll(label,for_start_only,title1, player_update, title2, enemy_update);

        //middle of root
        HBox middle = new HBox(10, player_grid, updates, enemy_grid);
        middle.setAlignment(Pos.CENTER);
        root.setCenter(middle);

        //down of root
        VBox down = new VBox();
        down.setSpacing(10);
        down.setAlignment(Pos.CENTER);
        down.setPadding(new Insets(10, 10, 10, 25));

        Label choose_move = new Label("Choose your next move wisely:");
        choose_move.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 12));
        choose_move.setMinSize(30,30);

        //insert values
        HBox insert_values = new HBox(10);
        Label coordinate_x = new Label("Coordinate x:");
        coordinate_x.setFont(Font.font("verdana",FontPosture.REGULAR,11));

        Label coordinate_y = new Label("Coordinate y:");
        coordinate_y.setFont(Font.font("verdana",FontPosture.REGULAR,11));

        coord_x = new Spinner<>(0,9,0);
        coord_x.getValueFactory().setWrapAround(true);
        coord_y = new Spinner<>(0,9,0);
        coord_y.getValueFactory().setWrapAround(true);
        shootButton = new Button("Shoot!");
        shootButton.setDisable(true);
        shootButton.setMaxSize(70,70);

        insert_values.getChildren().addAll(coordinate_x,coord_x,coordinate_y,coord_y);
        insert_values.setAlignment(Pos.CENTER);

        down.getChildren().addAll(choose_move,insert_values,shootButton);

        root.setBottom(down);

        shootButton.setOnAction(e -> {
            shootButton.setDisable(true);   //disable shoot button in order to execute
                                            //player's move and probably enemy's move
            if(player_move()) {             //if the game is still on, create a new thread, pause it
                                            //for 1.5 sec and then execute enemy's move
                Thread delay = new Thread(()->{
                    try {Thread.sleep(1500);} catch (InterruptedException ex) { ex.printStackTrace();}
                    Platform.runLater(this::enemy_move);
                });
                delay.start();
            }
        });
        //if an exception is raised by reading the files, a popup window containing the error details is shown
        //and asks user to select new files
        try {
            addShips(player_board,file_player); //initialize grids based on input scenarios
            addShips(enemy_board,file_enemy);
        } catch (BattleShipException | IOException e) {
            boolean answer = AlertBox.handle_exit("Error",((e.getMessage() != null)? e.getMessage(): e) + "\nDo you want to select new scenario files?");
            if (answer) {
                get_scenarios(false);
                root = (BorderPane) setup();
            } else {
                stage.close();
                root = null;
            }
        }
        return root;
    }

    private void addShips(Board board, File file) throws BattleShipException, IOException{
        //reads the file containing the scenario and builds the board
        Scanner myReader = null;
        try {
            myReader = new Scanner(file);
            int counter = 0;
            while (myReader.hasNextLine()) {
                if ((counter++) > 4) throw new InvalidInputException("More lines than expected!");
                //we expect only 5 lines i.e 5 ships
                String data = myReader.nextLine();
                String[] token = data.split(",");
                int[] tokens = check(token);
                board.addShip(tokens[0] - 1, tokens[1], tokens[2], tokens[3] == 1);
            }
        }
        finally{
            if(myReader != null) myReader.close();
        }
    }

    private int[] check(String[] tokens) throws BattleShipException{
        //checks whether these strings can be converted to integers and if so, whether they follow the rules
        //and returns the integer representation of the strings of the array
        int[] temp = new int[4];
        int counter = 0;

            for (String x : tokens) {
                try {
                    if (counter > 3) throw new InvalidInputException("More arguments than needed!");
                    temp[counter++] = Integer.parseInt(x);
                }
                catch(NumberFormatException e){
                    throw new InvalidInputException("Only integers allowed in scenarios files!");
                }

            }
            if(temp[0] < 1 || temp[0] > 5) {
                throw new InvalidInputException("Type of ship is integer between 1 and 5!");
            }
            else if(temp[3] != 1 && temp[3] != 2){
                throw new InvalidInputException("Direction of ship is indicated by 1 (horizontal) or 2 (vertical)!");
            }

        return temp;
    }

    private boolean player_move(){
        //same as enemy_move but also returns whether the game is still on
        int x,y;
        coord_x.commitValue();
        x = coord_x.getValue();
        coord_y.commitValue();
        y =  coord_y.getValue();
        Cell c = enemy_board.getCell(x,y);


        Move player_move;
        if(c.is_shot()){
            player_update.setText("You wasted a shot!");
            player_move = new Move(c,0,false);
        }
        else{
            int points = enemy_board.hit(c);

            if(points == 0){
                player_update.setText("Better luck next time!");
                player_move = new Move(c,0,false);
            }
            else {
                if (enemy_board.sunk(c)) {
                    player_update.setText("You sank a ship and earned " + points + " points. Way to go!");
                    player_move = new Move(c,points,true);
                } else {
                    player_update.setText("Success! You earned " + points + " points!Go on!");
                    player_move = new Move(c,points,false);
                }
            }
        }
        player.addMove(player_move);
        player_percentage.setText("Percentage: " + player.getPercentage() + "%");
        enemy_alive_ships.setText("Alive: " + enemy_board.getQuantity());
        player_points.setText("Points: " + player.getPoints());
        if(enemy_board.defeat()) {
            play = false;
            label.setText("VICTORIOUS!");
            Sounds.success.play();
            Sounds.success.seek(Sounds.success.getStartTime());
            start_new_game("End", "You shank all of their ships!\n" + "Want again?", false);
        }
        else if((!turn) && enemy.done()) {
            play = false;
            if(enemy.getPoints() >= player.getPoints()) {
                label.setText("LOSER!");
                Sounds.failure.play();
                Sounds.failure.seek(Sounds.failure.getStartTime());
                start_new_game("End", "You ran out of shots and lost!\n" + "Want again?", false);
            }
            else{
                label.setText("VICTORIOUS!");
                Sounds.success.play();
                Sounds.success.seek(Sounds.success.getStartTime());
                start_new_game("End", "You ran out of shots and won!\n" + "Want again?", false);
            }
        }
        label.setText("It's their turn!");
        return play;
    }

    private void enemy_move(){
        //executes enemy's move and updates the appropriate labels based on the result of the move
        //checks whether the enemy has won by sinking all ships
        //checks whether the game is over due to zeroing the available left shots
        Tuple enemy_tuple = enemy.make_move();
        Cell enemy_cell = player_board.getCell(enemy_tuple);
        int points = player_board.hit(enemy_cell);

        Move enemy_move;
        if (points == 0) {
            enemy_update.setText("You got lucky!");
            enemy_move = new Move(enemy_cell, 0, false);
        } else {
            if (player_board.sunk(enemy_cell)) {
                enemy_update.setText("They sank a ship and earned " + points + " points!");
                enemy_move = new Move(enemy_cell, points, true);
            } else {
                enemy_update.setText("They earned " + points + " points! Come stronger!");
                enemy_move = new Move(enemy_cell, points, false);
            }
        }

        enemy.addMove(enemy_move);
        enemy_percentage.setText("Percentage: " + enemy.getPercentage() + "%");
        player_alive_ships.setText("Alive: " + player_board.getQuantity());
        enemy_points.setText("Points: " + enemy.getPoints());
        label.setText("It's your turn!");
        shootButton.setDisable(false);
        if (player_board.defeat()) {

            label.setText("LOSER!");
            Sounds.failure.play();
            Sounds.failure.seek(Sounds.failure.getStartTime());

            start_new_game("End", "They shank all of your ships and you lost!\n" + "Want again?", false);

        }
        else if (turn && enemy.done()) {
            if (enemy.getPoints() >= player.getPoints()) {
                label.setText("LOSER!");

                Sounds.failure.play();
                Sounds.failure.seek(Sounds.failure.getStartTime());

                start_new_game("End", "You ran out of shots and lost!\n" + "Want again?", false);

            } else {
                Sounds.success.play();
                Sounds.success.seek(Sounds.success.getStartTime());
                label.setText("VICTORIOUS!");
                start_new_game("End", "You ran out of shots and won!\n" + "Want again?", false);
            }
        }
    }

    private MenuBar setup_top(){
        MenuBar top = new MenuBar();
        top.getMenus().addAll(Application(),Details(),Help());
        return top;
    }

    private Menu Application(){
        Menu app = new Menu("Application");
        MenuItem Start = new MenuItem("Start");
        MenuItem Load = new MenuItem("Load");
        MenuItem Exit = new MenuItem("Exit");

        Start.setOnAction(e->start_new_game("Load new game","Want to play another game?",true));

        Load.setOnAction(e->get_scenarios(true));

        Exit.setOnAction(e->handle_exit());

        app.getItems().addAll(Start,Load,Exit);
        return app;

    }

    private void start_new_game(String title,String message, boolean stay) {
        //triggers popup window to determine whether to begin new session or not
        //variable stay indicates whether the negative answer should trigger termination of game or not
        boolean answer = AlertBox.handle_exit(title,message);
        if (answer) {
            Pane temp = setup();
            if (temp != null)
                stage.setScene(new Scene(temp));

        } else if (!stay) {
            stage.close();
        }
    }

    private void get_scenarios(boolean load){
        //creates popup window in order to select scenario files for each role
        Stage window = new Stage();
        window.getIcons().add(folder);
        Button select_player_scenario = new Button("Select player scenario");
        Button select_enemy_scenario = new Button("Select enemy scenario");
        select_player_scenario.setOnAction(e->{File f = load_file(window); if(f != null)  file_player = f;});
        select_enemy_scenario.setOnAction(e->{File f = load_file(window); if(f != null)  file_enemy = f;});

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Choose scenarios");
        window.setResizable(false);
        window.setWidth(350);
        window.setHeight(200);
        HBox down = new HBox(20,select_player_scenario,select_enemy_scenario);
        Button ok = new Button("Ok!");
        ok.setOnAction(e -> {
            window.close();
            Pane temp;
            if(load && (temp = setup()) != null) stage.setScene(new Scene(temp));
        });

        down.setAlignment(Pos.CENTER);
        VBox all = new VBox(30,down, ok);
        all.setAlignment(Pos.CENTER);
        Scene scene = new Scene(all,300,250);
        window.setScene(scene);
        window.showAndWait();
    }

    private File load_file(Stage win){
        //triggers FileChooser that accepts only .txt files
        //returns the selected txt file
        FileChooser fil_chooser = new FileChooser();
        fil_chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        fil_chooser.setTitle("Select File");
        fil_chooser.setInitialDirectory(new File("scenarios/"));

        return fil_chooser.showOpenDialog(win);
    }

    private void handle_exit(){
        boolean answer = AlertBox.handle_exit("Exit","Are you sure you want to leave?");
        if(answer){
            stage.close();
        }
    }

    private Menu Details(){
        Menu det = new Menu("Details");

        MenuItem enemy_ships = new MenuItem("Enemy ships");
        MenuItem enemy_shots = new MenuItem("Enemy shots");
        MenuItem player_shots = new MenuItem("Player Shots");

        enemy_ships.setOnAction(e -> EnumBox.display(enemy_board.getShips(),"Enemy Ships","Here are enemy's ships:"));
        enemy_shots.setOnAction(e -> EnumBox.display(enemy.getLast_five(), "Enemy shots","Here are enemy's last five shots (most recent are shown last):"));
        player_shots.setOnAction(e -> EnumBox.display(player.getLast_five(), "Your shots","Here are your last five shots (most recent are shown last):"));

        det.getItems().addAll(enemy_ships,enemy_shots,player_shots);
        return det;
    }

    private Menu Help(){
        Menu help = new Menu("Help");
        MenuItem rules = new MenuItem("Rules");
        help.getItems().add(rules);
        rules.setOnAction(e -> show_help());
        return help;
    }

    private void show_help(){
        Stage window = new Stage();
        window.getIcons().add(question_mark);

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Help");
        window.setResizable(false);

        Background backGround = new Background(new BackgroundImage((table),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, false)));
        VBox all = new VBox(10);
        all.setBackground(backGround);
        all.setAlignment(Pos.CENTER);
        Scene scene = new Scene(all,400,300);
        window.setScene(scene);
        window.setWidth(400);
        window.setHeight(300);
        window.showAndWait();
    }
}
