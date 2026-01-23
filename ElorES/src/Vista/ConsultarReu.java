package Vista;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import Controlador.Controlador;

public class ConsultarReu extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    private JLabel lblLogo;
    private JLabel lblFondo;

    private JButton btnVolver;
    private JButton btnPendientes;

    private JTable table;
    private DefaultTableModel modelo;

    public ConsultarReu(Controlador controlador, int idProfe) {

        controlador.setConsultarReu(this);

        setTitle("Consultar mis reuniones");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 534);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // LOGO
        lblLogo = new JLabel();
        lblLogo.setBounds(327, 11, 120, 120);
        ImageIcon icono = new ImageIcon("fotos/logo.png");
        Image imagen = icono.getImage().getScaledInstance(
                lblLogo.getWidth(),
                lblLogo.getHeight(),
                Image.SCALE_SMOOTH
        );
        lblLogo.setIcon(new ImageIcon(imagen));
        contentPane.add(lblLogo);

        // BOTÓN VOLVER
        btnVolver = new JButton("");
        btnVolver.setIcon(new ImageIcon("fotos/volver.png"));
        btnVolver.setBounds(10, 11, 45, 45);
        btnVolver.setContentAreaFilled(false);
        btnVolver.setBorderPainted(false);
        btnVolver.addActionListener(e -> {
            GestionReuniones ventana = new GestionReuniones(controlador, idProfe);
            ventana.setVisible(true);
            dispose();
        });
        contentPane.add(btnVolver);
        
        //BOTON PENDIENTES
       btnPendientes = new JButton("Pendientes");
       btnPendientes.setBounds(652, 32, 109, 36);
       btnPendientes.setBackground(new Color(232, 220, 202));
       btnPendientes.setForeground(Color.BLACK);
       btnPendientes.setFont(new Font("Segoe UI", Font.BOLD, 14));
       contentPane.add(btnPendientes);
       btnPendientes.addActionListener(e -> {
    	   GestionPendientes pendientes = new GestionPendientes(controlador, idProfe);
    	   pendientes.setVisible(true);
    	   this.dispose();
       });
       

     // TABLA DEL HORARIO
        String[] columnas = {"Hora", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes"};
        modelo = new DefaultTableModel(columnas, 6);

        // Rellenar columna de horas
        for (int i = 0; i < 6; i++) {
            modelo.setValueAt("Hora " + (i + 1), i, 0);
        }

        table = new JTable(modelo);
        table.setEnabled(false);
        
        
        table.setDefaultRenderer(Object.class, new javax.swing.table.TableCellRenderer() {

            DefaultTableCellRenderer base = new DefaultTableCellRenderer();

            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {

                Component c = base.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                c.setForeground(Color.BLACK);
                c.setBackground(Color.WHITE);

                if (value != null && value.toString().startsWith("Reunión")) {

                    String texto = value.toString(); 
                    String id = texto.replace("Reunión", "").split("con")[0].trim(); 

                    String estado = controlador.getEstadoReunion("Reunion" + id);

                    if (estado != null) {
                        if (estado.equals("pendiente")) {
                            c.setBackground(new Color(255, 255, 150)); 
                        }
                        else if (estado.equals("aceptada")) {
                            c.setBackground(new Color(150, 255, 150));
                        }
                        else if (estado.equals("denegada")) {
                            c.setBackground(new Color(255, 120, 120)); 
                        }
                        else if (estado.equals("conflicto")) {
                            c.setBackground(new Color(180, 180, 180)); 
                        }
                    }
                }

                return c;
            }
        });


        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 142, 754, 330);
        contentPane.add(scrollPane);

        // CARGAR DATOS DESDE EL CONTROLADOR
        
        controlador.cargarHorarioReuniones();
        controlador.CargarReuniones();
        
        // FONDO
        lblFondo = new JLabel("");
        lblFondo.setBounds(0, 0, 800, 534);
        lblFondo.setIcon(new ImageIcon("fotos/backgroundGRANDE.png"));
        contentPane.add(lblFondo);
    }

	public DefaultTableModel getModelo() {
		return modelo;
	}

	public JTable getTable() {
		return table;
	}
}
