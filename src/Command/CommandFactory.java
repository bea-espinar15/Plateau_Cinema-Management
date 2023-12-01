
package Presentacion.Command;

import java.util.HashMap;

import Presentacion.Controller.ContextEnum;


public abstract class CommandFactory {
	
	private static CommandFactory Instance;
	protected static HashMap<ContextEnum, Command> map;
	
	public static CommandFactory GetInstance() {
		if(Instance == null){
			Instance = new CommandFactoryImp();
			map = new HashMap<>();
		}
		return Instance;
	}

	public abstract Command GetCommand(ContextEnum contextEnum);
	
}