package Vista;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.awt.event.ActionEvent;

public class MenuProfe extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	



	/**
	 * Create the frame.
	 */
	public MenuProfe(Socket cliente, DataInputStream dis, DataOutputStream dos, int id) {
		setTitle("Mi Area De Trabajo");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 706, 485);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// EL LOGO
        JLabel lblLogo = new JLabel();
        lblLogo.setBounds(286, 46, 120, 120);
		ImageIcon icono = new ImageIcon("fotos/logo.png");
        Image imagen = icono.getImage().getScaledInstance(
                lblLogo.getWidth(),
                lblLogo.getHeight(),
                Image.SCALE_SMOOTH
        );
        lblLogo.setIcon(new ImageIcon(imagen));
        contentPane.add(lblLogo);
        
        // CONSULTAR ALUMNOS
        Boton btn1 = new Boton("Consultar mis alumnos");
        btn1.setBounds(62, 194, 223, 40);
        btn1.setBackground(new Color(232, 220, 202));
        btn1.setForeground(Color.BLACK);
        btn1.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConsultarAlumnos consultarAlumnos = new ConsultarAlumnos(cliente, dis, dos, id);
				consultarAlumnos.setVisible(true);   // MOSTRAR ConsultarAlumnos
				dispose();
			}
		});
        contentPane.add(btn1);
        
        // CONSULTAR HORARIO
        Boton btn2 = new Boton("Consultar mi horario");
        btn2.setBounds(62, 277, 223, 40);
        btn2.setBackground(new Color(232, 220, 202));
        btn2.setForeground(Color.BLACK);
        btn2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MiHorario miHorario = new MiHorario(cliente, dis, dos, id);
				miHorario.setVisible(true);   // MOSTRAR MiHorario
				dispose();
			}
        });
        contentPane.add(btn2);
        
        // GESTIONAR REUNIONES
        Boton btn3 = new Boton("Gestionar Reuniones");
        btn3.setBounds(383, 194, 223, 40);
        btn3.setBackground(new Color(232, 220, 202));
        btn3.setForeground(Color.BLACK);
        btn3.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GestionReuniones gestionarReuniones = new GestionReuniones(cliente, dis, dos, id);
				gestionarReuniones.setVisible(true);   // MOSTRAR GestionarReuniones
				dispose();
			}
        });
        contentPane.add(btn3);
        
        // CONSULTAR OTROS HORARIOS
        Boton btn4 = new Boton("Consultar otros horarios");
        btn4.setBounds(383, 277, 223, 40);
        btn4.setBackground(new Color(232, 220, 202));
        btn4.setForeground(Color.BLACK);
        btn4.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OtrosHorarios otrosHorarios = new OtrosHorarios(cliente, dis, dos, id);
				otrosHorarios.setVisible(true);   // MOSTRAR OtrosHorarios
				dispose();
			}
        });
        contentPane.add(btn4);
        
        // VER MI PERFIL
        JButton btn5 = new JButton("");
        btn5.setIcon(new ImageIcon("fotos/perfil.png"));
        btn5.setBounds(577, 11, 45, 45);
        btn5.setContentAreaFilled(false);
        btn5.setBorderPainted(false);
        btn5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MiPerfil miPerfil = new MiPerfil(cliente, dis, dos, id);
				miPerfil.setVisible(true);   // MOSTRAR MiPerfil
				dispose();
			}
		});
        contentPane.add(btn5);
        
        // BOTON SALIR
        JButton btn6 = new JButton("");
        btn6.setIcon(new ImageIcon("fotos/salir.png"));
        btn6.setBounds(632, 11, 45, 45);
        btn6.setContentAreaFilled(false); // para que no se vea el fondo del botón
        btn6.setBorderPainted(false);
        btn6.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Login login = new Login(cliente, dis, dos);
        		login.setVisible(true);   // MOSTRAR login
        		try {
					cliente.close();
				} catch (IOException e1) {
					e1.printStackTrace();
					System.out.println("Error al cerrar el socket en MenuProfe");
				}
                dispose();
        	}
        });
        contentPane.add(btn6);

        // EL FONDO
        JLabel lblFondo = new JLabel("");
        lblFondo.setBounds(0, 0, 724, 458);
        lblFondo.setIcon(new ImageIcon("fotos/background.png"));
        contentPane.add(lblFondo);
        
        
	}

}
