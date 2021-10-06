package ignoreplayersoverdiscordrelaismod;

import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modloader.interfaces.Configurable;
import org.gotti.wurmunlimited.modloader.interfaces.Initable;
import org.gotti.wurmunlimited.modloader.interfaces.WurmClientMod;

import javassist.ClassPool;
import javassist.CtClass;


public class IgnorePlayersOverDiscordRelaisMod implements WurmClientMod, Initable, Configurable
{
	private static Logger LOGGER = Logger.getLogger( IgnorePlayersOverDiscordRelaisMod.class.getName() );
	public static String[] IGNOREDPLAYERS;
	
	
	
	
	@Override
	public void configure( Properties pProperties ) 
	{
		IGNOREDPLAYERS = pProperties.getProperty( "players" ).split( ";" );
		LOGGER.log( Level.INFO, "Config IgnorePlayersOverDiscordRelaisMod loaded" );
	}
	
	@Override
    public void init() 
    {
        try 
        {
            ClassPool lClassPool = HookManager.getInstance().getClassPool();
	        
	        CtClass lCtWurmChat = lClassPool.getCtClass( "com.wurmonline.client.renderer.gui.ChatPanelComponent" );
	        lCtWurmChat.getMethod( "addText", "(Ljava/lang/String;Ljava/lang/String;FFFZ)V" ).insertBefore( 
	        "if ( ignoreplayersoverdiscordrelaismod.IgnorePlayersOverDiscordRelaisMod.handleMessage($1,$2) ) return;"
	        );
        }
        catch( Throwable e )
        {
        	LOGGER.log( Level.SEVERE, "Error loading IgnorePlayersOverDiscordRelaisMod", e.getMessage() );
        }
    }
	
	public static boolean handleMessage( String lTabName, String lMessage ) throws InterruptedException 
    {
		LOGGER.log( Level.INFO, "TEST LOG" );
		if ( lMessage.contains( "<" ) && lMessage.contains( ">" ) )
		{
			LOGGER.log( Level.INFO, lMessage.substring( lMessage.indexOf( "<" ) + 1, lMessage.indexOf( ">" ) ) );
			if ( Arrays.asList( IGNOREDPLAYERS ).contains( lMessage.substring( lMessage.indexOf( "<" ) + 1, lMessage.indexOf( ">" ) ) ) )
			{
				return true;
			}			
		}
		return false;
    }
	
	public static String[] getMutedPlayers()
	{
		return IGNOREDPLAYERS;
	}
}
