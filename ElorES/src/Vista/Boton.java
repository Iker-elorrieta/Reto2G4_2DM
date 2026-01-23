package Vista;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Boton extends JButton {
	private static final long serialVersionUID = 1L;
	private int radio = 25;
    private Color colorOriginal;
    private Rectangle tamañoOriginal;

    public Boton(String texto) {
        super(texto);

        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);

        setForeground(Color.BLACK);

        // HOVER
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                colorOriginal = getBackground();
                tamañoOriginal = getBounds();

                // Color un poco más oscuro
                setBackground(colorOriginal.darker());

                // Agrandar ligeramente
                setBounds(
                    tamañoOriginal.x - 5,
                    tamañoOriginal.y - 3,
                    tamañoOriginal.width + 10,
                    tamañoOriginal.height + 6
                );
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(colorOriginal);
                setBounds(tamañoOriginal);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radio, radio);

        super.paintComponent(g);
        g2.dispose();
    }
}
