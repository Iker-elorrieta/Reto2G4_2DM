package Vista;


import java.awt.Image;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Controlador.Controlador;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPasswordField;


public class Login extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField tfcorreo;
    private JLabel lblError;
    private JButton btnAcceder;
    
    private JPasswordField tfcontraseña;
    
    Controlador controlador = new Controlador(this);


    /**
     * Create the frame.
     */
    public Login(Socket cliente, DataInputStream dis, DataOutputStream dos) {
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

        tfcorreo = new JTextField();
        tfcorreo.setColumns(10);
        tfcorreo.setBounds(182, 224, 361, 31);
        contentPane.add(tfcorreo);

        JLabel lblCorreo = new JLabel("Correo:");
        lblCorreo.setForeground(new Color(255, 255, 255));
        lblCorreo.setBounds(80, 232, 59, 14);
        contentPane.add(lblCorreo);

        JLabel lblContraseña = new JLabel("Contraseña:");
        lblContraseña.setForeground(new Color(255, 255, 255));
        lblContraseña.setBounds(80, 293, 92, 14);
        contentPane.add(lblContraseña);

        btnAcceder = new JButton("Acceder");
        btnAcceder.setBounds(319, 345, 89, 23);
        contentPane.add(btnAcceder);
        
        lblError = new JLabel("");
        lblError.setForeground(Color.RED);
        lblError.setBounds(274, 379, 224, 14);
        contentPane.add(lblError);
        
        lblError.setText("");
        
        // EL FONDO
        JLabel lblFondo = new JLabel("");
        lblFondo.setBounds(0, 0, 724, 458);
        lblFondo.setIcon(new ImageIcon("fotos/background.png"));
        contentPane.add(lblFondo);
        
        tfcontraseña = new JPasswordField();
        tfcontraseña.setBounds(182, 290, 361, 31);
        contentPane.add(tfcontraseña);
        
        
        btnAcceder.addActionListener(e -> {
        	 int id = controlador.validarUsuario(cliente, dis, dos);
             if(id != -1 ) {
                 this.dispose();
             }
        });
       
    }

	public JTextField getTfcorreo() {
		return tfcorreo;
	}


	public void setTfcorreo(JTextField tfcorreo) {
		this.tfcorreo = tfcorreo;
	}


	public JPasswordField getTfcontraseña() {
		return tfcontraseña;
	}


	public void setTfcontraseña(JPasswordField tfcontraseña) {
		this.tfcontraseña = tfcontraseña;
	}

	public JLabel getLblError() {
		return lblError;
	}

	public void setLblError(JLabel lblError) {
		this.lblError = lblError;
	}

	public JButton getBtnAcceder() {
		return btnAcceder;
	}

	public void setBtnAcceder(JButton btnAcceder) {
		this.btnAcceder = btnAcceder;
	}
    
    
	
       


}
