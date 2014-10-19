package rabbitescape.engine.menu;

import static rabbitescape.engine.config.ConfigKeys.*;

import java.util.Map;

import rabbitescape.engine.config.ConfigTools;
import rabbitescape.engine.config.IConfig;

public class ConfigBasedLevelsCompleted implements LevelsCompleted
{
    private final IConfig config;

    public ConfigBasedLevelsCompleted( IConfig config )
    {
        this.config = config;
    }

    @Override
    public int highestLevelCompleted( String levelsDir )
    {
        Map<String, Integer> completed = ConfigTools.getMap(
            config, CFG_LEVELS_COMPLETED, Integer.class );

        Integer ret = completed.get( levelsDir );

        if ( ret == null )
        {
            return 0;
        }
        else
        {
            return ret;
        }
    }

    @Override
    public void setCompletedLevel( String levelsDir, int levelNum )
    {
        Map<String, Integer> completed = ConfigTools.getMap(
            config, CFG_LEVELS_COMPLETED, Integer.class );

        completed.put( levelsDir, levelNum );

        ConfigTools.setMap( config, CFG_LEVELS_COMPLETED, completed );
        config.save();
    }
}
