import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.HashMap;

public class ReservasiGUI extends JFrame {

    // --- Enhanced Design System ---
    private final Color SB_BG         = new Color(15, 23, 42);
    private final Color SB_HOVER      = new Color(30, 41, 59);
    private final Color SB_SUB_BG     = new Color(2, 6, 23);
    private final Color TEXT_INACTIVE = new Color(148, 163, 184);
    private final Color ACCENT_COLOR  = new Color(59, 130, 246);
    private final Color ACCENT_HOVER  = new Color(37, 99, 235);
    private final Color SUCCESS_COLOR = new Color(34, 197, 94);
    private final Color WARNING_COLOR = new Color(251, 146, 60);
    private final Color BG_LIGHT      = new Color(248, 250, 252);
    private final Color CARD_BG       = Color.WHITE;
    private final Color BORDER_COLOR  = new Color(226, 232, 240);

    private CardLayout cardLayout;
    private JPanel pnlCards, pnlSubKategori;
    private JButton btnKategoriParent;
    private boolean isExpanded = false;
    private JButton btnAktif = null;
    private boolean isMaximized = false;
    private Rectangle normalBounds;

    private HashMap<String, Event> daftarEvent;
    private String kategoriTerpilih = "";

    private JLabel lblNamaEv, lblLokasiEv, lblHargaEv, lblSisaEv;
    private JTextField txtNama, txtEmail, txtJumlah;
    private JTextArea areaLog;
    private JProgressBar progressBar;
    private JButton btnMaximize;

    public ReservasiGUI() {
        initData();
        renderLayout();
    }

    private void initData() {
        daftarEvent = new HashMap<>();
        daftarEvent.put("Game", new Event("M-World Championship", "Istora Senayan", 150000, 80));
        daftarEvent.put("Musik", new Event("K-Pop World Tour", "JIS Jakarta", 2500000, 150));
        daftarEvent.put("Makanan", new Event("Bazaar Kuliner", "Monas", 25000, 300));
    }

    private void renderLayout() {
        setTitle("Event Management Enterprise");

        // Get screen dimensions
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int)(screenSize.width * 0.85);
        int height = (int)(screenSize.height * 0.90);

        setSize(width, height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Store normal bounds for maximize/restore
        normalBounds = new Rectangle(
                (screenSize.width - width) / 2,
                (screenSize.height - height) / 2,
                width,
                height
        );

        // Add modern look with shadow effect
        setUndecorated(true);

        // Main container with shadow effect
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBorder(new CompoundBorder(
                new LineBorder(new Color(15, 23, 42), 2),
                BorderFactory.createEmptyBorder()
        ));

        // Custom title bar
        JPanel titleBar = createTitleBar();
        mainContainer.add(titleBar, BorderLayout.NORTH);

        JPanel sidebar = createModernSidebar();

        cardLayout = new CardLayout();
        pnlCards = new JPanel(cardLayout);
        pnlCards.add(createWelcomePage(), "DASHBOARD");
        pnlCards.add(createProfilePage(), "PROFILE");
        pnlCards.add(createEventPage(), "EVENT_DETAIL");

        mainContainer.add(sidebar, BorderLayout.WEST);
        mainContainer.add(pnlCards, BorderLayout.CENTER);

        add(mainContainer);
        setVisible(true);
    }

    private JPanel createTitleBar() {
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(SB_BG);
        titleBar.setPreferredSize(new Dimension(1200, 50));
        titleBar.setBorder(new EmptyBorder(0, 25, 0, 10));

        JLabel titleLabel = new JLabel("⚡ Event Management Enterprise");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        titleLabel.setForeground(Color.WHITE);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 10));
        controlPanel.setOpaque(false);

        JButton btnMinimize = createTitleBarButton("─");
        btnMaximize = createTitleBarButton("□");
        JButton btnClose = createTitleBarButton("✕");

        btnMinimize.addActionListener(e -> setState(JFrame.ICONIFIED));
        btnMaximize.addActionListener(e -> toggleMaximize());
        btnClose.addActionListener(e -> System.exit(0));
        btnClose.setBackground(new Color(220, 38, 38));

        controlPanel.add(btnMinimize);
        controlPanel.add(btnMaximize);
        controlPanel.add(btnClose);

        titleBar.add(titleLabel, BorderLayout.WEST);
        titleBar.add(controlPanel, BorderLayout.EAST);

        // Enable dragging (only when not maximized)
        enableDragging(titleBar);

        return titleBar;
    }

    private void toggleMaximize() {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();

        if (!isMaximized) {
            // Save current bounds
            normalBounds = getBounds();

            // Get screen bounds (excluding taskbar)
            Rectangle screenBounds = device.getDefaultConfiguration().getBounds();
            Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(device.getDefaultConfiguration());

            // Set to maximized bounds
            setBounds(
                    screenBounds.x,
                    screenBounds.y + screenInsets.top,
                    screenBounds.width,
                    screenBounds.height - screenInsets.top - screenInsets.bottom
            );

            btnMaximize.setText("❐");
            isMaximized = true;
            setShape(null); // Remove rounded corners when maximized
        } else {
            // Restore to normal size
            setBounds(normalBounds);
            btnMaximize.setText("□");
            isMaximized = false;
        }
        revalidate();
        repaint();
    }

    private JButton createTitleBarButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(45, 35));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(30, 41, 59));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (!text.equals("✕")) {
                    btn.setBackground(new Color(51, 65, 85));
                } else {
                    btn.setBackground(new Color(239, 68, 68));
                }
            }
            public void mouseExited(MouseEvent e) {
                if (!text.equals("✕")) {
                    btn.setBackground(new Color(30, 41, 59));
                } else {
                    btn.setBackground(new Color(220, 38, 38));
                }
            }
        });

        return btn;
    }

    private void enableDragging(JPanel titleBar) {
        final Point[] dragOffset = {null};

        titleBar.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (!isMaximized) {
                    dragOffset[0] = e.getPoint();
                }
            }
            public void mouseReleased(MouseEvent e) {
                dragOffset[0] = null;
            }
            public void mouseClicked(MouseEvent e) {
                // Double click to maximize/restore
                if (e.getClickCount() == 2) {
                    toggleMaximize();
                }
            }
        });

        titleBar.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (dragOffset[0] != null && !isMaximized) {
                    Point location = getLocation();
                    setLocation(location.x + e.getX() - dragOffset[0].x,
                            location.y + e.getY() - dragOffset[0].y);
                }
            }
        });
    }

    private JPanel createModernSidebar() {
        JPanel sidebar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                // Create gradient background
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(15, 23, 42),
                        0, getHeight(), new Color(2, 6, 23)
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Add subtle pattern overlay
                g2d.setColor(new Color(255, 255, 255, 3));
                for (int i = 0; i < getHeight(); i += 4) {
                    g2d.drawLine(0, i, getWidth(), i);
                }
            }
        };

        sidebar.setPreferredSize(new Dimension(300, 800));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 0, 1, new Color(30, 41, 59)),
                new EmptyBorder(25, 0, 25, 0)
        ));

        // Logo Section with enhanced design
        JPanel logoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw circular background for icon
                int size = 80;
                int x = (getWidth() - size) / 2;
                int y = 10;

                // Outer glow
                g2d.setColor(new Color(59, 130, 246, 30));
                g2d.fillOval(x - 5, y - 5, size + 10, size + 10);

                // Main circle
                GradientPaint gp = new GradientPaint(
                        x, y, new Color(59, 130, 246),
                        x, y + size, new Color(37, 99, 235)
                );
                g2d.setPaint(gp);
                g2d.fillOval(x, y, size, size);
            }
        };

        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setOpaque(false);
        logoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoPanel.setMaximumSize(new Dimension(300, 200));

        JLabel logoIcon = new JLabel("🎫");
        logoIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 56));
        logoIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoIcon.setBorder(new EmptyBorder(35, 0, 0, 0));

        JLabel logo = new JLabel("EVENT PRO");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel tagline = new JLabel("Professional Event System");
        tagline.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        tagline.setForeground(new Color(148, 163, 184));
        tagline.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Decorative line
        JPanel decorLine = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(59, 130, 246, 0),
                        getWidth() / 2, 0, new Color(59, 130, 246, 255)
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth() / 2, 2);

                gp = new GradientPaint(
                        getWidth() / 2, 0, new Color(59, 130, 246, 255),
                        getWidth(), 0, new Color(59, 130, 246, 0)
                );
                g2d.setPaint(gp);
                g2d.fillRect(getWidth() / 2, 0, getWidth() / 2, 2);
            }
        };
        decorLine.setPreferredSize(new Dimension(200, 2));
        decorLine.setMaximumSize(new Dimension(200, 2));
        decorLine.setOpaque(false);
        decorLine.setAlignmentX(Component.CENTER_ALIGNMENT);

        logoPanel.add(logoIcon);
        logoPanel.add(Box.createVerticalStrut(15));
        logoPanel.add(logo);
        logoPanel.add(Box.createVerticalStrut(8));
        logoPanel.add(tagline);
        logoPanel.add(Box.createVerticalStrut(20));
        logoPanel.add(decorLine);

        sidebar.add(logoPanel);
        sidebar.add(Box.createVerticalStrut(30));

        // Navigation Section Label - CENTERED
        JLabel navLabel = new JLabel("NAVIGATION");
        navLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        navLabel.setForeground(new Color(100, 116, 139));
        navLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        navLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        sidebar.add(navLabel);

        // Center-aligned navigation buttons
        JButton btnDashboard = createNavButton("🏠  Dashboard", "DASHBOARD", true);
        btnDashboard.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(btnDashboard);
        sidebar.add(Box.createVerticalStrut(6));

        JButton btnProfile = createNavButton("👤  Profile", "PROFILE", true);
        btnProfile.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(btnProfile);
        sidebar.add(Box.createVerticalStrut(6));

        btnKategoriParent = createNavButton("📦  Kategori   ▸", "NONE", true);
        btnKategoriParent.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnKategoriParent.addActionListener(e -> toggleDropdown());
        sidebar.add(btnKategoriParent);

        pnlSubKategori = new JPanel();
        pnlSubKategori.setOpaque(false);
        pnlSubKategori.setLayout(new BoxLayout(pnlSubKategori, BoxLayout.Y_AXIS));
        pnlSubKategori.setVisible(false);
        pnlSubKategori.setBorder(new EmptyBorder(8, 0, 8, 0));
        pnlSubKategori.setAlignmentX(Component.CENTER_ALIGNMENT);

        String[][] menuItems = {
                {"🎮", "Game"},
                {"🎵", "Musik"},
                {"🍔", "Makanan"}
        };

        for (String[] item : menuItems) {
            JButton subBtn = createSubButton(item[0] + "  " + item[1]);
            subBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            subBtn.addActionListener(e -> {
                setButtonActive(subBtn);
                updateContent(item[1]);
            });
            pnlSubKategori.add(subBtn);
            pnlSubKategori.add(Box.createVerticalStrut(4));
        }
        sidebar.add(pnlSubKategori);

        sidebar.add(Box.createVerticalGlue());

        // Stats Section - CENTERED with border
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setOpaque(true);
        statsPanel.setBackground(new Color(30, 41, 59));
        statsPanel.setBorder(new CompoundBorder(
                new LineBorder(new Color(59, 130, 246, 50), 1),
                new EmptyBorder(15, 20, 15, 20)
        ));
        statsPanel.setMaximumSize(new Dimension(260, 100));
        statsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel statsTitle = new JLabel("QUICK STATS");
        statsTitle.setFont(new Font("Segoe UI", Font.BOLD, 10));
        statsTitle.setForeground(new Color(100, 116, 139));
        statsTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel totalEvents = new JLabel("📊 Total Events: 3");
        totalEvents.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        totalEvents.setForeground(new Color(148, 163, 184));
        totalEvents.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel activeUsers = new JLabel("👥 Active Users: 127");
        activeUsers.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        activeUsers.setForeground(new Color(148, 163, 184));
        activeUsers.setAlignmentX(Component.CENTER_ALIGNMENT);

        statsPanel.add(statsTitle);
        statsPanel.add(Box.createVerticalStrut(10));
        statsPanel.add(totalEvents);
        statsPanel.add(Box.createVerticalStrut(5));
        statsPanel.add(activeUsers);

        sidebar.add(statsPanel);
        sidebar.add(Box.createVerticalStrut(15));

        // Footer info - CENTERED
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.Y_AXIS));
        footerPanel.setOpaque(false);
        footerPanel.setBorder(new EmptyBorder(15, 25, 10, 25));
        footerPanel.setMaximumSize(new Dimension(300, 80));
        footerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel versionLabel = new JLabel("Version 2.0 Pro");
        versionLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        versionLabel.setForeground(ACCENT_COLOR);
        versionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel copyrightLabel = new JLabel("© 2024 Event Pro");
        copyrightLabel.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        copyrightLabel.setForeground(new Color(100, 116, 139));
        copyrightLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        footerPanel.add(versionLabel);
        footerPanel.add(Box.createVerticalStrut(5));
        footerPanel.add(copyrightLabel);

        sidebar.add(footerPanel);

        return sidebar;
    }

    private void setButtonActive(JButton selectedButton) {
        if (btnAktif != null) {
            btnAktif.setForeground(TEXT_INACTIVE);
            btnAktif.repaint();
        }
        btnAktif = selectedButton;
        animateButtonActivation(btnAktif);
    }

    private void animateButtonActivation(JButton btn) {
        btn.setForeground(Color.WHITE);
        btn.repaint();
    }

    private void toggleDropdown() {
        isExpanded = !isExpanded;

        pnlSubKategori.setVisible(isExpanded);

        btnKategoriParent.setText(
                isExpanded ? "📦  Kategori   ▾" : "📦  Kategori   ▸"
        );
        btnKategoriParent.setForeground(Color.WHITE);

        // 🔑 PENTING untuk fullscreen & resize
        pnlSubKategori.revalidate();
        pnlSubKategori.repaint();
    }


    private void updateContent(String key) {
        this.kategoriTerpilih = key;
        Event ev = daftarEvent.get(key);
        NumberFormat cur = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

        lblNamaEv.setText(ev.namaEvent);
        lblLokasiEv.setText(ev.lokasiEvent);
        lblHargaEv.setText(cur.format(ev.hargaEvent));
        lblSisaEv.setText(ev.sisaTiket + " / " + ev.kapasitasAwal);

        // Update progress bar
        int percentage = (ev.sisaTiket * 100) / ev.kapasitasAwal;
        progressBar.setValue(percentage);

        if (percentage > 50) {
            progressBar.setForeground(SUCCESS_COLOR);
        } else if (percentage > 20) {
            progressBar.setForeground(WARNING_COLOR);
        } else {
            progressBar.setForeground(new Color(239, 68, 68));
        }

        cardLayout.show(pnlCards, "EVENT_DETAIL");
    }

    private JPanel createEventPage() {
        JPanel p = new JPanel(new BorderLayout(25, 25));
        p.setBackground(BG_LIGHT);
        p.setBorder(new EmptyBorder(35, 45, 35, 45));

        // Header with Stats Cards
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel pageTitle = new JLabel("Event Details & Reservation");
        pageTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        pageTitle.setForeground(new Color(30, 41, 59));
        pageTitle.setBorder(new EmptyBorder(0, 0, 20, 0));
        header.add(pageTitle, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(1, 4, 20, 0));
        grid.setOpaque(false);

        lblNamaEv = new JLabel("-");
        lblLokasiEv = new JLabel("-");
        lblHargaEv = new JLabel("-");
        lblSisaEv = new JLabel("-");

        grid.add(createStatCard("🎯", "EVENT NAME", lblNamaEv, ACCENT_COLOR));
        grid.add(createStatCard("📍", "LOCATION", lblLokasiEv, new Color(139, 92, 246)));
        grid.add(createStatCard("💰", "PRICE", lblHargaEv, SUCCESS_COLOR));
        grid.add(createStatCard("🎫", "AVAILABILITY", lblSisaEv, WARNING_COLOR));

        header.add(grid, BorderLayout.CENTER);
        p.add(header, BorderLayout.NORTH);

        // Main Content Area
        JPanel mainContent = new JPanel(new GridLayout(1, 2, 25, 0));
        mainContent.setOpaque(false);

        // Left: Reservation Form
        mainContent.add(createReservationForm());

        // Right: Transaction Log
        mainContent.add(createTransactionLog());

        p.add(mainContent, BorderLayout.CENTER);

        return p;
    }

    private JPanel createReservationForm() {
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(CARD_BG);
        formContainer.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(30, 30, 30, 30)
        ));

        JLabel formTitle = new JLabel("📝 Reservation Form");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitle.setForeground(new Color(30, 41, 59));
        formTitle.setBorder(new EmptyBorder(0, 0, 25, 0));
        formContainer.add(formTitle, BorderLayout.NORTH);

        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, 20, 0);

        txtNama = createStyledTextField();
        txtEmail = createStyledTextField();
        txtJumlah = createStyledTextField();

        gbc.gridy = 0;
        pnlForm.add(createFieldLabel("👤 Nama Lengkap"), gbc);
        gbc.gridy = 1;
        pnlForm.add(txtNama, gbc);

        gbc.gridy = 2;
        pnlForm.add(createFieldLabel("📧 Email Address"), gbc);
        gbc.gridy = 3;
        pnlForm.add(txtEmail, gbc);

        gbc.gridy = 4;
        pnlForm.add(createFieldLabel("🎫 Jumlah Tiket"), gbc);
        gbc.gridy = 5;
        pnlForm.add(txtJumlah, gbc);

        // Progress Bar
        gbc.gridy = 6;
        gbc.insets = new Insets(10, 0, 5, 0);
        JLabel progressLabel = new JLabel("Ketersediaan Tiket");
        progressLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        progressLabel.setForeground(Color.GRAY);
        pnlForm.add(progressLabel, gbc);

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new Dimension(0, 25));
        progressBar.setForeground(SUCCESS_COLOR);
        progressBar.setBackground(new Color(226, 232, 240));
        progressBar.setBorderPainted(false);

        gbc.gridy = 7;
        gbc.insets = new Insets(0, 0, 25, 0);
        pnlForm.add(progressBar, gbc);

        JButton btnOrder = createPrimaryButton("KONFIRMASI RESERVASI");
        btnOrder.addActionListener(e -> eksekusiReservasi());

        gbc.gridy = 8;
        gbc.insets = new Insets(10, 0, 0, 0);
        pnlForm.add(btnOrder, gbc);

        formContainer.add(pnlForm, BorderLayout.CENTER);

        return formContainer;
    }

    private JPanel createTransactionLog() {
        JPanel logContainer = new JPanel(new BorderLayout());
        logContainer.setBackground(CARD_BG);
        logContainer.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(30, 30, 30, 30)
        ));

        JLabel logTitle = new JLabel("📊 Transaction History");
        logTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logTitle.setForeground(new Color(30, 41, 59));
        logTitle.setBorder(new EmptyBorder(0, 0, 20, 0));
        logContainer.add(logTitle, BorderLayout.NORTH);

        areaLog = new JTextArea();
        areaLog.setFont(new Font("Consolas", Font.PLAIN, 12));
        areaLog.setEditable(false);
        areaLog.setLineWrap(true);
        areaLog.setWrapStyleWord(true);
        areaLog.setBackground(new Color(248, 250, 252));
        areaLog.setBorder(new EmptyBorder(15, 15, 15, 15));

        JScrollPane scroll = new JScrollPane(areaLog);
        scroll.setBorder(new LineBorder(BORDER_COLOR));
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        logContainer.add(scroll, BorderLayout.CENTER);

        return logContainer;
    }

    private void eksekusiReservasi() {
        try {
            String nama = txtNama.getText().trim();
            String email = txtEmail.getText().trim();
            int jml = Integer.parseInt(txtJumlah.getText().trim());
            Event ev = daftarEvent.get(kategoriTerpilih);

            if (nama.isEmpty()) {
                showNotification("Nama tidak boleh kosong!", "error");
                return;
            }

            if (!email.endsWith("@gmail.com")) {
                showNotification("Email harus menggunakan @gmail.com!", "error");
                return;
            }

            if (jml <= 0) {
                showNotification("Jumlah tiket harus lebih dari 0!", "error");
                return;
            }

            if (ev.cekKetersediaan(jml)) {
                ev.kurangiKapasitas(jml);

                // Add to log with timestamp
                String timestamp = java.time.LocalDateTime.now()
                        .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                areaLog.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
                areaLog.append("✓ RESERVASI BERHASIL\n");
                areaLog.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
                areaLog.append("Nama      : " + nama + "\n");
                areaLog.append("Email     : " + email + "\n");
                areaLog.append("Event     : " + ev.namaEvent + "\n");
                areaLog.append("Tiket     : " + jml + " pcs\n");
                areaLog.append("Waktu     : " + timestamp + "\n");
                areaLog.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n");

                updateContent(kategoriTerpilih);
                showNotification("Reservasi berhasil dikonfirmasi!", "success");

                txtNama.setText("");
                txtEmail.setText("");
                txtJumlah.setText("");
            } else {
                showNotification("Tiket tidak mencukupi! Tersisa: " + ev.sisaTiket, "error");
            }
        } catch (NumberFormatException e) {
            showNotification("Jumlah tiket harus berupa angka!", "error");
        }
    }

    private void showNotification(String message, String type) {
        JOptionPane pane = new JOptionPane(message,
                type.equals("success") ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);

        JDialog dialog = pane.createDialog(this, type.equals("success") ? "Success" : "Error");

        Timer timer = new Timer(2000, e -> dialog.dispose());
        timer.setRepeats(false);
        timer.start();

        dialog.setVisible(true);
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(0, 40));
        field.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1),
                new EmptyBorder(8, 12, 8, 12)
        ));

        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                field.setBorder(new CompoundBorder(
                        new LineBorder(ACCENT_COLOR, 2),
                        new EmptyBorder(8, 12, 8, 12)
                ));
            }
            public void focusLost(FocusEvent e) {
                field.setBorder(new CompoundBorder(
                        new LineBorder(BORDER_COLOR, 1),
                        new EmptyBorder(8, 12, 8, 12)
                ));
            }
        });

        return field;
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(new Color(71, 85, 105));
        return label;
    }

    private JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(0, 45));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(ACCENT_COLOR);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(ACCENT_HOVER);
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(ACCENT_COLOR);
            }
        });

        return btn;
    }

    private JPanel createStatCard(String icon, String title, JLabel valueLabel, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(CARD_BG);
        card.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        titleLabel.setForeground(Color.GRAY);

        topPanel.add(iconLabel, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.EAST);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        valueLabel.setForeground(accentColor);

        card.add(topPanel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JButton createNavButton(String text, String cardName, boolean isMain) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (this == btnAktif) {
                    // Active state with gradient
                    GradientPaint gp = new GradientPaint(
                            0, 0, ACCENT_COLOR,
                            getWidth(), 0, new Color(37, 99, 235)
                    );
                    g2d.setPaint(gp);
                    g2d.fillRoundRect(10, 0, getWidth() - 20, getHeight(), 12, 12);

                    // Add left accent bar
                    g2d.setColor(Color.WHITE);
                    g2d.fillRoundRect(15, getHeight() / 4, 3, getHeight() / 2, 3, 3);
                } else if (getModel().isRollover()) {
                    // Hover state
                    g2d.setColor(SB_HOVER);
                    g2d.fillRoundRect(10, 0, getWidth() - 20, getHeight(), 12, 12);
                }

                super.paintComponent(g);
            }
        };

        btn.setMaximumSize(new Dimension(270, 52));
        btn.setPreferredSize(new Dimension(270, 52));
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setForeground(TEXT_INACTIVE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setHorizontalAlignment(SwingConstants.CENTER);
        btn.setBorder(new EmptyBorder(0, 20, 0, 20));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (!cardName.equals("NONE")) {
            btn.addActionListener(e -> {
                setButtonActive(btn);
                cardLayout.show(pnlCards, cardName);
            });
        }

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setForeground(Color.WHITE);
                btn.repaint();
            }
            public void mouseExited(MouseEvent e) {
                if (btn != btnAktif) {
                    btn.setForeground(TEXT_INACTIVE);
                }
                btn.repaint();
            }
        });

        return btn;
    }

    private JButton createSubButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (this == btnAktif) {
                    // Active state
                    g2d.setColor(new Color(59, 130, 246, 40));
                    g2d.fillRoundRect(25, 0, getWidth() - 50, getHeight(), 10, 10);

                    // Left indicator
                    g2d.setColor(ACCENT_COLOR);
                    g2d.fillRoundRect(30, getHeight() / 3, 3, getHeight() / 3, 3, 3);
                } else if (getModel().isRollover()) {
                    g2d.setColor(new Color(30, 41, 59, 80));
                    g2d.fillRoundRect(25, 0, getWidth() - 50, getHeight(), 10, 10);
                }

                super.paintComponent(g);
            }
        };

        btn.setMaximumSize(new Dimension(260, 44));
        btn.setPreferredSize(new Dimension(260, 44));
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setForeground(TEXT_INACTIVE);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setHorizontalAlignment(SwingConstants.CENTER);
        btn.setBorder(new EmptyBorder(0, 20, 0, 20));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setForeground(Color.WHITE);
                btn.repaint();
            }
            public void mouseExited(MouseEvent e) {
                if (btn != btnAktif) {
                    btn.setForeground(TEXT_INACTIVE);
                }
                btn.repaint();
            }
        });

        return btn;
    }

    private JPanel createWelcomePage() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(BG_LIGHT);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);

        JLabel welcomeIcon = new JLabel("🎉");
        welcomeIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 80));
        welcomeIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel welcomeText = new JLabel("Selamat Datang");
        welcomeText.setFont(new Font("Segoe UI", Font.BOLD, 36));
        welcomeText.setForeground(new Color(30, 41, 59));
        welcomeText.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subText = new JLabel("Portal Admin Event Management");
        subText.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subText.setForeground(Color.GRAY);
        subText.setAlignmentX(Component.CENTER_ALIGNMENT);

        content.add(welcomeIcon);
        content.add(Box.createVerticalStrut(20));
        content.add(welcomeText);
        content.add(Box.createVerticalStrut(10));
        content.add(subText);

        p.add(content);
        return p;
    }

    private JPanel createProfilePage() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG_LIGHT);
        p.setBorder(new EmptyBorder(40, 50, 40, 50));

        JLabel title = new JLabel("Profile Settings");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(30, 41, 59));
        title.setBorder(new EmptyBorder(0, 0, 30, 0));
        p.add(title, BorderLayout.NORTH);

        JPanel profileCard = new JPanel(new BorderLayout());
        profileCard.setBackground(CARD_BG);
        profileCard.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(40, 40, 40, 40)
        ));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel avatar = new JLabel("👤");
        avatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
        avatar.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nameLabel = new JLabel("Admin User");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        nameLabel.setForeground(new Color(30, 41, 59));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel roleLabel = new JLabel("System Administrator");
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        roleLabel.setForeground(Color.GRAY);
        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        infoPanel.add(avatar);
        infoPanel.add(Box.createVerticalStrut(20));
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(roleLabel);

        profileCard.add(infoPanel, BorderLayout.CENTER);
        p.add(profileCard, BorderLayout.CENTER);

        return p;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new ReservasiGUI().setVisible(true);
        });
    }

}