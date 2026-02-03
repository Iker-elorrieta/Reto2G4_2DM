package Vista;

import java.awt.Image;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Controlador.Controlador;

public class Login extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    private JTextField tfcorreo;
    private JPasswordField tfcontraseña;
    private JLabel lblError;


    public Login(Controlador controlador) {

        controlador.setLogin(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 706, 485);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

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
        tfcorreo.setBounds(182, 224, 361, 31);
        contentPane.add(tfcorreo);

        tfcontraseña = new JPasswordField();
        tfcontraseña.setBounds(182, 290, 361, 31);
        contentPane.add(tfcontraseña);

        JLabel lblCorreo = new JLabel("Correo:");
        lblCorreo.setForeground(java.awt.Color.WHITE);
        lblCorreo.setBounds(80, 232, 92, 14);
        contentPane.add(lblCorreo);

        JLabel lblContraseña = new JLabel("Contraseña:");
        lblContraseña.setForeground(java.awt.Color.WHITE);
        lblContraseña.setBounds(80, 293, 92, 14);
        contentPane.add(lblContraseña);

        Boton btnAcceder = new Boton("Acceder");
        btnAcceder.setBounds(319, 345, 89, 23);
        btnAcceder.addActionListener(e -> {
            int id = controlador.validarUsuario();
            if (id != -1) {
                MenuProfe menu = new MenuProfe(controlador, id);
                menu.setVisible(true);
                dispose();
            }
        });
        contentPane.add(btnAcceder);

        lblError = new JLabel("");
        lblError.setForeground(java.awt.Color.RED);
        lblError.setBounds(274, 379, 224, 14);
        contentPane.add(lblError);

        JLabel lblFondo = new JLabel("");
        lblFondo.setBounds(0, 0, 724, 458);
        lblFondo.setIcon(new ImageIcon("fotos/background.png"));
        contentPane.add(lblFondo);
    }

    public JTextField getTfcorreo() { return tfcorreo; }
    public JPasswordField getTfcontraseña() { return tfcontraseña; }
    public JLabel getLblError() { return lblError; }
}
