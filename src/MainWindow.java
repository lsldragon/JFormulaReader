import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MainWindow extends JPanel implements DropTargetListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	BufferedImage img;

	@Override
	public void paint(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(this.img, 0, 0, this);
	}

	public MainWindow() throws Exception {

		try {
			this.img = ImageIO.read(new File("JFormulaReader.png"));
		} catch (Exception e) {
			this.img = ImageIO.read(getClass().getResourceAsStream("JFormulaReader.png"));
		}

		JFrame frame = new JFrame("JFormulaReader 0.1");
		DropTarget dt = new DropTarget(frame, this);
		setDropTarget(dt);

		frame.setDefaultCloseOperation(3);
		frame.getContentPane().add(this);
		frame.setSize(348, 378);
		frame.setIconImage(ImageIO.read(getClass().getResourceAsStream("icon.png")));
		frame.setResizable(false);
		frame.setBackground(Color.WHITE);
		frame.setLocationRelativeTo(getRootPane());
		frame.setVisible(true);
	}

	public static void main(String[] args) throws Exception {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					new MainWindow();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void dragEnter(DropTargetDragEvent dtde) {
	}

	@Override
	public void dragOver(DropTargetDragEvent dtde) {
		try {

			@SuppressWarnings("unchecked")
			List<File> files = (List<File>) dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
			if (files.size() != 1) {
				dtde.rejectDrag();
				return;
			}
			if (files.get(0).getName().endsWith(".jft") || files.get(0).getName().endsWith(".txt")) {
				dtde.acceptDrag(1); // 0 reject 1 Duplicate 2 Move
			} else {
				dtde.rejectDrag();
			}
		} catch (Exception e) {
		}

	}

	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {
	}

	@Override
	public void dragExit(DropTargetEvent dte) {
	}

	@Override
	public void drop(DropTargetDropEvent dtde) {
		dtde.acceptDrop(2);
		String inFileName = "";

		try {

			@SuppressWarnings("unchecked")
			List<File> files = (List<File>) dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
			File inFile = files.get(0);
			inFileName = inFile.getAbsolutePath();

			try {

				JFrame jFrame = new JFrame("");
				jFrame.add(new RenderFormula(inFileName));

				jFrame.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						jFrame.dispose();
					}
				});

				jFrame.setTitle("Your chosed file is : " + inFileName);
				jFrame.setSize(1200, 350);
				jFrame.setResizable(true);
				jFrame.setVisible(true);

			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (UnsupportedFlavorException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
