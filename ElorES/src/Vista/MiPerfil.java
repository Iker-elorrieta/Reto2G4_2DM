package Vista;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


import Controlador.Controlador;

public class MiPerfil extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;


	// Labels a nivel de clase
	private JLabel lblLogo;
	private JLabel lblEmail;
	private JLabel lblUsername;
	private JLabel lblNombre;
	private JLabel lblApellidos;
	private JLabel lblDNI;
	private JLabel lblDireccion;
	private JLabel lblTelefono;
	private JLabel lblFoto;
	private JLabel lblFondo;
	
	Controlador controlador = new Controlador(this);

	public MiPerfil(Socket cliente, DataInputStream dis, DataOutputStream dos, int id) {

		setTitle("Mi Perfil");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 534);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// EL LOGO
		lblLogo = new JLabel();
		lblLogo.setBounds(51, 0, 85, 89);
		ImageIcon icono = new ImageIcon("fotos/logo.png");
		Image imagen = icono.getImage().getScaledInstance(
		        lblLogo.getWidth(),
		        lblLogo.getHeight(),
		        Image.SCALE_SMOOTH
		);        
		lblLogo.setIcon(new ImageIcon(imagen));
		contentPane.add(lblLogo);

		// Botón volver
		JButton btnVolver = new JButton("");
		btnVolver.setIcon(new ImageIcon("fotos/volver.png"));
		btnVolver.setBounds(10, 11, 45, 45);
		btnVolver.setContentAreaFilled(false);
		btnVolver.setBorderPainted(false);
		btnVolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MenuProfe menu = new MenuProfe(cliente, dis, dos, id);
				menu.setVisible(true);
				try {
					dos.writeUTF("33");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				dispose();
			}
		});
		contentPane.add(btnVolver);
		
		// Botón salir
		JButton btnLogin = new JButton("");
		btnLogin.setIcon(new ImageIcon("fotos/salir.png"));
		btnLogin.setBounds(729, 11, 45, 45);
		btnLogin.setContentAreaFilled(false);
		btnLogin.setBorderPainted(false);
		
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Login login = new Login(cliente, dis, dos);
				try {
					dos.writeUTF("0");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				login.setVisible(true);
				dispose();
			}
		});
		contentPane.add(btnLogin);

		// Panel de usuario
		JPanel panel = new JPanel();
		panel.setBounds(227, 36, 311, 428);
		contentPane.add(panel);
		panel.setLayout(null);

		lblFoto = new JLabel("");
		lblFoto.setBounds(118, 42, 60, 60);
		lblFoto.setIcon(new ImageIcon("fotos/profesor1.png"));
		panel.add(lblFoto);

		lblEmail = new JLabel("Email:");
		lblEmail.setBounds(28, 42, 273, 14);
		panel.add(lblEmail);

		lblUsername = new JLabel("Username:");
		lblUsername.setBounds(28, 77, 273, 14);
		panel.add(lblUsername);

		lblNombre = new JLabel("Nombre:");
		lblNombre.setBounds(28, 113, 273, 14);
		panel.add(lblNombre);

		lblApellidos = new JLabel("Apellidos:");
		lblApellidos.setBounds(28, 152, 273, 14);
		panel.add(lblApellidos);

		lblDNI = new JLabel("DNI:");
		lblDNI.setBounds(28, 199, 273, 14);
		panel.add(lblDNI);

		lblDireccion = new JLabel("Dirección: ");
		lblDireccion.setBounds(28, 245, 273, 14);
		panel.add(lblDireccion);

		lblTelefono = new JLabel("Teléfono:");
		lblTelefono.setBounds(28, 290, 273, 14);
		panel.add(lblTelefono);
		
		// Fondo
		lblFondo = new JLabel("");
		lblFondo.setBounds(0, 0, 800, 534);
		lblFondo.setIcon(new ImageIcon("fotos/backgroundGRANDE.png"));
		contentPane.add(lblFondo);
		
		controlador.cargarDatosPerfil(dis,dos, id);		
	}

	public JLabel getLblLogo() {
		return lblLogo;
	}

	public void setLblLogo(JLabel lblLogo) {
		this.lblLogo = lblLogo;
	}

	public JLabel getLblEmail() {
		return lblEmail;
	}

	public void setLblEmail(JLabel lblEmail) {
		this.lblEmail = lblEmail;
	}

	public JLabel getLblUsername() {
		return lblUsername;
	}

	public void setLblUsername(JLabel lblUsername) {
		this.lblUsername = lblUsername;
	}

	public JLabel getLblNombre() {
		return lblNombre;
	}

	public void setLblNombre(JLabel lblNombre) {
		this.lblNombre = lblNombre;
	}

	public JLabel getLblApellidos() {
		return lblApellidos;
	}

	public void setLblApellidos(JLabel lblApellidos) {
		this.lblApellidos = lblApellidos;
	}

	public JLabel getLblDNI() {
		return lblDNI;
	}

	public void setLblDNI(JLabel lblDNI) {
		this.lblDNI = lblDNI;
	}

	public JLabel getLblDireccion() {
		return lblDireccion;
	}

	public void setLblDireccion(JLabel lblDireccion) {
		this.lblDireccion = lblDireccion;
	}

	public JLabel getLblTelefono() {
		return lblTelefono;
	}

	public void setLblTelefono(JLabel lblTelefono) {
		this.lblTelefono = lblTelefono;
	}

	public JLabel getLblFoto() {
		return lblFoto;
	}

	public void setLblFoto(JLabel lblFoto) {
		this.lblFoto = lblFoto;
	}

	public JLabel getLblFondo() {
		return lblFondo;
	}

	public void setLblFondo(JLabel lblFondo) {
		this.lblFondo = lblFondo;
	}

	
	
}
