package Principal;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class ConsultarReu extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblLogo;
    private JLabel lblFondo;
    private JButton btnVolver;
    private JButton btnLogin;

	/**
	 * Create the frame.
	 * @param id 
	 * @param dos 
	 * @param dis 
	 * @param cliente 
	 */
	public ConsultarReu(Socket cliente, DataInputStream dis, DataOutputStream dos, int id) {
		setTitle("Consultar mis reuniones");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 534);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// LOGO
        lblLogo = new JLabel();
        lblLogo.setBounds(327, 44, 120, 120);
        ImageIcon icono = new ImageIcon("fotos/logo.png");
        Image imagen = icono.getImage().getScaledInstance(
                lblLogo.getWidth(),
                lblLogo.getHeight(),
                Image.SCALE_SMOOTH
        );
        lblLogo.setIcon(new ImageIcon(imagen));
        contentPane.add(lblLogo);
        
        // Botón volver
        btnVolver = new JButton("");
		btnVolver.setIcon(new ImageIcon("fotos/volver.png"));
		btnVolver.setBounds(10, 11, 45, 45);
		btnVolver.setContentAreaFilled(false);
		btnVolver.setBorderPainted(false);
		btnVolver.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		GestionReuniones gestionReuniones = new GestionReuniones(cliente, dis, dos, id);
        		gestionReuniones.setVisible(true);
        		dispose();
        	}
        });
		contentPane.add(btnVolver);
		
		// Botón salir
		btnLogin = new JButton("");
		btnLogin.setIcon(new ImageIcon("fotos/salir.png"));
		btnLogin.setBounds(729, 11, 45, 45);
		btnLogin.setContentAreaFilled(false);
        btnLogin.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Login login = new Login();
        		login.setVisible(true);   // MOSTRAR login
                dispose();
        	}
        });
		btnLogin.setBorderPainted(false);
		contentPane.add(btnLogin);
        
        // FONDO
        lblFondo = new JLabel("");
        lblFondo.setBounds(0, 0, 800, 534);
        lblFondo.setIcon(new ImageIcon("fotos/backgroundGRANDE.png"));
        contentPane.add(lblFondo);

	}

}
