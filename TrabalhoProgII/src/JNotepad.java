import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class JNotepad extends JFrame {

    // Declaração de componentes
    protected static JTextArea textArea;
    protected static JLabel statusLabel; // Para exibir o contador de caracteres
    public static String textoInicial = ""; // Salvando o texto inicial pra poder comparar se foi ou não alterado

    public static void main(String[] args) {
        JNotepad em = new JNotepad();
        em.setup();
        em.setVisible(true);
    }

    public void setup() {
        // Configurações da janela principal
        setSize(800, 600);
        setTitle("JNotepad - por Fábio H. Araújo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Listener para o menu Arquivo
        ActionListener menuArquivoListener = new ActionListener() {
            private String caminhoAtual = ""; // Caminho do arquivo atualmente aberto

            @Override
            public void actionPerformed(ActionEvent e) {
                JMenuItem menuItem = (JMenuItem) e.getSource();
                if (menuItem.getText().equalsIgnoreCase("sair")) {
                    System.exit(0);
                } else if (menuItem.getText().equalsIgnoreCase("abrir")) {
                    // Diálogo para escolher arquivo a ser aberto
                    JFileChooser fc = new JFileChooser();
                    fc.setDialogTitle("Abrir Arquivo");
                    FileNameExtensionFilter filtro = new FileNameExtensionFilter("Arquivos de Texto (.txt)", "txt");
                    fc.setFileFilter(filtro);

                    int result = fc.showOpenDialog(JNotepad.this);

                    if (result == JFileChooser.APPROVE_OPTION) {
                        // Se um arquivo for selecionado, chama o método Carregar da classe ManipulaArquivos
                        ManipulaArquivos.Carregar(fc.getSelectedFile().getAbsolutePath());
                        caminhoAtual = fc.getSelectedFile().getAbsolutePath();
                    }
                } else {
                    // Opções para salvar o arquivo
                    String caminhoArquivo = caminhoAtual;
                    if (caminhoAtual.equals("")) {
                        JFileChooser fc = new JFileChooser();
                        fc.setDialogTitle("Salvar Arquivo");
                        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Arquivos de Texto (.txt)", "txt");
                        fc.setFileFilter(filtro);

                        int result = fc.showSaveDialog(JNotepad.this);

                        if (result == JFileChooser.APPROVE_OPTION) {
                            // Se um local de salvamento for escolhido, chama o método Salvar da classe ManipulaArquivos
                            caminhoArquivo = fc.getSelectedFile().getAbsolutePath();
                            ManipulaArquivos.Salvar(caminhoArquivo, textArea.getText());
                            JOptionPane.showMessageDialog(JNotepad.this, "Arquivo salvo com sucesso!", "Salvo",
                                    JOptionPane.INFORMATION_MESSAGE);
                            // Salva o caminho do Arquivo
                            caminhoAtual = caminhoArquivo;
                        }
                    } else {
                        // Se já existe um caminho de arquivo, salva diretamente
                        ManipulaArquivos.Salvar(caminhoArquivo, textArea.getText());
                        JOptionPane.showMessageDialog(JNotepad.this, "Arquivo salvo com sucesso!", "Salvo",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        };

        // Listener para o menu Fontes
        ActionListener fonteListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JMenuItem menuItem = (JMenuItem) e.getSource();
                String fontName = menuItem.getText();
                int tamanhoFonte = fontName.equals("Arial") ? 14 : 24;
                Font fonte = new Font(fontName, Font.PLAIN, tamanhoFonte);
                textArea.setFont(fonte);
            }
        };

        // Configuração da barra de menus
        JMenuBar menu = new JMenuBar();
        JMenu arquivo = new JMenu("Arquivo");
        JMenu fontes = new JMenu("Fontes");
        JMenu ajuda = new JMenu("Ajuda");

        JMenuItem abrir = new JMenuItem("Abrir");
        JMenuItem salvar = new JMenuItem("Salvar");
        JMenuItem sair = new JMenuItem("Sair");

        JMenuItem sobre = new JMenuItem("Sobre");
        JMenuItem naoclica = new JMenuItem("Não clique aqui!");

        // Configuração da área de texto
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        // Configuração do painel de rolagem
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane);

        // Adiciona uma JLabel para exibir o contador de caracteres
        statusLabel = new JLabel("Caracteres: 0 Alterado: Não");
        statusLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        add(statusLabel, BorderLayout.SOUTH);

        // Configuração dos menus
        setJMenuBar(menu);
        menu.add(arquivo);
        menu.add(fontes);
        menu.add(ajuda);

        arquivo.add(abrir);
        arquivo.add(salvar);
        arquivo.add(sair);

        ajuda.add(sobre);
        ajuda.add(naoclica);

        // Ação quando clicar em "Sobre"
        sobre.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(JNotepad.this,
                        "JNotepad, um editor de texto simples.\n" + "Construído na disciplina de Programação II.",
                        "Sobre o JNotepad", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Isso aqui é segredo hein!
        naoclica.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Definitivamente, faz algo 
                    java.awt.Desktop.getDesktop().browse(new java.net.URI("https://www.youtube.com/watch?v=dQw4w9WgXcQ"));
                } catch (IOException | URISyntaxException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(JNotepad.this, "Deu erro :(", "Erro!", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Fontes disponíveis
        String[] fontesDesejadas = { "Arial", "Courier New" };

        for (String fonte : fontesDesejadas) {
            JMenuItem fonteItem = new JMenuItem(fonte);
            int tamanhoFonte = fonte.equals("Arial") ? 14 : 24;
            fonteItem.setFont(new Font(fonte, Font.PLAIN, tamanhoFonte));
            fonteItem.addActionListener(fonteListener);
            fontes.add(fonteItem);
        }

        // Ações para os itens do menu Arquivo
        abrir.addActionListener(menuArquivoListener);
        salvar.addActionListener(menuArquivoListener);
        sair.addActionListener(menuArquivoListener);

        // Adiciona um ouvinte de eventos para atualizar o contador de caracteres
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                // Atualiza o contador de caracteres quando um novo texto é inserido
            	if (!textoInicial.equals(textArea.getText())) {
            	    ManipulaArquivos.atualizarContador(true);
            	} else {
            	    ManipulaArquivos.atualizarContador(false);
            	}
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                // Atualiza o contador de caracteres quando um texto é removido
            	if (!textoInicial.equals(textArea.getText())) {
            	    ManipulaArquivos.atualizarContador(true);
            	} else {
            	    ManipulaArquivos.atualizarContador(false);
            	}
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Atualiza o contador de caracteres quando o texto é alterado
            	if (!textoInicial.equals(textArea.getText())) {
            	    ManipulaArquivos.atualizarContador(true);
            	} else {
            	    ManipulaArquivos.atualizarContador(false);
            	}
            }
        });
    }
}
