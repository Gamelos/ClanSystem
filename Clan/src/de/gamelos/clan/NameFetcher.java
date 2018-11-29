package de.gamelos.clan;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class NameFetcher 
{
	private static final JSONParser jsonParser = new JSONParser();
	private static final String REQUEST_URL = "http://mcapi.sweetcode.de/api/v2/?uuid&request=";
	private static final String PROFILE_URL = "https://sessionserver.mojang.com/session/minecraft/profile/";
	
	public static String getRequest(UUID uuid)
	{
		try 
		{
			HttpURLConnection connection = (HttpURLConnection) new URL(REQUEST_URL + fromUniqueId(uuid)).openConnection();
			JSONObject response = (JSONObject) jsonParser.parse(new InputStreamReader(connection.getInputStream()));	
			
			String uniqueId = (String) response.get("uuid");
			if (uniqueId.length() == 0) 
			{
				throw new IllegalArgumentException("A Username for UUID '" + uuid.toString() + "' was not found in the Mojang database! Is the account not premium?");
			}
			
			return (String) response.get("username");
		}
		catch (Exception e)
		{
			return callMojang(uuid);
		}
	}
	
	private static String callMojang(UUID uuid)
	{
		try 
		{
			HttpURLConnection connection = (HttpURLConnection) new URL(PROFILE_URL + fromUniqueId(uuid)).openConnection();
			JSONObject response = (JSONObject) jsonParser.parse(new InputStreamReader(connection.getInputStream()));	
			
			String name = (String) response.get("name");
			if (name == null) 
			{
				throw new IllegalArgumentException("A Username for UUID '" + uuid.toString() + "' was not found in the Mojang database! Is the account not premium?");
			}

			String cause = (String) response.get("cause");
			String errorMessage = (String) response.get("errorMessage");
			if (cause != null && cause.length() > 0)
			{
				throw new IllegalStateException(errorMessage);
			}
			return name;
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException("A Username for UUID '" + uuid.toString() + "' was not found in the Mojang database! Is the account not premium?");
		}
	}
	
	private static String fromUniqueId(UUID uuid)
	{
		return uuid.toString().replaceAll("-", "");
	}
}