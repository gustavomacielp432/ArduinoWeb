package br.com.ArduinoWeb.service;

import java.io.InputStream;
import java.io.PrintWriter;

import com.fazecast.jSerialComm.SerialPort;

public class HomeService {
	
	public void enviaInformacaoPorta(String inf,SerialPort porta) throws Exception{
		
		System.out.println("Enviando informações");
		try {
			PrintWriter output = new PrintWriter(porta.getOutputStream());
			output.print(inf);
			output.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	public String carregaInformacaoPorta(SerialPort porta){
		
		System.out.println("carregando informações");
		String info = "";
		char retorno = 0;
		try {
			InputStream in = porta.getInputStream();
			while(true) {
				retorno = (char)in.read();
				if(retorno!=';') {
					info+=retorno;
				}else {
					break;
				}
			}
		    in.close();	
		}catch(Exception e) {
			e.printStackTrace();
		}
		return info;
	}
	
	public boolean conectar(SerialPort porta) {
		porta.setComPortParameters(9600, 8, 1, 0);
        porta.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
        if (porta.openPort()) {
        	return true;
        }
        return false;
	}
	public void fechar(SerialPort porta) {
		porta.closePort();
	}
}
