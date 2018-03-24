

package servidorbasico.control;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import java.util.Scanner;
import servidorbasico.ServidorBasico;


public class GestorCliente implements Runnable {
    
    public GestorCliente(ServidorBasico gestor, Socket skt, int num) {
        gestorPrincipal = gestor;
        if(skt!=null){
            sktCliente = skt;
        }
        direccionCliente = sktCliente.getInetAddress();
        nCliente = num; 
    }

    @Override
    public void run() {
        try {            
            entrada =
                    new BufferedInputStream(sktCliente.getInputStream());
            
            salida = new PrintWriter(sktCliente.getOutputStream());
            
            while(true) {
                leer();                
            }
            
        } catch (IOException e) {
            System.err.println("Ocurrio un error con la entrada de datos ... ");
        }
        catch (Exception e) {
            System.err.println(" Se perdió la conexión con el cliente ...");
        }
        finally{
            salida.close();
            try {
                entrada.close();
            } catch (IOException ex) {
                System.err.println("Ocurrio un error con la entrada de datos ... ");
            }
            System.out.println("Servidor - Conexión cerrada ...");
        }
    } 
    
    public void escribir(String mensaje){        
        
        salida.println(mensaje);
        salida.flush();
    }
    
    public void leer(){
        Scanner respServidor = new Scanner(entrada);
        String c = respServidor.nextLine();
        System.out.println("Mensaje recibido del Cliente#" + nCliente + ": " + c);
    }
    
    @Override
    public String toString() {
        return String.format(
                "Cliente@%s", direccionCliente.getHostName(),nCliente);
    }
    
    
    private ServidorBasico gestorPrincipal;
    private InetAddress direccionCliente;
    private Socket sktCliente;
    private BufferedInputStream entrada;
    private PrintWriter salida;   
    private final int nCliente;
    
}
