package ui;

import analyzer.CompileError;
import analyzer.Tokeninfo;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import service.CompilerService;

public class MainFrame extends JFrame {

    private final JTextArea inputArea;
    private final JTextArea outputArea;

    private final JTable tokensTable;
    private final JTable errorsTable;

    private final DefaultTableModel tokensModel;
    private final DefaultTableModel errorsModel;

    private final JTabbedPane reportsTabs;

    private final CompilerService compilerService;

    private File currentFile;

    public MainFrame() {

        compilerService =
                new CompilerService();

        setTitle("BattleScript");

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setSize(1200, 760);

        setMinimumSize(
                new Dimension(850, 600)
        );

        setLocationRelativeTo(null);

        inputArea = new JTextArea();
        outputArea = new JTextArea();

        Font editorFont =
                new Font(
                        Font.MONOSPACED,
                        Font.PLAIN,
                        15
                );

        inputArea.setFont(editorFont);

        outputArea.setFont(editorFont);

        outputArea.setEditable(false);

        tokensModel =
                new DefaultTableModel(
                        new Object[]{
                            "#",
                            "Lexema",
                            "Tipo",
                            "Línea",
                            "Columna"
                        },
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {

                        return false;
                    }
                };

        errorsModel =
                new DefaultTableModel(
                        new Object[]{
                            "#",
                            "Tipo",
                            "Descripción",
                            "Línea",
                            "Columna"
                        },
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {

                        return false;
                    }
                };

        tokensTable =
                new JTable(tokensModel);

        errorsTable =
                new JTable(errorsModel);

        reportsTabs =
                new JTabbedPane();

        reportsTabs.addTab(
                "Tokens",
                new JScrollPane(tokensTable)
        );

        reportsTabs.addTab(
                "Errores",
                new JScrollPane(errorsTable)
        );

        JScrollPane inputScroll =
                new JScrollPane(inputArea);

        JScrollPane outputScroll =
                new JScrollPane(outputArea);

        inputScroll.setBorder(
                BorderFactory
                        .createTitledBorder(
                                "Entrada"
                        )
        );

        reportsTabs.setBorder(
                BorderFactory
                        .createTitledBorder(
                                "Reportes"
                        )
        );

        outputScroll.setBorder(
                BorderFactory
                        .createTitledBorder(
                                "Salida"
                        )
        );

        JSplitPane topSplit =
                new JSplitPane(
                        JSplitPane.HORIZONTAL_SPLIT,
                        inputScroll,
                        reportsTabs
                );

        topSplit.setResizeWeight(0.62);

        topSplit.setDividerLocation(700);

        JSplitPane mainSplit =
                new JSplitPane(
                        JSplitPane.VERTICAL_SPLIT,
                        topSplit,
                        outputScroll
                );

        mainSplit.setResizeWeight(0.68);

        mainSplit.setDividerLocation(480);

        setJMenuBar(createMenu());

        add(
                mainSplit,
                BorderLayout.CENTER
        );
    }

    private JMenuBar createMenu() {

        JMenuBar menuBar =
                new JMenuBar();

        JMenu fileMenu =
                new JMenu("Archivo");

        JMenu reportsMenu =
                new JMenu("Reportes");

        JMenu executeMenu =
                new JMenu("Ejecutar");

        JMenuItem newItem =
                new JMenuItem("Nuevo");

        JMenuItem openItem =
                new JMenuItem("Abrir");

        JMenuItem saveItem =
                new JMenuItem("Guardar");

        JMenuItem tokensItem =
                new JMenuItem(
                        "Reporte de Tokens"
                );

        JMenuItem errorsItem =
                new JMenuItem(
                        "Reporte de Errores"
                );

        JMenuItem executeItem =
                new JMenuItem(
                        "Ejecutar código"
                );

        newItem.addActionListener(
                this::newFile
        );

        openItem.addActionListener(
                this::openFile
        );

        saveItem.addActionListener(
                this::saveFile
        );

        tokensItem.addActionListener(
                e ->
                    reportsTabs
                            .setSelectedIndex(0)
        );

        errorsItem.addActionListener(
                e ->
                    reportsTabs
                            .setSelectedIndex(1)
        );

        executeItem.addActionListener(
                this::executeSource
        );

        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);

        reportsMenu.add(tokensItem);
        reportsMenu.add(errorsItem);

        executeMenu.add(executeItem);

        menuBar.add(fileMenu);
        menuBar.add(reportsMenu);
        menuBar.add(executeMenu);

        return menuBar;
    }

    private void newFile(
            ActionEvent event
    ) {

        inputArea.setText("");
        outputArea.setText("");

        tokensModel.setRowCount(0);
        errorsModel.setRowCount(0);

        currentFile = null;

        setTitle("BattleScript");
    }

    private void openFile(
            ActionEvent event
    ) {

        JFileChooser chooser =
                createBtlChooser();

        int option =
                chooser.showOpenDialog(this);

        if (option
                != JFileChooser.APPROVE_OPTION) {

            return;
        }

        File selected =
                chooser.getSelectedFile();

        try {

            String content =
                    Files.readString(
                            selected.toPath(),
                            StandardCharsets.UTF_8
                    );

            inputArea.setText(content);

            currentFile = selected;

            setTitle(
                    "BattleScript - "
                    + currentFile.getName()
            );

        } catch (IOException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo abrir el archivo:\n"
                    + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void saveFile(
            ActionEvent event
    ) {

        if (currentFile == null) {

            JFileChooser chooser =
                    createBtlChooser();

            int option =
                    chooser.showSaveDialog(this);

            if (option
                    != JFileChooser.APPROVE_OPTION) {

                return;
            }

            File selected =
                    chooser.getSelectedFile();

            if (!selected.getName()
                    .toLowerCase()
                    .endsWith(".btl")) {

                selected =
                        new File(
                                selected.getParentFile(),
                                selected.getName()
                                + ".btl"
                        );
            }

            currentFile = selected;
        }

        try {

            Files.writeString(
                    currentFile.toPath(),
                    inputArea.getText(),
                    StandardCharsets.UTF_8
            );

            setTitle(
                    "BattleScript - "
                    + currentFile.getName()
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Archivo guardado correctamente."
            );

        } catch (IOException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo guardar el archivo:\n"
                    + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private JFileChooser createBtlChooser() {

        JFileChooser chooser =
                new JFileChooser();

        chooser.setFileFilter(
                new FileNameExtensionFilter(
                        "BattleScript (*.btl)",
                        "btl"
                )
        );

        chooser.setAcceptAllFileFilterUsed(
                false
        );

        return chooser;
    }

    private void executeSource(
            ActionEvent event
    ) {

        tokensModel.setRowCount(0);
        errorsModel.setRowCount(0);

        outputArea.setText("");

        CompilerService.Result result =
                compilerService.execute(
                        inputArea.getText()
                );

        for (Tokeninfo token
                : result.getTokens()) {

            tokensModel.addRow(
                    new Object[]{
                        token.getNumero(),
                        token.getLexema(),
                        token.getTipo(),
                        token.getLinea(),
                        token.getColumna()
                    }
            );
        }

        int number = 1;

        for (CompileError error
                : result.getErrors()) {

            Object line =
                    error.getLinea() <= 0
                            ? "-"
                            : error.getLinea();

            Object column =
                    error.getColumna() <= 0
                            ? "-"
                            : error.getColumna();

            errorsModel.addRow(
                    new Object[]{
                        number++,
                        error.getTipo(),
                        error.getDescripicion(),
                        line,
                        column
                    }
            );
        }

        outputArea.setText(
                result.getOutput()
        );

        outputArea.setCaretPosition(0);

        reportsTabs.setSelectedIndex(
                result.getErrors().isEmpty()
                        ? 0
                        : 1
        );
    }
}