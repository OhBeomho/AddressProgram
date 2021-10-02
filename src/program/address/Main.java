package program.address;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import program.address.datas.Person;
import program.address.datas.TableData;
import program.address.dialogs.AddDialog;
import program.address.dialogs.EditDialog;

public class Main extends Application {
	private static final int WIDTH = 600, HEIGHT = 400;
	private static final String[] PERSON_DATAS = { "이름", "주소", "전화번호", "나이" };
	private static final Main instance = new Main("");
	private Scene scene;
	private StackPane pane;

	private TableView<TableData> personTable;
	private Button addButton, removeButton, editButton, searchButton;
	private TextField searchField;
	private StackPane detailPane;
	private Label nameLabel, addressLabel, telLabel, ageLabel;
	private ChoiceBox<String> searchTypes;
	private AddDialog addDialog;
	private EditDialog editDialog;

	private ArrayList<Person> persons;
	private ObservableList<TableData> tableData, fullTableData;
	private int editingIndex;

	public Main() {
		pane = new StackPane();
		scene = new Scene(pane, WIDTH, HEIGHT);

		persons = new ArrayList<>();
		tableData = FXCollections.observableArrayList();
		fullTableData = FXCollections.observableArrayList();

		personTable = new TableView<>();
		addButton = new Button("추가");
		removeButton = new Button("삭제");
		editButton = new Button("수정");
		searchButton = new Button("검색");
		searchField = new TextField();
		detailPane = new StackPane();
		searchTypes = new ChoiceBox<>(FXCollections.observableArrayList(PERSON_DATAS));
		nameLabel = new Label("이름\n");
		addressLabel = new Label("주소\n");
		telLabel = new Label("전화번호\n");
		ageLabel = new Label("나이\n");
		addDialog = new AddDialog();
		editDialog = new EditDialog();

		loadDatas();
	}

	public Main(String s) {
	}

	@SuppressWarnings("unchecked")
	@Override
	public void start(Stage stage) {
		HBox[] hboxes = { new HBox(), new HBox(), new HBox(), new HBox() };
		VBox vbox = new VBox(), vbox0 = new VBox();

		Label label = new Label("상세 정보");
		label.setStyle("-fx-font-size: 24pt;");

		hboxes[0].getChildren().addAll(addButton, removeButton);
		hboxes[1].getChildren().addAll(searchField, searchTypes, searchButton);
		hboxes[2].getChildren().addAll(personTable, detailPane);
		hboxes[3].getChildren().addAll(hboxes[0], hboxes[1]);
		vbox.getChildren().addAll(hboxes[3], hboxes[2]);
		vbox0.getChildren().addAll(label, nameLabel, addressLabel, telLabel, ageLabel, editButton);
		detailPane.getChildren().add(vbox0);

		for (HBox hbox : hboxes) hbox.setAlignment(Pos.CENTER);

		searchField.setPrefWidth(200);
		searchButton.setPrefWidth(50);
		addButton.setPrefWidth(60);
		removeButton.setPrefWidth(60);
		searchTypes.setPrefWidth(80);
		hboxes[1].setPrefWidth(searchField.getPrefWidth() + searchButton.getPrefWidth() + searchTypes.getPrefWidth());
		hboxes[0].setPrefWidth(addButton.getPrefWidth() + removeButton.getPrefWidth());
		hboxes[3].setPrefHeight(30);
		detailPane.prefWidthProperty().bind(stage.widthProperty().subtract(personTable.prefWidthProperty()));
		personTable.prefWidthProperty().bind(stage.widthProperty().subtract(stage.widthProperty().divide(1.7)));
		detailPane.prefHeightProperty().bind(stage.heightProperty().subtract(hboxes[3].prefHeightProperty().subtract(vbox.spacingProperty())));
		personTable.prefHeightProperty().bind(detailPane.prefHeightProperty());

		hboxes[3].spacingProperty().bind(stage.widthProperty().subtract(hboxes[1].getPrefWidth() + hboxes[0].getPrefWidth()));

		vbox.setAlignment(Pos.CENTER);
		vbox0.setAlignment(Pos.CENTER);
		vbox.setSpacing(10);
		vbox0.setSpacing(10);

		TableColumn<TableData, String> nameColumn = new TableColumn<>("이름");
		TableColumn<TableData, String> ageColumn = new TableColumn<>("전화번호");

		nameColumn.setCellValueFactory(cell -> cell.getValue().getName());
		ageColumn.setCellValueFactory(cell -> cell.getValue().getTel());

		personTable.getColumns().addAll(nameColumn, ageColumn);

		nameColumn.prefWidthProperty().bind(personTable.prefWidthProperty().divide(2));
		ageColumn.prefWidthProperty().bind(personTable.prefWidthProperty().divide(2));

		addButton.setOnAction(e -> addDialog.show());
		removeButton.setOnAction(e -> remove());
		editButton.setOnAction(e -> edit());
		searchButton.setOnAction(e -> search(searchField.getText()));
		personTable.setOnMouseClicked(e -> select());
		searchTypes.getSelectionModel().select(0);
		searchField.setOnAction(e -> search(searchField.getText()));

		Label[] labels = { nameLabel, addressLabel, telLabel, ageLabel };
		for (Label l : labels) l.setTextAlignment(TextAlignment.CENTER);

		pane.getChildren().add(vbox);

		scene.getStylesheets().add(Main.class.getResource("style.css").toString());

		stage.setScene(scene);
		stage.setTitle("Address Program");
		stage.setMinWidth(420);
		stage.setMinHeight(300);
		stage.show();

		instance.persons = persons;
		instance.tableData = tableData;
		instance.fullTableData = fullTableData;
		instance.personTable = personTable;
	}

	private void loadDatas() {
		try (BufferedReader reader = new BufferedReader(new FileReader(Person.PERSONS_FILE))) {
			String data = "";

			while ((data = reader.readLine()) != null) {
				String[] personData = data.split("/");
				String name = personData[0], address = personData[1], tel = personData[2], stringAge = personData[3];
				int age = Integer.parseInt(stringAge);

				TableData newData = new TableData(name, tel);

				persons.add(new Person(name, address, tel, age));
				tableData.add(newData);
				fullTableData.add(newData);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		personTable.setItems(tableData);
	}

	public void add(String data) {
		if (!data.equals("")) {
			String[] personData = data.split("/");
			String name = personData[0], address = personData[1], tel = personData[2], stringAge = personData[3];
			int age = Integer.parseInt(stringAge);

			TableData newData = new TableData(name, tel);

			persons.add(new Person(name, address, tel, age));
			tableData.add(newData);
			fullTableData.add(newData);

			try (BufferedWriter writer = new BufferedWriter(new FileWriter(Person.PERSONS_FILE, true))) {
				writer.write(data + "\n");
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();

				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("오류");
				alert.setHeaderText("사람을 추가(파일에 저장)하는 과정에서\n오류가 발생하였습니다.");
				alert.setContentText("파일에 저장되었는지 확인해 주세요.\n파일 경로: " + Person.PERSONS_FILE.getPath());
				alert.show();
			}
		}
	}

	private void edit() {
		int index = personTable.getSelectionModel().getSelectedIndex();
		TableData selectedData = personTable.getSelectionModel().getSelectedItem();
		String data = "";

		if (index == -1) return;

		for (Person person : persons) {
			if (person.getTel().equals(selectedData.getTel().get())) {
				data = person.getName() + "/" + person.getAddress() + "/" + person.getTel() + "/" + person.getAge();
				break;
			}
		}

		editingIndex = index;

		editDialog.show(data);
	}

	public void setData(String newData) {
		String[] personData = newData.split("/");
		String name = personData[0], address = personData[1], tel = personData[2], stringAge = personData[3];
		int age = Integer.parseInt(stringAge);

		TableData editedData = new TableData(name, tel);

		editingIndex++;

		persons.remove(editingIndex);
		persons.add(editingIndex, new Person(name, address, tel, age));
		tableData.remove(editingIndex);
		tableData.add(editingIndex, editedData);
		fullTableData.remove(editingIndex);
		fullTableData.add(editingIndex, editedData);

		editingIndex = 0;
	}

	private void remove() {
		int index = personTable.getSelectionModel().getSelectedIndex();

		if (index == -1) return;

		tableData.remove(index);
		persons.remove(index);
		fullTableData.remove(index);

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(Person.PERSONS_FILE))) {
			for (Person p : persons) writer.write(p.getName() + "/" + p.getAddress() + "/" + p.getTel() + "/" + p.getAge() + "\n");

			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void select() {
		TableData selectedData = personTable.getSelectionModel().getSelectedItem();

		if (selectedData == null) return;

		if (!(nameLabel.getText().length() == 3)) {
			String name = nameLabel.getText().substring(2);

			if (name.equals(selectedData.getName().get())) return;
			else {
				nameLabel.setText("이름\n");
				addressLabel.setText("주소\n");
				telLabel.setText("전화번호\n");
				ageLabel.setText("나이\n");
			}
		}

		for (Person p : persons) {
			if (selectedData.getName().get().equals(p.getName()) && selectedData.getTel().get().equals(p.getTel())) {
				nameLabel.setText(nameLabel.getText() + p.getName());
				addressLabel.setText(addressLabel.getText() + p.getAddress());
				telLabel.setText(telLabel.getText() + p.getTel());
				ageLabel.setText(ageLabel.getText() + p.getAge() + "세");
				return;
			}
		}

		nameLabel.setText("이름\n");
		addressLabel.setText("주소\n");
		telLabel.setText("전화번호\n");
		ageLabel.setText("나이\n");
	}

	private void search(String searchData) {
		if (searchData.length() == 0) {
			tableData.setAll(fullTableData);
			return;
		}

		String searchType = searchTypes.getSelectionModel().getSelectedItem();
		Person[] foundPersons = new Person[persons.size()];
		int count = 0;

		switch (searchType) {
		case "이름":
			for (Person p : persons) {
				if (p.getName().contains(searchData)) {
					foundPersons[count] = p;
					count++;
				}
			}
			break;
		case "주소":
			for (Person p : persons) {
				if (p.getAddress().contains(searchData)) {
					foundPersons[count] = p;
					count++;
				}
			}
			break;
		case "전화번호":
			for (Person p : persons) {
				if (p.getTel().contains(searchData)) {
					foundPersons[count] = p;
					count++;
				}
			}
			break;
		case "나이":
			for (Person p : persons) {
				if (String.valueOf(p.getAge()).contains(searchData)) {
					foundPersons[count] = p;
					count++;
				}
			}
			break;
		}

		tableData.clear();

		count = 0;
		for (TableData t : fullTableData) {
			if (foundPersons[count] == null) break;

			if (foundPersons[count].getTel().equals(t.getTel().get())) {
				tableData.add(t);
				count++;
			}
		}
	}

	public static Main getInstance() {
		return instance;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
