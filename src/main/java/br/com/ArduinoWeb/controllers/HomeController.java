package br.com.ArduinoWeb.controllers;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fazecast.jSerialComm.SerialPort;

import br.com.ArduinoWeb.service.HomeService;



@Controller
public class HomeController {
	
	private String portaSelecionada = "";
	
	
	@RequestMapping(
			value = "/status", 
			method = RequestMethod.POST, 
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> statusPost(
			
			@RequestBody String body) {
		try {
			JSONObject json = new JSONObject(body);
			String info = json.getString("status");
			return enviaInfo(info);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(
			value = "/status", 
			method = RequestMethod.GET, 
			//consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> statusGet(
			) {
		try {
			return enviaInfo("status");
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(
			value = "/conexao", 
			method = RequestMethod.GET, 
			//consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> testeConexao(){//@RequestParam String portaSelecionada) {
		try {
//			SerialPort[] portNames = SerialPort.getCommPorts();
//			for(SerialPort porta:portNames) {
//				if(porta.getSystemPortName().contains(portaSelecionada)) {
//					this.portaSelecionada = porta.getSystemPortName();
//					return new ResponseEntity<String>("{\"retorno\":\""+HttpStatus.OK+"\"}",HttpStatus.OK);
//				}
//			}
			System.out.println("conectando...");
			return new ResponseEntity<String>("{\"retorno\":\""+HttpStatus.OK+"\"}",HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("{\"retorno\":\""+e.getMessage()+"\"}",HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(
			value ="/abrir",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String>ligarMotor(){
		try {
			return enviaInfo("abrir");
			
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(
			value ="/fechar",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String>desligarMotor(){
		try {
			return enviaInfo("fechar");
			
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(
			value ="/portas",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String>listarPortas(){
		try {
			SerialPort[] porta = SerialPort.getCommPorts();
			JSONArray array = new JSONArray();
			for(int i = 0;i<porta.length;i++) {
				JSONObject obj = new JSONObject();
				obj.put("porta"+i, porta[i].getSystemPortName());
				array.put(obj);
			}
			return new ResponseEntity<String>(array.toString(),HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private ResponseEntity<String> enviaInfo(String envio) throws Exception {
		HomeService homeService = new HomeService();
		SerialPort[] portNames = SerialPort.getCommPorts();
		for(SerialPort porta:portNames) {
			if(porta.getSystemPortName().contains(this.portaSelecionada)) {
				homeService.conectar(porta);
				homeService.enviaInformacaoPorta(envio, porta);
				String inf = homeService.carregaInformacaoPorta(porta);
				homeService.fechar(porta);
				System.out.println(inf);
				return new ResponseEntity<String>(inf,HttpStatus.OK);
			}
		}
		return null;
	}
}
