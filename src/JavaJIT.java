import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import parser.Parser;
import parser.ParserResult;
import tokens.Token;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

public class JavaJIT extends Application {
	
	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		AnchorPane ap = new AnchorPane();
		ap.setPrefSize(600, 400);
		
		Button b = new Button("Open main java file to convert");
		b.setLayoutX(10);
		b.setLayoutY(10);
		b.setPrefSize(280, 20);
		b.setOnAction(event -> {
			FileChooser fd = new FileChooser();
			fd.setTitle("Choose the main java file.");
			fd.getExtensionFilters().add(new ExtensionFilter("Java source files (*.java)", "*.java"));
			File opened = fd.showOpenDialog(primaryStage);
			if (opened == null){
				return;
			}
			System.out.println(opened.getAbsolutePath());
			try {
				ParserResult p = Parser.parse(opened.getAbsolutePath());
				System.out.println("Package id: " + p.packageId);
				System.out.println("Imports: ");
				for (String i : p.imports) {
					System.out.println(i);
				}
				System.out.println("Code:");
				for (Token i : p.contents) {
					System.out.println(i.toString());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		ap.getChildren().add(b);

		TextArea t = new TextArea();
		t.setLayoutX(10);
		t.setLayoutY(40);
		t.setPrefSize(580, 350);
		ap.getChildren().add(t);
		
		primaryStage.setScene(new Scene(ap));
		primaryStage.setTitle("Java -> C Compiler");
		primaryStage.show();
		
	}

}
