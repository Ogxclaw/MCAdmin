package com.kirik.zen.bans;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class MCBansResolver {
	
	public static void checkPlayer(String uuid){
		if(Desktop.isDesktopSupported()){
			try {
				Desktop.getDesktop().browse(new URI("https://www.mcbans.com/player/" + uuid.replaceAll("-", "")));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
	}

}
