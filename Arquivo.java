
import java.io.*;
import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.lang.reflect.Constructor;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Arquivo<T extends Entidade> {
	static int numLinha=0;
	static HttpURLConnection connection;
    RandomAccessFile arquivo;
    String nomeArquivo;
    Constructor<T> construtor;
    static BufferedReader br;
    public Arquivo(Constructor<T> c, String n) throws Exception {
        nomeArquivo = n;
        construtor = c;
        arquivo = new RandomAccessFile(nomeArquivo, "rw");
        if(arquivo.length()<4)
            arquivo.writeInt(0);
    }
    
    public int incluir(T obj) throws Exception {
    	 
        arquivo.seek(0);
        int id = arquivo.readInt();
        id++;
        obj.setId(id);
        arquivo.seek(0);
        arquivo.writeInt(id);
        arquivo.seek(arquivo.length());
        arquivo.writeByte(' ');
        byte[] b = obj.getByteArray();
        arquivo.writeInt(b.length);
        arquivo.write(b);
        
        
        return id;
    }
    
    public Object[] listar() throws Exception {

        // Em um sistema real, o nÃºmero de registros serÃ¡ muito superior ao que
        // um ArrayList poderia comportar em memÃ³ria. Esta operaÃ§Ã£o estÃ¡ aqui
        // apenas para facilitar a depuraÃ§Ã£o do cÃ³digo
        ArrayList<T> lista = new ArrayList<>();
        arquivo.seek(4);
        byte lapide;
        byte[] b;
        int s;
        T obj;
        while(arquivo.getFilePointer()<arquivo.length()) {
            obj = construtor.newInstance();
            lapide = arquivo.readByte();
            s = arquivo.readInt();
            b = new byte[s];
            arquivo.read(b);
            obj.setByteArray(b);
            if(lapide==' ')
                lista.add(obj);
        }
        
        Object[] ls = lista.toArray();
        return ls;
    }
    
    public Entidade buscar(int id) throws Exception {
        
        T obj = construtor.newInstance();
        byte lapide;
        byte[] b;
        int s;
        
        arquivo.seek(4);
        while(arquivo.getFilePointer() < arquivo.length()) {
            lapide = arquivo.readByte();
            s = arquivo.readInt();
            b = new byte[s];
            arquivo.read(b);
            obj.setByteArray(b);
            if(lapide == ' ' && obj.getId()==id)
                return obj;
        }
        return null;
    }
    
    
    public boolean excluir(int id) throws Exception {
        
        T obj = construtor.newInstance();
        byte lapide;
        byte[] b;
        int s;
        long endereco;
        
        arquivo.seek(4);
        while(arquivo.getFilePointer() < arquivo.length()) {
            endereco = arquivo.getFilePointer();
            lapide = arquivo.readByte();
            s = arquivo.readInt();
            b = new byte[s];
            arquivo.read(b);
            obj.setByteArray(b);
            if(lapide == ' ' && obj.getId()==id) {
                arquivo.seek(endereco);
                arquivo.write('*');
                return true;
            }
        }
        return false;
    }
    
    public boolean alterar(T novoObj) throws Exception {

        T obj = construtor.newInstance();
        byte lapide;
        byte[] b;
        int s;
        long endereco;
        int id = novoObj.getId();
        
        arquivo.seek(4);
        while(arquivo.getFilePointer() < arquivo.length()) {
            endereco = arquivo.getFilePointer();
            lapide = arquivo.readByte();
            s = arquivo.readInt();
            b = new byte[s];
            arquivo.read(b);
            obj.setByteArray(b);
            if(lapide == ' ' && obj.getId()==id) {
                arquivo.seek(endereco);
                arquivo.write('*');
                
                arquivo.seek(arquivo.length());
                arquivo.writeByte(' ');
                b = novoObj.getByteArray();
                arquivo.writeInt(b.length);
                arquivo.write(b);
                
                return true;
            }
        }
        return false;

    }    
    
	public static void pesquisa(String palavra) throws IOException {
		try {
			FileInputStream arqEntrada = new FileInputStream("ArquivoInvertido2.txt");
			DataInputStream entrada = new DataInputStream(arqEntrada);

			boolean registroEncontrado = false;

			while (entrada.available() != 0) { 

				String linha = entrada.readLine(); 
				StringTokenizer listaPalavras = new StringTokenizer(linha); 

				String palavraLinha = listaPalavras.nextToken();

				if (palavraLinha.startsWith(palavra.toLowerCase())) {
					System.out.println(linha);
					System.out.println("Registro encontrado!\n");
					registroEncontrado = true;
					break;
				}
			}
			if (!registroEncontrado)
				System.out.println("Registro nao encontrado!\n");
		} catch (FileNotFoundException e) {
			System.out.println("O Arquivo informado não existe.\n" + e.toString());
		}

	}
    
    public static Map conteudoWEB(Map<String, String> lista, String enderecoProxy, int portaProxy) throws Exception {
		{
			ArrayList<String> tudo = new ArrayList<String>();
			Map<String, String> texto = new HashMap<String, String>();
			if (enderecoProxy != null && enderecoProxy != "") {
				System.setProperty("http.proxyHost", enderecoProxy);
				System.setProperty("http.proxyPort", portaProxy + "");
			}
			try {
				
				
				for (Map.Entry<String, String> element : lista.entrySet()) {
					String id_pagina = element.getKey();

					String url = element.getValue();
					System.out.println("PEGANDO URL ====" + url);
				
				URL endereco = new URL(url);
			
				// Cria uma conexÃ£o com o site
				connection = (HttpURLConnection) endereco.openConnection();
			
				// Conecta-se ao site
				connection.connect();

				// Obtem o conteÃºdo e armazena em um objeto BufferedReader
				 
			
				br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//				String teste;
				String s = "";

				for (int i = 0; null != ((s = br.readLine())); i++) {
				
					Document doc = Jsoup.parse(s.toString());

					String titulo = doc.getElementsByTag("h1").text();
					String paragrafo = doc.getElementsByTag("p").text();
					String topico = doc.getElementsByTag("ol").text();
					String strong = doc.getElementsByTag("strong").text();
					String div = doc.getElementsByTag("div").text();
					String negrito = doc.getElementsByTag("b").text();
					int size = paragrafo.length();
					if (size > 2) {
						String[] separarTexto = titulo.split(" ");

						separarTexto = paragrafo.split(" ");
						texto.put(titulo,String.valueOf(id_pagina));
						texto.put(paragrafo,String.valueOf(id_pagina));
						texto.put(topico,String.valueOf(id_pagina));
						texto.put(strong, String.valueOf(id_pagina));
						texto.put(div, String.valueOf(id_pagina));
						texto.put(negrito, String.valueOf(id_pagina));
					}}
					
				}
				
				
				
			} catch (Exception e) {
				e.printStackTrace();

			}
		System.out.println(texto);
			return texto;
		}
		
	}
    public void criarArquivo(Map<String, String> lista, ArrayList id) throws Exception {
		String enderecoProxy = null;
		int portaProxy = 0;
//		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("Arquivo.txt", true)));
		FileOutputStream arq = new FileOutputStream("ArquivoInvertido2.txt");
		DataOutputStream saida = new DataOutputStream(arq);
		PrintStream saida2 = new PrintStream(arq);

		// Exemplo de RequisiÃ§Ã£o
		Map<String, String> lista1 = conteudoWEB(lista, enderecoProxy, portaProxy);
		
		Map<String, String> ArqInvertidoDic = new HashMap<String, String>();
		
			
		for (Map.Entry<String, String> element : lista1.entrySet()) {
		
			
			String linha = element.getKey();
//			String linha = list.get(i).toString().replace(",", " ").replace("/", " ");
			StringTokenizer listaPalavras = new StringTokenizer(linha, " ");
			numLinha = numLinha+1;
			for (int j = 1; j < listaPalavras.countTokens(); j++) {
			
				String palavra = listaPalavras.nextToken();
			
			
				int numColuna = j;
				
				String novaLinhaColuna = " <" + element.getValue() + ", " + numLinha + ", " + numColuna + ">";

				if ((ArqInvertidoDic).containsKey(palavra)) {
					String valor = ArqInvertidoDic.get(palavra);
					ArqInvertidoDic.replace(palavra, valor + novaLinhaColuna);

				} else {
					ArqInvertidoDic.put(palavra, novaLinhaColuna);

				}
			}
			}
				
		for (Map.Entry<String, String> elemento : ArqInvertidoDic.entrySet()) {
		


			String key = elemento.getKey();

			String val = elemento.getValue();
		
			saida2.println(key + val);
		}
		
		saida.close();
		saida2.close();
		arq.close();		

	}
    
}
