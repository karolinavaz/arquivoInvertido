
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Pagina implements Entidade {
    protected int    id;
    protected String url;

    LocalDate hoje = LocalDate.now();
   

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public String hojeFormatado = hoje.format(formatter);
                                           

  
    public Pagina(int c, String u){
        this.id = c;
        this.url = u;
        this.hojeFormatado = hojeFormatado;
      
    }
    public Pagina() {
        this.id = 0;
        this.url = "";
        this.hojeFormatado="";
    }
    
    public void setId(int c) {
        this.id = c;
    }
    
    public int getId() {
        return this.id;
    }
    
    public String toString() {
        return "\nCódigo: " + this.id +
               "\nSite: " + this.url+
               "\nData: " + this.hojeFormatado;
    }
    
    public byte[] getByteArray() throws IOException {
        ByteArrayOutputStream dados = new ByteArrayOutputStream();
        DataOutputStream saida = new DataOutputStream( dados );
        saida.writeInt(this.id);
        saida.writeUTF(this.url);
        saida.writeUTF(this.hojeFormatado);
        return dados.toByteArray();        
    }
    
    public void setByteArray(byte[] b) throws IOException {
        ByteArrayInputStream dados = new ByteArrayInputStream(b);
        DataInputStream entrada = new DataInputStream(dados);
        this.id = entrada.readInt();
        this.url = entrada.readUTF();
        this.hojeFormatado = entrada.readUTF();
    }


}
