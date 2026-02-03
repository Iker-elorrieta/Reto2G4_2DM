package Vista;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Controlador.Controlador;

public class MenuProfe extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;


    public MenuProfe(Controlador controlador, int idProfe) {

        controlador.setMenuProfe(this);

        setTitle("Mi Área de Trabajo");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
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

        Boton btn1 = new Boton("Consultar mis alumnos");
        btn1.setBounds(62, 194, 223, 40);
        btn1.addActionListener(e -> {
            ConsultarAlumnos ventana = new ConsultarAlumnos(controlador, idProfe);
            ventana.setVisible(true);
            dispose();
        });
        contentPane.add(btn1);

        Boton btn2 = new Boton("Consultar mi horario");
        btn2.setBounds(62, 277, 223, 40);
        btn2.addActionListener(e -> {
            MiHorario ventana = new MiHorario(controlador, idProfe);
            ventana.setVisible(true);
            dispose();
        });
        contentPane.add(btn2);

        Boton btn3 = new Boton("Gestionar Reuniones");
        btn3.setBounds(383, 194, 223, 40);
        btn3.addActionListener(e -> {
            GestionReuniones ventana = new GestionReuniones(controlador, idProfe);
            ventana.setVisible(true);
            dispose();
        });
        contentPane.add(btn3);

        Boton btn4 = new Boton("Consultar otros horarios");
        btn4.setBounds(383, 277, 223, 40);
        btn4.addActionListener(e -> {
            OtrosHorarios ventana = new OtrosHorarios(controlador, idProfe);
            ventana.setVisible(true);
            dispose();
        });
        contentPane.add(btn4);

        JButton btnPerfil = new JButton("");
        btnPerfil.setIcon(new ImageIcon("fotos/perfil.png"));
        btnPerfil.setBounds(577, 11, 45, 45);
        btnPerfil.setContentAreaFilled(false);
        btnPerfil.setBorderPainted(false);
        btnPerfil.addActionListener(e -> {
            MiPerfil ventana = new MiPerfil(controlador, idProfe);
            ventana.setVisible(true);
            dispose();
        });
        contentPane.add(btnPerfil);

        JButton btnSalir = new JButton("");
        btnSalir.setIcon(new ImageIcon("fotos/salir.png"));
        btnSalir.setBounds(632, 11, 45, 45);
        btnSalir.setContentAreaFilled(false);
        btnSalir.setBorderPainted(false);
        btnSalir.addActionListener(e -> {
            Login login = new Login(new Controlador());
            login.setVisible(true);
			controlador.cerrarConexion();
            dispose();
        });
        contentPane.add(btnSalir);

        JLabel lblFondo = new JLabel("");
        lblFondo.setBounds(0, 0, 724, 458);
        lblFondo.setIcon(new ImageIcon("fotos/background.png"));
        contentPane.add(lblFondo);
    }
}
