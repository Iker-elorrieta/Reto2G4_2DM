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

public class OtrosHorarios extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	
	public OtrosHorarios(Socket cliente, DataInputStream dis, DataOutputStream dos, int id) {
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 534);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// EL LOGO
        JLabel lblLogo = new JLabel();
        lblLogo.setBounds(327, 44, 120, 120);
        ImageIcon icono = new ImageIcon("fotos/logo.png");
        Image imagen = icono.getImage().getScaledInstance(
                lblLogo.getWidth(),
                lblLogo.getHeight(),
                Image.SCALE_SMOOTH
        );        lblLogo.setIcon(new ImageIcon(imagen));
        contentPane.add(lblLogo);
        
        JButton btnLogin = new JButton("");
        btnLogin.setIcon(new ImageIcon("fotos/salir.png"));
        btnLogin.setBounds(729, 11, 45, 45);
        btnLogin.setContentAreaFilled(false); // para que no se vea el fondo del botón
        btnLogin.setBorderPainted(false);
        btnLogin.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Login login = new Login();
        		login.setVisible(true);   // MOSTRAR login
                dispose();
        	}
        });
        contentPane.add(btnLogin);
        
        JButton btnVolver = new JButton("");
        btnVolver.setIcon(new ImageIcon("fotos/volver.png"));
        btnVolver.setBounds(10, 11, 45, 45);
        btnVolver.setContentAreaFilled(false); // para que no se vea el fondo del botón
        btnVolver.setBorderPainted(false);
        btnVolver.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		MenuProfe menu = new MenuProfe(cliente, dis, dos, id);
        		menu.setVisible(true);   // MOSTRAR menu
				dispose();
        	}
        });
        contentPane.add(btnVolver);
        
     // EL FONDO
        JLabel lblFondo = new JLabel("");
        lblFondo.setBounds(0, 0, 800, 534);
        lblFondo.setIcon(new ImageIcon("fotos/backgroundGRANDE.png"));
        contentPane.add(lblFondo);
	}

}
