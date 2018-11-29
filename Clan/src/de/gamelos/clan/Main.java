package de.gamelos.clan;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class Main extends Plugin implements Listener{
	
	
	public static MySQL mysql;
	public static String Prefix = ChatColor.DARK_GRAY+"["+ChatColor.YELLOW+"Clan"+ChatColor.DARK_GRAY+"] "+ChatColor.GRAY;
	
	public void onEnable(){
		System.out.println("[BungeeFriends] Das Plugin wurde geladen");
		BungeeCord.getInstance().getPluginManager().registerCommand(this, new clan("clan"));
		ProxyServer.getInstance().getPluginManager().registerListener(this, this);
		ConnectMySQL();
	}
	
	private void ConnectMySQL(){
		mysql = new MySQL(de.gamelos.system.Main.gethost(), de.gamelos.system.Main.getuser(), de.gamelos.system.Main.getdatabase(), de.gamelos.system.Main.getpassword());
		mysql.update("CREATE TABLE IF NOT EXISTS Clan(Clanname varchar(64), Clanshort varchar(1000), Clanleader varchar(1000), Clanmod varchar(1000), Clanmember varchar(1000));");
		mysql.update("CREATE TABLE IF NOT EXISTS Claninfo(UUID varchar(64),Clanname varchar(64), Clanshort varchar(1000));");
	}
	
	@Override
	public void onDisable(){
		
		System.out.println("[BungeeFriends] Das Plugin wurde deaktiviert");
		
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onmsg(PluginMessageEvent e){
		if(!e.getTag().equalsIgnoreCase("BungeeCord"))
			return;
		DataInputStream stream = new DataInputStream(new ByteArrayInputStream(e.getData()));
		
		try {
			String channel = stream.readUTF();
			if(channel.equals("data")){
				String input = stream.readUTF();
				ProxiedPlayer p = ((ProxiedPlayer)e.getReceiver());
				if(input.contains("clan invite ")){
					String player = input.split(" ")[2];
//					=================================================================
					if(Claninfo.playerExists(p.getUniqueId().toString())){
						String clanname = Claninfo.getClanname(p.getUniqueId().toString());
						@SuppressWarnings("unchecked")
						List<String> list = MySQLRang.getClanleader(clanname);
						@SuppressWarnings("unchecked")
						List<String> list2 = MySQLRang.getClanmod(clanname);
						if(list.contains(p.getUniqueId().toString())||list2.contains(p.getUniqueId().toString())){
						
							if(BungeeCord.getInstance().getPlayer(player)!=null){
								ProxiedPlayer pp = BungeeCord.getInstance().getPlayer(player);
								if(!Claninfo.playerExists(pp.getUniqueId().toString())){
							clan.invites.put(player, clanname);
							p.sendMessage(ChatColor.GREEN+"Du hast den Spieler erfolgreich eingeladen!");
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
									p.sendMessage(ChatColor.RED+"Dieser Spieler ist beriets in einem andren Clan!");
								}
							}else{
								p.sendMessage(ChatColor.RED+"Dieser Spieler ist nicht Online!");
							}
						}else{
							p.sendMessage(ChatColor.RED+"Du bist nicht Clanleader oder Clanmoderator!");
						}
						
					}else{
						p.sendMessage(ChatColor.RED+"Du bist in keinem Clan!");
					}
//					=================================================================
				}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
