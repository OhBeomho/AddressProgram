module AddressProgram {
	requires javafx.controls;
	requires javafx.base;
	
	opens program.address to javafx.graphics, javafx.fxml;
}
