package rabbitescape.render;

import java.util.HashMap;
import java.util.Map;

public class AnimationCache
{
    private final String[] names;
    private final Map<String, FrameNameAndOffset[]> animations;

    public AnimationCache( AnimationLoader animationLoader )
    {
        this.names = animationLoader.listAll();
        this.animations = new HashMap<>();

        for ( String name : names )
        {
            if ( !name.equals( AnimationLoader.NONE ) )
            {
                this.animations.put( name, animationLoader.load( name ) );
            }
        }
    }

    public String[] listAll()
    {
        return names;
    }

    public FrameNameAndOffset[] get( String animationName )
    {
        return animations.get( animationName );
    }
}
