package Vista;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import Controlador.Controlador;

public class MiHorario extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    private JTable table;
    private DefaultTableModel modelo;


    public MiHorario(Controlador controlador, int idProfe) {

        controlador.setMiHorario(this);

        setTitle("Mi Horario");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 800, 534);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

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

        // TABLA DEL HORARIO
        String[] columnas = {"Hora", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes"};
        modelo = new DefaultTableModel(columnas, 6);

        // Rellenar columna de horas
        for (int i = 0; i < 6; i++) {
            modelo.setValueAt("Hora " + (i + 1), i, 0);
        }

        table = new JTable(modelo) {
            private static final long serialVersionUID = 1L;

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);

                // Colores
                Color azulClaro = new Color(173, 216, 230); // light blue
                Color crema = new Color(255, 253, 208);     // cream
                Color azulEncabezado = new Color(70, 130, 180); // steel blue

                // Encabezado
                if (row == -1) {
                    c.setBackground(azulEncabezado);
                    c.setForeground(Color.WHITE);
                } else {
                    // Alternar colores por fila
                    if (row % 2 == 0) {
                        c.setBackground(azulClaro);
                    } else {
                        c.setBackground(crema);
                    }
                    c.setForeground(Color.BLACK);
                }

                // Selección (opcional)
                if (isCellSelected(row, column)) {
                    c.setBackground(new Color(100, 149, 237)); // cornflower blue
                    c.setForeground(Color.WHITE);
                }

                return c;
            }
        };
        table.setEnabled(false);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 142, 754, 330);
        contentPane.add(scrollPane);

        // CARGAR DATOS DESDE EL CONTROLADOR
        controlador.cargarHorario(idProfe);
        
        ajustarAlturaFilas();
        ajustarAnchoColumnas();
        
        JButton btnSalir = new JButton("");
        btnSalir.setIcon(new ImageIcon("fotos/salir.png"));
        btnSalir.setBounds(729, 11, 45, 45);
        btnSalir.setContentAreaFilled(false);
        btnSalir.setBorderPainted(false);
        btnSalir.addActionListener(e -> {
            Login login = new Login(new Controlador());
            login.setVisible(true);
			controlador.cerrarConexion();
            dispose();
        });
        contentPane.add(btnSalir);

        // FONDO
        JLabel lblFondo = new JLabel("");
        lblFondo.setBounds(0, 0, 800, 534);
        lblFondo.setIcon(new ImageIcon("fotos/backgroundGRANDE.png"));
        contentPane.add(lblFondo);
    }

    
    public void ajustarAlturaFilas() {
        for (int row = 0; row < table.getRowCount(); row++) {
            int maxHeight = 30;

            for (int column = 0; column < table.getColumnCount(); column++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                maxHeight = Math.max(comp.getPreferredSize().height + 6, maxHeight);
            }

            table.setRowHeight(row, maxHeight);
        }
    }

    public void ajustarAnchoColumnas() {
        for (int col = 0; col < table.getColumnCount(); col++) {
            int maxWidth = 70;

            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, col);
                Component comp = table.prepareRenderer(renderer, row, col);
                maxWidth = Math.max(comp.getPreferredSize().width + 15, maxWidth);
            }

            table.getColumnModel().getColumn(col).setPreferredWidth(maxWidth);
        }
    }


    
    public DefaultTableModel getModelo() {
        return modelo;
    }
}
