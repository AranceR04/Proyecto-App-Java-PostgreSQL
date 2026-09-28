package Visual;

import com.formdev.flatlaf.FlatDarkLaf;
import Consultas.VehiculoDB;
import Clase.Vehiculo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class VentanaPrincipal extends JFrame {
    private JPanel cards;
    private CardLayout cardLayout;
    private JLabel lblReloj;
    private JPanel gridCoches;
    private VehiculoDB consultas = new VehiculoDB();
    private List<Vehiculo> listaVehiculos;

    //para filtrar y ordenar
    private JCheckBox chkGasolina;
    private JCheckBox chkHibrido;
    private JCheckBox chkDiesel;
    private JCheckBox chkElectrico;
    private JSlider sliderPrecio;
    private JLabel lblPrecioMax;
    private JComboBox<String> comboOrden;

    public VentanaPrincipal() {
        FlatDarkLaf.setup();
        setTitle("ARANCE RENT-A-CAR | Gestión de Flota Premium");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        iniciarReloj();
        crearPantallaMenu();
        crearPantallaCatalogo();

        add(cards);
    }

    //reloj con formato y visible en verde
    private void iniciarReloj() {
        lblReloj = new JLabel();
        lblReloj.setFont(new Font("Monospaced", Font.BOLD, 18));
        lblReloj.setForeground(new Color(0, 255, 150));
        new Timer(1000, e -> {
            lblReloj.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        }).start();
    }

    //primera pantalla
    private void crearPantallaMenu() {
        //creamos un panel dividido en 2 columnas
        JPanel menu = new JPanel(new GridLayout(1, 2));
        menu.setBackground(new Color(33, 33, 33));

        JPanel panelIzquierdo = new JPanel(new GridBagLayout());
        panelIzquierdo.setBackground(new Color(38, 38, 38));

        JPanel contenedorTextos = new JPanel();
        contenedorTextos.setLayout(new BoxLayout(contenedorTextos, BoxLayout.Y_AXIS));
        contenedorTextos.setOpaque(false);

        JLabel lblSubSuperior = new JLabel("ARANCE-RENT-A-CAR");
        lblSubSuperior.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblSubSuperior.setForeground(new Color(200, 200, 200));
        lblSubSuperior.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenedorTextos.add(lblSubSuperior);
        contenedorTextos.add(Box.createRigidArea(new Dimension(0, 40)));

        JLabel lblTitulo1 = new JLabel("ALQUILER DE");
        lblTitulo1.setFont(new Font("Segoe UI", Font.BOLD, 64));
        lblTitulo1.setForeground(new Color(230, 190, 90));
        lblTitulo1.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitulo2 = new JLabel("VEHÍCULOS");
        lblTitulo2.setFont(new Font("Segoe UI", Font.BOLD, 64));
        lblTitulo2.setForeground(new Color(230, 190, 90));
        lblTitulo2.setAlignmentX(Component.LEFT_ALIGNMENT);

        contenedorTextos.add(lblTitulo1);
        contenedorTextos.add(lblTitulo2);
        contenedorTextos.add(Box.createRigidArea(new Dimension(0, 40)));

        JLabel lblSubInferior = new JLabel("LOS MEJORES VEHÍCULOS AL ALCANCE DE TU MANO");
        lblSubInferior.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblSubInferior.setForeground(Color.WHITE);
        lblSubInferior.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenedorTextos.add(lblSubInferior);
        contenedorTextos.add(Box.createRigidArea(new Dimension(0, 80)));

        JButton btnEntrar = new JButton("EXPLORAR CATÁLOGO");
        btnEntrar.setPreferredSize(new Dimension(340, 65));
        btnEntrar.setMaximumSize(new Dimension(340, 65));
        btnEntrar.setBackground(new Color(0, 102, 204));
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnEntrar.setCursor(new Cursor(Cursor.HAND_CURSOR)); //transformar el raton en una mano al pasar por encima
        btnEntrar.setAlignmentX(Component.LEFT_ALIGNMENT);

        //boton redondito
        btnEntrar.putClientProperty("FlatLaf.style", "arc: 15; borderWidth: 2; borderColor: #ffffff");

        //al pulsarlo inicializa los filtros y va al catalogo
        btnEntrar.addActionListener(e -> {
            chkGasolina.setSelected(true);
            chkHibrido.setSelected(true);
            chkDiesel.setSelected(true);
            chkElectrico.setSelected(true);
            sliderPrecio.setValue(120);
            comboOrden.setSelectedIndex(0);

            refrescarCatalogo();
            cardLayout.show(cards, "CATALOGO");
        });

        contenedorTextos.add(btnEntrar);

        //alineacion y margenes del bloque izquierdo para que no quede pegado a los bordes de la pantalla
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 50, 0, 50);
        gbc.anchor = GridBagConstraints.WEST;
        panelIzquierdo.add(contenedorTextos, gbc);

        URL cocheUrl = getClass().getResource("/fotos/inicio.png");
        JLabel lblFotoPortada = new JLabel("", SwingConstants.CENTER);


        //gestion de error por si la imagen inicial no se muestra
        if (cocheUrl != null) {
            ImageIcon imgOriginal = new ImageIcon(cocheUrl);
            //listener que detecta si la ventana cambia de tamaño
            lblFotoPortada.addComponentListener(new java.awt.event.ComponentAdapter() {
                @Override
                public void componentResized(java.awt.event.ComponentEvent e) {
                    int ancho = lblFotoPortada.getWidth();
                    int alto = lblFotoPortada.getHeight();
                    if (ancho > 0 && alto > 0) {
                        Image imgEscalada = imgOriginal.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
                        lblFotoPortada.setIcon(new ImageIcon(imgEscalada));
                    }
                }
            });
        } else {
            lblFotoPortada.setText("Imagen de portada no encontrada");
            lblFotoPortada.setForeground(Color.GRAY);
        }

        menu.add(panelIzquierdo);
        menu.add(lblFotoPortada);

        cards.add(menu, "MENU");
    }

    //segunda pantalla
    private void crearPantallaCatalogo() {
        JPanel catalogo = new JPanel(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(25, 25, 25));
        header.setBorder(new EmptyBorder(10, 40, 10, 40));

        //menu desplegable
        JMenuBar menuBar = new JMenuBar();
        JMenu menuOpciones = new JMenu("Opciones");

        JMenuItem itemVolver = new JMenuItem("Volver al Menú");
        itemVolver.addActionListener(e -> cardLayout.show(cards, "MENU"));

        JMenuItem itemAyuda = new JMenuItem("Ayuda / Soporte");
        itemAyuda.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Para reservar, filtre por tipo de motor o precio máximo.\nLos vehículos marcados como ALQUILADO no están disponibles temporalmente.",
                "Ayuda del Sistema", JOptionPane.INFORMATION_MESSAGE));

        menuOpciones.add(itemVolver);
        menuOpciones.add(itemAyuda);
        menuBar.add(menuOpciones);

        header.add(menuBar, BorderLayout.WEST);

        JLabel lblTitulo = new JLabel("CATÁLOGO DE VEHÍCULOS", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.add(lblTitulo, BorderLayout.CENTER);
        header.add(lblReloj, BorderLayout.EAST);

        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelFiltros.setBackground(new Color(35, 35, 35));
        panelFiltros.setBorder(new EmptyBorder(10, 10, 10, 10));

        chkGasolina = new JCheckBox("Gasolina", true);
        chkHibrido = new JCheckBox("Híbrido", true);
        chkDiesel = new JCheckBox("Diésel", true);
        chkElectrico = new JCheckBox("Eléctrico", true);

        sliderPrecio = new JSlider(JSlider.HORIZONTAL, 0, 120, 120);
        sliderPrecio.setBackground(new Color(35, 35, 35));
        lblPrecioMax = new JLabel("Precio Máx: 120 €");

        //ComboBox para filtrar por precios
        String[] opcionesOrden = {"Precio: Menor a Mayor", "Precio: Mayor a Menor"};
        comboOrden = new JComboBox<>(opcionesOrden);
        comboOrden.addActionListener(e -> refrescarCatalogo());

        chkGasolina.addActionListener(e -> refrescarCatalogo());
        chkHibrido.addActionListener(e -> refrescarCatalogo());
        chkDiesel.addActionListener(e -> refrescarCatalogo());
        chkElectrico.addActionListener(e -> refrescarCatalogo());

        //slider de la cantidad a gastar
        sliderPrecio.addChangeListener(e -> {
            lblPrecioMax.setText("Precio Máx: " + sliderPrecio.getValue() + " €");
            refrescarCatalogo();
        });

        panelFiltros.add(new JLabel("Combustible:"));
        panelFiltros.add(chkGasolina);
        panelFiltros.add(chkHibrido);
        panelFiltros.add(chkDiesel);
        panelFiltros.add(chkElectrico);
        panelFiltros.add(new JSeparator(SwingConstants.VERTICAL));
        panelFiltros.add(lblPrecioMax);
        panelFiltros.add(sliderPrecio);
        panelFiltros.add(new JSeparator(SwingConstants.VERTICAL));
        panelFiltros.add(new JLabel("Ordenar por:"));
        panelFiltros.add(comboOrden);

        JPanel zonaNorte = new JPanel(new BorderLayout());
        zonaNorte.add(header, BorderLayout.NORTH);
        zonaNorte.add(panelFiltros, BorderLayout.SOUTH);

        gridCoches = new JPanel(new GridLayout(0, 3, 30, 30));
        gridCoches.setBackground(new Color(30, 30, 30));
        gridCoches.setBorder(new EmptyBorder(30, 30, 30, 30));

        //metemos el grid dentro de un jscrollPane para poder hacer scroll vertical si hay muchos coches
        JScrollPane scroll = new JScrollPane(gridCoches);
        scroll.setBorder(null);

        catalogo.add(zonaNorte, BorderLayout.NORTH);
        catalogo.add(scroll, BorderLayout.CENTER);
        cards.add(catalogo, "CATALOGO");
    }

    private void refrescarCatalogo() {
        gridCoches.removeAll();
        listaVehiculos = consultas.obtenerDisponibles();

        if (comboOrden.getSelectedIndex() == 0) {
            listaVehiculos.sort((v1, v2) -> Integer.compare(v1.getPrecio(), v2.getPrecio()));
        } else {
            listaVehiculos.sort((v1, v2) -> Integer.compare(v2.getPrecio(), v1.getPrecio()));
        }

        int precioMaximo = sliderPrecio.getValue();
        boolean verGasolina = chkGasolina.isSelected();
        boolean verHibrido = chkHibrido.isSelected();
        boolean verDiesel = chkDiesel.isSelected();
        boolean verElectrico = chkElectrico.isSelected();

        //mostrar los vehiculos de la base de datos
        for (Vehiculo v : listaVehiculos) {
            boolean cumplePrecio = v.getPrecio() <= precioMaximo;

            String categoria = "";
            String mod = v.getModelo().toLowerCase();
            String mar = v.getMarca().toLowerCase();

            if (mod.contains("corolla") || mod.contains("tucson") || mar.contains("toyota") || mar.contains("hyundai")) {
                categoria = "hibrido";
                v.setCombustible("Híbrido");
            } else if (mod.contains("model 3") || mar.contains("tesla")) {
                categoria = "electrico";
                v.setCombustible("Eléctrico");
            } else if (mod.contains("ibiza") || mod.contains("focus")) {
                categoria = "gasolina";
                v.setCombustible("Gasolina");
            } else if (mod.contains("golf")) {
                categoria = "diesel";
                v.setCombustible("Diésel");
            } else {
                String cbDB = v.getCombustible() != null ? v.getCombustible().toLowerCase() : "";
                if (cbDB.contains("hib") || cbDB.contains("h")) {
                    categoria = "hibrido";
                    v.setCombustible("Híbrido");
                } else if (cbDB.contains("elec") || cbDB.contains("e")) {
                    categoria = "electrico";
                    v.setCombustible("Eléctrico");
                } else if (cbDB.contains("gas")) {
                    categoria = "gasolina";
                    v.setCombustible("Gasolina");
                } else {
                    categoria = "diesel";
                    v.setCombustible("Diésel");
                }
            }

            boolean cumpleCombustible = (categoria.equals("gasolina") && verGasolina) ||
                    (categoria.equals("hibrido") && verHibrido) ||
                    (categoria.equals("diesel") && verDiesel) ||
                    (categoria.equals("electrico") && verElectrico);

            if (cumplePrecio && cumpleCombustible) {
                gridCoches.add(crearTarjeta(v));
            }
        }

        gridCoches.revalidate();
        gridCoches.repaint();
    }

    private JPanel crearTarjeta(Vehiculo v) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(45, 45, 45));
        card.putClientProperty("FlatLaf.style", "arc: 30");
        card.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70), 1));

        //boton de borrado
        JPanel panelSuperiorTarjeta = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSuperiorTarjeta.setOpaque(false); //transparente

        JButton btnEliminar = new JButton("X");
        btnEliminar.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnEliminar.setBackground(new Color(180, 40, 40));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        //lo hacemos redondo con flatLaf
        btnEliminar.putClientProperty("FlatLaf.style", "arc: 999");


        btnEliminar.addActionListener(e -> {
            //pedir confirmacion
            int confirmar = JOptionPane.showConfirmDialog(
                    this,
                    "¿Estás seguro de que deseas eliminar permanentemente el " + v.getMarca() + " " + v.getModelo() + "?",
                    "Confirmar Borrado",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirmar == JOptionPane.YES_OPTION) {
                boolean exito = consultas.eliminarVehiculo(v.getId());

                if (exito) {
                    JOptionPane.showMessageDialog(this, "Vehículo eliminado correctamente de la flota.");
                    refrescarCatalogo();
                } else {
                    JOptionPane.showMessageDialog(this, "Error: No se pudo eliminar el vehículo.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panelSuperiorTarjeta.add(btnEliminar);
        card.add(panelSuperiorTarjeta, BorderLayout.NORTH);
        boolean esTesla = v.getModelo().equalsIgnoreCase("Model 3") || v.getMarca().equalsIgnoreCase("Tesla");

        String path = "/fotos/" + v.getId() + ".png";
        URL imgUrl = getClass().getResource(path);
        JLabel lblImg = new JLabel();
        lblImg.setHorizontalAlignment(SwingConstants.CENTER);

        if (imgUrl != null) {
            ImageIcon imgOriginal = new ImageIcon(imgUrl);

            if (esTesla) {
                ImageFilter filtroGris = new GrayFilter(true, 50);
                ImageProducer productor = new FilteredImageSource(imgOriginal.getImage().getSource(), filtroGris);
                Image imgGris = Toolkit.getDefaultToolkit().createImage(productor);
                lblImg.setIcon(new ImageIcon(imgGris.getScaledInstance(350, 200, Image.SCALE_SMOOTH)));
            } else {
                lblImg.setIcon(new ImageIcon(imgOriginal.getImage().getScaledInstance(350, 200, Image.SCALE_SMOOTH)));
            }
        } else {
            lblImg.setText("Imagen no encontrada");
        }
        card.add(lblImg, BorderLayout.CENTER);

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setOpaque(false);
        info.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel nombre = new JLabel(v.getMarca().toUpperCase() + " " + v.getModelo());
        nombre.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JLabel motor = new JLabel("Motor: " + v.getCombustible());
        motor.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        motor.setForeground(Color.GRAY);

        JLabel precio = new JLabel(v.getPrecio() + " €/día");
        precio.setForeground(esTesla ? Color.GRAY : new Color(0, 162, 255));
        precio.setFont(new Font("Segoe UI", Font.BOLD, 20));

        JButton btnReservar = new JButton();
        if (esTesla) {
            btnReservar.setText("ALQUILADO");
            btnReservar.setBackground(new Color(90, 90, 90));
            btnReservar.setForeground(Color.LIGHT_GRAY);
            btnReservar.setEnabled(false); //boton no disponible
        } else {
            btnReservar.setText("RESERVAR AHORA");
            btnReservar.setBackground(new Color(0, 120, 215));
            btnReservar.setForeground(Color.WHITE);
            btnReservar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnReservar.addActionListener(e -> mostrarDialogoReserva(v));
        }
        btnReservar.setFont(new Font("Segoe UI", Font.BOLD, 14));

        info.add(nombre);
        info.add(motor);
        info.add(Box.createRigidArea(new Dimension(0, 10)));
        info.add(precio);
        info.add(Box.createRigidArea(new Dimension(0, 15)));
        info.add(btnReservar);
        card.add(info, BorderLayout.SOUTH);

        return card;
    }

    private void mostrarDialogoReserva(Vehiculo v) {
        if (v.getMarca().equalsIgnoreCase("Seat") && v.getModelo().equalsIgnoreCase("Ibiza")) {
            mostrarDetallesVehiculo();
        }

        JTextField txtDni = new JTextField();
        String[] opciones = {"Confirmar Alquiler", "Cancelar"};

        JPanel p = new JPanel(new GridLayout(0, 1, 5, 5));
        p.add(new JLabel("Introduzca su DNI para la reserva (ej: 12345678A):"));
        p.add(txtDni);

        int seleccion = JOptionPane.showOptionDialog(
                this, p, "Reserva - " + v.getModelo(),
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);

        if (seleccion == 0) {
            String dniIntroducido = txtDni.getText().trim().toUpperCase();

            if (!dniIntroducido.matches("\\d{8}[A-Z]")) {
                JOptionPane.showMessageDialog(this, "Formato de DNI no válido.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean exito = consultas.alquilarVehiculo(v.getId(), dniIntroducido);

            if (exito) {
                JOptionPane.showMessageDialog(this,
                        "RESERVA EXITOSA \n Vehículo: " + v.getModelo());
                refrescarCatalogo();
            } else {
                JOptionPane.showMessageDialog(this,
                        "ERROR: El DNI introducido no esta registrado en el sistema.", "Error de Reserva", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void mostrarDetallesVehiculo() {
        JDialog ventanaPopUp = new JDialog(this, "Prestaciones y Ubicación - SEAT IBIZA", true);
        ventanaPopUp.setSize(550, 650);
        ventanaPopUp.setLocationRelativeTo(this);
        ventanaPopUp.setLayout(new BorderLayout(10, 10));

        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelContenido.setBackground(new Color(30, 30, 30));

        JLabel lblTitulo = new JLabel("SEAT IBIZA - Ficha Técnica");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(230, 190, 90));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelContenido.add(lblTitulo);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 15)));

        JTextArea txtPrestaciones = new JTextArea(
                "• Motor: 1.0 TSI 110 CV (Gasolina)\n" +
                        "• Consumo medio: 5.2 L / 100km\n" +
                        "• Transmisión: Manual de 6 velocidades\n" +
                        "• Capacidad Maletero: 355 Litros\n" +
                        "• Equipamiento: Pantalla táctil 8.25\", Apple CarPlay,\n" +
                        "  Android Auto, Sensores de aparcamiento traseros y\n" +
                        "  Asistente de salida involuntaria de carril."
        );
        txtPrestaciones.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtPrestaciones.setForeground(Color.WHITE);
        txtPrestaciones.setOpaque(false);
        txtPrestaciones.setEditable(false);
        txtPrestaciones.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelContenido.add(txtPrestaciones);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 25)));

        JLabel lblUbicacionTitulo = new JLabel("Ubicación Actual del Vehículo:");
        lblUbicacionTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblUbicacionTitulo.setForeground(new Color(0, 162, 255));
        lblUbicacionTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelContenido.add(lblUbicacionTitulo);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 10)));

        URL mapaUrl = getClass().getResource("/fotos/mapa.png");
        JLabel lblMapa = new JLabel();
        lblMapa.setAlignmentX(Component.CENTER_ALIGNMENT);

        if (mapaUrl != null) {
            ImageIcon imgMapa = new ImageIcon(new ImageIcon(mapaUrl).getImage().getScaledInstance(450, 250, Image.SCALE_SMOOTH));
            lblMapa.setIcon(imgMapa);
        } else {
            lblMapa.setText("[ Imagen /fotos/mapa_ibiza.png no encontrada ]");
            lblMapa.setForeground(Color.LIGHT_GRAY);
        }
        panelContenido.add(lblMapa);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 25)));

        JButton btnCerrar = new JButton("ENTENDIDO");
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCerrar.setBackground(new Color(0, 120, 215));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.addActionListener(e -> ventanaPopUp.dispose());
        panelContenido.add(btnCerrar);

        ventanaPopUp.add(panelContenido, BorderLayout.CENTER);
        ventanaPopUp.setVisible(true);
    }
}
