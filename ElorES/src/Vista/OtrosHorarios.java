package Vista;

import java.awt.Component;
import java.awt.Image;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import Controlador.Controlador;
import modelo.Users;

public class OtrosHorarios extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    private JComboBox<String> cbProfesores;
    private JTable table;
    private DefaultTableModel modelo;

    public OtrosHorarios(Controlador controlador, int idProfe) {

        controlador.setOtrosHorarios(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 534);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // LOGO
        JLabel lblLogo = new JLabel();
        lblLogo.setBounds(380, 30, 140, 140);
        ImageIcon icono = new ImageIcon("fotos/logo.png");
        Image imagen = icono.getImage().getScaledInstance(
                lblLogo.getWidth(),
                lblLogo.getHeight(),
                Image.SCALE_SMOOTH
        );
        lblLogo.setIcon(new ImageIcon(imagen));
        contentPane.add(lblLogo);

        // BOTÓN VOLVER
        JButton btnVolver = new JButton("");
        btnVolver.setIcon(new ImageIcon("fotos/volver.png"));
        btnVolver.setBounds(10, 41, 45, 45);
        btnVolver.setContentAreaFilled(false);
        btnVolver.setBorderPainted(false);
        btnVolver.addActionListener(e -> {
            MenuProfe menu = new MenuProfe(controlador, idProfe);
            menu.setVisible(true);
            dispose();
        });
        contentPane.add(btnVolver);

        // COMBOBOX PROFESORES
        cbProfesores = new JComboBox<>();
        cbProfesores.setBounds(20, 100, 200, 30);
        contentPane.add(cbProfesores);

        // TABLA
        String[] columnas = {"Hora", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes"};
        modelo = new DefaultTableModel(columnas, 6) {
			private static final long serialVersionUID = 1L;

			@Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (int i = 0; i < 6; i++) {
            modelo.setValueAt("Hora " + (i + 1), i, 0);
        }

        table = new JTable(modelo);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setRowHeight(40);
        table.setFont(table.getFont().deriveFont(14f));
        table.setEnabled(false);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 181, 620, 320);
        contentPane.add(scrollPane);

        // CARGAR PROFESORES
        controlador.otrosHorarios();

  
        cbProfesores.addActionListener(e -> {
            if (cbProfesores.getSelectedItem() == null) return;

            String nombreVisible = cbProfesores.getSelectedItem().toString();
            Users profesor = controlador.getMapaProfesores().get(nombreVisible);

            if (profesor != null) {
                controlador.cargarHorarioDeProfesor(profesor);
                ajustarAlturaFilas();
                ajustarAnchoColumnas();
            }
        });

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

    public JComboBox<String> getCbProfesores() { 
        return cbProfesores; 
    }

    public DefaultTableModel getModelo() { 
        return modelo; 
    }
}
