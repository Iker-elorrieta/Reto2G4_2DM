package Vista;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Controlador.Controlador;

public class GestionReuniones extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;


    public GestionReuniones(Controlador controlador, int idProfe) {

        controlador.setConsultarReu(null); // para despues
        setTitle("Gestionar Reuniones");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 706, 485);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // LOGO
        JLabel lblLogo = new JLabel();
        lblLogo.setBounds(287, 31, 120, 120);
        ImageIcon icono = new ImageIcon("fotos/logo.png");
        Image imagen = icono.getImage().getScaledInstance(
                lblLogo.getWidth(),
                lblLogo.getHeight(),
                Image.SCALE_SMOOTH
        );
        lblLogo.setIcon(new ImageIcon(imagen));
        contentPane.add(lblLogo);

        // BOTÓN SALIR
        JButton btnSalir = new JButton("");
        btnSalir.setIcon(new ImageIcon("fotos/salir.png"));
        btnSalir.setBounds(632, 11, 45, 45);
        btnSalir.setContentAreaFilled(false);
        btnSalir.setBorderPainted(false);
        btnSalir.addActionListener(e -> {
            Login login = new Login(controlador);
            login.setVisible(true);
            dispose();
        });
        contentPane.add(btnSalir);

        // BOTÓN VOLVER
        JButton btnVolver = new JButton("");
        btnVolver.setIcon(new ImageIcon("fotos/volver.png"));
        btnVolver.setBounds(10, 11, 45, 45);
        btnVolver.setContentAreaFilled(false);
        btnVolver.setBorderPainted(false);
        btnVolver.addActionListener(e -> {
            MenuProfe menu = new MenuProfe(controlador, idProfe);
            menu.setVisible(true);
            dispose();
        });
        contentPane.add(btnVolver);

        // BOTÓN CREAR REUNIONES
        JButton btnCrear = new JButton("Crear Reuniones");
        btnCrear.setBounds(243, 190, 223, 40);
        btnCrear.setBackground(new Color(232, 220, 202));
        btnCrear.setForeground(Color.BLACK);
        btnCrear.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCrear.addActionListener(e -> {
            CrearReu crear = new CrearReu(controlador, idProfe);
            crear.setVisible(true);
            dispose();
        });
        contentPane.add(btnCrear);

        // BOTÓN VER REUNIONES
        JButton btnVer = new JButton("Ver Mis Reuniones");
        btnVer.setBounds(243, 272, 223, 40);
        btnVer.setBackground(new Color(232, 220, 202));
        btnVer.setForeground(Color.BLACK);
        btnVer.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnVer.addActionListener(e -> {
            ConsultarReu ventana = new ConsultarReu(controlador, idProfe);
            ventana.setVisible(true);
            dispose();
        });
        contentPane.add(btnVer);

        // FONDO
        JLabel lblFondo = new JLabel("");
        lblFondo.setBounds(0, 0, 724, 458);
        lblFondo.setIcon(new ImageIcon("fotos/background.png"));
        contentPane.add(lblFondo);
    }
}
