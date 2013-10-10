package freedomFighter;
import javax.swing.JFrame;
import java.awt.Dimension;
public class TheGame {
	public static final Dimension dimension = new Dimension(1280, 720);
	public static JFrame jFrame = new JFrame();
	public static void main(String[] args) {
        jFrame.setSize((int)dimension.getHeight(), (int)dimension.getWidth());   
        jFrame.setTitle("Freedom Fighter");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        WorldEngine we = new WorldEngine();
        jFrame.add(we);
        
        jFrame.setResizable(false);   
        jFrame.getContentPane().setPreferredSize(dimension);
        jFrame.pack(); 
        
        jFrame.setVisible(true);
	}

}
  