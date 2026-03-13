import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

// 1. Кастомная непроверяемая ошибка
class RoomAlreadyBookedException extends RuntimeException {
    public RoomAlreadyBookedException(String message) {
        super(message);
    }
}

// 2. Перечисление цен
enum Prices {
    ECONOMY(100),
    STANDARD(200),
    PRO(300),
    LUX(500),
    ULTRA_LUX(1000);

    private final int price;

    Prices(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }
}

// 3. Абстрактный класс Room
abstract class Room {
    protected int roomNumber;
    protected int maxCapacity;
    protected int pricePerNight;
    protected boolean isBooked;

    public Room(int roomNumber, int maxCapacity, int pricePerNight) {
        this.roomNumber = roomNumber;
        this.maxCapacity = maxCapacity;
        this.pricePerNight = pricePerNight;
        this.isBooked = false;
    }

    public Room(int roomNumber, int pricePerNight) {
        this.roomNumber = roomNumber;
        this.maxCapacity = (int)(Math.random() * 4) + 1;
        this.pricePerNight = pricePerNight;
        this.isBooked = false;
    }

    public Room(int roomNumber, Prices price) {
        this.roomNumber = roomNumber;
        this.maxCapacity = (int)(Math.random() * 4) + 1;
        this.pricePerNight = price.getPrice();
        this.isBooked = false;
    }

    public int getRoomNumber() { return roomNumber; }
    public int getMaxCapacity() { return maxCapacity; }
    public int getPricePerNight() { return pricePerNight; }
    public boolean isBooked() { return isBooked; }
    public void setBooked(boolean booked) { isBooked = booked; }

    @Override
    public String toString() {
        return "Room " + roomNumber + " | " + maxCapacity + " guests | $" + pricePerNight +
                " | " + (isBooked ? "BOOKED" : "Available");
    }

    public abstract String getType();
}

// 4. Класс EconomyRoom
class EconomyRoom extends Room {
    public EconomyRoom(int roomNumber, int maxCapacity, int pricePerNight) {
        super(roomNumber, maxCapacity, pricePerNight);
    }
    public EconomyRoom(int roomNumber, int pricePerNight) {
        super(roomNumber, pricePerNight);
    }
    public EconomyRoom(int roomNumber, Prices price) {
        super(roomNumber, price);
    }

    @Override
    public String getType() {
        return "Economy";
    }
}

// 5. Абстрактный класс ProRoom
abstract class ProRoom extends Room {
    public ProRoom(int roomNumber, int maxCapacity, int pricePerNight) {
        super(roomNumber, maxCapacity, pricePerNight);
    }
    public ProRoom(int roomNumber, int pricePerNight) {
        super(roomNumber, pricePerNight);
    }
    public ProRoom(int roomNumber, Prices price) {
        super(roomNumber, price);
    }
}

// 6. Класс StandardRoom
class StandardRoom extends ProRoom {
    public StandardRoom(int roomNumber, int maxCapacity, int pricePerNight) {
        super(roomNumber, maxCapacity, pricePerNight);
    }
    public StandardRoom(int roomNumber, int pricePerNight) {
        super(roomNumber, pricePerNight);
    }
    public StandardRoom(int roomNumber, Prices price) {
        super(roomNumber, price);
    }

    @Override
    public String getType() {
        return "Standard";
    }
}

// 7. Класс LuxRoom
class LuxRoom extends ProRoom {
    public LuxRoom(int roomNumber, int maxCapacity, int pricePerNight) {
        super(roomNumber, maxCapacity, pricePerNight);
    }
    public LuxRoom(int roomNumber, int pricePerNight) {
        super(roomNumber, pricePerNight);
    }
    public LuxRoom(int roomNumber, Prices price) {
        super(roomNumber, price);
    }

    @Override
    public String getType() {
        return "Lux";
    }
}

// 8. Класс UltraLuxRoom
class UltraLuxRoom extends LuxRoom {
    public UltraLuxRoom(int roomNumber, int maxCapacity, int pricePerNight) {
        super(roomNumber, maxCapacity, pricePerNight);
    }
    public UltraLuxRoom(int roomNumber, int pricePerNight) {
        super(roomNumber, pricePerNight);
    }
    public UltraLuxRoom(int roomNumber, Prices price) {
        super(roomNumber, price);
    }

    @Override
    public String getType() {
        return "UltraLux";
    }
}

// 9. Интерфейс RoomService
interface RoomService<T extends Room> {
    void clean(T room);
    void reserve(T room);
    void free(T room);
}

// 10. Интерфейс LuxRoomService
interface LuxRoomService<T extends LuxRoom> extends RoomService<T> {
    void foodDelivery(T room);
}

// 11. Реализация RoomService
class RoomServiceImpl<T extends Room> implements RoomService<T> {

    @Override
    public void clean(T room) {
        room.setBooked(false);
    }

    @Override
    public void reserve(T room) {
        if (room.isBooked()) {
            throw new RoomAlreadyBookedException("Room N" + room.getRoomNumber() + " is already booked!");
        }
        room.setBooked(true);
    }

    @Override
    public void free(T room) {
        room.setBooked(false);
    }
}

// 12. Реализация LuxRoomService
class LuxRoomServiceImpl<T extends LuxRoom> implements LuxRoomService<T> {

    private final RoomService<T> baseService = new RoomServiceImpl<>();

    @Override
    public void clean(T room) {
        baseService.clean(room);
    }

    @Override
    public void reserve(T room) {
        baseService.reserve(room);
    }

    @Override
    public void free(T room) {
        baseService.free(room);
    }

    @Override
    public void foodDelivery(T room) {
        // Food delivery available
    }
}

// Утилиты для rounded components
class RoundedButton extends JButton {
    private int radius = 20;

    public RoundedButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setFont(new Font("Inter", Font.BOLD, 13));
        setForeground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (getModel().isPressed()) {
            g2.setColor(getBackground().darker());
        } else if (getModel().isRollover()) {
            g2.setColor(getBackground().brighter());
        } else {
            g2.setColor(getBackground());
        }

        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        super.paintComponent(g2);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        // No border
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}

class RoundedPanel extends JPanel {
    private int radius = 15;
    private Color backgroundColor;

    public RoundedPanel(Color bg) {
        this.backgroundColor = bg;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(backgroundColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        // Add shadow effect
        g2.setColor(new Color(0, 0, 0, 20));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

        g2.dispose();
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}

// 13. Главное окно приложения - ULTRA MODERN DESIGN
public class Main extends JFrame {
    private List<Room> rooms;
    private RoomServiceImpl<Room> roomService;
    private LuxRoomServiceImpl<LuxRoom> luxRoomService;

    private JComboBox<String> roomTypeCombo;
    private JTextField roomNumberField;
    private JTextField capacityField;
    private JComboBox<Prices> priceCombo;

    private JPanel roomsPanel;
    private JLabel statusLabel;
    private JLabel statsLabel;
    private JComboBox<String> actionRoomCombo;

    // Modern Color Scheme - Dark Theme
    private final Color PRIMARY_COLOR = new Color(99, 102, 241);
    private final Color SECONDARY_COLOR = new Color(168, 85, 247);
    private final Color ACCENT_COLOR = new Color(236, 72, 153);
    private final Color SUCCESS_COLOR = new Color(16, 185, 129);
    private final Color WARNING_COLOR = new Color(245, 158, 11);
    private final Color DANGER_COLOR = new Color(239, 68, 68);
    private final Color BG_COLOR = new Color(15, 23, 42);
    private final Color CARD_BG = new Color(30, 41, 59);
    private final Color TEXT_PRIMARY = new Color(241, 245, 249);
    private final Color TEXT_SECONDARY = new Color(148, 163, 184);

    public Main() {
        rooms = new ArrayList<>();
        roomService = new RoomServiceImpl<>();
        luxRoomService = new LuxRoomServiceImpl<>();

        setTitle("Hotel Management System - Ultra Modern");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 850);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(BG_COLOR);

        // Modern Header with gradient - УМЕНЬШЕНА ВЫСОТА
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Main Content with modern layout - УМЕНЬШЕНЫ ОТСТУПЫ
        JPanel mainContentPanel = new JPanel(new GridBagLayout());
        mainContentPanel.setBackground(BG_COLOR);
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); // БЫЛО 20

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8); // БЫЛО 10
        gbc.fill = GridBagConstraints.BOTH;

        // Left Panel - Create Room - УМЕНЬШЕНА ВЫСОТА
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.35;
        gbc.weighty = 0.35; // БЫЛО 0.45
        mainContentPanel.add(createCreateRoomPanel(), gbc);

        // Right Panel - Actions - УМЕНЬШЕНА ВЫСОТА
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.35;
        gbc.weighty = 0.35; // БЫЛО 0.45
        mainContentPanel.add(createActionsPanel(), gbc);

        // Bottom Panel - Rooms List - УВЕЛИЧЕНА ВЫСОТА
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.70; // БЫЛО 0.55
        mainContentPanel.add(createRoomsListPanel(), gbc);

        add(mainContentPanel, BorderLayout.CENTER);

        // Modern Status Bar
        JPanel statusBar = createStatusBar();
        add(statusBar, BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Modern gradient
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(30, 58, 138),
                        getWidth(), getHeight(), new Color(67, 56, 202)
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Decorative circles
                g2d.setColor(new Color(255, 255, 255, 15));
                g2d.fillOval(100, -20, 150, 150);
                g2d.fillOval(getWidth() - 200, 10, 120, 120);
            }
        };
        header.setPreferredSize(new Dimension(getWidth(), 80)); // БЫЛО 110 - УМЕНЬШЕНО
        header.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40)); // БЫЛО 25

        JLabel titleLabel = new JLabel("HOTEL MANAGEMENT", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Inter", Font.BOLD, 28)); // БЫЛО 32
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Ultra Modern Edition", SwingConstants.LEFT);
        subtitleLabel.setFont(new Font("Inter", Font.PLAIN, 13)); // БЫЛО 14
        subtitleLabel.setForeground(new Color(200, 200, 255));

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);
        textPanel.add(titleLabel);
        textPanel.add(subtitleLabel);

        header.add(textPanel, BorderLayout.CENTER);

        statsLabel = new JLabel("Total: 0 | Available: 0 | Booked: 0", SwingConstants.RIGHT);
        statsLabel.setFont(new Font("Inter", Font.BOLD, 13)); // БЫЛО 14
        statsLabel.setForeground(Color.WHITE);
        header.add(statsLabel, BorderLayout.EAST);

        return header;
    }

    private JPanel createCreateRoomPanel() {
        RoundedPanel panel = new RoundedPanel(CARD_BG);
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25)); // БЫЛО 30

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5); // БЫЛО 12
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        // Section Header
        JLabel headerLabel = new JLabel("CREATE NEW ROOM");
        headerLabel.setFont(new Font("Inter", Font.BOLD, 14)); // БЫЛО 16
        headerLabel.setForeground(PRIMARY_COLOR);
        panel.add(headerLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        // Room Type
        gbc.weightx = 0.35;
        panel.add(createModernLabel("Room Type"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.65;
        roomTypeCombo = createModernComboBox(new String[]{"Economy", "Standard", "Lux", "UltraLux"});
        panel.add(roomTypeCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0.35;
        panel.add(createModernLabel("Room Number"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.65;
        roomNumberField = createModernTextField();
        panel.add(roomNumberField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0.35;
        panel.add(createModernLabel("Capacity"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.65;
        capacityField = createModernTextField();
        panel.add(capacityField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0.35;
        panel.add(createModernLabel("Price Category"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.65;
        priceCombo = createModernComboBox(Prices.values());
        panel.add(priceCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        RoundedButton createButton = new RoundedButton("CREATE ROOM");
        createButton.setBackground(SUCCESS_COLOR);
        createButton.setRadius(25);
        createButton.setMaximumSize(new Dimension(300, 45)); // БЫЛО 50
        createButton.addActionListener(e -> createRoom());
        panel.add(createButton, gbc);

        return panel;
    }

    private JPanel createActionsPanel() {
        RoundedPanel panel = new RoundedPanel(CARD_BG);
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25)); // БЫЛО 30

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5); // БЫЛО 10
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;

        // Section Header
        JLabel headerLabel = new JLabel("ROOM ACTIONS");
        headerLabel.setFont(new Font("Inter", Font.BOLD, 14)); // БЫЛО 16
        headerLabel.setForeground(ACCENT_COLOR);
        panel.add(headerLabel, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        panel.add(createModernLabel("Select Room"), gbc);

        gbc.gridy++;
        actionRoomCombo = createModernComboBox(new String[]{});
        actionRoomCombo.setPreferredSize(new Dimension(0, 40)); // БЫЛО 45
        panel.add(actionRoomCombo, gbc);

        // Action Buttons Grid
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(15, 5, 8, 5); // БЫЛО 20, 10

        JPanel buttonsPanel = new JPanel(new GridLayout(2, 2, 10, 10)); // БЫЛО 12
        buttonsPanel.setOpaque(false);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0)); // БЫЛО 15

        RoundedButton reserveBtn = new RoundedButton("RESERVE");
        reserveBtn.setBackground(DANGER_COLOR);
        reserveBtn.setRadius(20);
        reserveBtn.addActionListener(e -> reserveRoom());
        buttonsPanel.add(reserveBtn);

        RoundedButton freeBtn = new RoundedButton("FREE");
        freeBtn.setBackground(SUCCESS_COLOR);
        freeBtn.setRadius(20);
        freeBtn.addActionListener(e -> freeRoom());
        buttonsPanel.add(freeBtn);

        RoundedButton cleanBtn = new RoundedButton("CLEAN");
        cleanBtn.setBackground(WARNING_COLOR);
        cleanBtn.setRadius(20);
        cleanBtn.addActionListener(e -> cleanRoom());
        buttonsPanel.add(cleanBtn);

        RoundedButton foodBtn = new RoundedButton("FOOD DELIVERY");
        foodBtn.setBackground(new Color(139, 92, 246));
        foodBtn.setRadius(20);
        foodBtn.addActionListener(e -> foodDelivery());
        buttonsPanel.add(foodBtn);

        panel.add(buttonsPanel, gbc);

        return panel;
    }

    private JPanel createRoomsListPanel() {
        RoundedPanel panel = new RoundedPanel(CARD_BG);
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25)); // БЫЛО 30

        // Header
        JLabel headerLabel = new JLabel("ALL ROOMS");
        headerLabel.setFont(new Font("Inter", Font.BOLD, 18)); // БЫЛО 20
        headerLabel.setForeground(SUCCESS_COLOR);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0)); // БЫЛО 25
        panel.add(headerLabel, BorderLayout.NORTH);

        // Rooms panel with scroll
        roomsPanel = new JPanel(new GridLayout(0, 1, 10, 10)); // БЫЛО 12
        roomsPanel.setBackground(BG_COLOR);
        roomsPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8)); // БЫЛО 10

        JScrollPane scrollPane = new JScrollPane(roomsPanel);
        scrollPane.setBackground(BG_COLOR);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Modern scrollbar
        scrollPane.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(99, 102, 241);
                this.trackColor = BG_COLOR;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                return button;
            }
        });

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(new Color(30, 58, 138));
        statusBar.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30)); // БЫЛО 12

        statusLabel = new JLabel("System Ready", SwingConstants.LEFT);
        statusLabel.setFont(new Font("Inter", Font.PLAIN, 12)); // БЫЛО 13
        statusLabel.setForeground(Color.WHITE);

        JLabel versionLabel = new JLabel("v3.0 Ultra Modern", SwingConstants.RIGHT);
        versionLabel.setFont(new Font("Inter", Font.ITALIC, 10)); // БЫЛО 11
        versionLabel.setForeground(new Color(200, 200, 255));

        statusBar.add(statusLabel, BorderLayout.CENTER);
        statusBar.add(versionLabel, BorderLayout.EAST);

        return statusBar;
    }

    // Modern component helpers
    private JLabel createModernLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Inter", Font.PLAIN, 12)); // ИСПРАВЛЕНО: MEDIUM -> PLAIN
        label.setForeground(TEXT_SECONDARY);
        return label;
    }

    private JTextField createModernTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Inter", Font.PLAIN, 13));
        field.setBackground(new Color(51, 65, 85));
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(Color.red);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(71, 85, 105), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12) // БЫЛО 10, 15
        ));
        field.setPreferredSize(new Dimension(300, 40)); // БЫЛО 45
        field.setMaximumSize(new Dimension(500, 40));    // БЫЛО 45
        return field;
    }

    private <T> JComboBox<T> createModernComboBox(T[] items) {
        JComboBox<T> combo = new JComboBox<>(items);
        combo.setFont(new Font("Inter", Font.PLAIN, 13));
        combo.setPreferredSize(new Dimension(300, 40)); // БЫЛО 45
        combo.setMaximumSize(new Dimension(500, 40));    // БЫЛО 45
        combo.setBackground(new Color(51, 65, 85));
        combo.setForeground(Color.red);
        combo.setCursor(new Cursor(Cursor.HAND_CURSOR));

        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);
                label.setFont(new Font("Inter", Font.PLAIN, 13));
                label.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12)); // БЫЛО 10, 15
                if (isSelected) {
                    label.setBackground(PRIMARY_COLOR);
                    label.setForeground(Color.WHITE);
                } else {
                    label.setBackground(new Color(51, 65, 85));
                    label.setForeground(Color.red);
                }
                return label;
            }
        });

        return combo;
    }

    private void createRoom() {
        try {
            int roomNumber = Integer.parseInt(roomNumberField.getText().trim());
            int capacity = capacityField.getText().trim().isEmpty() ?
                    (int)(Math.random() * 4) + 1 : Integer.parseInt(capacityField.getText().trim());
            Prices price = (Prices) priceCombo.getSelectedItem();
            String type = (String) roomTypeCombo.getSelectedItem();

            Room room = null;
            switch (type) {
                case "Economy":
                    room = new EconomyRoom(roomNumber, capacity, price.getPrice());
                    break;
                case "Standard":
                    room = new StandardRoom(roomNumber, capacity, price.getPrice());
                    break;
                case "Lux":
                    room = new LuxRoom(roomNumber, capacity, price.getPrice());
                    break;
                case "UltraLux":
                    room = new UltraLuxRoom(roomNumber, capacity, price.getPrice());
                    break;
            }

            if (room != null) {
                rooms.add(room);
                updateRoomCombo();
                updateRoomsList();
                updateHeaderStats();
                showStatus("Room " + roomNumber + " (" + type + ") created successfully", SUCCESS_COLOR);

                roomNumberField.setText("");
                capacityField.setText("");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Please enter valid numeric values",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            showStatus("Error creating room", DANGER_COLOR);
        }
    }

    private void updateRoomCombo() {
        actionRoomCombo.removeAllItems();
        for (Room room : rooms) {
            String status = room.isBooked() ? "BOOKED" : "Available";
            actionRoomCombo.addItem("Room " + room.getRoomNumber() + " - " + room.getType() + " - " + status);
        }
    }

    private void updateRoomsList() {
        roomsPanel.removeAll();

        if (rooms.isEmpty()) {
            JLabel emptyLabel = new JLabel("No rooms created yet. Create your first room!", SwingConstants.CENTER);
            emptyLabel.setFont(new Font("Inter", Font.ITALIC, 14));
            emptyLabel.setForeground(TEXT_SECONDARY);
            emptyLabel.setBorder(BorderFactory.createEmptyBorder(60, 0, 0, 0));
            roomsPanel.add(emptyLabel);
        } else {
            for (Room room : rooms) {
                JPanel roomCard = createRoomCard(room);
                roomsPanel.add(roomCard);
            }
        }

        roomsPanel.revalidate();
        roomsPanel.repaint();
    }

    private JPanel createRoomCard(Room room) {
        RoundedPanel card = new RoundedPanel(new Color(51, 65, 85));
        card.setLayout(new BorderLayout(20, 0));
        card.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        // Left - Info
        JPanel infoPanel = new JPanel(new GridLayout(3, 2, 15, 8));
        infoPanel.setOpaque(false);

        infoPanel.add(createInfoLabel("Number:"));
        infoPanel.add(createInfoValue(String.valueOf(room.getRoomNumber())));

        infoPanel.add(createInfoLabel("Type:"));
        infoPanel.add(createInfoValue(room.getType()));

        infoPanel.add(createInfoLabel("Capacity:"));
        infoPanel.add(createInfoValue(room.getMaxCapacity() + " guests"));

        infoPanel.add(createInfoLabel("Price:"));
        infoPanel.add(createInfoValue("$" + room.getPricePerNight()));

        infoPanel.add(createInfoLabel("Status:"));
        JLabel statusLbl = new JLabel(room.isBooked() ? "BOOKED" : "Available");
        statusLbl.setFont(new Font("Inter", Font.BOLD, 13));
        statusLbl.setForeground(room.isBooked() ? DANGER_COLOR : SUCCESS_COLOR);
        infoPanel.add(statusLbl);

        card.add(infoPanel, BorderLayout.CENTER);

        // Right - Type Badge
        JPanel typePanel = new JPanel();
        typePanel.setOpaque(false);
        typePanel.setPreferredSize(new Dimension(120, 100));

        Color typeColor = getTypeColor(room.getType());
        JLabel typeBadge = new JLabel(room.getType(), SwingConstants.CENTER);
        typeBadge.setFont(new Font("Inter", Font.BOLD, 14));
        typeBadge.setForeground(Color.WHITE);
        typeBadge.setOpaque(true);
        typeBadge.setBackground(typeColor);
        typeBadge.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));

        typePanel.add(typeBadge);
        card.add(typePanel, BorderLayout.EAST);

        return card;
    }

    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Inter", Font.PLAIN, 12));
        label.setForeground(TEXT_SECONDARY);
        return label;
    }

    private JLabel createInfoValue(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Inter", Font.BOLD, 13));
        label.setForeground(TEXT_PRIMARY);
        return label;
    }

    private Color getTypeColor(String type) {
        switch (type) {
            case "Economy": return new Color(100, 116, 139);
            case "Standard": return new Color(59, 130, 246);
            case "Lux": return new Color(168, 85, 247);
            case "UltraLux": return new Color(236, 72, 153);
            default: return Color.GRAY;
        }
    }

    private void updateHeaderStats() {
        int total = rooms.size();
        long booked = rooms.stream().filter(Room::isBooked).count();
        long free = total - booked;

        if (statsLabel != null) {
            statsLabel.setText(String.format("Total: %d | Available: %d | Booked: %d",
                    total, free, booked));
        }
    }

    private void showStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color == DANGER_COLOR ? new Color(254, 202, 202) : Color.WHITE);
    }

    private void reserveRoom() {
        if (actionRoomCombo.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Please select a room", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Room room = rooms.get(actionRoomCombo.getSelectedIndex());
            roomService.reserve(room);
            updateRoomCombo();
            updateRoomsList();
            updateHeaderStats();
            showStatus("Room " + room.getRoomNumber() + " reserved", SUCCESS_COLOR);
            JOptionPane.showMessageDialog(this,
                    "Room " + room.getRoomNumber() + " successfully reserved!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (RoomAlreadyBookedException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            showStatus(e.getMessage(), DANGER_COLOR);
        }
    }

    private void freeRoom() {
        if (actionRoomCombo.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Please select a room", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Room room = rooms.get(actionRoomCombo.getSelectedIndex());
        roomService.free(room);
        updateRoomCombo();
        updateRoomsList();
        updateHeaderStats();
        showStatus("Room " + room.getRoomNumber() + " freed", SUCCESS_COLOR);
        JOptionPane.showMessageDialog(this,
                "Room " + room.getRoomNumber() + " is now available",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void cleanRoom() {
        if (actionRoomCombo.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Please select a room", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Room room = rooms.get(actionRoomCombo.getSelectedIndex());
        roomService.clean(room);
        updateRoomCombo();
        updateRoomsList();
        updateHeaderStats();
        showStatus("Room " + room.getRoomNumber() + " cleaned", WARNING_COLOR);
        JOptionPane.showMessageDialog(this,
                "Room " + room.getRoomNumber() + " has been cleaned",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void foodDelivery() {
        if (actionRoomCombo.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Please select a room", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Room room = rooms.get(actionRoomCombo.getSelectedIndex());

        if (!(room instanceof LuxRoom)) {
            JOptionPane.showMessageDialog(this,
                    "Food delivery is only available for Lux and UltraLux rooms!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            showStatus("Food delivery not available for " + room.getType(), DANGER_COLOR);
            return;
        }

        luxRoomService.foodDelivery((LuxRoom) room);
        showStatus("Food delivery ordered for room " + room.getRoomNumber(), new Color(139, 92, 246));
        JOptionPane.showMessageDialog(this,
                "Food delivery ordered for room " + room.getRoomNumber() + "\n" +
                        "Room Type: " + room.getType() + "\n" +
                        "Enjoy your meal!",
                "Food Delivery",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            Main gui = new Main();
            gui.setVisible(true);
        });
    }
}