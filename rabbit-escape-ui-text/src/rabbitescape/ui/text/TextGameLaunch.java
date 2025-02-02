package rabbitescape.ui.text;

import static rabbitescape.engine.i18n.Translation.*;
import static rabbitescape.engine.util.Util.*;

import rabbitescape.engine.LevelWinListener;
import rabbitescape.engine.World;
import rabbitescape.engine.World.CompletionState;
import rabbitescape.engine.textworld.TextWorldManip;
import rabbitescape.render.GameLaunch;

public class TextGameLaunch implements GameLaunch
{
    private final World world;
    private final LevelWinListener winListener;
    private final Terminal terminal;

    public TextGameLaunch(
        World world, LevelWinListener winListener, Terminal terminal )
    {
        this.world = world;
        this.winListener = winListener;
        this.terminal = terminal;
    }

    @Override
    public void run( String[] args )
    {
        boolean useInput = false;
        if ( args.length > 1 && args[1].equals( "--interactive" ) )
        {
            useInput = true;
        }

        while( world.completionState() == CompletionState.RUNNING )
        {
            try
            {
                if ( !useInput )
                {
                    printWorldWithState();
                    Thread.sleep( 200 );
                }

                printWorld();

                if ( useInput )
                {
                    InputHandler inputHandler =
                        new InputHandler( world, terminal );

                    //noinspection StatementWithEmptyBody
                    while ( !inputHandler.handle() )
                    {
                    }
                }
                else
                {
                    Thread.sleep( 200 );
                }
            }
            catch ( InterruptedException e )
            {
                e.printStackTrace();
            }

            world.step();
            checkWon();
        }
    }

    private void checkWon()
    {
        if ( world.completionState() == CompletionState.WON )
        {
            winListener.won();
        }
    }

    private void printWorld()
    {
        printWorldImpl( false );
    }

    private void printWorldWithState()
    {
        printWorldImpl( true );
    }

    private void printWorldImpl( boolean showChanges )
    {
        String[] txt = TextWorldManip.renderWorld( world, showChanges, true );

        terminal.out.println( join( "\n", txt ) );
    }

    @Override
    public void showResult()
    {
        if ( world.completionState() == CompletionState.WON )
        {
            terminal.out.println( t( "You won!" ) );
        }
        else
        {
            terminal.out.println( t( "You lost." ) );
        }
    }
}
