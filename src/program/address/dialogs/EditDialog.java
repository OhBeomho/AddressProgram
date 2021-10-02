package program.address.dialogs;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import program.address.Main;

public class EditDialog extends Stage {
	private Scene scene;
	private StackPane pane;
	private TextField nameField, addressField, telField, ageField;
	private Button okButton, cancelButton;

	public EditDialog() {
		pane = new StackPane();
		scene = new Scene(pane, 300, 200);
		nameField = new TextField();
		addressField = new TextField();
		telField = new TextField();
		ageField = new TextField();
		okButton = new Button("확인");
		cancelButton = new Button("취소");

		setUI();
	}

	private void setUI() {
		HBox[] hboxes = { new HBox(), new HBox(), new HBox(), new HBox(), new HBox() };
		VBox vbox = new VBox();

		hboxes[0].getChildren().addAll(new Label("이름"), nameField);
		hboxes[1].getChildren().addAll(new Label("주소"), addressField);
		hboxes[2].getChildren().addAll(new Label("전화번호"), telField);
		hboxes[3].getChildren().addAll(new Label("나이"), ageField);
		hboxes[4].getChildren().addAll(okButton, cancelButton);

		vbox.getChildren().addAll(hboxes);

		nameField.setPromptText("이름을 입력해 주세요.");
		addressField.setPromptText("주소를 입력해 주세요.");
		telField.setPromptText("전화번호를 입력해 주세요.");
		ageField.setPromptText("나이를 입력해 주세요.");

		okButton.setOnAction(e -> {
			Main.getInstance().setData(nameField.getText() + "/" + addressField.getText() + "/" + telField.getText() + "/" + ageField.getText());
			hide();
		});
		cancelButton.setOnAction(e -> {
			nameField.clear();
			addressField.clear();
			telField.clear();
			ageField.clear();
			hide();
		});

		for (HBox hbox : hboxes) {
			hbox.setAlignment(Pos.CENTER);
			hbox.setSpacing(10);
		}

		pane.getChildren().add(vbox);
		pane.prefWidth(getWidth());
		pane.prefHeight(getHeight());
		pane.getStylesheets().add(Main.class.getResource("style.css").toString());

		setTitle("수정");
		setScene(scene);
	}

	public void show(String data) {
		String[] datas = data.split("/");
		String name = datas[0], address = datas[1], tel = datas[2], age = datas[3];

		nameField.setText(name);
		addressField.setText(address);
		telField.setText(tel);
		ageField.setText(age);

		show();
	}
}
