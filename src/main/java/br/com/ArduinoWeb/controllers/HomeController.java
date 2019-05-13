package br.com.ArduinoWeb.controllers;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fazecast.jSerialComm.SerialPort;

import br.com.ArduinoWeb.service.HomeService;



@Controller
public class HomeController {
	@RequestMapping(
			value = "/status", 
			method = RequestMethod.POST, 
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> statusPost(
			@RequestParam String token,
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
			@RequestParam String token) {
		try {
			return enviaInfo("1");
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
	public ResponseEntity<String> testeConexao() {
		try {
			System.out.println("testando conexão");
			return new ResponseEntity<String>("{\"retorno\":\""+HttpStatus.OK+"\"}",HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("{\"retorno\":\""+e.getMessage()+"\"}",HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(
			value ="/ligar",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String>ligarMotor(@RequestParam String token){
		try {
			System.out.println("ligar");
			return enviaInfo("1");
			
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(
			value ="/desligar",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String>desligarMotor(@RequestParam String token){
		try {
			return enviaInfo("0");
			
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private ResponseEntity<String> enviaInfo(String envio) throws Exception {
		HomeService homeService = new HomeService();
		SerialPort[] portNames = SerialPort.getCommPorts();
		SerialPort porta = portNames[0];
		homeService.conectar(porta);
		homeService.enviaInformacaoPorta(envio, porta);
		String inf = homeService.carregaInformacaoPorta(porta);
		homeService.fechar(porta);
		System.out.println(inf);
		return new ResponseEntity<String>(inf,HttpStatus.OK);
	}
}
