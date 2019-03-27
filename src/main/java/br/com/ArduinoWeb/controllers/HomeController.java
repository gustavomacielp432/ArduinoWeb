package br.com.ArduinoWeb.controllers;

import java.io.PrintWriter;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fazecast.jSerialComm.SerialPort;


@Controller
public class HomeController {

	@RequestMapping("/")
	public String index() {
		SerialPort[] portNames = SerialPort.getCommPorts();
		for(SerialPort portName : portNames) {
			portName.openPort();
			
			PrintWriter output = new PrintWriter(portName.getOutputStream());
			output.print(1);
			output.flush();
			portName.closePort();
			
		}
		return "home";
	}
	
	@RequestMapping("/desliga")
	public String desliga() {
		SerialPort[] portNames = SerialPort.getCommPorts();
		for(SerialPort portName : portNames) {
			portName.openPort();
			
			PrintWriter output = new PrintWriter(portName.getOutputStream());
			output.print(0);
			output.flush();
			portName.closePort();
			
		}
		return "desliga";
	}
}
