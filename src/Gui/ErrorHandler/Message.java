package Presentacion.Gui.ErrorHandler;

public class Message {
	private String title;
	private String text;
	
	public Message(String text, String title) {
		this.title = title;
		this.text = text;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public String getText() {
		return this.text;
	}
}
