import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class ModernBankApp extends JFrame {
    private BankAccount account;
    private BankAccount otherAccount;

    private JLabel lblNumber, lblOwner, lblBalance, lblOpenDate, lblStatus;
    private JTextField txtDepositAmount, txtWithdrawAmount, txtTransferAmount;
    private JButton btnDeposit, btnWithdraw, btnTransfer, btnBlock, btnRefresh;
    private JLabel lblStatusMessage;

    private final Color PRIMARY_COLOR = new Color(138, 43, 226);
    private final Color SECONDARY_COLOR = new Color(255, 20, 147);
    private final Color ACCENT_COLOR = new Color(0, 212, 255);
    private final Color DARK_BG = new Color(20, 20, 35);
    private final Color CARD_BG = new Color(40, 40, 60);
    private final Color TEXT_LIGHT = new Color(255, 255, 255);
    private final Color TEXT_DIM = new Color(180, 180, 200);

    public ModernBankApp() {
        account = new BankAccount("Клиент");
        otherAccount = new BankAccount("Получатель");
        otherAccount.deposit(100000);

        setTitle("Premium Bank");
        setSize(550, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        refreshData();

        JOptionPane.showMessageDialog(this,
                "Добро пожаловать в Premium Bank!\n\n" +
                        "Номер счёта для перевода:\n" + otherAccount.getNumber(),
                "Premium Bank",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(15, 15, 35),
                        0, getHeight(), new Color(35, 20, 50)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                g2d.setColor(new Color(138, 43, 226, 40));
                g2d.fillOval(-100, 100, 300, 300);

                g2d.setColor(new Color(255, 20, 147, 30));
                g2d.fillOval(250, 400, 250, 250);

                g2d.setColor(new Color(0, 212, 255, 25));
                g2d.fillOval(100, 600, 200, 200);
            }
        };

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Логотип и заголовок
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setMaximumSize(new Dimension(500, 80));

        JLabel lblLogo = new JLabel("P");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 40));
        lblLogo.setForeground(PRIMARY_COLOR);

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);
        JLabel lblTitle = new JLabel("Premium Bank");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(TEXT_LIGHT);

        JLabel lblSubtitle = new JLabel("Премиальное обслуживание");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitle.setForeground(TEXT_DIM);

        titlePanel.add(lblTitle);
        titlePanel.add(lblSubtitle);

        headerPanel.add(lblLogo, BorderLayout.WEST);
        headerPanel.add(titlePanel, BorderLayout.CENTER);
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(headerPanel);
        mainPanel.add(Box.createVerticalStrut(20));

        // КОМПАКТНАЯ КАРТОЧКА БАЛАНСА
        JPanel balanceCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(138, 43, 226, 200),
                        getWidth(), getHeight(), new Color(255, 20, 147, 200)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                g2d.setColor(new Color(255, 255, 255, 40));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight()/2, 15, 15);

                g2d.setColor(new Color(255, 255, 255, 50));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 15, 15);
            }
        };

        balanceCard.setLayout(null);
        balanceCard.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        balanceCard.setPreferredSize(new Dimension(480, 130));
        balanceCard.setMaximumSize(new Dimension(480, 130));

        JLabel lblCardLabel = new JLabel("ОБЩИЙ БАЛАНС");
        lblCardLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblCardLabel.setForeground(new Color(230, 230, 240));
        lblCardLabel.setBounds(20, 15, 200, 20);
        balanceCard.add(lblCardLabel);

        lblBalance = new JLabel("0 ₽");
        lblBalance.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblBalance.setForeground(Color.WHITE);
        lblBalance.setBounds(20, 35, 250, 45);
        balanceCard.add(lblBalance);

        JLabel lblCardNumber = new JLabel("**** **** **** " + account.getNumber().substring(4));
        lblCardNumber.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblCardNumber.setForeground(new Color(220, 220, 235));
        lblCardNumber.setBounds(20, 85, 200, 20);
        balanceCard.add(lblCardNumber);

        // Иконка карты (прямоугольник)
        JPanel cardIconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(255, 255, 255, 200));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(5, 5, 50, 35, 5, 5);
                g2d.setColor(new Color(255, 215, 0, 150));
                g2d.fillRect(10, 10, 15, 10);
            }
        };
        cardIconPanel.setBounds(380, 35, 60, 45);
        cardIconPanel.setOpaque(false);
        balanceCard.add(cardIconPanel);

        // Чип
        JPanel chipPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(255, 215, 0, 150));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 5, 5);
                g2d.setColor(new Color(255, 215, 0, 200));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 5, 5);
            }
        };
        chipPanel.setBounds(380, 15, 35, 22);
        chipPanel.setOpaque(false);
        balanceCard.add(chipPanel);

        balanceCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(balanceCard);
        mainPanel.add(Box.createVerticalStrut(20));

        // Информация о счёте
        JPanel infoCard = createGlassCard();
        infoCard.setLayout(new GridLayout(4, 2, 15, 10));
        infoCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        infoCard.setPreferredSize(new Dimension(480, 160));
        infoCard.setMaximumSize(new Dimension(480, 160));

        infoCard.add(createInfoLabel(" Номер счёта:"));
        lblNumber = createInfoValue("");
        infoCard.add(lblNumber);

        infoCard.add(createInfoLabel(" Владелец:"));
        lblOwner = createInfoValue("");
        infoCard.add(lblOwner);

        infoCard.add(createInfoLabel(" Дата открытия:"));
        lblOpenDate = createInfoValue("");
        infoCard.add(lblOpenDate);

        infoCard.add(createInfoLabel(" Статус:"));
        lblStatus = createInfoValue("");
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        infoCard.add(lblStatus);

        infoCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(infoCard);
        mainPanel.add(Box.createVerticalStrut(20));

        // Панель операций
        JPanel operationsCard = createGlassCard();
        operationsCard.setLayout(new BoxLayout(operationsCard, BoxLayout.Y_AXIS));
        operationsCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        operationsCard.setMaximumSize(new Dimension(480, 320));

        JLabel lblOpsTitle = new JLabel("Быстрые операции");
        lblOpsTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblOpsTitle.setForeground(TEXT_LIGHT);
        lblOpsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        operationsCard.add(lblOpsTitle);
        operationsCard.add(Box.createVerticalStrut(20));

        operationsCard.add(createModernButtonRow("+ Пополнить", txtDepositAmount = new JTextField(10),
                btnDeposit = createModernButton("Пополнить", PRIMARY_COLOR)));
        btnDeposit.addActionListener(e -> deposit());
        operationsCard.add(Box.createVerticalStrut(15));

        operationsCard.add(createModernButtonRow("- Снять", txtWithdrawAmount = new JTextField(10),
                btnWithdraw = createModernButton("Снять", SECONDARY_COLOR)));
        btnWithdraw.addActionListener(e -> withdraw());
        operationsCard.add(Box.createVerticalStrut(15));

        JPanel transferRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        transferRow.setOpaque(false);
        transferRow.setMaximumSize(new Dimension(420, 40));

        JLabel lblTransferIcon = new JLabel(">>>");
        lblTransferIcon.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTransferIcon.setForeground(ACCENT_COLOR);
        transferRow.add(lblTransferIcon);

        txtTransferAmount = new JTextField(8);
        txtTransferAmount.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtTransferAmount.setPreferredSize(new Dimension(100, 35));
        txtTransferAmount.setBackground(new Color(50, 50, 70));
        txtTransferAmount.setForeground(TEXT_LIGHT);
        txtTransferAmount.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(138, 43, 226, 100), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        transferRow.add(txtTransferAmount);

        JLabel lblTo = new JLabel("->");
        lblTo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTo.setForeground(ACCENT_COLOR);
        transferRow.add(lblTo);

        JLabel lblAccNum = new JLabel(otherAccount.getNumber().substring(0, 4) + "...");
        lblAccNum.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblAccNum.setForeground(TEXT_DIM);
        transferRow.add(lblAccNum);

        btnTransfer = createModernButton(">", ACCENT_COLOR);
        btnTransfer.setPreferredSize(new Dimension(50, 35));
        btnTransfer.addActionListener(e -> transfer());
        transferRow.add(btnTransfer);

        operationsCard.add(transferRow);
        operationsCard.add(Box.createVerticalStrut(20));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        btnPanel.setOpaque(false);

        btnBlock = createModernButton("Блок", new Color(255, 165, 0));
        btnBlock.addActionListener(e -> toggleBlock());
        btnPanel.add(btnBlock);

        btnRefresh = createModernButton("Обновить", new Color(100, 100, 120));
        btnRefresh.setPreferredSize(new Dimension(90, 35));
        btnRefresh.addActionListener(e -> refreshData());
        btnPanel.add(btnRefresh);

        operationsCard.add(btnPanel);
        operationsCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(operationsCard);
        mainPanel.add(Box.createVerticalStrut(20));

        lblStatusMessage = new JLabel("Система готова", SwingConstants.CENTER);
        lblStatusMessage.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblStatusMessage.setForeground(ACCENT_COLOR);
        lblStatusMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(lblStatusMessage);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane);
    }

    private JPanel createGlassCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(50, 50, 75, 180));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(255, 255, 255, 20),
                        0, 50, new Color(255, 255, 255, 5)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), 100, 20, 20);

                g2d.setColor(new Color(255, 255, 255, 30));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 20, 20);
            }
        };
        return card;
    }

    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setForeground(TEXT_DIM);
        return label;
    }

    private JLabel createInfoValue(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT_LIGHT);
        return label;
    }

    private JPanel createModernButtonRow(String labelText, JTextField textField, JButton button) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(420, 40));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(TEXT_DIM);
        panel.add(label);

        textField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textField.setPreferredSize(new Dimension(120, 35));
        textField.setBackground(new Color(50, 50, 70));
        textField.setForeground(TEXT_LIGHT);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        panel.add(textField);

        button.setPreferredSize(new Dimension(110, 35));
        panel.add(button);

        return panel;
    }

    private JButton createModernButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(bgColor.darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(bgColor.brighter());
                } else {
                    g2d.setColor(bgColor);
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                g2d.setColor(new Color(255, 255, 255, 30));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight()/2, 10, 10);

                super.paintComponent(g);
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    private void refreshData() {
        lblNumber.setText(account.getNumber());
        lblOwner.setText(account.getOwnerName());
        lblBalance.setText(String.format("%,d ₽", account.getBalance()));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        lblOpenDate.setText(account.getOpenDate().format(formatter));

        if (account.isBlocked()) {
            lblStatus.setText("Заблокирован");
            lblStatus.setForeground(Color.RED);
        } else {
            lblStatus.setText("Активен");
            lblStatus.setForeground(new Color(0, 255, 100));
        }

        boolean blocked = account.isBlocked();
        btnDeposit.setEnabled(!blocked);
        btnWithdraw.setEnabled(!blocked);
        btnTransfer.setEnabled(!blocked);
    }

    private void deposit() {
        try {
            String text = txtDepositAmount.getText().trim();
            if (text.isEmpty()) {
                showMessage("Введите сумму", Color.ORANGE);
                return;
            }
            int amount = Integer.parseInt(text);
            if (amount <= 0) {
                showMessage("Сумма > 0", Color.ORANGE);
                return;
            }
            if (account.deposit(amount)) {
                showMessage("+" + amount + " ₽", new Color(0, 255, 100));
                refreshData();
                txtDepositAmount.setText("");
            }
        } catch (NumberFormatException e) {
            showMessage("Ошибка ввода", Color.RED);
        }
    }

    private void withdraw() {
        try {
            String text = txtWithdrawAmount.getText().trim();
            if (text.isEmpty()) {
                showMessage("Введите сумму", Color.ORANGE);
                return;
            }
            int amount = Integer.parseInt(text);
            if (amount <= 0) {
                showMessage("Сумма > 0", Color.ORANGE);
                return;
            }
            if (account.withdraw(amount)) {
                showMessage("-" + amount + " ₽", new Color(0, 255, 100));
                refreshData();
                txtWithdrawAmount.setText("");
            } else {
                showMessage("Недостаточно средств", Color.RED);
            }
        } catch (NumberFormatException e) {
            showMessage("Ошибка ввода", Color.RED);
        }
    }

    private void transfer() {
        try {
            String text = txtTransferAmount.getText().trim();
            if (text.isEmpty()) {
                showMessage("Введите сумму", Color.ORANGE);
                return;
            }
            int amount = Integer.parseInt(text);
            if (amount <= 0) {
                showMessage("Сумма > 0", Color.ORANGE);
                return;
            }
            if (account.transfer(otherAccount, amount)) {
                showMessage("Перевод " + amount + " ₽", new Color(0, 255, 100));
                refreshData();
                txtTransferAmount.setText("");
            } else {
                showMessage("Ошибка перевода", Color.RED);
            }
        } catch (NumberFormatException e) {
            showMessage("Ошибка ввода", Color.RED);
        }
    }

    private void toggleBlock() {
        account.setBlocked(!account.isBlocked());
        if (account.isBlocked()) {
            showMessage("Счёт заблокирован", Color.RED);
        } else {
            showMessage("Счёт разблокирован", new Color(0, 255, 100));
        }
        refreshData();
    }

    private void showMessage(String text, Color color) {
        lblStatusMessage.setText(text);
        lblStatusMessage.setForeground(color);
    }
}