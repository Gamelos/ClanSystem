package de.gamelos.clan;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class clan extends Command {

	public clan(String name) {
		super(name);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		
		ProxiedPlayer p = (ProxiedPlayer) sender;
		
		
		if(args.length>0){
			
			if(args[0].equals("help")){
				p.sendMessage(ChatColor.GRAY+"========================");
				p.sendMessage(ChatColor.YELLOW+"/clan create <Clanname> <Clankürzel>"+ChatColor.GRAY+"Erstelle einen Clan");
				p.sendMessage(ChatColor.YELLOW+"/clan delete "+ChatColor.GRAY+"Löscht deinen Clan");
				p.sendMessage(ChatColor.YELLOW+"/clan invite <Spieler> "+ChatColor.GRAY+"Läd einen Spieler in deinen Clan ein");
				p.sendMessage(ChatColor.YELLOW+"/clan leave "+ChatColor.GRAY+"Trete deinen Clan aus");
				p.sendMessage(ChatColor.YELLOW+"/clan promote <Spieler> "+ChatColor.GRAY+"Promotet einen Spieler");
				p.sendMessage(ChatColor.YELLOW+"/clan reduce <Spieler> "+ChatColor.GRAY+"Degradiert einen Spieler");
				p.sendMessage(ChatColor.YELLOW+"/clan list "+ChatColor.GRAY+"Zeigt alle Clanmitglieder an");
				p.sendMessage(ChatColor.YELLOW+"/clan rename "+ChatColor.GRAY+"Ändert den namen deinses Clans");
				p.sendMessage(ChatColor.YELLOW+"/clan remove <Spieler>"+ChatColor.GRAY+"Kickt einen Spieler aus deinen Clan");
				p.sendMessage(ChatColor.YELLOW+"/clan tinfo <Clantag>"+ChatColor.GRAY+"Zeigt die Infos eines Clanes an");
				p.sendMessage(ChatColor.GRAY+"========================");
			}else if(args[0].equals("create")){
				
				if(args.length == 3){
				
				String clanname = args[1];
				String clanshort = args[2];
				if((!MySQLRang.clanExists(clanname))&&(!MySQLRang.shortExists(clanname))){
					if(clanshort.length()<7){
						
						if(!Claninfo.playerExists(p.getUniqueId().toString())){
							
						MySQLRang.createClan(clanname, clanshort, p);
						Claninfo.setClanname(clanname, clanshort, p);
						p.sendMessage(Main.Prefix+ChatColor.GREEN+"Der Clan wurde erfolgreich erstellt!");
						
						}else{
							p.sendMessage(Main.Prefix+ChatColor.RED+"Du bist bereits in einem anderen Clan");
						}
					}else{
						p.sendMessage(Main.Prefix+ChatColor.RED+"Das Clankürzel darf maximal 6 Zeichen lang sein!");
					}
					
				}else{
					p.sendMessage(Main.Prefix+ChatColor.RED+"Diesen Clan gibt es bereits!");	
				}
				}else{
					p.sendMessage(Main.Prefix+ChatColor.RED+"Nutze: /clan create <Clanname> <Clankürzel>!");	
				}
				
			}else if(args[0].equals("delete")){
				
				if(args.length == 1){
				if(Claninfo.playerExists(p.getUniqueId().toString())){
					String clanname = Claninfo.getClanname(p.getUniqueId().toString());
					@SuppressWarnings("unchecked")
					List<String> list = MySQLRang.getClanleader(clanname);
					if(list.contains(p.getUniqueId().toString())){
					Claninfo.delClan(clanname);
					MySQLRang.delClan(clanname);
					p.sendMessage(Main.Prefix+ChatColor.GREEN+"Dein Clan wurde erfolgreich gelöscht!");
					
					}else{
						p.sendMessage(Main.Prefix+ChatColor.RED+"Du bist nicht Clanleader!");
					}
					
				}else{
					p.sendMessage(Main.Prefix+ChatColor.RED+"Du bist in keinem Clan!");
				}
				
				}else{
					p.sendMessage(Main.Prefix+ChatColor.RED+"Nutze: /clan delete!");	
				}
				
			}else if(args[0].equals("invite")){
				
				if(args.length==2){
				
				if(Claninfo.playerExists(p.getUniqueId().toString())){
					String clanname = Claninfo.getClanname(p.getUniqueId().toString());
					@SuppressWarnings("unchecked")
					List<String> list = MySQLRang.getClanleader(clanname);
					@SuppressWarnings("unchecked")
					List<String> list2 = MySQLRang.getClanmod(clanname);
					if(list.contains(p.getUniqueId().toString())||list2.contains(p.getUniqueId().toString())){
					if(!invites.containsKey(args[1])){
						if(BungeeCord.getInstance().getPlayer(args[1])!=null){
							ProxiedPlayer pp = BungeeCord.getInstance().getPlayer(args[1]);
							if(!Claninfo.playerExists(pp.getUniqueId().toString())){
						invites.put(args[1], clanname);
						p.sendMessage(Main.Prefix+ChatColor.GREEN+"Du hast den Spieler erfolgreich eingeladen!");
						pp.sendMessage(Main.Prefix+p.getName()+ChatColor.GRAY+" läd dich in seinen Clan ein!");
						
						TextComponent message = new TextComponent(Main.Prefix+"Du hast die Optionen: ");
						TextComponent accept = new TextComponent("[Akzeptieren]");
						accept.setColor(ChatColor.GREEN);
						accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clan accept "+clanname));
						accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GREEN+"Nehme die Clananfrage an").create()));
						message.addExtra(accept);
						TextComponent free = new TextComponent(" ");
						message.addExtra(free);
						TextComponent deny = new TextComponent("[Ablehnen]");
						deny.setColor(ChatColor.RED);
						deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clan deny "+clanname));
						deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.RED+"Lehnt die Clananfrage ab").create()));
						message.addExtra(deny);
						pp.sendMessage(message);
							}else{
								p.sendMessage(Main.Prefix+ChatColor.RED+"Dieser Spieler ist bereits in einem andren Clan!");
							}
						}else{
							p.sendMessage(Main.Prefix+ChatColor.RED+"Dieser Spieler ist nicht Online!");
						}
					}else{
						p.sendMessage(Main.Prefix+ChatColor.RED+"Du hast diesem Spieler bereits eine Einladung geschickt!");
					}
					}else{
						p.sendMessage(Main.Prefix+ChatColor.RED+"Du bist nicht Clanleader oder Clanmoderator!");
					}
					
				}else{
					p.sendMessage(Main.Prefix+ChatColor.RED+"Du bist in keinem Clan!");
				}
				
				}else{
					p.sendMessage(ChatColor.RED+"Nutze: /clan invite <Spieler>!");
				}
				
			}else if(args[0].equals("accept")){
				
				String clanname = args[1];
				if(invites.containsKey(p.getName())){
					if(invites.get(p.getName()).equals(clanname)){
						
						if(!Claninfo.playerExists(p.getUniqueId().toString())){
						
						MySQLRang.addmember(clanname, p.getUniqueId().toString());
						Claninfo.setClanname(clanname, MySQLRang.getShort(clanname), p);
						p.sendMessage(Main.Prefix+ChatColor.GREEN+"Du bist dem Clan erfolgreich beigetreten!");
						invites.remove(p.getName());
						List<String> l = MySQLRang.getmember(clanname);
						for(String ss : l){
							if(BungeeCord.getInstance().getPlayer(SpielerUUID.getSpielername(ss))!=null){
								BungeeCord.getInstance().getPlayer(SpielerUUID.getSpielername(ss)).sendMessage(Main.Prefix+ChatColor.YELLOW+p.getName()+ChatColor.GREEN+" ist dem Clan beigetreten!");
							}
						}
						
						
						}else{
							p.sendMessage(Main.Prefix+ChatColor.RED+"Du bist noch in einem anderen Clan");
						}
						
					}else{
						p.sendMessage(Main.Prefix+ChatColor.RED+"Du hast keine Einladung dieses Clans");
					}
				}else{
					p.sendMessage(Main.Prefix+ChatColor.RED+"Du hast keine Einladung dieses Clans");
				}
				
			}else if(args[0].equals("deny")){
				
				if(invites.containsKey(p.getName())){
					invites.remove(p.getName());
					p.sendMessage(Main.Prefix+ChatColor.GREEN+"Du hast erfolgreich die Clananfrage abgelehnt!");
				}else{
					p.sendMessage(Main.Prefix+ChatColor.RED+"Du hast keine Clananfrage bekommen!");
				}
			}else if(args[0].equals("leave")){
				if(args.length == 1){
//				========================================================================
				if(Claninfo.playerExists(p.getUniqueId().toString())){
				String clanname = Claninfo.getClanname(p.getUniqueId().toString());
				@SuppressWarnings("unchecked")
				List<String> list = MySQLRang.getClanleader(clanname);
				@SuppressWarnings("unchecked")
				List<String> list2 = MySQLRang.getClanmod(clanname);
				if(!list.contains(p.getUniqueId().toString())){
				
				Claninfo.delPlayer(p.getUniqueId().toString());
					if(list2.contains(p.getUniqueId().toString())){
						MySQLRang.delmod(clanname, p.getUniqueId().toString());
					}else{
						MySQLRang.delmember(clanname, p.getUniqueId().toString());
					}
					
					List<String> l = MySQLRang.getmember(clanname);
					for(String ss : l){
						if(BungeeCord.getInstance().getPlayer(SpielerUUID.getSpielername(ss))!=null){
							BungeeCord.getInstance().getPlayer(SpielerUUID.getSpielername(ss)).sendMessage(Main.Prefix+ChatColor.YELLOW+p.getName()+ChatColor.RED+" ist dem Clan ausgetreten!");
						}
					}
				p.sendMessage(Main.Prefix+ChatColor.GREEN+"Du bist dem Clan erfolgreich ausgetreten!");	
					
				}else{
					p.sendMessage(Main.Prefix+ChatColor.RED+"Du bist Clanleader! Du musst den Clan löschen um auszutreten");
				}
				
			}else{
				p.sendMessage(Main.Prefix+ChatColor.RED+"Du bist in keinem Clan!");
			}
				
				}else{
					p.sendMessage(Main.Prefix+ChatColor.RED+"Nutze: /clan leave!");
				}
//				========================================================================
			}else if(args[0].equals("remove")){
				if(args.length == 2){
//				========================================================================
				if(Claninfo.playerExists(p.getUniqueId().toString())){
				String clanname = Claninfo.getClanname(p.getUniqueId().toString());
				@SuppressWarnings("unchecked")
				List<String> list = MySQLRang.getClanleader(clanname);
				@SuppressWarnings("unchecked")
				List<String> list2 = MySQLRang.getClanmod(clanname);
				@SuppressWarnings("unchecked")
				List<String> list3 = MySQLRang.getClanmember(clanname);
				if(list.contains(p.getUniqueId().toString())){
				String kickplayer = SpielerUUID.getUUID(args[1]);
				if(list.contains(SpielerUUID.getUUID(args[1]))) {
					p.sendMessage(Main.Prefix+ChatColor.GREEN+"Du darfst keinen Leader removen!");	
					return;
				}
				
				if(Claninfo.playerExists(kickplayer)&&(list.contains(kickplayer)||list2.contains(kickplayer)||list3.contains(kickplayer))){
				Claninfo.delPlayer(kickplayer);
					if(list2.contains(kickplayer)){
						MySQLRang.delmod(clanname, kickplayer);
					}else{
						MySQLRang.delmember(clanname, kickplayer);
					}
					
					List<String> l = MySQLRang.getmember(clanname);
					for(String ss : l){
						if(BungeeCord.getInstance().getPlayer(SpielerUUID.getSpielername(ss))!=null){
							BungeeCord.getInstance().getPlayer(SpielerUUID.getSpielername(ss)).sendMessage(Main.Prefix+ChatColor.YELLOW+args[1]+ChatColor.RED+" ist dem Clan ausgetreten!");
						}
					}
				p.sendMessage(Main.Prefix+ChatColor.GREEN+"Du hast den Spieler erfolgreich gekickt!");	
				}else{
					p.sendMessage(Main.Prefix+ChatColor.RED+"Dieser Spieler ist nicht im Clan");
				}
				}else{
					p.sendMessage(Main.Prefix+ChatColor.RED+"Du bist nicht Clanleader!");
				}
				
			}else{
				p.sendMessage(Main.Prefix+ChatColor.RED+"Du bist in keinem Clan!");
			}
				
				}else{
					p.sendMessage(Main.Prefix+ChatColor.RED+"Nutze: /clan remove <Spieler>!");
				}
//				========================================================================
				
			}else if(args[0].equals("promote")){
				if(args.length == 2){
//				==================================================
				if(Claninfo.playerExists(p.getUniqueId().toString())){
				String clanname = Claninfo.getClanname(p.getUniqueId().toString());
				@SuppressWarnings("unchecked")
				List<String> list = MySQLRang.getClanleader(clanname);
				@SuppressWarnings("unchecked")
				List<String> list2 = MySQLRang.getClanmod(clanname);
				@SuppressWarnings("unchecked")
				List<String> list3 = MySQLRang.getClanmember(clanname);
				if(list.contains(p.getUniqueId().toString())){
				
					String uuid = SpielerUUID.getUUIDaboutid(args[1].toUpperCase());
					if(list3.contains(uuid)){
						MySQLRang.delmember(clanname, uuid);
						MySQLRang.addMod(clanname, uuid);
						p.sendMessage(ChatColor.GREEN+"Du hast den Spieler erfolgreich Promotet! Der Spieler ist nun "+ChatColor.YELLOW+"Mod");	
					}else if(list2.contains(uuid)){
						MySQLRang.delmod(clanname, uuid);
						MySQLRang.addLeader(clanname, uuid);
						p.sendMessage(ChatColor.GREEN+"Du hast den Spieler erfolgreich Promotet! Der Spieler ist nun "+ChatColor.YELLOW+"Leader");	
					}else if(list.contains(uuid)){
						if(list.size()>1){
						MySQLRang.delleader(clanname, uuid);
						MySQLRang.addmember(clanname, uuid);
						p.sendMessage(ChatColor.GREEN+"Du hast den Spieler erfolgreich Promotet! Der Spieler ist nun "+ChatColor.YELLOW+"Member");
						}else{
							p.sendMessage(ChatColor.RED+"Es muss mindestens 2 Leader geben damit du dich zum Member machen kannst");	
						}
					}
					
				}else{
					p.sendMessage(Main.Prefix+ChatColor.RED+"Du bist nicht Clanleader!");
				}
				
			}else{
				p.sendMessage(Main.Prefix+ChatColor.RED+"Du bist in keinem Clan!");
			}
				}else{
					p.sendMessage(Main.Prefix+ChatColor.RED+"Nutze: /clan promote <Spieler>!");
				}
//				===================================================
				
			}else if(args[0].equals("reduce")){
				if(args.length == 2){
//				==================================================
				if(Claninfo.playerExists(p.getUniqueId().toString())){
				String clanname = Claninfo.getClanname(p.getUniqueId().toString());
				@SuppressWarnings("unchecked")
				List<String> list = MySQLRang.getClanleader(clanname);
				@SuppressWarnings("unchecked")
				List<String> list2 = MySQLRang.getClanmod(clanname);
				if(list.contains(p.getUniqueId().toString())){
				
					String uuid = SpielerUUID.getUUIDaboutid(args[1].toUpperCase());
					if(list.contains(uuid)) {
						if(list.size()>1) {
							MySQLRang.delleader(clanname, uuid);
							MySQLRang.addMod(clanname, uuid);
							p.sendMessage(Main.Prefix+ChatColor.GREEN+"Du hast den Spieler erfolgreich zum Moderator gemacht!");
						}else {
							p.sendMessage(Main.Prefix+ChatColor.RED+"Es muss mindestens 2 Leader geben damit du dich zum Moderator machen kannst");
						}
					}else if(list2.contains(uuid)) {
						MySQLRang.delmod(clanname, uuid);
						MySQLRang.addmember(clanname, uuid);
						p.sendMessage(Main.Prefix+ChatColor.GREEN+"Du hast den Spieler erfolgreich zum Spieler gemacht!");
					}else {
						p.sendMessage(Main.Prefix+ChatColor.RED+"Du kannst diesen Spieler nicht weiter downgraden!");
					}	
					
				}else{
					p.sendMessage(Main.Prefix+ChatColor.RED+"Du bist nicht Clanleader!");
				}
				
			}else{
				p.sendMessage(Main.Prefix+ChatColor.RED+"Du bist in keinem Clan!");
			}
				}else{
					p.sendMessage(Main.Prefix+ChatColor.RED+"Nutze: /clan reduce <Spieler>!");
				}
//				===================================================
				
				
			}else if(args[0].equals("list")){
				if(args.length == 1){
					if(Claninfo.playerExists(p.getUniqueId().toString())){
						String clanname = Claninfo.getClanname(p.getUniqueId().toString());
						@SuppressWarnings("unchecked")
						List<String> list = MySQLRang.getClanleader(clanname);
						@SuppressWarnings("unchecked")
						List<String> list2 = MySQLRang.getClanmod(clanname);
						@SuppressWarnings("unchecked")
						List<String> list3 = MySQLRang.getClanmember(clanname);
					p.sendMessage(ChatColor.DARK_GRAY+"================================");
					p.sendMessage(ChatColor.GRAY+"Name: "+ChatColor.YELLOW+clanname);
					p.sendMessage(ChatColor.GRAY+"Kürzel: "+ChatColor.YELLOW+Claninfo.getshort(p.getUniqueId().toString()));
					p.sendMessage(ChatColor.DARK_GRAY+"================================");
					p.sendMessage(ChatColor.GRAY+"Clanleader:");
					for(String ss : list){
						p.sendMessage(ChatColor.DARK_AQUA+getUsername(ss));
					}
					p.sendMessage(ChatColor.GRAY+"Clanmoderatoren:");
						for(String ss : list2){
							p.sendMessage(ChatColor.AQUA+getUsername(ss));
						}
					p.sendMessage(ChatColor.GRAY+"Clanmember:");
						for(String ss : list3){
							p.sendMessage(ChatColor.GREEN+getUsername(ss));
						}
					p.sendMessage(ChatColor.DARK_GRAY+"================================");
						
						
					}else{
						p.sendMessage(Main.Prefix+ChatColor.RED+"Du bist in keinem Clan!");
					}
				}else{
					p.sendMessage(Main.Prefix+ChatColor.RED+"Nutze: /clan list");
				}
				
//				======================================================================================
			}else if(args[0].equals("tinfo")){
				if(args.length == 2){
					if(MySQLRang.shortExists(args[1])) {
						String clanname = MySQLRang.getClanname(args[1]);
						@SuppressWarnings("unchecked")
						List<String> list = MySQLRang.getClanleader(clanname);
						@SuppressWarnings("unchecked")
						List<String> list2 = MySQLRang.getClanmod(clanname);
						@SuppressWarnings("unchecked")
						List<String> list3 = MySQLRang.getClanmember(clanname);
					p.sendMessage(ChatColor.DARK_GRAY+"================================");
					p.sendMessage(ChatColor.GRAY+"Name: "+ChatColor.YELLOW+clanname);
					p.sendMessage(ChatColor.GRAY+"Kürzel: "+ChatColor.YELLOW+args[1]);
					p.sendMessage(ChatColor.DARK_GRAY+"================================");
					p.sendMessage(ChatColor.GRAY+"Clanleader:");
					for(String ss : list){
						p.sendMessage(ChatColor.DARK_AQUA+getUsername(ss));
					}
					p.sendMessage(ChatColor.GRAY+"Clanmoderatoren:");
						for(String ss : list2){
							p.sendMessage(ChatColor.AQUA+getUsername(ss));
						}
					p.sendMessage(ChatColor.GRAY+"Clanmember:");
						for(String ss : list3){
							p.sendMessage(ChatColor.GREEN+getUsername(ss));
						}
					p.sendMessage(ChatColor.DARK_GRAY+"================================");
						
						
					}else{
						p.sendMessage(Main.Prefix+ChatColor.RED+"Diesen Clan gibt es nicht!");
					}
				}else{
					p.sendMessage(Main.Prefix+ChatColor.RED+"Nutze: /clan tinfo <Clantag>");
				}
				
//				======================================================================================
			}else if(args[0].equals("rename")){
				if(args.length == 3){
					
					if(Claninfo.playerExists(p.getUniqueId().toString())){
					String clanoldname = Claninfo.getClanname(p.getUniqueId().toString());
					String clanoldshort = Claninfo.getshort(p.getUniqueId().toString());
					String clanname = args[1];
					String clanshort = args[2];
					
					if((!MySQLRang.clanExists(clanname)||clanname.equals(clanoldname))&&(!MySQLRang.shortExists(clanname)||clanshort.equals(clanoldshort))){
						if(clanshort.length()<7){
							
							
							@SuppressWarnings("unchecked")
							List<String> list = MySQLRang.getClanleader(clanname);
							if(list.contains(p.getUniqueId().toString())){
							
								Claninfo.setClanname1(clanname, clanshort);
								Claninfo.setClanshort1(clanname, clanshort);
								MySQLRang.setClanname(clanname, clanshort);
								MySQLRang.setClanshort(clanname, clanshort);
								p.sendMessage(Main.Prefix+ChatColor.GREEN+"Der Name wurde erfolgreich geendert");
							
							}else{
								p.sendMessage(Main.Prefix+ChatColor.RED+"Du bist nicht Clanleader!");
							}
							
						}else{
							p.sendMessage(Main.Prefix+ChatColor.RED+"Das Clankürzel darf maximal 6 Zeichen lang sein!");
						}
						
					}else{
						p.sendMessage(Main.Prefix+ChatColor.RED+"Diesen Clan gibt es bereits!");	
					}
					}else{
						p.sendMessage(Main.Prefix+ChatColor.RED+"Du bist in keinem Clan");
					}
				}else{
					p.sendMessage(Main.Prefix+ChatColor.RED+"Nutze: /clan rename <Clanname> <Clankürzel>");
				}
//				======================================================================================
				
			}else{
				p.sendMessage(ChatColor.RED+"Diesen Command gibt es nicht! Nutze /clan help um alle befehle zu sehen");
			}
			
		}else{
			p.sendMessage(ChatColor.RED+"Diesen Command gibt es nicht! Nutze /clan help um alle befehle zu sehen");
		}
		
	}
	
	//Playername, clanname
	public static HashMap<String, String> invites = new HashMap<>();

	public static String getUsername(String uuid){
    	
	return SpielerUUID.getSpielername(uuid);
		
   	}
	
}
