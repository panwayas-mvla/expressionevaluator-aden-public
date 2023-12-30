
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * The Class ExpressionEvaluatorGUI.
 */
// TODO: Auto-generated Javadoc
public class ExpressionEvaluatorGUI extends Application {
	
	/** The exp eval. */
	private ExpressionEvaluator expEval;
	
	/** The eval pane. */
	private BorderPane evalPane;
    
    /** The eval results. */
    private GridPane evalResults;	
	
	/** The eval label. */
	private Label evalLabel;
	
	/** The expression. */
	private TextField expression;
	
	/** The output results. */
	private Text outputResults;
	
	/** The evaluate. */
	private Button evaluate;
	
	/** The btn box. */
	private HBox btnBox;
	
	/**
	 * Instantiates a new expression evaluator GUI.
	 */
	public ExpressionEvaluatorGUI() {
		expEval = new ExpressionEvaluator();
	}
	
	/**
	 * Inits the Labels, Text, and TextField objects.
	 * Defines the event handler to clear the output results if mouse
	 * is clicked in the expression TextField
	 */
	private void initLabels_Text_TextFields() {
		evalLabel = new Label("Enter an Expression:");
		evalLabel.setFont(Font.font("Cambria Math",18));
	    outputResults = new Text(0,0,"");
	    outputResults.setFont(Font.font("Cambria Math",18));
		expression = new TextField();
		expressionHandler();
		expression.setOnMouseClicked(e -> outputResults.setText(""));
		expression.setPrefWidth(600);
		expression.setFont(Font.font("Cambria Math",18));	
	}
	
	/**
	 * Expression handler. Creates a ChangeListener to detect new values being typed
	 * into the expression TextField character by character. If the character is not a valid 
	 * digit, space or operation, it will be rejected, the user will be notified, and the prior value 
	 * of the text field is restored.
	 */
	private void expressionHandler() {
		expression.textProperty().addListener(new ChangeListener<String>() { 
			@Override
			public void changed (ObservableValue<? extends String> obs, String oldV, String newV) {
				outputResults.setText("");
				if (!newV.equals("")) {
					if (!newV.matches("[\\d\\.\\s\\(\\+\\-\\*\\/\\\\^)]+")) {
						expression.setText(oldV);
						outputResults.setText("Character "+
						                       newV.substring(newV.length()-1)+
						                       " is not part of a valid expression");
					}
				}
			}
			
		});
	}
	
	/**
	 * Inits the button and adds to an HBox. Includes the event handler to
	 * actually call the expression evaluator when the Evaluate button is pressed.
	 */
	private void initButtons_HBox() {
		evaluate = new Button("Evaluate");
		evaluate.setOnAction(e -> outputResults.setText(expEval.evaluateExpression(expression.getText())));

		btnBox = new HBox(15);
		btnBox.getChildren().add(evaluate);
		HBox.setMargin(evaluate, new Insets(15,15,15,15));
		btnBox.setAlignment(Pos.CENTER);
	}
	
	/**
	 * Inits the grid pane and populates it.
	 */
	private void initGridPane() {
		evalResults = new GridPane();
		evalResults.add(expression,0,0);
	    evalResults.add(new Label(""), 0, 1);
		evalResults.add(outputResults,0,2);
		GridPane.setHalignment(outputResults, HPos.CENTER);		
	}
	
	/**
	 * Start.
	 *
	 * @param primaryStage the primary stage
	 * @throws Exception the exception
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		initLabels_Text_TextFields();
		initButtons_HBox();
		initGridPane();	
		evalPane = new BorderPane();
		evalPane.setTop(evalLabel);
		evalPane.setCenter(evalResults);
		evalPane.setBottom(btnBox);		
		Scene scene = new Scene(evalPane, 600,150);
		primaryStage.setTitle("Expression Evaluator");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		Application.launch(args);

	}

}
