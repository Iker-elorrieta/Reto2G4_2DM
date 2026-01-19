package Principal;

import java.awt.Image;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MiHorario extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;


    private DataInputStream dis;
    private DataOutputStream dos;

    private JTable table;
    private DefaultTableModel modelo;

    public MiHorario(Socket cliente, DataInputStream dis, DataOutputStream dos, int id) {
        this.dis = dis;
        this.dos = dos;
        setTitle("Mi Horario");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 534);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // LOGO
        JLabel lblLogo = new JLabel();
        lblLogo.setBounds(332, 11, 120, 120);
        ImageIcon icono = new ImageIcon("fotos/logo.png");
        Image imagen = icono.getImage().getScaledInstance(
                lblLogo.getWidth(),
                lblLogo.getHeight(),
                Image.SCALE_SMOOTH
        );
        lblLogo.setIcon(new ImageIcon(imagen));
        contentPane.add(lblLogo);

        // BOTÓN SALIR
        JButton btnLogin = new JButton("");
        btnLogin.setIcon(new ImageIcon("fotos/salir.png"));
        btnLogin.setBounds(729, 11, 45, 45);
        btnLogin.setContentAreaFilled(false);
        btnLogin.setBorderPainted(false);
        btnLogin.addActionListener(e -> {
            Login login = new Login();
            login.setVisible(true);
            try {
				dos.writeUTF("0");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
            dispose();
        });
        contentPane.add(btnLogin);

        // BOTÓN VOLVER
        JButton btnVolver = new JButton("");
        btnVolver.setIcon(new ImageIcon("fotos/volver.png"));
        btnVolver.setBounds(10, 11, 45, 45);
        btnVolver.setContentAreaFilled(false);
        btnVolver.setBorderPainted(false);
        btnVolver.addActionListener(e -> {
            MenuProfe menu = new MenuProfe(cliente, dis, dos, id);
            menu.setVisible(true);
            dispose();
        });
        contentPane.add(btnVolver);

        // TABLA
        String[] columnas = {"Hora", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes"};
        modelo = new DefaultTableModel(columnas, 6);

        // Rellenar horas
        for (int i = 0; i < 6; i++) {
            modelo.setValueAt("Hora " + (i + 1), i, 0);
        }

        table = new JTable(modelo);
        table.setEnabled(false);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 142, 754, 330);
        contentPane.add(scrollPane);

        // FONDO
        JLabel lblFondo = new JLabel("");
        lblFondo.setBounds(0, 0, 800, 534);
        lblFondo.setIcon(new ImageIcon("fotos/backgroundGRANDE.png"));
        contentPane.add(lblFondo);

        // CARGAR DATOS EN HILO SEPARADO
        cargarDatos();
    }

    private void cargarDatos() {
        new Thread(() -> {
            try {
                dos.writeUTF("3");
                dos.flush();

                String json = dis.readUTF();

                Gson gson = new Gson();
                ArrayList<Map<String, Object>> listaHorarios = gson.fromJson(
                        json,
                        new TypeToken<ArrayList<Map<String, Object>>>(){}.getType()
                );
                

                SwingUtilities.invokeLater(() -> {
                    rellenarTabla(listaHorarios);
                });


            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void rellenarTabla(ArrayList<Map<String, Object>> lista) {
    	
    

        for (Map<String, Object> horario : lista) {

        
        	
            String hora = (String.valueOf(horario.get("hora"))); // 1–6
            String dia = (String) horario.get("dia");
            String asignatura = (String) horario.get("modulos");

            Double horaInt = Double.parseDouble(hora);
            int fila = horaInt.intValue() - 1;
            int columna = switch (dia.toLowerCase()) {
                case "lunes" -> 1;
                case "martes" -> 2;
                case "miercoles" -> 3;
                case "jueves" -> 4;
                case "viernes" -> 5;
                default -> -1;
            };

            if (columna != -1) {
                modelo.setValueAt(asignatura, fila, columna);
            }
        }
    }
}
