package csc258comp.debugger;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;


@SuppressWarnings("serial")
final class DebugPanel extends JPanel {
	
	static final Color unchangedColor = Color.WHITE;
	static final Color changedColor = new Color(1.0f, 1.0f, 0.5f);
	
	static final Font monospacedFont = new Font("Monospaced", Font.PLAIN, 12);
	
	
	DebugMachine machine;
	
	Controller controller;
	
	RegisterPanel registerPanel;
	
	
	MachineTableModel tableModel;
	
	
	
	public DebugPanel(final DebugMachine machineState) {
		if (machineState == null)
			throw new NullPointerException();
		this.machine = machineState;
		controller = new Controller(machineState);
		
		setLayout(new GridBagLayout());
		GridBagConstraints g = new GridBagConstraints();
		g.anchor = GridBagConstraints.CENTER;
		g.insets = new Insets(4, 4, 4, 4);
		
		g.gridx = 0;
		g.gridy = 0;
		g.weightx = 0;
		g.weighty = 0;
		g.fill = GridBagConstraints.NONE;
		registerPanel = new RegisterPanel(this);
		add(registerPanel, g);
		
		g.gridx = 1;
		g.gridy = 0;
		g.weightx = 0;
		g.weighty = 0;
		g.fill = GridBagConstraints.NONE;
		add(new ControlPanel(this), g);
		
		g.gridx = 2;
		g.gridy = 0;
		g.weightx = 1;
		g.weighty = 0;
		g.fill = GridBagConstraints.HORIZONTAL;
		add(new JPanel(), g);
		
		g.gridx = 0;
		g.gridy = 1;
		g.gridwidth = 3;
		g.weightx = 1;
		g.weighty = 1;
		g.fill = GridBagConstraints.BOTH;
		tableModel = new MachineTableModel(this);
		JTable table = new JTable(tableModel);
		table.setFont(monospacedFont);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		int[] columnWidths = {30, 100, 80, 400};
		for (int i = 0; i < columnWidths.length; i++)
			table.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
		table.setColumnSelectionAllowed(true);
		JScrollPane scrollpane = new JScrollPane(table);
		add(scrollpane, g);
	}
	
}
