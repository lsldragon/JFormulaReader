import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

public class RenderFormula extends JPanel {

	private static final long serialVersionUID = 1L;
	private String filepath;
	private StringBuffer filecontent = new StringBuffer();

	private TeXFormula formula;
	private TeXIcon icon;
	private JLabel jLabel;

	private static int i = 1;
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	public RenderFormula(String filepath) throws Exception {

		this.filepath = filepath;

		this.setLayout(new BorderLayout(0, 0));

		JButton savaAsPng = new JButton("Save as .png");
		savaAsPng.setFont(new Font("Arial", Font.PLAIN, 30));
		savaAsPng.setBackground(Color.WHITE);
		savaAsPng.setForeground(Color.MAGENTA);
		savaAsPng.setIcon(new ImageIcon(getClass().getResource("save1.png")));
		savaAsPng.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				savaAsPng();
			}
		});

		add(savaAsPng, BorderLayout.SOUTH);

		BufferedReader bReader = new BufferedReader(new FileReader(filepath));
		String line = null;

		while ((line = bReader.readLine()) != null) {
			filecontent.append(line + LINE_SEPARATOR);
		}
		bReader.close();
	}

	protected void savaAsPng() {

		try {
			generate(formula, icon, i + "_" + "formula.png");
			JOptionPane.showMessageDialog(this, "Sava as png Done", "Done", JOptionPane.INFORMATION_MESSAGE);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "ERROR: " + e.getMessage(), "Failed", JOptionPane.ERROR_MESSAGE);
		}

		i++;
	}

	public void generate(TeXFormula formula, TeXIcon icon, String path) throws Exception {

		icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 40);
		BufferedImage bimg = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(),
				BufferedImage.TYPE_4BYTE_ABGR);

		Graphics2D g2d = bimg.createGraphics();

		jLabel.setForeground(new Color(0, 0, 0));
		icon.paintIcon(jLabel, g2d, 0, 0);

		File out = new File(path);
		ImageIO.write(bimg, "png", out);

	}

	@Override
	public void paint(Graphics g) {

		try {

			formula = new TeXFormula(new String(filecontent));

			icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 40);
			icon.setInsets(new Insets(5, 5, 5, 5));

			jLabel = new JLabel();
			jLabel.setForeground(new Color(0, 0, 0));

			icon.paintIcon(jLabel, g, 0, 0);

			filecontent.delete(0, filecontent.length());
			g.dispose();

		} catch (Exception e) {
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					JOptionPane.showMessageDialog(getRootPane(), "ERROR: " + e.getMessage(), "Failed",
							JOptionPane.ERROR_MESSAGE);
				}
			});

		}
	}

}
