package program.address.datas;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TableData {
	private StringProperty name, tel;

	public TableData(String name, String tel) {
		this.name = new SimpleStringProperty(name);
		this.tel = new SimpleStringProperty(tel);
	}

	public StringProperty getName() {
		return name;
	}

	public void setName(String name) {
		this.name = new SimpleStringProperty(name);
	}

	public StringProperty getTel() {
		return tel;
	}

	public void setTel(StringProperty tel) {
		this.tel = tel;
	}
}
