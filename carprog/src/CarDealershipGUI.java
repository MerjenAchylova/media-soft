import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class CarDealershipGUI extends JFrame {
    private CarDealership dealership;
    private JTabbedPane tabbedPane;
    private JLabel lblTotalCarsValue, lblTotalValueValue, lblAvgPriceValue;
    private DefaultTableModel tableModel;
    private JTable carsTableInTab;

    private final Color HEADER_COLOR = new Color(25, 118, 210);
    private final Color PRIMARY_COLOR = new Color(30, 136, 229);
    private final Color SECONDARY_COLOR = new Color(76, 175, 80);
    private final Color ACCENT_COLOR = new Color(156, 39, 176);
    private final Color BUTTON_PRIMARY = new Color(33, 150, 243);
    private final Color BUTTON_SUCCESS = new Color(76, 175, 80);
    private final Color BUTTON_WARNING = new Color(255, 152, 0);
    private final Color BUTTON_INFO = new Color(0, 188, 212);
    private final Color TABLE_HEADER_BG = new Color(76, 175, 80);
    private final Color TABLE_HEADER_FOREGROUND = Color.WHITE;
    private final Color BG_COLOR = new Color(245, 247, 250);
    private final Color CARD_COLOR = Color.WHITE;
    private final Color TEXT_DARK = new Color(33, 33, 33);
    private final Color TEXT_SECONDARY = new Color(100, 116, 139);

    public CarDealershipGUI() {
        dealership = new CarDealership("Premium Auto");
        addTestCars();

        setTitle("Автоцентр - Система управления");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void addTestCars() {
        Random random = new Random();
        String[] manufacturers = {"Toyota", "BMW", "Mercedes", "Tesla", "Audi", "Honda"};
        String[] models = {"Camry", "X5", "E-Class", "Model 3", "A6", "Civic"};
        CarType[] types = {CarType.SEDAN, CarType.SUV, CarType.ELECTRIC, CarType.HATCHBACK};

        for (int i = 0; i < 15; i++) {
            String vin = "VIN" + String.format("%05d", i);
            String manufacturer = manufacturers[random.nextInt(manufacturers.length)];
            String model = models[random.nextInt(models.length)];
            int year = random.nextInt(26) + 2000;
            int mileage = random.nextInt(200000);
            double price = random.nextInt(5000000) + 500000;
            CarType type = types[random.nextInt(types.length)];

            dealership.addCar(new Car(vin, model, manufacturer, year, mileage, price, type));
        }

        System.out.println("Добавлено " + dealership.getCarsCount() + " тестовых машин");
    }

    private void initComponents() {
        getContentPane().setBackground(BG_COLOR);

        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabbedPane.setBackground(CARD_COLOR);
        tabbedPane.setForeground(TEXT_DARK);

        tabbedPane.addTab("Главная", createDashboardPanel());
        tabbedPane.addTab("Автопарк", createCarsPanel());
        tabbedPane.addTab("Поиск по году", createYearSearchPanel());
        tabbedPane.addTab("Переименование модели", createRenameModelPanel());
        tabbedPane.addTab("Задание 3: Equals", createTask3Panel());
        tabbedPane.addTab("Задание 4: Stream API", createTask4Panel());
        tabbedPane.addTab("Добавить авто", createAddCarPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(HEADER_COLOR);
        panel.setPreferredSize(new Dimension(getWidth(), 90));
        panel.setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("Premium Auto Center", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitle.setForeground(Color.WHITE);
        panel.add(lblTitle, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(BG_COLOR);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setBackground(BG_COLOR);
        statsPanel.setMaximumSize(new Dimension(1000, 120));

        // Создаем карточки с сохранением ссылок на значения
        JLabel[] totalCarsRef = new JLabel[1];
        JLabel[] totalValueRef = new JLabel[1];
        JLabel[] avgPriceRef = new JLabel[1];

        statsPanel.add(createStatCard("Всего машин", "0", new Color(66, 165, 245), totalCarsRef));
        statsPanel.add(createStatCard("Общая стоимость", "0 RUB", new Color(102, 187, 106), totalValueRef));
        statsPanel.add(createStatCard("Средняя цена", "0 RUB", new Color(255, 183, 77), avgPriceRef));

        // Сохраняем ссылки
        lblTotalCarsValue = totalCarsRef[0];
        lblTotalValueValue = totalValueRef[0];
        lblAvgPriceValue = avgPriceRef[0];

        panel.add(statsPanel);
        panel.add(Box.createVerticalStrut(20));

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(CARD_COLOR);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(224, 224, 224), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblTableTitle = new JLabel(" Все автомобили в наличии");
        lblTableTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTableTitle.setForeground(new Color(76, 175, 80));
        lblTableTitle.setBackground(new Color(232, 245, 233));
        lblTableTitle.setOpaque(true);
        lblTableTitle.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(76, 175, 80)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        tablePanel.add(lblTableTitle, BorderLayout.NORTH);

        String[] columns = {"VIN", "Производитель", "Модель", "Год", "Пробег (км)", "Цена (RUB)", "Тип"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable carsTable = new JTable(tableModel);
        styleTable(carsTable);

        JScrollPane scrollPane = new JScrollPane(carsTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        panel.add(tablePanel);

        // Обновляем статистику после создания всех компонентов
        SwingUtilities.invokeLater(this::updateDashboard);

        return panel;
    }

    private JPanel createStatCard(String title, String value, Color accentColor, JLabel[] valueLabelRef) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(accentColor, 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setMaximumSize(new Dimension(300, 120));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(TEXT_SECONDARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(accentColor);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Сохраняем ссылку для последующего обновления
        if (valueLabelRef != null && valueLabelRef.length > 0) {
            valueLabelRef[0] = valueLabel;
        }

        card.add(titleLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(valueLabel);

        return card;
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(35);
        table.setSelectionBackground(new Color(227, 242, 253));
        table.setGridColor(new Color(238, 238, 238));
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(TABLE_HEADER_BG);
        header.setForeground(TABLE_HEADER_FOREGROUND);
        header.setPreferredSize(new Dimension(0, 45));

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(TABLE_HEADER_BG);
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                setHorizontalAlignment(JLabel.CENTER);
                setOpaque(true);
                return this;
            }
        };

        header.setDefaultRenderer(headerRenderer);

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(JLabel.CENTER);
        cellRenderer.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
    }

    private JPanel createCarsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filterPanel.setBackground(CARD_COLOR);
        filterPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(224, 224, 224), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        filterPanel.add(new JLabel("Производитель:"));
        JComboBox<String> comboManufacturer = new JComboBox<>(new String[]{"Все", "Toyota", "BMW", "Mercedes", "Tesla", "Audi", "Honda"});
        comboManufacturer.setPreferredSize(new Dimension(160, 32));
        comboManufacturer.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        filterPanel.add(comboManufacturer);

        filterPanel.add(Box.createHorizontalStrut(20));
        filterPanel.add(new JLabel("Тип:"));

        JComboBox<CarType> comboType = new JComboBox<>(CarType.values());
        comboType.insertItemAt(CarType.values()[0], 0);
        comboType.setSelectedIndex(0);
        comboType.setPreferredSize(new Dimension(160, 32));
        comboType.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        filterPanel.add(comboType);

        JButton btnFilter = createStyledButton("Фильтр", BUTTON_PRIMARY);
        btnFilter.addActionListener(e -> {
            String selectedManufacturer = comboManufacturer.getSelectedItem().toString();
            CarType selectedType = (CarType) comboType.getSelectedItem();

            System.out.println("Фильтр: Производитель=" + selectedManufacturer + ", Тип=" + selectedType);

            List<Car> filtered = dealership.getCarsSortedByYear().stream()
                    .filter(car -> {
                        boolean manufacturerMatch = selectedManufacturer.equals("Все") ||
                                car.getManufacturer().equals(selectedManufacturer);
                        boolean typeMatch = selectedType == null || car.getType() == selectedType;
                        return manufacturerMatch && typeMatch;
                    })
                    .collect(Collectors.toList());

            System.out.println("Найдено: " + filtered.size() + " машин");

            if (carsTableInTab != null) {
                DefaultTableModel model = (DefaultTableModel) carsTableInTab.getModel();
                model.setRowCount(0);
                for (Car car : filtered) {
                    model.addRow(new Object[]{
                            car.getVin(),
                            car.getManufacturer(),
                            car.getModel(),
                            car.getYear(),
                            String.format("%,d", car.getMileage()),
                            String.format("%,d", (int)car.getPrice()),
                            car.getType()
                    });
                }
                System.out.println("Таблица обновлена!");
            } else {
                System.out.println("Ошибка: таблица не найдена!");
            }
        });
        filterPanel.add(btnFilter);

        JButton btnReset = createStyledButton("Сбросить", new Color(120, 144, 156));
        btnReset.addActionListener(e -> {
            comboManufacturer.setSelectedIndex(0);
            comboType.setSelectedIndex(0);
            List<Car> allCars = new ArrayList<>(dealership.getCarsSortedByYear());
            if (carsTableInTab != null) {
                DefaultTableModel model = (DefaultTableModel) carsTableInTab.getModel();
                model.setRowCount(0);
                for (Car car : allCars) {
                    model.addRow(new Object[]{
                            car.getVin(),
                            car.getManufacturer(),
                            car.getModel(),
                            car.getYear(),
                            String.format("%,d", car.getMileage()),
                            String.format("%,d", (int)car.getPrice()),
                            car.getType()
                    });
                }
            }
        });
        filterPanel.add(btnReset);

        panel.add(filterPanel, BorderLayout.NORTH);

        String[] columns = {"VIN", "Производитель", "Модель", "Год", "Пробег (км)", "Цена (RUB)", "Тип"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        styleTable(table);
        carsTableInTab = table;

        List<Car> cars = new ArrayList<>(dealership.getCarsSortedByYear());
        for (Car car : cars) {
            model.addRow(new Object[]{
                    car.getVin(),
                    car.getManufacturer(),
                    car.getModel(),
                    car.getYear(),
                    String.format("%,d", car.getMileage()),
                    String.format("%,d", (int)car.getPrice()),
                    car.getType()
            });
        }

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setBorderPainted(false);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private JPanel createYearSearchPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(BG_COLOR);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        inputPanel.setBackground(CARD_COLOR);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        inputPanel.setMaximumSize(new Dimension(650, 120));

        inputPanel.add(new JLabel("C года:"));
        JTextField txtFromYear = new JTextField(8);
        txtFromYear.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtFromYear.setPreferredSize(new Dimension(120, 35));
        txtFromYear.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        inputPanel.add(txtFromYear);

        inputPanel.add(new JLabel("По год:"));
        JTextField txtToYear = new JTextField(8);
        txtToYear.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtToYear.setPreferredSize(new Dimension(120, 35));
        txtToYear.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        inputPanel.add(txtToYear);

        JButton btnFind = createStyledButton("Найти", BUTTON_PRIMARY);
        btnFind.setPreferredSize(new Dimension(140, 40));
        btnFind.addActionListener(e -> {
            try {
                int fromYear = Integer.parseInt(txtFromYear.getText().trim());
                int toYear = Integer.parseInt(txtToYear.getText().trim());

                if (fromYear > toYear) {
                    JOptionPane.showMessageDialog(this,
                            "Год 'C года' не может быть больше 'По год'!",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                List<Car> filtered = dealership.getCarsSortedByYear().stream()
                        .filter(c -> c.getYear() >= fromYear && c.getYear() <= toYear)
                        .collect(Collectors.toList());

                if (filtered.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Машины не найдены!",
                            "Результат",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    showYearSearchResults(filtered, fromYear, toYear);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Введите корректные года!",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        inputPanel.add(btnFind);

        panel.add(inputPanel);
        panel.add(Box.createVerticalStrut(20));

        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        textArea.setEditable(false);
        textArea.setBackground(Color.WHITE);
        textArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        textArea.setText("Введите диапазон лет для поиска автомобилей\n\nПример:\nC года: 2015\nПо год: 2020\n\nНажмите 'Найти' для поиска");

        panel.add(new JScrollPane(textArea));

        return panel;
    }

    private void showYearSearchResults(List<Car> cars, int fromYear, int toYear) {
        Component tabComponent = tabbedPane.getComponentAt(2);
        if (!(tabComponent instanceof JPanel)) return;

        JPanel taskPanel = (JPanel) tabComponent;
        Component[] components = taskPanel.getComponents();

        for (Component comp : components) {
            if (comp instanceof JScrollPane) {
                JTextArea textArea = (JTextArea) ((JScrollPane) comp).getViewport().getView();

                StringBuilder sb = new StringBuilder();
                sb.append("=============================================================\n");
                sb.append("           ПОИСК ПО ГОДАМ: ").append(fromYear).append(" - ").append(toYear).append("\n");
                sb.append("=============================================================\n\n");
                sb.append("Найдено машин: ").append(cars.size()).append("\n\n");

                int currentYear = 2025;
                int totalAge = 0;
                for (Car car : cars) {
                    totalAge += (currentYear - car.getYear());
                }
                double avgAge = (double) totalAge / cars.size();

                sb.append("-------------------------------------------------------------\n");
                sb.append("Средний возраст найденных авто: ").append(String.format("%.1f", avgAge)).append(" лет\n");
                sb.append("-------------------------------------------------------------\n\n");

                sb.append("СПИСОК АВТОМОБИЛЕЙ:\n");
                sb.append("-------------------------------------------------------------\n");
                int num = 1;
                for (Car car : cars) {
                    sb.append(String.format("%2d. %s %s (%d)\n",
                            num++, car.getManufacturer(), car.getModel(), car.getYear()));
                    sb.append(String.format("    VIN: %s | Пробег: %,d км | Цена: %,d RUB | Тип: %s\n",
                            car.getVin(), car.getMileage(), (int)car.getPrice(), car.getType()));
                    sb.append("-------------------------------------------------------------\n");
                }

                textArea.setText(sb.toString());
                break;
            }
        }
    }

    private JPanel createRenameModelPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(BG_COLOR);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        textArea.setEditable(false);
        textArea.setBackground(Color.WHITE);
        textArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JButton btnSearch = createStyledButton("Поиск", BUTTON_INFO);
        btnSearch.setPreferredSize(new Dimension(220, 45));
        btnSearch.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            sb.append("=============================================================\n");
            sb.append("           ЗАДАНИЕ 2: ПЕРЕИМЕНОВАНИЕ МОДЕЛЕЙ\n");
            sb.append("=============================================================\n\n");

            List<String> models = new ArrayList<>(Arrays.asList(
                    "Toyota Camry", "BMW X5", "Tesla Model 3", "Mercedes E-Class",
                    "Toyota Camry", "Audi A6", "Tesla Model S", "BMW X5",
                    "Honda Civic", "Tesla Model 3", "Volkswagen Golf", "Ford Focus"
            ));

            sb.append("1. ИСХОДНЫЙ СПИСОК (с дубликатами):\n");
            sb.append("-------------------------------------------------------------\n");
            for (int i = 0; i < models.size(); i++) {
                sb.append(String.format("%2d. %s\n", i+1, models.get(i)));
            }
            sb.append("\n");

            Map<String, Integer> countMap = new HashMap<>();
            for (String model : models) {
                countMap.put(model, countMap.getOrDefault(model, 0) + 1);
            }

            sb.append("2. НАЙДЕННЫЕ ДУБЛИКАТЫ:\n");
            sb.append("-------------------------------------------------------------\n");
            boolean hasDuplicates = false;
            for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
                if (entry.getValue() > 1) {
                    sb.append(String.format("   %s - встречается %d раз(а)\n", entry.getKey(), entry.getValue()));
                    hasDuplicates = true;
                }
            }
            if (!hasDuplicates) {
                sb.append("   Дубликаты не найдены\n");
            }
            sb.append("\n");

            Set<String> uniqueModels = new LinkedHashSet<>(models);
            sb.append("3. СПИСОК БЕЗ ДУБЛИКАТОВ:\n");
            sb.append("-------------------------------------------------------------\n");
            int num = 1;
            for (String model : uniqueModels) {
                sb.append(String.format("%2d. %s\n", num++, model));
            }
            sb.append("\n");

            List<String> sortedModels = new ArrayList<>(uniqueModels);
            sortedModels.sort(Collections.reverseOrder());
            sb.append("4. ОТСОРТИРОВАНО (Z-A):\n");
            sb.append("-------------------------------------------------------------\n");
            num = 1;
            for (String model : sortedModels) {
                sb.append(String.format("%2d. %s\n", num++, model));
            }
            sb.append("\n");

            List<String> finalModels = sortedModels.stream()
                    .map(m -> {
                        if (m.contains("Tesla")) {
                            return "ELECTRO_CAR (было: " + m + ")";
                        }
                        return m;
                    })
                    .collect(Collectors.toList());

            sb.append("5. ПОСЛЕ ЗАМЕНЫ Tesla НА ELECTRO_CAR:\n");
            sb.append("-------------------------------------------------------------\n");
            num = 1;
            for (String model : finalModels) {
                sb.append(String.format("%2d. %s\n", num++, model));
            }
            sb.append("\n");

            Set<String> finalSet = new HashSet<>(finalModels);
            sb.append("6. СОХРАНЕНО В SET: ").append(finalSet.size()).append(" уникальных моделей\n");

            textArea.setText(sb.toString());
        });

        panel.add(btnSearch);
        panel.add(Box.createVerticalStrut(20));
        panel.add(new JScrollPane(textArea));

        return panel;
    }

    private JPanel createTask3Panel() {
        JPanel panel = new JPanel();
        panel.setBackground(BG_COLOR);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        textArea.setEditable(false);
        textArea.setBackground(Color.WHITE);
        textArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JButton btnRun = createStyledButton("Выполнить задание 3", ACCENT_COLOR);
        btnRun.setPreferredSize(new Dimension(270, 45));
        btnRun.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            sb.append("=============================================================\n");
            sb.append("           ЗАДАНИЕ 3: EQUALS/HASHCODE\n");
            sb.append("=============================================================\n\n");

            Car car1 = new Car("VIN001", "Camry", "Toyota", 2020, 30000, 2000000, CarType.SEDAN);
            Car car2 = new Car("VIN001", "Camry", "Toyota", 2020, 50000, 1800000, CarType.SEDAN);
            Car car3 = new Car("VIN002", "Camry", "Toyota", 2020, 30000, 2000000, CarType.SEDAN);

            sb.append("ТЕСТОВЫЕ ДАННЫЕ:\n");
            sb.append("-------------------------------------------------------------\n");
            sb.append("Машина 1: ").append(car1.getVin()).append(" - ").append(car1.getModel()).append("\n");
            sb.append("Машина 2: ").append(car2.getVin()).append(" - ").append(car2.getModel()).append(" (тот же VIN)\n");
            sb.append("Машина 3: ").append(car3.getVin()).append(" - ").append(car3.getModel()).append(" (другой VIN)\n\n");

            sb.append("ПРОВЕРКА equals():\n");
            sb.append("-------------------------------------------------------------\n");
            sb.append("car1.equals(car2): ").append(car1.equals(car2)).append(" (одинаковый VIN)\n");
            sb.append("car1.equals(car3): ").append(car1.equals(car3)).append(" (разный VIN)\n");
            sb.append("car1.hashCode() == car2.hashCode(): ").append(car1.hashCode() == car2.hashCode()).append("\n\n");

            sb.append("ТЕСТ С HashSet:\n");
            sb.append("-------------------------------------------------------------\n");
            Set<Car> set = new HashSet<>();
            sb.append("Добавили car1: ").append(set.add(car1)).append("\n");
            sb.append("Добавили car2 (дубль VIN): ").append(set.add(car2)).append("\n");
            sb.append("Добавили car3: ").append(set.add(car3)).append("\n");
            sb.append("Итого в наборе: ").append(set.size()).append(" машины (дубль не добавился)\n");

            textArea.setText(sb.toString());
        });

        panel.add(btnRun);
        panel.add(Box.createVerticalStrut(20));
        panel.add(new JScrollPane(textArea));

        return panel;
    }

    private JPanel createTask4Panel() {
        JPanel panel = new JPanel();
        panel.setBackground(BG_COLOR);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        textArea.setEditable(false);
        textArea.setBackground(Color.WHITE);
        textArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JButton btnRun = createStyledButton("Выполнить задание 4 (Stream API)", SECONDARY_COLOR);
        btnRun.setPreferredSize(new Dimension(320, 45));
        btnRun.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            sb.append("=============================================================\n");
            sb.append("           ЗАДАНИЕ 4: STREAM API\n");
            sb.append("=============================================================\n\n");

            List<Car> cars = new ArrayList<>(dealership.getCarsSortedByYear());

            sb.append("1. МАШИНЫ С ПРОБЕГОМ < 50,000 КМ:\n");
            sb.append("-------------------------------------------------------------\n");
            List<Car> lowMileage = cars.stream()
                    .filter(c -> c.getMileage() < 50000)
                    .collect(Collectors.toList());
            sb.append("Найдено: ").append(lowMileage.size()).append(" машин\n\n");
            for (Car car : lowMileage) {
                sb.append(String.format("   - %s %s (%d) - %,d км\n",
                        car.getManufacturer(), car.getModel(), car.getYear(), car.getMileage()));
            }
            sb.append("\n");

            sb.append("2. ТОП-3 САМЫЕ ДОРОГИЕ:\n");
            sb.append("-------------------------------------------------------------\n");
            List<Car> top3 = cars.stream()
                    .sorted(Comparator.comparingDouble(Car::getPrice).reversed())
                    .limit(3)
                    .collect(Collectors.toList());
            for (int i = 0; i < top3.size(); i++) {
                sb.append(String.format("   %d. %s %s - %,d RUB\n",
                        i+1, top3.get(i).getManufacturer(), top3.get(i).getModel(), (int)top3.get(i).getPrice()));
            }
            sb.append("\n");

            double avgMileage = cars.stream()
                    .mapToInt(Car::getMileage)
                    .average()
                    .orElse(0);
            sb.append("3. СРЕДНИЙ ПРОБЕГ: ").append(String.format("%,d", (int)avgMileage)).append(" км\n\n");

            sb.append("4. ГРУППИРОВКА ПО ПРОИЗВОДИТЕЛЮ:\n");
            sb.append("-------------------------------------------------------------\n");
            Map<String, List<Car>> byManufacturer = cars.stream()
                    .collect(Collectors.groupingBy(Car::getManufacturer));
            byManufacturer.forEach((manufacturer, carList) ->
                    sb.append(String.format("   %s: %d шт.\n", manufacturer, carList.size())));

            textArea.setText(sb.toString());
        });

        panel.add(btnRun);
        panel.add(Box.createVerticalStrut(20));
        panel.add(new JScrollPane(textArea));

        return panel;
    }

    private JPanel createAddCarPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(BG_COLOR);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBackground(CARD_COLOR);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        formPanel.setMaximumSize(new Dimension(650, 380));

        JTextField txtVin = new JTextField();
        JTextField txtModel = new JTextField();
        JTextField txtManufacturer = new JTextField();
        JTextField txtYear = new JTextField();
        JTextField txtMileage = new JTextField();
        JTextField txtPrice = new JTextField();
        JComboBox<CarType> comboType = new JComboBox<>(CarType.values());

        txtVin.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtModel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtManufacturer.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtYear.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtMileage.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtPrice.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboType.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        formPanel.add(createStyledLabel("VIN:"));
        formPanel.add(txtVin);
        formPanel.add(createStyledLabel("Модель:"));
        formPanel.add(txtModel);
        formPanel.add(createStyledLabel("Производитель:"));
        formPanel.add(txtManufacturer);
        formPanel.add(createStyledLabel("Год выпуска:"));
        formPanel.add(txtYear);
        formPanel.add(createStyledLabel("Пробег (км):"));
        formPanel.add(txtMileage);
        formPanel.add(createStyledLabel("Цена (RUB):"));
        formPanel.add(txtPrice);
        formPanel.add(createStyledLabel("Тип:"));
        formPanel.add(comboType);

        panel.add(formPanel);
        panel.add(Box.createVerticalStrut(20));

        JButton btnAdd = createStyledButton("Добавить автомобиль", BUTTON_SUCCESS);
        btnAdd.setPreferredSize(new Dimension(270, 45));
        btnAdd.addActionListener(e -> {
            try {
                Car car = new Car(
                        txtVin.getText().trim(),
                        txtModel.getText().trim(),
                        txtManufacturer.getText().trim(),
                        Integer.parseInt(txtYear.getText().trim()),
                        Integer.parseInt(txtMileage.getText().trim()),
                        Double.parseDouble(txtPrice.getText().trim()),
                        (CarType) comboType.getSelectedItem()
                );

                if (dealership.addCar(car)) {
                    JOptionPane.showMessageDialog(this,
                            "Автомобиль успешно добавлен!",
                            "Успех",
                            JOptionPane.INFORMATION_MESSAGE);

                    txtVin.setText("");
                    txtModel.setText("");
                    txtManufacturer.setText("");
                    txtYear.setText("");
                    txtMileage.setText("");
                    txtPrice.setText("");

                    updateDashboard();
                    updateCarsTableDirect(new ArrayList<>(dealership.getCarsSortedByYear()));
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Автомобиль с таким VIN уже существует!",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Проверьте правильность ввода чисел!",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(btnAdd);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(HEADER_COLOR);
        return label;
    }

    private void updateDashboard() {
        List<Car> cars = new ArrayList<>(dealership.getCarsSortedByYear());

        System.out.println("Обновление статистики: " + cars.size() + " машин");

        // Обновляем значения через сохраненные ссылки
        if (lblTotalCarsValue != null) {
            lblTotalCarsValue.setText(String.valueOf(cars.size()));
            lblTotalCarsValue.setForeground(new Color(66, 165, 245));
        }

        double totalValue = cars.stream().mapToDouble(Car::getPrice).sum();
        if (lblTotalValueValue != null) {
            lblTotalValueValue.setText(String.format("%,d", (int)totalValue) + " RUB");
            lblTotalValueValue.setForeground(new Color(102, 187, 106));
        }

        double avgPrice = cars.size() > 0 ? cars.stream().mapToDouble(Car::getPrice).average().orElse(0) : 0;
        if (lblAvgPriceValue != null) {
            lblAvgPriceValue.setText(String.format("%,d", (int)avgPrice) + " RUB");
            lblAvgPriceValue.setForeground(new Color(255, 183, 77));
        }

        // Обновляем таблицу на главной
        tableModel.setRowCount(0);
        for (Car car : cars) {
            tableModel.addRow(new Object[]{
                    car.getVin(),
                    car.getManufacturer(),
                    car.getModel(),
                    car.getYear(),
                    String.format("%,d", car.getMileage()),
                    String.format("%,d", (int)car.getPrice()),
                    car.getType()
            });
        }

        System.out.println("Статистика обновлена!");
    }

    private void updateCarsTable(List<Car> cars) {
        updateCarsTableDirect(cars);
    }

    private void updateCarsTableDirect(List<Car> cars) {
        if (carsTableInTab != null) {
            DefaultTableModel model = (DefaultTableModel) carsTableInTab.getModel();
            model.setRowCount(0);
            for (Car car : cars) {
                model.addRow(new Object[]{
                        car.getVin(),
                        car.getManufacturer(),
                        car.getModel(),
                        car.getYear(),
                        String.format("%,d", car.getMileage()),
                        String.format("%,d", (int)car.getPrice()),
                        car.getType()
                });
            }
        }
    }

    private void filterCars(String manufacturer, CarType type) {
        List<Car> filtered = dealership.getCarsSortedByYear().stream()
                .filter(c -> manufacturer.equals("Все") || c.getManufacturer().equals(manufacturer))
                .filter(c -> type == null || c.getType() == type)
                .collect(Collectors.toList());

        updateCarsTableDirect(filtered);
    }
}