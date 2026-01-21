package Vista;

import java.awt.Color;
import java.awt.Font;
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

public class GestionReuniones extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	
	public GestionReuniones(Socket cliente, DataInputStream dis, DataOutputStream dos, int id) {
		setTitle("Gestionar Reuniones");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 706, 485);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// EL LOGO
        JLabel lblLogo = new JLabel();
        lblLogo.setBounds(287, 31, 120, 120);
        ImageIcon icono = new ImageIcon("fotos/logo.png");
        Image imagen = icono.getImage().getScaledInstance(
                lblLogo.getWidth(),
                lblLogo.getHeight(),
                Image.SCALE_SMOOTH
        );        lblLogo.setIcon(new ImageIcon(imagen));
        contentPane.add(lblLogo);
        
        // BOTON SALIR
        JButton btnLogin = new JButton("");
        btnLogin.setIcon(new ImageIcon("fotos/salir.png"));
        btnLogin.setBounds(632, 11, 45, 45);
        btnLogin.setContentAreaFilled(false); // para que no se vea el fondo del botón
        btnLogin.setBorderPainted(false);
        btnLogin.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Login login = new Login(cliente, dis, dos);
        		login.setVisible(true);   // MOSTRAR login
                dispose();
        	}
        });
        contentPane.add(btnLogin);
        
        // BOTON VOLVER
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
        
        // BOTON CREAR REUNIONES
        Boton btnCrear = new Boton("Crear Reuniones");
        btnCrear.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		CrearReu crearreuniones = new CrearReu(cliente, dis, dos, id);
        		crearreuniones.setVisible(true);
        		dispose();
        	}
        });
        btnCrear.setBounds(243, 190, 223, 40);
        btnCrear.setBackground(new Color(232, 220, 202));
        btnCrear.setForeground(Color.BLACK);
        btnCrear.setFont(new Font("Segoe UI", Font.BOLD, 14));
        contentPane.add(btnCrear);

        
        // BOTON VER REUNIONES
        Boton btnVer = new Boton("Ver Mis Reuniones");
        btnVer.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		ConsultarReu consultarReu = new ConsultarReu(cliente, dis, dos, id);
        		consultarReu.setVisible(true);
        		dispose();
        	}
        });
        btnVer.setBounds(243, 272, 223, 40);
        btnVer.setBackground(new Color(232, 220, 202));
        btnVer.setForeground(Color.BLACK);
        btnVer.setFont(new Font("Segoe UI", Font.BOLD, 14));
        contentPane.add(btnVer);
   
        
        // EL FONDO
        JLabel lblFondo = new JLabel("");
        lblFondo.setBounds(0, 0, 724, 458); 
        lblFondo.setIcon(new ImageIcon("fotos/background.png"));
        contentPane.add(lblFondo);
	}
}
