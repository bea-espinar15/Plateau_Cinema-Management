
package Presentacion.Controller;

import Presentacion.Command.Command;
import Presentacion.Command.CommandFactory;


public class ApplicationController {
	
	private static ApplicationController Instance;	
	
	public static ApplicationController GetInstance() {
		if (Instance == null) 
			Instance = new ApplicationController();
		return Instance;
	}
	
	public void ManageRequest(Context context) {
		Command command = CommandFactory.GetInstance().GetCommand(context.getContext());
		Dispatcher.getInstance().Dispatch(command.Execute(context.getData()));
	}
	
}