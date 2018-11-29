package de.gamelos.clan;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;



public class MySQLRang {
	
	//TODO Rangnamen:
	//   - Admin
	//   - Mod
	//   - Sup
	//   - Builder
	//   - Youtuber
	//   - Eiszeit
	//   - Premium

public static boolean clanExists(String clanname){
		
		
		try {
			@SuppressWarnings("static-access")
			ResultSet rs = Main.mysql.querry("SELECT * FROM Clan WHERE Clanname = '"+ clanname + "'");
			if(rs.next()){
				return rs.getString("Clanshort") != null;
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

public static boolean shortExists(String so){
	
	
	try {
		@SuppressWarnings("static-access")
		ResultSet rs = Main.mysql.querry("SELECT * FROM Clan WHERE Clanshort = '"+ so + "'");
		if(rs.next()){
			return rs.getString("Clanname") != null;
		}
		return false;
	} catch (SQLException e) {
		e.printStackTrace();
	}
	return false;
}
	
	public static void createClan(String clanname,String Clanshort, ProxiedPlayer p){
		if(!(clanExists(clanname))){
			Main.mysql.update("INSERT INTO Clan(Clanname, Clanshort, Clanleader, Clanmod, Clanmember) VALUES ('" +clanname+ "', '"+Clanshort+"', '"+p.getUniqueId().toString()+"', '', '');");
		}
	}
	
	//get-----------------------------------------------------------------------------------------------------------------------------------
	public static String getShort(String clanname){
		String i = null;
		if(clanExists(clanname)){
			try {
				@SuppressWarnings("static-access")
				ResultSet rs = Main.mysql.querry("SELECT * FROM Clan WHERE Clanname = '"+ clanname + "'");
				
				if((!rs.next()) || (String.valueOf(rs.getString("Clanshort")) == null));
				
				i = rs.getString("Clanshort");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return i;
	}
	
	public static String getClanname(String tag){
		String i = null;
		if(shortExists(tag)){
			try {
				@SuppressWarnings("static-access")
				ResultSet rs = Main.mysql.querry("SELECT * FROM Clan WHERE Clanshort = '"+ tag + "'");
				
				if((!rs.next()) || (String.valueOf(rs.getString("Clanname")) == null));
				
				i = rs.getString("Clanname");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return i;
	}
	
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List getClanleader(String clanname){
		List list = new ArrayList<>();
		if(clanExists(clanname)){
				String i = "";
				try {
					@SuppressWarnings("static-access")
					ResultSet rs = Main.mysql.querry("SELECT * FROM Clan WHERE Clanname = '"+ clanname + "'");
					
					if((!rs.next()) || (String.valueOf(rs.getString("Clanleader")) == null));
					
					i = rs.getString("Clanleader");
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				if(i.contains(",")){
				String[] ss = i.split(",");
				for(String b : ss){
					if(!b.equals(" ")){
						list.add(b);
					}
					}
				}else{
					if(i.length()>5){
						list.add(i);
					}
				}
		}
		return list;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List getClanmod(String clanname){
		List list = new ArrayList<>();
		if(clanExists(clanname)){
				String i = "";
				
				try {
					@SuppressWarnings("static-access")
					ResultSet rs = Main.mysql.querry("SELECT * FROM Clan WHERE Clanname = '"+ clanname + "'");
					
					if((!rs.next()) || (String.valueOf(rs.getString("Clanmod")) == null));
					
					i = rs.getString("Clanmod");
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				if(i.contains(",")){
				String[] ss = i.split(",");
				for(String b : ss){
					if(!b.equals(" ")){
						list.add(b);
					}
			}
		}else{
			if(i.length()>5){
				list.add(i);
			}
		}
		}
		return list;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List getClanmember(String clanname){
		List list = new ArrayList<>();
		if(clanExists(clanname)){
				String i = "";
				
				try {
					@SuppressWarnings("static-access")
					ResultSet rs = Main.mysql.querry("SELECT * FROM Clan WHERE Clanname = '"+ clanname + "'");
					
					if((!rs.next()) || (String.valueOf(rs.getString("Clanmember")) == null));
					
					i = rs.getString("Clanmember");
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				if(i.contains(",")){
				String[] ss = i.split(",");
				for(String b : ss){
					if(!b.equals(" ")){
						list.add(b);
					}
			}
		}else{
			if(i.length()>5){
				list.add(i);
			}
		}
		}
		return list;
	}
	
	//set-----------------------------------------------------------------------------------------------------------------------------------
	
	@SuppressWarnings("unchecked")
	public static void addLeader(String clanname, String uuid){
		
		if(clanExists(clanname)){
			List<String> list = getClanleader(clanname);
			list.add(uuid);
			
			String sss = "";
			for(String l : list){
				sss = sss+l+",";
			}
			Main.mysql.update("UPDATE Clan SET Clanleader= '" + sss+ "' WHERE Clanname= '" + clanname+ "';");	
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public static void addMod(String clanname, String uuid){
		
		if(clanExists(clanname)){
			List<String> list = getClanmod(clanname);
			list.add(uuid);
			
			String sss = "";
			for(String l : list){
				sss = sss+l+",";
			}
			Main.mysql.update("UPDATE Clan SET Clanmod= '" + sss+ "' WHERE Clanname= '" + clanname+ "';");	
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public static void addmember(String clanname, String uuid){
		
		if(clanExists(clanname)){
			List<String> list = getClanmember(clanname);
			list.add(uuid);
			
			String sss = "";
			for(String l : list){
				sss = sss+l+",";
			}
			Main.mysql.update("UPDATE Clan SET Clanmember= '" + sss+ "' WHERE Clanname= '" + clanname+ "';");	
		}
		
	}
	
	

@SuppressWarnings("unchecked")
public static void delmember(String clanname, String uuid){
	
	if(clanExists(clanname)){
		List<String> list = getClanmember(clanname);
		if(list.contains(uuid)){
		list.remove(uuid);
		
		String sss = "";
		for(String l : list){
			sss = sss+l+",";
		}
		Main.mysql.update("UPDATE Clan SET Clanmember= '" + sss+ "' WHERE Clanname= '" + clanname+ "';");	
		}
	}
	
}
@SuppressWarnings("unchecked")
public static void delleader(String clanname, String uuid){
	
	if(clanExists(clanname)){
		List<String> list = getClanleader(clanname);
		if(list.contains(uuid)){
		list.remove(uuid);
		
		String sss = "";
		for(String l : list){
			sss = sss+l+",";
		}
		Main.mysql.update("UPDATE Clan SET Clanleader= '" + sss+ "' WHERE Clanname= '" + clanname+ "';");	
		}
	}
	
}
@SuppressWarnings("unchecked")
public static void delmod(String clanname, String uuid){
	
	if(clanExists(clanname)){
		List<String> list = getClanmod(clanname);
		if(list.contains(uuid)){
		list.remove(uuid);
		
		String sss = "";
		for(String l : list){
			sss = sss+l+",";
		}
		Main.mysql.update("UPDATE Clan SET Clanmod= '" + sss+ "' WHERE Clanname= '" + clanname+ "';");	
		}
	}
	
}

public static void delClan(String clanname){
	
	if(clanExists(clanname)){
		Main.mysql.update("DELETE FROM Clan WHERE Clanname = '"+clanname+"';");
	}
}

public static void setClanname(String clanname,String clanshort){
	
	if(shortExists(clanshort)){
		Main.mysql.update("UPDATE Clan SET Clanname= '" + clanname+ "' WHERE Clanshort= '" +clanshort+ "';");	
	}
	
}

public static void setClanshort(String clanname,String clanshort){
	
	if(clanExists(clanname)){
		Main.mysql.update("UPDATE Clan SET Clanshort= '" + clanshort+ "' WHERE Clanname= '" + clanname+ "';");	
	}
	
}
	
@SuppressWarnings("unchecked")
public static List<String> getmember(String clanname){
	List<String> player = new ArrayList<>();
	
	List<String> l1 = getClanmember(clanname);
	List<String> l2 = getClanleader(clanname);
	List<String> l3 = getClanmod(clanname);
	
		player.addAll(l1);
		player.addAll(l2);
		player.addAll(l3);
	
	return player;
}

}