package Principal;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

public class ConsultarAlumnos extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private DefaultTableModel model;

    private DataInputStream dis;
    private DataOutputStream dos;

    public ConsultarAlumnos(Socket cliente, DataInputStream dis, DataOutputStream dos, int idProfe) {
        this.dis = dis;
        this.dos = dos;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 534);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // LOGO
        JLabel lblLogo = new JLabel();
        lblLogo.setBounds(327, 44, 120, 120);
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
                cliente.close();
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
            MenuProfe menu = new MenuProfe(cliente, dis, dos, idProfe);
            menu.setVisible(true);
            dispose();
        });
        contentPane.add(btnVolver);

        // SCROLL Y TABLA
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(102, 197, 580, 236);
        contentPane.add(scrollPane);

        String[] columnas = {"ID", "DNI", "Nombre", "Apellidos"};

        model = new DefaultTableModel(columnas, 0) {
 
			private static final long serialVersionUID = 1L;

			@Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        scrollPane.setViewportView(table);

        // OCULTAR COLUMNA ID
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);

        cargarDatos();

        // SELECCIÓN DE FILA
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = table.getSelectedRow();
                if (fila != -1) {
                    int idAlumno = Integer.parseInt(table.getValueAt(fila, 0).toString());

                    DetalleAlumno ficha = new DetalleAlumno(cliente, dis, dos, idProfe, idAlumno);
                    ficha.setVisible(true);
                    dispose();
                }
            } 
        });

        // FONDO
        JLabel lblFondo = new JLabel("");
        lblFondo.setBounds(0, 0, 800, 534);
        lblFondo.setIcon(new ImageIcon("fotos/backgroundGRANDE.png"));
        contentPane.add(lblFondo);
    }

    public void cargarDatos() {
        try {
            dos.writeUTF("2");
            dos.flush();

            String json = dis.readUTF();

            Gson gson = new Gson();
            ArrayList<Map<String, Object>> listaAlumnos = gson.fromJson(
                    json,
                    new TypeToken<ArrayList<Map<String, Object>>>() {}.getType()
            );

            model.setRowCount(0);

            for (Map<String, Object> alumno : listaAlumnos) {
            	int idAlumno = ((Double) alumno.get("id")).intValue();
            	String dni = alumno.get("dni").toString();
                String nombre = alumno.get("nombre").toString();
                String apellidos = alumno.get("apellidos").toString();

                model.addRow(new Object[]{idAlumno, dni, nombre, apellidos});
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
