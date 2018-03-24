package servidorbasico;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Scanner;
//import protocoloBasico.ProtocoloBasico;
import servidorbasico.control.GestorCliente;

public class ServidorBasico {
        
    public static void main(String[] args) {
        ServidorBasico a = new ServidorBasico();
        a.iniciar();
    }
    
    public ServidorBasico(){
        clientes = new ArrayList<>();
        numCliente = 0;
    }

    public void iniciar() {
        System.out.println("Iniciando aplicación..");
        hiloAtencion = new Thread(new Runnable() {
            @Override
            public void run() {
                atenderClientes();
                
            }
        });
        hiloAtencion.start();
    }
    
    public void atenderClientes(){
        int i = 0;
        int puerto = 24782;
        try {
            System.out.println("Esperando conexión..");
            srv = new ServerSocket(puerto);            
            while (i < 3 && hiloAtencion == Thread.currentThread()) {                
                    skt = srv.accept();
                    numCliente++;
                    System.out.println("Conexión iniciada por el Cliente#" + numCliente);                    
                    GestorCliente nuevoCliente = new GestorCliente(this, skt,numCliente);
                    clientes.add(nuevoCliente);
                    iniciarCanalComunicacion(nuevoCliente);
                                  
                    i++;
            }
            System.out.println("Se conectaron tres clientes ...");
            while (true)  {
                escribir();
            }
            
            
        } 
        catch (Exception e) {
            System.err.println(" Se perdió la conexión con el cliente ...");
        }
        finally{
            try {
                skt.close();
                srv.close();
            } catch (IOException ex) {
                System.err.println("Ocurrio un error con la entrada de datos ... ");
            }
            System.out.println("Servidor - Conexión cerrada ...");
        }
    }
    
    public void escribir(){
        Scanner msj = new Scanner(System.in);
        String mensaje = msj.nextLine();
        for(GestorCliente c : clientes){            
            c.escribir(mensaje);
        }
    }    
    
    
    public void iniciarCanalComunicacion(GestorCliente nuevoCliente){
        hiloCliente= new Thread(nuevoCliente);
        if(hiloCliente!=null){
            hiloCliente.start();
        }
        
    }
    
    
    
    //Atributos
    private ServerSocket srv;
    private Socket skt;
    private Thread hiloAtencion;
    private Thread hiloCliente;
    private ArrayList<GestorCliente> clientes;
    private int numCliente;
    
}
