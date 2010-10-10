package csc258comp.debugger;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import csc258comp.runner.Program;


@SuppressWarnings("serial")
final class DebugPanel extends JPanel {
	
	public static final Color UNCHANGED_COLOR = Color.WHITE;
	public static final Color CHANGED_COLOR = new Color(1.0f, 1.0f, 0.5f);
	
	public static final Font monospacedFont = new Font("Monospaced", Font.PLAIN, 12);
	
	
	public final Controller controller;
	
	public final RegisterPanel registerPanel;
	
	public final MachineTableModel tableModel;
	
	
	
	public DebugPanel(DebugMachine m, Program p) {
		if (m == null)
			throw new NullPointerException();
		controller = new Controller(m);
		
		setLayout(new GridBagLayout());
		GridBagConstraints g = new GridBagConstraints();
		g.anchor = GridBagConstraints.WEST;
		g.insets = new Insets(4, 4, 4, 4);
		g.gridwidth = 1;
		g.gridheight = 1;
		
		g.gridx = 0;
		g.gridy = 0;
		g.weightx = 0;
		g.weighty = 0;
		g.fill = GridBagConstraints.NONE;
		registerPanel = new RegisterPanel(this);
		add(registerPanel, g);
		
		g.gridx = 0;
		g.gridy = 1;
		g.weightx = 0;
		g.weighty = 0;
		g.fill = GridBagConstraints.NONE;
		add(new ControlPanel(this), g);
		
		g.gridx = 0;
		g.gridy = 2;
		g.weightx = 1;
		g.weighty = 1;
		g.fill = GridBagConstraints.BOTH;
		tableModel = new MachineTableModel(this, p);
		JTable table = new JTable(tableModel);
		table.setFont(monospacedFont);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		int[] columnWidths = {30, 100, 80, 400};
		for (int i = 0; i < columnWidths.length; i++)
			table.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
		table.setColumnSelectionAllowed(true);
		JScrollPane scrollpane = new JScrollPane(table);
		scrollpane.setPreferredSize(new Dimension(650, 500));
		add(scrollpane, g);
	}
	
	
	
	// Execution handlers
	
	public void updateView() {
		registerPanel.updateView();
		tableModel.updateView();
	}
	
	
	public void beginStep() {
		registerPanel.beginStep();
		tableModel.beginStep();
	}
	
	
	public void endStep() {
		registerPanel.endStep();
		tableModel.endStep();
	}
	
	
	public void beginRun() {
		registerPanel.beginRun();
		tableModel.beginRun();
	}
	
	
	public void endRun() {
		registerPanel.endRun();
		tableModel.endRun();
	}
	
}
