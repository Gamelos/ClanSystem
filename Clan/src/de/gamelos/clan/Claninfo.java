package de.gamelos.clan;

import java.sql.ResultSet;
import java.sql.SQLException;
import net.md_5.bungee.api.connection.ProxiedPlayer;



public class Claninfo {

public static boolean playerExists(String uuid){
		
		
		try {
			@SuppressWarnings("static-access")
			ResultSet rs = Main.mysql.querry("SELECT * FROM Claninfo WHERE UUID = '"+ uuid + "'");
			if(rs.next()){
				if( rs.getString("Clanname") != null){
					return true;
				}
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void setPlayerinClan(String clanname,String Clanshort, ProxiedPlayer p){
		if(!(playerExists(p.getUniqueId().toString()))){
			Main.mysql.update("INSERT INTO Claninfo(UUID, Clanname, Clanshort) VALUES ('" +p.getUniqueId().toString()+ "', '"+clanname+"', '"+Clanshort+"');");
		}
	}
	
	//get-----------------------------------------------------------------------------------------------------------------------------------
	public static String getClanname(String uuid){
		String i = null;
		if(playerExists(uuid)){
			try {
				@SuppressWarnings("static-access")
				ResultSet rs = Main.mysql.querry("SELECT * FROM Claninfo WHERE UUID = '"+ uuid + "'");
				
				if((!rs.next()) || (String.valueOf(rs.getString("Clanname")) == null));
				
				i = rs.getString("Clanname");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return i;
	}
	
	public static String getshort(String uuid){
		String i = null;
		if(playerExists(uuid)){
			try {
				@SuppressWarnings("static-access")
				ResultSet rs = Main.mysql.querry("SELECT * FROM Claninfo WHERE UUID = '"+ uuid + "'");
				
				if((!rs.next()) || (String.valueOf(rs.getString("Clanshort")) == null));
				
				i = rs.getString("Clanshort");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return i;
	}
	

	
	//set-----------------------------------------------------------------------------------------------------------------------------------
	
	public static void setClanname(String clanname,String clanshort, ProxiedPlayer p){
		
		if(playerExists(clanname)){
			Main.mysql.update("UPDATE Claninfo SET Clanname= '" + clanname+ "' WHERE UUID= '" + p.getUniqueId().toString()+ "';");	
		}else{
			setPlayerinClan(clanname, clanshort, p);
		}
		
	}
	
	public static void setClanshort(String clanname,String clanshort, ProxiedPlayer p){
		
		if(playerExists(clanname)){
			Main.mysql.update("UPDATE Claninfo SET Clanshort= '" + clanshort+ "' WHERE UUID= '" + p.getUniqueId().toString()+ "';");	
		}else{
			setPlayerinClan(clanname, clanshort, p);
		}
		
	}
	
	public static void delClan(String clanname){
		
			Main.mysql.update("DELETE FROM Claninfo WHERE Clanname = '"+clanname+"';");
	}	

public static void delPlayer(String uuid){
	
	if(playerExists(uuid)){
		Main.mysql.update("DELETE FROM Claninfo WHERE UUID = '"+uuid+"';");
	}
}

public static void setClanname1(String clanname,String clanshort){
	
		Main.mysql.update("UPDATE Claninfo SET Clanname= '" + clanname+ "' WHERE Clanshort= '" +clanshort+ "';");
	
}

public static void setClanshort1(String clanname,String clanshort){
	
		Main.mysql.update("UPDATE Claninfo SET Clanshort= '" + clanshort+ "' WHERE Clanname= '" + clanname+ "';");	
}
	
	
}