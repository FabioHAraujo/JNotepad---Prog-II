import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.swing.JOptionPane;

public class ManipulaArquivos extends JNotepad {
    // Variáveis estáticas para armazenar o caminho do arquivo, número de caracteres e sinalizador de modificação
    public static String caminho = "";
    protected static int numeroCaracteres = 0;
    protected static boolean modificado = false;

    // Método para carregar o conteúdo de um arquivo na textArea
    protected static void Carregar(String caminhoArquivo) {
        // Atualiza o caminho do arquivo
        ManipulaArquivos.caminho = caminhoArquivo;

        // Verifica se há texto não salvo e pergunta ao usuário se deseja continuar
        if (numeroCaracteres != 0 && modificado) {
            int resposta = JOptionPane.showConfirmDialog(null, 
                    "Você tem texto não salvo. Carregar outro arquivo irá sobrescrevê-lo. Deseja continuar?", 
                    "Confirmação", JOptionPane.YES_NO_OPTION);

            if (resposta == JOptionPane.YES_OPTION) {
                // O usuário escolheu "Sim"
                try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
                    StringBuilder conteudo = new StringBuilder();
                    String linha;
                    boolean primeiraLinha = true;

                    // Lê cada linha do arquivo e a adiciona ao StringBuilder
                    while ((linha = br.readLine()) != null) {
                        if (!primeiraLinha) {
                            conteudo.append("\n");
                        } else {
                            primeiraLinha = false;
                        }
                        conteudo.append(linha);
                    }

                    // Define o conteúdo na textArea, exibe mensagem e atualiza textoInicial
                    textArea.setText(conteudo.toString());
                    JOptionPane.showMessageDialog(null, "Arquivo carregado!", "Texto Sobrescrito", JOptionPane.INFORMATION_MESSAGE);
                    textoInicial = textArea.getText();
                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erro ao carregar o arquivo.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // O usuário escolheu "Não" ou fechou a caixa de diálogo
                JOptionPane.showMessageDialog(null, "Operação cancelada. Seu texto atual foi mantido.", 
                		"Operação Cancelada", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            // Código de carregamento sem adicionar automaticamente uma nova linha
            try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
                StringBuilder conteudo = new StringBuilder();
                String linha;
                boolean primeiraLinha = true;

                // Lê cada linha do arquivo e a adiciona ao StringBuilder
                while ((linha = br.readLine()) != null) {
                    if (!primeiraLinha) {
                        conteudo.append("\n");
                    } else {
                        primeiraLinha = false;
                    }
                    conteudo.append(linha);
                }

                // Define o conteúdo na textArea, exibe mensagem e atualiza textoInicial
                textArea.setText(conteudo.toString());
                JOptionPane.showMessageDialog(null, "Arquivo carregado com sucesso!", "Arquivo carregado", JOptionPane.INFORMATION_MESSAGE);
                textoInicial = textArea.getText();
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erro ao carregar o arquivo.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
            
            // Atualiza o contador de caracteres
            atualizarContador(false);
        }
    }

 // Método para salvar o conteúdo da textArea em um arquivo
    protected static void Salvar(String caminhoArquivo, String conteudo) {
        try {
            // Verifica se o caminho do arquivo já possui a extensão .txt
            if(caminhoArquivo.endsWith(".txt")) {
                // Se sim, escreve o conteúdo diretamente no arquivo
                Files.write(Paths.get(caminhoArquivo), conteudo.getBytes());
            } else {
                // Se não, concatena a extensão .txt ao caminho e escreve o conteúdo no arquivo
                caminhoArquivo = caminhoArquivo.concat(".txt");
                Files.write(Paths.get(caminhoArquivo), conteudo.getBytes());
            }

            // Atualiza o contador de caracteres após a operação de salvar
            atualizarContador(false);

        } catch (IOException e) {
            // Em caso de exceção de E/S, imprime o rastreamento da pilha e exibe uma mensagem de erro
            e.printStackTrace();
            System.err.println("Erro ao salvar o arquivo.");
        }

        // Atualiza o texto inicial para o conteúdo atual da textArea
        textoInicial = textArea.getText();
    }

    // Método para atualizar o contador de caracteres e o statusLabel
    public static void atualizarContador(boolean alterado) {
        // Converte o booleano alterado em uma string "Sim" ou "Não"
        String estado = (alterado ? "Sim" : "Não");

        // Se o texto foi alterado, marca o sinalizador modificado como verdadeiro
        if (alterado) modificado = true;

        // Acessa a textArea para obter o número de caracteres
        numeroCaracteres = JNotepad.textArea.getText().length();

        // Define o texto do statusLabel com o número de caracteres e o estado de alteração
        JNotepad.statusLabel.setText("Caracteres: " + numeroCaracteres + " Alterado: " + estado);
    }
}
