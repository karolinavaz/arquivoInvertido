

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Pagina_CRUD {

    private static Scanner console = new Scanner(System.in);
    private static Arquivo<Pagina> arqPaginas;

    /**
     * MÃ©todo principal, cujo objetivo Ã© criar uma interface para o usuÃ¡rio
     */
    public static void main(String[] args) {

        try {

            arqPaginas = new Arquivo<>(Pagina.class.getConstructor(), "ArquivoInvertido.db");
            
            // menu
           int opcao;
           do {
               System.out.println("\n\nGESTÃO DE PÁGINAS");
               System.out.println("-----------------------------\n");
               System.out.println("1 - Listar");
               System.out.println("2 - Incluir");
               System.out.println("3 - Alterar");
               System.out.println("4 - Excluir");
               System.out.println("5 - Buscar");
               System.out.println("6 - Gerar Arquivo Invertido");
               System.out.println("7 - Pesquisar no Arquivo Invertido");
               System.out.println("0 - Sair");
               System.out.print("\nOpcao: ");
               opcao = Integer.valueOf(console.nextLine());
               
               switch(opcao) {
                   case 1: listarPaginas(); break;
                   case 2: incluirPagina(); break;
                   case 3: alterarPagina(); break;
                   case 4: excluirPagina(); break;
                   case 5: buscarPagina(); break;
                   case 6: gerarArquivoInvertido(); break;
                   case 7: pesquisarArquivoInvertido(); break;

                   case 0: break;
                   default: System.out.println("Opção inválida");
               }
               
           } while(opcao!=0);
       } catch(Exception e) {
           e.printStackTrace();
       }
       }
    

   public static void listarPaginas() throws Exception {
       
       Object[] obj = arqPaginas.listar();
       
       System.out.println("\nLISTA DE PÁGINAS");
       for(int i=0; i<obj.length; i++) {
           System.out.println((Pagina)obj[i]);
       }
       pausa();
       
   }
   
   public static void incluirPagina() throws Exception {
       
       String url;
       
       System.out.println("\nINCLUSÃO DE PÁGINA");
       System.out.print("URL: ");
       url = console.nextLine();
       
       
       System.out.print("\nConfirma inclusão? ");
       char confirma = console.nextLine().charAt(0);
       if(confirma=='s' || confirma=='S') {
           Pagina obj = new Pagina(-1, url);
           int id = arqPaginas.incluir(obj);
           System.out.println("Página incluída com ID: "+id);
       }

       pausa();
   }

   
   public static void alterarPagina() throws Exception {
       
       System.out.println("\nALTERAÇÃO DE PÁGINA");

       int id;
       System.out.print("ID da página: ");
       id = Integer.valueOf(console.nextLine());
       if(id <=0) 
           return;
       
       Pagina obj;
       if( (obj = (Pagina)arqPaginas.buscar(id))!=null ) {
            System.out.println(obj);
            
            String url;
           
            
            System.out.print("\nNovo URL: ");
            url = console.nextLine();
          

            if(url.length()>0 ) {
                System.out.print("\nConfirma alteração? ");
                char confirma = console.nextLine().charAt(0);
                if(confirma=='s' || confirma=='S') {

                obj.url = (url.length()>0 ? url : obj.url);
              

                if( arqPaginas.alterar(obj) ) 
                        System.out.println("Pagina alterada.");
                    else
                        System.out.println("Pagina não pode ser alterada.");
                }
            }
       }
       else
           System.out.println("Pagina não encontrada");
       pausa();
       
   }
  
   
   public static void excluirPagina() throws Exception {
       
       System.out.println("\nEXCLUSÃO DE PÁGINA");

       int id;
       System.out.print("ID da página: ");
       id = Integer.valueOf(console.nextLine());
       if(id <=0) 
           return;
       
       Pagina obj;
       if( (obj = (Pagina)arqPaginas.buscar(id))!=null ) {
            System.out.println(obj);
            
            System.out.print("\nConfirma exclusão? ");
            char confirma = console.nextLine().charAt(0);
            if(confirma=='s' || confirma=='S') {
                if( arqPaginas.excluir(id) ) {
                    System.out.println("Página excluída.");
                }
            }
       }
       else
           System.out.println("Página não encontrada");
       pausa();
       
   }
   
   
   public static void buscarPagina() throws Exception {
       
       System.out.println("\nBUSCA DE PÁGINA POR CÓDIGO");
       
       int codigo;
       System.out.print("Código: ");
       codigo = Integer.valueOf(console.nextLine());
       if(codigo <=0) 
           return; 
       
       Pagina obj;
       if( (obj = (Pagina)arqPaginas.buscar(codigo))!=null )
           System.out.println(obj);
       else
           System.out.println("Página não encontrada");
       pausa();
       
   }

    public static void pausa() throws Exception {
        System.out.println("\nPressione ENTER para continuar ...");
        console.nextLine();
    }
    

    public static void gerarArquivoInvertido() throws Exception{
    	
        System.out.println("\nGERAR ARQUIVO INVERTIDO");
        Object[] obj = arqPaginas.listar();
        Pagina aux=null;
        Map<String, String> lista = new HashMap<String, String>();
        ArrayList<String> id = new ArrayList<String>();
       System.out.println("ESTE É O TAMANHO"+obj.length);
        for(int i=0; i<=obj.length+1; i++) {
        	
        	 if( (aux = (Pagina)arqPaginas.buscar(i))!=null ) {
        		 
        		 
        		 id.add(String.valueOf(aux.id));
        		 lista.put(String.valueOf(aux.id), aux.url);
        	
        }
        }
        arqPaginas.criarArquivo(lista, id);
    }
    
    public static void pesquisarArquivoInvertido() throws Exception {
    	  System.out.println("\nPESQUISAR NO ARQUIVO INVERTIDO");
    	  System.out.println("\nDigite a palavra para busca: ");
    	  String palavra= console.nextLine();
    	arqPaginas.pesquisa(palavra);
    	pausa();
    }
}

