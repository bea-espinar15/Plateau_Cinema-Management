package Presentacion.Gui.Panels.Pase;

import Presentacion.Controller.ApplicationController;
import Presentacion.Controller.Context;
import Presentacion.Controller.ContextEnum;
import Presentacion.Gui.ErrorHandler.EntityEnum;
import Presentacion.Gui.ErrorHandler.ErrorHandlerManager;
import Presentacion.Gui.ErrorHandler.Message;
import Presentacion.Gui.Panels.GeneralPanel;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import java.util.ArrayList;
import java.util.List;
import Negocio.Pase.TPase;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
 * Autores: Nico y Axel
 */

public class ListarPasesPanel extends GeneralPanel {

	private static final long serialVersionUID = 1L;

	private static ListarPasesPanel instance;
	private Panel_Main_Pase parent;
	private JTable pasesTable;
	private ArrayList<TPase> pases;
	private ListarPasesTableModel listarPasesTableModel;
	
	public ListarPasesPanel()
	{
		this.pases = new ArrayList<TPase>();
		initGUI();
	}

	private void initGUI() {
		setLayout(new BorderLayout());
		
		//__________ CABECERA _________
		
		JLabel listarPases = new JLabel("LISTAR PASES");
		Font f = new Font("Monospaced", Font.ITALIC, 40);
		listarPases.setFont(f);
		listarPases.setHorizontalAlignment(SwingConstants.CENTER);
		listarPases.setForeground(Color.DARK_GRAY);
		listarPases.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
		
		this.add(listarPases, BorderLayout.PAGE_START);
		
		//______________ BOTON ____________________
		
		JPanel listarBtnPanel = new JPanel();
		listarBtnPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
		JButton listarBtn = new JButton("Listar");
		listarBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
					Context request = new Context(ContextEnum.LISTARPASES, null);
					ApplicationController.GetInstance().ManageRequest(request);
			}
		});
		
		listarBtnPanel.add(listarBtn);
		
		this.add(listarBtnPanel, BorderLayout.SOUTH);
		
		//_____________ CENTERPANEL ______________
				
		this.add(crearTabla(pases), BorderLayout.CENTER);
			
	}

	public static ListarPasesPanel getInstance() {
		if(instance == null)
		{
			instance = new ListarPasesPanel();
		}
		return instance;
	}


	public void setParent(Panel_Main_Pase parent) {
		this.parent = parent;
	}

	public Component crearTabla(List<TPase> pases) {
		listarPasesTableModel = new ListarPasesTableModel(pases);
		this.pasesTable = new JTable(listarPasesTableModel);
        JScrollPane scroll = new JScrollPane(this.pasesTable);
        scroll.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
		return scroll;
	}

	public void update(Object o) {
		if (hasError(o)) {
			ErrorHandlerManager ehm = ErrorHandlerManager.getInstance();
			Message msg = ehm.getMessage(EntityEnum.PASE, ((ArrayList<TPase>)o).get(0).GetID());
			JOptionPane.showMessageDialog(parent, msg.getText(), msg.getTitle(), JOptionPane.ERROR_MESSAGE);
			clear();
		}
		else {
			parent.showPanel(this);
			pases = (ArrayList<TPase>) o;
			listarPasesTableModel.updateList(pases);
		}
	}

	public boolean hasError(Object response) {
		if(((ArrayList<TPase>)response).size() > 0 && ((ArrayList<TPase>)response).get(0).GetID() < 0)
			return true;
		return false;
	}

	public void clear() {
		listarPasesTableModel.updateList(new ArrayList<TPase>());
	}

	@Override
	public boolean validation() {
		return true;
	}
}