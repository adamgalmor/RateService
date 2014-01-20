package il.ac.shenkar;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;

public class GUI implements ChangeListener, ActionListener {
    public static void main(String[] args) {
	new GUI();
    }

    public GUI() {
	window = new JFrame();
	window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	JMenuBar menuBar = new JMenuBar();
	JMenu menu = new JMenu("File");
	JMenuItem item = new JMenuItem("Quit");
	item.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		window.dispose();
	    }
	});
	menu.add(item);
	menuBar.add(menu);
	menu = new JMenu("Backend");
	final JRadioButtonMenuItem jRadioBtnMI = new JRadioButtonMenuItem(
		"Java");
	final JRadioButtonMenuItem sRadioBtnMI = new JRadioButtonMenuItem(
		"Scala");
	jRadioBtnMI.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		if (service instanceof ScalaRateService)
		    service = new JavaRateService();
		sRadioBtnMI.setSelected(false);
		jRadioBtnMI.setSelected(true);
	    }
	});
	sRadioBtnMI.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		if (service instanceof JavaRateService)
		    service = new ScalaRateService();
		jRadioBtnMI.setSelected(false);
		sRadioBtnMI.setSelected(true);
	    }
	});
	menu.add(jRadioBtnMI);
	menu.add(sRadioBtnMI);
	menuBar.add(menu);
	menuBar.add(Box.createGlue());
	JMenu help = new JMenu("Help");
	help.setMnemonic('H');
	item = new JMenuItem("About");
	item.setMnemonic('A');
	item.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		JOptionPane
			.showMessageDialog(
				null,
				"<html><center>"
					+ "<h1>Final Project - Currency Exchange Application</h1><br/><br/>"
					+ "Adam Galmor &lt;adamgalmor@gmail.com&gt;<br/>"
					+ "Omer Goshen &lt;gershon@goosemoose.com&gt;<br/>"
					+ "Java-Scala Autumn 2011<br/>"
					+ "Shenkar College of Eng. Ramat-Gan, Israel.<br/>"
					+ "</center></html>",
				"About Currency Exchanger",
				JOptionPane.PLAIN_MESSAGE);
	    }
	});
	help.add(item);
	menuBar.add(help);

	// Java Rate Service by default
	jRadioBtnMI.setSelected(true);
	service = new JavaRateService();

	SwingUtilities.invokeLater(new Runnable() {
	    @Override
	    public void run() {
		try {
		    model = new CurrencyTableModel(service.GetAllRates());
		    table.setModel(model);
		} catch (Exception ex) {
		    JOptionPane.showMessageDialog(window, ex.toString());
		}
	    }
	});
	table = new JTable(model);

	JPanel p = new JPanel();
	String[] keys = RateService.currencyCodes.keySet().toArray(
		new String[0]);
	srcCurr = new JComboBox<String>(keys);
	destCurr = new JComboBox<String>(keys);

	SpinnerNumberModel snm = new SpinnerNumberModel(0.0, 0.0,
		Double.MAX_VALUE, 0.001);
	spinner = new JSpinner(snm);
	spinner.setFont(new Font("Courier", Font.PLAIN, 16));
	spinner.setPreferredSize(new Dimension(100,
		spinner.getPreferredSize().height));
	tf = new JTextField("", 10);
	tf.setFont(new Font("Courier", Font.PLAIN, 16));
	tf.setEditable(false);
	tf.setBackground(SystemColor.text);
	p.add(spinner);
	p.add(srcCurr);
	JButton btn = new JButton("<-->");
	btn.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		int temp = srcCurr.getSelectedIndex();
		srcCurr.setSelectedIndex(destCurr.getSelectedIndex());
		destCurr.setSelectedIndex(temp);
	    }
	});
	p.add(btn);
	p.add(destCurr);
	p.add(tf);

	spinner.addChangeListener(this);
	srcCurr.addActionListener(this);
	destCurr.addActionListener(this);

	window.setJMenuBar(menuBar);

	Box box = Box.createVerticalBox();
	box.add(table.getTableHeader(), BorderLayout.NORTH);
	box.add(table);
	window.add(box, BorderLayout.NORTH);
	p.setBorder(new LineBorder(SystemColor.controlShadow));
	window.add(p, BorderLayout.SOUTH);

	window.setPreferredSize(new Dimension(640, 480));
	window.setMinimumSize(new Dimension(640, 480));
	window.pack();
	window.setVisible(true);
    }

    public double convert(String source, String dest, double amount)
	    throws Exception {
	Currency sCur = service.GetRate(source);
	Currency dCur = service.GetRate(dest);
	return amount * (sCur.rate / dCur.rate)
		* ((float) dCur.unit / sCur.unit);
    }

    public void update() {
	String srcVal = (String) srcCurr.getSelectedItem();
	String destVal = (String) destCurr.getSelectedItem();
	double spinVal = Double.parseDouble(spinner.getValue().toString());
	try {
	    double result = convert(srcVal, destVal, spinVal);
	    tf.setText(Double.toString(result));
	} catch (Exception e) {
	    JOptionPane.showMessageDialog(window, e.toString());
	}
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	update();
    }

    @Override
    public void stateChanged(ChangeEvent arg0) {
	update();
    }

    class CurrencyTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;

	protected String[] columnNames = { "Name", "Unit", "CurrencyCode",
		"Country", "Rate", "Change" };

	protected Currency[] data;

	public CurrencyTableModel(Currency[] currencies) {
	    data = currencies;
	}

	@Override
	public int getColumnCount() {
	    return columnNames.length;
	}

	@Override
	public String getColumnName(int column) {
	    return columnNames[column];
	}

	@Override
	public int getRowCount() {
	    return data.length;
	}

	@Override
	public Object getValueAt(int arg0, int arg1) {
	    Currency c = data[arg0];
	    switch (arg1) {
	    case 0:
		return c.name;
	    case 1:
		return c.unit;
	    case 2:
		return c.currencyCode;
	    case 3:
		return c.country;
	    case 4:
		return c.rate;
	    case 5:
		return c.change;
	    }
	    return null;
	}

    }

    private JComboBox<String> srcCurr;
    private JComboBox<String> destCurr;
    private JSpinner spinner;
    private JTextField tf;

    private JFrame window;
    private JTable table;
    private CurrencyTableModel model;

    private RateService service;
}
